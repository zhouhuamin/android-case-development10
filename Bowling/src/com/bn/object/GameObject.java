package com.bn.object;

import javax.vecmath.Vector3f;
import static com.bn.util.constant.Constant.*;
import com.bn.bowling.MySurfaceView;
import com.bn.util.constant.SourceConstant;
import com.bn.util.matrix.MatrixState3D;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.CompoundShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;

public class GameObject
{
	public LoadedObjectVertexNormalTexture lovnt;//保龄球还是瓶子
	int texId;//纹理id
	public RigidBody body;//对应的刚体对象
    MySurfaceView mv;
	//复合体的构造器方法=================start====================
	public GameObject(MySurfaceView mv,
			LoadedObjectVertexNormalTexture lovnt,
			int texId,CollisionShape[] colShape,
			DiscreteDynamicsWorld dynamicsWorld,
			float r,float halfWidth,float halfHeight,
			float rCapsule,float heightCapsule,
			float mass,float cx,float cy,float cz){
		CompoundShape comShape=new CompoundShape(); //创建组合形状
		Transform localTransform = new Transform();//创建变换对象
		localTransform.setIdentity();//初始化变换
		localTransform.origin.set(new Vector3f(0, -(halfHeight+r*2), 0));//设置变换的起点
		comShape.addChildShape(localTransform, colShape[0]);//添加子形状----胶囊
		
		localTransform.setIdentity();//初始化变换
		localTransform.origin.set(new Vector3f(0, -r, 0));//设置变换的起点1.403
		comShape.addChildShape(localTransform, colShape[1]);//添加子形状---球
		 
		localTransform.setIdentity();//初始化变换
		localTransform.origin.set(new Vector3f(0, (heightCapsule/2+rCapsule), 0));//设置变换的起点2.003f
		comShape.addChildShape(localTransform, colShape[2]);//添加子形状---立方体
		
		boolean isDynamic = (mass != 0f);//判断刚体是否可运动
		Vector3f localInertia = new Vector3f(0, 0, 0);//创建惯性向量
		if (isDynamic) //如果刚体可以运动
		{
			comShape.calculateLocalInertia(mass, localInertia);//计算刚体的惯性
		}
		Transform startTransform = new Transform();//创建刚体的初始变换对象
		startTransform.setIdentity();//初始化变换对象
		startTransform.origin.set(new Vector3f(cx, cy, cz));//设置变换的起点
		DefaultMotionState myMotionState = new DefaultMotionState(startTransform);//创建刚体的运动状态对象
		RigidBodyConstructionInfo rbInfo = new RigidBodyConstructionInfo//创建刚体信息对象
		(
				mass, myMotionState, comShape, localInertia
		);
		body = new RigidBody(rbInfo);//创建刚体对象
		body.setRestitution(0.05f);//设置恢复系数
		body.setFriction(0.93f);//设置摩擦系数  0.95
		body.setAngularVelocity(new Vector3f(0,0,0));
		synchronized (lockAdd)//加锁
		{
			SourceConstant.Bottles.add(body);//添加刚体
		}
		this.mv=mv;
		this.lovnt=lovnt;
		this.texId=texId;
	}
	//复合体的构造器方法=================end====================
	
	public GameObject(
			MySurfaceView mv,
			LoadedObjectVertexNormalTexture lovnt,
			int texId,CollisionShape colShape,
			DiscreteDynamicsWorld dynamicsWorld,
			float mass,float cx,float cy,float cz)
	{
		boolean isDynamic = (mass != 0f);//判断刚体是否可运动
		Vector3f localInertia = new Vector3f(0, 0, 0);//创建惯性向量
		if (isDynamic) //如果刚体可以运动
		{
			colShape.calculateLocalInertia(mass, localInertia);//计算刚体的惯性
		}
		Transform startTransform = new Transform();//创建刚体的初始变换对象
		startTransform.setIdentity();//初始化变换对象
		startTransform.origin.set(new Vector3f(cx, cy, cz));//设置变换的起点
		DefaultMotionState myMotionState = new DefaultMotionState(startTransform);//创建刚体的运动状态对象
		RigidBodyConstructionInfo rbInfo = new RigidBodyConstructionInfo//创建刚体信息对象
		(
				mass, myMotionState, colShape, localInertia
		);
		body = new RigidBody(rbInfo);//创建刚体对象
		body.setRestitution(0.03f);//设置反弹系数
		body.setFriction(0.4f); //设置摩擦系数//摩擦系数越小，球滚动的越慢；否则越快
		synchronized(lockAdd)
		{
			SourceConstant.Bottles.add(body);
		}
		this.mv=mv;
		this.lovnt=lovnt;
		this.texId=texId;
	}
	public void drawSelf(int isShadow,float shadowPosition)
	{//绘制瓶子
		MatrixState3D.pushMatrix();//保护现场
		MatrixState3D.translate(0,-(BOX_HALF_HEIGHT*2+BOTTOM_BALL_RADIUS*2), 0);
		if(lovnt!=null)
		{
			lovnt.drawSelf(texId,isShadow,shadowPosition);//绘制物体
		}
		MatrixState3D.popMatrix();//恢复现场
	}
	
	public void drawSelfBall(int isShadow,float shadowPosition)
	{
		MatrixState3D.pushMatrix();//保护现场
		if(lovnt!=null)
		{
			lovnt.drawSelf(texId,isShadow,shadowPosition);//绘制物体			
		}
		MatrixState3D.popMatrix();//恢复现场
	}
}
