# ToolBox `个人工具库`-- 私有网盘
## 基础配置  
### resources>>application.yml文件进行MYsql 数据库的配置 和swagger的配置
```yml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://数据库服务器:3306/toolbox?serverTimezone=GMT%2B8&useSSL=true
    username: 账号
    password: 密码
    
 #是否开启swagger  
    mconfig:
  swagger-ui-open: ture 
```
## swagger
  ### 一个图形化的界面来做功能调试  
  > 通过下面的地址访问自己的后端调试（地址和端口都需要替换成自己的）
   * http://localhost:8081/swagger-ui.html#/
   * http://localhost:8081/doc.html  
   ### 使用方法
   #### 在需要添加调试的controller class上面添加注释@API（）
   #### 在需要添加调试的方法上面添加注释 @ApiOeration()
   ```java
   @RestController()
@Api(value = "/file", tags = { "上传文件接口" })
@RequestMapping("/localFile")
public class LocalFileUploadController {

	@Autowired
	FileStorageService fileStorageService;

	@ApiOperation(value = "/upload", notes = "上传文件", httpMethod = "POST")
	@PostMapping("/upload")
	public RequestBean<String> upload(@RequestParam("file") MultipartFile file) {
		try {
			fileStorageService.save(file);
			return RequestBean.Success();
		} catch (Exception e) {
			return RequestBean.Error();
		}
   ```

> # 一、私有网盘功能
## 1.文件的存储与上传
---------------------------------
数据的存储，采用的是七牛云的对象存储，可以去七牛云官方进行注册，免费使用
* 登录后通过右上角头像，找到密钥管理
(Access/Secret Key) 
* 通过产品管理中的对象存储，来创建自己的存储空间（bucket）
* 选择自己的空间位置 
[{'zone0':'华东'}, {'zone1':'华北'},{'zone2':'华南'},{'zoneNa0':'北美'},{'zoneAs0':''}]
* 如果自己没有域名就使用七牛提供得默认域名（默认域名是有时间限制的，自己配置的域名需要进行对应的域名解析）（domain-of-bucket）
* 将这些数据记录下来在 ressources>>qiniu.properties中进行对应信息的配置  
```yml
# 七牛云配置
qiniu.access-key=fZbGmSOmOJjhD3870jITywJ80SJNzVm9h8Nkxcuh
qiniu.secret-key=_sd8eK1TTcnIfUvzYizDMO1b0Z22-sTbVZgdI0P0
qiniu.bucket=toolbox-zxyang
# [{'zone0':'华东'}, {'zone1':'华北'},{'zone2':'华南'},{'zoneNa0':'北美'},{'zoneAs0':''}]
qiniu.zone=zone0
qiniu.domain-of-bucket=qiniu.zxyang.cn
# 链接过期时间，单位是秒，3600代表1小时，-1代表永不过期
qiniu.expire-in-seconds=-1
```  
---------------------------------
## 网盘的功能点介绍  
* ### 1.文件的上传  
由于对象存储没有目录结构，所以一方面我们在调用qiniu接口上传接口的时候还需要在自己本地的数据库进行有关于文件目录的存储，对于访问云端的key 和 存储的目录path进行关系存储

id | file_name |  file_path  
-|-|-
1 | 1.png | / |
2 | 2.jpeg | /文件夹1/ |
3 | 3.md | /文件夹2/文件夹43/ |  

* ### 2.文件下载  
> 1.采用返回http请求直接调用浏览器下载  
```java
    @ApiOperation(value = "/downLoadByIe", notes = "调用浏览器下载文件", httpMethod = "GET")
    @GetMapping("/downLoadByIe")
    public ResponseEntity<Resource> downLoadByIe(String fileName) throws IOException {
        Resource file = QiNiuUtil.downloadByIE(fileName);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + URLEncoder.encode(fileName, "UTF-8") + "\"")
                .body(file);
    }
```
> 2.返回下载url地址前端处理下载请求  
```java
    @ApiOperation(value = "/downLoad", notes = "url下载", httpMethod = "GET")
    @GetMapping("/downLoad")
    public RequestBean<down_ret> downLoad(String fileName) throws IOException {

        return RequestBean.Success(QiNiuUtil.downloadByUrl(fileName));
    }
```  
> 3.直接将文件存储在服务器，不返回前端，相当于对云端数据做一个持久化保存  

* ### 3.文件目录和详细信息  ` //首先从数据库查询数据，然后将查出的name也就是key列表从云端查数据 `
```java 
    @ApiOperation(value = "/listByPath", notes = "文件目录", httpMethod = "GET")
    @GetMapping("/listByPath")
    public RequestBean<List<Files>> listByPath(String path) throws JsonProcessingException {
          
        return RequestBean.Success(QiNiuUtil.listByPath(fileService.selectName(path)));
    }
```  
> 将云端没有正确读取到的但是数据库存在的数据进行标注  
```java
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
                    files.setName(name[i]);
                    files.setSize(batchStatusList[i].data.fsize);
                    files.setMimeType(batchStatusList[i].data.mimeType);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
                    long signtime = ((batchStatusList[i].data.putTime)) / 10000;
                    String lastSignTime = dateFormat.format(new Date(signtime));
                    files.setPutTime(lastSignTime);
                } else {
                    if (name[i].endsWith("/")) {
                        files.setName(name[i]);
                    } else {

                        files.setName(name[i] + "(文件云端获取失败)");
                    }
                }
                filesList.add(files);
            }
            return filesList;

        } catch (QiniuException e) {
            System.out.println(e.response.toString());
            return null;
        }

    }
```  
* ### 4.创建目录  
对象存储没有目录这个概念，所以我们直接在本地数据库进行插入  
#### 这里方法有两种解决对象存储的思路  
>  1.上传云端的时候将key也就是名字存储为\目录\目录2\文件.png 的形式，来模拟出树的结构   
>  2.在本地数据库建立一个文件和目录的关系，通过先查数据库path获取对应文件再通过获取到的key去云端取具体的文件数据  
>>  第一个是需要对文件名字做正则表达的处理，前端调用的时候也需要做展示之前的处理
>>  第二个只需要进行数据库操作,比较简单
>  如何选择请大家自己按照自己的需求来，我选择的方案是第二种  
* ### 5.修改名字  
直接调用接口  
* ###  6.删除文件  
> 删除步骤：  
>> 1.通过文件名去云端删除数据  
>> 2.获取删除成功的文件名存在一个Names[] 对象中  
>> 3.通过Names[] 的对象去本地数据库删除数据  
##注意这样一个操作，必须考虑调用接口和执行数据库的操作在一个事务之中，如果一步执行错误，中间出错，必须通过log日志中的数据进行数据的逆向回复现场  
···java  
    @ApiOperation(value = "/batchDelete", notes = "批量删除", httpMethod = "POST")
    @PostMapping("/batchDelete")
    public RequestBean<List<DeleteResult>> batchDelete(String[] name) throws JsonProcessingException {
        List<DeleteResult> deleteResultList = QiNiuUtil.batchDelete(name);
        List<String> list = new ArrayList<>();

        // 取出只有成功的操作key
        for (DeleteResult deleteResult : deleteResultList) {
            if (deleteResult.getResult().equals("succeed")) {
                list.add(deleteResult.getName());
            }
        }
        if (list.size() != 0) {
            String[] deleteVar = list.toArray(new String[list.size()]);
            fileService.batchDelete(deleteVar);
            return RequestBean.Success(deleteResultList);
        } else {
            return RequestBean.Error();
        }
    }
···



    




---------------------------------
---------------------------------
