# 基于Spring Boot的FTP工具类Demo

汇总整理了FTP常用的文件(目录)上传、下载与删除

<font color="red">*支持路径和文件名同时包含中文的情况*</font>

***需要注意的是：在操作FTP文件时(上传、下载、删除)，如果文件名包含中文，需要在代码层面对文件名进行显示的转码操作；否则会因为ftp服务器上的文件编码与给定的不一直而导致操作失败***

