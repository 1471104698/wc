package cn.oy.test.utils;

import cn.oy.test.constant.StringConstant;

import java.util.Arrays;
import java.util.List;

/**
 * @author 蒜头王八
 * @project: ruangong1
 * @Description:
 * @Date 2020/3/14 21:49
 */
public class StringUtil {

    /**
     * 判断操作字符串数组是否    不符合标准
     * @param opStrs
     * @return
     */
    public static boolean opStrsIsBad(String[] opStrs){
        List<String> opList = Arrays.asList("-a", "-c", "-l", "-w", "-s");

        //wc -c F:\idea-workspace\ruangong1\src\cn\oy\test\Test.java

        int len = opStrs.length;

        //1、操作数组长度不满足要求
        if(len < StringConstant.OP_COMMON_MIN_LEN){
            return true;
        }

        //2、首字符串不符合要求，即不为 "wc"
        if(!StringConstant.HEAD_CHARACTER.equals(opStrs[0])){
            return true;
        }

        //3、中间操作不符合要求，如 -s 后面没有跟着别的操作如 -c、-a，或者 没有 4 个以上元素
        if(StringConstant.RECURSION_CHARACTER.equals(opStrs[1]) && (len < StringConstant.OP_REC_MIN_LEN || !opList.contains(opStrs[2]))){
            return true;
        }

        //4、中间操作不符合要求，如 不为 -c、-a 等
        if(!opList.contains(opStrs[1])){
            return true;
        }

        //5、最后一个元素不是目录而仍然是操作数
        if(opList.contains(opStrs[len - 1])){
            return true;
        }

        return false;
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
