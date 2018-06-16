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
	public PhyCaulate phc;//物理封装类
	private boolean flag = true;//是否开启线程
	private boolean worldStep=true;
	float[] tempB=null;//缓存球的速度
	float[] ballPositionB=null;//缓存球的位置
	private ArrayList<RigidBody> addBottles=new ArrayList<RigidBody>();//存放要添加的刚体列表
	private ArrayList<RigidBody> removeBottles=new ArrayList<RigidBody>();//存放要移除的保龄球列表
	@SuppressLint("UseSparseArrays")
	private Map<Integer,GameObject>  hmIG=new HashMap<Integer,GameObject>();
	
	public GetNumberBN2D getNumberBN2D;//获取分数绘制对象
	public GetTriangleNumber getTriangleNumber;//获取保龄球索引值绘制对象
	public int[] ballIndex=new int[10];//允许绘制的保龄球索引值
	
	public boolean restartTriangle=false;//重新开始绘制保龄索引值
	public boolean restartScore=false;//重新开始绘制分数
	public boolean isFristThird=false;//是否第一球判断进行第三次
	
	int maxCount=10;//最大轮数
	int updateCount=0;
	public boolean isRecordPosition=true;
	public PhysicsThread(MySurfaceView mv)
	{//构造器
		this.mv=mv;//初始化界面显示类的引用
		this.phc=new PhyCaulate();
		getNumberBN2D=new GetNumberBN2D();
		getTriangleNumber=new GetTriangleNumber();
		for(int i=0;i<ballIndex.length;i++)
		{
			ballIndex[i]=0;
		}
	}
	public void setFlag(boolean flag)
	{//设置标志位的方法
		this.flag = flag;//改变标志位
	}
	int countF=0;//计算帧速率的时间间隔次数--计算器
	long timeStart=System.nanoTime();//开始时间
	
	static final long spanMin=(long)((1/60.0)*1000*1000*1000);//最小速率
	long lastTimeStamp=System.nanoTime();//起始时间
	public void run()
	{//重写run方法	
		while(flag)
		{//如果标志位为true，启动线程	
			//判断=================start=========
			if(isPause||drawCameraTip||ShangChuanView)
			{
				worldStep=false;
			}else
			{
				worldStep=true;
			}
			//判断=================end=========
			if(worldStep)
			{
				if(countF==9)//每十次一计算帧速率
				{
					long timeEnd=System.nanoTime();//结束时间	
					countF=0;//计算器置0
					timeStart=timeEnd;//起始时间置为结束时间
				}
				countF=(countF+1)%10;//更新计数器的值
				long currTimeStamp=System.nanoTime();//当前时间
				if((currTimeStamp-lastTimeStamp)<spanMin)//判断速率是否过快
				{
					try 
					{
						Thread.sleep(5);//休眠5毫秒
					} catch (InterruptedException e) 
					{
						e.printStackTrace();//打印异常信息
					}
					continue;//继续循环
				}
				lastTimeStamp=currTimeStamp;//将当前时间赋值给起始时间
				dynamicsWorld.stepSimulation(TIME_STEP, MAX_SUB_STEPS);//开始模拟	
					
				deleteBody();//从物理世界中删除刚体的方法
				updateScene();//更新场景
				addBody();//向物理世界中添加刚体的方法
				
				synchronized(lockAll)
				{
					if(hm.size()>0)
					{
						hmIG.clear();
						hmIG.putAll(hm);
					}
				}
				
				updateDrawBallData();//更新球
				updateDrawBottlesData();//更新瓶子
				updateBodyVer();//更新球的速度
			}
		}
	}
	
	public void updateScene()//更新场景
	{
		if(countTurn>maxCount||wall==null)
		{//当球为空，或者网为空，或者总轮数大于10时，不再更新场景
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
		{//当球运动到边界时
			if(!Constant.effictOff&&status!=1){
    			MainActivity.sound.playMusic(Constant.BALL_WALL_SOUND, 0);//播放撞击墙的音效
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
		{//球移动中或者达到10轮，返回
			return;
		}
		updateCount++;
		if(updateCount<=260)
		{
			return;
		}
		calBottlesNumber();//当瓶子近乎停止时，计算撞到的瓶子个数
		if(countTurn<maxCount)
		{//小于2轮
			if(BALL_SUM>0&&ballCount==1)
			{//如果第一球未全部击倒
				phc.initWorldBall(mv);//重新创建球，开始第二球
			}else if((BALL_SUM==0&&ballCount==1)||(ballCount==2&&countTurn<maxCount)){//如果该球全部击倒，或者连续扔了两个球				
				countTurn++;//轮数加1
				updateRound=true;
            	if(!Constant.effictOff){
            		MainActivity.sound.playMusic(Constant.INIT_BOTTLE, 0);//初始化瓶子音效
            	}
				ballCount=0;//记录每轮的次数
				phc.initAllObject(mv);
				deleteBody();
				restartTriangle=true;
			}
			isCrashWall=false;
//			System.out.println("第"+countTurn+"轮开始======");
		}
		else//最后一轮
		{
			if(ballCount==1&&BALL_SUM==0)
			{//第一球全部击倒
				if(!Constant.effictOff){
            		MainActivity.sound.playMusic(Constant.INIT_BOTTLE, 0);//初始化瓶子音效
            	}
//				System.out.println("=====第10轮   第1球结束------第2球开始========");
				phc.initAllObject(mv);//进行第二球
				deleteBody();
				isFristThird=true;//允许第三球标志位
			}
			else if(ballCount==1&&BALL_SUM>0)
			{
//				System.out.println("=====第10轮   第2球开始========");
				phc.initWorldBall(mv);//重新创建球，进行第二球
			}
			else if(ballCount==2&&BALL_SUM==0)
			{
				if(!Constant.effictOff){
            		MainActivity.sound.playMusic(Constant.INIT_BOTTLE, 0);//初始化瓶子音效
            	}
//				System.out.println("=====第10轮   第2球结束       -----第3球开始========");
				phc.initAllObject(mv);//创建第三球
				deleteBody();
				isFristThird=false;//标志位置为false
			}
			else if(ballCount==2&&BALL_SUM>0)
			{
				if(!isFristThird){
//					System.out.println("=====第10轮   第2球结束==本局结束======");
					countTurn++;
				}
				else{
//					System.out.println("=====第10轮   第3球开始========");
					if(!Constant.effictOff){
	            		MainActivity.sound.playMusic(Constant.INIT_BOTTLE, 0);//初始化瓶子音效
	            	}			
					phc.initAllObject(mv);//进行第三球
					deleteBody();
					isFristThird=false;
				}
			}
			else if(ballCount==3){
//				System.out.println("********第10轮   第3球结束===本局结束=====");
				countTurn++;
			}
			if(countTurn>maxCount){//十轮已经结束，标志位置为false
				isGamePlay=false;
			}
		}
		isDrawAN=true;
		isMoveFlag=false;//球移动标志位置为false
		isMoveGuiDaoFlag=true;
		status=-1;//初始化状态
		EYE_Z=24;
		TARGET_Z=0;
		updateCount=0;
		isDrawScore=true;
		isScoreDown=true;
		isDrawCurrentScore=true;
	}
	
	public void addBody()
	{//向物理世界中添加刚体的方法
		if(Bottles.size()<=0)
		{//存放要添加的刚体对象列表为空时
			return;//返回
		}
		synchronized (lockAdd)
		{//加锁
			addBottles.addAll(Bottles);//为临时存放要添加的刚体列表赋值
			Bottles.clear();//清空存放要添加的刚体对象列表
		}
		for(int i=0;i<addBottles.size();i++)
		{//循环遍历临时存放要添加的刚体列表--一一将刚体添加进物理世界中
			dynamicsWorld.addRigidBody(addBottles.get(i));//将刚体添加进物理世界
		}
		addBottles.clear();//清空临时存放要添加的刚体列表
	}
	public void deleteBody()
	{//从物理世界中删除刚体的方法
		if(deleteBottles.size()<=0)
		{//存放要删除的刚体列表为空时
			return;//返回
		}
		synchronized (lockDelete)
		{//加锁
			removeBottles.addAll(deleteBottles);//为临时存放要删除的刚体列表赋值
			deleteBottles.clear();//清空存放要删除的刚体列表
		}
		for(int i=0;i<removeBottles.size();i++)
		{//循环遍历临时存放要删除的刚体列表--从物理世界中一一删除刚体
			dynamicsWorld.removeRigidBody(removeBottles.get(i));//从物理世界中删除刚体
		}
		removeBottles.clear();//清空临时存放要删除的刚体列表
	}
	public void updateBodyVer(){//更新球刚体速度
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
			return;//返回
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
		tempBall.get(0).body.activate();//激活刚体
		tempBall.get(0).body.setLinearVelocity(new Vector3f(tempB[0]*1f,tempB[1],tempB[2]));
		tempBall.get(0).body.setAngularVelocity(new Vector3f(0,0,0));
	}
	
	
	int result=0;
	public void calBottlesNumber()//计算撞到的瓶子个数
	{
		if(hmIG.size()<=0||countTurn>10){
			return;
		}
		int[] temp={0,0,0,0,0,0,0,0,0,0};
		ArrayList<RigidBody> delBody=new ArrayList<RigidBody>();//记录待删除的刚体

		for(Entry<Integer, GameObject> entry: hmIG.entrySet())
		{//判断球是否被撞倒，并记录个数
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
			{//如果temp[i]!s=0,表示本瓶子需要被删除
				hmIG.remove(temp[i]);
			}
		}
		
		synchronized(lockAll)
		{
			hm.clear();
			hm.putAll(hmIG);
		}
		result=delBody.size();//获取此次撞到的瓶子数
		BALL_SUM-=result;
		if(BALL_SUM==0&&ballCount==1){
			if(!effictOff){//鼓掌
				MainActivity.sound.playMusic(Constant.APPLAUSE, 0);
			}
		}
		
		synchronized (lockDelete){
			deleteBottles.addAll(delBody);//删除撞到的瓶子刚体
		}
		
		//获取分数===========
		CalculateScore.calculateScore(result);
		getNumberBN2D.getEveryTimeScore();
		getNumberBN2D.getNumberBN2D();
		//获取保龄索引值========
		firstStartGame=false;//非第一次开始游戏
		ballIndex=getTriangleNumber.getDrawBallIndex(ballNumber);
	}
	
	public void playBeaterBottleSound()//播放撞击瓶子的音效
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
		//获取球的位置以及姿态===========start==========
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
		Quat4f ro=trans.getRotation(new Quat4f());//获取当前变换的旋转信息
		if(ro.x!=0||ro.y!=0||ro.z!=0)
		{
			float[] fa=SYSUtil.fromSYStoAXYZ(ro);//将四元数转换成AXYZ的形式
			if(fa[0]!=0){
				bPosi[3]=fa[0];//记录第i个瓶子的旋转信息
				bPosi[4]=fa[1];//记录第i个瓶子的旋转信息
				bPosi[5]=fa[2];//记录第i个瓶子的旋转信息
				bPosi[6]=fa[3];//记录第i个瓶子的旋转信息
			}
		}
		synchronized(lockBallTransform){
			ballTransform.offer(bPosi);
		}
		//获取球的位置以及姿态===========end==========
	}
	
	public void updateDrawBottlesData()//更新瓶子数据
	{
		//获取每个瓶子的位置以及姿态===========start==========
		if(hmIG.size()<=0){
			return;
		}
		float[][] result=new float[10][];
		for(Entry<Integer, GameObject> entry: hmIG.entrySet())
		{
			//从物理世界中获取这个物体对应刚体的变换信息对象
		  	Transform trans=hmIG.get(entry.getKey()).body.getMotionState().getWorldTransform(new Transform());
	    	result[entry.getKey()-1]=new float[7];
	    	result[entry.getKey()-1][0]=trans.origin.x;
	    	result[entry.getKey()-1][1]=trans.origin.y;
	    	result[entry.getKey()-1][2]=trans.origin.z;
			for(int j=3;j<7;j++)
			{
				result[entry.getKey()-1][j]=1;
			}
			Quat4f ro=trans.getRotation(new Quat4f());//获取当前变换的旋转信息
			if(ro.x!=0||ro.y!=0||ro.z!=0)
			{
				float[] fa=SYSUtil.fromSYStoAXYZ(ro);//将四元数转换成AXYZ的形式
				if(fa[0]!=0)
				{
					result[entry.getKey()-1][3]=fa[0];//记录第i个瓶子的旋转信息
					result[entry.getKey()-1][4]=fa[1];//记录第i个瓶子的旋转信息
					result[entry.getKey()-1][5]=fa[2];//记录第i个瓶子的旋转信息
					result[entry.getKey()-1][6]=fa[3];//记录第i个瓶子的旋转信息
				}
			}
		}
		synchronized(lockTransform){
			bTransform.offer(result);
		}
		//获取每个瓶子的位置以及姿态===========end==========
	}
}
