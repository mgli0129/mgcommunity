码匠社区
快速运行

    安装必备工具
    JDK，Maven
    克隆代码到本地
    运行命令创建数据库脚本

mvn flyway:migrate

    运行打包命令

mvn package

    运行项目

java -jar target/community-0.0.1-SNAPSHOT.jar

    访问项目

http://localhost:8887

资料

Spring 文档
Spring Web
es
Github deploy key
Bootstrap
Github OAuth
Spring
菜鸟教程
Thymeleaf
Spring Dev Tool
Spring MVC
Markdown 插件
UFfile SDK
Count(*) VS Count(1)
工具

Git
Visual Paradigm
Flyway
Lombok
ctotree
Table of content sidebar
One Tab
Live Reload
Postman
脚本

CREATE TABLE USER
(
    ID int AUTO_INCREMENT PRIMARY KEY NOT NULL,
    ACCOUNT_ID VARCHAR(100),
    NAME VARCHAR(50),
    TOKEN VARCHAR(36),
    GMT_CREATE BIGINT,
    GMT_MODIFIED BIGINT
);

mvn flyway:migrate
mvn -Dmybatis.generator.overwrite=true mybatis-generator:generate

更新日志

    2019-7-30 修复 session 过期时间很短问题
    2019-8-2 修复因为*和+号产生的搜索异常问题
    2019-8-18 添加首页按照最新、最热、零回复排序
    2019-8-18 修复搜索输入 ? 号出现异常问题
    2019-8-22 修复图片大小限制和提问内容为空问题
    2019-9-1 添加动态导航栏
