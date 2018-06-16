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
	//��ʼ����������ķ���
	public void initWorld()
	{
		//������ײ���������Ϣ����
		CollisionConfiguration collisionConfiguration = new DefaultCollisionConfiguration();		
		//������ײ����㷨�����߶����书��Ϊɨ�����е���ײ���ԣ���ȷ�����õļ����Զ�Ӧ���㷨
		CollisionDispatcher dispatcher = new CollisionDispatcher(collisionConfiguration);		
		//����������������ı߽���Ϣ
		Vector3f worldAabbMin = new Vector3f(-10000, -10000, -10000);
		Vector3f worldAabbMax = new Vector3f(10000, 10000, 10000);
		int maxProxies = 1024;
		//������ײ���ֲ�׶εļ����㷨����
		AxisSweep3 overlappingPairCache =new AxisSweep3(worldAabbMin, worldAabbMax, maxProxies);
		//�����ƶ�Լ������߶���
		SequentialImpulseConstraintSolver solver = new SequentialImpulseConstraintSolver();
		//���������������
		dynamicsWorld = new DiscreteDynamicsWorld(dispatcher, overlappingPairCache, solver,collisionConfiguration);
		//�����������ٶ�
		dynamicsWorld.setGravity(new Vector3f(0, -10, 0));
		capsuleShape=new CapsuleShape(CAPSULE_RADIUS,CAPSULE_HEIGHT);//����塪��������״0.28
		sphereShape=new SphereShape(BOTTOM_BALL_RADIUS);//����塪��������״
		boxShape=new BoxShape(new Vector3f(BOX_HALF_WIDTH,BOX_HALF_HEIGHT,BOX_HALF_WIDTH));//����塪������-��߳�����ߺͰ�߿�
		planeShape=new StaticPlaneShape(new Vector3f(0, 1, 0), 0);//�������õ�ƽ����״
		
		capsuleShape2=new CapsuleShapeZ(CAPSULE_RADIUS2,CAPSULE_HEIGHT2);//���õĽ�����״0.28
		boxShape2=new BoxShape(new Vector3f(WANG_HALF_X,WANG_HALF_Y,WANG_HALF_Z));//��
		boxShape3=new BoxShape(new Vector3f(GUIDAO_HALF_X,GUIDAO_HALF_Y,GUIDAO_HALF_Z));//̨��
		
		//�������õ���״����
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
			{//����ʮ��ƿ��
				tempGO=new GameObject(
						viewManager,
						bottle_model,BottleId,
						csa,dynamicsWorld,
						BOTTOM_BALL_RADIUS,//����������İ뾶
						BOX_HALF_WIDTH,BOX_HALF_HEIGHT,//�������糤������ӵĳߴ�--��߳������
						CAPSULE_RADIUS,CAPSULE_HEIGHT,//�������罺�ҵĳߴ�
						BOTTLE_QUALITY,
						BATTLE_ORIGN_X*i+j*BATTLE_SPAN_X,
						FLOOR_Y+GUIDAO_HALF_Y*2+BOTTOM_BALL_RADIUS*2f+BOX_HALF_HEIGHT*2,
						BATTLE_ORIGN_Z-BATTLE_SPAN_Z*i);//����ƿ������Ļ��ƶ��� 
				tempGO.body.forceActivationState(RigidBody.WANTS_DEACTIVATION);//��ֹ
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
		synchronized (lockDelete)//����
	    {
			for(GameObject go:tempBall)
			{
				deleteBottles.add(go.body);//���ɾ���������
			}
			tempBall.clear();
	    }
		sphereShape=null;
		sphereShape=new SphereShape(BALL_RADISU);//���õ�������״
		float BALL_X=0.0f;
		float BALL_Y=FLOOR_Y+GUIDAO_HALF_Y*2+BALL_RADISU;
		float BALL_Z=BOWLING_BALL_Z;
		GameObject ball=new GameObject(viewManager,ball_model,BallId,sphereShape,dynamicsWorld,
 	   		BALL_QUALITY,BALL_X,BALL_Y,BALL_Z);
 	   	ball.body.forceActivationState(RigidBody.WANTS_DEACTIVATION);//��ֹ
 	   	ball.body.setLinearVelocity(new Vector3f(0,0,0));//��ֱ���˶����ٶ�--Vx,Vy,Vz��������
 	   	ball.body.setAngularVelocity(new Vector3f(0,0,0)); //��������ת���ٶ�--���������x,y,x������ת���ٶ�  
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
		synchronized(lockDelete){//ɾ����ײ��
			for(Entry<Integer, GameObject> entry: hmTemp.entrySet()) 
			{
				GameObject go=hmTemp.get(entry.getKey());
				deleteBottles.add(go.body);//ɾ��ƿ�Ӹ���
				ballNumber[entry.getKey()-1]=entry.getKey();
			}
		}
		initWorldBottles(viewManager);//����ƿ��
		initWorldBall(viewManager);//������
		BALL_SUM=10;//��ʼ��ƿ�Ӹ���
	}
	
	
	public RigidBody createBody(MySurfaceView mv,float mass,CollisionShape colShape,float cx,float cy,float cz)//��������-----�õ���������
	{//������������---�õ���������
		boolean isDynamic = (mass != 0f);//�����Ƿ�����˶�
		Vector3f localInertia = new Vector3f(0, 0, 0);//��������
		if(isDynamic) //�����������˶�
		{
			colShape.calculateLocalInertia(mass, localInertia);//�������
		}
		Transform startTransform = new Transform();//��������ĳ�ʼ�任����
		startTransform.setIdentity();//�任��ʼ��
		startTransform.origin.set(new Vector3f(cx, cy, cz));//���ó�ʼ��λ��
		//����������˶�״̬����
		DefaultMotionState myMotionState = new DefaultMotionState(startTransform);
		//����������Ϣ����
		RigidBodyConstructionInfo rbInfo = new RigidBodyConstructionInfo
									(mass, myMotionState, colShape, localInertia);
		RigidBody body = new RigidBody(rbInfo);//��������
		body.setRestitution(0.1f);//���÷���ϵ��
		body.setFriction(0.45f);//����Ħ��ϵ��
		
		synchronized(lockAdd)//����
		{
			SourceConstant.Bottles.add(body);//��Ӹ���
		}		
		return body;//���ظ������
	}
	
}
