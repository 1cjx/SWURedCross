package com.jx.utils;

import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckUtils {

    public static boolean checkEmail(String email) {
        boolean flag = false;
        try {
            String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }
    public static boolean isValidPhoneNumber(String phoneNumber) {
        if ((phoneNumber != null) && (!phoneNumber.isEmpty())) {
            return Pattern.matches("^1[3-9]\\d{9}$", phoneNumber);
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.print(isValidPhoneNumber("15102397776"));
    }

    public static boolean checkObjAllFieldsIsNull(Object object) {
        // 如果对象为null直接返回true
        if (null == object) {
            return true;
        }
        try {
            // 挨个获取对象属性值
            for (Field f : object.getClass().getDeclaredFields()) {
                f.setAccessible(true);
                // 如果属性名不为serialVersionUID，有一个属性值不为null，且值不是空字符串，就返回false
                if (!"serialVersionUID".equals(f.getName()) &&
                        f.get(object) != null && StringUtils.hasText(f.get(object).toString())) {
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}