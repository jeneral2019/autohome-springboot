<h1>测试环境部署</h1>

# 数据库配置
## 使用已有数据库
修改 application.properties文件
## docker本地搭建mysql

### docker下载安装
https://www.docker.com/get-started/

### 执行初始化脚本
```shell
# 进入项目autohome.docker/bin下
cd autohome.docker/bin

# 创建mysql-spider镜像（第一次执行）
./deploy_mysql.sh init

# 启动mysql容器
./deploy_mysql.sh start

## 关闭容器
#./deploy_mysql.sh stop
```

# 调试环境是否可用
```shell
mvn clean install -DskipTests
```