package com.gome.redline.utils.io;

import com.gome.redline.utils.Constant;
import com.gome.redline.utils.common.NumberUtils;
import com.gome.redline.utils.common.PropertiesUtils;
import com.gome.redline.utils.common.StringUtils;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialClob;
import javax.sql.rowset.serial.SerialException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Created by Lanxiaowei at 2017/1/20 13:33
 * 文件流操作工具类
 */
public class FileUtils {
    private static Logger logger = LogManager.getLogger(FileUtils.class.getName());

    /**
     * 删除整个目录，包含所有子目录和文件
     *
     * @param path
     */
    public static void deleteDirs(String path) {
        File rootFile = new File(path);
        if (!rootFile.exists()) {
            return;
        }
        File[] files = rootFile.listFiles();
        if (null == files) {
            return;
        }
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (file.isDirectory()) {
                deleteDirs(file.getPath());
            } else {
                file.delete();
            }
        }
        rootFile.delete();
    }

    /**
     * 删除指定文件
     *
     * @param filePath
     * @return
     */
    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return false;
        }
        return file.delete();
    }

    /**
     * 文件复制
     *
     * @param is    输入流
     * @param os    输出流
     * @param close 写入之后是否需要关闭OutputStream
     * @throws IOException
     */
    public static int copy(InputStream is, OutputStream os, boolean close) {
        try {
            return IOUtils.copy(is, os);
        } catch (IOException e) {
            logger.error("Executing IOUtils.copy() Occur IOException,please check and fix it.", e);
        } finally {
            if (close) {
                try {
                    if (null != is) {
                        is.close();
                    }
                } catch (IOException e1) {
                    logger.error("Close [InputStream] Occur IOException,please check and fix it.", e1);
                }
                try {
                    if (null != os) {
                        os.close();
                    }
                } catch (IOException e1) {
                    logger.error("Close [OutputStream] Occur IOException,please check and fix it.", e1);
                }
            }
        }
        return 0;
    }

    /**
     * 文件复制
     *
     * @param inputName
     * @param outputName
     * @return
     */
    public static boolean copyFile(String inputName, String outputName) {
        InputStream is = null;
        OutputStream os = null;
        int copyed = 0;
        try {
            is = new FileInputStream(inputName);
            os = new FileOutputStream(outputName);
            copyed = copy(is, os, true);
        } catch (FileNotFoundException e) {
            logger.error("Executing FileInputStream.read() Occur FileNotFoundException,please check the input file{} and the out file are all exists.", inputName, outputName);
        }
        return copyed > 0;
    }

    /**
     * 文件夹复制
     *
     * @param srcFolder
     * @param destFolder
     * @return
     */
    public static boolean copyDirctory(String srcFolder, String destFolder) {
        File srcFile = new File(srcFolder);
        File destFile = new File(destFolder);
        if (!srcFile.exists() || (srcFile.isDirectory() && destFile.isFile())) {
            return false;
        }
        //文件copy到文件
        if (srcFile.isFile() && destFile.isFile()) {
            return copyFile(srcFolder, destFolder);
        }
        //创建目标目录
        if (!destFile.exists() && !destFile.isFile()) {
            destFile.mkdir();
        }
        //文件copy到目录
        if (srcFile.isFile()) {
            String srcFileName = srcFile.getName();
            String destFilePath = wrapFilePath(getFullFilePath(srcFileName, destFolder));
            return copyFile(wrapFilePath(srcFolder), destFilePath);
        }

        //目录copy到目录
        File[] allFiles = srcFile.listFiles();
        String srcName = null;
        String desName = null;
        for (File file : allFiles) {
            srcName = file.getName();
            if (file.isFile()) {
                desName = wrapFilePath(getFullFilePath(srcName, destFolder));
                copyFile(wrapFilePath(file.getAbsolutePath()), desName);
            } else {
                copyDirctory(wrapFilePath(file.getAbsolutePath()), getFullFilePath(srcName, destFolder));
            }
        }
        return true;
    }

    /**
     * 得到某文件夹下的所有文件
     *
     * @param path
     * @return
     */
    public static List<File> getAllFile(String path) {
        File file = new File(path);
        File[] files = file.listFiles();
        List<File> fileList = new ArrayList<File>();
        for (File f : files) {
            if (f.isFile()) {
                fileList.add(f);
            }
        }
        return fileList;
    }

    /**
     * @param @param  path
     * @param @return
     * @return List<File>
     * @throws
     * @Title: getAllFolder
     * @Description: 得到某文件夹下的所有文件夹
     */
    public static List<File> getAllFolder(String path) {
        File file = new File(path);
        File[] files = file.listFiles();
        List<File> folderList = new ArrayList<File>();
        for (File f : files) {
            if (f.isDirectory()) {
                folderList.add(f);
            }
        }
        return folderList;
    }

    /**
     * 读取文件
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    public static String readFile(String filePath) throws IOException {
        InputStreamReader isr = new InputStreamReader(new FileInputStream(new File(filePath)), "UTF-8");
        BufferedReader in = new BufferedReader(isr);
        StringBuffer content = new StringBuffer();
        String line = null;
        while ((line = in.readLine()) != null) {
            content.append(line).append("\n");
        }
        return content.toString().replaceAll("\n$", "");
    }

    /**
     * 读取文件
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static String readFile(File file) throws IOException {
        InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "UTF-8");
        BufferedReader in = new BufferedReader(isr);
        StringBuffer content = new StringBuffer();
        String line = null;
        while ((line = in.readLine()) != null) {
            content.append(line).append("\n");
        }
        return content.toString().replaceAll("\n$", "");
    }

    /**
     * 把字符串以指定编码写入文件，<br/>可以指定写入方式：追加/覆盖
     *
     * @param content  写入的字符串
     * @param filePath 文件保存路径
     * @param charset  写入编码
     * @param append   是否追加
     */
    public static void writeFile(String content, String filePath, String charset, boolean append) {
        BufferedWriter writer = null;
        OutputStream os = null;
        OutputStreamWriter osw = null;
        try {
            os = new FileOutputStream(filePath, append);
            osw = new OutputStreamWriter(os, charset);
            writer = new BufferedWriter(osw);
            writer.write(content);
            writer.flush();
        } catch (IOException e) {
            logger.error("Executing BufferedWriter.write() Occur IOException,please check and fix it.", e);
        } finally {
            try {
                if (null != os) {
                    os.close();
                }
            } catch (IOException e) {
                logger.error("Close [OutputStream] Occur IOException,please check and fix it.", e);
            }
            try {
                if (null != osw) {
                    osw.close();
                }
            } catch (IOException e) {
                logger.error("Close [OutputStreamWriter] Occur IOException,please check and fix it.", e);
            }
            try {
                if (null != writer) {
                    writer.close();
                }
            } catch (IOException e) {
                logger.error("Close [BufferedWriter] Occur IOException,please check and fix it.", e);
            }
        }
    }

    /**
     * 把字符串以指定编码写入文件，<br/>可以指定写入方式：追加/覆盖
     * <br/>默认写入方式为覆盖
     *
     * @param content  写入的字符串
     * @param filePath 文件保存路径
     * @param charset  写入编码
     */
    public static void writeFile(String content, String filePath, String charset) {
        writeFile(content, filePath, charset, false);
    }

    /**
     * 把字符串以指定编码写入文件，<br/>可以指定写入方式：追加/覆盖
     * <br/>默认写入编码为UTF-8
     *
     * @param content  写入的字符串
     * @param filePath 文件保存路径
     * @param append   是否追加
     */
    public static void writeFile(String content, String filePath, boolean append) {
        writeFile(content, filePath, "UTF-8", append);
    }

    /**
     * 把字符串以指定编码写入文件，<br/>可以指定写入方式：追加/覆盖
     * <br/>默认写入方式为覆盖,默认写入编码为UTF-8
     *
     * @param content  写入的字符串
     * @param filePath 文件保存路径
     */
    public static void writeFile(String content, String filePath) {
        writeFile(content, filePath, "UTF-8", false);
    }

    /**
     * 下载文件
     *
     * @param link
     * @param filePath
     * @throws IOException
     */
    public static void download(String link, String filePath) {
        URL url = null;
        try {
            url = new URL(link);
        } catch (MalformedURLException e) {
            logger.error("The provided URL is not illegal,please check and fix it.", e);
        }
        BufferedReader in = null;
        FileOutputStream fos = null;
        String inputLine = null;
        try {
            in = new BufferedReader(new InputStreamReader(
                    url.openStream()));
            fos = new FileOutputStream(filePath);
            while ((inputLine = in.readLine()) != null) {
                fos.write(inputLine.getBytes());
            }
        } catch (IOException e) {
            logger.error("Executing FileOutputStream.write() Occur IOException,please check and fix it.", e);
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.error("Close [BufferedReader] Occur IOException,please check and fix it.", e);
                }
            }
            if (null != fos) {
                try {
                    fos.close();
                } catch (IOException e) {
                    logger.error("Close [FileOutputStream] Occur IOException,please check and fix it.", e);
                }
            }
        }
    }

    /**
     * 获取远程文件的输入流并以字节数组形式返回
     *
     * @param link
     * @return
     */
    public static byte[] getBinaryDataFromURL(String link) {
        BufferedInputStream bis = null;
        HttpURLConnection httpUrl = null;
        URL url = null;
        byte[] bytes = null;
        try {
            url = new URL(link);
            httpUrl = (HttpURLConnection) url.openConnection();
            httpUrl.connect();
            bis = new BufferedInputStream(httpUrl.getInputStream());
            bytes = inputStream2ByteArray(bis);
        } catch (IOException e) {
            logger.error("Executing HttpURLConnection.openConnection() or Construct the [URL] instance Occur IOException,please check your URL or your network doesn't works.", e);
        } finally {
            httpUrl.disconnect();
            return bytes;
        }
    }

    /**
     * 获取远程文件的输入流并以输出流返回
     *
     * @param link
     * @return
     */
    public static OutputStream getOutputStreamFromURL(String link) {
        BufferedInputStream bis = null;
        HttpURLConnection httpUrl = null;
        URL url = null;
        OutputStream os = null;
        try {
            url = new URL(link);
            httpUrl = (HttpURLConnection) url.openConnection();
            httpUrl.connect();
            bis = new BufferedInputStream(httpUrl.getInputStream());
            os = inputStream2OutputStream(bis);
        } catch (IOException e) {
            logger.error("Executing HttpURLConnection.openConnection() or Construct the [URL] instance Occur IOException,please check your URL or your network doesn't works.", e);
        } finally {
            if (null != bis) {
                try {
                    bis.close();
                } catch (IOException e) {
                    logger.error("Close [BufferedInputStream] Occur IOException,please check and fix it.", e);
                }
            }
            httpUrl.disconnect();
            return os;
        }
    }

    /**
     * 字符串转换成Clob
     *
     * @param string
     * @return
     */
    public static Clob string2Clob(String string) {
        if (StringUtils.isEmpty(string)) {
            return null;
        }
        try {
            return new SerialClob(string.toCharArray());
        } catch (SerialException e) {
            return null;
        } catch (SQLException e) {
            return null;
        }
    }

    /**
     * Clob转换成字符串
     *
     * @param clob
     * @return
     */
    public static String clob2String(Clob clob) {
        if (null == clob) {
            return null;
        }
        try {
            return clob.getSubString(1L, (int) clob.length());
        } catch (SQLException e) {
            return null;
        }
    }

    /**
     * 字节数组转换成Clob
     *
     * @param byteArray
     * @param charsetName
     * @return
     */
    public static Clob byteArray2Clob(byte[] byteArray, String charsetName) {
        if (null == byteArray) {
            return null;
        }
        Clob clob = null;
        try {
            String string = new String(byteArray,
                    charsetName == null ? Constant.DEFAULT_CHARSET
                            : charsetName);
            clob = string2Clob(string);
        } catch (UnsupportedEncodingException e) {
            logger.error("the parameter[charsetName] your provided is illegal.", e);
        } finally {
            return clob;
        }
    }

    /**
     * 字节数组转换成Clob(重载) 若不显式指定编码，默认编码为UTF-8
     *
     * @param byteArray
     * @return
     */
    public static Clob byteArray2Clob(byte[] byteArray) {
        return byteArray2Clob(byteArray, null);
    }

    /**
     * Clob转换成字节数组
     *
     * @param clob
     * @return
     */
    public static byte[] clob2ByteArray(Clob clob) {
        if (null == clob) {
            return null;
        }
        InputStream in = null;
        byte[] byteArray = null;
        int length = 0;
        try {
            length = (int) clob.length();
            byteArray = new byte[length];
            in = clob.getAsciiStream();
        } catch (SQLException e) {
            return null;
        }
        int offset = 0;
        int n = 0;
        try {
            do {
                n = in.read(byteArray, offset, length - offset);
            } while (n != -1);
        } catch (IOException e) {
            logger.error("Executing InputStream.read() Occur IOException,please check and fix it.", e);
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                logger.error("Close [InputStream] Occur IOException,please check and fix it.", e);
            }
        }
        return byteArray;
    }

    /**
     * 字节数组转换成Blob
     *
     * @param byteArray
     * @return
     * @throws SQLException
     * @throws SerialException
     */
    public static Blob byteArray2Blob(byte[] byteArray) {
        if (null == byteArray) {
            return null;
        }
        try {
            return new SerialBlob(byteArray);
        } catch (SerialException e) {
            return null;
        } catch (SQLException e) {
            return null;
        }
    }

    /**
     * Blob转换成字节数组
     *
     * @param blob
     * @return
     */
    public static byte[] blob2ByteArray(Blob blob) {
        BufferedInputStream is = null;
        byte[] bytes = null;
        try {
            is = new BufferedInputStream(blob.getBinaryStream());
            bytes = new byte[(int) blob.length()];
            int len = bytes.length;
            int offset = 0;
            int read = 0;
            while (offset < len
                    && (read = is.read(bytes, offset, len - offset)) != -1) {
                offset += read;
            }
        } catch (SQLException e) {
            return null;
        } catch (IOException e) {
            logger.error("Executing BufferedInputStream.read() Occur IOException,please check and fix it.", e);
        } finally {
            try {
                is.close();
                is = null;
            } catch (IOException e) {
                logger.error("Close [BufferedInputStream] Occur IOException,please check and fix it.", e);
            }
            return bytes;
        }
    }

    /**
     * 字节数组转换成输入流
     *
     * @param byteArray 字节数组
     * @return
     */
    public static InputStream byteArray2InputStream(byte[] byteArray) {
        return new ByteArrayInputStream(byteArray);
    }

    /**
     * 输入流转换成字节数组
     *
     * @param is 输入流对象
     * @return
     * @throws IOException
     */
    public static byte[] inputStream2ByteArray(InputStream is) {
        ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
        byte imgdata[] = null;
        int ch = 0;
        try {
            while ((ch = is.read()) != -1) {
                bytestream.write(ch);
            }
            imgdata = bytestream.toByteArray();
        } catch (IOException e) {
            logger.error("Executing InputStream.read()/ByteArrayOutputStream.toByteArray() Occur IOException,please check and fix it.", e);
        } finally {
            if (null != bytestream) {
                try {
                    bytestream.close();
                } catch (IOException e1) {
                    logger.error("Close [ByteArrayOutputStream] Occur IOException,please check and fix it.", e1);
                }
            }
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e1) {
                    logger.error("Close [InputStream] Occur IOException,please check and fix it.", e1);
                }
            }
            return imgdata;
        }
    }

    /**
     * String转换成输入流
     *
     * @param text
     * @param charset
     * @return
     */
    public static InputStream string2InputStream(String text, String charset) {
        try {
            return new ByteArrayInputStream(text.getBytes(charset));
        } catch (UnsupportedEncodingException e) {
            logger.error("The incoming parameter[charset] is not a illegal Charset,the correct one like 'iso8859-1/utf8/gbk/gb2312' and so on.please check and fix it.", e);
        }
        return null;
    }

    /**
     * 输入流转换成String(速度快但耗内存)
     * 此方法是一次读一行，所以100%不会出现中文乱码，除非你内容自身就是乱码
     *
     * @param is
     * @return
     * @throws IOException
     */
    public static String inputStream2String(InputStream is) {
        if (null == is) {
            return null;
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        StringBuffer buffer = new StringBuffer();
        String line = "";
        try {
            while ((line = in.readLine()) != null) {
                buffer.append(line).append("\n");
            }
        } catch (IOException e) {
            logger.error("Executing InputStream.readLine() Occur IOException,please check and fix it.", e);
        }
        return buffer.toString();
    }

    /**
     * 输入流转换成String(可以指定编码)
     * 此方法是一次读一行，所以100%不会出现中文乱码，除非你内容自身就是乱码
     *
     * @param is
     * @return
     * @throws IOException
     */
    public static String inputStream2String(InputStream is, String charsetName) {
        if (null == is) {
            return null;
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(is, (null == charsetName || charsetName.length() == 0) ? Charset.defaultCharset() : Charset.forName(charsetName)));
        StringBuffer buffer = new StringBuffer();
        String line = "";
        try {
            while ((line = in.readLine()) != null) {
                buffer.append(line).append("\n");
            }
        } catch (IOException e) {
            logger.error("Executing InputStream.readLine() Occur IOException,please check and fix it.", e);
        }
        return buffer.toString();
    }

    /**
     * 输入流转换成String(消耗资源少但速度慢)
     * 对于中文，可能出现中文乱码，因为一个中文汉字占3个字节，比如刚好读到4094个字节都正常，此时缓冲区只剩2个字节，
     * 而假如下一个字符刚好是汉字，那就杯具了，乱码就产生了。
     *
     * @param is
     * @return
     * @throws IOException
     */
    public static String inputStreamToString(InputStream is) {
        if (null == is) {
            return null;
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] bt = new byte[4096];
        int i = -1;
        try {
            while ((i = is.read(bt)) > 0) {
                bos.write(bt, 0, i);
            }
        } catch (IOException e) {
            logger.error("Executing InputStream.read() Occur IOException,please check and fix it.", e);
        }
        return bos.toString();
    }

    /**
     * 文件转换成输入流
     *
     * @param file
     * @return
     */
    public static InputStream file2InputStream(File file) {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            logger.error("Construct FileInputStream Object Occur IOException,please check the target file{} is exists.", file.getAbsolutePath());
        }
        return null;
    }

    /**
     * 输入流写入文件,默认是追加写入
     *
     * @param is       输入流
     * @param filePath 文件保存目录路径
     * @throws IOException
     */
    public static void write2File(InputStream is, String filePath)
            throws IOException {
        write2File(is, filePath, true);
    }

    /**
     * 输入流写入文件
     *
     * @param is       输入流
     * @param filePath 文件保存目录路径
     * @param append   是否追加写入，如果你需要追加写入，请将append参数设置为true
     * @throws IOException
     */
    public static void write2File(InputStream is, String filePath, boolean append) {
        OutputStream os = null;
        try {
            os = new FileOutputStream(filePath, append);
            int len = 8192;
            byte[] buffer = new byte[len];
            while ((len = is.read(buffer, 0, len)) != -1) {
                os.write(buffer, 0, len);
            }
        } catch (FileNotFoundException e) {
            logger.error("Construct FileInputStream Object Occur IOException,please check the target file{} is exists.", filePath);
        } catch (IOException e) {
            logger.error("Executing InputStream.read() Occur IOException,please check and fix it.", e);
        } finally {
            try {
                os.close();
            } catch (IOException e1) {
                logger.error("Close [FileOutputStream] Occur IOException,please check and fix it.", e1);
            }
            try {
                is.close();
            } catch (IOException e1) {
                logger.error("Close [InputStream] Occur IOException,please check and fix it.", e1);
            }
        }
    }

    /**
     * 获取文件的完整路径
     *
     * @param fileName 文件名称
     * @param filePath 文件保存路径
     * @return
     */
    public static String getFullFilePath(String fileName, String filePath) {
        if (StringUtils.isEmpty(filePath)
                || StringUtils.isEmpty(fileName)) {
            return null;
        }
        filePath = wrapFilePath(filePath);
        return filePath + fileName;
    }

    /**
     * 转换文件路径中的\\为/
     *
     * @param filePath
     * @return
     */
    public static String wrapFilePath(String filePath) {
        if (filePath.split("\\\\").length > 1) {
            filePath = filePath.replace("\\", "/");
        }
        if (!filePath.endsWith("/")) {
            filePath = filePath + "/";
        }
        return filePath;
    }

    /**
     * 从文件路径中获取文件所在路径
     *
     * @param fullPath 文件全路径
     * @return 文件所在路径
     */
    public static String getFileDir(String fullPath) {
        int iPos1 = fullPath.lastIndexOf("/");
        int iPos2 = fullPath.lastIndexOf("\\");
        if (-1 == iPos1 && -1 == iPos2) {
            return fullPath;
        }
        iPos1 = (iPos1 > iPos2 ? iPos1 : iPos2);
        return fullPath.substring(0, iPos1 + 1);
    }

    /**
     * 从文件路径中获取文件名称(包含后缀名)
     *
     * @param fullPath
     * @return
     */
    public static String getFileName(String fullPath) {
        if (StringUtils.isEmpty(fullPath)) {
            return "";
        }
        int iPos1 = fullPath.lastIndexOf("/");
        int iPos2 = fullPath.lastIndexOf("\\");
        if (-1 == iPos1 && -1 == iPos2) {
            return fullPath;
        }
        iPos1 = (iPos1 > iPos2 ? iPos1 : iPos2);
        return fullPath.substring(iPos1 + 1);
    }

    /**
     * 从URL链接中提取文件名称
     *
     * @param url
     * @return
     */
    public static String getFileNameFromUrl(String url) {
        if (StringUtils.isEmpty(url)) {
            return "";
        }
        if (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }
        url = url.replaceAll("(?:http|https)://www\\.([\\s\\S]*)", "$1");
        return getFileName(url);
    }

    /**
     * 从文件路径中获取文件名称(去除后缀名)
     *
     * @param fullPath
     * @return
     */
    public static String getPureFileName(String fullPath) {
        String fileFullName = getFileName(fullPath);
        int index = fileFullName.lastIndexOf(".");
        if (index != -1) {
            return fileFullName.substring(0, index);
        }
        return fileFullName;
    }

    /**
     * 获得文件名中的后缀名
     *
     * @param fileName 源文件名
     * @return String 后缀名
     */
    public static String getFileSuffix(String fileName) {
        int index = fileName.lastIndexOf(".");
        if (index != -1) {
            return fileName.substring(index + 1, fileName.length());
        }
        return fileName;
    }

    /**
     * 从CLASSPATH路径下加载指定文件
     *
     * @param fileName 文件名称
     * @param charset  文件内容的编码
     * @return
     */
    public static String getFileFromClassPath(String fileName, String charset) {
        URL url = FileUtils.class.getClassLoader().getResource(fileName);
        String filepath = url.getFile();
        File file = new File(filepath);
        byte[] retBuffer = new byte[(int) file.length()];
        FileInputStream fis = null;
        String content = null;
        try {
            fis = new FileInputStream(filepath);
            fis.read(retBuffer);
            content = new String(retBuffer, (null == charset || "".equals(charset) ? Constant.DEFAULT_CHARSET : charset));
        } catch (IOException e) {
            logger.error("Executing FileInputStream.read() Occur IOException,please check and fix it.", e);
        } finally {
            if (null != fis) {
                try {
                    fis.close();
                } catch (IOException e1) {
                    logger.error("Close [FileInputStream] Occur IOException,please check and fix it.", e1);
                }
            }
            return content;
        }
    }

    /**
     * 从CLASSPATH路径下加载指定文件
     *
     * @param fileName 文件名称
     * @return
     */
    public static String getFileFromClassPath(String fileName) {
        return getFileFromClassPath(fileName, null);
    }

    /**
     * 从URL链接中猜测文件名称
     *
     * @param url
     * @return
     */
    public static String guessFileNameFromUrl(String url) {
        String reg = "(/|=)([^/&?]+\\.[a-zA-Z]+)";
        if (StringUtils.isEmpty(url) || Pattern.compile(reg).matcher(url).find()) {
            return "UnknowName.temp";
        }
        Matcher matcher = Pattern.compile(reg).matcher(url);
        String s = "";
        while (matcher.find()) {
            s = matcher.group(2);
        }
        return s;
    }

    /**
     * 从Content-Disposition中提取文件名
     *
     * @param contentDisposition
     * @return
     */
    public static String getFileNameFromContentDisposition(String contentDisposition) {
        if (StringUtils.isEmpty(contentDisposition)) {
            return "UnknowName.temp";
        }
        if (!contentDisposition.startsWith("attachment")) {
            return null;
        }
        return contentDisposition.substring(contentDisposition.indexOf("=") + 1);
    }

    /**
     * @param @param  is
     * @param @return
     * @return OutputStream
     * @throws
     * @Title: inputStream2OutputStream
     * @Description: 输入流写入到字节数组缓冲流
     */
    @SuppressWarnings("finally")
    public static OutputStream inputStream2OutputStream(InputStream is) {
        if (null == is) {
            logger.error("The target parameter[InputStream is] MUST not be null.");
        }
        OutputStream os = new ByteArrayOutputStream();
        byte[] buf = new byte[Constant.BUFFER_SIZE];
        try {
            for (int readNum; (readNum = is.read(buf)) != -1; ) {
                os.write(buf, 0, readNum);
            }
        } catch (IOException e) {
            logger.error("Executing InputStream.read() Occur IOException,please check and fix it.", e);
        } finally {
            try {
                if (null != is) {
                    is.close();
                }
            } catch (IOException e) {
                logger.error("Close [InputStream] Occur IOException,please check and fix it.", e);
            }
            return os;
        }
    }

    /**
     * @param @param  is
     * @param @return
     * @return File
     * @throws
     * @Title: inputStream2File
     * @Description: 将输入流中数据写入文件
     */
    public static File inputStream2File(InputStream is, String fileSavePath) {
        OutputStream outputStream = null;
        File file = new File(fileSavePath);
        try {
            outputStream = new FileOutputStream(file);
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = is.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        } catch (IOException e) {
            logger.error("Executing InputStream.read() Occur IOException,please check and fix it.", e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    logger.error("Close [InputStream] Occur IOException,please check and fix it.", e);
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    logger.error("Close [OutputStream] Occur IOException,please check and fix it.", e);
                }
            }
        }
        return file;
    }

    /**
     * @param @return
     * @return String
     * @throws
     * @Title: randomFileName
     * @Description: 生成随机文件名
     */
    public static String randomFileName() {
        String id = UUID.randomUUID().toString();
        id = id.replace("-", "");
        int num = NumberUtils.generateRandomNumber(1000000, 0);
        if (num % 2 == 0) {
            id = id.toUpperCase();
        }
        return id;
    }

    /**
     * @param @param  filePath  文件的相对路径(相对于src)
     * @param @return
     * @return String
     * @throws
     * @Title: getAbsolutePath
     * @Description: 根据文件的相对路径获取该文件的硬盘绝对路径，
     * 如src目录下的xxxx.properties文件绝对路径为D:/projectName/bin/xxxx.properties
     */
    public static String getAbsolutePath(String filePath) {
        String path = PropertiesUtils.class.getClassLoader().getResource(filePath).getPath();
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        if (-1 != path.indexOf("/") && path.lastIndexOf("/") != path.indexOf("/")) {
            path = path.substring(0, path.lastIndexOf("/"));
        }
        if (!path.endsWith("/")) {
            path += "/";
        }
        return path;
    }

    /**
     * @param @param  file
     * @param @return
     * @param @throws IOException
     * @return byte[]
     * @throws
     * @Title: file2ByteArray
     * @Description: 文件转换成字节数组
     */
    public static byte[] file2ByteArray(File file) {
        if (null == file) {
            logger.error("The target parameter [file] in FileUtils.file2ByteArray()  MUST not be null.");
        }
        if (file.length() > Constant.MAX_FILE_SIZE) {
            logger.error("The uploading file MUST not be over 20M.");
        }
        ByteArrayOutputStream ous = null;
        InputStream ios = null;
        byte[] byteArray = null;
        try {
            byte[] buffer = new byte[4096];
            ous = new ByteArrayOutputStream();
            ios = new FileInputStream(file);
            int read = 0;
            while ((read = ios.read(buffer)) != -1) {
                ous.write(buffer, 0, read);
            }
            byteArray = ous.toByteArray();
        } catch (FileNotFoundException e) {
            logger.error("Reading one file to the ByteArray occur FileNotFoundException,please check the target file{} is exists really or not.", file.getAbsolutePath());
        } catch (IOException e) {
            logger.error("Reading one file to the ByteArray occur IOException,maybe the target file has been opened or it had been broken,please check and fix it.", e);
        } finally {
            try {
                if (ous != null) {
                    ous.close();
                }
                if (ios != null) {
                    ios.close();
                }
            } catch (IOException e) {
                logger.error("Close ByteArrayOutputStream/InputStream Occur IOException,please check and fix it.", e);
            }
        }
        return byteArray;
    }
}
