package com.medui.yixiu.ui.teacher.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.medui.yixiu.R;
import com.medui.yixiu.adapter.PublishCourseClassifyAdapter;
import com.medui.yixiu.api.YiXiuGeApi;
import com.medui.yixiu.bean.CourseClassifyBean;
import com.medui.yixiu.event.AddCourseEvent;
import com.medui.yixiu.event.UpdateUploadEvent;
import com.medui.yixiu.ui.teacher.uploadvideo.DBUploadViewHelper;
import com.medui.yixiu.ui.teacher.uploadvideo.UploadVideoBean;
import com.medui.yixiu.utils.CommonUtils;
import com.medui.yixiu.utils.UpdateFileTask;
import com.medui.yixiu.utils.UriUtils;
import com.medui.yixiu.webviewpage.FileUtils;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.BaseBean;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.L;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 发布教程
 */
public class PublishCourseActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;
    @InjectView(R.id.name_tv)
    TextView nameTv;

    @InjectView(R.id.video_title_et)
    EditText videoTitleEt;//视频标题
    @InjectView(R.id.video_pic_tv)
    ImageView videoPicTv;//视频封面
    @InjectView(R.id.video_name_tv)
    TextView videoNameTv;//视频名称

    public String videoPath;//视频地址
    public String videoPicPath;//视频封面地址
    public CourseClassifyBean classifyBean;

    private PublishCourseClassifyAdapter adapter;
    private UpdateFileTask updateFileTask;//上传视频和视频封面任务
    private DBUploadViewHelper dbUploadViewHelper;

    @Override
    public int getContentViewId() {
        return R.layout.activity_publish_course;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.publish_course);
        actionbar.setHomeAsUpIndicator(R.drawable.back);
        dbUploadViewHelper = new DBUploadViewHelper(this);//


        adapter = new PublishCourseClassifyAdapter(this);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setChooseCourseClassifyListener(new PublishCourseClassifyAdapter.ChooseCourseClassifyListener() {
            @Override
            public void chooseCourseClassify(CourseClassifyBean bean) {
                classifyBean = bean;
                nameTv.setText(bean.getName());
            }
        });
        loadCourseClassify();

    }

    private void loadCourseClassify() {
        YiXiuGeApi api = new YiXiuGeApi("app/course_list");
        HttpClient.newInstance(this).request(api, new BeanRequest.SuccessListener<NoPageListBean<CourseClassifyBean>>() {
            @Override
            public void onResponse(NoPageListBean<CourseClassifyBean> response) {
                if (isFinishing()) {
                    return;
                }
                adapter.setData(response.data);
                recyclerView.setAdapter(adapter);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_publish, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_publish) {
            loadVideoCourse();
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadVideoCourse() {
        final String videoTitle = CommonUtils.getStringByEditText(videoTitleEt);
        if (StringUtils.isEmpty(videoTitle)) {
            UIHelper.ToastMessage(this, R.string.input_video_title);
            return;
        }
        List<UploadVideoBean> uploadVideoList = dbUploadViewHelper.getUploadVideoList();
        if (!StringUtils.isEmpty(uploadVideoList)) {
            int size = uploadVideoList.size();
            for (int i = 0; i < size; i++) {
                UploadVideoBean bean = uploadVideoList.get(i);
                if(StringUtils.isSame(bean.getPath(),videoPath)){
                    UIHelper.ToastMessage(this, "该视频已在上传视频列表中...");
                    return;
                }
            }
        }
        if (StringUtils.isEmpty(videoPath) || StringUtils.isEmpty(videoPicPath)) {
            UIHelper.ToastMessage(this, "请选择上传的教程视频");
            return;
        }
        if (StringUtils.isEmpty(classifyBean)) {
            UIHelper.ToastMessage(this, getString(R.string.selection_sort));
            return;
        }
        updateFileTask = new UpdateFileTask(this,CommonUtils.isOne);
        updateFileTask.setUploadingText("正在处理,请稍后...");
        updateFileTask.setUploadingFileCallBack(new UpdateFileTask.UploadingFileCallBack() {
            @Override
            public void success(List<String> paths) {
                if (isFinishing()) {
                    return;
                }
                if (!StringUtils.isEmpty(paths) && paths.size() == 1) {
                    L.d("AyncListObjects", "上传视频封面到阿里云返回的图片地址：" + paths.get(0));
                    UploadVideoBean bean = new UploadVideoBean();
                    bean.setPic(paths.get(0));
                    bean.setStatus(getString(R.string.waiting));
                    bean.setCid(classifyBean.getId());
                    bean.setTitle(videoTitle);
                    bean.setPath(videoPath);
                    bean.setProgress(0);
                    dbUploadViewHelper.insertUploadVideoBean(bean);

                    UpdateUploadEvent event = new UpdateUploadEvent();
                    event.setStatus(6);
                    EventBus.getDefault().post(event);

                    finish();
                }
            }
        });
        updateFileTask.execute(videoPicPath);
    }

    private void submitVideoCourse(List<String> list) {
        YiXiuGeApi api = new YiXiuGeApi("app/video_add");
        api.addParams("title", CommonUtils.getStringByEditText(videoTitleEt));
        api.addParams("pic", list.get(0));
        api.addParams("video", list.get(1));
        api.addParams("cid", classifyBean.getId());
        api.addParams("uid", api.getUserId(this));
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (isFinishing()) {
                    return;
                }
                UIHelper.ToastMessage(getContext(), "发布成功");
                EventBus.getDefault().post(new AddCourseEvent(classifyBean.getId()));//刷新cid == classifyBean.getCid()的教程列表
                finish();
            }
        });
    }

    @OnClick(R.id.video_pic_tv)
    public void onViewClicked() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("video/*");//选择视频 （mp4 3gp 是android支持的视频格式）
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Uri uri = data.getData();
                videoPath = UriUtils.getPath(this, uri);
                //创建MediaMetadataRetriever对象
                MediaMetadataRetriever mmr = new MediaMetadataRetriever();
//绑定资源
                mmr.setDataSource(videoPath);
//获取第一帧图像的bitmap对象
                Bitmap bitmap = mmr.getFrameAtTime();
//加载到ImageView控件上
                videoPicTv.setImageBitmap(bitmap);

                FileUtils.savePhoto(getContext(), bitmap, new FileUtils.SaveResultCallback() {
                    @Override
                    public void onSavedSuccess(String path) {
                        videoPicPath = path;
                    }

                    @Override
                    public void onSavedFailed() {

                    }
                });
                videoNameTv.setVisibility(View.VISIBLE);
                videoNameTv.setText(CommonUtils.getFileName(videoPath));
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (updateFileTask != null && updateFileTask.getStatus() == AsyncTask.Status.RUNNING) {
            updateFileTask.cancel(true);
        }
        updateFileTask = null;
    }
}

