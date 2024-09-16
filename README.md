# 后端
## sql 执行
执行/sql/init.sql

## 修改数据库
application.yml配置文件里面  
datasource.url  
datasource.username  
datasource.password

# 前端项目
![image](https://github.com/duzengjie/navigation/blob/main/file/主界面1.png)
![image](https://github.com/duzengjie/navigation/blob/main/file/编辑界面.png)
![image](https://github.com/duzengjie/navigation/blob/main/file/新增卡片按钮.png)

## 本地运行
```shell
cd ./front
```

```shell
pnpm install
```

```shell
pnpm run dev
```

# 打包
1. 修改生产数据库配置
2. 运行PackageAll.main
3. 打包文件在target下  navigation.jar  

# 部署
前提得初始化mysql
```shell
java -jar navigation.jar 
```
