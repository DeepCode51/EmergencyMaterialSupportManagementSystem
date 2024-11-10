package com.bestbigkk.boot;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.bestbigkk.BBKApplication;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
* @author: 开
* @date: 2020-04-19 22:28:26
* @describe: 自动代码生成
*/
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BBKApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AutoCodeGeneration {

    @Test
    public void fun() {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        //用于多个模块下生成到精确的目录下
//        String projectPath = "E:/IDEA WorkSpace/demo_springcoud2/demo_security_oauth2";
        //生成文件的输出目录
        gc.setOutputDir(projectPath + "/src/main/java");
        //开发人员
        gc.setAuthor("pwj");
        //是否打开输出目录
        gc.setOpen(false);
        // 实体属性 Swagger2 注解
        gc.setSwagger2(true);

        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://cdb-41wrstjc.bj.tencentcdb.com:10187/xugongkai_bbk_mss?useSSL=true&autoReconnect=true&serverTimezone=Asia/Shanghai");
        //设置驱动
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        //用户名
        dsc.setUsername("bestbigkk");
        //密码
        dsc.setPassword("KK131421!!!");
        //设置数据源
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        //添加这个后 会以一个实体为一个模块 比如user实体会生成user模块 每个模块下都会生成三层
//        pc.setModuleName(scanner("模块名"));
        pc.setParent("cn.mybatis.plus.test");
        pc.setEntity("domain");
        mpg.setPackageInfo(pc);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() { }
        };

        // 如果模板引擎是 freemarker
        String templatePath = "/templates/mapper.xml.ftl";
        // 如果模板引擎是 velocity
        // String templatePath = "/templates/mapper.xml.vm";

        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
//                return projectPath + "/src/main/resources/mappers/" + pc.getModuleName()
                return projectPath + "/src/main/resources/mapper/"
                        + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;

            }
        });
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();

        // 配置自定义输出模板（如果不配置这些，则会按照官方的配置进行生成）
        //指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
        templateConfig.setEntity("templates/entity2.java");
        templateConfig.setMapper("templates/mapper2.java");
        templateConfig.setController("templates/controller2.java");
        templateConfig.setServiceImpl("templates/serviceImpl2.java");
        //设置为空则代表不需要生成
        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        //数据库表映射到实体的命名策略
        strategy.setNaming(NamingStrategy.underline_to_camel);
        //数据库表字段映射到实体的命名策略, 未指定按照 naming 执行
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        //为实体类添加公共基类
//        strategy.setSuperEntityClass("com.wt.demo01.BaseEntity");
        //实体类采用lombok的形式
        strategy.setEntityLombokModel(true);
        //设置controller为restcontroller
        strategy.setRestControllerStyle(true);
        //【实体】是否生成字段常量（默认 false）
        strategy.setEntityColumnConstant(true);
        //是否生成实体时，生成字段配置 即在字段属性上加上@TableField("")注解
        strategy.setEntityTableFieldAnnotationEnable(true);

        // controller的公共父类
//        strategy.setSuperControllerClass("com.wt.demo01.BaseController");
        // 写于父类中的公共字段
//        strategy.setSuperEntityColumns("id");
        strategy.setInclude(scanner("表名，多个英文逗号分割").split(","));
        //驼峰转连字符
        strategy.setControllerMappingHyphenStyle(true);
        //表前缀
        strategy.setTablePrefix(pc.getModuleName() + "_");
        mpg.setStrategy(strategy);
        //设置模板引擎
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }

    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotEmpty(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }
}
