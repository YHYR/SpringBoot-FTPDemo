package com.yhyr.ftpdemo.service.impl;

import java.io.IOException;
import java.io.OutputStream;

import com.yhyr.ftpdemo.service.StorageService;
import com.yhyr.ftpdemo.utils.FtpUtil;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author yhyr
 * @since 2018/09/07 14:37
 */
@Service
public class StorageServiceImpl implements StorageService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean uploadFile(String host, int port, String userName, String passWord, String remoteDirPath,
        String remoteFileName, String uploadFilePath) {
        FTPClient ftpClient = new FTPClient();
        try {
            logger.info("connecting... ftp server: {}:{}; And logging with user: {}", host, port, userName);
            ftpClient.connect(host, port);
            ftpClient.login(userName, passWord);
            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                logger.error("connect failed... ftp server: {}:{}", host, port);
            }
            logger.info("connect and login successful with: {}:{} => user: {}", host, port, userName);
            ftpClient.setControlEncoding("utf-8");
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            boolean result = new FtpUtil().uploadFile(ftpClient, remoteDirPath, remoteFileName, uploadFilePath);
            ftpClient.logout();
            return result;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException ioe) {
                    logger.error(ioe.getMessage(), ioe);
                    ioe.printStackTrace();
                }
            }
        }
        return false;
    }

    @Override
    public boolean uploadDir(String host, int port, String userName, String passWord, String uploadDirPath,
        String remoteDirPath) {
        FTPClient ftpClient = new FTPClient();
        try {
            logger.debug("connecting... ftp server: {}:{}; And logging with user: {}", host, port, userName);
            ftpClient.connect(host, port);
            ftpClient.login(userName, passWord);
            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                logger.error("connect failed... ftp server: {}:{}", host, port);
            }
            logger.debug("connect and login successful with: {}:{} => user: {}", host, port, userName);
            ftpClient.setControlEncoding("GBK");
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            boolean result = new FtpUtil().uploadDirectory(ftpClient, uploadDirPath, remoteDirPath);

            ftpClient.logout();
            return result;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException ioe) {
                    logger.error(ioe.getMessage(), ioe);
                    ioe.printStackTrace();
                }
            }
        }
        return false;
    }

    @Override
    public boolean downloadFile(String host, int port, String userName, String passWord, String remoteDirPath,
        String remoteFileName, Object parameter) {
        FTPClient ftpClient = new FTPClient();
        try {
            logger.debug("connecting... ftp server: {}:{}; And logging with user: {}", host, port, userName);
            ftpClient.connect(host, port);
            ftpClient.login(userName, passWord);
            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                logger.error("connect failed... ftp server: {}:{}", host, port);
            }
            logger.debug("connect and login successful with: {}:{} => user: {}", host, port, userName);
            ftpClient.setControlEncoding("utf-8");
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            boolean result = false;
            if (parameter instanceof String) {
                result = new FtpUtil().downloadFile(ftpClient, remoteDirPath, remoteFileName, (String)parameter);
            } else if (parameter instanceof OutputStream) {
                result = new FtpUtil().downloadFile(ftpClient, remoteDirPath, remoteFileName, (OutputStream)parameter);
            } else {
                logger.info("download failed; parameter: {} type is not support", parameter);
            }
            ftpClient.logout();
            return result;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException ioe) {
                    logger.error(ioe.getMessage(), ioe);
                    ioe.printStackTrace();
                }
            }
        }
        return false;
    }

    @Override
    public boolean downloadDirectory(String host, int port, String userName, String passWord, String remoteDirPath,
        String localDirPath) {
        boolean result = false;
        FTPClient ftpClient = new FTPClient();
        try {
            logger.debug("connecting... ftp server: {}:{}; And logging with user: {}", host, port, userName);
            ftpClient.connect(host, port);
            ftpClient.login(userName, passWord);
            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                logger.error("connect failed... ftp server: {}:{}", host, port);
            }
            logger.debug("connect and login successful with: {}:{} => user: {}", host, port, userName);
            ftpClient.setControlEncoding("utf-8");
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            result = new FtpUtil().downloadDirectory(ftpClient, remoteDirPath, localDirPath);
            ftpClient.logout();
            return result;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException ioe) {
                    logger.error(ioe.getMessage(), ioe);
                    ioe.printStackTrace();
                }
            }
        }
        return result;
    }

    @Override
    public boolean deleteFile(String host, int port, String userName, String passWord, String remoteFilePath) {
        boolean result = false;
        FTPClient ftpClient = new FTPClient();
        try {
            logger.debug("connecting... ftp server: {}:{}; And logging with user: {}", host, port, userName);
            ftpClient.connect(host, port);
            ftpClient.login(userName, passWord);
            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                logger.error("connect failed... ftp server: {}:{}", host, port);
            }
            logger.debug("connect and login successful with: {}:{} => user: {}", host, port, userName);
            ftpClient.setControlEncoding("utf-8");
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            result = new FtpUtil().deleteFile(ftpClient, remoteFilePath);
            ftpClient.logout();
            return result;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException ioe) {
                    logger.error(ioe.getMessage(), ioe);
                    ioe.printStackTrace();
                }
            }
        }
        return result;
    }

    @Override
    public boolean deleteDirectory(String host, int port, String userName, String passWord, String remoteDirPath) {
        boolean result = false;
        FTPClient ftpClient = new FTPClient();
        try {
            logger.debug("connecting... ftp server: {}:{}; And logging with user: {}", host, port, userName);
            ftpClient.connect(host, port);
            ftpClient.login(userName, passWord);
            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                logger.error("connect failed... ftp server: {}:{}", host, port);
            }
            logger.debug("connect and login successful with: {}:{} => user: {}", host, port, userName);
            ftpClient.setControlEncoding("utf-8");
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            result = new FtpUtil().deleteDirectory(ftpClient, remoteDirPath);
            ftpClient.logout();
            return result;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException ioe) {
                    logger.error(ioe.getMessage(), ioe);
                    ioe.printStackTrace();
                }
            }
        }
        return result;
    }
}
