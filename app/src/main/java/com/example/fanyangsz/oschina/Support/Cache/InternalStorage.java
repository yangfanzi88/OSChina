package com.example.fanyangsz.oschina.Support.Cache;

import android.content.Context;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by fanyang.sz on 2016/7/13.
 */

public class InternalStorage {
   /* //需要保存当前调用对象的Context
    private Context context;

    public InternalStorage(Context context) {
        this.context = context;
    }*/
    /**
     * 保存内容到内部存储器中
     * @param filename 文件名
     * @param content 内容
     */
    public static void fileSave(Context context, String filename, String content) throws IOException {
        // FileOutputStream fos=context.openFileOutput(filename,
        // Context.MODE_PRIVATE);
        File file = new File(context.getFilesDir(), filename);
        FileOutputStream fos = new FileOutputStream(file);

        fos.write(content.getBytes());
        fos.close();
    }
    /**
     *  通过文件名获取内容
     * @param filename 文件名
     * @return 文件内容
     */
    public static String fileGet(Context context, String filename) throws IOException {
        FileInputStream fis = context.openFileInput(filename);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = -1;
        while ((len = fis.read(data)) != -1) {
            baos.write(data, 0, len);
        }
        return new String(baos.toByteArray());
    }
    /**
     * 以追加的方式在文件的末尾添加内容
     * @param filename 文件名
     * @param content 追加的内容
     */
    public static void fileAppend(Context context, String filename, String content) throws IOException {
        //自己写的为了迎合news blog tweet等带文件头的追加
        /*String s = fileGet(context,filename);
        s = s.substring(0,s.length()-1);
        content = s+","+ content.substring(1,content.length());
        FileOutputStream fos = context.openFileOutput(filename,
                Context.MODE_PRIVATE);
        fos.write(content.getBytes());
        fos.close();*/

        //原来的方法
        FileOutputStream fos = context.openFileOutput(filename,
                Context.MODE_APPEND);
        fos.write(content.getBytes());
        fos.close();
    }

    /**
     * 自己写的为了保存带文件头的追加
     * @param context
     * @param filename
     * @param content
     * @param type
     * @throws IOException
     */
    public static void fileAppend(Context context, String filename, String content, String type) throws IOException {
        String s = fileGet(context,filename);
        String typeStart = "<" + type + ">";
        String typeEnd = "</" + type + ">";

        int i = s.indexOf(typeEnd);
        int j = content.indexOf(typeStart);

        content = s.substring(0, i) + content.substring(j+typeStart.length());
        FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
        fos.write(content.getBytes());
        fos.close();

    }
    /**
     * 删除文件
     * @param filename 文件名
     * @return 是否成功
     */
    public static boolean fileDelete(Context context, String filename) {
        return context.deleteFile(filename);
    }
    /**
     * 获取内部存储路径下的所有文件名
     * @return 文件名数组
     */
    public static String[] queryAllFile(Context context) {
        return context.fileList();
    }

    /**
     * 判断缓存数据是否过期
     * @param context
     * @param name
     * @return
     */
    public static boolean isDataFailure(Context context, String name){
        boolean failure = false;
        File data = context.getFileStreamPath(name);
        if (data.exists()
                && (System.currentTimeMillis() - data.lastModified()) > CacheConfig.REFRESH_INTERVAL)
            failure = true;
        else if (!data.exists())
            failure = true;
        return failure;
    }
}

