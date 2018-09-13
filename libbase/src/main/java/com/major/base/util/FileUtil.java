package com.major.base.util;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.Properties;

/**
 * TODO
 * Created by MEI on 2017/9/8.
 */

public class FileUtil {

    /**
     * 创建目录
     *
     * @param dir
     */
    public static void createDirIfNotExist(String dir) {
        if (!CommonUtil.isEmpty(dir)) {
            File file = new File(dir);
            if (!file.exists()) {
                file.mkdirs();
            }
        }
    }

    /**
     * 复制文件，可以选择是否删除源文件
     */
    public static boolean copyFile(String srcPath, String destPath, boolean deleteSrc) {
        File srcFile = new File(srcPath);
        File destFile = new File(destPath);
        return copyFile(srcFile, destFile, deleteSrc);
    }

    /**
     * 复制文件，可以选择是否删除源文件
     */
    public static boolean copyFile(File srcFile, File destFile, boolean deleteSrc) {
        if (!srcFile.exists() || !srcFile.isFile()) {
            return false;
        }
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(srcFile);
            out = new FileOutputStream(destFile);
            byte[] buffer = new byte[1024];
            int i = -1;
            while ((i = in.read(buffer)) > 0) {
                out.write(buffer, 0, i);
                out.flush();
            }
            if (deleteSrc) {
                srcFile.delete();
            }
        } catch (Exception e) {
            return false;
        } finally {
            CloseUtil.close(in, out);
        }

        return true;
    }

    /**
     * 修改文件的权限,例如"777"等
     */
    public static void chmod(String path, String mode) {
        try {
            String command = "chmod " + mode + " " + path;
            Runtime runtime = Runtime.getRuntime();
            runtime.exec(command);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 把数据写入文件
     *
     * @param is       数据流
     * @param path     文件路径
     * @param recreate 如果文件存在，是否需要删除重建
     * @return 是否写入成功
     */
    public static boolean writeFile(InputStream is, String path, boolean recreate) {
        boolean res = false;
        File f = new File(path);
        FileOutputStream fos = null;
        try {
            if (recreate && f.exists()) {
                f.delete();
            }
            if (!f.exists() && null != is) {
                File parentFile = new File(f.getParent());
                parentFile.mkdirs();
                int count = -1;
                byte[] buffer = new byte[1024];
                fos = new FileOutputStream(f);
                while ((count = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, count);
                }
                res = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseUtil.close(fos);
        }
        return res;
    }

    /**
     * 把字符串数据写入文件
     *
     * @param content 需要写入的字符串
     * @param path    文件路径名称
     * @param append  是否以添加的模式写入
     * @return 是否写入成功
     */
    public static boolean writeFile(String content, String path, boolean append) {
        return writeFile(content.getBytes(), path, append);
    }

    /**
     * 把字符串数据写入文件
     *
     * @param content 需要写入的字符串
     * @param path    文件路径名称
     * @param append  是否以添加的模式写入
     * @return 是否写入成功
     */
    public static boolean writeFile(byte[] content, String path, boolean append) {
        boolean res = false;
        File f = new File(path);
        RandomAccessFile raf = null;
        try {
            if (f.exists()) {
                if (!append) {
                    f.delete();
                    f.createNewFile();
                }
            } else {
                f.createNewFile();
            }
            if (f.canWrite()) {
                raf = new RandomAccessFile(f, "rw");
                raf.seek(raf.length());
                raf.write(content);
                res = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseUtil.close(raf);
        }
        return res;
    }

    /**
     * 把键值对写入文件
     *
     * @param filePath 文件路径
     * @param key      键
     * @param value    值
     * @param comment  该键值对的注释
     */
    public static void writeProperties(String filePath, String key, String value, String comment) {
        if (CommonUtil.isEmpty(key) || CommonUtil.isEmpty(filePath)) {
            return;
        }
        FileInputStream fis = null;
        FileOutputStream fos = null;
        File f = new File(filePath);
        try {
            if (!f.exists() || !f.isFile()) {
                f.createNewFile();
            }
            fis = new FileInputStream(f);
            Properties p = new Properties();
            p.load(fis);// 先读取文件，再把键值对追加到后面
            p.setProperty(key, value);
            fos = new FileOutputStream(f);
            p.store(fos, comment);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseUtil.close(fis, fos);
        }
    }

    /**
     * 根据值读取
     */
    public static String readProperties(String filePath, String key, String defaultValue) {
        if (CommonUtil.isEmpty(key) || CommonUtil.isEmpty(filePath)) {
            return null;
        }
        String value = null;
        FileInputStream fis = null;
        File f = new File(filePath);
        try {
            if (!f.exists() || !f.isFile()) {
                f.createNewFile();
            }
            fis = new FileInputStream(f);
            Properties p = new Properties();
            p.load(fis);
            value = p.getProperty(key, defaultValue);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            CloseUtil.close(fis);
        }
        return value;
    }

    /**
     * 复制文件
     *
     * @param src    源文件
     * @param des    目标文件
     * @param delete
     * @return
     */
    public static boolean copy(String src, String des, boolean delete) {
        File file = new File(src);
        if (!file.exists()) {
            return false;
        }
        File desFile = new File(des);
        FileInputStream in = null;
        FileOutputStream out = null;
        try {
            in = new FileInputStream(file);
            out = new FileOutputStream(desFile);
            byte[] buffer = new byte[1024];
            int count;
            while ((count = in.read(buffer)) != -1) {
                out.write(buffer, 0, count);
                out.flush();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseUtil.close(in, out);
        }
        if (delete) {
            file.delete();
        }
        return false;
    }

    /**
     * 从指定的文件中读取字符串
     */
    public static String readDataFromFile(File file) {

        BufferedReader br = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            InputStreamReader inputreader = new InputStreamReader(fis);
            br = new BufferedReader(inputreader);
            String line;
            StringBuffer content = new StringBuffer();
            //分行读取
            while ((line = br.readLine()) != null) {
                content.append(line + "\n");
            }

            return content.toString();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseUtil.close(br, fis);
        }

        return "";
    }

    /**
     * 获取指定文件夹、文件大小
     */
    public static long getFileSize(File file) {
        long size = 0L;
        File[] list = file.listFiles();
        if (list == null || list.length == 0) {
            return 0L;
        }
        for (File fileList : list) {
            if (fileList.isDirectory()) {
                size = size + getFileSize(fileList);
            } else {
                size = size + fileList.length();
            }
        }

        return size;
    }

    /**
     * 获取文件后缀
     *
     * @param filePath
     * @return
     */
    public static String getFileSuffix(String filePath) {
        if (CommonUtil.isNotEmpty(filePath)) {
            int start = filePath.lastIndexOf(".");
            if (start != -1) {
                return filePath.substring(start + 1);
            }
        }
        return null;
    }

    /**
     * 判断是否是 sdcard 路径
     */
    public static final boolean isSDCardPath(String path) {
        if (CommonUtil.isEmpty(path)) {
            return false;
        }
        String sdRootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        return path.startsWith(sdRootPath);
    }

    /**
     * 获取可用空间大小
     */
    public static long getAvailableSpace(String fileDirPath) {
        try {
            File file = new File(fileDirPath);
            if (!file.exists()) {
                file.mkdirs();// create to make sure it is not error below
            }
            final StatFs stats = new StatFs(fileDirPath);
            long result = (long) stats.getBlockSize() * (long) stats.getAvailableBlocks();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 文件是否存在
     *
     * @param filePath the check file path
     * @return true means exist
     */
    public static boolean isFileExist(String filePath) {
        if (!isFilePath(filePath)) {
            return false;
        }

        File file = new File(filePath);
        return file.exists() && file.isFile();
    }

    /**
     * 判断是否是文件
     *
     * @param path file path
     * @return true means the path is file path
     */
    public static boolean isFilePath(String path) {
        if (CommonUtil.isEmpty(path)) {
            return false;
        }
        return path.startsWith(File.separator);
    }

    public static boolean deleteFile(String path) {
        return deleteFile(new File(path));
    }

    /**
     * 删除文件
     */
    public static boolean deleteFile(File file) {
        if (file == null) {
            return false;
        }
        if (file.exists()) {
            if (file.isFile()) {
                return file.delete();
            } else if (file.isDirectory()) {
                // 如果它是一个目录
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                    deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
                }
            }
            return file.delete();
        }

        return false;
    }

    /**
     * @param msg      写入的消息
     * @param path     路径
     * @param isAppend 是否是追加
     * @return 是否保存成功
     */
    public static boolean saveFile(String msg, String path, boolean isAppend) {
        if (TextUtils.isEmpty(msg)) {
            return false;
        }
        File file = new File(path);
        createDirIfNotExist(file.getParent());

        try {
            FileWriter fw = new FileWriter(path, isAppend);
            PrintWriter pw = new PrintWriter(fw, true);
            pw.println(msg);
            return true;
        } catch (IOException e) {
            Log.e("FileUtil", "saveFile: 发生异常");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 拷贝目录
     */
    public static boolean copyDir(File sourceFile, File targetFile) {
        if (sourceFile == null || targetFile == null) {
            return false;
        }
        if (!sourceFile.exists()) {
            return false;
        }
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        // 获取目录下所有文件和文件夹的列表
        File[] files = sourceFile.listFiles();
        if (files == null || files.length < 1) {
            return false;
        }
        File file = null;
        StringBuilder buffer = new StringBuilder();
        boolean isSuccessful = false;
        // 遍历目录下的所有文件文件夹，分别处理
        for (File file1 : files) {
            file = file1;
            buffer.setLength(0);
            buffer.append(targetFile.getAbsolutePath()).append(File.separator).append(file.getName());
            if (file.isFile()) {
                // 文件直接调用拷贝文件方法
                isSuccessful = copyFile(file.getAbsolutePath(), buffer.toString(), false);
                if (!isSuccessful) {
                    return false;
                }
            } else if (file.isDirectory()) {
                // 目录再次调用拷贝目录方法
                copyDir(file, new File(buffer.toString()));
            }

        }
        return true;
    }

    /**
     * 剪切目录，先将目录拷贝完后再删除源目录
     *
     * @param sourceDir
     * @param targetDir
     * @return
     */
    public static boolean cutDir(String sourceDir, String targetDir) {
        File sourceFile = new File(sourceDir);
        File targetFile = new File(targetDir);
        boolean isCopySuccessful = copyDir(sourceFile, targetFile);
        if (isCopySuccessful) {
            return deleteDir(sourceDir);
        }
        return false;
    }

    /**
     * 删除目录
     *
     * @param dir
     * @return
     */
    public static boolean deleteDir(String dir) {
        File file = new File(dir);
        if (!file.exists()) {
            return false;
        }
        File[] files = file.listFiles();
        boolean isSuccessful = false;
        if (files.length == 0) {
            file.delete();
            return true;
        }
        // 对所有列表中的路径进行判断是文件还是文件夹
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                isSuccessful = deleteDir(files[i].getAbsolutePath());
            } else if (files[i].isFile()) {
                isSuccessful = deleteFile(files[i].getAbsolutePath());
            }

            if (!isSuccessful) {
                // 如果有删除失败的情况直接跳出循环
                break;
            }
        }
        if (isSuccessful) {
            file.delete();
        }
        return isSuccessful;
    }


    /**
     * 将object对象写入outFile文件
     *
     * @param outFile
     * @param object
     * @param context
     */
    public static void writeObject2File(String outFile, Object object, Context context) {
        ObjectOutputStream out = null;
        FileOutputStream outStream = null;
        try {
            File dir = context.getDir("cache", Context.MODE_PRIVATE);
            outStream = new FileOutputStream(new File(dir, outFile));
            out = new ObjectOutputStream(new BufferedOutputStream(outStream));
            out.writeObject(object);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseUtil.close(outStream, out);
        }
    }

    /**
     * 从outFile文件读取对象
     *
     * @param filePath
     * @param context
     * @return
     */
    public static Object readObjectFromPath(String filePath, Context context) {
        Object object = null;
        ObjectInputStream in = null;
        FileInputStream inputStream = null;
        try {
            File dir = context.getDir("cache", Context.MODE_PRIVATE);
            File f = new File(dir, filePath);
            if (!f.exists()) {
                return null;
            }
            inputStream = new FileInputStream(new File(dir, filePath));
            in = new ObjectInputStream(new BufferedInputStream(inputStream));
            object = in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseUtil.close(in, inputStream);
        }

        return object;
    }

    /**
     * 读取指定路径下的文件内容
     *
     * @param path
     * @return 文件内容
     */
    public static String readFile(String path) {
        BufferedReader br = null;
        try {
            File myFile = new File(path);
            br = new BufferedReader(new FileReader(myFile));
            StringBuffer sb = new StringBuffer();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseUtil.close(br);
        }
        return null;
    }

    /**
     * 根据指定路径，创建父目录及文件
     *
     * @param filePath
     * @return File 如果创建失败的话，返回null
     */
    public static File createFile(String filePath) {
        return createFile(filePath, "755");
    }

    /**
     * 创建文件，并修改读写权限
     *
     * @param filePath
     * @param mode
     * @return
     */
    public static File createFile(String filePath, String mode) {
        File desFile = null;
        try {
            String desDir = filePath.substring(0, filePath.lastIndexOf(File.separator));
            File dir = new File(desDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            chmodFile(dir.getAbsolutePath(), mode);
            desFile = new File(filePath);
            if (!desFile.exists()) {
                desFile.createNewFile();
            }
            chmodFile(desFile.getAbsolutePath(), mode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return desFile;
    }

    /**
     * 修改文件读写权限
     *
     * @param fileAbsPath
     * @param mode
     */
    public static void chmodFile(String fileAbsPath, String mode) {
        String cmd = "chmod " + mode + " " + fileAbsPath;
        try {
            Runtime.getRuntime().exec(cmd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
