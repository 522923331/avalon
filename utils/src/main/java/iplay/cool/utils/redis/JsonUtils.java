package iplay.cool.utils.redis;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.TimeZone;

/**
 * @author yp-23040708
 */
public class JsonUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final ObjectWriter writer;
    private static final ObjectWriter prettyWriter;

    static {
        objectMapper.configure(Feature.ALLOW_COMMENTS, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setSerializationInclusion(Include.NON_NULL);

        // 时间格式化显示
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));

        SimpleModule simpleModule = new SimpleModule();
        // Long类型处理，转换成String防止精度丢失
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        // Double类型处理，转换成String防止精度丢失
        simpleModule.addSerializer(Double.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Double.TYPE, ToStringSerializer.instance);
        // Float类型处理，转换成String防止精度丢失
        simpleModule.addSerializer(Float.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Float.TYPE, ToStringSerializer.instance);
        // BigDecimal类型处理，转换成String防止精度丢失
        simpleModule.addSerializer(BigDecimal.class, ToStringSerializer.instance);
        objectMapper.registerModule(simpleModule);

        writer = objectMapper.writer();
        prettyWriter = objectMapper.writerWithDefaultPrettyPrinter();
    }

    public JsonUtils() {
    }

    public static String toJsonPrettyString(Object value) throws JsonProcessingException {
        return prettyWriter.writeValueAsString(value);
    }

    public static String toJsonString(Object value) {
        try {
            return writer.writeValueAsString(value);
        } catch (Exception var2) {
            throw new IllegalStateException(var2);
        }
    }

    public static <T> T fromJsonString(String json, Class<T> clazz) {
        if (json == null) {
            return null;
        } else {
            try {
                return objectMapper.readValue(json, clazz);
            } catch (Exception var3) {
                throw new RuntimeException("Unable to parse Json String.", var3);
            }
        }
    }

    public static <T> T fromJsonString(String json, TypeReference<T> typeReference) {
        if (json == null) {
            return null;
        } else {
            try {
                return objectMapper.readValue(json, typeReference);
            } catch (Exception var3) {
                throw new RuntimeException("Unable to parse Json String.", var3);
            }
        }
    }

    public static JsonNode jsonNodeOf(String json) {
        return (JsonNode) fromJsonString(json, JsonNode.class);
    }

    public static JsonGenerator jsonGeneratorOf(Writer writer) throws IOException {
        return (new JsonFactory()).createGenerator(writer);
    }

    public static <T> T loadFrom(File file, Class<T> clazz) throws IOException {
        try {
            return objectMapper.readValue(file, clazz);
        } catch (IOException var3) {
            throw var3;
        } catch (Exception var4) {
            throw new IllegalStateException(var4);
        }
    }

    public static void load(InputStream input, Object obj) throws IOException, JsonProcessingException {
        objectMapper.readerForUpdating(obj).readValue(input);
    }

    public static <T> T loadFrom(InputStream input, Class<T> clazz) throws JsonParseException, JsonMappingException, IOException {
        return objectMapper.readValue(input, clazz);
    }

    public static void main(String[] args) {
        String dd = "{\"username\":\"mas_fwpt\",\"password\":\"NFwzF@1110@\",\"url\":\"http://idc.core:9999/channel/redirect/invoke?targetHost=http://10.91.1.151:8086/api/message/mass/send&targetName=sms&hasEncode=true\"}";
        System.out.println(JsonUtils.fromJsonString(dd, Map.class));
    }
}
