//package iplay.cool.utils.encrypt;
//
//import org.apache.commons.io.IOUtils;
//import org.apache.tomcat.util.codec.binary.Base64;
//
//import javax.crypto.Cipher;
//import java.io.ByteArrayOutputStream;
//import java.security.*;
//import java.security.interfaces.RSAPrivateKey;
//import java.security.interfaces.RSAPublicKey;
//import java.security.spec.PKCS8EncodedKeySpec;
//import java.security.spec.X509EncodedKeySpec;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * 需要导入ioUtil包才能使用
// */
//public class XRsa {
//    public static final String CHARSET = "UTF-8";
//    public static final String RSA_ALGORITHM = "RSA";
//    public static final String RSA_ALGORITHM_SIGN = "SHA256WithRSA";
//
//    private RSAPublicKey publicKey;
//    private RSAPrivateKey privateKey;
//
//    public XRsa(String publicKey, String privateKey) {
//        try {
//            KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
//            // 通过X509编码的Key指令获得公钥对象
//            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKey.getBytes()));
//            this.publicKey = (RSAPublicKey) keyFactory.generatePublic(x509KeySpec);
//            // 通过PKCS#8编码的Key指令获得私钥对象
//            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey.getBytes()));
//            this.privateKey = (RSAPrivateKey) keyFactory.generatePrivate(pkcs8KeySpec);
//        } catch (Exception e) {
//            throw new RuntimeException("不支持的密钥: ", e);
//        }
//    }
//
//    // 私钥解密
//    public String privateDecrypt(String data) {
//        try {
//            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
//            cipher.init(Cipher.DECRYPT_MODE, privateKey);
//            return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.decodeBase64(data.getBytes()),publicKey.getModulus().bitLength()), CHARSET);
//        } catch (Exception e) {
//            throw new RuntimeException("私钥解密字符串[" + data + "]时遇到异�?", e);
//        }
//    }
//
//    // 私钥加密
//    public String privateEncrypt(String data) {
//        try {
//            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
//            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
//            return new String(Base64.encodeBase64(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET),publicKey.getModulus().bitLength())));
//        } catch (Exception e) {
//            throw new RuntimeException("私钥加密字符串[" + data + "]时遇到异�?", e);
//        }
//    }
//
//    // 公钥解密
//    public String publicDecrypt(String data) {
//        try {
//            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
//            cipher.init(Cipher.DECRYPT_MODE, publicKey);
//            return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.decodeBase64(data.getBytes()),publicKey.getModulus().bitLength()), CHARSET);
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException("公钥解密字符串[" + data + "]时遇到异�?", e);
//        }
//    }
//
//    // 私钥签名
//    public String sign(String data) {
//        try {
//            Signature signature = Signature.getInstance(RSA_ALGORITHM_SIGN);
//            signature.initSign(privateKey);
//            signature.update(data.getBytes(CHARSET));
//            return new String(Base64.encodeBase64(signature.sign()));
//        } catch (Exception e) {
//            throw new RuntimeException("私钥签名字符串[" + data + "]时遇到异�?", e);
//        }
//    }
//
//    // 公钥验签
//    public boolean verify(String data, String sign) {
//        try {
//            Signature signature = Signature.getInstance(RSA_ALGORITHM_SIGN);
//            signature.initVerify(publicKey);
//            signature.update(data.getBytes(CHARSET));
//            return signature.verify(Base64.decodeBase64(sign.getBytes()));
//        } catch (Exception e) {
//            throw new RuntimeException("公钥验签字符串[" + data + "]时遇到异�?", e);
//        }
//    }
//
//    private static byte[] rsaSplitCodec(Cipher cipher, int opmode, byte[] datas, int keySize) {
//        int maxBlock = 0;
//        if (opmode == Cipher.DECRYPT_MODE) {
//            maxBlock = keySize / 8;
//        } else {
//            maxBlock = keySize / 8 - 11;
//        }
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        int offSet = 0;
//        byte[] buff;
//        int i = 0;
//        try {
//            while (datas.length > offSet) {
//                if (datas.length - offSet > maxBlock) {
//                    buff = cipher.doFinal(datas, offSet, maxBlock);
//                } else {
//                    buff = cipher.doFinal(datas, offSet, datas.length - offSet);
//                }
//                out.write(buff, 0, buff.length);
//                i++;
//                offSet = i * maxBlock;
//            }
//        } catch (Exception e) {
//            throw new RuntimeException("加解密阀值为[" + maxBlock + "]的数据时发生异常", e);
//        }
//        byte[] resultDatas = out.toByteArray();
//        IOUtils.closeQuietly(out);
//        return resultDatas;
//    }
//
//
//    public static Map<String, String> createKeys(int keySize) {
//        KeyPairGenerator kpg;
//        try {
//            kpg = KeyPairGenerator.getInstance(RSA_ALGORITHM);
//        } catch (NoSuchAlgorithmException e) {
//            throw new IllegalArgumentException("No such algorithm -> [" + RSA_ALGORITHM + "]");
//        }
//        // 初始化KeyPairGenerator对象
//        kpg.initialize(keySize);
//        // 生成秘钥对
//        KeyPair keyPair = kpg.generateKeyPair();
//        // 得到公钥
//        Key publicKey = keyPair.getPublic();
//        String publicKeyStr = new String(Base64.encodeBase64(publicKey.getEncoded()));
//        // 得到私钥
//        Key privateKey = keyPair.getPrivate();
//        String privateKeyStr = new String(Base64.encodeBase64(privateKey.getEncoded()));
//        Map<String, String> keyPairMap = new HashMap<String, String>();
//        keyPairMap.put("publicKey", publicKeyStr);
//        keyPairMap.put("privateKey", privateKeyStr);
//        return keyPairMap;
//    }
//
//    public static void main(String[] args) {
//        Map<String, String> keys = createKeys(512);
//        String data = "123456789";
//        XRsa xRsa = new XRsa(keys.get("publicKey"), keys.get("privateKey"));
//        String s = xRsa.privateEncrypt(data);
//        String s1 = xRsa.publicDecrypt(s);
//        System.out.println(s);
//        System.out.println(s1);
//    }
//}
