package com.lanmei.yixiu.ui.teacher.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.lanmei.yixiu.R;
import com.lanmei.yixiu.adapter.PublishCoursewareClassifyAdapter;
import com.lanmei.yixiu.adapter.UploadingNotesAdapter;
import com.lanmei.yixiu.api.YiXiuGeApi;
import com.lanmei.yixiu.bean.CourseClassifyBean;
import com.lanmei.yixiu.bean.NotesBean;
import com.lanmei.yixiu.event.PublishCoursewareEvent;
import com.lanmei.yixiu.helper.BGASortableNinePhotoHelper;
import com.lanmei.yixiu.utils.CommonUtils;
import com.lanmei.yixiu.utils.CompressPhotoUtils;
import com.lanmei.yixiu.utils.UpdateFileTask;
import com.leon.lfilepickerlibrary.LFilePicker;
import com.leon.lfilepickerlibrary.utils.Constant;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.BaseBean;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.JsonUtil;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIBaseUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.DividerItemDecoration;
import com.xson.common.widget.DrawClickableEditText;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.bingoogolapple.photopicker.widget.BGASortableNinePhotoLayout;

/**
 * 发课件
 */
public class PublishCoursewareActivity extends BaseActivity implements BGASortableNinePhotoLayout.Delegate {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.snpl_moment_add_photos)
    BGASortableNinePhotoLayout mPhotosSnpl;//拖拽排序九宫格控件
    BGASortableNinePhotoHelper mPhotoHelper;
    @InjectView(R.id.note_title_et)
    DrawClickableEditText noteTitleEt;
    @InjectView(R.id.note_content_et)
    DrawClickableEditText noteContentEt;
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;//附件
    @InjectView(R.id.recyclerView_c)
    RecyclerView recyclerView_c;//选择的分类
    @InjectView(R.id.name_tv)
    TextView nameTv;
    private CompressPhotoUtils compressPhotoUtils;
    private UploadingNotesAdapter adapter;
    private UpdateFileTask updateFileTask;
    private boolean isCompressPhotoUtils;
    private boolean isUpdateFileTask;
    public List<String> picList;//上传阿里云的图片地址
    public List<String> fileList;//上传阿里云的文件地址

    private PublishCoursewareClassifyAdapter coursewareClassifyAdapter;

    @Override
    public int getContentViewId() {
        return R.layout.activity_publish_courseware;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.publish_courseware);
        actionbar.setHomeAsUpIndicator(R.drawable.back);
        initPhotoHelper();
        initRecyclerView();

        coursewareClassifyAdapter = new PublishCoursewareClassifyAdapter(this);
        recyclerView_c.setNestedScrollingEnabled(false);
        recyclerView_c.setLayoutManager(new LinearLayoutManager(this));
        coursewareClassifyAdapter.setChooseCoursewareClassifyListener(new PublishCoursewareClassifyAdapter.ChooseCoursewareClassifyListener() {
            @Override
            public void chooseCourseClassify(List<CourseClassifyBean> list) {
                StringBuilder builder = new StringBuilder();
                for (CourseClassifyBean bean:list){
                    if (bean.isChoose()){
                        builder.append(bean.getName()+",");
                    }
                }
                String name = builder.toString();
                if (!StringUtils.isEmpty(name)){
                    name = name.substring(0,name.length()-1);
                }
                nameTv.setText(name);
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
                coursewareClassifyAdapter.setData(response.data);
                recyclerView_c.setAdapter(coursewareClassifyAdapter);
            }
        });
    }

    private void initPhotoHelper() {
        mPhotosSnpl.setVisibility(View.VISIBLE);
        mPhotoHelper = new BGASortableNinePhotoHelper(this, mPhotosSnpl);
        // 设置拖拽排序控件的代理
        mPhotoHelper.setDelegate(this);
    }

    private void initRecyclerView() {
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL_LIST);
        itemDecoration.setDivider(new ColorDrawable(Color.TRANSPARENT));
        itemDecoration.setDividerHeight((int) UIBaseUtils.dp2px(this, 5));
        recyclerView.addItemDecoration(itemDecoration);
        adapter = new UploadingNotesAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_publish, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_publish) {
            submitNote();
        }
        return super.onOptionsItemSelected(item);
    }

    private void submitNote() {
        final String title = CommonUtils.getStringByEditText(noteTitleEt);
        if (StringUtils.isEmpty(title)) {
            UIHelper.ToastMessage(this, R.string.input_courseware_title);
            return;
        }
        final String content = CommonUtils.getStringByEditText(noteContentEt);
        if (StringUtils.isEmpty(content)) {
            UIHelper.ToastMessage(this, R.string.input_courseware_content);
            return;
        }
        final List<CourseClassifyBean> classifyBeanList = coursewareClassifyAdapter.getData();
        if (StringUtils.isEmpty(classifyBeanList) || !isChoose(classifyBeanList)) {
            UIHelper.ToastMessage(this, getString(R.string.selection_sort));
            return;
        }

        isCompressPhotoUtils = !StringUtils.isEmpty(mPhotoHelper.getData());
        isUpdateFileTask = !StringUtils.isEmpty(adapter.getData());

        if (isUpdateFileTask) {//上传文件
            List<NotesBean.EnclosureBean> list = adapter.getData();
            int size = list.size();
            String[] strings = new String[size];
            for (int i = 0; i < size; i++) {
                strings[i] = list.get(i).getUrl();
            }
            updateFileTask = new UpdateFileTask(this,CommonUtils.isTwo);
            updateFileTask.setUploadingFileCallBack(new UpdateFileTask.UploadingFileCallBack() {
                @Override
                public void success(List<String> paths) {
                    if (isFinishing()) {
                        return;
                    }
                    isUpdateFileTask = false;
                    fileList = paths;
                    if (!isCompressPhotoUtils) {
                        loadSubmitNote(title, content,classifyBeanList);
                    }
                }
            });
            updateFileTask.setUploadingText("附件上传中");
            updateFileTask.execute(strings);
        }

        if (isCompressPhotoUtils) {
            compressPhotoUtils = new CompressPhotoUtils(this);
            compressPhotoUtils.compressPhoto(CommonUtils.toArray(mPhotoHelper.getData()), new CompressPhotoUtils.CompressCallBack() {//压缩图片（可多张）
                @Override
                public void success(List<String> list) {
                    if (isFinishing()) {
                        return;
                    }
                    picList = list;
                    isCompressPhotoUtils = false;
                    if (!isUpdateFileTask) {
                        loadSubmitNote(title, content,classifyBeanList);
                    }
                }
            }, CommonUtils.isOne);
        }
    }

    private boolean isChoose(List<CourseClassifyBean> list){
        for (CourseClassifyBean bean:list){
            if (bean.isChoose()){
                return bean.isChoose();
            }
        }
        return false;
    }

    private void loadSubmitNote(String title, String content,List<CourseClassifyBean> list) {
        YiXiuGeApi api = new YiXiuGeApi("app/add_note");
        api.addParams("uid", api.getUserId(this));
        api.addParams("title", title);
        api.addParams("enclosure", getEnclosure());
        api.addParams("content", content);
        api.addParams("type", 2);
        api.addParams("cid", getCid(list));
        api.addParams("pic", JsonUtil.getJSONArrayByList(picList));
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (isFinishing()) {
                    return;
                }
                UIHelper.ToastMessage(getContext(), response.getMsg());
                EventBus.getDefault().post(new PublishCoursewareEvent());//
                finish();
            }
        });
    }

    private String getCid(List<CourseClassifyBean> list){
        StringBuilder builder = new StringBuilder();
        for (CourseClassifyBean bean:list){
            if (bean.isChoose()){
                builder.append(bean.getId()+",");
            }
        }
        String cid = builder.toString();
        if (!StringUtils.isEmpty(cid)){
            cid = cid.substring(0,cid.length()-1);
        }
        return cid;
    }

    public JSONArray getEnclosure() {
        JSONArray array = new JSONArray();
        if (adapter.getCount() == 0 || StringUtils.isEmpty(fileList)) {
            return array;
        }
        List<NotesBean.EnclosureBean> list = adapter.getData();
        int size = fileList.size();
        if (list.size() != size){
            return array;
        }
        for (int i = 0;i<size;i++) {
            NotesBean.EnclosureBean bean = list.get(i);
            bean.setUrl(fileList.get(i));
            array.add(bean);
        }
        return array;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (compressPhotoUtils != null){
            compressPhotoUtils.cancelled();
        }
        if (updateFileTask != null && updateFileTask.getStatus() == AsyncTask.Status.RUNNING) {
            updateFileTask.cancel(true);
        }
    }

    @OnClick({R.id.file_iv2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.file_iv2:
//                CommonUtils.developing(this);
                new LFilePicker()
                        .withActivity(this)
                        .withRequestCode(100)
                        .withIconStyle(Constant.ICON_STYLE_YELLOW)
                        .withTitle("选择上传附件")//标题文字
                        .withStartPath("/sdcard")//指定初始显示路径
                        .withMutilyMode(true)//多选
                        .withFileFilter(new String[]{".pdf", ".jpg", ".xlsx", ".doc", ".docx", ".ppt", ".pptx", ".png", ".xls", ".txt"})//过滤！
                        .withBackIcon(Constant.BACKICON_STYLEONE)
                        .withBackgroundColor("#1593f0")//标题背景颜色
                        .start();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 100) {
                List<String> list = data.getStringArrayListExtra(Constant.RESULT_INFO);
//                Toast.makeText(getApplicationContext(), "选中了" + list.size() + "个文件", Toast.LENGTH_SHORT).show();
                if (!StringUtils.isEmpty(list)) {
                    List<NotesBean.EnclosureBean> notesBeanList = new ArrayList<>();
                    for (String s : list) {
                        if (!StringUtils.isEmpty(s)) {
                            NotesBean.EnclosureBean bean = new NotesBean.EnclosureBean();
                            bean.setName(CommonUtils.getFileName(s));
                            bean.setUrl(s);
                            notesBeanList.add(bean);
                        }
                    }
                    if (adapter.getCount() == 0) {
                        adapter.setData(notesBeanList);
                    } else {
                        adapter.addData(notesBeanList);
                    }
                    adapter.notifyDataSetChanged();
                }

            } else {
                mPhotoHelper.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    @Override
    public void onClickAddNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, ArrayList<String> models) {
        mPhotoHelper.onClickAddNinePhotoItem(sortableNinePhotoLayout, view, position, models);
    }

    @Override
    public void onClickDeleteNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        mPhotoHelper.onClickDeleteNinePhotoItem(sortableNinePhotoLayout, view, position, model, models);
    }

    @Override
    public void onClickNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        mPhotoHelper.onClickNinePhotoItem(sortableNinePhotoLayout, view, position, model, models);
    }

}
