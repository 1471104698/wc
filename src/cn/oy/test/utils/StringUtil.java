package cn.oy.test.utils;

import cn.oy.test.constant.StringConstant;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author 蒜头王八
 * @project: ruangong1
 * @Description:
 * @Date 2020/3/14 21:49
 */
public class StringUtil {

    static List<String> opList = Arrays.asList("-a", "-c", "-l", "-w", "-s");

    /**
     * 判断操作字符串数组是否符合标准
     * @param opStrs
     * @return 当不符合标准时返回 false
     */

    public static boolean opStrsIsOk(String[] opStrs){

        //wc -c F:\idea-workspace\ruangong1\src\cn\oy\test\Test.java

        int len = opStrs.length;

        //1、操作数组长度不满足要求
        if(len < StringConstant.OP_COMMON_MIN_LEN){
            return false;
        }

        //2、首字符串不符合要求，即不为 "wc"
        if(!StringConstant.HEAD_CHARACTER.equals(opStrs[0])){
            return false;
        }

        //3、最后一个元素不是目录而仍然是操作数
        if(opList.contains(opStrs[len - 1])){
            return false;
        }

        //4、递归标识符位置检查 以及 指令查重 、中间是否是操作数检查
        if(!checkOp(opStrs)){
            return false;
        }

        return true;

    }

    /**
     * 指令查重 以及 递归标识符 -s 可用性检查
     *
     * @param opStrs
     * @return 指令有误返回 false
     */
    private static boolean checkOp(String[] opStrs){
        //是否出现其他操作符
        boolean com_flag = false;
        //是否出现过递归标识符
        boolean rec_flag = false;
        Set<String> set  = new HashSet<>();
        for(int i = 1; i < opStrs.length - 1; i++){
            //出现重复指令
            if(!set.add(opStrs[i])){
                return false;
            }
            //出现递归标识符
            if(StringConstant.RECURSION_CHARACTER.equals(opStrs[i])){
                //在之前就出现了普通操作数，那么顺序不对
                if(com_flag){
                    return false;
                }else{
                    rec_flag = true;
                }
            }else if(opList.contains(opStrs[i])){  //普通操作数
                com_flag = true;
            }else {     //如果什么都不是，即不是 -s、-c、-a 等指令
                return false;
            }
        }
        //两个同时出现（在此已经保证了顺序的正确性，因为上面对顺序不正确的已经做了处理） 或者 只出现普通操作数
        return rec_flag && com_flag || !rec_flag && com_flag;
    }


    /**
     * 判断是否是需要处理的文件类型
     * @param fileName
     * @return
     */
    public static boolean fileFilter(String fileName, String suffixName){
        return fileName.endsWith(suffixName);
    }
}
