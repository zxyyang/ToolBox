
package com.toolbox.config;

import java.util.Properties;

import com.qiniu.common.Zone;

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

	private Zone imageZone;

	private Zone zone;

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
			if (zoneName.equals("zone0")) {
				zone = Zone.zone0();
			} else if (zoneName.equals("zone1")) {
				zone = Zone.zone1();
			} else if (zoneName.equals("zone2")) {
				zone = Zone.zone2();
			} else if (zoneName.equals("zoneNa0")) {
				zone = Zone.zoneNa0();
			} else if (zoneName.equals("zoneAs0")) {
				zone = Zone.zoneAs0();
			} else {
				throw new Exception("Zone对象配置错误！");
			}
			String imageZoneName = prop.getProperty("qiniu.image.zone");
			if (imageZoneName.equals("zone0")) {
				zone = Zone.zone0();
			} else if (imageZoneName.equals("zone1")) {
				zone = Zone.zone1();
			} else if (imageZoneName.equals("zone2")) {
				zone = Zone.zone2();
			} else if (imageZoneName.equals("zoneNa0")) {
				zone = Zone.zoneNa0();
			} else if (imageZoneName.equals("zoneAs0")) {
				zone = Zone.zoneAs0();
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