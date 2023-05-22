package mysql;

import java.util.HashMap;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;

/**
 * @Description MySQL代码生成器
 */
public class MysqlGenerator {

	interface Sql {

		String author = "yinmf";//作者
		String tableName = "process_detail";// 需要生成的表名
		String entityPackage = "model";
		String mapperPackage = "dao";
		String mapperXmlPackage = "dao.mapper";
		String iServicePackage = "service";
		String serviceImplPackage = "service.impl";
		//String controllerPackage = "web.common";
	}

	interface Db {

		String url = "jdbc:mysql://localhost:3306/charge_system?characterEncoding=utf8&useSSL=true&serverTimezone=Asia/Shanghai";
		String username = "root";// 数据库用户名
		String password = "123456";// 数据库密码
	}

	interface Cfg {

		//com.course.app.webadmin.upms.model.DormFee
		String projectPath = System.getProperty("user.dir");
		String filePath = "src/main/java/";
		String parentPackage = "com.course.app.webadmin.upms";//父包名
		String outputDir = projectPath + "/src/main/java";
		String parentPath = projectPath.substring(0, projectPath.lastIndexOf("\\")) + "/charge-webadmin";
		//String controllerPath = parentPath + "/" + controlProjectName + filePath + parentPackage.replaceAll("\\.", "/") + "/" + Sql.controllerPackage.replaceAll("\\.", "/");
		String iservicePath = parentPath + "/" + filePath + parentPackage.replaceAll("\\.", "/") + "/"
				+ Sql.iServicePackage.replaceAll("\\.", "/");
		String serviceImplPath = parentPath + "/" + filePath + parentPackage.replaceAll("\\.", "/") + "/"
				+ Sql.serviceImplPackage.replaceAll("\\.", "/");
		String entityPath = parentPath + "/" + filePath + parentPackage.replaceAll("\\.", "/") + "/"
				+ Sql.entityPackage.replaceAll("\\.", "/");
		String mapperPath = parentPath + "/" + filePath + parentPackage.replaceAll("\\.", "/") + "/"
				+ Sql.mapperPackage.replaceAll("\\.", "/");
		//String mapperXmlPath = parentPath + "/" + mapperxmlProjectName + "/src/main/resources/" + Sql.mapperXmlPackage.replaceAll("\\.", "/");
	}

	public static void main(String[] args) {
		FastAutoGenerator.create(Db.url, Db.username, Db.password).globalConfig(builder -> {
			builder.author(Sql.author) // 设置作者
					.disableOpenDir()//禁止打开输出目录
					//.fileOverride()
					.outputDir(Cfg.outputDir);// 指定输出目录
		}).packageConfig(builder -> {
			builder.parent(Cfg.parentPackage) // 设置父包名
					//.controller(Sql.controllerPackage)//Controller 包名
					.service(Sql.iServicePackage)//Service 包名
					.serviceImpl(Sql.serviceImplPackage)//Service Impl 包名
					.entity(Sql.entityPackage)//Entity 包名
					.mapper(Sql.mapperPackage)//Mapper 包名
					//.xml(Sql.mapperXmlPackage)//Mapper XML 包名
					.pathInfo(new HashMap<OutputFile, String>(11) {

						private static final long serialVersionUID = 1L;
						{
							//put(OutputFile.controller, Cfg.controllerPath);
							put(OutputFile.service, Cfg.iservicePath);
							put(OutputFile.serviceImpl, Cfg.serviceImplPath);
							put(OutputFile.entity, Cfg.entityPath);
							put(OutputFile.mapper, Cfg.mapperPath);
							//put(OutputFile.mapperXml, Cfg.mapperXmlPath);
						}
					});
		}).strategyConfig(builder -> {
			//策略配置
			builder.addInclude(Sql.tableName)//增加表匹配
					//entity 策略配置
					.entityBuilder().enableLombok()//开启 lombok 模型
					.idType(IdType.INPUT)//全局主键类型，雪花算法
					.enableTableFieldAnnotation()//开启生成实体时生成字段注解
					.controllerBuilder().enableRestStyle()//RestController
					//mapper 策略配置
					.mapperBuilder().superClass(BaseMapper.class).enableMapperAnnotation()//开启 @Mapper 注解
					.enableBaseResultMap()//启用 BaseResultMap 生成
					.enableBaseColumnList();//启用 BaseColumnList
		}).templateConfig(builder -> {
			//builder.controller("/template/controller.java");
			builder.serviceImpl("/template/serviceImpl.java");
			builder.service("/template/service.java");
			builder.entity("/template/entity.java");
			builder.mapper("/template/mapper.java");
			//builder.mapperXml("/template/mapper.xml");
		}).templateEngine(new VelocityTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
				.execute();
	}
}
