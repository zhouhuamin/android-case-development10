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
	private SceneRenderer mRenderer;//������Ⱦ��
	public BNAbstractView currView;//��ǰ����
	public BNAbstractView mainView;//������
	public OptionView optionView;//ѡ�ؽ���
	public GameView gameView;//��ǰ����
	public BNAbstractView helpView;//��������
	public BNAbstractView SelcetpzView;//ѡ��ƿ�ӽ���
	public BNAbstractView SelcetqiuView;//ѡ���������
	public boolean isInitOver = false;						//��Դ�Ƿ��ʼ�����
	public boolean isMoveCameraBack=true;//�Ƿ�����ƶ������
	public boolean isMoveCameraFront=false;//�Ƿ���ǰ�ƶ������
	public int initIndex=0;
	private static boolean isExit = false;
	
	public GLRecorder recorder;//¼����Ƶ����
	public MySurfaceView(MainActivity activity)
	{
		super(activity);
		this.activity=activity;
		this.setEGLContextClientVersion(2);//����ʹ��OPENGL ES2.0
		mRenderer = new SceneRenderer();//����������Ⱦ��
		setRenderer(mRenderer);//������Ⱦ��
		setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//������ȾģʽΪ������Ⱦ
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
			}else if(currView==optionView)//��ѡ����淵��������
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
			}else if(currView==gameView)//����Ϸ���淵����ͣ����
			{
				isPause=true;
				gameView.pv.distance=24;
				gameView.Pause=new BN2DObject(980, 1700, 130, 130, TextureManager.getTextures("start.png"), 
						ShaderManager.getShader(1));
			}
			else if(currView==SelcetqiuView)//��ѡ��ƿ�ӽ��淵�ص��������汳������
			{
				if(isGamePlay){
					gameView.phc.initAllObject(this);
				}
				currView=gameView;
				isPause=false;
			}else if(currView==SelcetpzView)//��ѡ��ƿ�ӽ��淵�ص��������汳������
			{
				if(isGamePlay){
					gameView.phc.initAllObject(this);
				}
				currView=gameView;
				isPause=false;
			}else if(currView==gameView)//����Ϸ���淵�ص�������
			{
				currView=mainView;
			}else if(currView==helpView)//�Ӱ������淵�ص�������
			{
				currView=mainView;
			}
			else if(currView==mainView)//ֻ�д���������ʱ�ſ��԰����ؼ���������
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
			isExit = true; // ׼���˳�
			Toast.makeText(this.getContext(),"�ٰ�һ���˳���Ϸ", Toast.LENGTH_SHORT).show();
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
	
	public Handler handler = new Handler()//����ͼƬ�Ƿ�ɹ� ��ʾ��
	{
		@Override
		public void handleMessage(Message msg)
		{
			if(msg.what==0)//��ʼ¼����Ϸ��Ƶ��ʾ
			{
				Toast.makeText(MySurfaceView.this.getContext(),"��ʼ¼����Ϸ��Ƶ������", Toast.LENGTH_SHORT).show();
			}else if(msg.what==1)//����¼����Ϸ��Ƶ��ʾ
			{
				Toast.makeText(MySurfaceView.this.getContext(),"����¼����Ϸ��Ƶ������", Toast.LENGTH_SHORT).show();
			}else if(msg.what==2)//�ϴ�¼����Ϸ��Ƶ��ʾ
			{
				Toast.makeText(MySurfaceView.this.getContext(),"����ϴ����Խ���Ƶ���浽���أ�", Toast.LENGTH_SHORT).show();
			}
		}
	};
	
	
	private class SceneRenderer implements GLSurfaceView.Renderer 
	{
		@Override
		public void onDrawFrame(GL10 gl) 
		{
			//�����Ȼ�������ɫ����
			GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
			if(currView != null)
			{
				currView.drawView(gl);//���ƽ�����Ϣ
			}
		}
		@Override
		public void onSurfaceChanged(GL10 gl, int width, int height)
		{//�����Ӵ���С��λ��
        	if(ShangChuanView){
        		ShangChuanView=false;//�ϴ�����
        	}
			GLES20.glViewport
			(
					(int)Constant.ssr.lucX,//x
					(int)Constant.ssr.lucY,//y
					(int)(Constant.SCREEN_WIDTH_STANDARD*Constant.ssr.ratio),//width
					(int)(Constant.SCREEN_HEIGHT_STANDARD*Constant.ssr.ratio)//height
			);
			
			//����GLSurfaceView�Ŀ�߱�
			float ratio = (float) width / height;
			//��ʼ���任����
			MatrixState2D.setInitStack();
			MatrixState3D.setInitStack();
			//���ô˷����������ƽ��ͶӰ����
			MatrixState2D.setProjectOrtho(-ratio, ratio, -1, 1, 1, 100);
			//���ô˷������������9����λ�þ���
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
			//��ʼ����ɫ��
			ShaderManager.loadingShader(MySurfaceView.this);
			//������Ļ����ɫRGBA
			GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
			//�򿪱������
			GLES20.glEnable(GLES20.GL_CULL_FACE);
			
			 new Thread()
	         {
	            	public void run()
	            	{
	            		while(threadFlag)
	            		{
	            			eAngle=(eAngle+2)%360;//������ת�Ƕ�	            			
	            			cAngle=(cAngle+0.2f)%360;//������ת�Ƕ�
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
		// ����ShareRec��Appkey ÿ��Ӧ�õ�keyֵ��ͬ
		return "65b839a5b2c8";
	}
}