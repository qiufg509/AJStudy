package com.qiufengguang.ajstudy.utils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * 文件操作工具类（内存安全/线程安全说明：所有方法均为同步方法，推荐在子线程中使用）
 * 外部公共存储文件读取，请使用ContentResolver查询
 * ContentResolver resolver = context.getContentResolver();
 * String selection = MediaStore.Files.FileColumns.RELATIVE_PATH + " LIKE ?";
 * String[] selectionArgs = new String[]{path};
 * Cursor cursor = resolver.query(
 * collection,
 * projection,
 * selection,
 * selectionArgs,
 * MediaStore.Files.FileColumns.DATE_ADDED + " DESC");
 * while (cursor.moveToNext()) {
 * String name = cursor.getString(nameIndex);
 * Uri fileUri = ContentUris.withAppendedId(collection, id);
 * }
 *
 * @author qiufengguang
 * @since 2025/5/5 22:12
 */
public class FileUtil {
    private static final String TAG = "FileUtil";

    // 默认字符集
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    // 缓冲区大小（8KB）
    private static final int BUFFER_SIZE = 8192;

    private FileUtil() {
        throw new UnsupportedOperationException("Cannot instantiate utility class");
    }

    //--------------------------------- 读取Assets文件 ---------------------------------//

    /**
     * 读取assets文件为String（默认UTF-8）
     */
    public static String readAssetsToString(Context context, String fileName) {
        return readAssetsToString(context, fileName, DEFAULT_CHARSET);
    }

    /**
     * 读取assets文件为String（指定字符集）
     */
    public static String readAssetsToString(Context context, String fileName, Charset charset) {
        if (Objects.isNull(context) || TextUtils.isEmpty(fileName)) {
            return "";
        }

        try (InputStream is = context.getAssets().open(fileName)) {
            return streamToString(is, charset);
        } catch (IOException e) {
            Log.e(TAG, "readAssetsToString error. " + e.getMessage());
        }
        return "";
    }

    //--------------------------------- 内部存储操作 ---------------------------------//

    /**
     * 读取内部存储文件为String（默认UTF-8）
     */
    public static String readInternalFileToString(Context context, String fileName) {
        if (Objects.isNull(context) || TextUtils.isEmpty(fileName)) {
            return "";
        }
        return readFileToString(new File(context.getFilesDir(), fileName), DEFAULT_CHARSET);
    }

    /**
     * 获取内部存储File对象
     */
    public static File getInternalFile(Context context, String fileName) {
        File file = new File(context.getFilesDir(), fileName);
        return validateFile(file) ? file : null;
    }

    //--------------------------------- 外部存储操作 ---------------------------------//

    /**
     * 读取外部私有存储文件为String（默认UTF-8）
     */
    public static String readExternalFileToString(Context context, String fileName) {
        if (Objects.isNull(context) || TextUtils.isEmpty(fileName)) {
            return "";
        }
        File file = getExternalFile(context, fileName);
        return Objects.nonNull(file) ? readFileToString(file, DEFAULT_CHARSET) : "";
    }

    /**
     * 获取外部私有存储File对象
     */
    public static File getExternalFile(Context context, String fileName) {
        if (isExternalStorageInvalid()) {
            return null;
        }
        File file = new File(context.getExternalFilesDir(null), fileName);
        return validateFile(file) ? file : null;
    }

    /**
     * 获取外部私有存储目录的文件列表
     */
    public static List<String> getExternalFileName(Context context, String fileName) {
        if (Objects.isNull(context) || TextUtils.isEmpty(fileName)) {
            return null;
        }
        if (isExternalStorageInvalid()) {
            return null;
        }
        File dir = new File(context.getExternalFilesDir(null), fileName);
        if (!validateDirectory(dir)) {
            return null;
        }
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            return null;
        }
        Arrays.sort(files, Comparator.comparing(File::getName));
        List<String> fileList = new ArrayList<>(files.length);
        for (File file : files) {
            fileList.add(file.getName());
        }
        return fileList;
    }

    //--------------------------------- 通用文件操作 ---------------------------------//

    /**
     * 读取文件为String（默认UTF-8）
     */
    public static String readFileToString(File file) {
        return readFileToString(file, DEFAULT_CHARSET);
    }

    /**
     * 读取文件为String（指定字符集）
     */
    public static String readFileToString(File file, Charset charset) {
        if (!validateFile(file)) {
            return "";
        }

        try (FileInputStream fis = new FileInputStream(file)) {
            return streamToString(fis, charset);
        } catch (IOException e) {
            Log.e(TAG, "readFileToString error. " + e.getMessage());
        }
        return "";
    }

    //--------------------------------- 核心工具方法 ---------------------------------//

    /**
     * 输入流转String（自动关闭流）
     */
    private static String streamToString(InputStream is, Charset charset) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[BUFFER_SIZE];
        int length;
        while ((length = is.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return result.toString(charset);
        } else {
            String charsetName = charset.name();
            return result.toString(charsetName);
        }
    }

    /**
     * 校验文件有效性
     */
    private static boolean validateFile(File file) {
        return file != null && file.exists() && file.isFile();
    }

    /**
     * 校验文件夹有效性
     */
    private static boolean validateDirectory(File file) {
        return file != null && file.exists() && file.isDirectory();
    }

    /**
     * 检查外部存储可用性
     */
    private static boolean isExternalStorageInvalid() {
        return !Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    //------------------------------ 可选：写入方法增强 ------------------------------//

    /**
     * 可选：写入字符串到内部存储（默认UTF-8）
     */
    public static boolean writeStringToInternal(Context context, String fileName, String content) {
        return writeStringToFile(new File(context.getFilesDir(), fileName), content, DEFAULT_CHARSET);
    }

    /**
     * 通用文件写入方法
     */
    public static boolean writeStringToFile(File file, String content, Charset charset) {
        if (Objects.isNull(file) || TextUtils.isEmpty(content)) {
            return false;
        }

        try {
            if (file.getParentFile() == null || (!file.getParentFile().exists()
                && !file.getParentFile().mkdirs())) {
                return false;
            }

            try (OutputStream os = new FileOutputStream(file);
                 BufferedWriter writer = new BufferedWriter(
                     new OutputStreamWriter(os, charset))) {
                writer.write(content);
                return true;
            }
        } catch (IOException e) {
            Log.e(TAG, "writeStringToFile error. " + e.getMessage());
        }
        return false;
    }
}