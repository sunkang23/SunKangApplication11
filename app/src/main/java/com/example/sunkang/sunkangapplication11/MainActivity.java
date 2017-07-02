package com.example.sunkang.sunkangapplication11;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private Button btGet;
    private Button btPost;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) this.findViewById(R.id.tv_content);

        downLoad();

        btGet = (Button) this.findViewById(R.id.bt_get);
        btPost = (Button) this.findViewById(R.id.bt_post);
        btGet.setOnClickListener(new View.OnClickListener() {
            //使用okhttp去访问网站
            @Override
            public void onClick(View v) {
                //1.拿到okhttpclient对象
                OkHttpClient okHttpClient = new OkHttpClient();


                //2.通过构造者模式创建一个Request对象
                Request.Builder builder = new Request.Builder();
                Request request = builder.get().url("http://www.imooc.com").build();
                //3.将request封装为call对象
                Call call = okHttpClient.newCall(request);
                //4.执行call
                //直接执行
                //Response response = call.execute();
                //采用异步的方式
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        L.e("onFailure:" + e.getMessage());
                        e.printStackTrace();

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String string = response.body().string();
                        L.e("onResponse:" + string);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textView.setText(string);
                            }
                        });
                    }
                });

            }
        });
        btPost.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //1.拿到okhttpclient对象
                OkHttpClient okHttpClient = new OkHttpClient();
                //2.通过构造者模式创建一个Request对象
                Request.Builder builder = new Request.Builder();
                //3.构建RequestBody
                RequestBody requestBody = new FormBody.Builder().add("name", "sukskiller")
                        .add("password", "511620").build();
                Request request = builder.url("http://apis.juhe.cn/mobile/get?").post(requestBody).build();
                //3.将request封装为call对象
                Call call = okHttpClient.newCall(request);
                //4.执行call
                //直接执行
                //Response response = call.execute();
                //采用异步的方式
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        L.e("onFailure:" + e.getMessage());
                        e.printStackTrace();

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String string = response.body().string();
                        L.e("onResponse:" + string);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textView.setText(string);
                            }
                        });
                    }
                });

            }

        });
    }

    public void doPostString() {
        RequestBody.create(MediaType.parse("text/plain;chaset=utf-8"),
                "{\"name\":\"suk\",\"password\":\"511620\"}");
    }

    public void doPostFile() {
        File file = new File(Environment.getExternalStorageDirectory(), "sunkang.txt");
        if (!file.exists()) {
            return;
        }
        RequestBody.create(MediaType.parse("application/octet-stream"), file);
    }

    public void doUpload() {
        //1.拿到okhttpclient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //2.通过构造者模式创建一个Request对象
        Request.Builder builder = new Request.Builder();
        File file = new File(Environment.getExternalStorageDirectory(), "sunkang.txt");
        if (!file.exists()) {
            return;
        }
        MultipartBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)

                .addFormDataPart("username", "yy")

                .addFormDataPart("password", "zz0114yhbb")

                .addFormDataPart("mPhoto", "yy.jpg", RequestBody.create(MediaType.parse("application/octet-stream"), file))

                .build();
        CountingRequestBody countingRequestBody = new CountingRequestBody(body, new CountingRequestBody.Listener() {
            @Override
            public void onRequestProgress(long byteWrited, long contentLenth) {
                System.out.println("已经写入的数据是"+byteWrited+",总长度是"+contentLenth);
            }
        });
        Request request = builder.url("http://apis.juhe.cn/mobile/get?").post(countingRequestBody).build();
    }

    public void downLoad() {
        //1.拿到okhttpclient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //2.通过构造者模式创建一个Request对象
        Request.Builder builder = new Request.Builder();
        Request request = builder.get().url("https://www.baidu.com/img/bd_logo1.png").build();
        //3.将request封装为call对象
        Call call = okHttpClient.newCall(request);
        //4.执行call
        //直接执行
        //Response response = call.execute();
        //采用异步的方式
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                L.e("onFailure:" + e.getMessage());
                e.printStackTrace();

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //计算待下载图片的总长度
                final long total = response.body().contentLength();
                long sum = 0L;
                InputStream is = response.body().byteStream();
                File file = new File(Environment.getExternalStorageDirectory(), "suk.jpg");
                byte[] b = new byte[1024];
                FileOutputStream fos = new FileOutputStream(file);
                int len = 0;

                while ((len = is.read(b)) != -1) {
                    sum = sum + len;
                    fos.write(b, 0, len);
                    final long finalSum = sum;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            textView.setText("当前下载进度是："+finalSum+"/"+total);
                        }
                    });
                }
                fos.flush();
                fos.close();
                is.close();
            }
        });

    }
}
