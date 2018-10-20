package com.example.naber.fall2018hackathonandroidapp.photopicker;

import android.os.Handler;
import android.os.Looper;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;

public class Utils {
    static final String SERVER = "http://7ef55e72.ngrok.io/api/upload";


    public static void runonUiThread(Runnable runnable) {
        final Handler UIHandler = new Handler(Looper.getMainLooper());
        UIHandler.post(runnable);
    }

    public int indexOf(String[] arr, String val) {
        for (int i = 0; i < arr.length; i++) {
            if (val == null) {
                if (arr[i] == null) {
                    return i;
                }
            } else if (val.equals(arr[i])) {
                return i;
            }
        }

        return -1;

    }

    public static void upload(File[] files) {

        Thread t = new Thread(() -> {

            CloseableHttpClient httpclient = HttpClients.createSystem();
            try {
                HttpPost httppost = new HttpPost(Utils.SERVER);

                MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();

                for (File file : files) {
                    FileBody bin = new FileBody(file);
                    multipartEntityBuilder = multipartEntityBuilder.addPart("files", bin);
                }

                HttpEntity reqEntity = multipartEntityBuilder.build();

                httppost.setEntity(reqEntity);

                System.out.println("executing request " + httppost.getRequestLine());
                CloseableHttpResponse response = httpclient.execute(httppost);
                try {
                    System.out.println("----------------------------------------");
                    System.out.println(response.getStatusLine());
                    HttpEntity resEntity = response.getEntity();
                    if (resEntity != null) {
                        System.out.println("Response content length: " + resEntity.getContentLength());
                    }
    //                EntityUtils.consume(resEntity);
    //            } catch (IOException e) {
    //                // TODO Auto-generated catch block
    //                e.printStackTrace();
                } finally {
                    try {
                        response.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            } catch (ClientProtocolException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } finally {
                try {
                    httpclient.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        });

        t.start();
    }
}
