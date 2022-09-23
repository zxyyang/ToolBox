package com.toolbox;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentPBEConfig;

/**
 * Copyright: Copyright (C) 2022, Inc. All rights reserved.
 *
 * @author: zixuan.yang
 * @since: 2022/9/23 15:16
 */
public class JasyPT {

    /**
     * 加密方法
     */
    public static void TestEncrypt(String ps, String co) {
        EnvironmentPBEConfig config = new EnvironmentPBEConfig();
        // 加密的算法，这个算法是默认的
        config.setAlgorithm("PBEWithMD5AndDES");
        // 加密的秘钥，随便写，
        config.setPassword(ps);
        StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
        standardPBEStringEncryptor.setConfig(config);
        String encrypt = standardPBEStringEncryptor.encrypt(co);
        System.out.println("加密后的密码：" + encrypt);
    }

    /**
     * 解密方法
     */
    public static void testDe(String ps,String co) {
        EnvironmentPBEConfig config = new EnvironmentPBEConfig();
        // 加密的算法，这个算法是默认的
        config.setAlgorithm("PBEWithMD5AndDES");
        // 加密的秘钥，随便写，
        config.setPassword(ps);
        StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
        standardPBEStringEncryptor.setConfig(config);
        String decrypt = standardPBEStringEncryptor.decrypt(co);
        System.out.println("解密后的密码 = " + decrypt);
    }

    public static void main(String[] args) {
        TestEncrypt("","");
    }
}
