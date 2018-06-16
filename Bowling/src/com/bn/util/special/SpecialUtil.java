package com.bn.util.special;

import java.util.ArrayList;
import java.util.List;

import static com.bn.util.constant.SourceConstant.*;
import com.bn.util.constant.ParticleDataConstant;
import com.bn.util.constant.SourceConstant;
import com.bn.util.manager.ShaderManager;
import com.bn.util.manager.TextureManager;
import com.bn.util.matrix.MatrixState3D;
import com.bn.view.GameView;

public class SpecialUtil
{
	List<ParticleSystem> fps=new ArrayList<ParticleSystem>();//放粒子系统的列表
	ParticleForDraw[] fpfd;//雪花绘制的对象
	GameView gv;
	int count=0;
	int index=0;
	int index2=0;
	public SpecialUtil(GameView gv)
	{
		this.gv=gv;
	}
	public void initSpecial()
	{
		count=ParticleDataConstant.START_COLOR.length;
		fpfd=new ParticleForDraw[count];//4组绘制着，4种颜色
		for(int i=0;i<count;i++)
		{
			ParticleDataConstant.CURR_INDEX=i;
			if(i==0)//火焰
			{
				fpfd[i]=new ParticleForDraw(ParticleDataConstant.RADIS[ParticleDataConstant.CURR_INDEX],
						ShaderManager.getShader(1),TextureManager.getTextures("fire.png"));
				//创建对象,将雪花的初始位置传给构造器
				fps.add(new ParticleSystem(0,0,0,fpfd[i]));
			}else if(i>=1&&i<=3)//碰撞
			{
				fpfd[i]=new ParticleForDraw(ParticleDataConstant.RADIS[ParticleDataConstant.CURR_INDEX],
						ShaderManager.getShader(1),TextureManager.getTextures("stars.png"));
				//创建对象,将雪花的初始位置传给构造器
				fps.add(new ParticleSystem(0,0,0,fpfd[i]));
			}else if(i>3)//烟花
			{
				fpfd[i]=new ParticleForDraw(ParticleDataConstant.RADIS[ParticleDataConstant.CURR_INDEX],
						ShaderManager.getShader(1),TextureManager.getTextures("stars2.png"));
				//创建对象,将雪花的初始位置传给构造器
				fps.add(new ParticleSystem(0,0,0,fpfd[i]));
			}
		}
	}
	
	public void drawSpecial()
	{
		if(isDrawFire)//如果球动
		{
			SourceConstant.particleIndex=0;
			MatrixState3D.pushMatrix();
			fps.get(0).drawSelf();
			MatrixState3D.popMatrix();
		}
		if(isCrashWall)//如果碰撞到墙,播放粒子特效
		{
			SourceConstant.particleIndex=1;
			for(int i=1;i<3;i++)
			{
				MatrixState3D.pushMatrix();
				fps.get(i).positionX=crashBall[0];
				fps.get(i).positionY=crashBall[1];
				fps.get(i).positionZ=crashBall[2];
				fps.get(i).drawSelf();
				MatrixState3D.popMatrix();
			}
			index++;
		}
		if(index>=20)
		{
			isCrashWall=false;
			index=0;
		}
		if(isDrawFireWorks)//绘制游戏结束时的烟花
		{
			SourceConstant.particleIndex=1;
			if(index2<=100)
			{
				MatrixState3D.pushMatrix();
				fps.get(3).positionX=0;
				fps.get(3).positionY=7;
				fps.get(3).positionZ=0;
				fps.get(3).drawSelf();
				MatrixState3D.popMatrix();
			}
			else if(index2>=100&&index2<=200)
			{
				MatrixState3D.pushMatrix();
				fps.get(4).positionX=-5;
				fps.get(4).positionY=6;
				fps.get(4).positionZ=0;
				fps.get(4).drawSelf();
				MatrixState3D.popMatrix();
			}else if(index2>=200&&index2<=300)
			{
				MatrixState3D.pushMatrix();
				fps.get(5).positionX=5;
				fps.get(5).positionY=6;
				fps.get(5).positionZ=0;
				fps.get(5).drawSelf();
				MatrixState3D.popMatrix();
			}else
			{ 
				isDrawFireWorks=false;
				isDrawFireOver=true;
				index2=0;
			}
			index2++;
		}
	}
}
