package com.bn.util.tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.vecmath.Vector3f;
import static com.bn.util.constant.Constant.*;
import static com.bn.util.constant.SourceConstant.*;
import android.annotation.SuppressLint;

import com.bn.bowling.MySurfaceView;
import com.bn.object.GameObject;
import com.bn.util.constant.SourceConstant;
import com.bulletphysics.collision.broadphase.AxisSweep3;
import com.bulletphysics.collision.dispatch.CollisionConfiguration;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CapsuleShape;
import com.bulletphysics.collision.shapes.CapsuleShapeZ;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.SphereShape;
import com.bulletphysics.collision.shapes.StaticPlaneShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;

public class PhyCaulate {
	//初始化物理世界的方法
	public void initWorld()
	{
		//创建碰撞检测配置信息对象
		CollisionConfiguration collisionConfiguration = new DefaultCollisionConfiguration();		
		//创建碰撞检测算法分配者对象，其功能为扫描所有的碰撞检测对，并确定适用的检测策略对应的算法
		CollisionDispatcher dispatcher = new CollisionDispatcher(collisionConfiguration);		
		//设置整个物理世界的边界信息
		Vector3f worldAabbMin = new Vector3f(-10000, -10000, -10000);
		Vector3f worldAabbMax = new Vector3f(10000, 10000, 10000);
		int maxProxies = 1024;
		//创建碰撞检测粗测阶段的加速算法对象
		AxisSweep3 overlappingPairCache =new AxisSweep3(worldAabbMin, worldAabbMax, maxProxies);
		//创建推动约束解决者对象
		SequentialImpulseConstraintSolver solver = new SequentialImpulseConstraintSolver();
		//创建物理世界对象
		dynamicsWorld = new DiscreteDynamicsWorld(dispatcher, overlappingPairCache, solver,collisionConfiguration);
		//设置重力加速度
		dynamicsWorld.setGravity(new Vector3f(0, -10, 0));
		capsuleShape=new CapsuleShape(CAPSULE_RADIUS,CAPSULE_HEIGHT);//组合体——胶囊形状0.28
		sphereShape=new SphereShape(BOTTOM_BALL_RADIUS);//组合体——球体形状
		boxShape=new BoxShape(new Vector3f(BOX_HALF_WIDTH,BOX_HALF_HEIGHT,BOX_HALF_WIDTH));//组合体—立方体-半边长，半高和半边宽
		planeShape=new StaticPlaneShape(new Vector3f(0, 1, 0), 0);//创建共用的平面形状
		
		capsuleShape2=new CapsuleShapeZ(CAPSULE_RADIUS2,CAPSULE_HEIGHT2);//共用的胶囊形状0.28
		boxShape2=new BoxShape(new Vector3f(WANG_HALF_X,WANG_HALF_Y,WANG_HALF_Z));//网
		boxShape3=new BoxShape(new Vector3f(GUIDAO_HALF_X,GUIDAO_HALF_Y,GUIDAO_HALF_Z));//台子
		
		//创建共用的形状数组
		csa[2]=capsuleShape;
		csa[1]=sphereShape;
		csa[0]=boxShape;
	}
	
	@SuppressLint("UseSparseArrays")
	public void initWorldBottles(MySurfaceView viewManager)
	{		
		int k=0;
		GameObject tempGO=null;
		Map<Integer,GameObject>  hmT=new HashMap<Integer,GameObject>();
		for(int i=0;i<4;i++)
		{
			for(int j=0;j<=i;j++)
			{//创建十个瓶子
				tempGO=new GameObject(
						viewManager,
						bottle_model,BottleId,
						csa,dynamicsWorld,
						BOTTOM_BALL_RADIUS,//物理世界球的半径
						BOX_HALF_WIDTH,BOX_HALF_HEIGHT,//物理世界长方体盒子的尺寸--半边长，半高
						CAPSULE_RADIUS,CAPSULE_HEIGHT,//物理世界胶囊的尺寸
						BOTTLE_QUALITY,
						BATTLE_ORIGN_X*i+j*BATTLE_SPAN_X,
						FLOOR_Y+GUIDAO_HALF_Y*2+BOTTOM_BALL_RADIUS*2f+BOX_HALF_HEIGHT*2,
						BATTLE_ORIGN_Z-BATTLE_SPAN_Z*i);//创建瓶子物体的绘制对象 
				tempGO.body.forceActivationState(RigidBody.WANTS_DEACTIVATION);//静止
				k++;
				hmT.put(k, tempGO);
				ballNumber[k-1]=k;
			}
			synchronized(lockAll)
			{
				hm.putAll(hmT);
			}
		}		
	}
	public  void initWorldBall(MySurfaceView viewManager)
	{
		ArrayList<GameObject>  tempBall=new ArrayList<GameObject> ();
		synchronized(lockBall)
		{
			tempBall.addAll(alGBall);
		}
		synchronized (lockDelete)//加锁
	    {
			for(GameObject go:tempBall)
			{
				deleteBottles.add(go.body);//添加删除刚体对象
			}
			tempBall.clear();
	    }
		sphereShape=null;
		sphereShape=new SphereShape(BALL_RADISU);//共用的球体形状
		float BALL_X=0.0f;
		float BALL_Y=FLOOR_Y+GUIDAO_HALF_Y*2+BALL_RADISU;
		float BALL_Z=BOWLING_BALL_Z;
		GameObject ball=new GameObject(viewManager,ball_model,BallId,sphereShape,dynamicsWorld,
 	   		BALL_QUALITY,BALL_X,BALL_Y,BALL_Z);
 	   	ball.body.forceActivationState(RigidBody.WANTS_DEACTIVATION);//静止
 	   	ball.body.setLinearVelocity(new Vector3f(0,0,0));//球直线运动的速度--Vx,Vy,Vz三个分量
 	   	ball.body.setAngularVelocity(new Vector3f(0,0,0)); //球自身旋转的速度--绕球自身的x,y,x三轴旋转的速度  
 	   tempBall.add(ball);
 	  synchronized(lockBall)
 	  {
 		 alGBall.clear();
 		 alGBall.addAll(tempBall);
 	  }
	}
	
	@SuppressLint("UseSparseArrays")
	public void initAllObject(MySurfaceView viewManager)
	{
		Map<Integer,GameObject>  hmTemp=new HashMap<Integer,GameObject>();
		synchronized(lockAll)
		{
			hmTemp.putAll(hm);
			hm.clear();
		}
		synchronized(lockDelete){//删除碰撞者
			for(Entry<Integer, GameObject> entry: hmTemp.entrySet()) 
			{
				GameObject go=hmTemp.get(entry.getKey());
				deleteBottles.add(go.body);//删除瓶子刚体
				ballNumber[entry.getKey()-1]=entry.getKey();
			}
		}
		initWorldBottles(viewManager);//创建瓶子
		initWorldBall(viewManager);//创建球
		BALL_SUM=10;//初始化瓶子个数
	}
	
	
	public RigidBody createBody(MySurfaceView mv,float mass,CollisionShape colShape,float cx,float cy,float cz)//创建刚体-----用到物理世界
	{//创建单个刚体---用到物理世界
		boolean isDynamic = (mass != 0f);//物体是否可以运动
		Vector3f localInertia = new Vector3f(0, 0, 0);//惯性向量
		if(isDynamic) //如果物体可以运动
		{
			colShape.calculateLocalInertia(mass, localInertia);//计算惯性
		}
		Transform startTransform = new Transform();//创建刚体的初始变换对象
		startTransform.setIdentity();//变换初始化
		startTransform.origin.set(new Vector3f(cx, cy, cz));//设置初始的位置
		//创建刚体的运动状态对象
		DefaultMotionState myMotionState = new DefaultMotionState(startTransform);
		//创建刚体信息对象
		RigidBodyConstructionInfo rbInfo = new RigidBodyConstructionInfo
									(mass, myMotionState, colShape, localInertia);
		RigidBody body = new RigidBody(rbInfo);//创建刚体
		body.setRestitution(0.1f);//设置反弹系数
		body.setFriction(0.45f);//设置摩擦系数
		
		synchronized(lockAdd)//加锁
		{
			SourceConstant.Bottles.add(body);//添加刚体
		}		
		return body;//返回刚体对象
	}
	
}
