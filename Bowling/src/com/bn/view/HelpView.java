package com.bn.view;

import static com.bn.util.constant.SourceConstant.*;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import com.bn.bowling.MainActivity;
import com.bn.bowling.MySurfaceView;
import com.bn.object.BN2DObject;
import com.bn.util.constant.Constant;
import com.bn.util.manager.ShaderManager;
import com.bn.util.manager.TextureManager;

import android.opengl.GLES20;
import android.view.MotionEvent;

public class HelpView extends BNAbstractView{
	MySurfaceView mv;
	List<BN2DObject> al=new ArrayList<BN2DObject>();//存放BNObject对象
	float MAX_Y=1250;//边界最大值1700
	float MIN_Y=565;//边界最小值  340
	BN2DObject text=null;
	float START_Y=1250;
	float x,y;
	Object lock=new Object();
	public HelpView(MySurfaceView mv)
	{
		this.mv=mv;
		initView();
	}
	@Override
	public void initView() {
		//中心点以及图片的宽度和高度
		al.add(new BN2DObject(540,960,1080,1920,TextureManager.getTextures("jiemian.png"),ShaderManager.getShader(0)));
		//帮助界面背景图
		al.add(new BN2DObject(540,1000,1080,1600,TextureManager.getTextures("helpback.png"),ShaderManager.getShader(0)));
		//帮助
		al.add(new BN2DObject(540,285, 500,200,TextureManager.getTextures("bangzhu.png"),ShaderManager.getShader(0)));
		//帮助界面返回按钮
		al.add(new BN2DObject(100,1680, 150, 150, TextureManager.getTextures("backto.png"),ShaderManager.getShader(0)));
		//帮助界面文字图片
		text=new BN2DObject(540,1250,1080,1720,TextureManager.getTextures("helptext.png"),ShaderManager.getShader(0));
	}

	float y2=0;
	@Override
	public boolean onTouchEvent(MotionEvent e) {
		switch(e.getAction())
		{
			case MotionEvent.ACTION_DOWN:
				x=Constant.fromRealScreenXToStandardScreenX(e.getX());//将当前屏幕坐标转换为标准屏幕坐标
				y=Constant.fromRealScreenYToStandardScreenY(e.getY());
		    	if(x>=BACK_HELP_LEFT&&x<=BACK_HELP_RIGHT&&y>=BACK_HELP_UP&&y<=BACK_HELP_DOWN)
		    	{//点击单人游戏按钮
		    		if(!Constant.effictOff){
		    			MainActivity.sound.playMusic(Constant.BUTTON_PRESS, 0);
		    		}	
		    		reSetData();
		    		mv.currView=mv.mainView;
		    	}
		    	break;
			case MotionEvent.ACTION_MOVE:
				y2=Constant.fromRealScreenYToStandardScreenY(e.getY());
				if(y2!=y)
				{
					changePic(START_Y-(y-y2));
				}
				break;
			case MotionEvent.ACTION_UP:			
				START_Y=START_Y-(y-y2);				
				if(START_Y>MAX_Y){
					START_Y=MAX_Y;
				}else if(START_Y<MIN_Y)
				{
					START_Y=MIN_Y;
				}
				changePic(START_Y);
				break;
		}
		return true;
	}
	public void changePic(float y)
	{
		synchronized(lock){
			text=new BN2DObject(540,y,1080,1720,TextureManager.getTextures("helptext.png"),ShaderManager.getShader(0));
		}
	}

	@Override
	public void drawView(GL10 gl) {
		for(int i=0;i<al.size();i++)		
		{
			al.get(i).drawSelf();
		}
		//开启剪裁测试
		GLES20.glEnable(GLES20.GL_SCISSOR_TEST);
		GLES20.glScissor(//指定裁剪窗口的左下角坐标(该坐标是以屏幕左下角为原点的)和剪裁窗口的大小
				(int)Constant.fromStandardScreenXToRealScreenX(0), 
				(int)Constant.fromStandardScreenYToRealScreenY(280),
				(int)Constant.fromStandardScreenSizeToRealScreenSize(1080),
				(int)Constant.fromStandardScreenSizeToRealScreenSize(1300)
				);
		synchronized(lock)
		{
			if(text!=null){
				text.drawSelf();//绘制
			}			
		}
		//关闭剪裁测试
		GLES20.glDisable(GLES20.GL_SCISSOR_TEST);
	}
	
	public void reSetData()
	{
		START_Y=1250;
		x=0;
		y=0;
	}
}
