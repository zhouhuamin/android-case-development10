package com.bn.view;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import com.bn.bowling.MainActivity;
import com.bn.bowling.MySurfaceView;
import com.bn.object.BN2DObject;

import com.bn.util.constant.Constant;
import com.bn.util.constant.MyHHData;

import static com.bn.util.constant.Constant.countTurn;
import static com.bn.util.constant.Constant.effictOff;
import static com.bn.util.constant.SourceConstant.*;
import com.bn.util.manager.ShaderManager;
import com.bn.util.manager.TextureManager;

import android.view.MotionEvent;

public class SelectQiuView extends BNAbstractView
{
	MySurfaceView mv;
	List<BN2DObject> selectView=new ArrayList<BN2DObject>();//选球界面元素
	BN2DObject  bn;//选中按钮
	Object lock=new Object();
	public boolean isball1=false;
	public boolean isball2=false;
	public boolean isball3=false;
	public boolean isball4=false;
	public boolean isball5=false;
	public boolean isball6=false;
	public SelectQiuView(MySurfaceView mv)
	{
		this.mv=mv;
		initView();
	}
	public void initView() 
	{
		selectView=MyHHData.selectqiuView();
		 bn=new BN2DObject(240,600,150,150,TextureManager.getTextures("gou2.png"),
					ShaderManager.getShader(0));//默认为第一个球的纹理
		 
	}
	public boolean onTouchEvent(MotionEvent e) {
		float x=Constant.fromRealScreenXToStandardScreenX(e.getX());//将当前屏幕坐标转换为标准屏幕坐标
		float y=Constant.fromRealScreenYToStandardScreenY(e.getY());
		switch (e.getAction()){
		case MotionEvent.ACTION_DOWN:
			if(x>Selectqiuback_Left&&x<Selectqiuback_Right&&
					y>Selectqiuback_Top&&y<Selectqiuback_Bottom)
			{   //返回主界面
				mv.currView=mv.gameView;
				isPause=false;
			}
			if(countTurn==10){
				break;
			}
			if(x>Selectqiu1_Left&&x<Selectqiu1_Right&&
					y>Selectqiu1_Top&&y<Selectqiu1_Bottom)
			{   isball1=true;
			    if(isball1)
			    {
			    	 bn=new BN2DObject(240,600,150,150,
			    		TextureManager.getTextures("gou2.png"),
						ShaderManager.getShader(0));       
					  if(!effictOff){
			    			MainActivity.sound.playMusic(Constant.BUTTON_PRESS, 0);
			    		}
			    }
			
			}else if(x>Selectqiu2_Left&&x<Selectqiu2_Right&&
					y>Selectqiu2_Top&&y<Selectqiu2_Bottom)
			{
				isball2=true;	  
				if(isball2)
			    {
				  bn=new BN2DObject(580,600,150,150,
						  TextureManager.getTextures("gou2.png"),
							ShaderManager.getShader(0));
				  if(!effictOff){
		    			MainActivity.sound.playMusic(Constant.BUTTON_PRESS, 0);
		    		}
			    }
			}else if(x>Selectqiu3_Left&&x<Selectqiu3_Right&&
					y>Selectqiu3_Top&&y<Selectqiu3_Bottom)
			{
				isball3=true;
				  
				if(isball3)
			    {
				  bn=new BN2DObject(920,580,150,150,
						  TextureManager.getTextures("gou2.png"),
							ShaderManager.getShader(0));
				  if(!effictOff){
		    			MainActivity.sound.playMusic(Constant.BUTTON_PRESS, 0);
		    		}
			    }
			}
			else if(x>Selectqiu4_Left&&x<Selectqiu4_Right&&
					y>Selectqiu4_Top&&y<Selectqiu4_Bottom)
			{
				isball4=true;
				  
				  if(isball4)
				  {
					  bn=new BN2DObject(240,920,150,150,  
							  TextureManager.getTextures("gou2.png"),
								ShaderManager.getShader(0));
					  if(!effictOff){
			    			MainActivity.sound.playMusic(Constant.BUTTON_PRESS, 0);
			    		}
				  }
			}else if(x>Selectqiu5_Left&&x<Selectqiu5_Right&&
					y>Selectqiu5_Top&&y<Selectqiu5_Bottom)
			{
				isball5=true;
				  
				  if(isball5)
				 {
					  bn=new BN2DObject(600,920,150,150,  
							TextureManager.getTextures("gou2.png"),
								ShaderManager.getShader(0));
					  if(!effictOff){
			    			MainActivity.sound.playMusic(Constant.BUTTON_PRESS, 0);
			    		}
				   }
			}else if(x>Selectqiu6_Left&&x<Selectqiu6_Right&&
					y>Selectqiu6_Top&&y<Selectqiu6_Bottom)
			{
				isball6=true;
				  
				  if(isball6)
				  {
					  bn=new BN2DObject(900,920,150,150, 
							 TextureManager.getTextures("gou2.png"),
								ShaderManager.getShader(0));
					  if(!effictOff){
			    			MainActivity.sound.playMusic(Constant.BUTTON_PRESS, 0);
			    		}
				  }
			}
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_UP:
			if(countTurn==10){
				return false;
			}
			if(isball1&&x>Selectqiu1_Left&&x<Selectqiu1_Right&&
					y>Selectqiu1_Top&&y<Selectqiu1_Bottom)
			{   
				BallId=TextureManager.getTextures("ball1.png");
				
			}else if(isball2&&x>Selectqiu2_Left&&x<Selectqiu2_Right&&
					y>Selectqiu2_Top&&y<Selectqiu2_Bottom)
			{
				BallId=TextureManager.getTextures("ball2.png");
			}else if(isball3&&x>Selectqiu3_Left&&x<Selectqiu3_Right&&
					y>Selectqiu3_Top&&y<Selectqiu3_Bottom)
			{
				BallId=TextureManager.getTextures("ball3.png");
			}else if(isball4&&x>Selectqiu4_Left&&x<Selectqiu4_Right&&
					y>Selectqiu4_Top&&y<Selectqiu4_Bottom)
			{
				BallId=TextureManager.getTextures("ball4.png");	
			}else if(isball5&&x>Selectqiu5_Left&&x<Selectqiu5_Right&&
					y>Selectqiu5_Top&&y<Selectqiu5_Bottom)
			{
				BallId=TextureManager.getTextures("ball5.png");
			}else if(isball6&&x>Selectqiu6_Left&&x<Selectqiu6_Right&&
					y>Selectqiu6_Top&&y<Selectqiu6_Bottom)
			{
				BallId=TextureManager.getTextures("ball6.png");
			}
			break;
		}
		return true;
	}
	@Override
	public void drawView(GL10 gl) {
		synchronized(lock)
		{
			for(BN2DObject pause:selectView)
			{
				pause.drawSelf();
			}
		}
		bn.drawSelf();
	}
}
