package com.medui.yixiu.ui.teacher.uploadvideo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.medui.yixiu.utils.CommonUtils;
import com.xson.common.utils.StringUtils;

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
    public static final String Video_cid = "cid";//分类
    public static final String Video_pic = "pic";
    public static final String Video_status = "status";
    public static final String Video_progress = "progress";


    public static final String createTable = "CREATE TABLE " + Video +
            "(" + Video_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            Video_user_id + " TEXT, " +
            Video_pic + " TEXT, " +
            Video_cid + " TEXT, " +
            Video_path + " TEXT, " +
            Video_title + " TEXT, " +
            Video_status + " TEXT, " +
            Video_progress + " INTEGER)";

    public static String uid;

    public DBUploadViewHelper(Context context) {
        this.context = context;
        uid = CommonUtils.getUserId(context);
        dBhelper = new DBhelper(context);
        db = dBhelper.getWritableDatabase();
    }

    /**
     * 获取当前账户的上传视频列表
     */
    public List<UploadVideoBean> getUploadVideoList() {
        List<UploadVideoBean> shopCarBeanList = new ArrayList<>();
        String selection = Video_user_id + " = " + uid;
        Cursor c = db.query(Video, null, selection, null, null, null, null);
        if (c.getCount() > 0) {
            UploadVideoBean videoBean;
            while (c.moveToNext()) {
                videoBean = new UploadVideoBean();
                videoBean.setProgress(c.getInt(c.getColumnIndex(Video_progress)));
                videoBean.setPic(c.getString(c.getColumnIndex(Video_pic)));
                videoBean.setPath(c.getString(c.getColumnIndex(Video_path)));
                videoBean.setTitle(c.getString(c.getColumnIndex(Video_title)));
                videoBean.setCid(c.getString(c.getColumnIndex(Video_cid)));
                videoBean.setStatus(c.getString(c.getColumnIndex(Video_status)));
                shopCarBeanList.add(videoBean);
            }
        }
        c.close();
        return shopCarBeanList;
    }

    //获取要上传的视频个数
    public int getUploadVideoCount() {
        String selection = Video_user_id + " = " + uid;
        Cursor c = db.query(Video, null, selection, null, null, null, null);
        int count = c.getCount();
        c.close();
        return count;
    }


    //（将数据插入数据库）
    public long insertUploadVideoBean(UploadVideoBean bean) {
        String selection = Video_user_id + " = " + uid;
        Cursor c = db.query(Video, null, selection, null, null, null, null);
        String path = bean.getPath();
        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                if (StringUtils.isSame(c.getString(c.getColumnIndex(Video_path)), path)) {//已经插入的商品（根据id判断）
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
        values.put(Video_cid,bean.getCid());
        values.put(Video_path,path);
        values.put(Video_progress, bean.getProgress());
        long insC = db.insert(Video, Video_pic, values);
//        if (insC > 0) {
//        }
        c.close();
        return insC;
    }

    //
    public void deleteDatabase() {
        dBhelper.deleteDatabase(context);
    }

    //删除选中的上传视频任务
    public void delete(List<UploadVideoBean> list) {
        if (StringUtils.isEmpty(list)){
            return;
        }
        List<String> stringList = new ArrayList<>();
        String where = Video_pic + "=?";
        int size = list.size();
        for (int i = 0; i < size; i++) {
            UploadVideoBean bean = list.get(i);
            db.delete(Video, where, new String[]{bean.getPic()});
            stringList.add(bean.getPic());
        }
        CommonUtils.deleteOssObjectList(stringList);//删除阿里云上的图片
    }

    //删除某个数据
    public void deleteUploadVideoBean(UploadVideoBean bean) {
        String where = Video_path + "=?";
        db.delete(Video, where, new String[]{bean.getPath()});
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
     * 根据上传的路径 更新进度
     *
     * @param path
     * @param progress
     * @param status
     */
    public void updateUploadVideoBean(String path, int progress, String status) {
        ContentValues cv = new ContentValues();
        cv.put(Video_progress, progress);
        cv.put(Video_status, status);
        db.update(Video, cv, Video_path + " = \"" + path+"\"", null);
    }

}
