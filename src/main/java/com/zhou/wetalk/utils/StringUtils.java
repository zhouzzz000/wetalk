package com.zhou.wetalk.utils;

/**
 * @ProjectName wetalk
 * @Author zhouzzz
 * @Date 2020/4/9
 * @Time 13:14
 * @ClassName StringUtils
 * @see
 */

public class StringUtils {
    public static boolean isBlank(String s){
        return s==null || s.isEmpty() || s.trim().length() ==0;
    }
}