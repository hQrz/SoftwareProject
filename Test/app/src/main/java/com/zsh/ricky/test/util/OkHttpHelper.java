package com.zsh.ricky.test.util;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Ricky on 2017/10/16.
 */

public class OkHttpHelper {
    private OkHttpClient client;
    private Call call;
    private final String BASE_URL = "http://10.0.2.2:8080/CGService/";

    /**
     * send get request
     * @param url
     * @return the call object
     */
    public Call getRequest(final String url){

        client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(BASE_URL + url)
                .build();

        this.call = client.newCall(request);

        return this.call;
    }

    /**
     * send post request to service
     * @param url
     * @param rawsParam
     * @return the ok call object
     */
    public Call postRequest(final String url, final Map<String, String> rawsParam)
    {
        client = new OkHttpClient();

        FormBody.Builder builder = new FormBody.Builder();

        for (String key : rawsParam.keySet())
        {
            builder.add(key, rawsParam.get(key));
        }

        RequestBody requestBody = builder.build();

        Request requestPost = new Request.Builder()
                .url(BASE_URL + url)
                .post(requestBody)
                .build();

        this.call = client.newCall(requestPost);

        return this.call;
    }

    /**
     * 同步的向服务器发送请求，获取回复的字符串
     * @param url 服务器url
     * @return 服务器回复的字符串
     */
    public String get(final String url) {
        Call call = getRequest(url);
        String result = null;

        try {
            Response response = call.execute();

            result = response.body().string();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

}
