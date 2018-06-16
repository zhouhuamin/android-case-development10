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
	public LoadedObjectVertexNormalTexture lovnt;//��������ƿ��
	int texId;//����id
	public RigidBody body;//��Ӧ�ĸ������
    MySurfaceView mv;
	//������Ĺ���������=================start====================
	public GameObject(MySurfaceView mv,
			LoadedObjectVertexNormalTexture lovnt,
			int texId,CollisionShape[] colShape,
			DiscreteDynamicsWorld dynamicsWorld,
			float r,float halfWidth,float halfHeight,
			float rCapsule,float heightCapsule,
			float mass,float cx,float cy,float cz){
		CompoundShape comShape=new CompoundShape(); //���������״
		Transform localTransform = new Transform();//�����任����
		localTransform.setIdentity();//��ʼ���任
		localTransform.origin.set(new Vector3f(0, -(halfHeight+r*2), 0));//���ñ任�����
		comShape.addChildShape(localTransform, colShape[0]);//�������״----����
		
		localTransform.setIdentity();//��ʼ���任
		localTransform.origin.set(new Vector3f(0, -r, 0));//���ñ任�����1.403
		comShape.addChildShape(localTransform, colShape[1]);//�������״---��
		 
		localTransform.setIdentity();//��ʼ���任
		localTransform.origin.set(new Vector3f(0, (heightCapsule/2+rCapsule), 0));//���ñ任�����2.003f
		comShape.addChildShape(localTransform, colShape[2]);//�������״---������
		
		boolean isDynamic = (mass != 0f);//�жϸ����Ƿ���˶�
		Vector3f localInertia = new Vector3f(0, 0, 0);//������������
		if (isDynamic) //�����������˶�
		{
			comShape.calculateLocalInertia(mass, localInertia);//�������Ĺ���
		}
		Transform startTransform = new Transform();//��������ĳ�ʼ�任����
		startTransform.setIdentity();//��ʼ���任����
		startTransform.origin.set(new Vector3f(cx, cy, cz));//���ñ任�����
		DefaultMotionState myMotionState = new DefaultMotionState(startTransform);//����������˶�״̬����
		RigidBodyConstructionInfo rbInfo = new RigidBodyConstructionInfo//����������Ϣ����
		(
				mass, myMotionState, comShape, localInertia
		);
		body = new RigidBody(rbInfo);//�����������
		body.setRestitution(0.05f);//���ûָ�ϵ��
		body.setFriction(0.93f);//����Ħ��ϵ��  0.95
		body.setAngularVelocity(new Vector3f(0,0,0));
		synchronized (lockAdd)//����
		{
			SourceConstant.Bottles.add(body);//��Ӹ���
		}
		this.mv=mv;
		this.lovnt=lovnt;
		this.texId=texId;
	}
	//������Ĺ���������=================end====================
	
	public GameObject(
			MySurfaceView mv,
			LoadedObjectVertexNormalTexture lovnt,
			int texId,CollisionShape colShape,
			DiscreteDynamicsWorld dynamicsWorld,
			float mass,float cx,float cy,float cz)
	{
		boolean isDynamic = (mass != 0f);//�жϸ����Ƿ���˶�
		Vector3f localInertia = new Vector3f(0, 0, 0);//������������
		if (isDynamic) //�����������˶�
		{
			colShape.calculateLocalInertia(mass, localInertia);//�������Ĺ���
		}
		Transform startTransform = new Transform();//��������ĳ�ʼ�任����
		startTransform.setIdentity();//��ʼ���任����
		startTransform.origin.set(new Vector3f(cx, cy, cz));//���ñ任�����
		DefaultMotionState myMotionState = new DefaultMotionState(startTransform);//����������˶�״̬����
		RigidBodyConstructionInfo rbInfo = new RigidBodyConstructionInfo//����������Ϣ����
		(
				mass, myMotionState, colShape, localInertia
		);
		body = new RigidBody(rbInfo);//�����������
		body.setRestitution(0.03f);//���÷���ϵ��
		body.setFriction(0.4f); //����Ħ��ϵ��//Ħ��ϵ��ԽС���������Խ��������Խ��
		synchronized(lockAdd)
		{
			SourceConstant.Bottles.add(body);
		}
		this.mv=mv;
		this.lovnt=lovnt;
		this.texId=texId;
	}
	public void drawSelf(int isShadow,float shadowPosition)
	{//����ƿ��
		MatrixState3D.pushMatrix();//�����ֳ�
		MatrixState3D.translate(0,-(BOX_HALF_HEIGHT*2+BOTTOM_BALL_RADIUS*2), 0);
		if(lovnt!=null)
		{
			lovnt.drawSelf(texId,isShadow,shadowPosition);//��������
		}
		MatrixState3D.popMatrix();//�ָ��ֳ�
	}
	
	public void drawSelfBall(int isShadow,float shadowPosition)
	{
		MatrixState3D.pushMatrix();//�����ֳ�
		if(lovnt!=null)
		{
			lovnt.drawSelf(texId,isShadow,shadowPosition);//��������			
		}
		MatrixState3D.popMatrix();//�ָ��ֳ�
	}
}
