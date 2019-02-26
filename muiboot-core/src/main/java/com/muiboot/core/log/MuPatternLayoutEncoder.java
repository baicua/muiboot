package com.muiboot.core.log;

import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>Description: </p>
 *
 * @version 1.0 2019/2/26
 */
public class MuPatternLayoutEncoder extends PatternLayoutEncoder {
    private static final String LOG_PRE = "[LOGS";

    private static final String LOG_SUFFIX = "[LOGE";

    public MuPatternLayoutEncoder() {
        super();
    }

    // 重写原有的 doEncode方法 中 converToBytes 中的 入参
    // 也就是 根据 event和格式化好的 txt  进一步处理
    @Override
    public void doEncode(ILoggingEvent event) throws IOException {

        String txt = this.layout.doLayout(event);

        this.outputStream.write(this.convertToBytes(LogEyeLayout(event, txt)));

        if(isImmediateFlush()) {
            this.outputStream.flush();
        }

    }

    protected String LogEyeLayout(ILoggingEvent iLoggingEvent, String originTxt)  {

        Date now=new Date();

        StringBuilder sbuf = addTextPre(iLoggingEvent, now);

        sbuf.append(addTextSuffix(iLoggingEvent, replaceEnter(new StringBuilder(originTxt)), now).toString());
        sbuf.append(System.getProperty("line.separator"));

        return sbuf.toString();
    }

    // 替换orignSb 最后的回车换行
    protected String replaceEnter(StringBuilder orignSb) {

        int index = -1;
        String orignStr = orignSb.toString();
        int orignStrLen = orignStr.length();

        if((index = orignStr.lastIndexOf("\r\n")) > -1 ) {
            // window
            if(orignStrLen - 2 == index) {
                orignStr =  orignStr.substring(0, index);
            }
        } else if((index =orignStr.lastIndexOf("\r")) > -1) {
            // mac
            if(orignStrLen - 1 == index) {
                orignStr = orignStr.substring(0, index);
            }
        } else if((index =orignStr.lastIndexOf("\n")) > -1) {
            // linux
            if(orignStrLen - 1 == index) {
                orignStr = orignStr.substring(0, index);
            }
        }


        return orignStr.toString();
    }

    // 原数据 之后需要加的 字符串
    protected StringBuilder addTextSuffix(ILoggingEvent iLoggingEvent, String txtOrign,  Date now) {

        String trackNumber = "track:\""+ StringUtils.trimToEmpty(LogTrackContext.getTrackNumber())+"\"";
        String level = "LEVEL:\""+getLevelStr(iLoggingEvent)+"\"";
        String className = "CLASS:\""+iLoggingEvent.getLoggerName()+"\"";
        String dateTime="TIME:\""+now.getTime()+"\"";

        StringBuilder tailText = new StringBuilder();
        if(txtOrign != null) {
            tailText.append(txtOrign);
        }

        tailText.append(LOG_SUFFIX);
        tailText.append(" " + dateTime);
        tailText.append(" " + level);
        tailText.append(" " + trackNumber);
        tailText.append(" " + className);

        tailText.append("]");


        return tailText;
    }


    private String getLevelStr(ILoggingEvent event) {
        return event.getLevel().toString();
    }

    // 原数据 之前需要加的 字符串
    protected StringBuilder addTextPre(ILoggingEvent event, Date now) {

        StringBuilder newText = new StringBuilder();
        newText.append(LOG_PRE);
        newText.append("]");

        return newText;
    }

    private byte[] convertToBytes(String s) {
        if(getCharset() == null) {
            return s.getBytes();
        } else {
            try {
                return s.getBytes(getCharset().name());
            } catch (UnsupportedEncodingException var3) {
                throw new IllegalStateException("An existing charset cannot possibly be unsupported.");
            }
        }
    }
}
