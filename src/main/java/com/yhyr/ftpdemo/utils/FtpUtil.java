package com.yhyr.ftpdemo.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yhyr
 * @since 2018/08/22 15:04
 */

public class FtpUtil {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 上传文件
     * 
     * @param ftpClient
     * @param remoteDirPath
     * @param remoteFileName
     * @param uploadFilePath
     * @return
     */
    public boolean uploadFile(FTPClient ftpClient, String remoteDirPath, String remoteFileName, String uploadFilePath) {
        logger.info("Begin upload file: {} to FTP: {}/{}", uploadFilePath, remoteDirPath, remoteFileName);
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(new File(uploadFilePath));
            createRemoteDirecroty(ftpClient, remoteDirPath);
            ftpClient.changeWorkingDirectory(new String(remoteDirPath.getBytes("utf-8"), "iso-8859-1"));
            ftpClient.storeFile(new String(remoteFileName.getBytes("utf-8"), "iso-8859-1"), inputStream);
            inputStream.close();
            logger.info("Upload file: {} Successful", remoteFileName);
            return true;
        } catch (IOException e) {
            logger.error("Upload file: {} Failed", remoteFileName);
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 上传目录
     * 
     * @param ftpClient
     * @param uploadDirPath
     * @param remoteDirPath
     * @return
     */
    public boolean uploadDirectory(FTPClient ftpClient, String uploadDirPath, String remoteDirPath) {
        logger.info("Begin upload local directory: {} to FTP: {}", uploadDirPath, remoteDirPath);
        try {
            File dirFile = new File(uploadDirPath);
            if (dirFile.isDirectory()) {
                createRemoteDirecroty(ftpClient, remoteDirPath);
                ftpClient.changeWorkingDirectory(new String(remoteDirPath.getBytes("utf-8"), "iso-8859-1"));

                File[] fileList = dirFile.listFiles();
                for (File file : fileList) {
                    if (file.isDirectory()) {
                        logger.info("Begin upload sub directory: {}", file.getAbsolutePath());
                        uploadDirectory(ftpClient, file.getAbsolutePath(), remoteDirPath + "/" + file.getName());
                        logger.info("upload sub directory: {} successful", file.getAbsolutePath());
                    } else {
                        uploadFile(ftpClient, remoteDirPath, file.getName(), file.getAbsolutePath());
                    }
                }
                logger.info("Upload local directory: {} successful", uploadDirPath);
            } else {
                logger.info("Upload local directory: {} Failed; Current file is not directory", uploadDirPath);
            }
            return true;
        } catch (IOException e) {
            logger.error("Upload directory: {} Error", uploadDirPath);
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 下载文件到指定的输出流
     * 
     * @param ftpClient
     * @param remoteDirPath
     * @param remoteFileName
     * @param os
     * @return
     */
    public boolean downloadFile(FTPClient ftpClient, String remoteDirPath, String remoteFileName, OutputStream os) {
        boolean flag = false;
        logger.debug("Begin download FTP File: {}/{}", remoteDirPath, remoteFileName);
        try {
            ftpClient.changeWorkingDirectory(new String(remoteDirPath.getBytes("utf-8"), "iso-8859-1"));
            FTPFile[] ftpFiles = ftpClient.listFiles();
            for (FTPFile file : ftpFiles) {
                if (remoteFileName.equals(file.getName())) {
                    ftpClient.retrieveFile(new String(remoteFileName.getBytes("utf-8"), "iso-8859-1"), os);
                    os.close();
                    flag = true;
                }
            }
            logger.debug("FTP File: {}/{} download successful", remoteDirPath, remoteFileName);
        } catch (Exception e) {
            logger.error("FTP File: {}/{} download failed", remoteDirPath, remoteFileName);
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (null != os) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return flag;
    }

    /**
     * 下载文件到指定本地文件
     * 
     * @param ftpClient
     * @param remoteDirPath
     * @param remoteFileName
     * @param localFilePath
     * @return
     */
    public boolean downloadFile(FTPClient ftpClient, String remoteDirPath, String remoteFileName, String localFilePath) {
        boolean flag = false;
        OutputStream os = null;
        logger.debug("Begin download FTP File: {}/{}", remoteDirPath, remoteFileName);
        try {
            String localDirPath = localFilePath.substring(0, localFilePath.lastIndexOf("/"));
            File localDirFile = new File(localDirPath);
            if ((localDirFile.exists() && !localDirFile.isDirectory())) {
                if (!localDirFile.isDirectory()) {
                    localDirFile.delete();
                    localDirFile.mkdirs();
                }
            } else {
                localDirFile.mkdirs();
            }
            os = new FileOutputStream(new File(localFilePath));
            ftpClient.changeWorkingDirectory(new String(remoteDirPath.getBytes("utf-8"), "iso-8859-1"));
            FTPFile[] ftpFiles = ftpClient.listFiles();
            for (FTPFile file : ftpFiles) {
                if (remoteFileName.equals(file.getName())) {
                    ftpClient.retrieveFile(new String(remoteFileName.getBytes("utf-8"), "iso-8859-1"), os);
                    os.close();
                    flag = true;
                }
            }
            logger.debug("FTP File: {}/{} download successful", remoteDirPath, remoteFileName);
        } catch (Exception e) {
            logger.error("FTP File: {}/{} download failed", remoteDirPath, remoteFileName);
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (null != os) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return flag;
    }

    /**
     * 下载目录到本地
     * 
     * @param ftpClient
     * @param remoteDirPath
     * @param localDirPath
     * @return
     */
    public boolean downloadDirectory(FTPClient ftpClient, String remoteDirPath, String localDirPath) {
        boolean flag = false;
        logger.info("Begin download FTP Directory: {} to Local: {}", remoteDirPath, localDirPath);
        try {
            if (!existDir(ftpClient, remoteDirPath)) {
                logger.info("Target Directory: {} is not exist in ftp", remoteDirPath);
                return false;
            }
            FTPFile[] ftpFileArr = ftpClient.listFiles(new String(remoteDirPath.getBytes("utf-8"), "iso-8859-1"));
            logger.info("RemoteDir's file num is: {}", ftpFileArr.length);
            for (FTPFile ftpFile : ftpFileArr) {
                String fileName = ftpFile.getName();
                if (ftpFile.isDirectory()) {
                    downloadDirectory(ftpClient, remoteDirPath + "/" + fileName, localDirPath + "/" + fileName);
                } else {
                    downloadFile(ftpClient, remoteDirPath, fileName, localDirPath + "/" + fileName);
                }
            }
            flag = true;
            logger.info("FTP Directory: {} download successful", remoteDirPath);
        } catch (Exception e) {
            logger.error("FTP Directory: {} download failed", remoteDirPath);
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 删除文件
     * 
     * @param ftpClient
     * @param remoteFilePath
     * @return
     */
    public boolean deleteFile(FTPClient ftpClient, String remoteFilePath) {
        boolean flag = false;
        logger.info("Begin delete FTP File: {}", remoteFilePath);
        try {
            if (remoteFilePath.endsWith("/")
                || !existFile(ftpClient, remoteFilePath.substring(0, remoteFilePath.lastIndexOf("/")),
                    remoteFilePath.substring(remoteFilePath.lastIndexOf("/") + 1, remoteFilePath.length()))) {
                logger.info("Target File: {} is not exist in ftp or is not a file", remoteFilePath);
                return false;
            }
            if (ftpClient.deleteFile(new String(remoteFilePath.getBytes("utf-8"), "iso-8859-1"))) {
                logger.info("Delete FTP file: {} successful", remoteFilePath);
                flag = true;
            } else {
                logger.info("Delete FTP file: {} failed", remoteFilePath);
            }
        } catch (Exception e) {
            logger.error("Delete FTP file: {} failed", remoteFilePath);
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return flag;
    }

    public boolean deleteDirectory(FTPClient ftpClient, String remoteDirPath) {
        boolean flag = false;
        logger.info("Begin delete FTP Directory: {}", remoteDirPath);
        try {
            if (!existDir(ftpClient, remoteDirPath)) {
                logger.info("Target Path: {} is not a directory in ftp; delete failed", remoteDirPath);
                return false;
            }

            FTPFile[] ftpFileArr = ftpClient.listFiles(new String(remoteDirPath.getBytes("utf-8"), "iso-8859-1"));
            for (FTPFile ftpFile : ftpFileArr) {
                String fileName = ftpFile.getName();
                if (ftpFile.isDirectory()) {
                    deleteDirectory(ftpClient, remoteDirPath + "/" + fileName);
                } else {
                    deleteFile(ftpClient, remoteDirPath + '/' + fileName);
                }
            }
            if (ftpClient.listFiles(new String(remoteDirPath.getBytes("utf-8"), "iso-8859-1")).length == 0) {
                if (ftpClient.removeDirectory(new String(remoteDirPath.getBytes("utf-8"), "iso-8859-1"))) {
                    logger.info("Delete Empty Directory: {} successful", remoteDirPath);
                } else {
                    logger.info("Delete Empty Directory: {} failed", remoteDirPath);
                }
            }
            flag = true;
        } catch (Exception e) {
            logger.error("Delete FTP Directory: {} failed", remoteDirPath);
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 递归创建目录 如果存在, 忽略; 如果不存在则创建
     * 
     * @param ftpClient
     * @param remotePath
     * @return
     * @throws IOException
     */
    private void createRemoteDirecroty(FTPClient ftpClient, String remotePath) throws IOException {
        StringBuilder path = new StringBuilder("/");

        if (!remotePath.startsWith(path.toString())) {
            remotePath = path.append(remotePath).toString();
        }
        List<String> list = new ArrayList<>(Arrays.asList(remotePath.split("/")));
        list.remove(0);
        for (String dirName : list) {
            if (existFile(ftpClient, path.toString(), dirName)) {
                // 目录存在
                path.append(dirName);
            } else {
                // 目录不存在, 新建目录
                path.append(dirName);
                makeDirectory(ftpClient, path.toString());
            }
            path.append("/");
        }
    }

    /**
     * 判断ftp目录是否存在
     * 
     * @param ftpClient
     * @param dir
     * @param fileName
     * @return
     * @throws IOException
     */
    private boolean existFile(FTPClient ftpClient, String dir, String fileName) throws IOException {
        FTPFile[] ftpFileArr = ftpClient.listFiles(new String(dir.getBytes("utf-8"), "iso-8859-1"));
        if (ftpFileArr.length > 0) {
            for (FTPFile f : ftpFileArr) {
                if (f.getName().trim().equals(fileName)) {
                    logger.info("Target file: {} is exist in FTP: {}", fileName, dir);
                    return true;
                }
            }
        }
        logger.info("Target file: {} is not exist in FTP: {}", fileName, dir);
        return false;
    }

    private boolean existDir(FTPClient ftpClient, String path) throws IOException {
        String dirPath;
        if (!"/".equals(path) && path.endsWith("/")) {
            path = path.substring(0, path.lastIndexOf("/"));
        }
        if (path.lastIndexOf("/") == 0) {
            return true;
        } else {
            dirPath = path.substring(0, path.lastIndexOf("/"));
        }

        String dirName = path.substring(path.lastIndexOf("/") + 1, path.length());
        FTPFile[] ftpFiles = ftpClient.listDirectories(new String(dirPath.getBytes("utf-8"), "iso-8859-1"));
        for (FTPFile ftpFile : ftpFiles) {
            if (ftpFile.getName().trim().equals(dirName)) {
                logger.info("Target directory: {} is exist in FTP: {}", dirName, dirPath);
                return true;
            }
        }
        return false;
    }

    /**
     * 创建目录
     * 
     * @param dir
     * @return
     */
    private boolean makeDirectory(FTPClient ftpClient, String dir) {
        try {
            if (ftpClient.makeDirectory(new String(dir.getBytes("utf-8"), "iso-8859-1"))) {
                logger.info("Create directory: {} successful", dir);
                return true;
            } else {
                logger.info("Create directory: {} failed", dir);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}