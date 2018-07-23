package com.major.extra.util;

import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.Log;

import com.major.base.util.CloseUtil;
import com.major.base.util.StringUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.Properties;

/**
 * @author 写文件的工具类
 */
public class FileUtil{

    /**
     * 创建文件夹
     */
    public static boolean createDirs(String dirPath){
        File file = new File(dirPath);
        if(!file.exists() || !file.isDirectory()){
            return file.mkdirs();
        }
        return true;
    }

    /**
     * 复制文件，可以选择是否删除源文件
     */
    public static boolean copyFile(String srcPath, String destPath, boolean deleteSrc){
        File srcFile = new File(srcPath);
        File destFile = new File(destPath);
        return copyFile(srcFile, destFile, deleteSrc);
    }

    /**
     * 复制文件，可以选择是否删除源文件
     */
    public static boolean copyFile(File srcFile, File destFile, boolean deleteSrc){
        if(!srcFile.exists() || !srcFile.isFile()){
            return false;
        }
        InputStream in = null;
        OutputStream out = null;
        try{
            in = new FileInputStream(srcFile);
            out = new FileOutputStream(destFile);
            byte[] buffer = new byte[1024];
            int i = -1;
            while((i = in.read(buffer)) > 0){
                out.write(buffer, 0, i);
                out.flush();
            }
            if(deleteSrc){
                srcFile.delete();
            }
        }catch(Exception e){
            return false;
        }finally{

        }

        return true;
    }

    /**
     * 修改文件的权限,例如"777"等
     */
    public static void chmod(String path, String mode){
        try{
            String command = "chmod " + mode + " " + path;
            Runtime runtime = Runtime.getRuntime();
            runtime.exec(command);
        }catch(Exception e){
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
    public static boolean writeFile(InputStream is, String path, boolean recreate){
        boolean res = false;
        File f = new File(path);
        FileOutputStream fos = null;
        try{
            if(recreate && f.exists()){
                f.delete();
            }
            if(!f.exists() && null != is){
                File parentFile = new File(f.getParent());
                parentFile.mkdirs();
                int count = -1;
                byte[] buffer = new byte[1024];
                fos = new FileOutputStream(f);
                while((count = is.read(buffer)) != -1){
                    fos.write(buffer, 0, count);
                }
                res = true;
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{


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
    public static boolean writeFile(String content, String path, boolean append){
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
    public static boolean writeFile(byte[] content, String path, boolean append){
        boolean res = false;
        File f = new File(path);
        RandomAccessFile raf = null;
        try{
            if(f.exists()){
                if(!append){
                    f.delete();
                    f.createNewFile();
                }
            } else {
                f.createNewFile();
            }
            if(f.canWrite()){
                raf = new RandomAccessFile(f, "rw");
                raf.seek(raf.length());
                raf.write(content);
                res = true;
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{

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
    public static void writeProperties(String filePath, String key, String value, String comment){
        if(StringUtil.isEmpty(key) || StringUtil.isEmpty(filePath)){
            return;
        }
        FileInputStream fis = null;
        FileOutputStream fos = null;
        File f = new File(filePath);
        try{
            if(!f.exists() || !f.isFile()){
                f.createNewFile();
            }
            fis = new FileInputStream(f);
            Properties p = new Properties();
            p.load(fis);// 先读取文件，再把键值对追加到后面
            p.setProperty(key, value);
            fos = new FileOutputStream(f);
            p.store(fos, comment);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 根据值读取
     */
    public static String readProperties(String filePath, String key, String defaultValue){
        if(StringUtil.isEmpty(key) || StringUtil.isEmpty(filePath)){
            return null;
        }
        String value = null;
        FileInputStream fis = null;
        File f = new File(filePath);
        try{
            if(!f.exists() || !f.isFile()){
                f.createNewFile();
            }
            fis = new FileInputStream(f);
            Properties p = new Properties();
            p.load(fis);
            value = p.getProperty(key, defaultValue);
        }catch(IOException e){
            e.printStackTrace();
        }

        return value;
    }

    public static boolean copyFile(String fromFile, String toFile){
        InputStream is = null;
        OutputStream os = null;
        try{
            is = new FileInputStream(fromFile);
            os = new FileOutputStream(toFile);
            byte bt[] = new byte[1024];
            int c;
            while((c = is.read(bt)) > 0){
                os.write(bt, 0, c);
            }

            return true;
        }catch(Exception ex){
            ex.printStackTrace();

            return false;
        }finally{
            CloseUtil.close(is, os);
        }
    }

    /**
     * 从指定的文件中读取字符串
     */
    public static String readDataFromFile(File file){

        BufferedReader br = null;
        FileInputStream fis = null;
        try{
            fis = new FileInputStream(file);
            InputStreamReader inputreader = new InputStreamReader(fis);
            br = new BufferedReader(inputreader);
            String line;
            StringBuffer content = new StringBuffer();
            //分行读取
            while((line = br.readLine()) != null){
                content.append(line + "\n");
            }

            return content.toString();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            CloseUtil.close(br, fis);
        }

        return "";
    }

    // 创建目录
    public static void createDirIfNotExist(String path){
        if(!StringUtil.isEmpty(path)){
            File file = new File(path);
            if(!file.exists()){
                file.mkdirs();
            }
        }
    }

    /**
     * whether the file path is sdcard path
     *
     * @param path file path
     * @return true means the file path is sdcard path
     */
    public static final boolean isSDCardPath(String path){
        if(StringUtil.isEmpty(path)){
            return false;
        }
        String sdRootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        return path.startsWith(sdRootPath);
    }

    /**
     * get available space(free space can use) by fileDirPath
     *
     * @param fileDirPath file dir path
     * @return available space,-1 means failed
     */
    public static long getAvailableSpace(String fileDirPath){
        try{
            File file = new File(fileDirPath);
            if(!file.exists()){
                file.mkdirs();// create to make sure it is not error below
            }
            final StatFs stats = new StatFs(fileDirPath);
            long result = (long) stats.getBlockSize() * (long) stats.getAvailableBlocks();
            return result;
        }catch(Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * whether the file exist
     *
     * @param filePath the check file path
     * @return true means exist
     */
    public static boolean isFileExist(String filePath){
        if(!isFilePath(filePath)){
            return false;
        }

        File file = new File(filePath);
        return file.exists() && file.isFile();

    }

    /**
     * whether the path is file path
     *
     * @param path file path
     * @return true means the path is file path
     */
    public static boolean isFilePath(String path){
        if(TextUtils.isEmpty(path)){
            return false;
        }
        return path.startsWith(File.separator);
    }

    /**
     * @param msg      写入的消息
     * @param parent   父目录
     * @param filename 文件名
     * @param isAppend 是否是追加
     * @return 是否保存成功
     */
    public static boolean saveFile(String msg, String parent, String filename, boolean isAppend){
        if(TextUtils.isEmpty(msg)){
            return false;
        }
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            File dir = new File(parent);
            if(!dir.exists()){
                dir.mkdirs();
            }

            try{
                FileWriter fw = new FileWriter(dir + File.separator + filename, isAppend);
                PrintWriter pw = new PrintWriter(fw, true);
                pw.println(msg);
                return true;
            }catch(IOException e){
                Log.e("FileUtil", "saveFile: 发生异常");
                e.printStackTrace();
                return false;
            }
        } else {
            Log.e("FileUtil", "saveFile: 没有内存卡");
            return false;
        }
    }

    /**
     * 删除文件夹里面的所以文件
     */
    public static void deleteDir(File dir){
        if(dir.exists()){
            File[] files = dir.listFiles();
            for(int i = 0; i < files.length; i++){
                if(files[i].isFile()){
                    files[i].delete();
                } else {
                    deleteDir(files[i]);
                }
            }
        }
    }

    /**
     * 拷贝目录
     */
    public static boolean copyDir(File sourceFile, File targetFile){
        if(sourceFile == null || targetFile == null){
            return false;
        }
        if(!sourceFile.exists()){
            return false;
        }
        if(!targetFile.exists()){
            targetFile.mkdirs();
        }
        // 获取目录下所有文件和文件夹的列表
        File[] files = sourceFile.listFiles();
        if(files == null || files.length < 1){
            return false;
        }
        File file = null;
        StringBuilder buffer = new StringBuilder();
        boolean isSuccessful = false;
        // 遍历目录下的所有文件文件夹，分别处理
        for(File file1 : files){
            file = file1;
            buffer.setLength(0);
            buffer.append(targetFile.getAbsolutePath()).append(File.separator).append(file.getName());
            if(file.isFile()){
                // 文件直接调用拷贝文件方法
                isSuccessful = copyFile(file.getAbsolutePath(), buffer.toString());
                if(!isSuccessful){
                    return false;
                }
            } else if(file.isDirectory()){
                // 目录再次调用拷贝目录方法
                copyDir(file, new File(buffer.toString()));
            }

        }
        return true;
    }

    /**
     * 将流写入指定文件
     *
     * @param inputStream
     * @param path
     * @return
     */
    public static boolean stream2File(InputStream inputStream, String path){
        File file = new File(path);
        boolean isSuccessful = true;
        FileOutputStream fileOutputStream = null;
        try{
            if(!file.exists()){
                File file2 = file.getParentFile();
                file2.mkdirs();
                file.createNewFile();
            }
            fileOutputStream = new FileOutputStream(file);
            byte[] bs = new byte[1024];
            int length = 0;
            while((length = inputStream.read(bs)) != -1){
                fileOutputStream.write(bs, 0, length);
            }
        }catch(Exception e){
            isSuccessful = false;
        }finally{
            CloseUtil.close(fileOutputStream);
        }
        return isSuccessful;
    }

    /**
     * 读取指定路径下的文件内容
     *
     * @param path
     * @return 文件内容
     */
    public static String readFile(String path){
        BufferedReader br = null;
        try{
            File myFile = new File(path);
            br = new BufferedReader(new FileReader(myFile));
            StringBuffer sb = new StringBuffer();
            String line = br.readLine();
            while(line != null){
                sb.append(line);
                line = br.readLine();
            }
            return sb.toString();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
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
    public static File createFile(String filePath){
        return createFile(filePath, "755");
    }

    /**
     * 创建文件，并修改读写权限
     *
     * @param filePath
     * @param mode
     * @return
     */
    public static File createFile(String filePath, String mode){
        File desFile = null;
        try{
            String desDir = filePath.substring(0, filePath.lastIndexOf(File.separator));
            File dir = new File(desDir);
            if(!dir.exists()){
                dir.mkdirs();
            }
            chmodFile(dir.getAbsolutePath(), mode);
            desFile = new File(filePath);
            if(!desFile.exists()){
                desFile.createNewFile();
            }
            chmodFile(desFile.getAbsolutePath(), mode);
        }catch(Exception e){
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
    public static void chmodFile(String fileAbsPath, String mode){
        String cmd = "chmod " + mode + " " + fileAbsPath;
        try{
            Runtime.getRuntime().exec(cmd);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
