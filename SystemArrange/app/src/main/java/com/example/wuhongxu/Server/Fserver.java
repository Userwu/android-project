package com.example.wuhongxu.Server;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.wuhongxu.systemarrange.R;

public class Fserver extends Service implements SeekBar.OnSeekBarChangeListener,OnTouchListener
{

    //定义浮动窗口布局
            LinearLayout mFloatLayout;
    WindowManager.LayoutParams wmParams;
    //创建浮动窗口设置布局参数的对象
    WindowManager mWindowManager;
    AudioManager mAudioManager;

    ImageButton imgBtn1,imgBtn2,imgBtn3;
    SeekBar seekBar1,seekBar2,seekBar3;
    private static final String TAG = "Fserver";
    private float xStart, yStart;
    private float xEnd,yEnd;
    @Override
    public void onCreate()
    {
        // TODO Auto-generated method stub
        super.onCreate();
        System.out.println("ServiceCreate");
        createFloatView();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("commandcommandcommand");
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public IBinder onBind(Intent intent)
    {
        // TODO Auto-generated method stub
        System.out.println("onBind");
        return null;
    }

    private void createFloatView()
    {
        wmParams = new WindowManager.LayoutParams();
        //获取的是WindowManagerImpl.CompatModeWrapper
        mWindowManager = (WindowManager)getApplication().getSystemService(getApplication().WINDOW_SERVICE);
        Log.i(TAG, "mWindowManager--->" + mWindowManager);
        //设置window type
        wmParams.type = LayoutParams.TYPE_PHONE;
        //设置图片格式，效果为背景透明
        wmParams.format = PixelFormat.RGBA_8888;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE;
        //调整悬浮窗显示的停靠位置为左侧置顶
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
        // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
        wmParams.x = 0;
        wmParams.y = 0;

        //设置悬浮窗口长宽数据
        wmParams.width = 1000;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

         /*// 设置悬浮窗口长宽数据
        wmParams.width = 200;
        wmParams.height = 80;*/

        LayoutInflater inflater = LayoutInflater.from(getApplication());
        //获取浮动窗口视图所在布局
        mFloatLayout = (LinearLayout) inflater.inflate(R.layout.float_layout, null);
        //添加mFloatLayout
        mWindowManager.addView(mFloatLayout, wmParams);
        //浮动窗口按钮
        imgBtn1 = (ImageButton) mFloatLayout.findViewById(R.id.imgBtn1);
        imgBtn2 = (ImageButton) mFloatLayout.findViewById(R.id.imgBtn2);
        imgBtn3 = (ImageButton) mFloatLayout.findViewById(R.id.imgBtn3);
        seekBar1 = (SeekBar) mFloatLayout.findViewById(R.id.seekBar1);
        seekBar2 = (SeekBar) mFloatLayout.findViewById(R.id.seekBar2);
        seekBar3 = (SeekBar) mFloatLayout.findViewById(R.id.seekBar3);
        //音量控制,初始化定义
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        //最大音量
        int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);
        //当前音量
        int currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);
        seekBar1.setMax(maxVolume);
        seekBar1.setProgress(currentVolume);

        int maxVolume2 = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolume2 = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        seekBar2.setMax(maxVolume2);
        seekBar2.setProgress(currentVolume2);

        int maxVolume3 = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM);
        int currentVolume3 = mAudioManager.getStreamVolume(AudioManager.STREAM_ALARM);
        seekBar3.setMax(maxVolume3);
        seekBar3.setProgress(currentVolume3);


        mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
                .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        Log.i(TAG, "Width/2--->" + imgBtn1.getMeasuredWidth() / 2);
        Log.i(TAG, "Height/2--->" + imgBtn1.getMeasuredHeight() / 2);

        seekBar1.setOnSeekBarChangeListener(this);
        seekBar2.setOnSeekBarChangeListener(this);
        seekBar3.setOnSeekBarChangeListener(this);

        //设置监听浮动窗口的触摸移动
        mFloatLayout.setOnTouchListener(this);
        imgBtn1.setOnTouchListener(this);
        imgBtn2.setOnTouchListener(this);
        imgBtn3.setOnTouchListener(this);
        imgBtn1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(Fserver.this, "onClick", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroy()
    {
        // TODO Auto-generated method stub
        super.onDestroy();
        System.out.println("onDestroy");
        if(mFloatLayout != null)
        {
            //移除悬浮窗口
            mWindowManager.removeView(mFloatLayout);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()){
            case R.id.seekBar1:
                mAudioManager.setStreamVolume(AudioManager.STREAM_SYSTEM,progress,0);
                break;
            case R.id.seekBar2:
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,progress,0);
                break;
            case R.id.seekBar3:
                mAudioManager.setStreamVolume(AudioManager.STREAM_ALARM,progress,0);
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub
        //getRawX是触摸位置相对于屏幕的坐标，getX是相对于按钮的坐标
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (wmParams.width == 50) {
                    wmParams.width = 1000;
                    wmParams.x = 20;
                    mWindowManager.updateViewLayout(mFloatLayout, wmParams);
                }
                xStart = event.getRawX();
                yStart = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e(TAG, "onTouch 长按=====");
                xEnd = event.getRawX();
                yEnd = event.getRawY();
                wmParams.x = (int) (wmParams.x + (xEnd - xStart));
                wmParams.y = (int) (wmParams.y + yEnd - yStart);
                //wmParams.x = (int) event.getRawX() - imgBtn1.getMeasuredWidth() / 2;
                Log.e(TAG, "RawX" + event.getRawX());
                Log.e(TAG, "X" + event.getX());

                //减25为状态栏的高度
                //wmParams.y = (int) event.getRawY() - imgBtn1.getMeasuredHeight() / 2 - 25;
                Log.e(TAG, "RawY" + event.getRawY());
                Log.e(TAG, "Y" + event.getY());
                //刷新
                mWindowManager.updateViewLayout(mFloatLayout, wmParams);
                xStart = event.getRawX();
                yStart = event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                Log.e(TAG, "onTouch up wmparam.x === " + wmParams.x);

                if (wmParams.x <= 0) {
                    wmParams.x = 0;
                    wmParams.width = 50;
                    mWindowManager.updateViewLayout(mFloatLayout, wmParams);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.e(TAG, "onTouch cancel");
                break;
            case MotionEvent.ACTION_MASK:
                Log.e(TAG, "onTouch mask");
                break;
            case MotionEvent.ACTION_SCROLL:
                Log.e(TAG, "onTouch scroll");
                break;
            case MotionEvent.ACTION_OUTSIDE:
                Log.e(TAG, "onTouch outside");
                break;
        }

        return false;  //此处必须返回false，否则OnClickListener获取不到监听
    }
}