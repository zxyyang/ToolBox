
package com.toolbox.config;

import java.util.Properties;

import com.qiniu.common.Zone;

import com.qiniu.storage.Region;
import com.qiniu.storage.RegionGroup;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

/**
 * 七牛云配置
 **/


@Data
@Slf4j
@Component
public class QiNiuConfig implements InitializingBean {

//	private static QiNiuConfig instance = new QiNiuConfig();

	@Value("${qiniu.access-key}")
	private String accessKey;

	@Value("${qiniu.secret-key}")
	private String secretKey;

	@Value("${qiniu.bucket}")
	private String bucket;

	@Value("${qiniu.image.bucket}")
	private String imageBucket;

	@Value("${qiniu.image.zone}")
	private Object imageZone;

	@Value("${qiniu.zone}")
	private Object zone;

	@Value("${qiniu.domain-of-bucket}")
	private String domainOfBucket;

	@Value("${qiniu.image.domain-of-bucket}")
	private String imageDomainOfBucket;

	@Value("${qiniu.expire-in-seconds}")
	private long expireInSeconds;

	public QiNiuConfig() {
//		try {
//			YamlPropertiesFactoryBean yamlProFb = new YamlPropertiesFactoryBean();
//			yamlProFb.setResources(new ClassPathResource("application.yml"));
//			Properties object = yamlProFb.getObject();
//			accessKey = object.getProperty("qiniu.access-key");
//			secretKey = object.getProperty("qiniu.secret-key");
//			imageBucket = object.getProperty("qiniu.image.bucket");
//			bucket = object.getProperty("qiniu.bucket");
//			domainOfBucket = object.getProperty("qiniu.domain-of-bucket");
//			imageDomainOfBucket = object.getProperty("qiniu.image.domain-of-bucket");
//			expireInSeconds = Long.parseLong(object.getProperty("qiniu.expire-in-seconds"));
//			String zoneName = object.getProperty("qiniu.zone");
//			if (zoneName.equals("huadong")) {
//				 zone = Region.huadong();
//			} else if (zoneName.equals("huabei")) {
//				zone = Region.huabei();
//			} else if (zoneName.equals("huanan")) {
//				zone = Region.huanan();
//			} else if (zoneName.equals("beimei")) {
//				zone = Region.beimei();
//			} else if (zoneName.equals("xinjiapo")) {
//				zone = Region.xinjiapo();
//			} else {
//				throw new Exception("Zone对象配置错误！");
//			}
//			String imageZoneName = object.getProperty("qiniu.image.zone");
//			if (imageZoneName.equals("huadong")) {
//				imageZone = Region.huadong();
//			} else if (imageZoneName.equals("huabei")) {
//				imageZone = Region.huabei();
//			} else if (imageZoneName.equals("huanan")) {
//				imageZone = Region.huanan();
//			} else if (imageZoneName.equals("beimei")) {
//				imageZone = Region.beimei();
//			} else if (imageZoneName.equals("xinjiapo")) {
//				imageZone = Region.xinjiapo();
//			} else {
//				throw new Exception("Zone对象配置错误！");
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Object zoneName = String.valueOf(this.getZone());
		Region zone = new RegionGroup();
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
			this.setZone(zone);
		Object imageZoneName = String.valueOf(this.getImageZone());
		Region imageZone = new RegionGroup();
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
			this.setImageZone(imageZone);
	}

//	public static QiNiuConfig getInstance() {
//		return instance;
//	}
	//
	// public static void main(String[] args) {
	// System.out.println(QiNiuConfig.getInstance().getAccessKey());
	// }
}