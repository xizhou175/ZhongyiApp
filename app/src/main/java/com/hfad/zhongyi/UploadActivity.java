package com.hfad.zhongyi;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonWriter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;


import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static com.hfad.zhongyi.Patient.personalInfo;

public class UploadActivity extends AppCompatActivity {

    private String TAG = "upload";
    // private String server_url = "http://10.0.0.9:8080";
    private String server_url = "http://18.188.169.26";
    private String boundary = Long.toHexString(System.currentTimeMillis());
    private HttpURLConnection connection;
    private String CRLF = "\r\n";
    private String CharSet = "UTF-8";
    private byte[] image;

    /* onClick handlers */
    View.OnClickListener onClickSuccess = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(UploadActivity.this, BodyActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
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
                if (image == null) {
                    String filepath = getIntent().getStringExtra("imageFile");
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    try {
                        FileInputStream fis = new FileInputStream(filepath);
                        int read = 0;
                        byte[] tmp = new byte[4096];
                        while (read != -1) {
                            read = fis.read(tmp, 0, tmp.length);
                            if (read == -1) {
                                break;
                            }
                            bos.write(tmp, 0, read);
                        }
                        image = bos.toByteArray();
                    } catch (FileNotFoundException e) {
                        Log.d(TAG, "Image file not found");
                        e.printStackTrace();
                    } catch (IOException e) {
                        Log.d(TAG, "Read/write image data failed");
                        e.printStackTrace();
                    }
                }
                if (image != null && personalInfo.getHeartRate() > 0){
                    if (uploadImage() && uploadPatientData()) {
                        uploadFinished(true);
                        return;
                    }
                }
                uploadFinished(false);
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
            Log.d(TAG, String.format("upload start, file size: %d", image.length));
            connection = (HttpURLConnection) new URL(server_url + "/ingest").openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            OutputStream output = connection.getOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(output, CharSet);
            writer.append("--" + boundary).append(CRLF);
            writer.append("Content-Disposition: form-data; name=\"file\"; filename=" + getFileName() + ".jpg").append(CRLF);
            writer.append("Content-Transfer-Encoding: binary").append(CRLF);
            writer.append("Content-Type: image/jpeg").append(CRLF).append(CRLF);
            // Note here we need to flush the writer before we write body to output directly
            writer.flush();
            // Note here we need to write directly to output since body is binary
            output.write(image);
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

    private boolean uploadPatientData() {
        try {
            connection = (HttpURLConnection) new URL(server_url + "/patientdata").openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            OutputStream output = connection.getOutputStream();
            JsonWriter writer = new JsonWriter(new OutputStreamWriter(output, CharSet));
            writePatientData(writer);
            writer.flush();

            int responseCode = connection.getResponseCode();
            Log.d(TAG, String.format("upload patient data response Code: %d", responseCode));
            return responseCode == 200;

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return false;
        }
    }

    private String getFileName() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("Asia/Chongqing"));
        Date now = cal.getTime();
        String filename = personalInfo.getId() + "_" + new SimpleDateFormat("dd.MM.yy.hh.mm.ss").format(now);
        return filename;
    }

    private void writePatientData(JsonWriter writer) throws IOException {
        writer.beginObject();
        writer.name("id").value(getFileName());
        writer.name("heartRate").value(personalInfo.getHeartRate());
        writer.endObject();
    }
}
