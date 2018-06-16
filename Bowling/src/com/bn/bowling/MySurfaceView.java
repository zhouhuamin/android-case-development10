package com.bn.bowling;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import static com.bn.util.constant.Constant.cAngle;
import static com.bn.util.constant.Constant.eAngle;
import static com.bn.util.constant.Constant.skyX;
import static com.bn.util.constant.Constant.skyY;
import static com.bn.util.constant.Constant.threadFlag;
import static com.bn.util.constant.SourceConstant.*;
import static com.bn.util.constant.SourceConstant.isDrawAN;
import static com.bn.util.constant.SourceConstant.isDrawScore;
import static com.bn.util.constant.SourceConstant.isGamePlay;
import static com.bn.util.constant.SourceConstant.isPause;
import static com.bn.util.constant.SourceConstant.isScoreDown;

import cn.sharerec.recorder.GLRecorder;
import cn.sharerec.recorder.SrecGLSurfaceView;

import com.bn.object.BN2DObject;
import com.bn.util.constant.Constant;
import com.bn.util.constant.MyHHData;
import com.bn.util.constant.SourceConstant;
import com.bn.util.manager.ShaderManager;
import com.bn.util.manager.TextureManager;
import com.bn.util.matrix.MatrixState2D;
import com.bn.util.matrix.MatrixState3D;
import com.bn.view.BNAbstractView;
import com.bn.view.GameView;
import com.bn.view.LoadView;
import com.bn.view.OptionView;

import android.annotation.SuppressLint;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class MySurfaceView extends SrecGLSurfaceView
{
	public MainActivity activity;
	private SceneRenderer mRenderer;//场景渲染器
	public BNAbstractView currView;//当前界面
	public BNAbstractView mainView;//主界面
	public OptionView optionView;//选关界面
	public GameView gameView;//当前界面
	public BNAbstractView helpView;//帮助界面
	public BNAbstractView SelcetpzView;//选择瓶子界面
	public BNAbstractView SelcetqiuView;//选择保龄球界面
	public boolean isInitOver = false;						//资源是否初始化完毕
	public boolean isMoveCameraBack=true;//是否向后移动摄像机
	public boolean isMoveCameraFront=false;//是否向前移动摄像机
	public int initIndex=0;
	private static boolean isExit = false;
	
	public GLRecorder recorder;//录制视频对象
	public MySurfaceView(MainActivity activity)
	{
		super(activity);
		this.activity=activity;
		this.setEGLContextClientVersion(2);//设置使用OPENGL ES2.0
		mRenderer = new SceneRenderer();//创建场景渲染器
		setRenderer(mRenderer);//设置渲染器
		setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//设置渲染模式为主动渲染
	}

	public boolean onTouchEvent(MotionEvent e)
	{
		if(currView==null)
		{
			return false;
		}
		return currView.onTouchEvent(e);
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			if(SourceConstant.ShangChuanView)
			{
					currView=gameView;
					SourceConstant.ShangChuanView=false;
			}else if(currView==optionView)//从选项界面返回主界面
			{
					currView=mainView;
			}else if(drawCameraTip&&currView==gameView)
			{
				drawCameraTip=false;
			}
			else if(currView==gameView&&isPause)
			{
				if(gameView.pv.isbackmain)
				{
					gameView.pv.isbackmain=false;
					isPause=false;
					isDrawAN=true;
					gameView.pv.pauseView=MyHHData.pauseView();
					gameView.Pause=new BN2DObject(980, 1700, 130, 130, TextureManager.getTextures("pause.png"), 
							ShaderManager.getShader(1));
				}
				if(gameView.pv.isSheZhi)
				{
					 gameView.pv.isSheZhi=false;
					 isPause=false;
					 isDrawAN=true;
					 isDrawScore=true;
					 isScoreDown=true;
					 gameView.distance=0;
					 gameView.pv.pauseView=MyHHData.pauseView();
					 gameView.Pause=new BN2DObject(980, 1700, 130, 130, TextureManager.getTextures("pause.png"), 
							ShaderManager.getShader(1));
				}
				isPause=false;
				gameView.Pause=new BN2DObject(980, 1700, 130, 130, TextureManager.getTextures("pause.png"), 
						ShaderManager.getShader(1));
				gameView.pausecount=0;
			}else if(currView==gameView)//从游戏界面返回暂停界面
			{
				isPause=true;
				gameView.pv.distance=24;
				gameView.Pause=new BN2DObject(980, 1700, 130, 130, TextureManager.getTextures("start.png"), 
						ShaderManager.getShader(1));
			}
			else if(currView==SelcetqiuView)//从选择瓶子界面返回到设置桌面背景界面
			{
				if(isGamePlay){
					gameView.phc.initAllObject(this);
				}
				currView=gameView;
				isPause=false;
			}else if(currView==SelcetpzView)//从选择瓶子界面返回到设置桌面背景界面
			{
				if(isGamePlay){
					gameView.phc.initAllObject(this);
				}
				currView=gameView;
				isPause=false;
			}else if(currView==gameView)//从游戏界面返回到主界面
			{
				currView=mainView;
			}else if(currView==helpView)//从帮助界面返回到主界面
			{
				currView=mainView;
			}
			else if(currView==mainView)//只有处于主界面时才可以按返回键返回桌面
			{
				exit();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	private void exit()
	{
		if (isExit == false) 
		{
			isExit = true; // 准备退出
			Toast.makeText(this.getContext(),"再按一次退出游戏", Toast.LENGTH_SHORT).show();
			new Handler().postDelayed(new Runnable()
			{
				@Override  
				public void run()
				{
					isExit = false;
				}
			}, 2500);
		}else
		{
			android.os.Process.killProcess(android.os.Process.myPid()); 
		}
	}
	
	public Handler handler = new Handler()//保存图片是否成功 提示框
	{
		@Override
		public void handleMessage(Message msg)
		{
			if(msg.what==0)//开始录制游戏视频提示
			{
				Toast.makeText(MySurfaceView.this.getContext(),"开始录制游戏视频！！！", Toast.LENGTH_SHORT).show();
			}else if(msg.what==1)//结束录制游戏视频提示
			{
				Toast.makeText(MySurfaceView.this.getContext(),"结束录制游戏视频！！！", Toast.LENGTH_SHORT).show();
			}else if(msg.what==2)//上传录制游戏视频提示
			{
				Toast.makeText(MySurfaceView.this.getContext(),"点击上传可以将视频保存到本地！", Toast.LENGTH_SHORT).show();
			}
		}
	};
	
	
	private class SceneRenderer implements GLSurfaceView.Renderer 
	{
		@Override
		public void onDrawFrame(GL10 gl) 
		{
			//清除深度缓冲与颜色缓冲
			GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
			if(currView != null)
			{
				currView.drawView(gl);//绘制界面信息
			}
		}
		@Override
		public void onSurfaceChanged(GL10 gl, int width, int height)
		{//设置视窗大小及位置
        	if(ShangChuanView){
        		ShangChuanView=false;//上传结束
        	}
			GLES20.glViewport
			(
					(int)Constant.ssr.lucX,//x
					(int)Constant.ssr.lucY,//y
					(int)(Constant.SCREEN_WIDTH_STANDARD*Constant.ssr.ratio),//width
					(int)(Constant.SCREEN_HEIGHT_STANDARD*Constant.ssr.ratio)//height
			);
			
			//计算GLSurfaceView的宽高比
			float ratio = (float) width / height;
			//初始化变换矩阵
			MatrixState2D.setInitStack();
			MatrixState3D.setInitStack();
			//调用此方法计算产生平行投影矩阵
			MatrixState2D.setProjectOrtho(-ratio, ratio, -1, 1, 1, 100);
			//调用此方法产生摄像机9参数位置矩阵
			MatrixState2D.setCamera(0,0,5,0f,0f,0f,0f,1f,0f);
			MatrixState2D.setLightLocation(0,50,0);
			
			if(currView==null){
				LoadView lv=new LoadView(MySurfaceView.this);
				lv.initSound();
				currView=lv;
				lv=null;
			}			
		}
		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config)
		{
			recorder=MySurfaceView.this.getRecorder();
			//初始化着色器
			ShaderManager.loadingShader(MySurfaceView.this);
			//设置屏幕背景色RGBA
			GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
			//打开背面剪裁
			GLES20.glEnable(GLES20.GL_CULL_FACE);
			
			 new Thread()
	         {
	            	public void run()
	            	{
	            		while(threadFlag)
	            		{
	            			eAngle=(eAngle+2)%360;//地球自转角度	            			
	            			cAngle=(cAngle+0.2f)%360;//天球自转角度
	            			skyX-=0.55f;
	            			skyY+=0.15f;
	            			if(skyX<-30){
	            				skyX=25;
	            			}
	            			if(skyY>15){
	            				skyY=0;
	            			}
	            			try {
								Thread.sleep(100);
							} catch (InterruptedException e) {				  			
								e.printStackTrace();
							}
	            		}
	            	}
	            }.start();
		}
	}

	@Override
	protected String getShareRecAppkey() 
	{
		// 返回ShareRec的Appkey 每个应用的key值不同
		return "65b839a5b2c8";
	}
}