package cn.oy.test;

import cn.oy.test.utils.StringUtil;
import cn.oy.test.wc.WcTest;

import java.io.IOException;
import java.util.Scanner;

/**
 * @author 蒜头王八
 * @project: ruangong1
 * @Description:
 * @Date 2020/3/14 22:43
 */
public class Test {
    /**
     * 测试函数
     * @param args
     */
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        WcTest wcTest = new WcTest();
        String recChar = "-s";
        do {
            String[] opStrs = scanner.nextLine().split("\\s+");

            //wc -s -c F:\idea-workspace\ruangong1\src\cn\oy\test\Test.java
            //判断操作数组是否符合标准
            if(StringUtil.opStrsIsBad(opStrs)){
                //异常处理，这里用输出代替
                System.out.println("指令输入有误");
                continue;
            }

            if(!recChar.equals(opStrs[1])){
                wcTest.op(opStrs);
            }else {
                wcTest.recHandle(opStrs);
            }
        } while (true);
    }
}
