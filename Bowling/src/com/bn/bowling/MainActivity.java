package com.bn.bowling;
import com.bn.util.constant.Constant;
import com.bn.util.manager.SoundManager;
import com.bn.util.screenscale.ScreenScaleUtil;
import android.os.Bundle;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity 
{
	private MySurfaceView mGLSurfaceView;
	public static SoundManager sound;
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        //设置为全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		              WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//设置为竖屏模式
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Constant.ssr=ScreenScaleUtil.calScale(dm.widthPixels, dm.heightPixels);
        
        System.out.println("activity width:"+dm.widthPixels+" height:"+ dm.heightPixels);
        sound=new SoundManager(this);        
		//初始化GLSurfaceView
        mGLSurfaceView = new MySurfaceView(this);
        setContentView(mGLSurfaceView);	
        mGLSurfaceView.requestFocus();//获取焦点
        mGLSurfaceView.setFocusableInTouchMode(true);//设置为可触控  
    }
    @Override
    protected void onResume() 
    {
        super.onResume();
        //音乐
        if(MainActivity.sound.mp!=null)
   		{
        	MainActivity.sound.mp.start();
   		}
    }
    @Override
    protected void onPause() 
    {
        super.onPause();
        //音乐
        if(MainActivity.sound.mp!=null)
   		{
        	MainActivity.sound.mp.pause();
   		}
    }
}
