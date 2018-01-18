package com.example.emotionkeyboard.util;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.example.emotionkeyboard.view.manager.LocalPicManagerActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by chasing on 2017/12/18.
 * 文件工具
 */
public class FileUtil {
    private static File EmotionFileDir;
    private static ExecutorService mThreadExecutor;

    /**
     * 在app进行初始化文件夹
     */
    public static void initFile(Context ctx) {
        File BASE_PATH_FILE;
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            BASE_PATH_FILE = ctx.getFilesDir();
        } else {
            BASE_PATH_FILE = new File(Environment.getExternalStorageDirectory(), "emotion");
        }
        EmotionFileDir = new File(BASE_PATH_FILE, "user_pic");
        EmotionFileDir.mkdirs();
    }

    /**
     * 保存文件
     *
     * @param saveName 文件名，包含后缀
     * @param bytes    文件的字节数据
     */
    private static String saveFile(final Context context, final String saveName, final byte[] bytes) {
        if (!EmotionFileDir.exists()) {
            boolean isSuccess = EmotionFileDir.mkdirs();
            if (!isSuccess) {
                // 不存在且创建失败说明用户禁止了存储权限
                Toast.makeText(context, "请开启存储权限！", Toast.LENGTH_SHORT).show();
                return null;
            }
        }
        if (mThreadExecutor == null) {
            mThreadExecutor = Executors.newFixedThreadPool(5);
        }
        final File file = new File(EmotionFileDir, saveName);
        if (file.exists()) return "";
        mThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    FileOutputStream outputStream = new FileOutputStream(file);
                    outputStream.write(bytes);
                    outputStream.close();
//            ActivityUtils.showToast(context, "图片已保存至" + EmotionFileDir.getAbsolutePath() + "文件夹");
                    //通知系统图库更新
                    context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + EmotionFileDir + "/" + saveName)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return null;
    }

    /**
     * 保存图片到手机
     */
    public static String saveImageToPhonePng(Context context, String fileName, Bitmap bm) {
        //先保存图片
        String saveName = fileName + ".png";
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] bitmapBytes = baos.toByteArray();
            baos.flush();
            return saveFile(context, saveName, bitmapBytes);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null)
                    baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 保存Gif图片
     */
    public static String saveImageToPhoneGif(Context context, String fileName, byte[] bytes) {
        //先保存图片
        String saveName = fileName + ".gif";
        return saveFile(context, saveName, bytes);
    }

    /**
     * 获取表情文件列表
     */
    public static File[] getEmotionFiles() {
        if (!EmotionFileDir.exists() || !EmotionFileDir.isDirectory()) {
            return null;
        }
        return EmotionFileDir.listFiles();
    }

    /**
     * 获取系统图库的intent
     */
    public static Intent getSystemImageIntent() {
        Intent intent = new Intent();
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        //根据版本号不同使用不同的Action
        if (Build.VERSION.SDK_INT < 19) {
            intent.setAction(Intent.ACTION_GET_CONTENT);
        } else {
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        }
        return intent;
    }

    /**
     * 将系统图片保存到本地指定的文件夹
     */
    public static void saveSystemBitmap2Local(final Context context, final Uri uri, final Handler handler) {
        if (mThreadExecutor == null) {
            mThreadExecutor = Executors.newFixedThreadPool(5);
        }
        Cursor cursor = context.getContentResolver().query(uri, null, null,
                null, null);
        if (cursor == null) {
            handler.sendEmptyMessage(LocalPicManagerActivity.HANDLER_HAD_EXIST);
            return;
        }
        cursor.moveToFirst();
//        String imgNo = cursor.getString(0); // 图片编号
//        String imgPath = cursor.getString(1); // 图片文件路径 ( image/jpeg、image/gif... )
        String saveName = cursor.getString(2);// 图片文件名包含后缀
        cursor.close();
        if (saveName.contains("/")) {
            saveName = saveName.substring(saveName.lastIndexOf('/') + 1);
        }
        final File file = new File(EmotionFileDir, saveName);
        if (file.exists()) {
            handler.sendEmptyMessage(LocalPicManagerActivity.HANDLER_HAD_EXIST);
            return;
        }

        mThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                InputStream inputStream = null;
                FileOutputStream outputStream = null;
                try {
                    outputStream = new FileOutputStream(file);
                    byte[] temp = new byte[1024];
                    inputStream = context.getContentResolver().openInputStream(uri);
                    while (inputStream.read(temp) > 0){
                        outputStream.write(temp);
                    }
//                    context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + EmotionFileDir + "/" + finalSaveName)));

                    Message msg = handler.obtainMessage();
                    msg.obj = file.getAbsolutePath();
                    msg.what = LocalPicManagerActivity.HANDLER_SUCCESS;
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(LocalPicManagerActivity.HANDLER_FAIL);
                } finally {
                    try {
                        if (inputStream != null)
                            inputStream.close();
                        if (outputStream != null)
                            outputStream.close();
                    } catch (Exception ignored) {
                    }
                }
            }
        });
    }

    public static void deleteLocalPicFile(String path){
        File file = new File(path);
        if (!file.exists()) return;
        file.delete();
    }
}
