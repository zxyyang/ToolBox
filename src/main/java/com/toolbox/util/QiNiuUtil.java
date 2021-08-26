package com.toolbox.util;

import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.BatchStatus;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.model.FetchRet;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import com.qiniu.util.UrlSafeBase64;
import com.toolbox.config.QiNiuConfig;
import com.toolbox.service.FileService;
import com.toolbox.valueobject.Files;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * 七牛上传下载工具类
 **/
@Slf4j
public class QiNiuUtil {

	@Autowired
	private FileService fileService;

	/***
	 * 配置类
	 */
	public static BucketManager configBucketManager() {
		Configuration cfg = new Configuration(QiNiuConfig.getInstance().getZone());
		// ...生成上传凭证，然后准备上传
		Auth auth = Auth.create(QiNiuConfig.getInstance().getAccessKey(), QiNiuConfig.getInstance().getSecretKey());
		BucketManager bucketManager = new BucketManager(auth, cfg);
		return bucketManager;
	}

	/**
	 * 上传本地文件
	 *
	 * @param localFilePath
	 *            本地文件完整路径
	 * @param key
	 *            文件云端存储的名称
	 * @param override
	 *            是否覆盖同名同位置文件
	 * @return
	 */
	public static boolean upload(String localFilePath, String key, boolean override) {
		// 构造一个带指定Zone对象的配置类
		Configuration cfg = new Configuration(QiNiuConfig.getInstance().getZone());
		// ...其他参数参考类注释
		UploadManager uploadManager = new UploadManager(cfg);
		// ...生成上传凭证，然后准备上传
		Auth auth = Auth.create(QiNiuConfig.getInstance().getAccessKey(), QiNiuConfig.getInstance().getSecretKey());
		String upToken;
		if (override) {
			upToken = auth.uploadToken(QiNiuConfig.getInstance().getBucket(), key);// 覆盖上传凭证
		} else {
			upToken = auth.uploadToken(QiNiuConfig.getInstance().getBucket());
		}
		try {
			Response response = uploadManager.put(localFilePath, key, upToken);
			// 解析上传成功的结果
			DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
			System.out.println(putRet.key);
			System.out.println(putRet.hash);
			return true;
		} catch (QiniuException ex) {
			Response r = ex.response;
			System.err.println(r.toString());
			try {
				System.err.println(r.bodyString());
			} catch (QiniuException ex2) {
				// ignore
				return false;
			}
			return false;
		}
	}

	/**
	 * 上传Base64图片
	 *
	 * @param l
	 *            图片没经过base64处理的原图字节大小，获取文件大小的时候，切记要通过文件流的方式获取。而不是通过图片标签然后转换后获取。
	 * @param file64
	 *            图片base64字符串
	 * @param key
	 *            文件云端存储的名称
	 * @param override
	 *            是否覆盖同名同位置文件
	 * @return
	 * @throws IOException
	 */
	public static boolean uploadBase64(int l, String file64, String key, boolean override) throws IOException {
		/*
		 * FileInputStream fis = null;
		 * int l = (int) (new File(localFilePath).length());
		 * byte[] src = new byte[l];
		 * try {
		 * fis = new FileInputStream(new File(localFilePath));
		 * fis.read(src);
		 * }catch (FileNotFoundException e){
		 * e.printStackTrace();
		 * log.error(e.getMessage());
		 * log.error("图片文件读取失败");
		 * return false;
		 * }
		 * String file64 = Base64.encodeToString(src, 0);
		 */

		Auth auth = getAuth();
		String upToken;
		if (override) {
			upToken = auth.uploadToken(QiNiuConfig.getInstance().getBucket(), key);// 覆盖上传凭证
		} else {
			upToken = auth.uploadToken(QiNiuConfig.getInstance().getBucket());
		}

		String url = "http://upload.qiniup.com/putb64/" + l + "/key/" + UrlSafeBase64.encodeToString(key);
		// 非华东空间需要根据注意事项 1 修改上传域名
		RequestBody rb = RequestBody.create(null, file64);
		Request request = new Request.Builder().url(url).addHeader("Content-Type", "application/octet-stream")
				.addHeader("Authorization", "UpToken " + upToken).post(rb).build();
		// System.out.println(request.headers());
		OkHttpClient client = new OkHttpClient();
		okhttp3.Response response = client.newCall(request).execute();
		// System.out.println(response);
		return response.isSuccessful();
	}

	/**
	 * 获取文件访问地址
	 *
	 * @param fileName
	 *            文件云端存储的名称
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String fileUrl(String fileName) throws UnsupportedEncodingException {
		String encodedFileName = URLEncoder.encode(fileName, "utf-8");
		String publicUrl = String.format("%s/%s", QiNiuConfig.getInstance().getDomainOfBucket(), encodedFileName);
		Auth auth = getAuth();
		long expireInSeconds = QiNiuConfig.getInstance().getExpireInSeconds();
		if (-1 == expireInSeconds) {
			return auth.privateDownloadUrl(publicUrl);
		}
		return auth.privateDownloadUrl(publicUrl, expireInSeconds);
	}

	/**
	 * 上传MultipartFile
	 *
	 * @param file
	 * @param key
	 * @param override
	 * @return
	 * @throws IOException
	 */
	public static boolean uploadMultipartFile(MultipartFile file, String key, boolean override) {
		// 构造一个带指定Zone对象的配置类
		Configuration cfg = new Configuration(QiNiuConfig.getInstance().getZone());
		// ...其他参数参考类注释
		UploadManager uploadManager = new UploadManager(cfg);

		// 把文件转化为字节数组
		InputStream is = null;
		ByteArrayOutputStream bos = null;

		try {
			is = file.getInputStream();
			bos = new ByteArrayOutputStream();
			byte[] b = new byte[1024];
			int len = -1;
			while ((len = is.read(b)) != -1) {
				bos.write(b, 0, len);
			}
			byte[] uploadBytes = bos.toByteArray();

			Auth auth = getAuth();
			String upToken;
			if (override) {
				upToken = auth.uploadToken(QiNiuConfig.getInstance().getBucket(), key);// 覆盖上传凭证
			} else {
				upToken = auth.uploadToken(QiNiuConfig.getInstance().getBucket());
			}
			// 默认上传接口回复对象
			DefaultPutRet putRet;
			// 进行上传操作，传入文件的字节数组，文件名，上传空间，得到回复对象
			Response response = uploadManager.put(uploadBytes, key, upToken);
			putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
			System.out.println(putRet.key);// key 文件名
			System.out.println(putRet.hash);// hash 七牛返回的文件存储的地址，可以使用这个地址加七牛给你提供的前缀访问到这个视频。
			System.out.println("上传成功");
			return true;
		} catch (QiniuException e) {
			e.printStackTrace();
			System.out.println("上传失败");
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("上传失败");
			return false;
		}
	}

	public static Auth getAuth() {
		Auth auth = Auth.create(QiNiuConfig.getInstance().getAccessKey(), QiNiuConfig.getInstance().getSecretKey());
		return auth;
	}

	/**
	 * * 下载到服务器
	 *
	 * @param targetUrl
	 */
	public static void downloadBase(String targetUrl) {
		// 获取downloadUrl
		String downloadUrl = getDownloadUrl(targetUrl);
		// 本地保存路径
		String filePath = "D:/";
		download(downloadUrl, filePath);
	}

	/**
	 * * 通过发送http get 请求获取文件资源
	 *
	 * @param url
	 * @param filepath
	 */
	private static void download(String url, String filepath) {
		OkHttpClient client = new OkHttpClient();
		Request req = new Request.Builder().url(url).build();
		okhttp3.Response resp = null;
		try {
			resp = client.newCall(req).execute();
			System.out.println(resp.isSuccessful());
			if (resp.isSuccessful()) {
				ResponseBody body = resp.body();
				InputStream is = ((ResponseBody) body).byteStream();
				byte[] data = readInputStream(is);
				File imgFile = new File(filepath + "download123.jpg"); // 下载到本地的图片命名
				FileOutputStream fops = new FileOutputStream(imgFile);
				fops.write(data);
				fops.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Unexpected code " + resp);
		}
	}

	/**
	 * * 调用浏览器下载
	 *
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static Resource downloadByIE(String fileName) throws IOException {
		String domain = QiNiuConfig.getInstance().getDomainOfBucket();
		// 封装下载链接
		String targetUrl = "http://" + domain + "/" + fileName;
		String downloadUrl = getDownloadUrl(targetUrl);
		Resource resource = new UrlResource(downloadUrl);
		if (resource != null) {
			return resource;
		} else {
			return null;
		}

	}

	/**
	 * * 读取字节输入流内容
	 *
	 * @param is
	 * @return
	 */
	private static byte[] readInputStream(InputStream is) {
		ByteArrayOutputStream writer = new ByteArrayOutputStream();
		byte[] buff = new byte[1024 * 2];
		int len = 0;
		try {
			while ((len = is.read(buff)) != -1) {
				writer.write(buff, 0, len);
			}
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return writer.toByteArray();
	}

	/***
	 * * 获取下载文件路径，即：donwloadUrl
	 *
	 * @param targetUrl
	 * @return
	 */
	public static String getDownloadUrl(String targetUrl) {
		String downloadUrl = getAuth().privateDownloadUrl(targetUrl);
		return downloadUrl;
	}

	/***
	 * 删除文件
	 */
	public static Boolean delete(String fileName) {
		BucketManager bucketManager = configBucketManager();
		try {
			bucketManager.delete(QiNiuConfig.getInstance().getBucket(), fileName);
			return true;
		} catch (QiniuException e) {
			System.out.println("删除失败" + e.response.toString());
			return false;
		}

	}

	/***
	 * * 文件目录
	 *
	 * @return
	 */
	public static List<Files> directory() {
		BucketManager bucketManager = configBucketManager();
		// 文件名前缀
		String prefix = "";
		// 每次迭代的长度限制，最大1000，推荐值 1000
		int limit = 1000;
		// 指定目录分隔符，列出所有公共前缀（模拟列出目录效果）。缺省值为空字符串
		String delimiter = "";
		// 列举空间文件列表
		BucketManager.FileListIterator fileListIterator = bucketManager.createFileListIterator(QiNiuConfig.getInstance().getBucket(), prefix, limit,
				delimiter);

		List<Files> filesList = new ArrayList<>();
		while (fileListIterator.hasNext()) {

			FileInfo[] items = fileListIterator.next();
			for (FileInfo item : items) {
				Files files = new Files();
				files.setKey(item.key);
				files.setFsize(item.fsize);
				files.setMimeType(item.mimeType);
				files.setPutTime(String.valueOf(item.putTime));
				files.setEndUser(item.endUser);
				filesList.add(files);
			}

		}

		return filesList;
	}

	/**
	 * 获取该目录下的文件信息集合
	 * 
	 * @param name
	 * @return
	 */

	public static List<Files> listByPath(String[] name) {
		BucketManager bucketManager = configBucketManager();

		try {
			BucketManager.BatchOperations batchOperations = new BucketManager.BatchOperations();
			batchOperations.addStatOps(QiNiuConfig.getInstance().getBucket(), name);
			Response response = bucketManager.batch(batchOperations);
			BatchStatus[] batchStatusList = response.jsonToObject(BatchStatus[].class);
			List<Files> filesList = new ArrayList<>();
			for (int i = 0; i < batchStatusList.length; i++) {
				Files files = new Files();
				if (batchStatusList[i].code == 200) {
					files.setKey(name[i]);
					files.setFsize(batchStatusList[i].data.fsize);
					files.setMimeType(batchStatusList[i].data.mimeType);
					files.setPutTime(String.valueOf(batchStatusList[i].data.putTime));
				} else {
					files.setKey(name[i] + "(文件云端获取失败)");
				}
				filesList.add(files);
			}
			return filesList;

		} catch (QiniuException e) {
			System.out.println(e.response.toString());
			return null;
		}

	}

	/***
	 * * 修改文件名
	 * * 源空间 目标空间 源文件名 目标文件名 描述
	 * * BucketA BucketA KeyA KeyB 相当于同空间文件重命名
	 * * BucketA BucketB KeyA KeyA 移动文件到BucketB，文件名一致
	 * * BucketA BucketB KeyA KeyB 移动文件到BucketB，文件名变成KeyB
	 *
	 * @param originName
	 * @param objectName
	 * @return
	 */

	public static Boolean reNameOrMove(String originName, String objectName) {

		BucketManager bucketManager = configBucketManager();
		try {
			bucketManager.move(QiNiuConfig.getInstance().getBucket(), originName, QiNiuConfig.getInstance().getBucket(), objectName);

		} catch (QiniuException e) {
			System.out.println(e.response.toString());
			return false;
		}
		return true;
	}

	/***
	 * 网络资源存储在云端
	 *
	 * @param KEY
	 * @param SrcURL
	 * @return
	 */
	public static String networkResources(String KEY, String SrcURL) {
		BucketManager bucketManager = configBucketManager();
		try {
			FetchRet fetchRet = bucketManager.fetch(SrcURL, QiNiuConfig.getInstance().getBucket(), KEY);
			// 修改存储名字，带后缀名
			String[] type = fetchRet.mimeType.split("/");
			String UrlFilename = fetchRet.key + "." + type[1];
			reNameOrMove(fetchRet.key, UrlFilename);
			return UrlFilename;

		} catch (QiniuException e) {
			System.out.println(e.response.toString());
			return null;
		}

	}

	// TODO

	/**
	 * * 批量删除
	 * * 需要添加随着删除内容进度条效果线程
	 * 
	 * @param fileNameList
	 * @return
	 */
	public static Map<String, String> batchDelete(String[] fileNameList) {
		BucketManager bucketManager = configBucketManager();
		Map<String, String> map = new HashMap<>();
		try {
			BucketManager.BatchOperations batchOperations = new BucketManager.BatchOperations();
			batchOperations.addDeleteOp(QiNiuConfig.getInstance().getBucket(), fileNameList);
			Response response = bucketManager.batch(batchOperations);
			BatchStatus[] batchStatuses = response.jsonToObject(BatchStatus[].class);

			for (int i = 0; i < fileNameList.length; i++) {
				BatchStatus status = batchStatuses[i];
				String key = fileNameList[i];
				if (status.code == 200) {
					System.out.println(key + "删除成功!");
					map.put(key, "succeed");
				} else {
					System.out.println(key + "删除失败!");
					map.put(key, "failed");
				}
			}

		} catch (QiniuException e) {
			System.out.println(e.response.toString());
		}
		return map;
	}
}