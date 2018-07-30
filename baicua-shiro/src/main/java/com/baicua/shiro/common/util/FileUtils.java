package com.baicua.shiro.common.util;

import com.baicua.shiro.common.domain.ResponseBo;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;
import org.apache.pdfbox.util.Matrix;
import sun.misc.BASE64Encoder;

import javax.swing.text.Document;
import java.awt.*;
import java.io.*;
import java.nio.file.NoSuchFileException;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import static org.apache.poi.xwpf.usermodel.XWPFRun.FontCharRange.cs;

public class FileUtils {
    final static PDFont font = PDType1Font.HELVETICA_OBLIQUE;
    /**
     * 文件名加UUID
     *
     * @param filename 文件名
     * @return UUID_文件名
     */
    private static String makeFileName(String filename) {
        return UUID.randomUUID().toString() + "_" + filename;
    }

    /**
     * 文件名特殊字符过滤
     *
     * @param fileName 文件名
     * @return 过滤后的文件名
     * @throws PatternSyntaxException 正则异常
     */
    public static String StringFilter(String fileName) throws PatternSyntaxException {
        String regEx = "[`~!@#$%^&*+=|{}':; ',//[//]<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(fileName);
        return m.replaceAll("").trim();
    }

    /**
     * 生成Excel文件
     *
     * @param filename 文件名称
     * @param list     文件内容List
     * @param clazz    List中的对象类型
     * @return ResponseBo
     */
    public static ResponseBo createExcelByPOIKit(String filename, List<?> list, Class<?> clazz) {

        if (list.isEmpty()) {
            return ResponseBo.warn("导出数据为空！");
        } else {
            boolean operateSign = false;
            String fileName = filename + ".xlsx";
            fileName = makeFileName(fileName);
            try {
                File fileDir = new File("file");
                if (!fileDir.exists())
                    fileDir.mkdir();
                String path = "file/" + fileName;
                FileOutputStream fos;
                fos = new FileOutputStream(path);
                operateSign = ExcelUtils.$Builder(clazz)
                        // 设置每个sheet的最大记录数,默认为10000,可不设置
                        // .setMaxSheetRecords(10000)
                        .toExcel(list, "查询结果", fos);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if (operateSign) {
                return ResponseBo.ok(fileName);
            } else {
                return ResponseBo.error("导出Excel失败，请联系网站管理员！");
            }
        }
    }

    /**
     * 生成Csv文件
     *
     * @param filename 文件名称
     * @param list     文件内容List
     * @param clazz    List中的对象类型
     * @return ResponseBo
     */
    public static ResponseBo createCsv(String filename, List<?> list, Class<?> clazz) {

        if (list.isEmpty()) {
            return ResponseBo.warn("导出数据为空！");
        } else {
            boolean operateSign;
            String fileName = filename + ".csv";
            fileName = makeFileName(fileName);

            File fileDir = new File("file");
            if (!fileDir.exists())
                fileDir.mkdir();
            String path = "file/" + fileName;
            operateSign = ExcelUtils.$Builder(clazz)
                    .toCsv(list, path);
            if (operateSign) {
                return ResponseBo.ok(fileName);
            } else {
                return ResponseBo.error("导出Csv失败，请联系网站管理员！");
            }
        }
    }

    /**
     * 文件上传
     * @param file
     * @param filePath
     * @param fileName
     * @throws Exception
     */
    public static String uploadFile(byte[] file, String filePath, String fileName) throws IOException {
        File targetFile = new File(filePath);
        if(!targetFile.exists()){
            targetFile.mkdirs();
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(filePath+fileName);
            out.write(file);
            out.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (out!=null)
            out.close();
        }
        return targetFile.getAbsolutePath();
    }
    /**
     * Description: 将pdf文件转换为Base64编码
     */
    public static String PDFToBase64(File file) {
        BASE64Encoder encoder = new BASE64Encoder();
        FileInputStream fin =null;
        BufferedInputStream bin =null;
        ByteArrayOutputStream baos = null;
        BufferedOutputStream bout =null;
        try {
            fin = new FileInputStream(file);
            bin = new BufferedInputStream(fin);
            baos = new ByteArrayOutputStream();
            bout = new BufferedOutputStream(baos);
            byte[] buffer = new byte[1024];
            int len = bin.read(buffer);
            while(len != -1){
                bout.write(buffer, 0, len);
                len = bin.read(buffer);
            }
            //刷新此输出流并强制写出所有缓冲的输出字节
            bout.flush();
            byte[] bytes = baos.toByteArray();
            return encoder.encodeBuffer(bytes).trim();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                fin.close();
                bin.close();
                bout.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static PDDocument mergePdf(String[] files,String[] attribute) throws IOException {
        PDFMergerUtility pdfmerger = new PDFMergerUtility();
        if (null==files||files.length==0)
            throw new NoSuchFileException("文件为空");
        File file =null;
        PDDocument doc =null;
        PDDocument docRes =new PDDocument();
        for (int i=0;i<files.length;i++){
            try {
                file = new File(files[i]);
                doc =PDDocument.load(file);
                doc = FileUtils.addHeadText(doc,attribute[i]);
                pdfmerger.appendDocument(docRes,doc);
            } catch (IOException e) {
                throw new IOException("文件加载失败");
            }finally {
                if (null!=doc)
                    doc.close();
            }
        }
        return docRes;
    }

    private static PDDocument addHeadText(PDDocument doc, String s) throws IOException {
        // Creating a PDF Document
        PDPage page = doc.getPage(0);
        PDPageContentStream cs = null;
        try {
            cs = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, true, true);
            // Begin the Content stream
            //cs.beginText();
            // Setting the font to the Content stream
            //Font times = new Font(, 12f);
            //String fontPath=Thread.currentThread().getContextClassLoader().getResource("static/fonts/timesbd.ttf").getPath();
            //PDFont font = PDType0Font.load(doc, new File(fontPath));
            //PDFont font = PDType1Font.TIMES_ROMAN;
            float h=page.getMediaBox().getHeight();
            float w=page.getMediaBox().getWidth();
            float fontSize = 20.0f;
            PDResources resources = page.getResources();
            PDExtendedGraphicsState r0 = new PDExtendedGraphicsState();
            // 透明度
            r0.setNonStrokingAlphaConstant(0.2f);
            r0.setAlphaSourceFlag(true);
            cs.setGraphicsStateParameters(r0);
            cs.setNonStrokingColor(200,0,0);//Red
            cs.beginText();
            cs.setFont(font, fontSize);
            // 获取旋转实例
            cs.setTextMatrix(Matrix.getRotateInstance(0,w-100,h-50));
            cs.showText(s);


            //contentStream.drawString(s);
            cs.endText();
        } catch (IOException e) {
            throw new IOException("向PDF添加文本失败:"+e.getMessage());
        }finally {
            if (null!=cs)
                // Closing the content stream
                cs.close();
        }

        return doc;
    }

    public static void main (String[] args) throws IOException {
        String[] files=new String[]{"E:\\PDF测试.pdf","E:\\PDF测试.pdf","E:\\PDF测试.pdf"};
        String[] att=new String[]{"SSSSSSS","SSSSSSS","SSSSSSS"};
        PDDocument document = null;
        try {
            document = FileUtils.mergePdf(files,att);
            document.save("E:\\PDF合并.pdf");
        } catch (IOException e) {
            if (null!=document)
                document.close();
            e.printStackTrace();
        }
    }
}
