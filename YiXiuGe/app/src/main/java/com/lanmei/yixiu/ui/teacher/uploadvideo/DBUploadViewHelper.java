package com.lanmei.yixiu.ui.teacher.uploadvideo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lanmei.yixiu.utils.CommonUtils;
import com.xson.common.utils.L;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/2.
 */

public class DBUploadViewHelper {

    private Context context;
    private DBhelper dBhelper;
    private SQLiteDatabase db;

    public static final String Video = "video";//

    public static final String Video_id = "id";
    public static final String Video_user_id = "uid";
    public static final String Video_title = "title";
    public static final String Video_path = "path";
    public static final String Video_pic = "pic";
    public static final String Video_status = "status";
    public static final String Video_progress = "progress";


    public static final String createTable = "CREATE TABLE " + Video +
            "(" + Video_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            Video_user_id + " TEXT, " +
            Video_pic + " TEXT, " +
            Video_path + " TEXT, " +
            Video_title + " TEXT, " +
            Video_status + " TEXT, " +
            Video_progress + " INTEGER)";

    public static String uid;
    public static double momey = 0;
    public static int goodsCount = 0;
    public static DBUploadViewHelper dbGoodsCartManager;

    public DBUploadViewHelper(Context context) {
        this.context = context;
        uid = CommonUtils.getUserId(context);
        dBhelper = DBhelper.newInstance(context);
        db = dBhelper.getWritableDatabase();
    }

    public static DBUploadViewHelper getInstance(Context context) {
        if (dbGoodsCartManager == null) {
            dbGoodsCartManager = new DBUploadViewHelper(context);
        }
        return dbGoodsCartManager;
    }

    /**
     * 获取当前账户的上传视频列表
     */
    public List<UploadVideoBean> getUploadVideoList() {
        List<UploadVideoBean> shopCarBeanList = new ArrayList<>();
        String selection = Video_user_id + " = " + uid;
        Cursor c = db.query(Video, null, selection, null, null, null, null);
        momey = 0;
        goodsCount = 0;
        if (c.getCount() > 0) {
            UploadVideoBean videoBean;
            while (c.moveToNext()) {
                videoBean = new UploadVideoBean();
                videoBean.setProgress(c.getInt(c.getColumnIndex(Video_progress)));
                videoBean.setPic(c.getString(c.getColumnIndex(Video_pic)));
                videoBean.setPath(c.getString(c.getColumnIndex(Video_path)));
                videoBean.setTitle(c.getString(c.getColumnIndex(Video_title)));
                videoBean.setStatus(c.getString(c.getColumnIndex(Video_status)));
                shopCarBeanList.add(videoBean);
            }
        }
        L.d(Video, "c.getCount():" + c.getCount());
        c.close();
        return shopCarBeanList;
    }

    //获取购物车个数
    public int getShopCarListCount() {
        String selection = Video_user_id + " = " + uid;
        Cursor c = db.query(Video, null, selection, null, null, null, null);
        return c.getCount();
    }


    //（将数据插入数据库）
    public long insertUploadVideoBean(UploadVideoBean bean) {
        String selection = Video_user_id + " = " + uid;
        Cursor c = db.query(Video, null, selection, null, null, null, null);
        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                if (StringUtils.isSame(c.getString(c.getColumnIndex(Video_path)), bean.getPath())) {//已经插入的商品（根据id判断）
                    UIHelper.ToastMessage(context, "该视频已经在上传视频列表中...");
                    c.close();
                    return 0;
                }
            }
        }
        ContentValues values = new ContentValues();
        values.put(Video_user_id, uid);
        values.put(Video_pic, bean.getPic());
        values.put(Video_title,bean.getTitle());
        values.put(Video_status,bean.getStatus());
        values.put(Video_path,bean.getPath());
//        values.put(Cart_goodsParams, bean.getSpec());
        values.put(Video_progress, bean.getProgress());
        long insC = db.insert(Video, Video_pic, values);
        if (insC > 0) {
            UIHelper.ToastMessage(context, "视频上传中...");
//            EventBus.getDefault().post(new UploadVideoBean());
        }
        c.close();
        return insC;
    }

    public void deleteDatabase() {
        dBhelper.deleteDatabase(context);
    }


    public void delete(List<UploadVideoBean> list) {
        String where = Video_path + "=?";
        int size = list.size();
        for (int i = 0; i < size; i++) {
            db.delete(Video, where, new String[]{list.get(i).getPath()});
        }
    }

    /**
     * 根据goodsId 更新商品个数
     *
     * @param goodsId
     * @param count
     */
    public void update(String goodsId, int count) {
        ContentValues cv = new ContentValues();
        cv.put(Video_progress, count);
        db.update(Video, cv, Video_pic + "=" + goodsId, null);
    }

    /**
     * 根据goodsId 更新商品个数、图片、商品名称，价格
     *
     * @param goodsId
     * @param count
     * @param imge
     * @param goodsName
     * @param sellPrice
     */
    public void updateGoods(String goodsId, int count, String imge, String goodsName, double sellPrice) {
        ContentValues cv = new ContentValues();
        cv.put(Video_progress, count);
//        values.put(Cart_goodsParams, bean.getSpec());
        db.update(Video, cv, Video_pic + "=" + goodsId, null);
    }

}
