package cn.oy.test.utils;

import java.io.*;

/**
 * @author 蒜头王八
 * @project: ruangong1
 * @Description:
 * @Date 2020/3/14 21:50
 */
public class StreamUtil {
    /**
     * 关闭流工具方法
     * @param readers
     */
    public static void closeStream(Reader... readers){
        for(Reader in : readers){
            if(in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取文件字符流
     * @param file
     * @return
     */
    public static BufferedReader getReaderStream(File file) {


        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return reader;
    }
}
