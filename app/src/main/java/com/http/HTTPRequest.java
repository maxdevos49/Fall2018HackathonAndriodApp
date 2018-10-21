package com.http;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.MainMenu.AppMainMenu;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HTTPRequest {

    private static final String LOG_ID = HTTPRequest.class.getSimpleName();

    public static final String getServer() {
        SharedPreferences server = AppMainMenu.context.getSharedPreferences("com.Setting.sharedpreferences", Context.MODE_PRIVATE);
        String serverAddress = server.getString("serverAddress", null);
        Log.i(LOG_ID, "Giving server name: " + serverAddress);
        return serverAddress;
    }

    public static void upload(List<String> uris) {
        List<File> files = new ArrayList<>();
        for (String u : uris) {
            files.add(new File(u));
        }
        uploadFiles(files);
    }

    public static void getFiles(PhotoDataResponseListener dataResponse) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                CloseableHttpClient httpClient = HttpClients.createDefault();

                String server = getServer();

                Log.i(LOG_ID, "Making get request to: " + server + "/api/photos");
                HttpGet httpGet = new HttpGet(server + "/api/photos");
                try {
                    HttpResponse response = httpClient.execute(httpGet);

                    Log.i(LOG_ID, "Reading response");
                    BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }

                    Log.i(LOG_ID, sb.toString());
                    dataResponse.response(new JSONArray(sb.toString()));

                } catch (IOException e) {
                    dataResponse.failure("Error when attempting http client:");
                } catch (JSONException e) {
                    dataResponse.failure("Error parsing photo data response:");
                }

            }
        }).start();

    }

    public static void loadImage(String fileName, ImageLoadResponseListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    URL url = new URL(getServer() + fileName);
                    Log.i(LOG_ID, "Making image request to: " + url.toString());
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.setRequestMethod("GET");
                    connection.setReadTimeout(5000);
                    connection.setConnectTimeout(7000);
                    connection.connect();

                    Bitmap image = BitmapFactory.decodeStream(connection.getInputStream());

                    listener.imageLoaded(image);
                } catch (MalformedURLException e) {
                    listener.failure("Bad request URL");
                } catch (IOException e) {
                    listener.failure("Failure to connect with server");
                }
            }
        }).start();
    }

    public static void uploadFiles(List<File> files) {

        Thread t = new Thread(() -> {

            CloseableHttpClient httpclient = HttpClients.createSystem();
            try {
                HttpPost httppost = new HttpPost(getServer() + "/api/upload");

                MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();

                for (File file : files) {
                    FileBody bin = new FileBody(file);
                    multipartEntityBuilder = multipartEntityBuilder.addPart("files", bin);
                }

                HttpEntity reqEntity = multipartEntityBuilder.build();

                httppost.setEntity(reqEntity);

                CloseableHttpResponse response = httpclient.execute(httppost);
                try {
                    HttpEntity resEntity = response.getEntity();
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
