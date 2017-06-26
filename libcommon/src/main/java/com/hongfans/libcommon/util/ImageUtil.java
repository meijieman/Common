package com.hongfans.libcommon.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.View;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageUtil{

    private static final String TAG = "ImageUtils";

    /**
     * Stores an image on the storage
     *
     * @param image       the image to store.
     * @param pictureFile the file in which it must be stored
     */
    public static void storeImage(Bitmap image, File pictureFile){
        if(pictureFile == null){
            Log.d(TAG, "Error creating media file, check storage permissions: ");
            return;
        }
        try{
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch(FileNotFoundException e){
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch(IOException e){
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
    }

    /**
     * Get the screen height.
     */
    public static int getScreenHeight(Activity context){
        Display display = context.getWindowManager().getDefaultDisplay();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2){
            Point size = new Point();
            display.getSize(size);
            return size.y;
        }

        return display.getHeight();
    }

    /**
     * Get the screen width.
     */
    public static int getScreenWidth(Activity context){

        Display display = context.getWindowManager().getDefaultDisplay();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2){
            Point size = new Point();
            display.getSize(size);
            return size.x;
        }
        return display.getWidth();
    }

    public static Bitmap convertGreyImg(Bitmap img){
        int width = img.getWidth();
        int height = img.getHeight();

        int[] pixels = new int[width * height];

        img.getPixels(pixels, 0, width, 0, 0, width, height);
        int alpha = 0xFF << 24;
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                int grey = pixels[width * i + j];

                int red = ((grey & 0x00FF0000) >> 16);
                int green = ((grey & 0x0000FF00) >> 8);
                int blue = (grey & 0x000000FF);

                grey = (int)((float)red * 0.3 + (float)green * 0.59 + (float)blue * 0.11);
                grey = alpha | (grey << 16) | (grey << 8) | grey;
                pixels[width * i + j] = grey;
            }
        }
        Bitmap result = Bitmap.createBitmap(width, height, Config.RGB_565);
        result.setPixels(pixels, 0, width, 0, 0, width, height);
        return result;
    }

    public static Bitmap scaleBitmap(Bitmap bgimage, int newWidth, int newHeight){
        int width = bgimage.getWidth();
        int height = bgimage.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float)newWidth) / width;
        float scaleHeight = ((float)newHeight) / height;
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, width, height, matrix, true);
        return bitmap;
    }

    public static Bitmap compressBitmap(String fromFile, String toFile, int quality){
        Bitmap bitmap = BitmapFactory.decodeFile(fromFile);
        return compressBitmap(bitmap, toFile, quality);
    }

    public static Bitmap compressBitmap(Bitmap bitmap, String toFile, int quality){
        Bitmap newBM = null;
        FileOutputStream out = null;
        try{
            File myCaptureFile = new File(toFile);
            if(!myCaptureFile.exists()){
                File dir = myCaptureFile.getParentFile();
                if(!dir.exists()){
                    dir.mkdirs();
                }
                myCaptureFile.createNewFile();
            }
            out = new FileOutputStream(myCaptureFile);
            if(bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)){
                out.flush();
                out.close();
            }
            // Release memory resources
            if(!bitmap.isRecycled()){
                bitmap.recycle();
            }
            if(!bitmap.isRecycled()){
                bitmap.recycle();
            }
            newBM = BitmapFactory.decodeFile(toFile);
        } catch(Exception e){
            Log.i(TAG, e.toString());
        } finally {
            try{
                if(out != null){
                    out.close();
                }
            } catch(Exception e){
                Log.i(TAG, e.toString());
            }
        }
        return newBM;
    }

    public static Bitmap compressBitmap(InputStream inputstream, String toFile, int quality){
        Bitmap bitmap = BitmapFactory.decodeStream(inputstream);
        return compressBitmap(bitmap, toFile, quality);
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx){

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        // 规避当创建图片失败时，Canvas绘制图片异常问题；直接返回原图片
        if(output == null){
            return bitmap;
        }
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static Bitmap getViewBitmap(View v){

        v.clearFocus();
        v.setPressed(false);

        boolean willNotCache = v.willNotCacheDrawing();
        v.setWillNotCacheDrawing(false);
        int color = v.getDrawingCacheBackgroundColor();
        v.setDrawingCacheBackgroundColor(0);
        if(color != 0){
            v.destroyDrawingCache();
        }
        v.buildDrawingCache();
        Bitmap cacheBitmap = v.getDrawingCache();
        if(cacheBitmap == null){
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);
        // Restore the view
        v.destroyDrawingCache();
        v.setWillNotCacheDrawing(willNotCache);
        v.setDrawingCacheBackgroundColor(color);
        return bitmap;
    }

    public static Bitmap decodeBitmap(Context context, File imageFile){
        return decodeBitmap(context, imageFile, 32);
    }

    public static Bitmap decodeBitmap(Context context, File imageFile, int maxInSampleSize){
        Bitmap bitmap = null;
        Options options = new Options();
        options.inJustDecodeBounds = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inDither = true;
        options.inTempStorage = new byte[12 * 1024];
        options.inSampleSize = 1;

        // 设置最大缩放比例
        if(maxInSampleSize < 1){
            maxInSampleSize = 64;
        }

        while(null == bitmap){
            bitmap = getScaledBitmap(context, imageFile, options);

            options.inSampleSize = options.inSampleSize * 2;

            if(options.inSampleSize > maxInSampleSize * 2){
                return null;
            }
        }
        return bitmap;
    }

    public static Bitmap getScaledBitmap(Context context, File imageFile, Options options){
        Bitmap bitmap = null;
        // 获取资源图片
        FileInputStream fileInputStream = null;
        InputStream inputStream = null;
        try{
            fileInputStream = new FileInputStream(imageFile);
            inputStream = new BufferedInputStream(fileInputStream);
            bitmap = BitmapFactory.decodeStream(inputStream, null, options);
        } catch(OutOfMemoryError err){
            Log.i(TAG, "inSampleSize" + "-----" + options.inSampleSize + "-------" + err);
            return null;
        } catch(FileNotFoundException e){
            Log.i(TAG, e.getLocalizedMessage());
        } finally {
            closeInputStream(inputStream);
            closeInputStream(fileInputStream);
        }

        return bitmap;
    }

    private static void closeInputStream(InputStream stream){
        if(null != stream){
            try{
                stream.close();
            } catch(IOException e){
                Log.i(TAG, e.getLocalizedMessage());
            }
        }
    }

    /**
     * bitmap-->byte[]
     */
    public static byte[] bitmap2Bytes(Bitmap bm){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * bytes[]->bitmap
     */
    public static Bitmap bytes2Bitmap(byte[] bytes){
        if(bytes == null || bytes.length == 0){
            return null;
        }
        BitmapFactory bitmapFactory = new BitmapFactory();
        Bitmap bitMap = bitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return bitMap;
    }

    /**
     * 把bitmap转换成String
     */
    public static String bitmapToString(Bitmap bm){

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
        byte[] b = baos.toByteArray();

        return Base64.encodeToString(b, Base64.DEFAULT);

    }

    /**
     * 保存图片到sd卡 /data/data/com.maizuo.main/files/
     */
    public boolean saveImage(Context context, Bitmap bitmap, String fileName){
        boolean bool = false;
        BufferedOutputStream bos = null;
        BufferedInputStream bis = null;
        ByteArrayOutputStream baos = null;
        try{
            // if (FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) {
            // Log.w(TAG, "Low free space onsd, do not cache");
            // return false;
            // }
            bos = new BufferedOutputStream(context.openFileOutput(fileName, 0));

            baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            bis = new BufferedInputStream(new ByteArrayInputStream(baos.toByteArray()));
            int b = -1;
            while((b = bis.read()) != -1){
                bos.write(b);
            }
            bool = true;
        } catch(Exception e){
            bool = false;

        } finally {
            try{
                if(bos != null){
                    bos.close();
                }
                if(bis != null){
                    bis.close();
                }
            } catch(IOException e){
                bool = false;

            }
        }
        return bool;
    }

    /**
     * 删掉图片
     */
    public boolean deleteImage(Context context, String fileName){
        return context.deleteFile(fileName);
    }
}
