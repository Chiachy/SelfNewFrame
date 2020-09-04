package com.common.base.utils.base;

import java.io.File;

/**
 * Created by HSK° on 2018/9/10.
 * --function:
 */
public class FileUtil {

    public static void delete(File file) {
        if (file.exists() && file.isFile()) {
            file.delete();
            return;
        }

        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                file.delete();
                return;
            }

            for (int i = 0; i < childFiles.length; i++) {
                delete(childFiles[i]);
            }
            file.delete();
        }
    }
}
