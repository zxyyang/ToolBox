
package com.toolbox.config;

import java.util.Properties;

import com.qiniu.common.Zone;

import com.qiniu.storage.Region;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 七牛云配置
 **/
@Slf4j
@Data

public class QiNiuConfig {

	private static QiNiuConfig instance = new QiNiuConfig();

	private String accessKey;

	private String secretKey;

	private String bucket;

	private String imageBucket;

	private Region imageZone;

	private Region zone;

	private String domainOfBucket;

	private String imageDomainOfBucket;

	private long expireInSeconds;

	private QiNiuConfig() {
		Properties prop = new Properties();
		try {
			prop.load(QiNiuConfig.class.getResourceAsStream("/qiniu.properties"));
			accessKey = prop.getProperty("qiniu.access-key");
			secretKey = prop.getProperty("qiniu.secret-key");
			imageBucket = prop.getProperty("qiniu.image.bucket");
			bucket = prop.getProperty("qiniu.bucket");
			domainOfBucket = prop.getProperty("qiniu.domain-of-bucket");
			imageDomainOfBucket = prop.getProperty("qiniu.image.domain-of-bucket");
			expireInSeconds = Long.parseLong(prop.getProperty("qiniu.expire-in-seconds"));
			String zoneName = prop.getProperty("qiniu.zone");
			if (zoneName.equals("huadong")) {
				 zone = Region.huadong();
			} else if (zoneName.equals("huabei")) {
				zone = Region.huabei();
			} else if (zoneName.equals("huanan")) {
				zone = Region.huanan();
			} else if (zoneName.equals("beimei")) {
				zone = Region.beimei();
			} else if (zoneName.equals("xinjiapo")) {
				zone = Region.xinjiapo();
			} else {
				throw new Exception("Zone对象配置错误！");
			}
			String imageZoneName = prop.getProperty("qiniu.image.zone");
			if (imageZoneName.equals("huadong")) {
				imageZone = Region.huadong();
			} else if (imageZoneName.equals("huabei")) {
				imageZone = Region.huabei();
			} else if (imageZoneName.equals("huanan")) {
				imageZone = Region.huanan();
			} else if (imageZoneName.equals("beimei")) {
				imageZone = Region.beimei();
			} else if (imageZoneName.equals("xinjiapo")) {
				imageZone = Region.xinjiapo();
			} else {
				throw new Exception("Zone对象配置错误！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static QiNiuConfig getInstance() {
		return instance;
	}
	//
	// public static void main(String[] args) {
	// System.out.println(QiNiuConfig.getInstance().getAccessKey());
	// }
}