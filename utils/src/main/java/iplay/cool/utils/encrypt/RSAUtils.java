package iplay.cool.utils.encrypt;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author wu.dang
 * @since 2024/7/8
 */
public class RSAUtils {

	/**
	 * RSA最大加密明文大小
	 */
	private static final int MAX_ENCRYPT_BLOCK = 117;

	/**
	 * RSA最大解密密文大小
	 */
	private static final int MAX_DECRYPT_BLOCK = 128;

	/**
	 * 获取密钥对
	 *
	 * @return 密钥对
	 */
	public static KeyPair getKeyPair() throws Exception {
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		generator.initialize(1024);
		return generator.generateKeyPair();
	}

	/**
	 * 获取私钥
	 *
	 * @param privateKey 私钥字符串
	 * @return
	 */
	public static PrivateKey getPrivateKey(String privateKey) throws Exception {
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		byte[] decodedKey = Base64.decodeBase64(privateKey.getBytes());
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
		return keyFactory.generatePrivate(keySpec);
	}

	/**
	 * 获取公钥
	 *
	 * @param publicKey 公钥字符串
	 * @return
	 */
	public static PublicKey getPublicKey(String publicKey) throws Exception {
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		byte[] decodedKey = Base64.decodeBase64(publicKey.getBytes());
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
		return keyFactory.generatePublic(keySpec);
	}

	/**
	 * RSA加密
	 *
	 * @param data 待加密数据
	 * @param publicKey 公钥
	 * @return
	 */
	public static String encrypt(String data, PublicKey publicKey) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		int inputLen = data.getBytes().length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offset = 0;
		byte[] cache;
		int i = 0;
		// 对数据分段加密
		while (inputLen - offset > 0) {
			if (inputLen - offset > MAX_ENCRYPT_BLOCK) {
				cache = cipher.doFinal(data.getBytes(), offset, MAX_ENCRYPT_BLOCK);
			} else {
				cache = cipher.doFinal(data.getBytes(), offset, inputLen - offset);
			}
			out.write(cache, 0, cache.length);
			i++;
			offset = i * MAX_ENCRYPT_BLOCK;
		}
		byte[] encryptedData = out.toByteArray();
		out.close();
		// 获取加密内容使用base64进行编码,并以UTF-8为标准转化成字符串
		// 加密后的字符串
		return new String(Base64.encodeBase64String(encryptedData));
	}

	/**
	 * RSA解密
	 *
	 * @param data 待解密数据
	 * @param privateKey 私钥
	 * @return
	 */
	public static String decrypt(String data, PrivateKey privateKey) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] dataBytes = Base64.decodeBase64(data);
		int inputLen = dataBytes.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offset = 0;
		byte[] cache;
		int i = 0;
		// 对数据分段解密
		while (inputLen - offset > 0) {
			if (inputLen - offset > MAX_DECRYPT_BLOCK) {
				cache = cipher.doFinal(dataBytes, offset, MAX_DECRYPT_BLOCK);
			} else {
				cache = cipher.doFinal(dataBytes, offset, inputLen - offset);
			}
			out.write(cache, 0, cache.length);
			i++;
			offset = i * MAX_DECRYPT_BLOCK;
		}
		byte[] decryptedData = out.toByteArray();
		out.close();
		// 解密后的内容
		return new String(decryptedData, "UTF-8");
	}

	/**
	 * 签名
	 *
	 * @param data 待签名数据
	 * @param privateKey 私钥
	 * @return 签名
	 */
	public static String sign(String data, PrivateKey privateKey) throws Exception {
		byte[] keyBytes = privateKey.getEncoded();
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PrivateKey key = keyFactory.generatePrivate(keySpec);
		Signature signature = Signature.getInstance("SHA256withRSA");
		signature.initSign(key);
		signature.update(data.getBytes());
		return new String(Base64.encodeBase64(signature.sign()));
	}

	/**
	 * 验签
	 *
	 * @param srcData 原始字符串
	 * @param publicKey 公钥
	 * @param sign 签名
	 * @return 是否验签通过
	 */
	public static boolean verify(String srcData, PublicKey publicKey, String sign) throws Exception {
		byte[] keyBytes = publicKey.getEncoded();
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey key = keyFactory.generatePublic(keySpec);
		Signature signature = Signature.getInstance("SHA256withRSA");
		signature.initVerify(key);
		signature.update(srcData.getBytes());
		return signature.verify(Base64.decodeBase64(sign.getBytes()));
	}

	/**
	 * 对数据进行排序后拼接为字符串
	 * @param jsonObject
	 * @return
	 */
	public static String getSortedData(JSONObject jsonObject){
		List<String> keys = new ArrayList<>(jsonObject.keySet());
		StringBuilder sb = new StringBuilder();
		Collections.sort(keys);
		for (String key : keys) {
			sb.append(jsonObject.get(key));
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		try {
			// 生成密钥对
//			KeyPair keyPair = getKeyPair();
//			String privateKey = new String(Base64.encodeBase64(keyPair.getPrivate().getEncoded()));
//			String publicKey = new String(Base64.encodeBase64(keyPair.getPublic().getEncoded()));
//			System.out.println("私钥:" + privateKey);
//			System.out.println("公钥:" + publicKey);
//			// RSA加密
//			String data = "待加密的文字内容";
//			String encryptData = encrypt(data, getPublicKey(publicKey));
//			System.out.println("加密后内容:" + encryptData);
//			// RSA解密
//			String decryptData = decrypt(encryptData, getPrivateKey(privateKey));
//			System.out.println("解密后内容:" + decryptData);
//
//			// RSA签名
//			String sign = sign(data, getPrivateKey(privateKey));
//			// RSA验签
//			boolean result = verify(data, getPublicKey(publicKey), sign);
//			System.out.print("验签结果:" + result);
			String privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAILIk/bpuaep3S6EHCLkx3dJVzVv+DQ3ToOgyhdt5teH+HUrmCKoanN+hBF3NekERwrPZNaVbLnSEPGvN1dP0gW+dwYNSldtSvGnA/KtO8EVA5eMrC0/HKLrubHZ/Gemo6UD17fKZCErkaGmz67dBxlGbG64hFvS4oSpB4Y6EZ1zAgMBAAECgYA4xJWtZJBonYvbaA7KeqG2PohzMpH7IFKdQgrWlqbPwT5wpaaYzJ/AWBc9eZBV/7xSjelIV33lPrCKJ7MO3B/eLZAKfyp5W1LNB/drfMpXeWYKkRs9C30VgkjH7vMYoy5RPx6wKwzwY9yheV4Zdh9G2Twn6Nit8qOr4u4JF6I/MQJBALedrMSEDvHBwYhXhisijiAQdibgS922Sgyxk1DUrnlLuYFKpvF5uCEGd5URbypAvwiOowOTzNZxffFD2a+Qz5sCQQC2VxynpO+5sR+JUaLMZbUyCmTIQiDfSc+MmwLZATXk+OuvnpyUgwhbDNlBr4lkBnaH0A17W4il1jMNKtl6GoMJAkB2gFRYH8JlVF7K13HHtO101CrsvCU6WcNAnfotWJWhwrVeNAe6IIwtBDd5BV9xLYgcxbF/RYwVefmGE/wRyquxAkB+8hkOEkOgGVOma8KW4TyMUTYnQfrW2fF4p7cM083s0uxrgVbsAmn/0esz0v0pOWKuXUf1mR2Cr6UtRqbQXsLZAkAtKWx5KOTx8fuluUus0Ut4RQP7bRrX4s6UqqF1vBnFttsYIqFq/q28u/GFQ+BB3xCQwF3H0yUyQb9GXfebxVlk";
			String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCCyJP26bmnqd0uhBwi5Md3SVc1b/g0N06DoMoXbebXh/h1K5giqGpzfoQRdzXpBEcKz2TWlWy50hDxrzdXT9IFvncGDUpXbUrxpwPyrTvBFQOXjKwtPxyi67mx2fxnpqOlA9e3ymQhK5Ghps+u3QcZRmxuuIRb0uKEqQeGOhGdcwIDAQAB";
			String data="12311723046412345api test217204937522560xA33781f85f20CEE1bAf63aCA36e72Afc165bB5CC";
			String sign = "HddPJ0HC91jo9RPRpNr2oU8BUMogqG1QW7i+jsuDnCO7M1PL400T8si18RYYzL16VV349jSz+1uAl3G23d6gDgjgUDjw6kO2O8ezxgGpKCNt1y0JUjNF9ZBDmrq0w36l4DWLfslme+I3ZTcVpdzDHakPu2ATkJygDWxEXhmI6MI=";

			boolean result = verify(data, getPublicKey(publicKey), sign);
			System.out.println(result);
			sendData(privateKey);


		} catch (Exception e) {
			e.printStackTrace();
			System.out.print("加解密异常");
		}
	}

	private static void sendData(String privateKey) throws Exception{
		String reqData = "{\n" +
				"    \"description\":\"api test\",\n" +
				"    \"amount\":\"123\",\n" +
				"    \"toAddress\": \"0xA33781f85f20CEE1bAf63aCA36e72Afc165bB5CC\",\n" +
				"    \"apiId\": \"1\",\n" +
				"    \"closeAt\":\"1723046412345\",\n" +
				"    \"requestId\":\"4\",\n" +
				"    \"timestamp\":1720493752256\n" +
				"}";
		JSONObject jsonObject = JSONUtil.parseObj(reqData);
		String sortedData = getSortedData(jsonObject);
		String sign = sign(sortedData, getPrivateKey(privateKey));
		jsonObject.set("sign",sign);
		System.out.println("sortedData="+sortedData);
		System.out.println("sign="+sign);
		String url = "http://localhost:8081/api/pay/order/create";
		String post = HttpUtil.post(url, jsonObject.toString());
		System.out.println("ssssssss="+post);
	}

}

