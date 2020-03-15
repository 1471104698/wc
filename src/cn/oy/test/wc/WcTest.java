package cn.oy.test.wc;

import cn.oy.test.constant.StringConstant;
import cn.oy.test.utils.StreamUtil;
import cn.oy.test.utils.StringUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author 蒜头王八
 * @project: ruangong
 * @Description:
 * @Date 2020/3/14 11:02
 */
public class WcTest {

    //默认处理 .java 文件
    private String suffixName = ".java";

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
     * 对外开放主函数
     */
    public void input(){
        Scanner scanner = new Scanner(System.in);
        do {
            System.out.println("命令行格式：wc [options] [filePath]");
            System.out.println("ps：如需 -s 递归，请输入目录路径");
            System.out.println("退出输入：exit");
            String inStr = scanner.nextLine();
            if("exit".equals(inStr)){
                break;
            }

            String[] opStrs = inStr.split("\\s+");

            /*
            wc -c F:\idea-workspace\ruangong1\src\cn\oy\test\Test.java
            wc -s -c -c F:\idea-workspace\ruangong1\src\cn\oy\test

            wc -c F:\idea-workspace\ruangong1\src\cn\oy\test\wc\WcTest.java

            wc -c F:\idea-workspace\ruangong1\src\cn\oy\test\wc\??????.java

            wc -c F:\idea-workspace\ruangong1\src\cn\oy\test\utils\****.java
             */
            //判断操作数组是否符合标准
            if(!StringUtil.opStrsIsOk(opStrs)){
                //异常处理，这里用输出代替
                System.out.println("指令输入有误，请重新输入！！！");
                continue;
            }

            try {
                if(!StringConstant.RECURSION_CHARACTER.equals(opStrs[1])){
                    op(opStrs);
                }else {
                    recHandle(opStrs);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("***********************************");
            System.out.println("***********************************");
        } while (true);
    }


    /**
     * 文件操作函数
     * @param opStrs 操作字符串数组
     * @throws IOException
     */
    private void op(String[] opStrs) throws IOException {

        int pos = opStrs.length - 1;

        //如果文件类型不符合
        if(!StringUtil.fileFilter(opStrs[pos], suffixName)){
            //异常处理，这里用输出代替
            System.out.println("文件格式错误");
            return;
        }

        File file = new File(opStrs[pos]);

        //文件不存在
        if(!file.exists()){
            //判断是否满足通配符匹配，如果是从 -s 过来的，文件肯定是存在的，因此不会进入到这里的匹配阶段
            if(!matchFileHandle(opStrs, file)){
                //异常处理，这里用输出代替
                System.out.println("文件不存在");
            }
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
    private void recHandle(String[] opStrs) throws IOException {
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
     * 读取特殊行
     * @param reader
     */
    private void readFileSpecialLine(BufferedReader reader){
    }

    /**
     * 查找目录下的通配符匹配文件，不包括递归，只遍历跟当前文件同目录的文件
     * @param file
     */
    private boolean matchFileHandle(String[] opStrs, File file) throws IOException {
        //得到当前文件名称
        String fileName = file.getName();
        //判断是否存在匹配通配符
        if(!isExistMatch(fileName)){
            return false;
        }
        //得到父路径
        File parentFile = file.getParentFile();
        //遍历父路径下的所有文件
        File[] files = parentFile.listFiles();
        if (files != null) {
            for(File f : files){
                //判断是否匹配
                if(isMatch(f.getName(), fileName)){
                    //修改文件路径
                    opStrs[opStrs.length - 1] = f.getPath();
                    op(opStrs);
                }
            }
        }
        return true;
    }

    /**
     * 判断文件名是否存在 ? 或 * 通配符
     * @param fileName
     * @return
     */
    private boolean isExistMatch(String fileName){
        return fileName.contains("?") || fileName.contains("*");
    }

    /**
     * 通配符匹配    ？ 可表示任意单个字符， * 可以表示任意单个或多个字符，也可以表示空串
     * s1 和 s2 是否匹配
     * @param s
     * @param p
     * @return
     */
    private boolean isMatch(String s, String p){
        /*
        输入:s = "aa"  	p = "a"
        输出: false
        解释: "a" 无法匹配 "aa" 整个字符串。

        输入:s = "aa"		p = "*"
        输出: true
        解释: '*' 可以匹配任意字符串。

        使用动态规划
        dp[i][j] 表示 s1 的前 i 个字符是否能被 s2 的前 j 个字符进行匹配

           ""   a   d   c   e   b
        ""  T   F   F   F   F   F
        *   T   T   T   T   T   T
        *   T   T   T   T   T   T
        a   F   T   F   F   F   F
        *   F   T   T   T   T   T
        b   F   F   F   F   F   T
         */

        char[] ss = s.toCharArray();
        char[] ps = p.toCharArray();

        boolean[][] dp = new boolean[ss.length + 1][ps.length + 1];

        //当 ss 和 ps 都只有 0 个字符的时候，那么匹配
        dp[0][0] = true;

        //当 ss 为空串时，那么只有 ps 全部为 * 时才可以进行匹配（？ 必须匹配单个字符，不能匹配空串）
        for(int i = 1; i <= ps.length; i++){
            dp[0][i] = ps[i - 1] == '*' && dp[0][i - 1];
        }

        for(int i = 1; i <= ss.length; i++){
            for(int j = 1; j <= ps.length; j++){
                if(ps[j - 1] == '?' || ps[j - 1] == ss[i - 1]){
                    //如果 ps 的当前字符为 ? 或者 ss 和 ps 当前字符相同，那么直接看两者上一个字符的匹配情况
                    dp[i][j] = dp[i - 1][j - 1];
                }else if(ps[j - 1] == '*'){
                    //如果 ps 当前字符为 *，那么有两种情况，匹配 ss 当前字符（该选择类似完全背包问题） 或者不匹配，即匹配空串
                    dp[i][j] = dp[i - 1][j] || dp[i][j - 1];
                }
            }
        }
        return dp[ss.length][ps.length];
    }

}
