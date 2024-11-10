package com.bestbigkk.generation;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import lombok.SneakyThrows;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author: 开
* @date: 2020-03-24 16:04:13
* @describe: MyBatisPlus代码生成器
*/
public class MpGenerator {

    //config
    private static final String FILE_OUT_PATH = MpGenerator.class.getResource("").getPath().replace( "target/test-classes/com/bestbigkk/generation/", "src/test/java").substring(1);
    private static final String AUTHOR = "xugongkai";
    //db
    private static final String DB_TYPE = "com.mysql.cj.jdbc.Driver";
    public static final String DB_URL = "jdbc:mysql://cdb-41wrstjc.bj.tencentcdb.com:10187/xugongkai_bbk_mss?useSSL=true&autoReconnect=true&serverTimezone=Asia/Shanghai";
    public static final String DB_USER = "bestbigkk";
    public static final String DB_USER_PASSWORD = "KK131421!!!";
    //表前缀, 如t_user, 则前缀为 t_
    public static final String[] DB_TABLE_PREFIX = {"t_"};
    //要生成的表,如t_user, 则也必须输入 t_user, 这里不可以省略前缀。
    public static final String[] DB_TARGET_TABLE_NAME = {"t_approval", "t_transport"};
    public static final String PACKAGE = "com.bestbigkk";

    @Test
    public void generator() {

        AutoGenerator mpg = new AutoGenerator();
        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        //输出目录
        gc.setOutputDir(FILE_OUT_PATH+File.separator+"dist");
        gc.setFileOverride(true);
        gc.setActiveRecord(true);// 不需要ActiveRecord特性的请改为false
        gc.setEnableCache(false);// XML 二级缓存
        gc.setBaseResultMap(true);// XML ResultMap
        gc.setBaseColumnList(true);// XML columList
        // .setKotlin(true) 是否生成 kotlin 代码
        gc.setAuthor(AUTHOR);
        gc.setSwagger2(true);
        //是否覆盖文件
        gc.setFileOverride(true);

        // 自定义文件命名，注意 %s 会自动填充表实体属性！
         gc.setMapperName("%sDao");
         gc.setXmlName("%sDao");
         gc.setEntityName("%sPO");
        // gc.setServiceName("MP%sService");
        // gc.setServiceImplName("%sServiceDiy");
        // gc.setControllerName("%sAction");
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL);
        dsc.setTypeConvert(new MySqlTypeConvert());
        dsc.setDriverName(DB_TYPE);
        dsc.setUsername(DB_USER);
        dsc.setPassword(DB_USER_PASSWORD);
        dsc.setUrl(DB_URL);
        mpg.setDataSource(dsc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        // strategy.setCapitalMode(true);// 全局大写命名 ORACLE 注意
        strategy.setTablePrefix(DB_TABLE_PREFIX);// 此处可以修改为您的表前缀
        strategy.setNaming(NamingStrategy.underline_to_camel);// 表名生成策略
        strategy.setInclude(DB_TARGET_TABLE_NAME); // 需要生成的表
        strategy.setEntityLombokModel(true);//Lombok
        // strategy.setExclude(new String[]{"test"}); // 排除生成的表
        // 自定义实体父类
        //strategy.setSuperEntityClass("com.baomidou.demo.TestEntity");
        // 自定义实体，公共字段
        // strategy.setSuperEntityColumns(new String[] { "test_id", "age" });
        // 自定义 mapper 父类
        // strategy.setSuperMapperClass("com.baomidou.demo.TestMapper");
        // 自定义 service 父类
        // strategy.setSuperServiceClass("com.baomidou.demo.TestService");
        // 自定义 service 实现类父类
        // strategy.setSuperServiceImplClass("com.baomidou.demo.TestServiceImpl");
        // 自定义 controller 父类
        // strategy.setSuperControllerClass("com.baomidou.demo.TestController");
        // 【实体】是否生成字段常量（默认 false）
        // public static final String ID = "test_id";
        // strategy.setEntityColumnConstant(true);
        // 【实体】是否为构建者模型（默认 false）
        // public User setName(String name) {this.name = name; return this;}
        // strategy.setEntityBuilderModel(true);
        mpg.setStrategy(strategy);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent(PACKAGE);

        pc.setController("web.controller");
        pc.setEntity("persistence.entity");
        pc.setMapper("persistence.dao");
        pc.setXml("xml");

        mpg.setPackageInfo(pc);

        //自定义模板
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() { }
        };
        mpg.setCfg(cfg);

        // 执行生成
        mpg.execute();

        System.out.println("输出目录："+FILE_OUT_PATH);

    }
}
