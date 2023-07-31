package com.github.mrzhqiang.rowing.setting;

import lombok.Data;

/**
 * RSA 密钥对数据。
 */
@Data
public class RSAKeyData {

    /**
     * 私钥。
     */
    private final String privateKey;
    /**
     * 公钥。
     */
    private final String publicKey;

}
