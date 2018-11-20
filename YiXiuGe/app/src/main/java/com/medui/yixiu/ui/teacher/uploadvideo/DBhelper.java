package com.medui.yixiu.ui.teacher.uploadvideo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.xson.common.utils.L;


/**
 * Created by xkai
 */

public class DBhelper extends SQLiteOpenHelper {

    public static String TAG="DBhelper";

    private static String dbName="zhongshanyiyuan.db";
    private static int dbVersion = 3;

    public DBhelper(Context context) {
        super(context, dbName, null, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        L.d(TAG,"创建数据库成功:"+dbVersion);
        db.execSQL(DBUploadViewHelper.createTable);
//        update(0,db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        L.d(TAG,"更新数据库成功:"+oldVersion+"  newVersion:"+newVersion);
//        update(oldVersion,db);

    }

    private void update(int oldVersion,SQLiteDatabase db){
        switch (oldVersion){
            case 0:
                db.execSQL(DBUploadViewHelper.createTable);
            case 1:
                db.execSQL(DBUploadViewHelper.createTable);
            case 2:
                db.execSQL("alter table " +DBUploadViewHelper.Video +
                        " ADD " +DBUploadViewHelper.Video_title +" INTEGER ");
                db.execSQL(DBUploadViewHelper.createTable);
//                db.execSQL(DBDeviceListManager.createTable);
//            case 3:
//                db.execSQL("alter table " +DBShopCartHelper.Video+
//                        " ADD " +DBShopCartHelper.Cart_goodsIsCheck+" INTEGER Default 1");
//            case 4:
//                db.execSQL(DBDeviceErrManager.createTable_notexists);
//            case 5:
//                db.execSQL(DBSearchManager.createTable);
//
//                break;

        }
    }

    public void deleteDatabase(Context context){
        context.deleteDatabase(dbName);
    }

}
