package com.bn.view;

import java.util.ArrayList;
import java.util.List;
import javax.microedition.khronos.opengles.GL10;

import com.bn.bowling.MainActivity;
import com.bn.bowling.MySurfaceView;
import com.bn.bowling.R;
import com.bn.object.BN2DObject;
import com.bn.util.constant.Constant;
import com.bn.util.manager.ShaderManager;
import com.bn.util.manager.TextureManager;
import static com.bn.util.constant.SourceConstant.*;
import static com.bn.util.constant.Constant.*;
import android.media.MediaPlayer;
import android.view.MotionEvent;

public class MainView extends BNAbstractView
{
	MySurfaceView mv;
	List<BN2DObject> al=new ArrayList<BN2DObject>();//存放BNObject对象
	public MainView(MySurfaceView mv)
	{
		this.mv=mv;
		initView();
	}
	@Override
	public void initView()
	{
		//首界面背景图
		al.add(new BN2DObject(540,960,1080,1920,TextureManager.getTextures("jiemian.png"),ShaderManager.getShader(0)));
		//首界面开始游戏按钮
		al.add(new BN2DObject(540,1000, 650,140,TextureManager.getTextures("kuang.png"),ShaderManager.getShader(0)));
		//首界面选择场景按钮
		al.add(new BN2DObject(540,1200, 650,140,TextureManager.getTextures("kuang.png"),ShaderManager.getShader(0)));
		//首界面帮助按钮
		al.add(new BN2DObject(540,1400, 650,140,TextureManager.getTextures("kuang.png"),ShaderManager.getShader(0)));
		//首界面单人游戏
		al.add(new BN2DObject(560,1010, 500,220,TextureManager.getTextures("danrenyouxi.png"),ShaderManager.getShader(0)));
		//首界面场
		al.add(new BN2DObject(540,1200, 440,180,TextureManager.getTextures("changjing.png"),ShaderManager.getShader(0)));
		//首界面帮助
		al.add(new BN2DObject(540,1400, 440,180,TextureManager.getTextures("bangzhu.png"),ShaderManager.getShader(0)));
		
		//创建音乐
		if(!musicOff){
	 		if(MainActivity.sound.mp==null)
	 		{											
	 			MainActivity.sound.mp =  MediaPlayer.create(mv.activity, R.raw.beach);
	 			MainActivity.sound.mp.setVolume(0.2f, 0.2f);//设置左右声道音量
	 			MainActivity.sound.mp.setLooping(true);//循环播放
	 			MainActivity.sound.mp.start();
			}
		}

	}
	@Override
	public boolean onTouchEvent(MotionEvent e) 
	{
		float x=Constant.fromRealScreenXToStandardScreenX(e.getX());//将当前屏幕坐标转换为标准屏幕坐标
		float y=Constant.fromRealScreenYToStandardScreenY(e.getY());
		switch(e.getAction())
		{
		    case MotionEvent.ACTION_UP:
		    	if(x>=BOTTON_LEFT&&x<=BOTTON_RIGHT&&y>=DANREN_GAME_UP&&y<=DANREN_GAME_DOWN)
		    	{//点击单人游戏按钮
		    		if(!effictOff){
		    			MainActivity.sound.playMusic(Constant.BUTTON_PRESS, 0);
		    		}
		    		sceneIndex=(int)(Math.random()*2+1);
		    		mv.gameView.initSound();
		    		mv.currView=mv.gameView;
		    	}else if(x>=BOTTON_LEFT&&x<=BOTTON_RIGHT&&y>=OPTION_UP&&y<=DOPTION_DOWN)
		    	{//点击场景按钮
		    		if(!effictOff){
		    			MainActivity.sound.playMusic(Constant.BUTTON_PRESS, 0);
		    		}
		    		mv.currView=mv.optionView;
		    	}else if(x>=BOTTON_LEFT&&x<=BOTTON_RIGHT&&y>=HELP_UP&&y<=HELP_DOWN)
		    	{//点击帮助按钮
		    		if(!effictOff){
		    			MainActivity.sound.playMusic(Constant.BUTTON_PRESS, 0);
		    		}
		    		mv.helpView.initView();
		    		mv.currView=mv.helpView;
		    	}
			break;
		}
		return true;
	}
	@Override
	public void drawView(GL10 gl)//绘制
	{
		for(BN2DObject bo:al)
		{
			bo.drawSelf();
		}
	}
}
