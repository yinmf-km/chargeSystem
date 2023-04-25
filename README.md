### 服务接口文档
---
- Knife4j
  - 服务启动后，Knife4j的文档入口地址 [http://localhost:8082/doc.html#/plus](http://localhost:8082/doc.html#/plus)
- Postman
  - 无需启动服务，即可将当前工程的接口导出成Postman格式。在工程的common/common-tools/模块下，找到ExportApiApp文件，并执行main函数。

### 服务启动环境依赖
---

执行docker-compose up -d 命令启动下面依赖的服务。
执行docker-compose down 命令停止下面服务。

- Redis
  - 版本：4
  - 端口: 6379
  - 推荐客户端工具 [AnotherRedisDesktopManager](https://github.com/qishibo/AnotherRedisDesktopManager)
- Minio
  - 版本：7.0.2
  - 控制台URL：需要配置Nginx，将请求导入到我们缺省设置的19000端口，之后可通过浏览器操作minio。
  - 缺省用户名密码：admin/admin123456
- XXL-Job (可选，仅当启动Job服务时使用)
  - 重要声明：xxl_job 官网仅提供对mysql的支持，如果您在橙单中选择了postgresql，xxl-job-admin仍需依赖mysql。
  - 版本：2.2.0
  - 控制台URL：localhost:8081/xxl-job-admin/
  - 注意：该服务缺省端口为8080，容易冲突，所以改为了8081。我们在所有的配置中均使用了8081，而非8080。
  - 用户名密码：admin/123456
