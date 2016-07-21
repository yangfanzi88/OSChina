package com.example.fanyangsz.oschina.Support.util;

import android.content.Context;
import android.util.Log;

import com.example.fanyangsz.oschina.Support.sqlite.SqliteUtility;
import com.example.fanyangsz.oschina.Support.sqlite.SqliteUtilityBuilder;

import static android.R.attr.versionCode;

/**
 * Created by fanyang.sz on 2016/7/11.
 */

public class OSChinaDB {
    static final String DB_NAME = "OSChina";

    public static void setInitDB(Context context){
        Context mContext = context;

        try {
            int versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
            try {
                Log.w("OSChina", "初始化 db versionCode = " + versionCode);
                new SqliteUtilityBuilder().configVersion(versionCode).configDBName(DB_NAME).build(mContext);
                // 新增日志上报DB
//                new SqliteUtilityBuilder().configVersion(versionCode).configDBName(ANALYSIS_DB_NAME).build(mContext);
            } catch (Throwable e) {
                e.printStackTrace();
                Log.w("OSChina", "初始化 db versionCode = " + 60000);

                new SqliteUtilityBuilder().configVersion(60000).configDBName(DB_NAME).build(mContext);
//                new SqliteUtilityBuilder().configVersion(60000).configDBName(ANALYSIS_DB_NAME).build(mContext);
            }
        } catch (Throwable e) {
            Logger.printExc(OSChinaDB.class, e);
        }
    }
    public static SqliteUtility getDB() {
        return SqliteUtility.getInstance(DB_NAME);
    }
}
