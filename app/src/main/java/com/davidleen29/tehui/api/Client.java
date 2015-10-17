package com.davidleen29.tehui.api;

import com.davidleen29.tehui.BuildConfig;
import com.davidleen29.tehui.lang.CException;
import com.davidleen29.tehui.utils.Log;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.protobuf.MessageLite;
import com.huiyou.dp.service.protocol.User;
import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.Response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 *
 *   远程访问  客户端类。
 *
 *
 * Created by davidleen29 on 2015/7/15.
 */
@Singleton
public class Client {




    public static final String BODY_ENCODING="UTF-8";
    private static final String TAG = "HTTPCLIENT";
   private  final String CONTENT_TYPE = "application/x-protobuf";
    ByteArrayOutputStream byteOut=   new ByteArrayOutputStream();
    //客户端连接
    public AsyncHttpClient client;
    @Inject
    public Client() {//设置链接参数   默认超时时间6秒
        client = new AsyncHttpClient(new AsyncHttpClientConfig.Builder().build());
    }





    public  <T> T  post(String url,MessageLite lite,AsyncCompletionHandler<T> handler)throws CException
    {

        Log.d(TAG,"URL:"+url+",\n body:"+lite);
        AsyncHttpClient.BoundRequestBuilder builder;
        builder = client.preparePost(url);
        builder.addHeader("Content-Type", CONTENT_TYPE);
            try {
                synchronized (byteOut) {

                    byteOut.reset();
                lite.writeTo(byteOut);

                    byte[] body=byteOut.toByteArray();


                    builder.setBody(body);


                }



                T result= builder.execute(handler).get();
             //       Log.d(TAG,"URL:"+url+",\n result:"+result);

                return result;
            } catch (InterruptedException e) {
                e.printStackTrace();
                throw CException.create(CException.FAIL_ASYNC_CLIENT,e);

            } catch (ExecutionException e) {
                e.printStackTrace();
                throw CException.create(CException.FAIL_ASYNC_CLIENT,e);

            } catch (IOException e) {
                e.printStackTrace();
                throw CException.create(CException.FAIL_READ_RESPONSE_IO,e);

            }

    }

}
