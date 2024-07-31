package iplay.cool.utils.pdf;

/**
 * @author wu.dang
 * @since 2024/7/31
 */

import com.lowagie.text.Document;
import com.lowagie.text.pdf.*;

import javax.servlet.ServletOutputStream;
import java.io.ByteArrayOutputStream;
import java.util.Map;

/**
 * 经过验证，可以使用
 * @author wu.dang
 * @since 2024/7/26
 */
public class PdfUtil_openPDF {

    /**
     * 利用模板生成pdf
     *
     * @param data         写入的数据
     * @param out          自定义保存pdf的文件流
     * @param templatePath pdf模板路径
     */
    public static void fillTemplate(Map<String, Object> data, ServletOutputStream out, String templatePath) throws Exception {
        // 读取pdf模板
        PdfReader reader = new PdfReader(templatePath);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PdfStamper stamper = new PdfStamper(reader, bos);
        AcroFields acroFields = stamper.getAcroFields();
        // 赋值
        for (String name : acroFields.getAllFields().keySet()) {
            String value = data.get(name) != null ? data.get(name).toString() : null;
            //先设置样式，再设置value，否则样式不会生效
            if ("titleAmount".equals(name)){
                acroFields.setFieldProperty(name, "textsize", 50f, null);
            }else {
                acroFields.setFieldProperty(name, "textsize", 34f, null);
            }
            acroFields.setField(name, value);
        }
        // 如果为false那么生成的PDF文件还能编辑，一定要设为true
        stamper.setFormFlattening(true);
        stamper.close();
        Document doc = new Document();
        PdfCopy copy = new PdfCopy(doc, out);
        doc.open();
        PdfImportedPage importPage = copy.getImportedPage(new PdfReader(bos.toByteArray()), 1);
        copy.addPage(importPage);
        doc.close();
        bos.close();
    }
}
