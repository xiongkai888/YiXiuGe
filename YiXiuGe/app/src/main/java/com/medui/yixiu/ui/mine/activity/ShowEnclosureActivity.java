package com.medui.yixiu.ui.mine.activity;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnDrawListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.medui.yixiu.R;
import com.medui.yixiu.bean.NotesBean;
import com.medui.yixiu.helper.ShareHelper;
import com.medui.yixiu.webviewpage.WebViewPhotoBrowserUtil;
import com.xson.common.app.BaseActivity;
import com.xson.common.utils.L;
import com.xson.common.utils.StringUtils;
import com.xson.common.widget.CenterTitleToolbar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import butterknife.InjectView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 附件详情
 */
public class ShowEnclosureActivity extends BaseActivity implements OnPageChangeListener, OnLoadCompleteListener, OnDrawListener, OnPageErrorListener {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.webView)
    WebView webView;
    @InjectView(R.id.pdfView)
    PDFView pdfView;
    private NotesBean.EnclosureBean bean;
    private ShareHelper shareHelper;
    private String url = "";
    private OkHttpClient okHttpClient;

    @Override
    public int getContentViewId() {
        return R.layout.activity_show_enclosure;
    }

    @Override
    public void initIntent(Intent intent) {
        super.initIntent(intent);
        Bundle bundle = intent.getBundleExtra("bundle");
        if (bundle == null){
            return;
        }
        bean = (NotesBean.EnclosureBean)bundle.getSerializable("bean");
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.enclosure_details);
        actionbar.setHomeAsUpIndicator(R.drawable.back);

        okHttpClient = new OkHttpClient();
        shareHelper = new ShareHelper(this);

        if (bean == null){
            return;
        }
        url = bean.getUrl();
//        L.d(L.TAG,"enclosureUrl:"+bean.getUrl());
//        WebViewPhotoBrowserUtil.loadUrl(webView,bean.getUrl());
        if (StringUtils.isEmpty(url)) {
            return;
        }
        if (url.endsWith("pdf")) {
            pdfView.setVisibility(View.VISIBLE);
            webView.setVisibility(View.GONE);
            String SDPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            File file = new File(SDPath, url.substring(url.lastIndexOf("/") + 1));
            if (file.exists()) {
                displayFromFile(file);
                L.d("h_bl", "file.exists()");
                return;
            }
            load();
        } else {
            webView.setVisibility(View.VISIBLE);
            pdfView.setVisibility(View.GONE);
            WebViewPhotoBrowserUtil.loadUrl(webView,url);
        }
//        WebViewPhotoBrowserUtil.photoBrowser(this,webView,enclosureUrl);
    }


    private void displayFromFile(File file) {
        L.d("h_bl", file.getAbsolutePath());
        pdfView.fromFile(file)   //设置pdf文件地址
                .defaultPage(0)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .spacing(10) // in dp
                .onPageError(this)
                .load();
    }

    private void load() {
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("h_bl", "onFailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (isFinishing()){
                    return;
                }
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                String SDPath = Environment.getExternalStorageDirectory().getAbsolutePath();
                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    File file = new File(SDPath, url.substring(url.lastIndexOf("/") + 1));
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
//                        Log.d("h_bl", "progress=" + progress);
                        Message msg = handler.obtainMessage();
                        msg.what = 1;
                        msg.arg1 = progress;
                        handler.sendMessage(msg);
                    }
                    fos.flush();
                    L.d("h_bl", "文件下载成功");
                } catch (Exception e) {
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                    }
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_more, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_more:
                if (bean == null){
                    break;
                }
                shareHelper.share(bean.getUrl(),bean.getName());
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
        shareHelper.onDestroy();
        handler = null;
    }


    /**
     * 结果返回
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        shareHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {

    }

    @Override
    public void loadComplete(int nbPages) {

    }

    @Override
    public void onPageChanged(int page, int pageCount) {

    }

    @Override
    public void onPageError(int page, Throwable t) {

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == 100) {
                displayFromFile(new File(Environment.getExternalStorageDirectory().getAbsolutePath(), url.substring(url.lastIndexOf("/") + 1)));
            }
        }
    };


}
