package com.hfad.zhongyi;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

class onUploadSuccess implements View.OnClickListener {

    @Override
    public void onClick(View view) {
        Log.d("click on success", "clicked");
    }
}

class OnUploadFailed implements View.OnClickListener {

    @Override
    public void onClick(View view) {
        Log.d("click on failed", "clicked");
    }
}

public class UploadActivity extends AppCompatActivity {

    private String TAG = "upload";
    private String server_url = "http://10.0.0.9:8080"; // change this to real server address
    private String boundary = Long.toHexString(System.currentTimeMillis());
    private HttpURLConnection connection;
    private String CRLF = "\r\n";
    private String CharSet = "UTF-8";
    private byte[] body;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_page);
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "start upload");
        super.onStart();
        new Thread(new Runnable() {
            @Override
            public void run() {
                body = getIntent().getByteArrayExtra("imageData");
                startUpload();
                try {
                    int responseCode = connection.getResponseCode();
                    Log.d(TAG, String.format("response Code: %d", responseCode));
                    if (responseCode == 200) {
                        uploadFinished(true);
                    } else {
                        uploadFinished(false);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        uploadFinished(false);
                    }
                });
            }
        }).start();
    }

    private void uploadFinished(boolean success) {
        ProgressBar circle = findViewById(R.id.progressBar);
        circle.setVisibility(View.INVISIBLE);
        TextView text = findViewById(R.id.uploadText);
        Button btn = findViewById(R.id.continue_btn);
        if (success) {
            text.setText("上传成功");
            text.setTextColor(Color.GREEN);
            btn.setText("继续");
            btn.setOnClickListener(new onUploadSuccess());
        } else {
            text.setText("上传失败");
            text.setTextColor(Color.RED);
            btn.setText("重试");
            btn.setOnClickListener(new OnUploadFailed());
        }
        btn.setVisibility(View.VISIBLE);
    }

    private void startUpload() {
        try {
            Log.d(TAG, String.format("upload start, file size: %d", body.length));
            connection = (HttpURLConnection) new URL(server_url).openConnection();
            connection.setDoOutput(true);
            // connection.setRequestProperty("Content-Length", String.format("%d", body.length()));
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            OutputStream output = connection.getOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(output, CharSet);
            writer.append("--" + boundary).append(CRLF);
            writer.append("Content-Disposition: form-data; name=\"file\"; filename=\"myImage.jpg\"").append(CRLF);
            writer.append("Content-Transfer-Encoding: binary").append(CRLF);
            writer.append("Content-Type: image/jpeg").append(CRLF).append(CRLF);
            // Note here we need to flush the writer before we write body to output directly
            writer.flush();
            // Note here we need to write directly to output since body is binary
            output.write(body);
            writer.append(CRLF);
            writer.append("--" + boundary + "--").append(CRLF);
            writer.flush();
            Log.d(TAG, "upload finished");
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            e.printStackTrace();
        }
    }
}
