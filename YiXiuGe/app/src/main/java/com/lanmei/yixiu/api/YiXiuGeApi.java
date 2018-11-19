package com.lanmei.yixiu.api;

import com.xson.common.api.AbstractApi;

/**
 * Created by xkai on 2018/1/8.
 */

public class YiXiuGeApi extends AbstractApi {

    private String path;

    public YiXiuGeApi(String path){
        this.path = path;
    }

    @Override
    protected String getPath() {
        return path;
    }
}
