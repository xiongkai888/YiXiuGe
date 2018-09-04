package com.lanmei.yixiu.api;

import com.xson.common.api.ApiV2;

/**
 * Created by xkai on 2018/1/8.
 */

public class YiXiuGeApi extends ApiV2 {

    private String path;

    public YiXiuGeApi(String path){
        this.path = path;
    }

    @Override
    protected String getPath() {
        return path;
    }
}
