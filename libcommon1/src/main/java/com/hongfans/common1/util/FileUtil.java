package com.hongfans.common1.util;

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
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author 写文件的工具类
 */
public class FileUtil{

    public static final String ROOT_DIR = "Android/data/" + UIUtils.getPackageName();

    /**
     * 获取应用目录，当SD卡存在时，获取SD卡上的目录，当SD卡不存在时，获取应用的cache目录
     *
     * @param name
     * @return
     */
    public static String getDir(String name){
        StringBuilder sb = new StringBuilder();
        if(SDCardUtil.isSdCardMounted()){
            sb.append(getExternalStoragePath());
        } else {
            sb.append(getCachePath());
        }
        sb.append(name);
        sb.append(File.separator);
        String path = sb.toString();
        if(createDirs(path)){
            return path;
        } else {
            return null;
        }
    }

    /** 获取SD下的应用目录 */
    public static String getExternalStoragePath(){
        StringBuilder sb = new StringBuilder();
        sb.append(Environment.getExternalStorageDirectory().getAbsolutePath());
        sb.append(File.separator);
        sb.append(ROOT_DIR);
        sb.append(File.separator);
        return sb.toString();
    }

    /** 获取应用的cache目录 */
    public static String getCachePath(){
        File f = UIUtils.getContext().getCacheDir();
        if(null == f){
            return null;
        } else {
            return f.getAbsolutePath() + "/";
        }
    }

    /** 创建文件夹 */
    public static boolean createDirs(String dirPath){
        File file = new File(dirPath);
        if(!file.exists() || !file.isDirectory()){
            return file.mkdirs();
        }
        return true;
    }

    /** 复制文件，可以选择是否删除源文件 */
    public static boolean copyFile(String srcPath, String destPath, boolean deleteSrc){
        File srcFile = new File(srcPath);
        File destFile = new File(destPath);
        return copyFile(srcFile, destFile, deleteSrc);
    }

    /** 复制文件，可以选择是否删除源文件 */
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
        } catch(Exception e){
            return false;
        } finally {

        }

        return true;
    }

    /** 修改文件的权限,例如"777"等 */
    public static void chmod(String path, String mode){
        try{
            String command = "chmod " + mode + " " + path;
            Runtime runtime = Runtime.getRuntime();
            runtime.exec(command);
        } catch(Exception e){
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
        } catch(Exception e){
            e.printStackTrace();
        } finally {


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
        } catch(Exception e){
            e.printStackTrace();
        } finally {

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
    public static void writeProperties(String filePath, String key,
            String value, String comment){
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
        } catch(Exception e){
            e.printStackTrace();
        } finally {
        }
    }

    /** 根据值读取 */
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
        } catch(IOException e){
            e.printStackTrace();
        } finally {

        }
        return value;
    }

    /** 把字符串键值对的map写入文件 */
    public static void writeMap(String filePath, Map<String, String> map, boolean append, String comment){
        if(map == null || map.size() == 0 || StringUtil.isEmpty(filePath)){
            return;
        }
        FileInputStream fis = null;
        FileOutputStream fos = null;
        File f = new File(filePath);
        try{
            if(!f.exists() || !f.isFile()){
                f.createNewFile();
            }
            Properties p = new Properties();
            if(append){
                fis = new FileInputStream(f);
                p.load(fis);// 先读取文件，再把键值对追加到后面
            }
            p.putAll(map);
            fos = new FileOutputStream(f);
            p.store(fos, comment);
        } catch(Exception e){
            e.printStackTrace();
        } finally {

        }
    }

    /** 把字符串键值对的文件读入map */
    public static Map<String, String> readMap(String filePath, String defaultValue){
        if(StringUtil.isEmpty(filePath)){
            return null;
        }
        Map<String, String> map = null;
        FileInputStream fis = null;
        File f = new File(filePath);
        try{
            if(!f.exists() || !f.isFile()){
                f.createNewFile();
            }
            fis = new FileInputStream(f);
            Properties p = new Properties();
            p.load(fis);
            map = new HashMap<>((Map)p);// 因为properties继承了map，所以直接通过p来构造一个map
        } catch(Exception e){
            e.printStackTrace();
        } finally {
        }
        return map;
    }

    /** 改名 */
    public static boolean copy(String src, String des, boolean delete){
        File file = new File(src);
        if(!file.exists()){
            return false;
        }
        File desFile = new File(des);
        FileInputStream in = null;
        FileOutputStream out = null;
        try{
            in = new FileInputStream(file);
            out = new FileOutputStream(desFile);
            byte[] buffer = new byte[1024];
            int count = -1;
            while((count = in.read(buffer)) != -1){
                out.write(buffer, 0, count);
                out.flush();
            }
        } catch(Exception e){
            e.printStackTrace();
            return false;
        } finally {
        }
        if(delete){
            file.delete();
        }
        return true;
    }

    public static boolean createFolderIfNotExist(String strFolder){

        File basefile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/rearview");
        if(!basefile.exists()){
            boolean result = basefile.mkdir();
        }
        File file = new File(strFolder);

        if(!file.exists()){
            return file.mkdir();
        }
        return true;
    }

    public static boolean copyFile(String fromFile, String toFile){
        try{
            InputStream is = new FileInputStream(fromFile);
            OutputStream os = new FileOutputStream(toFile);
            byte bt[] = new byte[1024];
            int c;
            while((c = is.read(bt)) > 0){
                os.write(bt, 0, c);
            }
            is.close();
            os.close();

            return true;
        } catch(Exception ex){
            ex.printStackTrace();
            return false;
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

        } catch(Exception e){
            e.printStackTrace();
        } finally {
            try{
                if(br != null){
                    br.close();
                }

                if(fis != null){
                    fis.close();
                }
            } catch(IOException e){
                e.printStackTrace();
            }
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
     * 获取指定文件夹、文件大小
     */
    public static long getFileSize(File f){
        long size = 0L;
        File[] list = f.listFiles();
        if(list == null || list.length == 0){
            return 0L;
        }
        for(File file : list){
            if(file.isDirectory()){
                size = size + getFileSize(file);
            } else {
                // 排除播放文件
                if(!file.getName().equalsIgnoreCase("cache.db") && !file.getName().equalsIgnoreCase("private_data.db")){
                    size = size + file.length();
                }
            }
        }

        return size;
    }


    /**
     * create parent dir by file path
     */
    public static boolean createFileParentDir(String filePath){
        File file = new File(filePath);
        if(file.exists()){
            return true;// parent dir exist
        } else {
            File parentFile = file.getParentFile();
            if(parentFile != null){
                if(parentFile.exists()){
                    return true;// parent dir exist
                } else {
                    return parentFile.mkdirs();// create parent dir
                }
            }
        }
        return false;
    }

    /**
     * get file suffix by file path
     *
     * @param filePath file path
     * @return file suffix,return null means failed
     */
    public static String getFileSuffix(String filePath){
        if(!TextUtils.isEmpty(filePath)){
            int start = filePath.lastIndexOf(".");
            if(start != -1){
                return filePath.substring(start + 1);
            }
        }
        return null;
    }

    /**
     * whether the file path can write
     *
     * @param path file path
     * @return true means can write to
     */
    public static final boolean canWrite(String path){
        // if sdcard,needs the permission:  android.permission.WRITE_EXTERNAL_STORAGE
        if(isSDCardPath(path)){
            if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
                // TODO write bytes for test
                return true;
            }
        } else {
            // TODO write bytes for test
            return true;
        }
        return false;
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
            long result = (long)stats.getBlockSize() * (long)stats.getAvailableBlocks();
            return result;
        } catch(Exception e){
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
        return file != null && file.exists() && file.isFile();

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

    public static boolean deleteFile(String path){
        return deleteFile(new File(path));
    }


    /**
     * 从SD卡中删除文件
     */
    public static boolean deleteFile(File file){
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            if(file.exists()){
                if(file.isFile()){
                    return file.delete();
                } else if(file.isDirectory()){
                    // 如果它是一个目录
                    File files[] = file.listFiles();
                    for(int i = 0; i < files.length; i++){ // 遍历目录下所有的文件
                        deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
                    }
                }
                return file.delete();
            }
        }

        return false;
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
            } catch(IOException e){
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
     * 剪切目录，先将目录拷贝完后再删除源目录
     *
     * @param sourceDir
     * @param targetDir
     * @return
     */
    public static boolean cutDir(String sourceDir, String targetDir){
        File sourceFile = new File(sourceDir);
        File targetFile = new File(targetDir);
        boolean isCopySuccessful = copyDir(sourceFile, targetFile);
        if(isCopySuccessful){
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
    public static boolean deleteDir(String dir){
        File file = new File(dir);
        if(!file.exists()){
            return false;
        }
        File[] files = file.listFiles();
        boolean isSuccessful = false;
        if(files.length == 0){
            file.delete();
            return true;
        }
        // 对所有列表中的路径进行判断是文件还是文件夹
        for(int i = 0; i < files.length; i++){
            if(files[i].isDirectory()){
                isSuccessful = deleteDir(files[i].getAbsolutePath());
            } else if(files[i].isFile()){
                isSuccessful = deleteFile(files[i].getAbsolutePath());
            }

            if(!isSuccessful){
                // 如果有删除失败的情况直接跳出循环
                break;
            }
        }
        if(isSuccessful){
            file.delete();
        }
        return isSuccessful;
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
        } catch(Exception e){
            isSuccessful = false;
        } finally {
            try{
                if(fileOutputStream != null){
                    fileOutputStream.close();
                }
            } catch(IOException e){
            }
        }
        return isSuccessful;
    }

    /**
     * 创建目录
     *
     * @param path
     */
    public static void createDir(String path){
        File file = new File(path);
        if(!file.exists()){
            file.mkdirs();
        }
    }

    /**
     * 将object对象写入outFile文件
     *
     * @param outFile
     * @param object
     * @param context
     */
    public static void writeObject2File(String outFile, Object object, Context context){
        ObjectOutputStream out = null;
        FileOutputStream outStream = null;
        try{
            File dir = context.getDir("cache", Context.MODE_PRIVATE);
            outStream = new FileOutputStream(new File(dir, outFile));
            out = new ObjectOutputStream(new BufferedOutputStream(outStream));
            out.writeObject(object);
            out.flush();
        } catch(Exception e){
        } finally {
            if(outStream != null){
                try{
                    outStream.close();
                } catch(IOException e){
                }
            }
            if(out != null){
                try{
                    out.close();
                } catch(IOException e){
                }
            }
        }
    }

    /**
     * 从outFile文件读取对象
     *
     * @param filePath
     * @param context
     * @return
     */
    public static Object readObjectFromPath(String filePath, Context context){
        Object object = null;
        ObjectInputStream in = null;
        FileInputStream inputStream = null;
        try{
            File dir = context.getDir("cache", Context.MODE_PRIVATE);
            File f = new File(dir, filePath);
            if(f == null || !f.exists()){
                return null;
            }
            inputStream = new FileInputStream(new File(dir, filePath));
            in = new ObjectInputStream(new BufferedInputStream(inputStream));
            object = in.readObject();
        } catch(Exception e){
        } finally {
            if(in != null){
                try{
                    in.close();
                } catch(IOException e){
                }
            }
            if(inputStream != null){
                try{
                    inputStream.close();
                } catch(IOException e){
                }
            }

        }
        return object;
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
        } catch(Exception e){
        } finally {
            if(br != null){
                try{
                    br.close();
                } catch(IOException e){
                }
            }
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
        } catch(Exception e){
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
        } catch(Exception e){
        }
    }


}