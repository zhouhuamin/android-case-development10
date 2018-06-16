package com.bn.view;

import java.util.ArrayList;
import java.util.List;
import static com.bn.util.constant.Constant.*;

import javax.microedition.khronos.opengles.GL10;

import com.bn.bowling.MainActivity;
import com.bn.bowling.MySurfaceView;
import com.bn.bowling.R;
import com.bn.object.BN2DObject;
import com.bn.util.constant.Constant;
import com.bn.util.manager.ShaderManager;
import com.bn.util.manager.TextureManager;

import android.media.MediaPlayer;
import android.view.MotionEvent;

public class OptionView extends BNAbstractView{
	
	MySurfaceView mv;
	List<BN2DObject> al=new ArrayList<BN2DObject>();//存放BNObject对象
	Object lock=new Object();
	
	float x;
	float y;
	float SCENE1_X=270;//场景1的x值
	float SCENE2_X=800;//场景2的x值
	float SCENE3_X=1330;//场景3的x值
	
	float NOW1_X=270;//场景1当前的x值
	float NOW2_X=800;//场景2当前的x值
	float NOW3_X=1330;//场景3当前的x值
	
	boolean go=false;
	boolean down=false;
	boolean move=false;
	public OptionView(MySurfaceView mv)
	{
		this.mv=mv;
		initView();
	}
	@Override
	public void initView()
	{
		//场景界面背景
		al.add(new BN2DObject(540,960,1080,1920,TextureManager.getTextures("Back.png"),ShaderManager.getShader(0)));
		al.add(new BN2DObject(540,250,750,160,TextureManager.getTextures("kuang2.png"),ShaderManager.getShader(0)));
		//场景界面返回按钮
		al.add(new BN2DObject(100,1450, 150, 150, TextureManager.getTextures("backto.png"),ShaderManager.getShader(0)));
		//场景界面太空场景
		al.add(new BN2DObject(270,800,500,750,TextureManager.getTextures("space.png"),ShaderManager.getShader(0)));
		//场景界面科幻场景
		al.add(new BN2DObject(800,800,500,750,TextureManager.getTextures("science.png"),ShaderManager.getShader(0)));
		//场景界面沙漠场景
		al.add(new BN2DObject(1330,800,500,730, TextureManager.getTextures("desert.png"),ShaderManager.getShader(0)));
		//场景
		al.add(new BN2DObject(560,260,500,210,TextureManager.getTextures("changjing.png"),ShaderManager.getShader(0)));
		al.add(new BN2DObject(270,1300,400,200,TextureManager.getTextures("taikong.png"),ShaderManager.getShader(0)));
		al.add(new BN2DObject(800,1300,400,200,TextureManager.getTextures("kehuan.png"),ShaderManager.getShader(0)));
		al.add(new BN2DObject(1330,1300,400,200,TextureManager.getTextures("shamo.png"),ShaderManager.getShader(0)));
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent e)
	{
		switch(e.getAction()){
		case MotionEvent.ACTION_DOWN:
			x=Constant.fromRealScreenXToStandardScreenX(e.getX());//将当前屏幕坐标转换为标准屏幕坐标
			y=Constant.fromRealScreenYToStandardScreenY(e.getY());
			down=true;
			if(x>=25&&x<=175&&y>=1375&&y<=1525)
			{//返回上一个界面
				if(!effictOff){
	    			MainActivity.sound.playMusic(Constant.BUTTON_PRESS, 0);
	    		}
				reSetData();
				mv.currView=mv.mainView;
			}else if(x>=(NOW1_X-250)&&x<=(NOW1_X+250)&&y>=425&&y<=1175)
			{//太空
				if(!effictOff){
	    			MainActivity.sound.playMusic(Constant.BUTTON_PRESS, 0);
	    		}
				sceneIndex=1;
			}else if(x>=(NOW2_X-250)&&x<=(NOW2_X+250)&&y>=425&&y<=1175)
			{//科幻
				if(!effictOff){
	    			MainActivity.sound.playMusic(Constant.BUTTON_PRESS, 0);
	    		}
				sceneIndex=2;
			}else if(x>=(NOW3_X-250)&&x<=(NOW3_X+250)&&y>=425&&y<=1175)
			{//沙漠
				if(!effictOff){
	    			MainActivity.sound.playMusic(Constant.BUTTON_PRESS, 0);
	    		}
				sceneIndex=3;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			float mx=Constant.fromRealScreenXToStandardScreenX(e.getX());//将当前屏幕坐标转换为标准屏幕坐标
			if(x!=mx)
			{
				move=true;
				changePic(SCENE1_X-(x-mx),SCENE2_X-(x-mx),SCENE3_X-(x-mx));
				NOW1_X=SCENE1_X-(x-mx);
				NOW2_X=SCENE2_X-(x-mx);
				NOW3_X=SCENE3_X-(x-mx);
			}
			else
			{
				go=true;
			}
			break;
		case MotionEvent.ACTION_UP:
			if(NOW1_X>=270)
			{
				SCENE1_X=270;SCENE2_X=800;SCENE3_X=1330;
				NOW1_X=270;NOW2_X=800;NOW3_X=1330;
				changePic(270,800,1330);
			}else if(NOW3_X<=800)
			{
				SCENE1_X=-260;SCENE2_X=270;SCENE3_X=800;
				NOW1_X=-260;NOW2_X=270;NOW3_X=800;
				changePic(-260,270,800);
			}
			if((down&&go&&!move)||(down&&!move))
			{
				mv.gameView.initSound();
				if(sceneIndex==1)
				{
					mv.currView=mv.gameView;
					//切换到太空场景进行游戏
				}else if(sceneIndex==2)
				{
					mv.currView=mv.gameView;
					//切换到科幻场景进行游戏
				}else
				{
					mv.currView=mv.gameView;
					//切换到沙漠场景进行游戏
				}
			}
			down=false;
			go=false;
			move=false;
			break;
		}
		return true;
	}
	public void changePic(float x1,float x2,float x3)//切换场景图片
	{
		BN2DObject bn1=new BN2DObject(x1,800,500,750,
				TextureManager.getTextures("space.png"),ShaderManager.getShader(0));
		BN2DObject bn2=new BN2DObject(x2,800,500,750,
				TextureManager.getTextures("science.png"),ShaderManager.getShader(0));
		BN2DObject bn3=new BN2DObject(x3,800,500,750,
				TextureManager.getTextures("desert.png"),ShaderManager.getShader(0));
		
		BN2DObject bn4=new BN2DObject(x1,1300,400,200,
				TextureManager.getTextures("taikong.png"),ShaderManager.getShader(0));
		BN2DObject bn5=new BN2DObject(x2,1300,400,200,
				TextureManager.getTextures("kehuan.png"),ShaderManager.getShader(0));
		BN2DObject bn6=new BN2DObject(x3,1300,400,200,
				TextureManager.getTextures("shamo.png"),ShaderManager.getShader(0));
		synchronized(lock)
		{
			al.remove(3);
			al.add(3,bn1);
			al.remove(4);
			al.add(4,bn2);
			al.remove(5);
			al.add(5,bn3);
			al.remove(7);
			al.add(7,bn4);
			al.remove(8);
			al.add(8,bn5);
			al.remove(9);
			al.add(9,bn6);
		}
	}
	@Override
	public void drawView(GL10 gl)
	{
		synchronized(lock)
		{
			for(BN2DObject bo:al)
			{
				bo.drawSelf();
			}
		}
	}
	
	public void initSound()
	{
		//创建音乐
		if(!musicOff)
		{
	 		if(MainActivity.sound.mp!=null)
	 		{											
	 			MainActivity.sound.mp.pause();
	 			MainActivity.sound.mp =null;
			}
 			MainActivity.sound.mp =  MediaPlayer.create(mv.activity, R.raw.beach);
 			MainActivity.sound.mp.setVolume(0.2f, 0.2f);//设置左右声道音量
 			MainActivity.sound.mp.setLooping(true);//循环播放
 			MainActivity.sound.mp.start();
		}
	}
	public void reSetData()
	{
		x=0;
		y=0;
		SCENE1_X=270;//场景1的x值
		SCENE2_X=800;//场景2的x值
		SCENE3_X=1330;//场景3的x值
		
		NOW1_X=270;//场景1当前的x值
		NOW2_X=800;//场景2当前的x值
		NOW3_X=1330;//场景3当前的x值
	}
}
