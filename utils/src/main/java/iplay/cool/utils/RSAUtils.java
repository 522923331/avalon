package iplay.cool.utils;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * RSA工具类
 *
 */
public class RSAUtils {

	public static final String SIGN_ALGORITHMS = "SHA256withRSA";

	/**
	 * @param content:签名的参数内容
	 * @param privateKey：私钥
	 * @return
	 */
	public static String sign(String content, String privateKey) {
		String charset = "utf-8";
		try {
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decode(privateKey));
			KeyFactory keyf = KeyFactory.getInstance("RSA");
			PrivateKey priKey = keyf.generatePrivate(priPKCS8);
			java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);
			signature.initSign(priKey);
			signature.update(content.getBytes(charset));
			byte[] signed = signature.sign();
			return Base64.encode(signed);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param content：验证参数的内容
	 * @param sign：签名
	 * @param publicKey：公钥
	 * @return
	 */
	public static boolean doCheck(String content, String sign, String publicKey) {
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			byte[] encodedKey = Base64.decode(publicKey);
			PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
			java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);
			signature.initVerify(pubKey);
			signature.update(content.getBytes("utf-8"));
			boolean bverify = signature.verify(Base64.decode(sign));
			return bverify;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void main(String[] args) {
		long time = System.currentTimeMillis();
		String content="3203001000646087570" + "-" + time;
		String signstr= RSAUtils.sign(content, "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDGEuJYfuO5i1kcckBu4yf+nGPcXMJ+2olH2yQH9l1uN5Bc2CGFb5cPEALC1FTRyi9NGxQw9YlVzg/mbhNasVqx320jw8HhLD/deIx2pWnDkcnIvmX5mnB9EJYmHl5WvxY2zxlUVov4+CQHH7ZrTtceBdaXISHieIuVnWu7CVamvMJLdr5r6DK4hKLf2WcIXbebNlFBdEJZNfA603BwnkB6BOc0y2GmxoQN3gEBBQ7Gg6ccvMHoZYVwzvocobNbQ11526BFCOyzoeNJNr/xnbJAs2j7GhZfRi/kT7iUXgylalPKyiMvovMYK/mkB53tPLi2UTxanJKhyl9IMCzss6RJAgMBAAECggEANK9hNVjLCdde5IofSV9yi+7dQQgLU+KdEVfgxZ1qTJ7K72ctw2hjLcZ8dYY06Xh2DHRfcoZc+U3OPOSakU0LarSbOyAeud4jPq2J7yUgdLyah6LdTP8fXKTEy27YeQpqxjlf62b55EdcjeiJhRF7dqjm41wUv0CBIFK3DRD203J6Dl5q7nBzMtqkwOr0uc++CJSPPNl6uWCYkkucgYuJSdg6qTN5i4907riJLoe8sVvZ3HwNi08mJcvFcS38AqlZb3U/dcgq7lclwaluPCVa4Qa3V1Pffn1jtu3M6tqUp7MQoFjDW5jPn62Ns+Tc+8aOciZhpidsBw2kOgLLfYft4QKBgQDkDLxUrgm2Kk8JwQbaAC6Gs4B/qhUW9Iid0d+O+oNlq82MpcTNxXl4IoVI0kQbucd4eRR/ZF0Rj+7I2HjU4aGVFdQO8nSAKWm9SERt/24izkXgSv2YJv0RDyqwMf6uv8bnK7JwdXtnIE6+5+5+qiQ9UB2rKIIU/D/we2KF1FpTvwKBgQDeWaATyeue7hvf6V767ZEtBfu5moiHz+Rk0AsK+bAE51XzgNZeUjB9jd2YtdkaoEnSaJfIOSBH3cr/YT7hSQgrazcqPmC4VvceeQc75PmIEbbW2CBjKeF3Wlm6ISz5D7O//Fr2c7g9t0nxDOPjc+rdfShZrRg5/2Rv3MuxMazp9wKBgQCvyp12jT4RPWX19+TnMVKsJTy9rIdbTZ7uQTUay0N0oU3uyG3/zOTI1Zt2uhckZD4+QN+rH9uaSSBhlHT4bQGYod34s69YiBPphAaz4D2u+ODmeAgqPIvBlbZrYe6YGF8kHgwMIpPAuCfbg3/WBiOvW82y/aPgt142a3ZUAZCrkwKBgBXi9pncD2BmicHho+LJTbgD1cJwTq4nrZCq1dtvtn7VEqXUccV5C6LgOwbUJ5myW3Hlr8MatG2+jEPVG65rVO2JMTXsS/L5RHvwZ+DiahmVvMyrG40HS1VHRHvI1jdx7zyfp4iI3YLdrt6WaTwzZdQFhNpG6pKS7B0Cu94mSKIDAoGAb2a4gCNE347TpyqPKVi3enik97eHppVh9pcb4hdThVLDUQndlf4peQltGoUoV6gEtKzuC5sq5HstQcpp6HXrqy/G7ATvU1m8jm7DXzxxMIImbaQs5vJ0EbxOWZpwQxXw/UAcI+o+pzbPWG8gITkOpXfl4uzPRwWqqrDEiPGIBHo=");
		System.out.println("签名原串："+content);
		System.out.println("签名串："+signstr);
		System.out.println();
		System.out.println("---------------公钥校验签名------------------");
		System.out.println("签名原串："+content);
		System.out.println("签名串："+signstr);
		System.out.println("验签结果："+ RSAUtils.doCheck(content, signstr, "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxhLiWH7juYtZHHJAbuMn/pxj3FzCftqJR9skB/ZdbjeQXNghhW+XDxACwtRU0covTRsUMPWJVc4P5m4TWrFasd9tI8PB4Sw/3XiMdqVpw5HJyL5l+ZpwfRCWJh5eVr8WNs8ZVFaL+PgkBx+2a07XHgXWlyEh4niLlZ1ruwlWprzCS3a+a+gyuISi39lnCF23mzZRQXRCWTXwOtNwcJ5AegTnNMthpsaEDd4BAQUOxoOnHLzB6GWFcM76HKGzW0NdedugRQjss6HjSTa/8Z2yQLNo+xoWX0Yv5E+4lF4MpWpTysojL6LzGCv5pAed7Ty4tlE8WpySocpfSDAs7LOkSQIDAQAB"));
		System.out.println();
	}
}