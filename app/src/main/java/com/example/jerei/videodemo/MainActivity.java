package com.example.jerei.videodemo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.sh.shvideolibrary.VideoInputActivity;

import java.io.File;

public class MainActivity extends Activity {
    ImageView image;
    static String TAG="MainActivity";
    String path;//视频录制输出地址
    //视频压缩数据地址
    private String currentOutputVideoPath = "/mnt/sdcard/out.mp4";
    private static final int REQUEST_CODE_FOR_RECORD_VIDEO = 5230;//录制视频请求码
    Double videoLength=0.0;//视频时长

    // 左滑
    private float x1,x2;
    static final int MIN_DISTANCE = 150;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        QMUIStatusBarHelper.translucent(this);
        image = (ImageView) findViewById (R.id.image);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openView(path);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST_CODE_FOR_RECORD_VIDEO&&resultCode==RESULT_CANCELED){

        }
        if(requestCode==REQUEST_CODE_FOR_RECORD_VIDEO&&resultCode==RESULT_OK){
            String path = data.getStringExtra(VideoInputActivity.INTENT_EXTRA_VIDEO_PATH);
            Log.e("地址:",path);
            //根据视频地址获取缩略图
            this.path =path;
            Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Video.Thumbnails.MINI_KIND);
            image.setImageBitmap(bitmap);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void openView(String path){
        if(TextUtils.isEmpty(path)){
            return;
        }
        File file = new File(path);
        SystemAppUtils.openFile(file,this);
    }

    /**
     * 检测左滑事件
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                float deltaX = x2 - x1;
                if (Math.abs(deltaX) > MIN_DISTANCE)
                {
                    VideoInputActivity.startActivityForResult(MainActivity.this, REQUEST_CODE_FOR_RECORD_VIDEO,VideoInputActivity.Q720);
                }
                break;
        }
        return super.onTouchEvent(event);
    }
}
