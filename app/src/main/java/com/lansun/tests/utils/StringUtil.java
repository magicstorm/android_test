package com.lansun.tests.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by felix.fan on 2016/9/3.
 * 用户名、密码效验
 */
public class StringUtil {
    private static String usernameRegular = "^((13[0-9])|(14[0-9])|(15[0-9])|(17[0-9])|(18[0-9]))\\d{8}$";
    private static String passwordRegular = "^[a-zA-Z0-9]{6,12}$";
    private static String emailRegular = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,4}$";
    //用户名效验，11位手机号
    public static boolean usernameMatch(String useraname) {
        return useraname==null?false:useraname.matches(usernameRegular);
    }

    //密码6-12位的字母或数字
    public static boolean passwordMatch(String password) {
        return password == null ? false : password.matches(passwordRegular);
    }
    //邮箱验证
    public static boolean emailMatch(String email) {
        if (TextUtils.equals(email, "") || email == null) {
            return false;
        }
        Pattern p = Pattern.compile(emailRegular);
        Matcher m = p.matcher(email);
        return m.matches();
    }

}
