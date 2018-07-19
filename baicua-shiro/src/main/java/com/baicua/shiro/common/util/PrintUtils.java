package com.baicua.shiro.common.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;
import org.apache.pdfbox.printing.PDFPrintable;
import org.apache.pdfbox.printing.Scaling;

import javax.print.PrintService;
import java.awt.print.*;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class PrintUtils {

    /**
     * 文件名加UUID
     *
     * @return UUID_文件名
     */
    private static String printPdfFileName() throws IOException, PrinterException {
        // 使用打印机的名称
        String printName = "Win32 Printer : Microsoft Print to PDF";
        String pdfPath = "/Users/xutong/Documents/徐通副本.pdf";
        File file = new File(pdfPath);
        // 读取pdf文件
        PDDocument document = PDDocument.load(file);
        // 创建打印任务
        PrinterJob job = PrinterJob.getPrinterJob();
        // 遍历所有打印机的名称
        for (PrintService ps : PrinterJob.lookupPrintServices()) {
            String psName = ps.toString();
            System.out.println(psName);
            // 选用指定打印机
            if (psName.equals(printName)) {
                job.setPrintService(ps);
                break;
            }
        }

        job.setPageable(new PDFPageable(document));

        Paper paper = new Paper();
        // 设置打印纸张大小
        paper.setSize(598,842); // 1/72 inch
        // 设置打印位置 坐标
        paper.setImageableArea(0, 0, paper.getWidth(), paper.getHeight()); // no margins
        // custom page format
        PageFormat pageFormat = new PageFormat();
        pageFormat.setPaper(paper);
        // override the page format
        Book book = new Book();
        // append all pages 设置一些属性 是否缩放 打印张数等
        book.append(new PDFPrintable(document, Scaling.ACTUAL_SIZE), pageFormat, 1);
        job.setPageable(book);
        // 开始打印
        job.print();
        return null;

    }

    public static void main (String[] args) throws IOException, PrinterException {
        // 使用打印机的名称
        printPdfFileName();
    }
}
