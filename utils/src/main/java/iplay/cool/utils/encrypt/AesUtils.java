//package iplay.cool.utils.encrypt;
//
//import org.bouncycastle.jce.provider.BouncyCastleProvider;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import javax.crypto.Cipher;
//import javax.crypto.spec.IvParameterSpec;
//import javax.crypto.spec.SecretKeySpec;
//import java.nio.charset.StandardCharsets;
//import java.security.Security;
//import java.security.spec.AlgorithmParameterSpec;
//import java.util.Objects;
//
///***
// * 需要导入web3相关jar包才能使用
// */
//@Service
//public class AesUtils {
//
//    public static String k;
//    public static String v;
//
//    public static Logger logger = LoggerFactory.getLogger(AesUtils.class);
//
//    public AesUtils(@Value("${encrypt.ik}") String k, @Value("${encrypt.iv}") String v) {
//        AesUtils.k = k;
//        AesUtils.v = v;
//    }
//
//    static public byte hexStrToByte(String hexbytein) {
//        return (byte) Integer.parseInt(hexbytein, 16);
//    }
//
//    public static byte[] Str2Hex(String hexStrIn) {
//        int hexlen = hexStrIn.length() / 2;
//        byte[] result;
//        result = new byte[hexlen];
//        for (int i = 0; i < hexlen; i++) {
//            result[i] = hexStrToByte(hexStrIn.substring(i * 2, i * 2 + 2));
//        }
//        return result;
//    }
//
//    public static String Hex2Str(byte[] hexByteIn) {
//        int len = hexByteIn.length;
//        String restult = new String();
//        for (int i = 0; i < len; i++) {
//            restult += String.format("%02x", hexByteIn[i]);
//        }
//        return restult;
//    }
//
//    public static String encrypt(String value) {
//        return encrypt(value, AesUtils.k, AesUtils.v);
//    }
//
//    public static String encrypt(String value, String ik, String iv) {
//        try {
//            Security.addProvider(new BouncyCastleProvider());
//            byte[] key = ik.getBytes(StandardCharsets.UTF_8);
//            byte[] ivs = iv.getBytes(StandardCharsets.UTF_8);
//            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
//            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
//            AlgorithmParameterSpec paramSpec = new IvParameterSpec(ivs);
//            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, paramSpec);
//            return Hex2Str(cipher.doFinal(value.getBytes(StandardCharsets.UTF_8)));
//        } catch (Exception e) {
//            logger.error("AES加密异常，e:", e);
////            throw new BusinessException(ResponseStatusEnum.ENCRYPT_ERROR);
//        }
//    }
//
//    public static String decrypt(String value) {
//        return decrypt(value, AesUtils.k, AesUtils.v);
//    }
//
//    public static String decrypt(String value, String ik, String iv) {
//        try {
//            Security.addProvider(new BouncyCastleProvider());
//            byte[] key = ik.getBytes(StandardCharsets.UTF_8);
//            byte[] ivs = iv.getBytes(StandardCharsets.UTF_8);
//            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
//            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
//            AlgorithmParameterSpec paramSpec = new IvParameterSpec(ivs);
//            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, paramSpec);
//            logger.info("ik:{};iv:{},value={}", ik, iv, value);
//            return new String(cipher.doFinal(Objects.requireNonNull(Str2Hex(value))));
//        } catch (Exception e) {
//            logger.error("AES解密异常，e:", e);
////            throw new BusinessException(ResponseStatusEnum.DECRYPT_ERROR);
//        }
//    }
//
////    public static void main(String[] args) throws Exception {
////        String k="3GXQ1X3isq7AvGrD4P3Elz3kXRoBl88t";
////        String v="AUbRZyJubtMEJit";
////        String k = "fghjklghjklfghjkladsadas123jsdjf";
////        String v = "rdfyghujikoldask";
////        String encrypt = encrypt("1234567890", k, v);
////        String decrypt = decrypt(encrypt, k, v);
////        System.out.println(decrypt);
////        HttpHeaders httpHeaders = new HttpHeaders();
////        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
////        Map<String, Object> params = new HashMap<>();
////        params.put("coin","trx");
////        params.put("limit",1);
////        String js = JSON.toJSONString(params);
////        Security.addProvider(new BouncyCastleProvider());
////
////        String encrypt = AesUtils.encrypt(js, k, v);
////        String token = AesUtils.encrypt(String.valueOf(System.currentTimeMillis()), k, v);
////        Map<String, Object> req = new HashMap<>();
////        req.put("content", encrypt);
////        req.put("token", token);
////        RestTemplate restTemplate = new RestTemplate();
////            HttpEntity<Object> httpEntity = new HttpEntity<>(req, httpHeaders);
////        String url = "http://192.168.3.66:4040/get/address";
////        ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
////        JSONObject ret=JSON.parseObject(exchange.getBody());
////        System.out.println(AesUtils.decrypt(ret.getString("data"), k, v));
//
////        SecureRandom random = new SecureRandom();
////        byte[] key = new byte[32];
////        random.nextBytes(key);
////        String keyString = DatatypeConverter.printHexBinary(key);
////        byte[] iv = new byte[16];
////        random.nextBytes(iv);
////        String ivString = DatatypeConverter.printHexBinary(iv);
////
////
////        System.out.println(keyString);
////        System.out.println(ivString);
////    }
//
//    public static void main(String[] args) {
//        String encrypt = encrypt("1ce19ead5413de3a3368bb24bef717d4167dcaaeca5c67b8077ee6ad361025d8","3GXQ1X3isq7AvGrD4P3Elz3kXRoBl88t","AUbRZyJubtMEJibt");
//        System.out.println(encrypt);
//    }
//
//
//}