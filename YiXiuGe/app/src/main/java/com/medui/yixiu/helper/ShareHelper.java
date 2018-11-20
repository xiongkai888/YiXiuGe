package com.medui.yixiu.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.medui.yixiu.R;
import com.umeng.socialize.Config;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.ShareBoardConfig;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;

/**
 * Created by xkai on 2018/10/11.
 * 分享帮助类
 */

public class ShareHelper {

    private UMShareAPI mShareAPI;
    private Context context;
//    String shareUrl = "https://www.baidu.com/";

    public ShareHelper(Context context) {
        this.context = context;
        //分享初始化
        mShareAPI = UMShareAPI.get(context);
    }

    public void share(String shareUrl) {
        if (StringUtils.isEmpty(shareUrl)){
            UIHelper.ToastMessage(context,"分享的内容为空，分享失败");
            return;
        }
        Config.isJumptoAppStore = true;//其中qq 微信会跳转到下载界面进行下载，其他应用会跳到应用商店进行下载
        UMImage thumb = new UMImage(context, R.drawable.logo);//资源文件  SHARE_MEDIA.SINA,SHARE_MEDIA.QQ,, SHARE_MEDIA.QZONE
        UMWeb web = new UMWeb(shareUrl);
        web.setTitle(context.getString(R.string.app_name));//标题
        web.setThumb(thumb);  //缩略图
        web.setDescription(context.getString(R.string.app_name));//描述

        ShareAction shareAction = new ShareAction((Activity) context);

        shareAction.withText("快来加入我们吧！")
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN_FAVORITE)
                .withMedia(web)
                .setCallback(umShareListener);

                ShareBoardConfig config = new ShareBoardConfig();//新建ShareBoardConfig
                config.setMenuItemBackgroundShape(ShareBoardConfig.BG_SHAPE_CIRCULAR);
//                config.setShareboardPostion(ShareBoardConfig.SHAREBOARD_POSITION_CENTER);//设置位置
//                config.setTitleVisibility(false);
                config.setIndicatorVisibility(false);
//                config.setCancelButtonVisibility(false);
                shareAction.open(config);//传入分享面板中
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //分享开始的回调
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
//            shareMediaToastMessage(platform, "分享成功", "收藏成功");
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
//            shareMediaToastMessage(platform, "分享失败", "收藏失败");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
//            shareMediaToastMessage(platform, "分享取消", "收藏取消");
        }
    };

    private void shareMediaToastMessage(SHARE_MEDIA platform, String s1, String s2) {
        if (platform == SHARE_MEDIA.WEIXIN || platform == SHARE_MEDIA.WEIXIN_CIRCLE) {
            UIHelper.ToastMessage(context, s1);
        } else {
            UIHelper.ToastMessage(context, s2);
        }
    }

    public void onDestroy() {
        mShareAPI.get(context).release();
    }

    /**
     * 结果返回
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mShareAPI.get(context).onActivityResult(requestCode, resultCode, data);
    }

}
