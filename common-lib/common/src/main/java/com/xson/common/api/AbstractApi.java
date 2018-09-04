package com.xson.common.api;

import android.content.Context;

import com.xson.common.utils.L;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Milk <249828165@qq.com>
 */
public abstract class AbstractApi {

    private int p;
    public static String API_URL = "";
    public HashMap<String, Object> paramsHashMap = new HashMap<String, Object>();

    public static enum Method {
        GET,
        POST,
    }

    public static enum Enctype {
        TEXT_PLAIN,
        MULTIPART,
    }

    protected abstract String getPath();

    public Method requestMethod() {
        return Method.POST;
    }

    public Enctype requestEnctype() {
        return Enctype.TEXT_PLAIN;
    }

    public String getUrl() {
        return API_URL + getPath();
    }

    public void setPage(int page) {
        this.p = page;
    }

    public AbstractApi addParams(String key, Object value) {
        paramsHashMap.put(key, value);
        return this;
    }

    public Map<String, Object> getParams() {
        HashMap<String, Object> params = new HashMap<String, Object>();
        Field[] field;
        Class clazz = getClass();
        try {
            for (Class<?> c = clazz; c != null; c = c.getSuperclass()) {
                field = c.getDeclaredFields();
                for (Field f : field) {
                    f.setAccessible(true);
                    Object value = f.get(this);
                    if (value != null &&
                            !L.API_URL.equals(f.getName()) &&
                            !L.paramsHashMap.equals(f.getName()) &&
                            !L.serialVersionUID.equals(f.getName()) &&
                            !f.getName().contains(L.shadow) &&
                            !L.path.equals(f.getName())) {
                        params.put(f.getName(), value);
                    }
                }
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            L.e(e);
        }
        for (Map.Entry<String, Object> item : paramsHashMap.entrySet()) {
            if (item.getKey() != null && item.getValue() != null) {
                params.put(item.getKey(), item.getValue());
            }
        }
        if (p > 0) {
            params.put(L.p, p);
        } else {
            params.remove(L.p);
        }
        return params;
    }


    public void handleParams(Context context, Map<String, Object> params) {
        HashMap<String, Object> fileMap = new HashMap<>();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            L.d("api.handleParams", "handleParams=>Map.Entry<String, Object> entry : params.entrySet()");
            Object value = entry.getValue();
            if (value instanceof File) {
                L.d("api.handleParams", "handleParams=>value instanceof File");
                fileMap.put(entry.getKey(), entry.getValue());
            } else if (value instanceof Iterable) { // List<File>, Collection<File>, etc...
                Iterator iter = ((Iterable) value).iterator();
                L.d("api.handleParams", "handleParams=>value instanceof Iterable");
                if (iter.hasNext() && iter.next() instanceof File) {
                    fileMap.put(entry.getKey(), entry.getValue());
                    L.d("api.handleParams", "handleParams=>iter.hasNext() && iter.next() instanceof File");
                }
            }
        }
        if (!fileMap.isEmpty()){
            for (String key : fileMap.keySet()) {
                params.remove(key);
                L.d("api.handleParams", "handleParams=>params.remove(key)");
            }
        }
        params.putAll(fileMap);
        L.d("api.handleParams", "handleParams=>params.putAll(fileMap)");
    }

}
