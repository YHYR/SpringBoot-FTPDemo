package com.yhyr.ftpdemo.service;

/**
 * @author yhyr
 * @since 2018/09/07 14:37
 */
public interface StorageService {

    /**
     * 存储文件
     * 
     * @param host: 存储服务地址
     * @param port: 存储服务端口
     * @param userName: 存储服务用户名
     * @param passWord: 存储服务密码
     * @param remoteDirPath: 远端存储目录
     * @param remoteFileName: 远端存储文件名
     * @param uploadFilePath: 待上传文件路径
     * @return
     */
    boolean uploadFile(String host, int port, String userName, String passWord, String remoteDirPath,
        String remoteFileName, String uploadFilePath);

    /**
     * 存储目录
     * 
     * @param host: 存储服务地址
     * @param port: 存储服务端口
     * @param userName: 存储服务用户名
     * @param passWord: 存储服务密码
     * @param uploadDirPath: 待上传目录
     * @param remoteDirPath: 远端存储目录
     * @return
     */
    boolean uploadDir(String host, int port, String userName, String passWord, String uploadDirPath,
        String remoteDirPath);

    /**
     * 下载文件到本地文件
     * 
     * @param host
     * @param port
     * @param userName
     * @param passWord
     * @param remoteDirPath
     * @param remoteFileName
     * @param parameter: 可以为String或者OutputStream
     * @return
     */
    boolean downloadFile(String host, int port, String userName, String passWord, String remoteDirPath,
        String remoteFileName, Object parameter);

    /**
     * 下载目录; 递归下载子目录下的文件
     * 
     * @param host
     * @param port
     * @param userName
     * @param passWord
     * @param remoteDirPath
     * @param localDirPath
     * @return
     */
    boolean downloadDirectory(String host, int port, String userName, String passWord, String remoteDirPath,
        String localDirPath);

    /**
     * 删除文件
     * 
     * @param host
     * @param port
     * @param userName
     * @param passWord
     * @param remoteFilePath
     * @return
     */
    boolean deleteFile(String host, int port, String userName, String passWord, String remoteFilePath);

    /**
     * 删除目录; 递归删除子目录
     * 
     * @param host
     * @param port
     * @param userName
     * @param passWord
     * @param remoteDirPath
     * @return
     */
    boolean deleteDirectory(String host, int port, String userName, String passWord, String remoteDirPath);
}
