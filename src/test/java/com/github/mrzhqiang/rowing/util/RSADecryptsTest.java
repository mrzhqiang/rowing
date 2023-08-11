package com.github.mrzhqiang.rowing.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;

/**
 * <p>
 */
class RSADecryptsTest {

    @Test
    void test() {
        System.out.println("准备生成 RSA 公钥和私钥...");
        KeyPair keyPair = RSADecrypts.generateKeyPair();
        System.out.println("成功生成公钥：");
        String publicKey = RSADecrypts.publicKey(keyPair);
        System.out.println(publicKey);
        System.out.println("成功生成私钥：");
        String privateKey = RSADecrypts.privateKey(keyPair);
        System.out.println(privateKey);

        String rawText = "1a52e84e-7bf1-4aaa-b4d7-bf976a76a7c6";
        System.out.println("尝试公钥加密：" + rawText);
        String encrypt = RSADecrypts.encrypt(rawText, RSADecrypts.parsePublicKey(publicKey));
        System.out.println("尝试私钥解密：" + encrypt);
        String decrypt = RSADecrypts.decrypt(encrypt, RSADecrypts.parsePrivateKey(privateKey));
        System.out.println("解密完成：" + decrypt);
        Assertions.assertEquals(rawText, decrypt);
    }

    @Test
    void decrypt() {
        String privateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCXbc4nfIPV/U1gCGSuoVVvaYilb04/EUQ0QyTWfgarJJMNWCG/C10M/knJFvACN5vsrHdGiAd6tttD2N98hI7j8WR56ruCFAb6K47WVzWW8B0iFYdklJLFybhifAxWSI7cWqq12L2CuMGQ7yMsAtMdlcgbiZWr8vTbAIY6vjTsj5O0iTL5kCKUPOAfOmyofyvBCTVifarOzIgDgjQUOuy8fthd5uwDGw3y3pusIeGcJ67s0oofoov2v37yt2DWwP4kMGtzpwTD+exr/z/xEzcdp/Af2vmIt7fBxO2ojHu8Yyu4UDjFTpd1KoE66tfz+QFuc3ugL3OE77i0+mHiirInAgMBAAECggEAI0oitg8NhkwewReTpyLQD0PP5YX3/Tif5XIUXqxGD5JZds2oJ6aVS9bjCSsASfP9sX1L2rMO0ylUFg6vnowh5Ls3yDXM9vaF3b1S716pjYBtuai8wdLiFZf6EXGJSn5kVLQIfpKBL7qw2X7xems6GfXzCX/Z0pWvPG512bDEErDTWAwCoA++YesnY2+v0l/TzJR1mZkfNt82c7tIOU2Cft0kzFB9BJST4HrRVu3klHHCPOXDuqGcsQPPFcj8ctWekU+XxGe2RH3dCA3xGsqNxhwZ6l1TxT3rHWWffQf2yEvwfXZlhAFaPufL1T3wmU6/8LMia1cmaVfubUwTxvpM2QKBgQDJFnpuRoc6OHDUmMVLXoVm7TMtEHHngVO9eZpmPbWU3Fus1JqUANBYwp4SaQcAXc/d40gkmBLdDgpE8lFwSg4+TylV5LWuw0UHKY0NHCrO2HW2EOzrr/sjj6MbkKT7smm8PTFFH3XWm4ncm7M42QogkaagIXE67hCyQ+tePlzjgwKBgQDAx84OqsgquTNTK9OG7ilV29xMXyQ0V8584hNzt8BDgQEqm1QzDGfmGQ8ki77zX7n6U61ZznlRXmZHGWN4xjB8+KS1g7AJyMmnhycA2mC6ue0VsdCrMe4rpf4jG6a8hvhmWxuvAJiizxm7uALxRDYLQwcrRbovh6EPtPwn+sShjQKBgQCfXWwsJ0MWlhJ9upFp2QA6gTQ0u7q58ZnPKSd/bdqRnk4dW+Uh9L8qCRtDle8RkEekH3NWZRtWC8GBwNVf+6DsKsqkovyGA4WaVojN4gvfIFexshpB85dCKJzoxKqsO0pm/8wjbvpx2ZTZvJ++JuxKwYPfloFpZbDfxsorR5tQRwKBgQCVBN5/7/wzKl5GWIZ2svs4529vicwOuiFf39JxjQUOKVahfNilbV2TPKO7+Fpu/vxgSPajiTu2bmZZZ4LBBVLOB9YqidU2ewOGgK7hte036ikesAlBjKMzEmlU8jb9XMf/N0Y7DRbwI28c8K01myi7o+CSzp+RxiH2ZkzVCr+csQKBgAbvLosbNTXii8cqll3rNZoiDEuYxq7s2QLkOV244DySYeRv/XIo5mLefGeCY5ccflKHL8Sei3Hsw/tzuepHIshS7teR4dzZ/l136swqi5qHx5ZAPxRUCxgpADg4GGHIyK8zwLKHWANWQpfwoDq+SOpyny+cg366Fkh82IoTwpxh";
        String rawPassword = "MZVetu2aFxyyOonsAdEEg/VHB4+WGQ1xyu0Bs31V18068IRrA2x7lVtJseeZO4M21yyEKa9L1EP1UdvOCJsZxEliOg+QjFJBH2DgblwRA6/IwCDLhXlGj4iO1pG0YxiBuciLk2VvVen/VVJpww1cA3wFf9JoVeiV1YEBi+57MAw3lTZbWwuBm1hbk3Zity1PPgt9r/WrHcmErLo2icXgBi5rCzebQWkxN8+vG9YCsCUtEZA/n8uLNUOMbakPFon0AMABPQQiXQIrwWr2u8Ke/xWo87oiMGcrV85lbY52Lrbu1MKDBm4wtyg0AVsS8lAJtPUdEJnKs1/t1JOvt6G6mg==";
        String decrypt = RSADecrypts.decrypt(rawPassword, RSADecrypts.parsePrivateKey(privateKey));
        Assertions.assertEquals("09ea813f-f0ba-47ee-a98e-16f3e2d9d7f1", decrypt);
    }

}
