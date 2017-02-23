package com.gome.redline.utils.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Lanxiaowei at 2017/1/20 11:42
 * Java IO操作工具类
 */
public class IOUtils {
    private static Logger logger = LogManager.getLogger(IOUtils.class.getName());

    /**
     * @param @param  obj
     * @param @return
     * @param @throws IOException
     * @return byte[]
     * @throws
     * @Title: object2ByteArray
     * @Description: Object对象转换成字节数组
     */
    public static byte[] object2ByteArray(Object obj) throws IOException {
        ByteArrayOutputStream bout = null;
        ObjectOutputStream out = null;
        byte[] bytes = null;
        try {
            bout = new ByteArrayOutputStream();
            out = new ObjectOutputStream(bout);
            out.writeObject(obj);
            out.flush();
            bytes = bout.toByteArray();
        } catch (IOException e) {
            logger.error("Cast [Object] to [Byte Array] occur IOException,please check and correct it.", e);
        } finally {
            if (null != bout) {
                try {
                    bout.close();
                } catch (IOException e) {
                    logger.error("Close [ByteArrayOutputStream] occur IOException,please check and correct it.", e);
                }
            }
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    logger.error("Close [ObjectOutputStream] occur IOException,please check and correct it.", e);
                }
            }
            return bytes;
        }
    }

    /**
     * @param @param  bytes
     * @param @return
     * @param @throws IOException
     * @param @throws ClassNotFoundException
     * @return Object
     * @throws
     * @Title: byteArray2Object
     * @Description: 字节数组转换成Object对象
     */
    public static Object byteArray2Object(byte[] bytes) {
        ByteArrayInputStream bi = null;
        ObjectInputStream oi = null;
        Object obj = null;
        try {
            bi = new ByteArrayInputStream(bytes);
            oi = new ObjectInputStream(bi);
            obj = oi.readObject();
        } catch (IOException e) {
            logger.error("Cast [Byte Array] to [Object] occur IOException,please check and correct it.", e);
        } catch (ClassNotFoundException e) {
            logger.error("Execute ObjectInputStream's readObject method occur IOException,please check and correct it.", e);
        } finally {
            if (null != bi) {
                try {
                    bi.close();
                } catch (IOException e) {
                    logger.error("Close [ByteArrayInputStream] occur IOException,please check and correct it.", e);
                }
            }
            if (null != oi) {
                try {
                    oi.close();
                } catch (IOException e) {
                    logger.error("Close [ObjectInputStream] occur IOException,please check and correct it.", e);
                }
            }
            return obj;
        }
    }

    /**
     * Object对象转换成NIO中的ByteBuffer
     *
     * @param obj
     * @return
     * @throws IOException
     */
    public static ByteBuffer object2ByteBuffer(Object obj) throws IOException {
        byte[] bytes = object2ByteArray(obj);
        ByteBuffer buff = ByteBuffer.wrap(bytes);
        return buff;
    }

    /**
     * 输入流内容Copy到输出流<br/>
     * 字节复制
     *
     * @param input
     * @param output
     * @param buffer
     * @return
     * @throws IOException
     */
    public static long byteCopy(InputStream input, OutputStream output, byte buffer[]) throws IOException {
        long count = 0L;
        for (int n = 0; -1 != (n = input.read(buffer)); ) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    /**
     * 输入流内容Copy到输出流
     *
     * @param input
     * @param output
     * @return
     * @throws IOException
     */
    public static long byteCopy(InputStream input, OutputStream output) throws IOException {
        return byteCopy(input, output, new byte[4096]);
    }

    /**
     * 输入流内容Copy到输出流<br/>
     * 字符复制
     *
     * @param reader
     * @param writer
     * @param buffer
     * @return
     * @throws IOException
     */
    public static long charCopy(Reader reader, Writer writer, char buffer[]) throws IOException {
        long count = 0L;
        for (int n = 0; -1 != (n = reader.read(buffer)); ) {
            writer.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    /**
     * 输入流内容Copy到输出流<br/>
     * 字符复制
     *
     * @param reader
     * @param writer
     * @return
     * @throws IOException
     */
    public static long charCopy(Reader reader, Writer writer) throws IOException {
        return charCopy(reader, writer, new char[4096]);
    }

    /**
     * 字节复制
     *
     * @param input
     * @param output
     * @return 返回copy的字节总数
     * @throws IOException
     */
    public static int copy(InputStream input, OutputStream output) throws IOException {
        long count = byteCopy(input, output);
        if (count > 2147483647L) {
            return -1;
        }
        return (int) count;
    }

    /**
     * 字符复制
     *
     * @param reader
     * @param writer
     * @return
     * @throws IOException
     */
    public static long copy(Reader reader, Writer writer) throws IOException {
        long count = charCopy(reader, writer);
        return count > 9223372036854775807L ? -1 : count;
    }

    /**
     * IO流复制
     *
     * @param reader
     * @param output
     * @throws IOException
     */
    public static void copy(Reader reader, OutputStream output) throws IOException {
        OutputStreamWriter out = new OutputStreamWriter(output);
        charCopy(reader, ((Writer) (out)));
        out.flush();
    }

    /**
     * IO流复制
     *
     * @param reader
     * @param output
     * @param encoding 写入编码
     * @throws IOException
     */
    public static void copy(Reader reader, OutputStream output, String encoding) throws IOException {
        if (encoding == null) {
            copy(reader, output);
        } else {
            OutputStreamWriter out = new OutputStreamWriter(output, encoding);
            charCopy(reader, ((Writer) (out)));
            out.flush();
        }
    }

    /**
     * 字节数组写到输出流
     *
     * @param data     字节数组
     * @param output   输出流
     * @param encoding 字节数组数据原始编码
     * @throws IOException
     */
    public static void write(byte data[], OutputStream output, String encoding) throws IOException {
        if (data != null) {
            if (null != encoding && encoding.length() > 0) {
                output.write(new String(data, encoding).getBytes());
            } else {
                output.write(data);
            }
        }
    }

    /***************关闭IO资源************************/
    public static void closeSilence(Reader input) {
        closeSilence(((Closeable) (input)));
    }

    public static void closeSilence(Writer output) {
        closeSilence(((Closeable) (output)));
    }

    public static void closeSilence(InputStream input) {
        closeSilence(((Closeable) (input)));
    }

    public static void closeSilence(OutputStream output) {
        closeSilence(((Closeable) (output)));
    }

    public static void closeSilence(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException io) {
            logger.error("Close [Closeable] occur IOException,please check and correct it.", io);
        }
    }

    public static void closeSilence(Socket sock) {
        if (sock != null) {
            try {
                sock.close();
            } catch (IOException io) {
                logger.error("Close [Socket] occur IOException,please check and correct it.", io);
            }
        }
    }

    public static void closeSilence(Selector selector) {
        if (selector != null) {
            try {
                selector.close();
            } catch (IOException io) {
                logger.error("Close [Selector] occur IOException,please check and correct it.", io);
            }
        }
    }

    public static void closeSilence(ServerSocket sock) {
        if (sock != null) {
            try {
                sock.close();
            } catch (IOException io) {
                logger.error("Close [IOException] occur IOException,please check and correct it.", io);
            }
        }
    }
    /***************关闭IO资源 End************************/
}
