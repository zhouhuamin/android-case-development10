package com.bn.util.special;

import com.bn.util.constant.SourceConstant;
import com.bn.util.matrix.MatrixState3D;

public class ParticleSingle 
{    
    public float x1;
    public float y1;
    
    public float x2;
    public float y2;
    public float z2;
    
    public float vx1;
    public float vy1;
    
    public float vx2;
    public float vy2;
    public float vz2;
    public float lifeSpan;
    
    ParticleForDraw fpfd;
    
    public ParticleSingle(float vx,float vy,float vz,ParticleForDraw fpfd)
    {
    	this.x2=0;
    	this.y2=0;
    	this.z2=0;
    	this.vx2=vx;
    	this.vy2=vy;
    	this.vz2=vz;
    	this.fpfd=fpfd;
    }
    
    public ParticleSingle(float x,float y,float vx,float vy,ParticleForDraw fpfd)
    {
    	this.x1=x;
    	this.y1=y;
    	this.vx1=vx;
    	this.vy1=vy;
    	this.fpfd=fpfd;
    }
    
    
    public void go(float lifeSpanStep)
    {
    	//���ӽ����ƶ��ķ�����ͬʱ��������ķ���
    	if(SourceConstant.particleIndex==0)//����
    	{
    		x1=x1+vx1;
    		y1=y1+vy1;
    	}else if(SourceConstant.particleIndex==1)//��ײ
    	{
    		x2=vx2*lifeSpan;
        	z2=vz2*lifeSpan;
        	y2=(vy2*lifeSpan-0.5f*lifeSpan*lifeSpan*1.0f);
    	}
    	lifeSpan+=lifeSpanStep;
    }
    
    public void drawSelf(float[] startColor,float[] endColor,float maxLifeSpan)
    {
    	MatrixState3D.pushMatrix();//�����ֳ�
    	if(SourceConstant.particleIndex==0)
    	{
    		MatrixState3D.translate(x1, 0, y1);
    	}else if(SourceConstant.particleIndex==1)
    	{
    		MatrixState3D.translate(x2, y2, z2);
    	}
    	float sj=(maxLifeSpan-lifeSpan)/maxLifeSpan;//˥���������𽥵ı�С������Ϊ0
    	fpfd.drawSelf(sj,startColor,endColor);//���Ƶ�������   	
    	MatrixState3D.popMatrix();//�ָ��ֳ�
    }
}
