package cn.oy.test.wc;

import cn.oy.test.constant.StringConstant;
import cn.oy.test.utils.StreamUtil;
import cn.oy.test.utils.StringUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 蒜头王八
 * @project: ruangong
 * @Description:
 * @Date 2020/3/14 11:02
 */
public class WcTest {

    //默认处理 .java 文件
    String suffixName = ".java";

    /**
     * 构造方法
     * @param suffixName 需要处理的文件后缀名
     */
    public WcTest(String suffixName){
        this.suffixName = suffixName;
    }

    public WcTest(){
    }


    /**
     * 文件操作函数
     * @param opStrs 操作字符串数组
     * @throws IOException
     */
    public void op(String[] opStrs) throws IOException {

        int pos = opStrs.length - 1;

        //如果文件类型不符合
        if(!StringUtil.fileFilter(opStrs[pos], suffixName)){
            //异常处理，这里用输出代替
            System.out.println("文件格式错误");
            return;
        }
        //第一次提交，粗略完成部分功能，重要代码都添加了注释

        File file = new File(opStrs[pos]);

        //文件不存在
        if(!file.exists()){
            //异常处理，这里用输出代替
            System.out.println("文件不存在");
            return;
        }

        System.out.println("文件名：" + file.getName());

        BufferedReader reader = null;
        // 如果是 -s 递归进来的，如 wc -s -c -a file.c ，那么就是 从 opStrs[2] 开始，
        // 如果是普通操作，如 -a、-c 那么从 opStrs[1] 开始
        for(int i = StringConstant.RECURSION_CHARACTER.equals(opStrs[1]) ? 2 : 1; i < pos; i++){
            /*
            获取文件字符输出流
            为什么需要写在这里？ 而不是在 for 循环外面？
            因为如果写在外面，那么所有操作都使用一个 reader，而第一个操作会持续 readLine() 将字符全部读完，导致其他操作的 readLine() 为空
            因此，每一个操作都需要 重新获取一次 字符输出流
             */
            reader = StreamUtil.getReaderStream(file);
            switch (opStrs[i]){
                case "-c":
                    System.out.println("字符数：" + readFileCharacter(reader)); break;
                case "-w":
                    System.out.println("单词数：" + readFileWord(reader)); break;
                case "-l":
                    System.out.println("行数：" + readFileLine(reader)); break;
                case "-a":
                    // System.out.println(recHandle());
                default: break;
            }
        }
        //关闭流
        StreamUtil.closeStream(reader);
    }

    /**
     * 递归处理目录及子目录下的文件
     *
     * 路径下的 / 和 \\ 是等价的
     * @param opStrs 操作字符串
     */
    //wc -s -c file.c
    public void recHandle(String[] opStrs) throws IOException {
        int pos = opStrs.length - 1;
        //记录当前目录位置，防止因为后续修改而丢失
        String curCatalog = opStrs[pos];

        File file = new File(opStrs[pos]);
        //file.isFile() 能判断是否是文件或目录是否存在，如果是目录或目录不存在则返回 false
        if(file.isFile()){
            //异常处理
            System.out.println("目录错误 或 所选择路径不是一个目录");
        }
        //获取子目录
        File[] files = file.listFiles();
        //判空
        if(files != null){
            List<File> fileList = new ArrayList<>();
            for(File f : files){
                //如果是文件那么将文件先存储起来
                if(f.isFile()){
                    fileList.add(f);
                }else{
                    //将最后文件目录修改为子目录
                    opStrs[pos] = f.getPath();
                    recHandle(opStrs);
                }
            }
            System.out.println("当前目录：" + curCatalog);
            //统一处理文件
            for(File f : fileList){
                opStrs[pos] = f.getPath();
                op(opStrs);
            }
        }
    }


    /**
     * 获取行数
     * @param reader
     * @return
     * @throws IOException
     */
    private int readFileLine(BufferedReader reader) throws IOException {
        int countLine = 0;
        while(reader.readLine() != null){
            countLine++;
        }
        return countLine;
    }

    /**
     * 获取单词数
     * @param reader
     * @return
     * @throws IOException
     */
    private int readFileWord(BufferedReader reader) throws IOException {
        //使用正则进行分割：空格 Tab { } ；: ~ ！ ？ ^ % + - * / | & >> >>> << <<< [ ] ( ) \\
        int countWord = 0;
        String str = "";
        while((str = reader.readLine()) != null){
            countWord += str.split("\\s+|\\(|\\)|,|\\.|\\:|\\{|\\}|\\-|\\*|\\+|;|\\?|\\/|\\\\|/").length;
        }
        return countWord;
    }

    /**
     * 获取字符数
     * @param reader
     * @return
     * @throws IOException
     */
    private int readFileCharacter(BufferedReader reader) throws IOException {
        int countCharacter = 0;
        String str = "";
        while((str = reader.readLine()) != null){
            countCharacter += str.length();
        }
        return countCharacter;
    }

}
