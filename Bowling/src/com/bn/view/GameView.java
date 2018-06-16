package com.bn.view;
import static com.bn.util.constant.Constant.*;
import static com.bn.util.constant.SourceConstant.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.microedition.khronos.opengles.GL10;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import com.bn.bowling.MainActivity;
import com.bn.bowling.MySurfaceView;
import com.bn.bowling.R;
import com.bn.object.BN2DObject;
import com.bn.object.GameObject;
import com.bn.util.constant.Constant;
import com.bn.util.manager.ShaderManager;
import com.bn.util.manager.TextureManager;
import com.bn.util.matrix.MatrixState2D;
import com.bn.util.matrix.MatrixState3D;
import com.bn.util.special.SpecialUtil;
import com.bn.util.thread.PhysicsThread;
import com.bn.util.tool.AABB3;
import com.bn.util.tool.CalculateScore;
import com.bn.util.tool.IntersectantUtil;
import com.bn.util.tool.PhyCaulate;
import com.bn.util.tool.SYSUtil;
import com.bn.util.tool.Vector3f3D;
import com.bulletphysics.linearmath.Transform;
import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.opengl.GLES20;
import android.view.MotionEvent;

@SuppressLint("UseSparseArrays")
public class GameView extends BNAbstractView
{
	public PhysicsThread pt;//物理线程
	public MySurfaceView viewManager;
	public PhyCaulate phc;//物理封装类
	SpecialUtil special;
	GameObject curr=null;//当前触控的对象引用
	GameObject tempBall=null;
	float[][] resultA=null;//缓存瓶子数据
	public static float[] ballA=null;//缓存球数据
	BN2DObject triangle;//倒三角绘制对象
	BN2DObject showScore;//显示分数绘制对象
	Map<Integer,GameObject>  hmTemp1=new HashMap<Integer,GameObject>();
	List<BN2DObject> al=new ArrayList<BN2DObject>();//存放BNObject对象
	List<BN2DObject> al2=new ArrayList<BN2DObject>();//存放BNObject对象
	public BN2DObject Pause;//暂停按钮
	public PauseView pv;//暂停界面
	private boolean initObect=true;
	public boolean isInPause=false;//是否点击到暂停界面内

	int count=0;//计数器
	int drawScoreCount=0;	
	int drawPositionIndex=0;//绘制分数的位置
    float skyAngle=0;//天空旋转角度
    float guidao_z=50;//轨道z坐标
    float feixingwu1_z=50;//飞行物1
	float preX=0,preY=0;
	float ez=0;
	float tz=-24;//临时记录摄像机的两个参数	
	public int pausecount=0;
	public int distance=0;//绘制分数缓缓上升 下降的变量	
	int drawFireIndex=0;
	float a=0.6f;
	float saveA=0;
	boolean startRecorder=false;//开始录制、结束录制
	boolean isRecorder=false;//是否支持录制
	BN2DObject[] camera=new BN2DObject[2];//录像图标绘制对象
	float touchX,touchY;
	
	public GameView(MySurfaceView viewManager)
	{
		this.viewManager=viewManager;
		initView();
	}
	@Override
	public boolean onTouchEvent(MotionEvent e)
	{
		float x=Constant.fromRealScreenXToStandardScreenX(e.getX());//获取触控点的坐标
		float y=Constant.fromRealScreenYToStandardScreenY(e.getY());
		ArrayList<GameObject>  tempBall=new ArrayList<GameObject> ();
		synchronized(lockBall)
		{
			tempBall.addAll(alGBall);
		}
		if(tempBall.size()<=0){
			return false;
		}
        switch (e.getAction()) 
        {
        case MotionEvent.ACTION_DOWN://处理屏幕被按下的事件
        	curr=null;
        	if(!isMoveFlag&&x>=Pause_left&&x<=Pause_right&&y>=Pause_top&&y<=Pause_bottom)
        	{//点击暂停按钮
        		if(pausecount%2==0)
        		{
        			pv.distance=24;
        			Pause=new BN2DObject(980, 1700, 130, 130, TextureManager.getTextures("start.png"), 
        					ShaderManager.getShader(0));
        			isPause=true;
        			pausecount++;
        		}else
        		{
        			Pause=new BN2DObject(980, 1700, 130, 130, TextureManager.getTextures("pause.png"), 
        					ShaderManager.getShader(0));
        			isPause=false;
        			pausecount++;
        		}
        	}
        	
        	//游戏结束界面==========start================
        	if(inGameOverView&&x>=270&&x<=420&&y>=1100&&y<=1300)//回到选择场景界面
        	{
        		inGameOverView=false;
        		isDrawFireOver=false;//不再绘制粒子系统
    			isDrawFireWorks=false;
    			phc.initAllObject(viewManager);
    			reSetData();
        		viewManager.currView=viewManager.optionView;//回到选择场景界面
        	}else if(inGameOverView&&x>=650&&x<=800&&y>=1100&&y<=1300)//刷新游戏界面
        	{
        		inGameOverView=false;
        		isDrawFireOver=false;//不再绘制粒子系统
    			isDrawFireWorks=false;
        		viewManager.currView=viewManager.gameView;
        		phc.initAllObject(viewManager);
    			reSetData();
        	}
        	//游戏结束界面==========end================
        	
        	if(isPause&&x>=isPause_left&&x<=isPause_right&&y>=isPause_top&&y<=isPause_bottom)
        	{
        		isInPause=true;
        	}
        	if((isPause&&!isInPause)
        			||(isDrawFireOver&&CalculateScore.GameOver)
        			||(isDrawCurrentScore||(countTurn>10)))
        	{
        		return false;
        	}
        	
        	//计算仿射变换后AB两点的位置
			float[] AB=IntersectantUtil.calculateABPosition
			(
				x, //触控点X坐标
				y, //触控点Y坐标
				SCREEN_WIDTH_STANDARD, //屏幕宽度
				SCREEN_HEIGHT_STANDARD, //屏幕长度
				LEFT, //视角left、top值
				TOP,
				NEAR, //视角near、far值
				FAR
			);
			//射线AB
			Vector3f3D start = new Vector3f3D(AB[0], AB[1], AB[2]);//起点
			Vector3f3D end = new Vector3f3D(AB[3], AB[4], AB[5]);//终点
			Vector3f3D dir = end.minus(start);//长度和方向
			/*
			 * 计算AB线段与每个物体包围盒的最佳交点(与A点最近的交点)，
			 * 并记录有最佳交点的物体在列表中的索引值
			 */
			//记录列表中时间最小的索引值
    		float minTime=1;//记录列表中所有物体与AB相交的最短时间
    		AABB3 box = tempBall.get(0).lovnt.getCurrBox(); //获得物体AABB包围盒   
			float t = box.rayIntersect(start, dir, null);//计算相交时间
			if (t <= minTime) {
				minTime = t;//记录最小值
				curr=tempBall.get(0);
				touchX=x;
				touchY=y;
			}
			
			//录制视频（必须联网）===========start==============
        	
        	if(x>=CAMERA_START_LEFT&&x<=CAMERA_START_RIGHT
        			&&y>=CAMERA_START_TOP&&y<=CAMERA_START_BOTTOM)//点击此区域 则开始或者结束录制
        	{
        		startRecorder=!startRecorder;
        		isRecorder=viewManager.recorder.isAvailable();//是否支持录制视频
        		if(isRecorder)//如果支持录制
        		{
        			if(startRecorder)//开始录制
            		{
        				viewManager.handler.sendEmptyMessage(0);
        				viewManager.recorder.startRecorder();
            		}else//结束录制
            		{
            			viewManager.handler.sendEmptyMessage(1);
            			viewManager.recorder.stopRecorder();
            			drawCameraTip=true;
            		}
        		}
        	}
        	if(drawCameraTip&&x>=CAMERA_TIP_NO_LEFT&&x<=CAMERA_TIP_NO_RIGHT&&
        			y>=CAMERA_TIP_NO_TOP&&y<=CAMERA_TIP_NO_BOTTOM)//不上传录像
        	{
        		drawCameraTip=false;//不再绘制 放弃此视频
        	}else if(drawCameraTip&&x>=CAMERA_TIP_YES_LEFT&&x<=CAMERA_TIP_YES_RIGHT&&
        			y>=CAMERA_TIP_YES_TOP&&y<=CAMERA_TIP_YES_BOTTOM)//上传录像
        	{
        		ShangChuanView=true;
        		drawCameraTip=false;//不再绘制 放弃此视频
        		viewManager.recorder.showShare();
        	}
        	
        	//录制视频===========end==============			
   			break;
        case MotionEvent.ACTION_MOVE://处理滑动屏幕的事件
        	if(countTurn>10||(isPause&&!isInPause)){
        		return false;
        	}
    		if(isDrawCurrentScore||isDrawFireWorks)
    		{
    			return false;
    		}
        	break;
        case MotionEvent.ACTION_UP:
        	if(countTurn>10||(Math.abs(touchX-x)<TOUCH_DIS&&Math.abs(touchY-y)<TOUCH_DIS)){
        		return false;
        	}
        	if(!isMoveFlag&&curr!=null){
        		float dx=x-touchX;
        		float dy=y-touchY;
        		float vx=dx*0.03f;
        		float vy=dy*0.4f;
        		if(Math.abs(vx)<0.3f){//设置速度阈值
        			vx=0.0f;
        		}
        		vx=(vx>2.56f)?2.56f:vx;
        		vx=(vx<(-2.56f))?(-2.56f):vx;
        		
        		vy=(vy>(-15))?(-15):vy;
        		vy=(vy<(-35))?(-35):vy;
        		//设置百分比
        		if(((vy/vx)<-18)||(vy/vx)>18){
        			vx=(float) (vx*(Math.random()*0.3f));
        		}
        		else{
        			vx=(float) (vx*(Math.random()*0.7f));
        		}
        		
        		if(vy>(-20)){
        			a=0.3f;saveA=a;
        		}
        		else if(vy>(-30)&&vy<(-20)){
        			a=0.45f;saveA=a;
        		}
        		else{
        			a=0.6f;saveA=a;
        		}
    			status=0;//改变状态值status
    			isDrawFire=true;
        		isMoveFlag=true;//移动
        		isDrawAN=false;
        		isCastBall=true;//投球
    			ballCount++;//每轮的第几个球
    			synchronized(lockV)
    			{
    				qf.offer(new float[]{vx,0,vy});
    				tempBall.get(0).body.activate();//激活刚体
    			}
            	if(!effictOff){
            		MainActivity.sound.playMusic(Constant.BALL_ROLL, 0);//球滚音效
            	}
        	}
        	break;
        }
        preX=x;
        preY=y;
        if(isPause)
        {
        	return pv.onTouchEvent(e);
        }
    	return true;
	}
	public void moveCameraToBack()//向后移动摄像机
	{
		if(sceneIndex!=1)
		{//摄像机向后移动
			if(viewManager.isMoveCameraBack)
			{
				count++;
				if(EYE_Z<CAMERA_LIMIT_MAX){//向后移动
					if(count%2==0){
						EYE_Z+=CAMERA_SPAN;
						TARGET_Z+=CAMERA_SPAN;
					}		
				}
				else{//后移动标志位置为false
					viewManager.isMoveCameraBack=false;
				}
			}	
			else{
				count=0;
			}
		}
		else if(sceneIndex==1&&!viewManager.isMoveCameraFront)
		{
			EYE_Z=24;
			TARGET_Z=0;
		}
	}
	public void moveCameraToFront()//向前移动摄像机
	{
		if(viewManager.isMoveCameraFront){//摄像机向前移动
			if(EYE_Z>(CAMERA_LIMIT1_MIN-CAMERA_LIMIT_SPAN)
					&&ez>(CAMERA_LIMIT1_MIN-CAMERA_LIMIT_SPAN)){
				EYE_Z=ez;
				TARGET_Z=tz;
			}
			else if(EYE_Z<(CAMERA_LIMIT1_MIN+CAMERA_LIMIT_SPAN)
					&&EYE_Z>(CAMERA_LIMIT2_MIN))
			{//减速过程
				if((a-CAMERA_A_SPAN)>0)
				{
					a-=CAMERA_A_SPAN;
					EYE_Z-=a;
					TARGET_Z-=a;
				}				
				isScoreDown=false;//将分数框向上滑
			}
			else
			{
				a=saveA;
				isDrawFire=false;
				viewManager.isMoveCameraFront=false;//前移标志位置为false
			}
		}
	}	
	
	public void noMoveCameraToFront()
	{
		if(ballA!=null&&ballA.length>0)
		{
			if(ballA[2]<(-35)&&ballA[2]>(-38))
			{
				EYE_Z=ballA[2];
				TARGET_Z=EYE_Z-24;
				isScoreDown=false;//将分数框向上滑
				isDrawFire=false;
			}
		}
	}
	public void drawGame()
	{
    	ArrayList<GameObject>  tempBall=new ArrayList<GameObject> ();
		synchronized(lockBall)
		{
			tempBall.addAll(alGBall);
		}
		if(tempBall.size()<=0){
			return;
		}
		if(ballA!=null)
		{    			
    		MatrixState3D.pushMatrix();//保护现场
    		MatrixState3D.translate(ballA[0],ballA[1],ballA[2]);//进行平移变换
			MatrixState3D.rotate(ballA[3],ballA[4],ballA[5],ballA[6]);
			tempBall.get(0).drawSelfBall(0,0);//绘制球
    		MatrixState3D.popMatrix();//恢复现场
		}
		if(tempBall.get(0)!=null&&tempBall.get(0).body!=null){
            Vector3f v=tempBall.get(0).body.getLinearVelocity(new Vector3f());
            if(v.z!=0)
            {//如果球的z方向速度不为0，则向前移动摄像机
            	this.viewManager.isMoveCameraFront=true;
            }
            if(isCastBall&&(ballA[0]>GUIDAO_MAX_RIGHT||ballA[0]<GUIDAO_MIN_LEFT))
            {
            	isCastBall=false;
            	luoGouCount++;
            }
		}
    	synchronized(lockAll)
    	{
    		hmTemp1.clear();
    		hmTemp1.putAll(hm);
    	}
    	updateBottlesDatas();//获取所有瓶子的最新数据
    	if(resultA==null){
    		return;
    	}
    	for(int i=0;i<resultA.length;i++)
    	{//绘制瓶子
			if(resultA[i]!=null&&resultA[i].length>0&&hmTemp1.get(i+1)!=null)
			{
    			MatrixState3D.pushMatrix();//保护现场
    			MatrixState3D.translate(resultA[i][0],resultA[i][1],resultA[i][2]);//进行平移变换
    			MatrixState3D.rotate(resultA[i][3],resultA[i][4],resultA[i][5],resultA[i][6]);
    			hmTemp1.get(i+1).drawSelf(0,0);//绘制瓶子
    			MatrixState3D.popMatrix();//恢复现场
			}
    	}
	}
	
	public void drawGameMirror()//绘制镜像体
	{
    	ArrayList<GameObject>  tempBall=new ArrayList<GameObject> ();
		synchronized(lockBall)
		{
			tempBall.addAll(alGBall);
		}
		if(tempBall.size()<=0){
			return;
		}
		if(ballA!=null)
		{    			
			if(ballA[2]>-53f&&Math.abs(ballA[0])<5){
    	        GLES20.glFrontFace(GLES20.GL_CW);//GL_CW说明顺时针多边形为正面
    	        MatrixState3D.pushMatrix();//保护现场
        		MatrixState3D.translate(ballA[0],-ballA[1],ballA[2]);//进行平移变换
    			MatrixState3D.scale(1, -1, 1);
    			MatrixState3D.rotate(ballA[3],ballA[4],ballA[5],ballA[6]);
    			tempBall.get(0).drawSelfBall(0,0);
        		MatrixState3D.popMatrix();//恢复现场    	        
        		GLES20.glFrontFace(GLES20.GL_CCW);//GL_CCW说明逆时针多边形为正面，默认的是逆时针为正面
    	        
        		MatrixState3D.pushMatrix();//保护现场
        		MatrixState3D.translate(ballA[0],ballA[1],ballA[2]);//进行平移变换
    			MatrixState3D.rotate(ballA[3],ballA[4],ballA[5],ballA[6]);
    			tempBall.get(0).drawSelfBall(1,0);
        		MatrixState3D.popMatrix();//恢复现场
			}
		}
    	synchronized(lockAll)
    	{
    		hmTemp1.clear();
    		hmTemp1.putAll(hm);
    	}
    	updateBottlesDatas();//获取所有瓶子的最新数据
    	if(resultA==null){
    		return;
    	}    	
    	for(int i=0;i<resultA.length;i++)
    	{//绘制瓶子
			if(resultA[i]!=null&&resultA[i].length>0&&hmTemp1.get(i+1)!=null)
			{
				if(resultA[i][2]>-54f&&Math.abs(resultA[i][0])<5)
    			{
        	        GLES20.glFrontFace(GLES20.GL_CW);
        			MatrixState3D.pushMatrix();//保护现场
        			MatrixState3D.translate(resultA[i][0],-resultA[i][1],resultA[i][2]);//进行平移变换        		
        			MatrixState3D.scale(1, -1, 1);
        			MatrixState3D.rotate(resultA[i][3],resultA[i][4],resultA[i][5],resultA[i][6]);	
        			hmTemp1.get(i+1).drawSelf(0,0);//绘制瓶子
        			MatrixState3D.popMatrix();//恢复现场
              		 GLES20.glFrontFace(GLES20.GL_CCW);//逆时针
        	        
        			MatrixState3D.pushMatrix();//保护现场
        			MatrixState3D.translate(resultA[i][0],resultA[i][1],resultA[i][2]);//进行平移变换
        			MatrixState3D.rotate(resultA[i][3],resultA[i][4],resultA[i][5],resultA[i][6]);
        			hmTemp1.get(i+1).drawSelf(1,0);//绘制瓶子影子
        			MatrixState3D.popMatrix();//恢复现场
    			}
			}
    	}    	
	}
	
	public void drawScienceFictionScene()//绘制科幻场景
	{
		MatrixState3D.pushMatrix();    //保护现场        
		MatrixState3D.translate(-6,-0.9f, -10f);  //平移
        if(kjg_model!=null)
        {
            kjg_model.drawSelf(kjgId,0,0);//绘制场景     	
        }   
        MatrixState3D.popMatrix();    //恢复现场
		
        MatrixState3D.pushMatrix();//保护现场        
        MatrixState3D.translate(0, -0.5f, -19.5f);
        //若加载的物体部位空则绘制物体
        if(gd_model!=null)
        {
        	gd_model.drawSelf(gdId,0,0);
        }   
        MatrixState3D.popMatrix();  //恢复现场
	}
	
	public void drawBTMScienceFictionGD()//绘制科幻半透明轨道
	{
        MatrixState3D.pushMatrix();//保护现场        
        MatrixState3D.translate(0, -0.5f, -19.5f);
        //若加载的物体部位空则绘制物体
        if(gd_model!=null)
        {
        	gd_model.drawSelf(btmgdId,0,0);
        }   
        MatrixState3D.popMatrix();  //恢复现场
	}

	public void drawDesertScene()//绘制沙漠场景
	{
		 MatrixState3D.pushMatrix();
		 MatrixState3D.translate(0,-7f, -80f);   //ch.obj
         //若加载的物体部位空则绘制物体
         if(dimian_model!=null)
         {//地面
        	 dimian_model.drawSelf(desertId,0,0);
         }   
         MatrixState3D.popMatrix();    
        
        MatrixState3D.pushMatrix();        
        MatrixState3D.translate(0f,8f, -95f);   //ch.obj
        MatrixState3D.rotate(20, 0, 1, 0);
        //若加载的物体部位空则绘制物体
        if(smys_model!=null)
        {//沙漠岩石
        	smys_model.drawSelf(smysId,0,0);
        }   
        MatrixState3D.popMatrix();
        
        MatrixState3D.pushMatrix();
        MatrixState3D.translate(0, -0.5f, -19.5f);
        MatrixState3D.scale(0.7f, 1, 1);
        //若加载的物体部位空则绘制物体
        if(smgd_model!=null)
        {//沙漠轨道
        	smgd_model.drawSelf(smgdId,0,0);
        }   
        MatrixState3D.popMatrix(); 
        
        MatrixState3D.pushMatrix();
        MatrixState3D.translate(-9f, 0f, -40f); 
        //若加载的物体部位空则绘制物体
        if(tree2_model!=null)
        {
        	tree2_model.drawSelf(tree1Id,0,0);
        }   
        MatrixState3D.popMatrix(); 
        
        MatrixState3D.pushMatrix();
        MatrixState3D.translate(-18f, 0f, -84f);   
        //若加载的物体部位空则绘制物体
        if(tree3_model!=null)
        {
        	tree3_model.drawSelf(tree2Id,0,0);
        }   
        MatrixState3D.popMatrix(); 
        
        MatrixState3D.pushMatrix();
        MatrixState3D.translate(0f, 0f, -10f); 
        MatrixState3D.rotate(-3, 1,0,0);
        MatrixState3D.rotate(skyAngle, 0,1,0);     
        cloud.drawSelf(cloudId);
        MatrixState3D.popMatrix();
        skyAngle+=0.05f;
	}
	
	public void drawBTMDesertGD()//绘制半透明沙漠轨道
	{
		 MatrixState3D.pushMatrix();
        MatrixState3D.translate(0, -0.5f, -19.5f);
        MatrixState3D.scale(0.7f, 1, 1);
        //若加载的物体部位空则绘制物体
        if(smgd_model!=null)
        {//沙漠轨道
        	smgd_model.drawSelf(btmsmgdId,0,0);
        }   
        MatrixState3D.popMatrix(); 
	}
	
	public void drawSkyScene()//绘制星空场景
	{
		//坐标系推远
		MatrixState3D.pushMatrix();
		MatrixState3D.translate(-1.5f, -8f,-78f);   //ch.obj
		MatrixState3D.rotate(-110, 0, 0, 1);
        
        //若加载的物体部位空则绘制物体
        if(sky_model!=null)
        {
        	sky_model.drawSelf(skyId,0,0);
        }   
        MatrixState3D.popMatrix();     
        
        if(guidao_z>-20){
        	guidao_z--;
        }else{
        	isMoveGuiDaoFlag=false;//不移动轨道标识
        }
        MatrixState3D.pushMatrix();
        MatrixState3D.translate(-0.15f,-0.5f,guidao_z); 
		MatrixState3D.scale(0.85f, 1, 1);
        //若加载的物体部位空则绘制物体
        if(skygd_model2!=null)
        {//绘制长轨道
        	skygd_model2.drawSelf(skygdId,0,0);
        }   
        MatrixState3D.popMatrix();     
        
        if(feixingwu1_z>0)//-50
        {
        	feixingwu1_z--;
        }
        MatrixState3D.pushMatrix();
        MatrixState3D.translate(0,-6f,feixingwu1_z);
        MatrixState3D.rotate(180, 0, 1,0);
        MatrixState3D.rotate(180, 0, 0,1);
        //若加载的物体部位空则绘制物体
        if(feixingwu1_model!=null)
        {//绘制飞行物1
        	feixingwu1_model.drawSelf(feixingwu1Id,0,0);
        }   
        MatrixState3D.popMatrix();   
        
        MatrixState3D.pushMatrix();
        MatrixState3D.translate(skyX,skyY,-70); 
        MatrixState3D.rotate(60, 0,1,0);
        MatrixState3D.rotate(30,1,0,0);
        if(feixingwu2_model!=null)
        {//飞行物2
        	feixingwu2_model.drawSelf(feixingwu1Id,0,0);
        }   
        MatrixState3D.popMatrix();
        
        MatrixState3D.pushMatrix();
        MatrixState3D.translate(-skyX,-skyY-3,-50f); 
        MatrixState3D.rotate(-60, 0,1,0);
        if(feixingwu3_model!=null)
        {//飞行物3
        	feixingwu3_model.drawSelf(feixingwu2Id,0,0);
        }   
        MatrixState3D.popMatrix();
        
        MatrixState3D.pushMatrix();
        MatrixState3D.translate(-3f, 5, -10);  
        //地球自转
        MatrixState3D.rotate(eAngle, 0, 1, 0);
    	//绘制纹理圆球
        earth.drawSelf(textureIdEarth,textureIdEarthNight);     
        //推坐标系到月球位置            
        MatrixState3D.translate(1.5f, 0, 0);  
        //月球自转     
        MatrixState3D.rotate(eAngle, 0, 1, 0);
        //绘制月球
        moon.drawSelf(textureIdMoon);
        //恢复现场
        MatrixState3D.popMatrix();
        
        
        MatrixState3D.pushMatrix();
        MatrixState3D.translate(12f, 10, -65);  
        //地球自转
        MatrixState3D.rotate(eAngle, 0, 1, 0);
    	//绘制纹理圆球
        xing1.drawSelf(xingId);
        //恢复现场
        MatrixState3D.popMatrix();
	}

	public void drawBTMSkyGD()//绘制半透明星空轨道
	{
        if(guidao_z>-20){
        	guidao_z--;
        }else{
        	isMoveGuiDaoFlag=false;//不移动轨道标识
        }
        MatrixState3D.pushMatrix();
        MatrixState3D.translate(-0.15f,-0.5f,guidao_z); 
		MatrixState3D.scale(0.85f, 1, 1);
        //若加载的物体部位空则绘制物体
        if(skygd_model2!=null)
        {//绘制长轨道
        	skygd_model2.drawSelf(btmskygdId,0,0);
        }   
        MatrixState3D.popMatrix();     
	}
	
	public void updateBottlesDatas()//获取瓶子的最新数据
	{
		float[][] resultB=null;		
		if(hmTemp1.size()<=0){
			return;
		}
		synchronized(lockTransform){
			while(bTransform.size()>0)
			{
				resultB=bTransform.poll();
			}
		}
		if(resultB!=null)
		{
			resultA=resultB;
		}
	}
	
	public void updateBallDatas()//获取球的最新数据
	{
		float[] ballB=null;		
		synchronized(lockBallTransform){
			while(ballTransform.size()>0){
				ballB=ballTransform.poll();
			}
		}
		if(ballB!=null){
			ballA=ballB;
		}
		if(ballA!=null)
		{
			ez=ballA[2]+BALL_TO_EYE;
			tz=ballA[2]-BALL_TO_TARGET;
		}
	}
	public void initFirstBall()
	{
    	ArrayList<GameObject>  tempBall=new ArrayList<GameObject> ();
		synchronized(lockBall)
		{
			tempBall.addAll(alGBall);
		}
		if(tempBall.size()<=0){
			return;
		}
		ballA=new float[7];
		Transform trans=tempBall.get(0).body.getMotionState().getWorldTransform(new Transform());
		ballA[0]=trans.origin.x;
		ballA[1]=trans.origin.y;
		ballA[2]=trans.origin.z;
		for(int j=3;j<7;j++)
		{
			ballA[j]=1.0f;
		}
		Quat4f ro=trans.getRotation(new Quat4f());//获取当前变换的旋转信息
		if(ro.x!=0||ro.y!=0||ro.z!=0)
		{
			float[] fa=SYSUtil.fromSYStoAXYZ(ro);//将四元数转换成AXYZ的形式
			ballA[3]=fa[0];//记录第i个瓶子的旋转信息
			ballA[4]=fa[1];//记录第i个瓶子的旋转信息
			ballA[5]=fa[2];//记录第i个瓶子的旋转信息
			ballA[6]=fa[3];//记录第i个瓶子的旋转信息
		}
		if(sceneIndex==1){
			EYE_Z=24;
			TARGET_Z=0;
		}
	}
	@Override
	public void initView() 
	{	
		BallId=TextureManager.getTextures("ball1.png");		//初始化球的纹理id
		BottleId=TextureManager.getTextures("ping5.png");		//初始化瓶子的纹理id
		kjgId=TextureManager.getTextures("zh.png");//初始化科技馆纹理
		gdId=TextureManager.getTextures("guidao.png");//初始化科技馆场景轨道纹理
        desertId=TextureManager.getTextures("sand.png"); //初始化沙漠沙滩纹理
        tree1Id=TextureManager.getTextures("tree1.png");//初始化沙漠树纹理
        tree2Id=TextureManager.getTextures("tree3.png");//初始化沙漠树纹理
        smgdId=TextureManager.getTextures("smguidao.png");//初始化沙漠轨道纹理
        smysId=TextureManager.getTextures("yanshi.png");//初始化沙漠岩石纹理
        cloudId=TextureManager.getTextures("sky_cloud.png");//初始化沙漠云纹理
        skyId=TextureManager.getTextures("background.png");//初始化星空的背景纹理
        skygdId=TextureManager.getTextures("guidao2.png");//初始化星空轨道纹理
        feixingwu1Id=TextureManager.getTextures("feixingwu1.png");//初始化飞行物纹理
        feixingwu2Id=TextureManager.getTextures("feixingwu2.png");//初始化飞行物纹理
        textureIdEarth=TextureManager.getTextures("earth.png");//地球纹理
        textureIdEarthNight=TextureManager.getTextures("earthn.png");//地球纹理
        textureIdMoon=TextureManager.getTextures("moon.png");  //月球纹理
        xingId=TextureManager.getTextures("qiu2.png"); //星球纹理
		btmgdId=TextureManager.getTextures("btmguidao.png");//-----------------初始化科技馆的半透明轨道纹理
        btmsmgdId=TextureManager.getTextures("btmsmguidao.png");//-----------------初始化半透明沙漠轨道纹理
        btmskygdId=TextureManager.getTextures("btmguidao2.png");//-------------------------初始化半透明星空轨道纹理
		phc=new PhyCaulate();
		phc.initWorld();//初始化物理世界
		phc.createBody(viewManager, 0, planeShape, 0,FLOOR_Y,0);//创建地面
		phc.createBody(viewManager, 0, boxShape3, 0,FLOOR_Y+GUIDAO_HALF_Y,0);//台子
	    phc.createBody(viewManager, 0, capsuleShape2, 4.3f,0.5f,-20f);//轨道右边胶囊
	    phc.createBody(viewManager, 0, capsuleShape2, -4.3f,0.5f,-20f);//轨道左边胶囊
	    wall=phc.createBody(viewManager, 0, boxShape2, 0, WANG_HALF_Y-4, -66);
	    pt=new PhysicsThread(this.viewManager);//创建物理线程       
        pt.setFlag(true);//设置物理线程的标志位
        pt.start();//启动物理线程
        
		special=new SpecialUtil(this);
		special.initSpecial();
		//初始化绘制分数框对象
		al.add(new BN2DObject(540,300, 1080,600,TextureManager.getTextures("scorekuang.png"),ShaderManager.getShader(0)));
		al.add(new BN2DObject(960,250, 80,120,TextureManager.getTextures("wanjia.png"),ShaderManager.getShader(0)));
		al.add(new BN2DObject(540,80,400,180,TextureManager.getTextures("player.png"),ShaderManager.getShader(0)));
		triangle=new BN2DObject(151,1000,380,380,TextureManager.getTextures("triangle.png"),ShaderManager.getShader(0));
		showScore=new BN2DObject(540,1000,650,650,TextureManager.getTextures("showscore.png"),ShaderManager.getShader(0));
		
		al2.add(new BN2DObject(540,800,1000,1350,TextureManager.getTextures("helpback.png"),ShaderManager.getShader(0)));
		al2.add(new BN2DObject(250,400,400,200,TextureManager.getTextures("quanzhong.png"),ShaderManager.getShader(0)));
		al2.add(new BN2DObject(250,550,400,200,TextureManager.getTextures("buzhong.png"),ShaderManager.getShader(0)));
		al2.add(new BN2DObject(250,700,400,200,TextureManager.getTextures("luogouqiu.png"),ShaderManager.getShader(0)));
		al2.add(new BN2DObject(250,850,400,200,TextureManager.getTextures("zongfen.png"),ShaderManager.getShader(0)));
		al2.add(new BN2DObject(750,1200,200,200,TextureManager.getTextures("backb.png"),ShaderManager.getShader(0)));
		al2.add(new BN2DObject(350,1200,220,200,TextureManager.getTextures("houseb.png"),ShaderManager.getShader(0)));
		
		camera[0]=new BN2DObject(65, 1700, 130, 130, TextureManager.getTextures("camera.png"), 
				ShaderManager.getShader(0));//经典模式暂停按钮
		camera[1]=new BN2DObject(540, 960, 800, 600, TextureManager.getTextures("camerakuang.png"), 
				ShaderManager.getShader(0));//经典模式暂停按钮
		
		Pause=new BN2DObject(980, 1700, 130, 130, TextureManager.getTextures("pause.png"), 
				ShaderManager.getShader(0));
		pv=new PauseView(this);//暂停界面
	}
	
	public void initSound()
	{
		if(!musicOff)
		{
			if(MainActivity.sound.mp!=null){
				MainActivity.sound.mp.pause();
				MainActivity.sound.mp=null;
			}
			if(sceneIndex==1){//星空背景音乐				
				MainActivity.sound.mp =  MediaPlayer.create(this.viewManager.activity, R.raw.xk_beijing_music) ;
			}else if(sceneIndex==2){//科技馆背景音乐
				MainActivity.sound.mp =  MediaPlayer.create(this.viewManager.activity, R.raw.kjg_beijing_music) ;
			}else if(sceneIndex==3){//沙漠背景音乐
				MainActivity.sound.mp =  MediaPlayer.create(this.viewManager.activity, R.raw.desert_beijing_music) ;
			}
			MainActivity.sound.mp.setVolume(0.2f, 0.2f);//设置左右声道音量
 			MainActivity.sound.mp.setLooping(true);//循环播放
 			MainActivity.sound.mp.start();
		}
	}
	@Override
	public void drawView(GL10 gl) 
	{
		//清除颜色缓存于深度缓存
    	GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
    	//设置透视投影
    	MatrixState3D.setProjectFrustum(-LEFT, RIGHT, -TOP, BOTTOM, NEAR, FAR);
    	
    	ArrayList<GameObject>  tempBall=new ArrayList<GameObject> ();
		synchronized(lockBall)
		{
			tempBall.addAll(alGBall);
		}
    	if(tempBall.size()>0){
    		if(isMoveFlag){
    			updateBallDatas();
    		}else{
    			initFirstBall();
    		}
    	}
    	moveCameraToBack();//向后移动摄像机
    	if(isCameraMove){
    		moveCameraToFront(); //向前移动摄像机
    	}else{
    		noMoveCameraToFront();
    	}
        MatrixState3D.setCamera(EYE_X,EYE_Y,EYE_Z,TARGET_X,TARGET_Y,TARGET_Z,UP_X,UP_Y,UP_Z);  //设置摄像机参数
        MatrixState3D.setLightLocation(0, 80, 50);
        //启用深度测试
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);  
        if(sceneIndex==2)
        {
            drawScienceFictionScene();//绘制科幻场景
        }else if(sceneIndex==3)
        {
        	drawDesertScene();//绘制沙漠场景
        }else if(sceneIndex==1)
        {
        	drawSkyScene();//绘制星空
        }
   
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);  //关闭深度测试
        drawGameMirror();        //绘制镜像体
     
        //绘制半透明地板
        if(sceneIndex==2)
        {
        	drawBTMScienceFictionGD();//绘制科幻轨道
        }else if(sceneIndex==3)
        {
        	drawBTMDesertGD();//绘制沙漠轨道
        }else if(sceneIndex==1)
        {
        	drawBTMSkyGD();//绘制星空轨道
        }
        
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);          //启用深度测试
        drawGame();
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);//关闭深度测试
        
        draw2DBN2DObejct();
        
        if(isDrawFireOver&&CalculateScore.GameOver)
        {
        	drawGameOver();
        }
        
        MatrixState3D.pushMatrix();
        special.drawSpecial();
        MatrixState3D.popMatrix();
        if((sceneIndex==1&&!isMoveGuiDaoFlag&&initObect)||(sceneIndex!=1&&!this.viewManager.isMoveCameraBack&&initObect))
        {
        	if(!effictOff){
        		MainActivity.sound.playMusic(Constant.INIT_BOTTLE, 0);//初始化瓶子音效
        	}
        	initObect=false;
        	phc.initAllObject(viewManager);
        }
        if(isPause)
		{//绘制暂停菜单
			pv.drawView();
		}
        if(isDrawAN)
        {//绘制暂停按钮
     		MatrixState2D.pushMatrix();//保护现场 
    		Pause.drawSelf();//暂停按钮
    		MatrixState2D.popMatrix();//恢复现场
        }
    	MatrixState2D.pushMatrix();//保护现场 
		camera[0].drawSelf();//绘制录像按钮
		if(drawCameraTip)//绘制是否上传录像提示框...
		{
			camera[1].drawSelf();
		}
		MatrixState2D.popMatrix();
	}	
	
	public void drawGameOver()//绘制游戏结束对象
	{
		inGameOverView=true;//处于游戏结束界面
		pt.getNumberBN2D.getStrikeAndSpareCount();
		for(BN2DObject bn:al2)
		{
			bn.drawSelf();
		}
		if(pt.getNumberBN2D.strike[0]!=null)//绘制全中数量对象
		{
			pt.getNumberBN2D.strike[0].drawSelf(630,390);
			pt.getNumberBN2D.strike[1].drawSelf(690,390);
		}else
		{
			if(pt.getNumberBN2D.strike[1]!=null)
			{
				pt.getNumberBN2D.strike[1].drawSelf(660,390);
			}
		}
		
		if(pt.getNumberBN2D.spare[0]!=null)//绘制补中数量对象
		{
			pt.getNumberBN2D.spare[0].drawSelf(630,540);
			pt.getNumberBN2D.spare[1].drawSelf(690,540);
		}else
		{
			if(pt.getNumberBN2D.spare[1]!=null)
			{
				pt.getNumberBN2D.spare[1].drawSelf(660,540);
			}
		}
		
		if(pt.getNumberBN2D.luoGou[0]!=null)//绘制落沟球数量对象
		{
			pt.getNumberBN2D.luoGou[0].drawSelf(630,690);
			pt.getNumberBN2D.luoGou[1].drawSelf(690,690);
		}else
		{
			if(pt.getNumberBN2D.luoGou[1]!=null)
			{
				pt.getNumberBN2D.luoGou[1].drawSelf(660,690);
			}
		}
		
		if(pt.getNumberBN2D.allPointsBN2D[0]!=null)//如果总分大于100分
		{
			pt.getNumberBN2D.allPointsBN2D[0].drawSelf(600, 840);
			pt.getNumberBN2D.allPointsBN2D[1].drawSelf(660, 840);
			pt.getNumberBN2D.allPointsBN2D[2].drawSelf(720, 840);
		}else//如果总分小于100
		{
			if(pt.getNumberBN2D.allPointsBN2D[1]!=null)//如果总分大于10
			{
				pt.getNumberBN2D.allPointsBN2D[1].drawSelf(630, 840);
				pt.getNumberBN2D.allPointsBN2D[2].drawSelf(690, 840);
			}else//如果总分小于10
			{
				if(pt.getNumberBN2D.allPointsBN2D[2]!=null)
				{
					pt.getNumberBN2D.allPointsBN2D[2].drawSelf(660, 840);
				}
			}
		}
	}
	
	int distance2=0;
	public void draw2DBN2DObejct()//绘制2D对象
	{
		//绘制分数框=======================start=================
		if(isDrawScore)//绘制分数框
		{
			MatrixState2D.pushMatrix();//保护场景
			for(int i=0;i<al.size();i++)//显示分数的内容缓缓落下 缓缓上升
			{
				switch(i)
				{
				case 0:
					al.get(i).drawSelf(540,-300+12f*distance);//绘制分数框
					break;
				case 1:
					al.get(i).drawSelf(960,-250+10*distance);//绘制玩家
					break;
				case 2:
					al.get(i).drawSelf(540,-80+3.2f*distance);//绘制player
					break;
				}
			}
			for(int i=0;i<10;i++)//绘制第几轮
			{
				switch(i)
				{
				case 0:case 1:case 2:case 3:case 4:
					pt.getNumberBN2D.everyRound.get(i).drawSelf(100+165*i,-180+7.2f*distance);                                 
					break;
				case 5:case 6:case 7:case 8:case 9:
					pt.getNumberBN2D.everyRound.get(i).drawSelf(100+165*(i-5),-400+16f*distance);
					break;
				}
			}
			MatrixState2D.popMatrix();//保护场景
			
			for(int i=0;i<pt.getNumberBN2D.currBlueNumber.size();i++)
			{
				if(i<5)//如果是需要显示在第一行的分数
				{
					BN2DObject[] numbers=pt.getNumberBN2D.currBlueNumber.get(i);
					if(numbers[0]!=null)//如果分数大于100分
					{
						MatrixState2D.pushMatrix();//保护场景
						numbers[0].drawSelf(60+165*i, -335+13.4f*distance);//绘制三个数字
						numbers[1].drawSelf(100+165*i, -335+13.4f*distance);
						numbers[2].drawSelf(140+165*i, -335+13.4f*distance);
						MatrixState2D.popMatrix();//恢复场景
					}else//如果分数小于100
					{
						if(numbers[1]!=null)//如果分数大于10
						{
							MatrixState2D.pushMatrix();//保护场景
							numbers[1].drawSelf(80+165*i, -335+13.4f*distance);//绘制两个数字
							numbers[2].drawSelf(120+165*i, -335+13.4f*distance);
							MatrixState2D.popMatrix();//恢复场景
						}else//分数小于10
						{
							MatrixState2D.pushMatrix();//保护场景
							numbers[2].drawSelf(100+165*i, -335+13.4f*distance);//只需要绘制一个数字
							MatrixState2D.popMatrix();//恢复场景
						}
					}
				}else if(i>=5&&i<=9)//如果是需要显示在第二行的分数
				{
					BN2DObject[] numbers=pt.getNumberBN2D.currBlueNumber.get(i);
					if(numbers[0]!=null)//如果分数大于100分
					{
						MatrixState2D.pushMatrix();//保护场景
						numbers[0].drawSelf(60+165*(i-5), -560+22.4f*distance);//绘制三个数字
						numbers[1].drawSelf(100+165*(i-5), -560+22.4f*distance);
						numbers[2].drawSelf(140+165*(i-5), -560+22.4f*distance);
						MatrixState2D.popMatrix();//恢复场景
					}else//如果分数小于100
					{
						if(numbers[1]!=null)//如果分数大于10
						{
							MatrixState2D.pushMatrix();//保护场景
							numbers[1].drawSelf(80+165*(i-5), -560+22.4f*distance);//绘制两个数字
							numbers[2].drawSelf(120+165*(i-5), -560+22.4f*distance);
							MatrixState2D.popMatrix();//恢复场景
						}else
						{
							MatrixState2D.pushMatrix();//保护场景
							numbers[2].drawSelf(100+165*(i-5), -560+22.4f*distance);//只需要绘制一个数字
							MatrixState2D.popMatrix();//恢复场景
						}
					}
				}
			}
			if(pt.getNumberBN2D.totalPointsBN2D[0]!=null)//如果总分大于100分
			{
				MatrixState2D.pushMatrix();//保护场景
				pt.getNumberBN2D.totalPointsBN2D[0].drawSelf(910, -430+17.2f*distance);
				pt.getNumberBN2D.totalPointsBN2D[1].drawSelf(970, -430+17.2f*distance);
				pt.getNumberBN2D.totalPointsBN2D[2].drawSelf(1030, -430+17.2f*distance);
				MatrixState2D.popMatrix();//恢复场景
			}else//如果总分小于100
			{
				if(pt.getNumberBN2D.totalPointsBN2D[1]!=null)//如果总分大于10
				{
					MatrixState2D.pushMatrix();//保护场景
					pt.getNumberBN2D.totalPointsBN2D[1].drawSelf(940, -430+17.2f*distance);
					pt.getNumberBN2D.totalPointsBN2D[2].drawSelf(1000, -430+17.2f*distance);
					MatrixState2D.popMatrix();//恢复场景
				}else//如果总分小于10
				{
					if(pt.getNumberBN2D.totalPointsBN2D[2]!=null)
					{
						MatrixState2D.pushMatrix();//保护场景
						pt.getNumberBN2D.totalPointsBN2D[2].drawSelf(970, -430+17.2f*distance);
						MatrixState2D.popMatrix();//恢复场景
					}
				}
			}
			
			for(int i=0;i<pt.getNumberBN2D.everyTimeScore.size();i++)//遍历每次的分数
			{
				if(i!=0&&i%2==0&&i!=20)
				{
					drawPositionIndex++;
				}
				BN2DObject everyScore=pt.getNumberBN2D.everyTimeScore.get(i);//获取当前的绘制对象
				if(i<10)//显示在第一行的分数
				{
					if(everyScore!=null)
					{
						if(i%2==0)//如果是每轮的第一个分数
						{
							everyScore.drawSelf(60+165*drawPositionIndex,-260+10.4f*distance);
						}else//如果是每轮的第二个分数
						{
							everyScore.drawSelf(140+165*drawPositionIndex,-260+10.4f*distance);
						}
					}
				}else if(i>=10&&i<18)//显示第二行前九轮的分数
				{
					if(everyScore!=null)
					{
						if(i%2==0)//如果是每轮的第一个分数
						{
							everyScore.drawSelf(60+165*(drawPositionIndex-5),-480+19.2f*distance);
						}else//如果是每轮的第二个分数
						{
							everyScore.drawSelf(140+165*(drawPositionIndex-5),-480+19.2f*distance);
						}
					}
				}else//显示第十轮比赛的分数
				{
					if(everyScore!=null)
					{
						if(i==18)//如果是每轮的第一个分数
						{
							everyScore.drawSelf(40+165*(drawPositionIndex-5),-480+19.2f*distance);
						}else if(i==19)//如果是每轮的第二个分数
						{
							everyScore.drawSelf(100+165*(drawPositionIndex-5),-480+19.2f*distance);
						}else if(i==20)//如果是每轮的第三个分数
						{
							everyScore.drawSelf(160+165*(drawPositionIndex-5),-480+19.2f*distance);
						}
					}
				}
			}
			
			drawPositionIndex=0;
			
			if(isDrawCurrentScore)
			{
				if(pt.getNumberBN2D.showBN2D!=null)
				{
					if(pt.getNumberBN2D.isShow)//显示当前分数
					{
						MatrixState2D.pushMatrix();
						MatrixState2D.scale(0.02f*distance, 0.02f*distance, 1);
						showScore.drawSelf(570, 1005);
						MatrixState2D.popMatrix();
					}
					MatrixState2D.pushMatrix();
					MatrixState2D.scale(0.02f*distance, 0.02f*distance, 1);
					pt.getNumberBN2D.showBN2D.drawSelf(580,1010);
					MatrixState2D.popMatrix();
				}
				distance2++;
				if(distance2>=100)//不再绘制
				{
					distance2=0;
					isDrawCurrentScore=false;
					if(updateRound)//如果重新开始
					{
						firstStartGame=true;
						updateRound=false;
					}
					if(CalculateScore.GameOver)
					{
						isDrawFireWorks=true;
						isDrawScore=false;
					}
				}
			}
			if(isScoreDown&&distance<50)//缓缓下降
			{
				distance++;
			}
			if(!isScoreDown&&distance>0)//缓缓上升
			{
				distance--;
			}
			//绘制分数框=======================end=================
			
			//绘制现有保龄球的索引值============start================
			if(isScoreDown)//如果显示分数框 则显示保龄球索引值
			{
				triangle.drawSelf();//绘制倒三角
				if(firstStartGame)//如果是第一次开始游戏 则默认绘制全部索引值
				{
					int[] ballIndex=new int[10];
					for(int i=0;i<ballIndex.length;i++)
					{
						ballIndex[i]=i+1;
					}
					ballIndex=pt.getTriangleNumber.getDrawBallIndex(ballIndex);
					int zz=0;
					for(int i=0;i<4;i++)
					{
						for(int j=0;j<=i;j++)
						{
							if(ballIndex[zz]!=0)
							{
								pt.getTriangleNumber.drawBallIndex.get(zz).drawSelf(150-35*i+70*j, 1100-67*i);
							}
							zz++;
						}
					}
				}else//如果不是，则根据球倒的情况绘制
				{
					int kk=0;
					for(int i=0;i<4;i++)
					{
						for(int j=0;j<=i;j++)
						{
							if(pt.ballIndex[kk]!=0)
							{
								//绘制没有倒的球的索引值
								pt.getTriangleNumber.drawBallIndex.get(kk).drawSelf(150-35*i+70*j, 1100-67*i);
							}
							kk++;
						}
					}
				}
			}
			if(!isScoreDown&&distance==0)//摄像机定住，不再绘制分数等
			{
				distance=0;
				isDrawScore=false;
				pt.getNumberBN2D.isShow=true;
			}
		}else
		{
			if(pt.restartTriangle)//重新开始绘制保龄索引值
			{
				pt.getTriangleNumber.restart();
				pt.restartTriangle=false;
			}
		}
		//绘制现有保龄球的索引值============end================
	}
	
	public void reSetData()
	{
		curr=null;//当前触控的对象引用	
		ballA=null;//缓存球数据
		isMoveFlag=false;//球是否移动
		isDrawAN=true;
		isMoveGuiDaoFlag=true;//是否移动轨道的标识
		initObect=true;
		effictOff=false;
		musicOff=false;
		viewManager.isMoveCameraBack=true;
		viewManager.isMoveCameraFront=false;
		ballCount=0;//滑球次数
		count=0;//计数器
		drawScoreCount=0;
		drawPositionIndex=0;//绘制分数的位置
	    skyAngle=0;//天空旋转角度
	    guidao_z=50;//轨道z坐标
	    feixingwu1_z=50;//飞行物1
		preX=0;
		preY=0;
		ez=0;
		tz=-24;//临时记录摄像机的两个参数	
		countTurn=1;
    	isCastBall=false;
    	luoGouCount=0;
		status=-1;
		EYE_Z=0;
		TARGET_Z=-24;
		touchX=0;
		touchY=0;
		
		isGamePlay=true;
		isDrawFire=false;
		isCrashWall=false;
		isDrawScore=true;
		isScoreDown=true;
		firstStartGame=true;
		
		inGameOverView=false;
		isDrawCurrentScore=false;
		isPause=false;
		isInPause=false;
		
		distance=0;
		drawPositionIndex=0;
		CalculateScore.restart();
		pt.getNumberBN2D.restart();
		pt.getTriangleNumber.restart();	
	}
}
