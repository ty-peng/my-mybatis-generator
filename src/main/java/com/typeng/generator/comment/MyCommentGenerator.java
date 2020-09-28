package com.typeng.generator.comment;

import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Properties;
import java.util.Set;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.InnerClass;
import org.mybatis.generator.api.dom.java.InnerEnum;
import org.mybatis.generator.api.dom.java.JavaElement;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.internal.util.StringUtility;

/**
 * 注释生成器.
 *
 * @author ty-peng
 * @date 2020-09-27 17:14
 * @see org.mybatis.generator.internal.DefaultCommentGenerator
 */
public class MyCommentGenerator implements CommentGenerator {

    private Properties properties = new Properties();
    private boolean suppressDate = false;
    private boolean suppressAllComments = false;
    private boolean addRemarkComments = false;
    private String since;
    private String author;
    private SimpleDateFormat dateFormat;

    public MyCommentGenerator() {}

    @Override
    public void addJavaFileComment(CompilationUnit compilationUnit) {}

    @Override
    public void addComment(XmlElement xmlElement) {
        if (!this.suppressAllComments) {
            StringBuilder sb = new StringBuilder();
            sb.append("<!-- ");
            sb.append("@mbg.generated ");
            String s = this.getDateString();
            if (s != null) {
                sb.append(s);
            }
            sb.append(" -->");
            xmlElement.addElement(new TextElement(sb.toString()));
        }
    }

    @Override
    public void addRootComment(XmlElement rootElement) {}

    @Override
    public void addConfigurationProperties(Properties properties) {
        this.properties.putAll(properties);
        this.suppressDate = StringUtility.isTrue(properties.getProperty("suppressDate"));
        this.suppressAllComments = StringUtility.isTrue(properties.getProperty("suppressAllComments"));
        this.addRemarkComments = StringUtility.isTrue(properties.getProperty("addRemarkComments"));
        this.since = properties.getProperty("since");
        this.author = properties.getProperty("author");
        String dateFormatString = properties.getProperty("dateFormat");
        if (StringUtility.stringHasValue(dateFormatString)) {
            this.dateFormat = new SimpleDateFormat(dateFormatString);
        }

    }

    @Override
    public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable) {
        this.addClassComment(innerClass, introspectedTable, false);
    }

    @Override
    public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable, boolean markAsDoNotDelete) {
        if (!this.suppressAllComments) {
            this.addDocHeader(innerClass);
            this.addDocTableInfo(innerClass, introspectedTable);
            this.addDocDateTag(innerClass, markAsDoNotDelete);
            this.addDocSinceTag(innerClass);
            this.addDocFooter(innerClass);
        }
    }

    @Override
    public void addModelClassComment(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        if (!this.suppressAllComments) {
            this.addDocHeader(topLevelClass);
            this.addDocDbRemarks(topLevelClass, introspectedTable.getRemarks());
            this.addDocTableInfo(topLevelClass, introspectedTable);
            this.addDocBlankLine(topLevelClass);
            this.addDocAuthorTag(topLevelClass);
            this.addDocDateTag(topLevelClass, false);
            this.addDocSinceTag(topLevelClass);
            this.addDocFooter(topLevelClass);
        }
    }

    @Override
    public void addEnumComment(InnerEnum innerEnum, IntrospectedTable introspectedTable) {
        if (!this.suppressAllComments) {
            this.addDocHeader(innerEnum);
            this.addDocTableInfo(innerEnum, introspectedTable);
            this.addDocDateTag(innerEnum, false);
            this.addDocFooter(innerEnum);
        }
    }

    @Override
    public void addFieldComment(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
        if (!this.suppressAllComments) {
            this.addDocHeader(field);
            this.addDocDbRemarks(field, introspectedColumn.getRemarks());
            this.addDocBlankLine(field);
            this.addDocDateTag(field, false);
            this.addDocSinceTag(field);
            this.addDocFooter(field);
        }
    }

    @Override
    public void addFieldComment(Field field, IntrospectedTable introspectedTable) {
        if (!this.suppressAllComments) {
            this.addDocHeader(field);
            this.addDocDateTag(field, false);
            this.addDocSinceTag(field);
            this.addDocFooter(field);
        }
    }

    @Override
    public void addGeneralMethodComment(Method method, IntrospectedTable introspectedTable) {
        if (!this.suppressAllComments) {
            this.addDocHeader(method);
            this.addDocDateTag(method, false);
            this.addDocFooter(method);
        }
    }

    @Override
    public void addGetterComment(Method method, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {}

    @Override
    public void addSetterComment(Method method, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {}

    @Override
    public void addGeneralMethodAnnotation(Method method, IntrospectedTable introspectedTable, Set<FullyQualifiedJavaType> imports) {
        imports.add(new FullyQualifiedJavaType("javax.annotation.Generated"));
        String comment = "Source Table: " + introspectedTable.getFullyQualifiedTable().toString();
        method.addAnnotation(this.getGeneratedAnnotation(comment));
    }

    @Override
    public void addGeneralMethodAnnotation(Method method, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn,
        Set<FullyQualifiedJavaType> imports) {
        imports.add(new FullyQualifiedJavaType("javax.annotation.Generated"));
        String comment =
            "Source field: " + introspectedTable.getFullyQualifiedTable().toString() + "." + introspectedColumn.getActualColumnName();
        method.addAnnotation(this.getGeneratedAnnotation(comment));
    }

    @Override
    public void addFieldAnnotation(Field field, IntrospectedTable introspectedTable, Set<FullyQualifiedJavaType> imports) {
        imports.add(new FullyQualifiedJavaType("javax.annotation.Generated"));
        String comment = "Source Table: " + introspectedTable.getFullyQualifiedTable().toString();
        field.addAnnotation(this.getGeneratedAnnotation(comment));
    }

    @Override
    public void addFieldAnnotation(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn,
        Set<FullyQualifiedJavaType> imports) {
        imports.add(new FullyQualifiedJavaType("javax.annotation.Generated"));
        String comment =
            "Source field: " + introspectedTable.getFullyQualifiedTable().toString() + "." + introspectedColumn.getActualColumnName();
        field.addAnnotation(this.getGeneratedAnnotation(comment));
        if (!this.suppressAllComments && this.addRemarkComments) {
            String remarks = introspectedColumn.getRemarks();
            if (this.addRemarkComments && StringUtility.stringHasValue(remarks)) {
                field.addJavaDocLine("/**");
                String[] remarkLines = remarks.split(System.getProperty("line.separator"));

                for (String remarkLine : remarkLines) {
                    field.addJavaDocLine(" * " + remarkLine);
                }

                field.addJavaDocLine(" */");
            }
        }

    }

    @Override
    public void addClassAnnotation(InnerClass innerClass, IntrospectedTable introspectedTable, Set<FullyQualifiedJavaType> imports) {
        imports.add(new FullyQualifiedJavaType("javax.annotation.Generated"));
        String comment = "Source Table: " + introspectedTable.getFullyQualifiedTable().toString();
        innerClass.addAnnotation(this.getGeneratedAnnotation(comment));
    }

    protected String getDateString() {
        if (this.suppressDate) {
            return null;
        } else {
            return this.dateFormat != null ? this.dateFormat.format(new Date()) : (new Date()).toString();
        }
    }

    protected void addDocHeader(JavaElement javaElement) {
        javaElement.addJavaDocLine("/**");
    }

    protected void addDocBlankLine(JavaElement javaElement) {
        javaElement.addJavaDocLine(" *");
    }

    protected void addDocFooter(JavaElement javaElement) {
        javaElement.addJavaDocLine(" */");
    }

    protected void addDocDbRemarks(JavaElement javaElement, String remarks) {
        if (this.addRemarkComments && StringUtility.stringHasValue(remarks)) {
            String[] remarkLines = remarks.split(System.getProperty("line.separator"));

            for (String remarkLine : remarkLines) {
                remarkLine = remarkLine.replaceAll("表$", "");
                javaElement.addJavaDocLine(" * " + remarkLine + ".");
            }
        }
    }

    protected void addDocDateTag(JavaElement javaElement, boolean markAsDoNotDelete) {
        StringBuilder sb = new StringBuilder();
        sb.append(" * ");
        sb.append("@date");
        String s = this.getDateString();
        if (s != null) {
            sb.append(' ');
            sb.append(s);
        }
        if (markAsDoNotDelete) {
            sb.append(" do_not_delete_during_merge");
        }

        javaElement.addJavaDocLine(sb.toString());
    }

    protected void addDocSinceTag(JavaElement javaElement) {
        if (StringUtility.stringHasValue(since)) {
            javaElement.addJavaDocLine(" * @since " + since);
        }
    }

    protected void addDocAuthorTag(JavaElement javaElement) {
        if (StringUtility.stringHasValue(author)) {
            javaElement.addJavaDocLine(" * @author " + author);
        }
    }

    protected void addDocTableInfo(JavaElement javaElement, IntrospectedTable introspectedTable) {
        String str = " * <p>database table: " + introspectedTable.getFullyQualifiedTable() + "</p>";
        javaElement.addJavaDocLine(str);
    }

    private String getGeneratedAnnotation(String comment) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("@Generated(");
        if (this.suppressAllComments) {
            buffer.append('"');
        } else {
            buffer.append("value=\"");
        }

        buffer.append(MyBatisGenerator.class.getName());
        buffer.append('"');
        if (!this.suppressDate && !this.suppressAllComments) {
            buffer.append(", date=\"");
            buffer.append(DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(ZonedDateTime.now()));
            buffer.append('"');
        }

        if (!this.suppressAllComments) {
            buffer.append(", comments=\"");
            buffer.append(comment);
            buffer.append('"');
        }

        buffer.append(')');
        return buffer.toString();
    }
}
