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
        //����Ϊȫ��
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		              WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//����Ϊ����ģʽ
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Constant.ssr=ScreenScaleUtil.calScale(dm.widthPixels, dm.heightPixels);
        
        System.out.println("activity width:"+dm.widthPixels+" height:"+ dm.heightPixels);
        sound=new SoundManager(this);        
		//��ʼ��GLSurfaceView
        mGLSurfaceView = new MySurfaceView(this);
        setContentView(mGLSurfaceView);	
        mGLSurfaceView.requestFocus();//��ȡ����
        mGLSurfaceView.setFocusableInTouchMode(true);//����Ϊ�ɴ���  
    }
    @Override
    protected void onResume() 
    {
        super.onResume();
        //����
        if(MainActivity.sound.mp!=null)
   		{
        	MainActivity.sound.mp.start();
   		}
    }
    @Override
    protected void onPause() 
    {
        super.onPause();
        //����
        if(MainActivity.sound.mp!=null)
   		{
        	MainActivity.sound.mp.pause();
   		}
    }
}
