# Recommendation of City Room

## 系统概述
从大众参与的角度出发，允许用户发布任何自己感兴趣的城市空间，并且所有用户都能参与这个空间信息的建设与完善，基于用户所提供的各种信息，通过协同过滤等推荐算法，产生个性化的推荐结果，使得用户更容易接收到符合自己兴趣的空间信息。

客户端面向安卓平台以及PC端浏览器，以AngularJs作为主要开发框架，使用Cordova实现Hybrid App。
服务器端以Tomcat作为应用服务器，采用Java语言实现。使用Mybatis框架结合Mysql数据库完成数据持久层。
系统整体为C/S架构，实现了良好的前后端分离。

## 目录说明

YourTourSpot文件夹： 客户端实现

​    \www : JS、CSS、HTML等文件

​    \platforms\android\build\outputs\apk：安卓客户端安装包

YSTServer文件夹：服务器端实现

​    \src\NLPIR：中科院NLPIR汉语分词系统SDK

​    \src\recommendation:以基于用户的协同过滤为基础的推荐算法实现

​    \src\services:服务实现

​    \src\servlet:请求处理

​    \src\model:数据模型

​    \src\mapper:mybatis映射

​    \src\filter: SpringMVC拦截




## 环境需求

环境： tomcat7.0， jdk 1.8，android versiion:24+
