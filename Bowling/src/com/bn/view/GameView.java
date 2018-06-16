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
	public PhysicsThread pt;//�����߳�
	public MySurfaceView viewManager;
	public PhyCaulate phc;//�����װ��
	SpecialUtil special;
	GameObject curr=null;//��ǰ���صĶ�������
	GameObject tempBall=null;
	float[][] resultA=null;//����ƿ������
	public static float[] ballA=null;//����������
	BN2DObject triangle;//�����ǻ��ƶ���
	BN2DObject showScore;//��ʾ�������ƶ���
	Map<Integer,GameObject>  hmTemp1=new HashMap<Integer,GameObject>();
	List<BN2DObject> al=new ArrayList<BN2DObject>();//���BNObject����
	List<BN2DObject> al2=new ArrayList<BN2DObject>();//���BNObject����
	public BN2DObject Pause;//��ͣ��ť
	public PauseView pv;//��ͣ����
	private boolean initObect=true;
	public boolean isInPause=false;//�Ƿ�������ͣ������

	int count=0;//������
	int drawScoreCount=0;	
	int drawPositionIndex=0;//���Ʒ�����λ��
    float skyAngle=0;//�����ת�Ƕ�
    float guidao_z=50;//���z����
    float feixingwu1_z=50;//������1
	float preX=0,preY=0;
	float ez=0;
	float tz=-24;//��ʱ��¼���������������	
	public int pausecount=0;
	public int distance=0;//���Ʒ����������� �½��ı���	
	int drawFireIndex=0;
	float a=0.6f;
	float saveA=0;
	boolean startRecorder=false;//��ʼ¼�ơ�����¼��
	boolean isRecorder=false;//�Ƿ�֧��¼��
	BN2DObject[] camera=new BN2DObject[2];//¼��ͼ����ƶ���
	float touchX,touchY;
	
	public GameView(MySurfaceView viewManager)
	{
		this.viewManager=viewManager;
		initView();
	}
	@Override
	public boolean onTouchEvent(MotionEvent e)
	{
		float x=Constant.fromRealScreenXToStandardScreenX(e.getX());//��ȡ���ص������
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
        case MotionEvent.ACTION_DOWN://������Ļ�����µ��¼�
        	curr=null;
        	if(!isMoveFlag&&x>=Pause_left&&x<=Pause_right&&y>=Pause_top&&y<=Pause_bottom)
        	{//�����ͣ��ť
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
        	
        	//��Ϸ��������==========start================
        	if(inGameOverView&&x>=270&&x<=420&&y>=1100&&y<=1300)//�ص�ѡ�񳡾�����
        	{
        		inGameOverView=false;
        		isDrawFireOver=false;//���ٻ�������ϵͳ
    			isDrawFireWorks=false;
    			phc.initAllObject(viewManager);
    			reSetData();
        		viewManager.currView=viewManager.optionView;//�ص�ѡ�񳡾�����
        	}else if(inGameOverView&&x>=650&&x<=800&&y>=1100&&y<=1300)//ˢ����Ϸ����
        	{
        		inGameOverView=false;
        		isDrawFireOver=false;//���ٻ�������ϵͳ
    			isDrawFireWorks=false;
        		viewManager.currView=viewManager.gameView;
        		phc.initAllObject(viewManager);
    			reSetData();
        	}
        	//��Ϸ��������==========end================
        	
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
        	
        	//�������任��AB�����λ��
			float[] AB=IntersectantUtil.calculateABPosition
			(
				x, //���ص�X����
				y, //���ص�Y����
				SCREEN_WIDTH_STANDARD, //��Ļ���
				SCREEN_HEIGHT_STANDARD, //��Ļ����
				LEFT, //�ӽ�left��topֵ
				TOP,
				NEAR, //�ӽ�near��farֵ
				FAR
			);
			//����AB
			Vector3f3D start = new Vector3f3D(AB[0], AB[1], AB[2]);//���
			Vector3f3D end = new Vector3f3D(AB[3], AB[4], AB[5]);//�յ�
			Vector3f3D dir = end.minus(start);//���Ⱥͷ���
			/*
			 * ����AB�߶���ÿ�������Χ�е���ѽ���(��A������Ľ���)��
			 * ����¼����ѽ�����������б��е�����ֵ
			 */
			//��¼�б���ʱ����С������ֵ
    		float minTime=1;//��¼�б�������������AB�ཻ�����ʱ��
    		AABB3 box = tempBall.get(0).lovnt.getCurrBox(); //�������AABB��Χ��   
			float t = box.rayIntersect(start, dir, null);//�����ཻʱ��
			if (t <= minTime) {
				minTime = t;//��¼��Сֵ
				curr=tempBall.get(0);
				touchX=x;
				touchY=y;
			}
			
			//¼����Ƶ������������===========start==============
        	
        	if(x>=CAMERA_START_LEFT&&x<=CAMERA_START_RIGHT
        			&&y>=CAMERA_START_TOP&&y<=CAMERA_START_BOTTOM)//��������� ��ʼ���߽���¼��
        	{
        		startRecorder=!startRecorder;
        		isRecorder=viewManager.recorder.isAvailable();//�Ƿ�֧��¼����Ƶ
        		if(isRecorder)//���֧��¼��
        		{
        			if(startRecorder)//��ʼ¼��
            		{
        				viewManager.handler.sendEmptyMessage(0);
        				viewManager.recorder.startRecorder();
            		}else//����¼��
            		{
            			viewManager.handler.sendEmptyMessage(1);
            			viewManager.recorder.stopRecorder();
            			drawCameraTip=true;
            		}
        		}
        	}
        	if(drawCameraTip&&x>=CAMERA_TIP_NO_LEFT&&x<=CAMERA_TIP_NO_RIGHT&&
        			y>=CAMERA_TIP_NO_TOP&&y<=CAMERA_TIP_NO_BOTTOM)//���ϴ�¼��
        	{
        		drawCameraTip=false;//���ٻ��� ��������Ƶ
        	}else if(drawCameraTip&&x>=CAMERA_TIP_YES_LEFT&&x<=CAMERA_TIP_YES_RIGHT&&
        			y>=CAMERA_TIP_YES_TOP&&y<=CAMERA_TIP_YES_BOTTOM)//�ϴ�¼��
        	{
        		ShangChuanView=true;
        		drawCameraTip=false;//���ٻ��� ��������Ƶ
        		viewManager.recorder.showShare();
        	}
        	
        	//¼����Ƶ===========end==============			
   			break;
        case MotionEvent.ACTION_MOVE://��������Ļ���¼�
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
        		if(Math.abs(vx)<0.3f){//�����ٶ���ֵ
        			vx=0.0f;
        		}
        		vx=(vx>2.56f)?2.56f:vx;
        		vx=(vx<(-2.56f))?(-2.56f):vx;
        		
        		vy=(vy>(-15))?(-15):vy;
        		vy=(vy<(-35))?(-35):vy;
        		//���ðٷֱ�
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
    			status=0;//�ı�״ֵ̬status
    			isDrawFire=true;
        		isMoveFlag=true;//�ƶ�
        		isDrawAN=false;
        		isCastBall=true;//Ͷ��
    			ballCount++;//ÿ�ֵĵڼ�����
    			synchronized(lockV)
    			{
    				qf.offer(new float[]{vx,0,vy});
    				tempBall.get(0).body.activate();//�������
    			}
            	if(!effictOff){
            		MainActivity.sound.playMusic(Constant.BALL_ROLL, 0);//�����Ч
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
	public void moveCameraToBack()//����ƶ������
	{
		if(sceneIndex!=1)
		{//���������ƶ�
			if(viewManager.isMoveCameraBack)
			{
				count++;
				if(EYE_Z<CAMERA_LIMIT_MAX){//����ƶ�
					if(count%2==0){
						EYE_Z+=CAMERA_SPAN;
						TARGET_Z+=CAMERA_SPAN;
					}		
				}
				else{//���ƶ���־λ��Ϊfalse
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
	public void moveCameraToFront()//��ǰ�ƶ������
	{
		if(viewManager.isMoveCameraFront){//�������ǰ�ƶ�
			if(EYE_Z>(CAMERA_LIMIT1_MIN-CAMERA_LIMIT_SPAN)
					&&ez>(CAMERA_LIMIT1_MIN-CAMERA_LIMIT_SPAN)){
				EYE_Z=ez;
				TARGET_Z=tz;
			}
			else if(EYE_Z<(CAMERA_LIMIT1_MIN+CAMERA_LIMIT_SPAN)
					&&EYE_Z>(CAMERA_LIMIT2_MIN))
			{//���ٹ���
				if((a-CAMERA_A_SPAN)>0)
				{
					a-=CAMERA_A_SPAN;
					EYE_Z-=a;
					TARGET_Z-=a;
				}				
				isScoreDown=false;//�����������ϻ�
			}
			else
			{
				a=saveA;
				isDrawFire=false;
				viewManager.isMoveCameraFront=false;//ǰ�Ʊ�־λ��Ϊfalse
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
				isScoreDown=false;//�����������ϻ�
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
    		MatrixState3D.pushMatrix();//�����ֳ�
    		MatrixState3D.translate(ballA[0],ballA[1],ballA[2]);//����ƽ�Ʊ任
			MatrixState3D.rotate(ballA[3],ballA[4],ballA[5],ballA[6]);
			tempBall.get(0).drawSelfBall(0,0);//������
    		MatrixState3D.popMatrix();//�ָ��ֳ�
		}
		if(tempBall.get(0)!=null&&tempBall.get(0).body!=null){
            Vector3f v=tempBall.get(0).body.getLinearVelocity(new Vector3f());
            if(v.z!=0)
            {//������z�����ٶȲ�Ϊ0������ǰ�ƶ������
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
    	updateBottlesDatas();//��ȡ����ƿ�ӵ���������
    	if(resultA==null){
    		return;
    	}
    	for(int i=0;i<resultA.length;i++)
    	{//����ƿ��
			if(resultA[i]!=null&&resultA[i].length>0&&hmTemp1.get(i+1)!=null)
			{
    			MatrixState3D.pushMatrix();//�����ֳ�
    			MatrixState3D.translate(resultA[i][0],resultA[i][1],resultA[i][2]);//����ƽ�Ʊ任
    			MatrixState3D.rotate(resultA[i][3],resultA[i][4],resultA[i][5],resultA[i][6]);
    			hmTemp1.get(i+1).drawSelf(0,0);//����ƿ��
    			MatrixState3D.popMatrix();//�ָ��ֳ�
			}
    	}
	}
	
	public void drawGameMirror()//���ƾ�����
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
    	        GLES20.glFrontFace(GLES20.GL_CW);//GL_CW˵��˳ʱ������Ϊ����
    	        MatrixState3D.pushMatrix();//�����ֳ�
        		MatrixState3D.translate(ballA[0],-ballA[1],ballA[2]);//����ƽ�Ʊ任
    			MatrixState3D.scale(1, -1, 1);
    			MatrixState3D.rotate(ballA[3],ballA[4],ballA[5],ballA[6]);
    			tempBall.get(0).drawSelfBall(0,0);
        		MatrixState3D.popMatrix();//�ָ��ֳ�    	        
        		GLES20.glFrontFace(GLES20.GL_CCW);//GL_CCW˵����ʱ������Ϊ���棬Ĭ�ϵ�����ʱ��Ϊ����
    	        
        		MatrixState3D.pushMatrix();//�����ֳ�
        		MatrixState3D.translate(ballA[0],ballA[1],ballA[2]);//����ƽ�Ʊ任
    			MatrixState3D.rotate(ballA[3],ballA[4],ballA[5],ballA[6]);
    			tempBall.get(0).drawSelfBall(1,0);
        		MatrixState3D.popMatrix();//�ָ��ֳ�
			}
		}
    	synchronized(lockAll)
    	{
    		hmTemp1.clear();
    		hmTemp1.putAll(hm);
    	}
    	updateBottlesDatas();//��ȡ����ƿ�ӵ���������
    	if(resultA==null){
    		return;
    	}    	
    	for(int i=0;i<resultA.length;i++)
    	{//����ƿ��
			if(resultA[i]!=null&&resultA[i].length>0&&hmTemp1.get(i+1)!=null)
			{
				if(resultA[i][2]>-54f&&Math.abs(resultA[i][0])<5)
    			{
        	        GLES20.glFrontFace(GLES20.GL_CW);
        			MatrixState3D.pushMatrix();//�����ֳ�
        			MatrixState3D.translate(resultA[i][0],-resultA[i][1],resultA[i][2]);//����ƽ�Ʊ任        		
        			MatrixState3D.scale(1, -1, 1);
        			MatrixState3D.rotate(resultA[i][3],resultA[i][4],resultA[i][5],resultA[i][6]);	
        			hmTemp1.get(i+1).drawSelf(0,0);//����ƿ��
        			MatrixState3D.popMatrix();//�ָ��ֳ�
              		 GLES20.glFrontFace(GLES20.GL_CCW);//��ʱ��
        	        
        			MatrixState3D.pushMatrix();//�����ֳ�
        			MatrixState3D.translate(resultA[i][0],resultA[i][1],resultA[i][2]);//����ƽ�Ʊ任
        			MatrixState3D.rotate(resultA[i][3],resultA[i][4],resultA[i][5],resultA[i][6]);
        			hmTemp1.get(i+1).drawSelf(1,0);//����ƿ��Ӱ��
        			MatrixState3D.popMatrix();//�ָ��ֳ�
    			}
			}
    	}    	
	}
	
	public void drawScienceFictionScene()//���ƿƻó���
	{
		MatrixState3D.pushMatrix();    //�����ֳ�        
		MatrixState3D.translate(-6,-0.9f, -10f);  //ƽ��
        if(kjg_model!=null)
        {
            kjg_model.drawSelf(kjgId,0,0);//���Ƴ���     	
        }   
        MatrixState3D.popMatrix();    //�ָ��ֳ�
		
        MatrixState3D.pushMatrix();//�����ֳ�        
        MatrixState3D.translate(0, -0.5f, -19.5f);
        //�����ص����岿λ�����������
        if(gd_model!=null)
        {
        	gd_model.drawSelf(gdId,0,0);
        }   
        MatrixState3D.popMatrix();  //�ָ��ֳ�
	}
	
	public void drawBTMScienceFictionGD()//���ƿƻð�͸�����
	{
        MatrixState3D.pushMatrix();//�����ֳ�        
        MatrixState3D.translate(0, -0.5f, -19.5f);
        //�����ص����岿λ�����������
        if(gd_model!=null)
        {
        	gd_model.drawSelf(btmgdId,0,0);
        }   
        MatrixState3D.popMatrix();  //�ָ��ֳ�
	}

	public void drawDesertScene()//����ɳĮ����
	{
		 MatrixState3D.pushMatrix();
		 MatrixState3D.translate(0,-7f, -80f);   //ch.obj
         //�����ص����岿λ�����������
         if(dimian_model!=null)
         {//����
        	 dimian_model.drawSelf(desertId,0,0);
         }   
         MatrixState3D.popMatrix();    
        
        MatrixState3D.pushMatrix();        
        MatrixState3D.translate(0f,8f, -95f);   //ch.obj
        MatrixState3D.rotate(20, 0, 1, 0);
        //�����ص����岿λ�����������
        if(smys_model!=null)
        {//ɳĮ��ʯ
        	smys_model.drawSelf(smysId,0,0);
        }   
        MatrixState3D.popMatrix();
        
        MatrixState3D.pushMatrix();
        MatrixState3D.translate(0, -0.5f, -19.5f);
        MatrixState3D.scale(0.7f, 1, 1);
        //�����ص����岿λ�����������
        if(smgd_model!=null)
        {//ɳĮ���
        	smgd_model.drawSelf(smgdId,0,0);
        }   
        MatrixState3D.popMatrix(); 
        
        MatrixState3D.pushMatrix();
        MatrixState3D.translate(-9f, 0f, -40f); 
        //�����ص����岿λ�����������
        if(tree2_model!=null)
        {
        	tree2_model.drawSelf(tree1Id,0,0);
        }   
        MatrixState3D.popMatrix(); 
        
        MatrixState3D.pushMatrix();
        MatrixState3D.translate(-18f, 0f, -84f);   
        //�����ص����岿λ�����������
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
	
	public void drawBTMDesertGD()//���ư�͸��ɳĮ���
	{
		 MatrixState3D.pushMatrix();
        MatrixState3D.translate(0, -0.5f, -19.5f);
        MatrixState3D.scale(0.7f, 1, 1);
        //�����ص����岿λ�����������
        if(smgd_model!=null)
        {//ɳĮ���
        	smgd_model.drawSelf(btmsmgdId,0,0);
        }   
        MatrixState3D.popMatrix(); 
	}
	
	public void drawSkyScene()//�����ǿճ���
	{
		//����ϵ��Զ
		MatrixState3D.pushMatrix();
		MatrixState3D.translate(-1.5f, -8f,-78f);   //ch.obj
		MatrixState3D.rotate(-110, 0, 0, 1);
        
        //�����ص����岿λ�����������
        if(sky_model!=null)
        {
        	sky_model.drawSelf(skyId,0,0);
        }   
        MatrixState3D.popMatrix();     
        
        if(guidao_z>-20){
        	guidao_z--;
        }else{
        	isMoveGuiDaoFlag=false;//���ƶ������ʶ
        }
        MatrixState3D.pushMatrix();
        MatrixState3D.translate(-0.15f,-0.5f,guidao_z); 
		MatrixState3D.scale(0.85f, 1, 1);
        //�����ص����岿λ�����������
        if(skygd_model2!=null)
        {//���Ƴ����
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
        //�����ص����岿λ�����������
        if(feixingwu1_model!=null)
        {//���Ʒ�����1
        	feixingwu1_model.drawSelf(feixingwu1Id,0,0);
        }   
        MatrixState3D.popMatrix();   
        
        MatrixState3D.pushMatrix();
        MatrixState3D.translate(skyX,skyY,-70); 
        MatrixState3D.rotate(60, 0,1,0);
        MatrixState3D.rotate(30,1,0,0);
        if(feixingwu2_model!=null)
        {//������2
        	feixingwu2_model.drawSelf(feixingwu1Id,0,0);
        }   
        MatrixState3D.popMatrix();
        
        MatrixState3D.pushMatrix();
        MatrixState3D.translate(-skyX,-skyY-3,-50f); 
        MatrixState3D.rotate(-60, 0,1,0);
        if(feixingwu3_model!=null)
        {//������3
        	feixingwu3_model.drawSelf(feixingwu2Id,0,0);
        }   
        MatrixState3D.popMatrix();
        
        MatrixState3D.pushMatrix();
        MatrixState3D.translate(-3f, 5, -10);  
        //������ת
        MatrixState3D.rotate(eAngle, 0, 1, 0);
    	//��������Բ��
        earth.drawSelf(textureIdEarth,textureIdEarthNight);     
        //������ϵ������λ��            
        MatrixState3D.translate(1.5f, 0, 0);  
        //������ת     
        MatrixState3D.rotate(eAngle, 0, 1, 0);
        //��������
        moon.drawSelf(textureIdMoon);
        //�ָ��ֳ�
        MatrixState3D.popMatrix();
        
        
        MatrixState3D.pushMatrix();
        MatrixState3D.translate(12f, 10, -65);  
        //������ת
        MatrixState3D.rotate(eAngle, 0, 1, 0);
    	//��������Բ��
        xing1.drawSelf(xingId);
        //�ָ��ֳ�
        MatrixState3D.popMatrix();
	}

	public void drawBTMSkyGD()//���ư�͸���ǿչ��
	{
        if(guidao_z>-20){
        	guidao_z--;
        }else{
        	isMoveGuiDaoFlag=false;//���ƶ������ʶ
        }
        MatrixState3D.pushMatrix();
        MatrixState3D.translate(-0.15f,-0.5f,guidao_z); 
		MatrixState3D.scale(0.85f, 1, 1);
        //�����ص����岿λ�����������
        if(skygd_model2!=null)
        {//���Ƴ����
        	skygd_model2.drawSelf(btmskygdId,0,0);
        }   
        MatrixState3D.popMatrix();     
	}
	
	public void updateBottlesDatas()//��ȡƿ�ӵ���������
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
	
	public void updateBallDatas()//��ȡ�����������
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
		Quat4f ro=trans.getRotation(new Quat4f());//��ȡ��ǰ�任����ת��Ϣ
		if(ro.x!=0||ro.y!=0||ro.z!=0)
		{
			float[] fa=SYSUtil.fromSYStoAXYZ(ro);//����Ԫ��ת����AXYZ����ʽ
			ballA[3]=fa[0];//��¼��i��ƿ�ӵ���ת��Ϣ
			ballA[4]=fa[1];//��¼��i��ƿ�ӵ���ת��Ϣ
			ballA[5]=fa[2];//��¼��i��ƿ�ӵ���ת��Ϣ
			ballA[6]=fa[3];//��¼��i��ƿ�ӵ���ת��Ϣ
		}
		if(sceneIndex==1){
			EYE_Z=24;
			TARGET_Z=0;
		}
	}
	@Override
	public void initView() 
	{	
		BallId=TextureManager.getTextures("ball1.png");		//��ʼ���������id
		BottleId=TextureManager.getTextures("ping5.png");		//��ʼ��ƿ�ӵ�����id
		kjgId=TextureManager.getTextures("zh.png");//��ʼ���Ƽ�������
		gdId=TextureManager.getTextures("guidao.png");//��ʼ���Ƽ��ݳ����������
        desertId=TextureManager.getTextures("sand.png"); //��ʼ��ɳĮɳ̲����
        tree1Id=TextureManager.getTextures("tree1.png");//��ʼ��ɳĮ������
        tree2Id=TextureManager.getTextures("tree3.png");//��ʼ��ɳĮ������
        smgdId=TextureManager.getTextures("smguidao.png");//��ʼ��ɳĮ�������
        smysId=TextureManager.getTextures("yanshi.png");//��ʼ��ɳĮ��ʯ����
        cloudId=TextureManager.getTextures("sky_cloud.png");//��ʼ��ɳĮ������
        skyId=TextureManager.getTextures("background.png");//��ʼ���ǿյı�������
        skygdId=TextureManager.getTextures("guidao2.png");//��ʼ���ǿչ������
        feixingwu1Id=TextureManager.getTextures("feixingwu1.png");//��ʼ������������
        feixingwu2Id=TextureManager.getTextures("feixingwu2.png");//��ʼ������������
        textureIdEarth=TextureManager.getTextures("earth.png");//��������
        textureIdEarthNight=TextureManager.getTextures("earthn.png");//��������
        textureIdMoon=TextureManager.getTextures("moon.png");  //��������
        xingId=TextureManager.getTextures("qiu2.png"); //��������
		btmgdId=TextureManager.getTextures("btmguidao.png");//-----------------��ʼ���Ƽ��ݵİ�͸���������
        btmsmgdId=TextureManager.getTextures("btmsmguidao.png");//-----------------��ʼ����͸��ɳĮ�������
        btmskygdId=TextureManager.getTextures("btmguidao2.png");//-------------------------��ʼ����͸���ǿչ������
		phc=new PhyCaulate();
		phc.initWorld();//��ʼ����������
		phc.createBody(viewManager, 0, planeShape, 0,FLOOR_Y,0);//��������
		phc.createBody(viewManager, 0, boxShape3, 0,FLOOR_Y+GUIDAO_HALF_Y,0);//̨��
	    phc.createBody(viewManager, 0, capsuleShape2, 4.3f,0.5f,-20f);//����ұ߽���
	    phc.createBody(viewManager, 0, capsuleShape2, -4.3f,0.5f,-20f);//�����߽���
	    wall=phc.createBody(viewManager, 0, boxShape2, 0, WANG_HALF_Y-4, -66);
	    pt=new PhysicsThread(this.viewManager);//���������߳�       
        pt.setFlag(true);//���������̵߳ı�־λ
        pt.start();//���������߳�
        
		special=new SpecialUtil(this);
		special.initSpecial();
		//��ʼ�����Ʒ��������
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
				ShaderManager.getShader(0));//����ģʽ��ͣ��ť
		camera[1]=new BN2DObject(540, 960, 800, 600, TextureManager.getTextures("camerakuang.png"), 
				ShaderManager.getShader(0));//����ģʽ��ͣ��ť
		
		Pause=new BN2DObject(980, 1700, 130, 130, TextureManager.getTextures("pause.png"), 
				ShaderManager.getShader(0));
		pv=new PauseView(this);//��ͣ����
	}
	
	public void initSound()
	{
		if(!musicOff)
		{
			if(MainActivity.sound.mp!=null){
				MainActivity.sound.mp.pause();
				MainActivity.sound.mp=null;
			}
			if(sceneIndex==1){//�ǿձ�������				
				MainActivity.sound.mp =  MediaPlayer.create(this.viewManager.activity, R.raw.xk_beijing_music) ;
			}else if(sceneIndex==2){//�Ƽ��ݱ�������
				MainActivity.sound.mp =  MediaPlayer.create(this.viewManager.activity, R.raw.kjg_beijing_music) ;
			}else if(sceneIndex==3){//ɳĮ��������
				MainActivity.sound.mp =  MediaPlayer.create(this.viewManager.activity, R.raw.desert_beijing_music) ;
			}
			MainActivity.sound.mp.setVolume(0.2f, 0.2f);//����������������
 			MainActivity.sound.mp.setLooping(true);//ѭ������
 			MainActivity.sound.mp.start();
		}
	}
	@Override
	public void drawView(GL10 gl) 
	{
		//�����ɫ��������Ȼ���
    	GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
    	//����͸��ͶӰ
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
    	moveCameraToBack();//����ƶ������
    	if(isCameraMove){
    		moveCameraToFront(); //��ǰ�ƶ������
    	}else{
    		noMoveCameraToFront();
    	}
        MatrixState3D.setCamera(EYE_X,EYE_Y,EYE_Z,TARGET_X,TARGET_Y,TARGET_Z,UP_X,UP_Y,UP_Z);  //�������������
        MatrixState3D.setLightLocation(0, 80, 50);
        //������Ȳ���
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);  
        if(sceneIndex==2)
        {
            drawScienceFictionScene();//���ƿƻó���
        }else if(sceneIndex==3)
        {
        	drawDesertScene();//����ɳĮ����
        }else if(sceneIndex==1)
        {
        	drawSkyScene();//�����ǿ�
        }
   
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);  //�ر���Ȳ���
        drawGameMirror();        //���ƾ�����
     
        //���ư�͸���ذ�
        if(sceneIndex==2)
        {
        	drawBTMScienceFictionGD();//���ƿƻù��
        }else if(sceneIndex==3)
        {
        	drawBTMDesertGD();//����ɳĮ���
        }else if(sceneIndex==1)
        {
        	drawBTMSkyGD();//�����ǿչ��
        }
        
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);          //������Ȳ���
        drawGame();
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);//�ر���Ȳ���
        
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
        		MainActivity.sound.playMusic(Constant.INIT_BOTTLE, 0);//��ʼ��ƿ����Ч
        	}
        	initObect=false;
        	phc.initAllObject(viewManager);
        }
        if(isPause)
		{//������ͣ�˵�
			pv.drawView();
		}
        if(isDrawAN)
        {//������ͣ��ť
     		MatrixState2D.pushMatrix();//�����ֳ� 
    		Pause.drawSelf();//��ͣ��ť
    		MatrixState2D.popMatrix();//�ָ��ֳ�
        }
    	MatrixState2D.pushMatrix();//�����ֳ� 
		camera[0].drawSelf();//����¼��ť
		if(drawCameraTip)//�����Ƿ��ϴ�¼����ʾ��...
		{
			camera[1].drawSelf();
		}
		MatrixState2D.popMatrix();
	}	
	
	public void drawGameOver()//������Ϸ��������
	{
		inGameOverView=true;//������Ϸ��������
		pt.getNumberBN2D.getStrikeAndSpareCount();
		for(BN2DObject bn:al2)
		{
			bn.drawSelf();
		}
		if(pt.getNumberBN2D.strike[0]!=null)//����ȫ����������
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
		
		if(pt.getNumberBN2D.spare[0]!=null)//���Ʋ�����������
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
		
		if(pt.getNumberBN2D.luoGou[0]!=null)//�����乵����������
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
		
		if(pt.getNumberBN2D.allPointsBN2D[0]!=null)//����ִܷ���100��
		{
			pt.getNumberBN2D.allPointsBN2D[0].drawSelf(600, 840);
			pt.getNumberBN2D.allPointsBN2D[1].drawSelf(660, 840);
			pt.getNumberBN2D.allPointsBN2D[2].drawSelf(720, 840);
		}else//����ܷ�С��100
		{
			if(pt.getNumberBN2D.allPointsBN2D[1]!=null)//����ִܷ���10
			{
				pt.getNumberBN2D.allPointsBN2D[1].drawSelf(630, 840);
				pt.getNumberBN2D.allPointsBN2D[2].drawSelf(690, 840);
			}else//����ܷ�С��10
			{
				if(pt.getNumberBN2D.allPointsBN2D[2]!=null)
				{
					pt.getNumberBN2D.allPointsBN2D[2].drawSelf(660, 840);
				}
			}
		}
	}
	
	int distance2=0;
	public void draw2DBN2DObejct()//����2D����
	{
		//���Ʒ�����=======================start=================
		if(isDrawScore)//���Ʒ�����
		{
			MatrixState2D.pushMatrix();//��������
			for(int i=0;i<al.size();i++)//��ʾ���������ݻ������� ��������
			{
				switch(i)
				{
				case 0:
					al.get(i).drawSelf(540,-300+12f*distance);//���Ʒ�����
					break;
				case 1:
					al.get(i).drawSelf(960,-250+10*distance);//�������
					break;
				case 2:
					al.get(i).drawSelf(540,-80+3.2f*distance);//����player
					break;
				}
			}
			for(int i=0;i<10;i++)//���Ƶڼ���
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
			MatrixState2D.popMatrix();//��������
			
			for(int i=0;i<pt.getNumberBN2D.currBlueNumber.size();i++)
			{
				if(i<5)//�������Ҫ��ʾ�ڵ�һ�еķ���
				{
					BN2DObject[] numbers=pt.getNumberBN2D.currBlueNumber.get(i);
					if(numbers[0]!=null)//�����������100��
					{
						MatrixState2D.pushMatrix();//��������
						numbers[0].drawSelf(60+165*i, -335+13.4f*distance);//������������
						numbers[1].drawSelf(100+165*i, -335+13.4f*distance);
						numbers[2].drawSelf(140+165*i, -335+13.4f*distance);
						MatrixState2D.popMatrix();//�ָ�����
					}else//�������С��100
					{
						if(numbers[1]!=null)//�����������10
						{
							MatrixState2D.pushMatrix();//��������
							numbers[1].drawSelf(80+165*i, -335+13.4f*distance);//������������
							numbers[2].drawSelf(120+165*i, -335+13.4f*distance);
							MatrixState2D.popMatrix();//�ָ�����
						}else//����С��10
						{
							MatrixState2D.pushMatrix();//��������
							numbers[2].drawSelf(100+165*i, -335+13.4f*distance);//ֻ��Ҫ����һ������
							MatrixState2D.popMatrix();//�ָ�����
						}
					}
				}else if(i>=5&&i<=9)//�������Ҫ��ʾ�ڵڶ��еķ���
				{
					BN2DObject[] numbers=pt.getNumberBN2D.currBlueNumber.get(i);
					if(numbers[0]!=null)//�����������100��
					{
						MatrixState2D.pushMatrix();//��������
						numbers[0].drawSelf(60+165*(i-5), -560+22.4f*distance);//������������
						numbers[1].drawSelf(100+165*(i-5), -560+22.4f*distance);
						numbers[2].drawSelf(140+165*(i-5), -560+22.4f*distance);
						MatrixState2D.popMatrix();//�ָ�����
					}else//�������С��100
					{
						if(numbers[1]!=null)//�����������10
						{
							MatrixState2D.pushMatrix();//��������
							numbers[1].drawSelf(80+165*(i-5), -560+22.4f*distance);//������������
							numbers[2].drawSelf(120+165*(i-5), -560+22.4f*distance);
							MatrixState2D.popMatrix();//�ָ�����
						}else
						{
							MatrixState2D.pushMatrix();//��������
							numbers[2].drawSelf(100+165*(i-5), -560+22.4f*distance);//ֻ��Ҫ����һ������
							MatrixState2D.popMatrix();//�ָ�����
						}
					}
				}
			}
			if(pt.getNumberBN2D.totalPointsBN2D[0]!=null)//����ִܷ���100��
			{
				MatrixState2D.pushMatrix();//��������
				pt.getNumberBN2D.totalPointsBN2D[0].drawSelf(910, -430+17.2f*distance);
				pt.getNumberBN2D.totalPointsBN2D[1].drawSelf(970, -430+17.2f*distance);
				pt.getNumberBN2D.totalPointsBN2D[2].drawSelf(1030, -430+17.2f*distance);
				MatrixState2D.popMatrix();//�ָ�����
			}else//����ܷ�С��100
			{
				if(pt.getNumberBN2D.totalPointsBN2D[1]!=null)//����ִܷ���10
				{
					MatrixState2D.pushMatrix();//��������
					pt.getNumberBN2D.totalPointsBN2D[1].drawSelf(940, -430+17.2f*distance);
					pt.getNumberBN2D.totalPointsBN2D[2].drawSelf(1000, -430+17.2f*distance);
					MatrixState2D.popMatrix();//�ָ�����
				}else//����ܷ�С��10
				{
					if(pt.getNumberBN2D.totalPointsBN2D[2]!=null)
					{
						MatrixState2D.pushMatrix();//��������
						pt.getNumberBN2D.totalPointsBN2D[2].drawSelf(970, -430+17.2f*distance);
						MatrixState2D.popMatrix();//�ָ�����
					}
				}
			}
			
			for(int i=0;i<pt.getNumberBN2D.everyTimeScore.size();i++)//����ÿ�εķ���
			{
				if(i!=0&&i%2==0&&i!=20)
				{
					drawPositionIndex++;
				}
				BN2DObject everyScore=pt.getNumberBN2D.everyTimeScore.get(i);//��ȡ��ǰ�Ļ��ƶ���
				if(i<10)//��ʾ�ڵ�һ�еķ���
				{
					if(everyScore!=null)
					{
						if(i%2==0)//�����ÿ�ֵĵ�һ������
						{
							everyScore.drawSelf(60+165*drawPositionIndex,-260+10.4f*distance);
						}else//�����ÿ�ֵĵڶ�������
						{
							everyScore.drawSelf(140+165*drawPositionIndex,-260+10.4f*distance);
						}
					}
				}else if(i>=10&&i<18)//��ʾ�ڶ���ǰ���ֵķ���
				{
					if(everyScore!=null)
					{
						if(i%2==0)//�����ÿ�ֵĵ�һ������
						{
							everyScore.drawSelf(60+165*(drawPositionIndex-5),-480+19.2f*distance);
						}else//�����ÿ�ֵĵڶ�������
						{
							everyScore.drawSelf(140+165*(drawPositionIndex-5),-480+19.2f*distance);
						}
					}
				}else//��ʾ��ʮ�ֱ����ķ���
				{
					if(everyScore!=null)
					{
						if(i==18)//�����ÿ�ֵĵ�һ������
						{
							everyScore.drawSelf(40+165*(drawPositionIndex-5),-480+19.2f*distance);
						}else if(i==19)//�����ÿ�ֵĵڶ�������
						{
							everyScore.drawSelf(100+165*(drawPositionIndex-5),-480+19.2f*distance);
						}else if(i==20)//�����ÿ�ֵĵ���������
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
					if(pt.getNumberBN2D.isShow)//��ʾ��ǰ����
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
				if(distance2>=100)//���ٻ���
				{
					distance2=0;
					isDrawCurrentScore=false;
					if(updateRound)//������¿�ʼ
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
			if(isScoreDown&&distance<50)//�����½�
			{
				distance++;
			}
			if(!isScoreDown&&distance>0)//��������
			{
				distance--;
			}
			//���Ʒ�����=======================end=================
			
			//�������б����������ֵ============start================
			if(isScoreDown)//�����ʾ������ ����ʾ����������ֵ
			{
				triangle.drawSelf();//���Ƶ�����
				if(firstStartGame)//����ǵ�һ�ο�ʼ��Ϸ ��Ĭ�ϻ���ȫ������ֵ
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
				}else//������ǣ�������򵹵��������
				{
					int kk=0;
					for(int i=0;i<4;i++)
					{
						for(int j=0;j<=i;j++)
						{
							if(pt.ballIndex[kk]!=0)
							{
								//����û�е����������ֵ
								pt.getTriangleNumber.drawBallIndex.get(kk).drawSelf(150-35*i+70*j, 1100-67*i);
							}
							kk++;
						}
					}
				}
			}
			if(!isScoreDown&&distance==0)//�������ס�����ٻ��Ʒ�����
			{
				distance=0;
				isDrawScore=false;
				pt.getNumberBN2D.isShow=true;
			}
		}else
		{
			if(pt.restartTriangle)//���¿�ʼ���Ʊ�������ֵ
			{
				pt.getTriangleNumber.restart();
				pt.restartTriangle=false;
			}
		}
		//�������б����������ֵ============end================
	}
	
	public void reSetData()
	{
		curr=null;//��ǰ���صĶ�������	
		ballA=null;//����������
		isMoveFlag=false;//���Ƿ��ƶ�
		isDrawAN=true;
		isMoveGuiDaoFlag=true;//�Ƿ��ƶ�����ı�ʶ
		initObect=true;
		effictOff=false;
		musicOff=false;
		viewManager.isMoveCameraBack=true;
		viewManager.isMoveCameraFront=false;
		ballCount=0;//�������
		count=0;//������
		drawScoreCount=0;
		drawPositionIndex=0;//���Ʒ�����λ��
	    skyAngle=0;//�����ת�Ƕ�
	    guidao_z=50;//���z����
	    feixingwu1_z=50;//������1
		preX=0;
		preY=0;
		ez=0;
		tz=-24;//��ʱ��¼���������������	
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
