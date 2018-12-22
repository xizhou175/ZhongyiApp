package com.hfad.zhongyi;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;


/**
 * This class extends Activity to handle a picture preview, process the preview
 * for a red values and determine a heart beat.
 *
 * @author Justin Wetherell <phishman3579@gmail.com>
 */
public class HeartRateMonitor extends Activity implements DialogInterface.OnClickListener {

    private static final String TAG = "HeartRateMonitor";
    private static final AtomicBoolean processing = new AtomicBoolean(false);

    private static CameraPreview mPreview = null;
    private static Camera mCamera = null;
    private static View image = null;
    private static TextView text = null;

    private static int averageIndex = 0;
    private static final int averageArraySize = 4;
    private static int[] averageArray = null;

    private boolean measurementFinished = false;
    private int finalBeats = 0;
    private Lock lock = new ReentrantLock();


    @Override
    public void onClick(DialogInterface dialogInterface, int which) {
        if (measurementFinished) {
            if (which == DialogInterface.BUTTON_POSITIVE) {
                Intent intent = new Intent(this, UploadActivity.class);
                intent.putExtra("imageFile", getIntent().getStringExtra("imageFile"));
                Patient.getPatient().setHeartRate(finalBeats);
                startActivity(intent);
                finish();
            } else {
                lock.lock();
                    beatsIndex = 0;
                lock.unlock();
            }
        } else {
            startCamera();
            cameraOpenConfirmed = true;
        }
    }

    public enum TYPE {
        GREEN, RED
    };

    private static TYPE currentType = TYPE.GREEN;

    public static TYPE getCurrent() {
        return currentType;
    }

    private static int beatsIndex = 0;
    private static final int beatsArraySize = 3;
    private static final int[] beatsArray = new int[beatsArraySize];
    private static double beats = 0;
    private static long startTime = 0;
    private boolean cameraOpenConfirmed = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_rate_monitor);

        image = findViewById(R.id.image);
        text = (TextView) findViewById(R.id.text);

        averageIndex = 0;
        averageArray = new int[averageArraySize];
        lock.lock();
            beatsIndex = 0;
        lock.unlock();

        measurementAlert().show();
    }

    private AlertDialog measurementAlert() {
        String message = "为了准确测量心率，请将手指按于摄像头前（指头需覆盖摄像头），直到系统提示完成（大约需要30秒）";
        if (measurementFinished) {
            message = "测量完成";
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message).setTitle("注意").setPositiveButton("确认", this);
        if (measurementFinished) {
            builder.setNegativeButton("重新测量", this);
        }
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    private void startCamera() {
        if (safeCameraOpen()) {
            setCameraDisplayOrientation(this, 0);
            mPreview = new CameraPreview(this, mCamera, previewCallback);
            FrameLayout preview = findViewById(R.id.preview);
            preview.addView(mPreview);
        }
    }

    private void setCameraDisplayOrientation(Activity activity, int cameraId) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result = (360 + info.orientation - degrees) % 360;
        Log.d("Camera Set Orientation", String.valueOf(result));
        mCamera.setDisplayOrientation(result);
    }

    private boolean safeCameraOpen() {
        boolean qOpened = false;
        try {
            releaseCameraAndPreview();
            mCamera = Camera.open(0);
            qOpened = (mCamera != null);
        } catch (Exception e) {
            Log.e(getString(R.string.app_name), "failed to open Camera");
            e.printStackTrace();
        }

        return qOpened;
    }

    private void releaseCameraAndPreview() {
        if (mPreview != null) {
            mPreview.getHolder().removeCallback(mPreview);
        }
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        if (cameraOpenConfirmed && mCamera == null) {
            startCamera();
        }
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause");
        releaseCameraAndPreview();
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
        releaseCameraAndPreview();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
        releaseCameraAndPreview();
    }

    private PreviewCallback previewCallback = new PreviewCallback() {

        @Override
        public void onPreviewFrame(byte[] data, Camera cam) {
            if (data == null) throw new NullPointerException();
            Camera.Size size;
            try {
                size = cam.getParameters().getPreviewSize();
            } catch (Exception e) {
                Log.d(TAG, e.getMessage());
                return;
            }

            // Log.d(TAG, "CameraPreviewSize: " + size.height + " " + size.width);

            FrameLayout layout = findViewById(R.id.preview);
            // Log.d(TAG, "Layout size: " + layout.getHeight() + " " + layout.getWidth());

            if (size == null) throw new NullPointerException();

            if (!processing.compareAndSet(false, true)) return;

            int imgAvg = ImageProcessing.decodeYUV420SPtoRedAvg(data, size.width, size.height);

            if (imgAvg < 180) {
                text.setText("--");
                processing.set(false);
                return;
            }

            int averageArrayAvg = 0;
            int averageArrayCnt = 0;
            for (int i = 0; i < averageArray.length; i++) {
                if (averageArray[i] > 0) {
                    averageArrayAvg += averageArray[i];
                    averageArrayCnt++;
                }
            }

            int rollingAverage = (averageArrayCnt > 0) ? (averageArrayAvg / averageArrayCnt) : 0;
            TYPE newType = currentType;
            if (imgAvg < rollingAverage) {
                newType = TYPE.RED;
                if (newType != currentType) {
                    beats++;
                    // Log.d(TAG, "BEAT!! beats="+beats);
                }
            } else if (imgAvg > rollingAverage) {
                newType = TYPE.GREEN;
            }

            if (averageIndex == averageArraySize) averageIndex = 0;
            averageArray[averageIndex] = imgAvg;
            averageIndex++;

            // Transitioned from one state to another to the same
            if (newType != currentType) {
                currentType = newType;
                image.postInvalidate();
            }

            long endTime = System.currentTimeMillis();
            double totalTimeInSecs = (endTime - startTime) / 1000d;
            if (totalTimeInSecs >= 11) {
                double bps = (beats / totalTimeInSecs);
                int dpm = (int) (bps * 60d);
                if (dpm < 30 || dpm > 200) {
                    startTime = System.currentTimeMillis();
                    beats = 0;
                    processing.set(false);
                    text.setText("--");
                    return;
                }

                if (beatsIndex >= beatsArraySize) {
                    return;
                }

                beatsArray[beatsIndex] = dpm;

                lock.lock();
                    beatsIndex++;
                lock.unlock();

                int beatsArrayAvg = 0;
                int beatsArrayCnt = 0;
                for (int i = 0; i < beatsArray.length; i++) {
                    if (beatsArray[i] > 0) {
                        beatsArrayAvg += beatsArray[i];
                        beatsArrayCnt++;
                    }
                }
                int beatsAvg = (beatsArrayAvg / beatsArrayCnt);
                text.setText(String.valueOf(beatsAvg));
                startTime = System.currentTimeMillis();
                beats = 0;

                if (beatsIndex == beatsArraySize) { // measure finished
                    finalBeats = beatsAvg;
                    finishMeasurement();
                }
            }

            processing.set(false);
        }
    };

    private void finishMeasurement() {
        measurementFinished = true;
        measurementAlert().show();
    }
}


/** A basic Camera preview class */
class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Camera mCamera;
    PreviewCallback mPreviewCallback;

    private String TAG = "CameraPreview";

    public CameraPreview(Context context, Camera camera, PreviewCallback cb) {
        super(context);
        mCamera = camera;
        mPreviewCallback = cb;

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.setPreviewCallback(mPreviewCallback);
            mCamera.startPreview();
        } catch (Exception e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (mHolder.getSurface() == null){
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e){
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(mHolder);

            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            mCamera.setParameters(parameters);
            mCamera.setPreviewCallback(mPreviewCallback);
            mCamera.startPreview();
        } catch (Exception e){
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }
}

