package com.bn.util.thread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;
import static com.bn.util.constant.Constant.*;
import static com.bn.util.constant.SourceConstant.*;
import android.annotation.SuppressLint;

import com.bn.bowling.MainActivity;
import com.bn.bowling.MySurfaceView;
import com.bn.object.GameObject;
import com.bn.util.constant.Constant;
import com.bn.util.tool.CalculateScore;
import com.bn.util.tool.GetNumberBN2D;
import com.bn.util.tool.GetTriangleNumber;
import com.bn.util.tool.PhyCaulate;
import com.bn.util.tool.SYSUtil;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.linearmath.Transform;

public class PhysicsThread extends Thread{
	
	private MySurfaceView mv;
	public PhyCaulate phc;//�����װ��
	private boolean flag = true;//�Ƿ����߳�
	private boolean worldStep=true;
	float[] tempB=null;//��������ٶ�
	float[] ballPositionB=null;//�������λ��
	private ArrayList<RigidBody> addBottles=new ArrayList<RigidBody>();//���Ҫ��ӵĸ����б�
	private ArrayList<RigidBody> removeBottles=new ArrayList<RigidBody>();//���Ҫ�Ƴ��ı������б�
	@SuppressLint("UseSparseArrays")
	private Map<Integer,GameObject>  hmIG=new HashMap<Integer,GameObject>();
	
	public GetNumberBN2D getNumberBN2D;//��ȡ�������ƶ���
	public GetTriangleNumber getTriangleNumber;//��ȡ����������ֵ���ƶ���
	public int[] ballIndex=new int[10];//������Ƶı���������ֵ
	
	public boolean restartTriangle=false;//���¿�ʼ���Ʊ�������ֵ
	public boolean restartScore=false;//���¿�ʼ���Ʒ���
	public boolean isFristThird=false;//�Ƿ��һ���жϽ��е�����
	
	int maxCount=10;//�������
	int updateCount=0;
	public boolean isRecordPosition=true;
	public PhysicsThread(MySurfaceView mv)
	{//������
		this.mv=mv;//��ʼ��������ʾ�������
		this.phc=new PhyCaulate();
		getNumberBN2D=new GetNumberBN2D();
		getTriangleNumber=new GetTriangleNumber();
		for(int i=0;i<ballIndex.length;i++)
		{
			ballIndex[i]=0;
		}
	}
	public void setFlag(boolean flag)
	{//���ñ�־λ�ķ���
		this.flag = flag;//�ı��־λ
	}
	int countF=0;//����֡���ʵ�ʱ��������--������
	long timeStart=System.nanoTime();//��ʼʱ��
	
	static final long spanMin=(long)((1/60.0)*1000*1000*1000);//��С����
	long lastTimeStamp=System.nanoTime();//��ʼʱ��
	public void run()
	{//��дrun����	
		while(flag)
		{//�����־λΪtrue�������߳�	
			//�ж�=================start=========
			if(isPause||drawCameraTip||ShangChuanView)
			{
				worldStep=false;
			}else
			{
				worldStep=true;
			}
			//�ж�=================end=========
			if(worldStep)
			{
				if(countF==9)//ÿʮ��һ����֡����
				{
					long timeEnd=System.nanoTime();//����ʱ��	
					countF=0;//��������0
					timeStart=timeEnd;//��ʼʱ����Ϊ����ʱ��
				}
				countF=(countF+1)%10;//���¼�������ֵ
				long currTimeStamp=System.nanoTime();//��ǰʱ��
				if((currTimeStamp-lastTimeStamp)<spanMin)//�ж������Ƿ����
				{
					try 
					{
						Thread.sleep(5);//����5����
					} catch (InterruptedException e) 
					{
						e.printStackTrace();//��ӡ�쳣��Ϣ
					}
					continue;//����ѭ��
				}
				lastTimeStamp=currTimeStamp;//����ǰʱ�丳ֵ����ʼʱ��
				dynamicsWorld.stepSimulation(TIME_STEP, MAX_SUB_STEPS);//��ʼģ��	
					
				deleteBody();//������������ɾ������ķ���
				updateScene();//���³���
				addBody();//��������������Ӹ���ķ���
				
				synchronized(lockAll)
				{
					if(hm.size()>0)
					{
						hmIG.clear();
						hmIG.putAll(hm);
					}
				}
				
				updateDrawBallData();//������
				updateDrawBottlesData();//����ƿ��
				updateBodyVer();//��������ٶ�
			}
		}
	}
	
	public void updateScene()//���³���
	{
		if(countTurn>maxCount||wall==null)
		{//����Ϊ�գ�������Ϊ�գ���������������10ʱ�����ٸ��³���
			return;
		}
		ArrayList<GameObject>  tempBall=new ArrayList<GameObject> ();
		synchronized(lockBall)
		{
			tempBall.addAll(alGBall);
		}
		if(tempBall.size()<=0){
			return;
		}
		playBeaterBottleSound();
		Transform posi=tempBall.get(0).body.getMotionState().getWorldTransform(new Transform());
		Transform posi2=wall.getMotionState().getWorldTransform(new Transform());
		if((posi.origin.z-posi2.origin.z)<0.8f)
		{//�����˶����߽�ʱ
			if(!Constant.effictOff&&status!=1){
    			MainActivity.sound.playMusic(Constant.BALL_WALL_SOUND, 0);//����ײ��ǽ����Ч
    		}
			crashBall[0]=posi.origin.x;
			crashBall[1]=posi.origin.y;
			crashBall[2]=posi.origin.z;
			isCrashWall=true;
		}
		if((posi.origin.z-posi2.origin.z)<0.8f||posi.origin.y<0.1f)
		{
			status=1;
		}
		if(status!=1||countTurn>maxCount)
		{//���ƶ��л��ߴﵽ10�֣�����
			return;
		}
		updateCount++;
		if(updateCount<=260)
		{
			return;
		}
		calBottlesNumber();//��ƿ�ӽ���ֹͣʱ������ײ����ƿ�Ӹ���
		if(countTurn<maxCount)
		{//С��2��
			if(BALL_SUM>0&&ballCount==1)
			{//�����һ��δȫ������
				phc.initWorldBall(mv);//���´����򣬿�ʼ�ڶ���
			}else if((BALL_SUM==0&&ballCount==1)||(ballCount==2&&countTurn<maxCount)){//�������ȫ��������������������������				
				countTurn++;//������1
				updateRound=true;
            	if(!Constant.effictOff){
            		MainActivity.sound.playMusic(Constant.INIT_BOTTLE, 0);//��ʼ��ƿ����Ч
            	}
				ballCount=0;//��¼ÿ�ֵĴ���
				phc.initAllObject(mv);
				deleteBody();
				restartTriangle=true;
			}
			isCrashWall=false;
//			System.out.println("��"+countTurn+"�ֿ�ʼ======");
		}
		else//���һ��
		{
			if(ballCount==1&&BALL_SUM==0)
			{//��һ��ȫ������
				if(!Constant.effictOff){
            		MainActivity.sound.playMusic(Constant.INIT_BOTTLE, 0);//��ʼ��ƿ����Ч
            	}
//				System.out.println("=====��10��   ��1�����------��2��ʼ========");
				phc.initAllObject(mv);//���еڶ���
				deleteBody();
				isFristThird=true;//����������־λ
			}
			else if(ballCount==1&&BALL_SUM>0)
			{
//				System.out.println("=====��10��   ��2��ʼ========");
				phc.initWorldBall(mv);//���´����򣬽��еڶ���
			}
			else if(ballCount==2&&BALL_SUM==0)
			{
				if(!Constant.effictOff){
            		MainActivity.sound.playMusic(Constant.INIT_BOTTLE, 0);//��ʼ��ƿ����Ч
            	}
//				System.out.println("=====��10��   ��2�����       -----��3��ʼ========");
				phc.initAllObject(mv);//����������
				deleteBody();
				isFristThird=false;//��־λ��Ϊfalse
			}
			else if(ballCount==2&&BALL_SUM>0)
			{
				if(!isFristThird){
//					System.out.println("=====��10��   ��2�����==���ֽ���======");
					countTurn++;
				}
				else{
//					System.out.println("=====��10��   ��3��ʼ========");
					if(!Constant.effictOff){
	            		MainActivity.sound.playMusic(Constant.INIT_BOTTLE, 0);//��ʼ��ƿ����Ч
	            	}			
					phc.initAllObject(mv);//���е�����
					deleteBody();
					isFristThird=false;
				}
			}
			else if(ballCount==3){
//				System.out.println("********��10��   ��3�����===���ֽ���=====");
				countTurn++;
			}
			if(countTurn>maxCount){//ʮ���Ѿ���������־λ��Ϊfalse
				isGamePlay=false;
			}
		}
		isDrawAN=true;
		isMoveFlag=false;//���ƶ���־λ��Ϊfalse
		isMoveGuiDaoFlag=true;
		status=-1;//��ʼ��״̬
		EYE_Z=24;
		TARGET_Z=0;
		updateCount=0;
		isDrawScore=true;
		isScoreDown=true;
		isDrawCurrentScore=true;
	}
	
	public void addBody()
	{//��������������Ӹ���ķ���
		if(Bottles.size()<=0)
		{//���Ҫ��ӵĸ�������б�Ϊ��ʱ
			return;//����
		}
		synchronized (lockAdd)
		{//����
			addBottles.addAll(Bottles);//Ϊ��ʱ���Ҫ��ӵĸ����б�ֵ
			Bottles.clear();//��մ��Ҫ��ӵĸ�������б�
		}
		for(int i=0;i<addBottles.size();i++)
		{//ѭ��������ʱ���Ҫ��ӵĸ����б�--һһ��������ӽ�����������
			dynamicsWorld.addRigidBody(addBottles.get(i));//��������ӽ���������
		}
		addBottles.clear();//�����ʱ���Ҫ��ӵĸ����б�
	}
	public void deleteBody()
	{//������������ɾ������ķ���
		if(deleteBottles.size()<=0)
		{//���Ҫɾ���ĸ����б�Ϊ��ʱ
			return;//����
		}
		synchronized (lockDelete)
		{//����
			removeBottles.addAll(deleteBottles);//Ϊ��ʱ���Ҫɾ���ĸ����б�ֵ
			deleteBottles.clear();//��մ��Ҫɾ���ĸ����б�
		}
		for(int i=0;i<removeBottles.size();i++)
		{//ѭ��������ʱ���Ҫɾ���ĸ����б�--������������һһɾ������
			dynamicsWorld.removeRigidBody(removeBottles.get(i));//������������ɾ������
		}
		removeBottles.clear();//�����ʱ���Ҫɾ���ĸ����б�
	}
	public void updateBodyVer(){//����������ٶ�
		ArrayList<GameObject>  tempBall=new ArrayList<GameObject> ();
		synchronized(lockBall)
		{
			tempBall.addAll(alGBall);
		}
		if(tempBall.size()<=0){
			return;
		}
		float[] tempA=null;
		if(qf.size()<=0)
		{
			return;//����
		}
		synchronized(lockV)
		{
			while(qf.size()>0){
				tempA=qf.poll();
			}
		}
		if(tempA!=null){
			tempB=tempA;
		}
		if(tempB==null){
			return;
		}
		tempBall.get(0).body.activate();//�������
		tempBall.get(0).body.setLinearVelocity(new Vector3f(tempB[0]*1f,tempB[1],tempB[2]));
		tempBall.get(0).body.setAngularVelocity(new Vector3f(0,0,0));
	}
	
	
	int result=0;
	public void calBottlesNumber()//����ײ����ƿ�Ӹ���
	{
		if(hmIG.size()<=0||countTurn>10){
			return;
		}
		int[] temp={0,0,0,0,0,0,0,0,0,0};
		ArrayList<RigidBody> delBody=new ArrayList<RigidBody>();//��¼��ɾ���ĸ���

		for(Entry<Integer, GameObject> entry: hmIG.entrySet())
		{//�ж����Ƿ�ײ��������¼����
			GameObject tt=hmIG.get(entry.getKey());
			Transform t=tt.body.getMotionState().getWorldTransform(new Transform());
			if(Math.abs(t.origin.x)>2.7f||t.origin.z<=-52f||t.origin.y<=RANGE_MAX||t.origin.z>(-45)){
				delBody.add(tt.body);
				temp[entry.getKey()-1]=entry.getKey();
				ballNumber[entry.getKey()-1]=0;
			}
		}
		
		for(int i=0;i<temp.length;i++)
		{
			if(temp[i]!=0)
			{//���temp[i]!s=0,��ʾ��ƿ����Ҫ��ɾ��
				hmIG.remove(temp[i]);
			}
		}
		
		synchronized(lockAll)
		{
			hm.clear();
			hm.putAll(hmIG);
		}
		result=delBody.size();//��ȡ�˴�ײ����ƿ����
		BALL_SUM-=result;
		if(BALL_SUM==0&&ballCount==1){
			if(!effictOff){//����
				MainActivity.sound.playMusic(Constant.APPLAUSE, 0);
			}
		}
		
		synchronized (lockDelete){
			deleteBottles.addAll(delBody);//ɾ��ײ����ƿ�Ӹ���
		}
		
		//��ȡ����===========
		CalculateScore.calculateScore(result);
		getNumberBN2D.getEveryTimeScore();
		getNumberBN2D.getNumberBN2D();
		//��ȡ��������ֵ========
		firstStartGame=false;//�ǵ�һ�ο�ʼ��Ϸ
		ballIndex=getTriangleNumber.getDrawBallIndex(ballNumber);
	}
	
	public void playBeaterBottleSound()//����ײ��ƿ�ӵ���Ч
	{
		ArrayList<GameObject>  tempBall=new ArrayList<GameObject> ();
		synchronized(lockBall)
		{
			tempBall.addAll(alGBall);
		}
		if(tempBall.size()<=0||hmIG.size()<=0){
			return;
		}
		Transform ballTran=tempBall.get(0).body.getMotionState().getWorldTransform(new Transform());
		for(Entry<Integer, GameObject> entry: hmIG.entrySet())
		{
			GameObject  tempBottle=hmIG.get(entry.getKey());
			Vector3f aabbMin=new Vector3f();
			Vector3f aabbMax=new Vector3f();
			tempBottle.body.getAabb(aabbMin, aabbMax);
			if(ballTran.origin.x>= aabbMin.x&&ballTran.origin.x<=aabbMax.x&&
					ballTran.origin.z>= aabbMin.z&&ballTran.origin.z<=aabbMax.z)
			{
				if(!effictOff){
	    			MainActivity.sound.playMusic(Constant.BALL_BATTLE_BEATER, 0);
	    		}	
			}
		}
	}
	
	public void updateDrawBallData()
	{
		//��ȡ���λ���Լ���̬===========start==========
		ArrayList<GameObject>  tempBall=new ArrayList<GameObject> ();
		synchronized(lockBall)
		{
			tempBall.addAll(alGBall);
		}
		if(tempBall.size()<=0){
			return;
		}
		float[] bPosi=new float[7];
		Transform trans=tempBall.get(0).body.getMotionState().getWorldTransform(new Transform());
		bPosi[0]=trans.origin.x;
		bPosi[1]=trans.origin.y;
		bPosi[2]=trans.origin.z;
		for(int j=3;j<7;j++)
		{
			bPosi[j]=1.0f;
		}
		Quat4f ro=trans.getRotation(new Quat4f());//��ȡ��ǰ�任����ת��Ϣ
		if(ro.x!=0||ro.y!=0||ro.z!=0)
		{
			float[] fa=SYSUtil.fromSYStoAXYZ(ro);//����Ԫ��ת����AXYZ����ʽ
			if(fa[0]!=0){
				bPosi[3]=fa[0];//��¼��i��ƿ�ӵ���ת��Ϣ
				bPosi[4]=fa[1];//��¼��i��ƿ�ӵ���ת��Ϣ
				bPosi[5]=fa[2];//��¼��i��ƿ�ӵ���ת��Ϣ
				bPosi[6]=fa[3];//��¼��i��ƿ�ӵ���ת��Ϣ
			}
		}
		synchronized(lockBallTransform){
			ballTransform.offer(bPosi);
		}
		//��ȡ���λ���Լ���̬===========end==========
	}
	
	public void updateDrawBottlesData()//����ƿ������
	{
		//��ȡÿ��ƿ�ӵ�λ���Լ���̬===========start==========
		if(hmIG.size()<=0){
			return;
		}
		float[][] result=new float[10][];
		for(Entry<Integer, GameObject> entry: hmIG.entrySet())
		{
			//�����������л�ȡ��������Ӧ����ı任��Ϣ����
		  	Transform trans=hmIG.get(entry.getKey()).body.getMotionState().getWorldTransform(new Transform());
	    	result[entry.getKey()-1]=new float[7];
	    	result[entry.getKey()-1][0]=trans.origin.x;
	    	result[entry.getKey()-1][1]=trans.origin.y;
	    	result[entry.getKey()-1][2]=trans.origin.z;
			for(int j=3;j<7;j++)
			{
				result[entry.getKey()-1][j]=1;
			}
			Quat4f ro=trans.getRotation(new Quat4f());//��ȡ��ǰ�任����ת��Ϣ
			if(ro.x!=0||ro.y!=0||ro.z!=0)
			{
				float[] fa=SYSUtil.fromSYStoAXYZ(ro);//����Ԫ��ת����AXYZ����ʽ
				if(fa[0]!=0)
				{
					result[entry.getKey()-1][3]=fa[0];//��¼��i��ƿ�ӵ���ת��Ϣ
					result[entry.getKey()-1][4]=fa[1];//��¼��i��ƿ�ӵ���ת��Ϣ
					result[entry.getKey()-1][5]=fa[2];//��¼��i��ƿ�ӵ���ת��Ϣ
					result[entry.getKey()-1][6]=fa[3];//��¼��i��ƿ�ӵ���ת��Ϣ
				}
			}
		}
		synchronized(lockTransform){
			bTransform.offer(result);
		}
		//��ȡÿ��ƿ�ӵ�λ���Լ���̬===========end==========
	}
}
