package com.houchaojie.blog.util.other;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.io.StringWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 侯超杰
 */
public class HtmlFilterTag extends SimpleTagSupport {
    /**
     *截取字符串长度
     */
    private static final int SUB_LENGTH = 100;
    /**
     *定义script的正则表达式
     */
    private static final String REG_EX_SCRIPT = "<script[^>]*?>[\\s\\S]*?<\\/script>";
    /**
     *定义style的正则表达式
     */
    private static final String REG_EX_STYLE = "<style[^>]*?>[\\s\\S]*?<\\/style>";
    /**
     *定义HTML标签的正则表达式
     */
    private static final String REG_EX_HTML = "<[^>]+>";
    /**
     *定义空格回车换行符
     */
    private static final String REG_EX_SPACE = "\\s*|\t|\r|\n";
    public static String filter(String htmlStr) {
        htmlStr = getString( htmlStr, REG_EX_SCRIPT, REG_EX_STYLE );

        htmlStr = getString( htmlStr, REG_EX_HTML, REG_EX_SPACE );
        // 返回文本字符串
        return htmlStr.trim();
    }

    private static String getString(String htmlStr, String regexScript, String regexStyle) {
        Pattern pScript = Pattern.compile( regexScript, Pattern.CASE_INSENSITIVE);
        Matcher mScript = pScript.matcher(htmlStr);
        // 过滤script标签
        htmlStr = mScript.replaceAll("");

        Pattern pStyle = Pattern.compile( regexStyle, Pattern.CASE_INSENSITIVE);
        Matcher mStyle = pStyle.matcher(htmlStr);
        // 过滤style标签
        htmlStr = mStyle.replaceAll("");
        return htmlStr;
    }

    @Override
    public void doTag() throws JspException, IOException {

        StringWriter sw = new StringWriter();
        JspFragment jf = this.getJspBody();
        jf.invoke(sw);
        String content = sw.getBuffer().toString();
        content = filter(content);
        content = content.replaceAll(" ", "");
        int contentLength =content.length();
        if(contentLength> SUB_LENGTH) {
            content = content.substring(0, SUB_LENGTH );
        } else {
            content = content.substring(0,contentLength);
        }
        this.getJspContext().getOut().write(content);
    }

}
