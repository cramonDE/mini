package com.sh.shvideolibrary;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.sh.shvideolibrary.Fragment.FollowFragment;
import com.sh.shvideolibrary.Fragment.GamingFragment;
import com.sh.shvideolibrary.Fragment.HomeFragment;
import com.sh.shvideolibrary.Fragment.LoginFragment;
import com.sh.shvideolibrary.Fragment.MusicFragment;
import com.sh.shvideolibrary.Fragment.PostFragment;
import com.sh.shvideolibrary.Fragment.PreviewFragment;
import com.sh.shvideolibrary.Fragment.ReadyFragment;
import com.sh.shvideolibrary.emojiFaceComparer.EmojiFaceComparer;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// need by 手q登陆-start
// need by 手q登陆-end

public class VideoInputActivity extends Activity {
    private CameraPreview mPreview;
    private Camera mCamera;
    private MediaRecorder mediaRecorder;
    private String url_file;
    private MusicService musicService;
    private boolean mBind;
    //服务连接类,实现ServiceConnection接口,在实现方法中接收service对象,通过bindService启动服务时，需要连接类对象
    private MyServiceConnection myServiceConnection;
    private long countUp;

    private int quality = CamcorderProfile.QUALITY_480P;
    private static final int FOCUS_AREA_SIZE = 500;
    String TAG="VideoInputActivity";
    public static final String INTENT_EXTRA_VIDEO_PATH = "intent_extra_video_path";//录制的视频路径
    public static final int RESULT_CODE_FOR_RECORD_VIDEO_FAILED = 3;//视频录制出错
    public static int Q480 = CamcorderProfile.QUALITY_480P;
    public static int Q720 = CamcorderProfile.QUALITY_720P;
    public static int Q1080 = CamcorderProfile.QUALITY_1080P;
    public static int Q21600 = CamcorderProfile.QUALITY_2160P;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    ImageView button_ChangeCamera;
    LinearLayout cameraPreview;
    ImageView buttonCapture ;
    ImageView buttonFlash ;
    Chronometer  textChrono;
    ImageView chronoRecordingImage;
    ImageView startMusicBtn;
    ImageView stopMusicBtn;

    public LoginFragment loginFragment;
    public ReadyFragment readyFragment;
    public MusicFragment musicFragment;
    public GamingFragment gamingFragment;
    public PreviewFragment previewFragment;
    public PostFragment postFragment;
    public FollowFragment followFragment;
    public HomeFragment homeFragment;

    // need by 手q登陆-start
    private static final String APP_ID = "101485084";   // 从手q互联平台获得的APP_ID
    public Tencent mTencent;  // 登陆SDK使用的接口
    public IUiListener mIUiListener;  // 登陆SDK完成的回调接口
    // need by 手q登陆-end

    public static void startActivityForResult(Activity activity, int requestCode,int quality) {
        Intent intent = new Intent(activity, VideoInputActivity.class);
        intent.putExtra("quality",quality);
//        startActivityForResult(activity, intent, requestCode, null);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_input);
        quality = getIntent().getIntExtra("quality",Q480);
        QMUIStatusBarHelper.translucent(this);

        mTencent = Tencent.createInstance(APP_ID, this.getApplicationContext());

        if (null  == savedInstanceState) {
            loginFragment = new LoginFragment();
            readyFragment = new ReadyFragment();
            musicFragment = new MusicFragment();
            gamingFragment = new GamingFragment();
            previewFragment = new PreviewFragment();
            postFragment = new PostFragment();
            homeFragment = new HomeFragment();
            followFragment = new FollowFragment();
            addFragment(loginFragment);
        }
        initialize();

        final FragmentManager manager = getFragmentManager();
        manager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {

            @Override
            public void onBackStackChanged() {
                for(int entry = 0; entry < manager.getBackStackEntryCount(); entry++){
                    Log.i("manager", "Found fragment: " + manager.getBackStackEntryAt(entry).getName());
                }
            }
        });
    }


    //点击对焦
    public void initialize() {
//        button_ChangeCamera = (ImageView) findViewById(R.id.button_ChangeCamera);
//        cameraPreview = (LinearLayout) findViewById(R.id.camera_preview);
//        buttonCapture = (ImageView) findViewById(R.id.button_capture);
//        buttonFlash= (ImageView) findViewById(R.id.buttonFlash);
//        chronoRecordingImage= (ImageView) findViewById(R.id.chronoRecordingImage);
//        textChrono= (Chronometer) findViewById(R.id.textChrono);
//        startMusicBtn = (ImageView) findViewById(R.id.start_music);
//        stopMusicBtn = (ImageView) findViewById(R.id.stop_music);
//        startMusicBtn.setOnClickListener(startMusicListener);
//        stopMusicBtn.setOnClickListener(stopMusicListener);
//        buttonFlash.setOnClickListener(stopCaptureFrame);
//        mPreview = new CameraPreview(VideoInputActivity.this, mCamera);
//        cameraPreview.addView(mPreview);
//        buttonCapture.setOnClickListener(captrureListener);
        myServiceConnection = new MyServiceConnection();

        //通过startService启动服务
        Intent intent = new Intent(this,MusicService.class);
        bindService(intent, myServiceConnection, Context.BIND_AUTO_CREATE);

//        button_ChangeCamera.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mCamera.setPreviewCallback(new Camera.PreviewCallback() {
//                    @Override
//                    public void onPreviewFrame(byte[] data, Camera camera) {
//                        Log.i(TAG, "processing frame");
//                        Camera.Size size = mCamera.getParameters().getPreviewSize();
//                        YuvImage image = new YuvImage(data, ImageFormat.NV21, size.width,
//                                size.height, null);
//                        if (image != null) {
//                            ByteArrayOutputStream outputSteam = new ByteArrayOutputStream();
//                            image.compressToJpeg(new Rect(0, 0, size.width, size.height),
//                                    80, outputSteam);
//                            Bitmap bmp = BitmapFactory.decodeByteArray(outputSteam.toByteArray(), 0, outputSteam.size());
//                            bmp = rotateMyBitmap(bmp);
//                            byte[] jpegData = outputSteam.toByteArray();
//                            try {
//                                String pictureFilePath = getOutputPhoto();
//                                File pictureFile = new File(pictureFilePath);
//                                if (pictureFile == null){
//                                    Log.d(TAG, "Error creating media file, check storage permissions: ");
//                                    return;
//                                }
//                                try {
//                                    FileOutputStream fos = new FileOutputStream(pictureFile);
//                                    bmp.compress(Bitmap.CompressFormat.JPEG, 90, fos);
//                                    fos.write(jpegData);
//                                    fos.close();
//                                } catch (FileNotFoundException e) {
//                                    Log.d(TAG, "File not found: " + e.getMessage());
//                                } catch (IOException e) {
//                                    Log.d(TAG, "Error accessing file: " + e.getMessage());
//                                }
//                                // 这里可以控制抓帧的数量
//                                Thread.sleep(500);
//                                // 发送图片到后台
//                                startFaceDetect(pictureFilePath, 1);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                    }
//                });
//            }
//        });
//        cameraPreview.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    try {
//                        focusOnTouch(event);
//                    } catch (Exception e) {
//                        Log.i(TAG, getString(R.string.fail_when_camera_try_autofocus, e.toString()));
//                        //do nothing
//                    }
//                }
//                return true;
//            }
//        });
    }

    private void focusOnTouch(MotionEvent event) {
        if (mCamera != null) {
            Camera.Parameters parameters = mCamera.getParameters();
            if (parameters.getMaxNumMeteringAreas() > 0) {
                Rect rect = calculateFocusArea(event.getX(), event.getY());
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                List<Camera.Area> meteringAreas = new ArrayList<Camera.Area>();
                meteringAreas.add(new Camera.Area(rect, 800));
                parameters.setFocusAreas(meteringAreas);
                mCamera.setParameters(parameters);
                mCamera.autoFocus(mAutoFocusTakePictureCallback);
            } else {
                mCamera.autoFocus(mAutoFocusTakePictureCallback);
            }
        }
    }

    private Rect calculateFocusArea(float x, float y) {
        int left = clamp(Float.valueOf((x / mPreview.getWidth()) * 2000 - 1000).intValue(), FOCUS_AREA_SIZE);
        int top = clamp(Float.valueOf((y / mPreview.getHeight()) * 2000 - 1000).intValue(), FOCUS_AREA_SIZE);
        return new Rect(left, top, left + FOCUS_AREA_SIZE, top + FOCUS_AREA_SIZE);
    }

    private int clamp(int touchCoordinateInCameraReper, int focusAreaSize) {
        int result;
        if (Math.abs(touchCoordinateInCameraReper) + focusAreaSize / 2 > 1000) {
            if (touchCoordinateInCameraReper > 0) {
                result = 1000 - focusAreaSize / 2;
            } else {
                result = -1000 + focusAreaSize / 2;
            }
        } else {
            result = touchCoordinateInCameraReper - focusAreaSize / 2;
        }
        return result;
    }

    private Camera.AutoFocusCallback mAutoFocusTakePictureCallback = new Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean success, Camera camera) {
            if (success) {
                // do something...
                Log.i("tap_to_focus", "success!");
            } else {
                // do something...
                Log.i("tap_to_focus", "fail!");
            }
        }
    };

    public void onResume() {
        super.onResume();
//        if (!hasCamera(getApplicationContext())) {
//            //这台设备没有发现摄像头
//            Toast.makeText(getApplicationContext(), R.string.dont_have_camera_error
//                    , Toast.LENGTH_SHORT).show();
//            setResult(RESULT_CANCELED);
//            releaseCamera();
//            releaseMediaRecorder();
//            finish();
//        }
//        if (mCamera == null) {
//            releaseCamera();
//            int cameraId = findFrontFacingCamera();
//            mCamera = Camera.open(cameraId);
//            mPreview.refreshCamera(mCamera);
//        }
    }

    //计时器
    private void startChronometer() {
        textChrono.setVisibility(View.VISIBLE);
        final long startTime = SystemClock.elapsedRealtime();
        textChrono.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer arg0) {
                countUp = (SystemClock.elapsedRealtime() - startTime) / 1000;
                if (countUp % 2 == 0) {
                    chronoRecordingImage.setVisibility(View.VISIBLE);
                } else {
                    chronoRecordingImage.setVisibility(View.INVISIBLE);
                }

                String asText = String.format("%02d", countUp / 60) + ":" + String.format("%02d", countUp % 60);
                textChrono.setText(asText);
            }
        });
        textChrono.start();
    }

    /**
     * 找前置摄像头,没有则返回-1
     *
     * @return cameraId
     */
    private int findFrontFacingCamera() {
        int cameraId = -1;
        //获取摄像头个数
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                cameraId = i;
                break;
            }
        }
        return cameraId;
    }

    private int findBackFacingCamera() {
        int cameraId = -1;
        //获取摄像头个数
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                cameraId = i;
                break;
            }
        }
        return cameraId;
    }

    //检查设备是否有摄像头
    private boolean hasCamera(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }

    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    private void releaseMediaRecorder() {
        if (mediaRecorder != null) {
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;
            mCamera.lock();
        }
    }

    private boolean prepareMediaRecorder() {
        mediaRecorder = new MediaRecorder();
        mCamera.unlock();
        mediaRecorder.setCamera(mCamera);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mediaRecorder.setOrientationHint(270);
        }

        mediaRecorder.setProfile(CamcorderProfile.get(quality));

        File file1 =  getOutputMediaFile();
        if (file1.exists()) {
            file1.delete();
        }

        mediaRecorder.setOutputFile(file1.toString());
        try {
            mediaRecorder.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            releaseMediaRecorder();
            return false;
        }
        return true;

    }

    private void stopChronometer() {
        textChrono.stop();
        chronoRecordingImage.setVisibility(View.INVISIBLE);
        textChrono.setVisibility(View.INVISIBLE);
    }

    boolean recording = false;
    View.OnClickListener stopCaptureFrame = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCamera.setPreviewCallback(null);
        }
    };

    View.OnClickListener captrureListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (recording) {
                //如果正在录制点击这个按钮表示录制完成
                mediaRecorder.stop(); //停止
                stopChronometer();
                buttonCapture.setImageResource(R.mipmap.player_record);
                changeRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                releaseMediaRecorder();
                Toast.makeText(VideoInputActivity.this, R.string.video_captured, Toast.LENGTH_SHORT).show();
                recording = false;
                Intent intent = new Intent();
                intent.putExtra(INTENT_EXTRA_VIDEO_PATH, url_file);
                setResult(RESULT_OK, intent);
                releaseCamera();
                releaseMediaRecorder();
                finish();
            } else {
                //准备开始录制视频
                if (!prepareMediaRecorder()) {
                    Toast.makeText(VideoInputActivity.this, getString(R.string.camera_init_fail), Toast.LENGTH_SHORT).show();
                    setResult(RESULT_CODE_FOR_RECORD_VIDEO_FAILED);
                    releaseCamera();
                    releaseMediaRecorder();
                    finish();
                }
                //开始录制视频
                runOnUiThread(new Runnable() {
                    public void run() {
                        // If there are stories, add them to the table
                        try {
                            mediaRecorder.start();
                            startChronometer();
                            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                                changeRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                            } else {
                                changeRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                            }
                            buttonCapture.setImageResource(R.mipmap.player_stop);
                        } catch (final Exception ex) {
                            Log.i("---", "Exception in thread");
                            setResult(RESULT_CODE_FOR_RECORD_VIDEO_FAILED);
                            releaseCamera();
                            releaseMediaRecorder();
                            finish();
                        }
                    }
                });
                recording = true;
            }
        }
    };

    View.OnClickListener startMusicListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "thread interrup");
            musicService.playerStart();
        }
    };

    View.OnClickListener stopMusicListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.i(TAG, "end");
            musicService.playerStop();
        }
    };

    private void changeRequestedOrientation(int orientation) {
        setRequestedOrientation(orientation);
    }

    // 储存视频
    private File getOutputMediaFile() {
        String appName = getPackageName();
        File dir = new File(Environment.getExternalStorageDirectory() + "/" +appName);
        if (!dir.exists()){
            dir.mkdir();
        }
        url_file = dir+ "/video_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".mp4";
        Log.i("filePath",url_file);
        return  new File(url_file);
    }

    // 储存图像
    private String getOutputPhoto() {
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory() + "/" + getPackageName());
        if (!mediaStorageDir.exists()){
            mediaStorageDir.mkdir();
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String path = mediaStorageDir.getPath() + File.separator + "IMG_"+ timeStamp + ".jpg";
        return path;
    }

    private class MyServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.LocalBinder binder = (MusicService.LocalBinder) service;
            musicService = binder.getLocalService();
            mBind = true;
            Log.i(TAG,"连接");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG,"取消连接");
            mBind = false;
        }
    }

    @Override
    protected void onDestroy() {
        // TODO 自动生成的方法存根
        super.onDestroy();
        // 取消绑定的播放音乐的后台服务
        unbindService(myServiceConnection);
    }

    private void startFaceDetect(final String filePath, final int emojiNumber) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                EmojiFaceComparer.test(filePath, emojiNumber);
            }
        });
        thread.start();
    }
    public Bitmap rotateMyBitmap(Bitmap bmp){
        Matrix matrix = new Matrix();
        matrix.postRotate(270);
        Bitmap nbmp2 = Bitmap.createBitmap(bmp, 0,0, bmp.getWidth(),  bmp.getHeight(), matrix, true);
        return nbmp2;
    };

    public void removeFragment(Fragment fragment) {
        getFragmentManager()
                .beginTransaction()
                .remove(fragment)
                .commit();
    }

    public void addFragment(Fragment fragment){
        getFragmentManager()
                .beginTransaction()
                .add(R.id.main_frame, fragment)
                .commit();
    }

    public void replaceFragment (Fragment fragment){
        String backStateName = fragment.getClass().getName();
        FragmentManager manager = getFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        if(backStateName.contains("Music")){
            ft.setCustomAnimations(R.animator.slide_up_in, R.animator.disappear, R.animator.disappear, R.animator.slide_down);
        }else if(backStateName.contains("Home")){
            ft.setCustomAnimations(0,0);
        }else{
            ft.setCustomAnimations(R.animator.enter_from_right, R.animator.exit_to_left, R.animator.enter_from_left, R.animator.exit_to_right);
        }

        boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);

        if (!fragmentPopped){ //fragment not in back stack, create it.
            ft.replace(R.id.main_frame, fragment);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }

    /**
     * send tab index message when turning to homepage
     * @param home
     * @param tabIndex
     */
    public void replaceFragment(HomeFragment home, int tabIndex){
        String backStateName = home.getClass().getName();

        FragmentManager manager = getFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.setCustomAnimations(0,0);
        boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);

        if (!fragmentPopped){ //fragment not in back stack, create it.
            Bundle bundle = new Bundle();
            bundle.putString("tab", tabIndex+"");
            //set Fragmentclass Arguments
            homeFragment.setArguments(bundle);

            ft.replace(R.id.main_frame, home);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }

    // need by 手q登陆-start
    public void login() {
        // 实现IUiListener三个回调
        mIUiListener = new IUiListener() {
            @Override
            public void onComplete(Object o) {
                Toast.makeText(VideoInputActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                // 登陆成功进行页面切换
                loginFragment.loginSuccess();
            }

            @Override
            public void onError(UiError uiError) {
                Toast.makeText(VideoInputActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(VideoInputActivity.this, "登录取消", Toast.LENGTH_SHORT).show();
            }
        };
        mTencent.login(this, "all", mIUiListener);
    }

    // 应用调用Andriod_SDK接口时，如果要成功接收到回调，需要在调用接口的Activity的onActivityResult方法中增加如下代码
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_LOGIN) {
            Tencent.onActivityResultData(requestCode, resultCode, data, mIUiListener);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    // need by 手q登陆-end

}
