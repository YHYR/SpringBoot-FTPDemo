package com.yhyr.ftpdemo;

import com.yhyr.ftpdemo.service.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.ResourceUtils;

import java.io.*;

/**
 * @author YHYR
 */
@SpringBootApplication
public class Application implements CommandLineRunner {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private StorageService storageService;

    @Value("${ftp.host}")
    private String ftpHostName;
    @Value("${ftp.port}")
    private Integer ftpPort;
    @Value("${ftp.user}")
    private String ftpUserName;
    @Value("${ftp.password}")
    private String ftpPassWord;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String[] args) throws InterruptedException, FileNotFoundException {
        /**
         * 上传文件
         */
        String remoteDirPath = "/test/data";
        String remoteFileName = "1.txt";
        String uploadFilePath = ResourceUtils.getFile("classpath:data/1.txt").getAbsolutePath();
        boolean resultFlag =
            storageService.uploadFile(ftpHostName, ftpPort, ftpUserName, ftpPassWord, remoteDirPath, remoteFileName,
                uploadFilePath);
        if (resultFlag) {
            logger.info("Upload Local File: {} successful", uploadFilePath);
        } else {
            logger.info("Upload Local File: {} failed", uploadFilePath);
        }

        /**
         * 上传目录
         */
//        String uploadDirPath = ResourceUtils.getFile("classpath:data").getAbsolutePath();
//        String remoteDirPath = "/test/data";
//        boolean resultFlag =
//            storageService.uploadDir(ftpHostName, ftpPort, ftpUserName, ftpPassWord, uploadDirPath, remoteDirPath);
//        if (resultFlag) {
//            logger.info("Upload Local Dir: {} successful", uploadDirPath);
//        } else {
//            logger.info("Upload Local Dir: {} failed", uploadDirPath);
//        }

        /**
         * 下载文件
         */
//        String remoteDirPath = "/test/data";
//        String remoteFileName = "中文测试.txt";
//        String localFilePath = "F://aa";
//        boolean resultFlag =
//            storageService.downloadFile(ftpHostName, ftpPort, ftpUserName, ftpPassWord, remoteDirPath, remoteFileName,
//                localFilePath);
//
//        if (resultFlag) {
//            logger.info("download file: {} successful", remoteDirPath + "/" + remoteFileName);
//        } else {
//            logger.info("download file: {} failed", remoteDirPath + "/" + remoteFileName);
//        }

        /**
         * 下载目录
         */
//        String remoteDirPath = "/test/data";
//        String localDirPath = "F://bb";
//        boolean resultFlag =
//            storageService.downloadDirectory(ftpHostName, ftpPort, ftpUserName, ftpPassWord, remoteDirPath,
//                localDirPath);
//
//        if (resultFlag) {
//            logger.info("download directory: {} successful", remoteDirPath);
//        } else {
//            logger.info("download directory: {} failed", remoteDirPath);
//        }

        /**
         * 删除文件
         */
//        String remoteFilePath = "/test/data/中文测试.txt";
//        boolean resultFlag = storageService.deleteFile(ftpHostName, ftpPort, ftpUserName, ftpPassWord, remoteFilePath);
//        if (resultFlag) {
//            logger.info("delete file: {} successful", remoteFilePath);
//        } else {
//            logger.info("delete file: {} failed", remoteFilePath);
//        }

        /**
         * 递归删除目录
         */
//        String remoteDirPath = "/test";
//        boolean resultFlag =
//            storageService.deleteDirectory(ftpHostName, ftpPort, ftpUserName, ftpPassWord, remoteDirPath);
//        if (resultFlag) {
//            logger.info("delete directory: {} successful", remoteDirPath);
//        } else {
//            logger.info("delete directory: {} failed", remoteDirPath);
//        }
    }
}
