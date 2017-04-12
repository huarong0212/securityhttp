package com.kk.securityhttp.utils;

import com.kk.securityhttp.domain.GoagalInfo;

import java.io.File;

/**
 * Created by zhangkai on 2017/2/16.
 */

public class PathUtil {
    public static String getConfigPath() {
        makeBaseDir();
        File dir = new File(GoagalInfo.PATH + "/config");
        if (!dir.exists()) {
            dir.mkdir();
        }
        return dir.getAbsolutePath();
    }

    private static void makeBaseDir() {
        File dir = new File(GoagalInfo.PATH);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }
}
