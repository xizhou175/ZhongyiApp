package com.hfad.zhongyi;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Callable;

public class CameraActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private Camera mCamera;
    private TexturePreview mPreview;
    private PictureHandler mPicture;
    private ImageView mImageView;
    private Callable<Void> showButtonsCallBack;
    private boolean viewingPicture = false;
    private final int MY_PERMISSIONS_REQUEST_CAMERA = 0;
    private final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    private final String TAG = "CameraActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        mImageView = findViewById(R.id.picture_preview);
        showButtonsCallBack = new Callable<Void>() {
            public Void call() {
                showButtons();
                return null;
            }
        };
        requestPermissionsIfNotGranted();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
        if (mCamera == null) {
            startCamera();
        }
        if (viewingPicture) {
            mPreview.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
        releaseCameraAndPreview();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
        releaseCameraAndPreview();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
        releaseCameraAndPreview();
    }

    public void onClickAccept(View view) {
        // mPicture.storePicture();
        mCamera.stopPreview();
        Log.d(TAG, "start HeartRateMonitor");

        Intent intent = new Intent(this, HeartRateMonitor.class);
        intent.putExtra("imageData", mPicture.getImageData());
        startActivity(intent);

        //finish();

        // destroy this activity
        //finish();
        // continueCameraPreview();
    }

    public void onClickCancel(View view) {
        continueCameraPreview();
    }

    private void continueCameraPreview() {
        try {
            mCamera.reconnect();
            mCamera.startPreview();
        } catch (IOException e) {
            Log.d(TAG, "IOException continue camera preview: " + e.getMessage());
        } catch (RuntimeException e) {
            Log.d(TAG, "Runtime Exception continue camera preview: " + e.getMessage());
        }
        // hide imagePreview
        mImageView.setImageResource(android.R.color.transparent);
        mImageView.setVisibility(View.INVISIBLE);
        // show texturePreview
        mPreview.setVisibility(View.VISIBLE);
        // hide cancel/accept buttons
        RelativeLayout buttonLayout = findViewById(R.id.button_layout);
        buttonLayout.setVisibility(View.INVISIBLE);
        // show take pic button
        Button button = findViewById(R.id.button_take_pic);
        button.setVisibility(View.VISIBLE);
        viewingPicture = false;
    }

    private void showButtons() {
        Log.d(TAG, "showButtons");
        // hide textureView
        mPreview.setVisibility(View.INVISIBLE);
        // show cancel/accept buttons
        RelativeLayout buttonLayout = findViewById(R.id.button_layout);
        buttonLayout.setVisibility(View.VISIBLE);
        // hide take pic button
        Button button = findViewById(R.id.button_take_pic);
        button.setVisibility(View.INVISIBLE);
        viewingPicture = true;
    }

    private void requestPermissionsIfNotGranted() {
        if (ContextCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CameraActivity.this,
                    new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },
                    MY_PERMISSIONS_REQUEST_CAMERA);
            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        } else {
            startCamera();
            mPicture = new PictureHandler(true, mImageView, showButtonsCallBack);
        }
    }

    private void startCamera() {
        if (safeCameraOpen()) {
            setCameraDisplayOrientation(this, 1);
            mPreview = new TexturePreview(this, mCamera);
            scalePreview();
        }
    }

    public void takePicture(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mCamera.takePicture(null, null, mPicture);
            }
        }).start();
    }

    private void scalePreview() {
        Camera.Parameters camParams = mCamera.getParameters();

        if (camParams.isZoomSupported()) {
            camParams.setZoom(camParams.getMaxZoom());
        }
        mCamera.setParameters(camParams);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        Log.d("Screen Size", String.format("height: %d, width: %d", height, width));

        float ratio = (float) width / height;

        FrameLayout background = findViewById(R.id.camera_background);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) background.getLayoutParams();

        int backgroundHeight = height - params.bottomMargin;
        int new_width = Math.round((float)backgroundHeight * ratio);

        FrameLayout preview = findViewById(R.id.camera_preview);
        ViewGroup.LayoutParams layoutParams = preview.getLayoutParams();
        layoutParams.width = new_width;
        layoutParams.height = backgroundHeight;

        preview.setLayoutParams(layoutParams);

        preview.addView(mPreview);
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

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (360 - info.orientation - degrees) % 360;
        } else {  // back-facing
            result = (360 + info.orientation - degrees) % 360;
        }
        Log.d("Camera Set Orientation", String.valueOf(result));
        mCamera.setDisplayOrientation(result);
        Camera.Parameters params = mCamera.getParameters();
        params.setRotation(360-result);
        mCamera.setParameters(params);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                Log.d(TAG, "Permission Response");
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startCamera();
                } else {
                    Log.d(TAG, "permission denied");
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                Log.d(TAG, "Storage Permission Response");
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mPicture = new PictureHandler(true, mImageView, showButtonsCallBack);
                } else {
                    Log.d(TAG, "storage permission denied");
                    mPicture = new PictureHandler(false, mImageView, showButtonsCallBack);
                }
            }
            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }


    private boolean safeCameraOpen() {
        boolean qOpened = false;

        try {
            releaseCameraAndPreview();
            mCamera = Camera.open(1);
            qOpened = (mCamera != null);
        } catch (Exception e) {
            Log.e(getString(R.string.app_name), "failed to open Camera");
            e.printStackTrace();
        }
        return qOpened;
    }

    private void releaseCameraAndPreview() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

}

class TexturePreview extends TextureView implements TextureView.SurfaceTextureListener {
    private final String TAG = "TexturePreview";

    Camera mCamera;

    TexturePreview(Context context, Camera camera) {
        super(context);
        mCamera = camera;
        setSurfaceTextureListener(this);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
        Log.d(TAG, "Starting preview");
        try {
            mCamera.setPreviewTexture(surfaceTexture);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        } catch (RuntimeException e) {
            Log.d(TAG, "Run time exception: " + e.getMessage());
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {
        // ignore
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

    }
}

class PictureHandler implements Camera.PictureCallback {
    private final String TAG = "PictureHandler";
    private boolean permitted;
    private byte[] tmpImageData;
    private ImageView imageView;
    private Callable<Void> callback;

    PictureHandler(boolean permit, ImageView imView, Callable<Void> cb) {
        permitted = permit;
        imageView = imView;
        callback = cb;
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        tmpImageData = data;
        camera.stopPreview();
        imageView.setImageDrawable(null); // clear imageView
        previewPicture();
        try {
            Log.d(TAG, "call show button callback");
            callback.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void previewPicture() {
        Bitmap imageBitmap = BitmapFactory.decodeByteArray(tmpImageData, 0, tmpImageData.length);
        imageView.setImageBitmap(imageBitmap);
        imageView.setVisibility(View.VISIBLE);
    }

    public void storePicture() {
        Log.d(TAG, "store picture");
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Log.d(TAG, "Error creating media file, check storage permissions: ");
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            fos.write(tmpImageData);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
    }

    public byte[] getImageData() {
        return tmpImageData;
    }

    private File getOutputMediaFile() {
        if (!permitted) {
            return null;
        }
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "ZhongyiApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("ZhongyiApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_"+ timeStamp + ".jpg");

        return mediaFile;
    }
}