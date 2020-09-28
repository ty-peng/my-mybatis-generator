package com.typeng.generator;

import java.io.InputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.mybatis.generator.internal.util.StringUtility;

import com.typeng.generator.util.RenameUtils;

/**
 * App.
 *
 * @author ty-peng
 * @date 2020-09-27 15:53
 */
public class App {

    public static void main(String[] args) throws Exception {
        new App().start();
    }

    private void start() throws Exception {
        // 加载配置
        List<String> warnings = new ArrayList<>();
        ConfigurationParser cp = new ConfigurationParser(warnings);
        InputStream resourceStream = this.getClass().getResourceAsStream("/generatorConfig.xml");
        Configuration config = cp.parseConfiguration(resourceStream);
        Context context = config.getContext("simple");
        // 加载数据源配置
        Properties dbProperties = new Properties();
        dbProperties.load(this.getClass().getResourceAsStream("/db.properties"));
        String urlKey = "url";
        if (StringUtility.stringHasValue(dbProperties.getProperty(urlKey))) {
            context.getJdbcConnectionConfiguration().setConnectionURL(dbProperties.getProperty(urlKey));
        }
        String userKey = "user";
        if (StringUtility.stringHasValue(dbProperties.getProperty(userKey))) {
            context.getJdbcConnectionConfiguration().setUserId(dbProperties.getProperty(userKey));
        }
        String passwordKey = "password";
        if (StringUtility.stringHasValue(dbProperties.getProperty(passwordKey))) {
            context.getJdbcConnectionConfiguration().setPassword(dbProperties.getProperty(passwordKey));
        }
        boolean overwrite = true;
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        // 构建 Generator
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        // 生成
        myBatisGenerator.generate(null);
        // 重命名 sqlmap.xml
        String sqlMapProjectPath = context.getSqlMapGeneratorConfiguration().getTargetProject();
        String sqlMapPackagePath = context.getSqlMapGeneratorConfiguration().getTargetPackage();
        RenameUtils.renameSqlMapXml(Paths.get(sqlMapProjectPath, sqlMapPackagePath));
    }
}
