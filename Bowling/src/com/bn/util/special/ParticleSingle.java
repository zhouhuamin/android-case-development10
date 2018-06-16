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
    	//粒子进行移动的方法，同时岁数增大的方法
    	if(SourceConstant.particleIndex==0)//火焰
    	{
    		x1=x1+vx1;
    		y1=y1+vy1;
    	}else if(SourceConstant.particleIndex==1)//碰撞
    	{
    		x2=vx2*lifeSpan;
        	z2=vz2*lifeSpan;
        	y2=(vy2*lifeSpan-0.5f*lifeSpan*lifeSpan*1.0f);
    	}
    	lifeSpan+=lifeSpanStep;
    }
    
    public void drawSelf(float[] startColor,float[] endColor,float maxLifeSpan)
    {
    	MatrixState3D.pushMatrix();//保护现场
    	if(SourceConstant.particleIndex==0)
    	{
    		MatrixState3D.translate(x1, 0, y1);
    	}else if(SourceConstant.particleIndex==1)
    	{
    		MatrixState3D.translate(x2, y2, z2);
    	}
    	float sj=(maxLifeSpan-lifeSpan)/maxLifeSpan;//衰减因子在逐渐的变小，最后变为0
    	fpfd.drawSelf(sj,startColor,endColor);//绘制单个粒子   	
    	MatrixState3D.popMatrix();//恢复现场
    }
}
