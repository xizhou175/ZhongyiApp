package com.hfad.zhongyi;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class UploadActivity extends AppCompatActivity {

    private String TAG = "upload";
    private String server_url = "http://10.0.0.9:8080"; // change this to real server address
    private String boundary = Long.toHexString(System.currentTimeMillis());
    private HttpURLConnection connection;
    private String CRLF = "\r\n";
    private String CharSet = "UTF-8";
    private byte[] body;
    private int heartRate = 0;

    /* onClick handlers */
    View.OnClickListener onClickSuccess = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(UploadActivity.this, BodyPartsActivity.class));
            finish();
        }
    };
    View.OnClickListener onClickRetry = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startUploads();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_page);
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "start upload");
        super.onStart();
        startUploads();
    }

    private void startUploads() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (body == null || heartRate == 0) {
                    Intent intent = getIntent();
                    body = intent.getByteArrayExtra("imageData");
                    heartRate = intent.getIntExtra("heartBeatData", 0);
                }
                if (body != null && heartRate > 0){
                    if (uploadImage() && uploadHeartRate()) {
                        uploadFinished(true);
                    } else {
                        uploadFinished(false);
                    }
                }
            }
        }).start();
    }

    private void uploadFinished(final boolean success) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ProgressBar circle = findViewById(R.id.progressBar);
                circle.setVisibility(View.INVISIBLE);
                TextView text = findViewById(R.id.uploadText);
                Button btn = findViewById(R.id.continue_btn);
                if (success) {
                    text.setText("上传成功");
                    text.setTextColor(Color.GREEN);
                    btn.setText("继续");
                    btn.setOnClickListener(onClickSuccess);
                } else {
                    text.setText("上传失败");
                    text.setTextColor(Color.RED);
                    btn.setText("重试");
                    btn.setOnClickListener(onClickRetry);
                }
                btn.setVisibility(View.VISIBLE);
            }
        });
    }

    private boolean uploadImage() {
        try {
            Log.d(TAG, String.format("upload start, file size: %d", body.length));
            connection = (HttpURLConnection) new URL(server_url + "/ingest").openConnection();
            connection.setDoOutput(true);
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

            int responseCode = connection.getResponseCode();
            Log.d(TAG, String.format("upload image response Code: %d", responseCode));
            return responseCode == 200;

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return false;
        }
    }

    private boolean uploadHeartRate() {
        try {
            connection = (HttpURLConnection) new URL(server_url + "/heartrate").openConnection();
            connection.setDoOutput(true);
            OutputStream output = connection.getOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(output, CharSet);
            writer.write("heartRate=" + heartRate);
            writer.flush();

            int responseCode = connection.getResponseCode();
            Log.d(TAG, String.format("upload heartRate response Code: %d", responseCode));
            return responseCode == 200;

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return false;
        }
    }
}
