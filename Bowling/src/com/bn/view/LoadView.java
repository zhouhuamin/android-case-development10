package com.bn.view;

import static com.bn.util.constant.Constant.musicOff;
import static com.bn.util.constant.SourceConstant.*;

import java.util.ArrayList;
import java.util.List;
import javax.microedition.khronos.opengles.GL10;

import com.bn.bowling.MainActivity;
import com.bn.bowling.MySurfaceView;
import com.bn.object.BN2DObject;
import com.bn.object.Earth;
import com.bn.object.Moon;
import com.bn.object.SkyCloud;
import com.bn.util.manager.ShaderManager;
import com.bn.util.manager.TextureManager;
import com.bn.util.tool.LoadUtil;
import android.view.MotionEvent;

public class LoadView extends BNAbstractView{
	MySurfaceView mv;
	List<BN2DObject> al=new ArrayList<BN2DObject>();//存放BNObject对象
	List<BN2DObject> al2=new ArrayList<BN2DObject>();//存放BNObject对象
	int initIndex=0;
	int index=0;
	public LoadView(MySurfaceView mv)
	{
		this.mv=mv;
		initView();
	}
	@Override
	public void initView() {
		TextureManager.loadingTexture(this.mv, 32, 18);//游戏加载界面图片资源
		al2.add(new BN2DObject(540,960,1124,1124,TextureManager.getTextures("bowling1.png"),ShaderManager.getShader(0))) ;
		al2.add(new BN2DObject(540,1690,1300,60,TextureManager.getTextures("lu.png"),ShaderManager.getShader(0))) ;
		al.add(new BN2DObject(540,1580,600,200,TextureManager.getTextures("load0.png"),ShaderManager.getShader(0)));
		al.add(new BN2DObject(540,1580,600,200,TextureManager.getTextures("load1.png"),ShaderManager.getShader(0)));
		al.add(new BN2DObject(540,1580,600,200,TextureManager.getTextures("load2.png"),ShaderManager.getShader(0)));
		al.add(new BN2DObject(540,1580,600,200,TextureManager.getTextures("load3.png"),ShaderManager.getShader(0)));
		al.add(new BN2DObject(540,1580,600,200,TextureManager.getTextures("load4.png"),ShaderManager.getShader(0)));
		al.add(new BN2DObject(540,1580,600,200,TextureManager.getTextures("load5.png"),ShaderManager.getShader(0)));
		al.add(new BN2DObject(540,1580,600,200,TextureManager.getTextures("load6.png"),ShaderManager.getShader(0)));
		al.add(new BN2DObject(540,1580,600,200,TextureManager.getTextures("load7.png"),ShaderManager.getShader(0)));
		al.add(new BN2DObject(540,1580,600,200,TextureManager.getTextures("load8.png"),ShaderManager.getShader(0)));
		al.add(new BN2DObject(540,1580,600,200,TextureManager.getTextures("load9.png"),ShaderManager.getShader(0)));
		al.add(new BN2DObject(540,1580,600,200,TextureManager.getTextures("load10.png"),ShaderManager.getShader(0)));
		al.add(new BN2DObject(540,1580,600,200,TextureManager.getTextures("load11.png"),ShaderManager.getShader(0)));
		al.add(new BN2DObject(540,1580,600,200,TextureManager.getTextures("load12.png"),ShaderManager.getShader(0)));
		al.add(new BN2DObject(540,1580,600,200,TextureManager.getTextures("load13.png"),ShaderManager.getShader(0)));
		al.add(new BN2DObject(540,1580,600,200,TextureManager.getTextures("load14.png"),ShaderManager.getShader(0)));
		al.add(new BN2DObject(540,1580,600,200,TextureManager.getTextures("load15.png"),ShaderManager.getShader(0)));
	}

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		return false;
	}
	
	public void initBNView(int index)
	{
		switch(index)
		{
			case 0:
				TextureManager.loadingTexture(this.mv, 0, 5);//主界面图片资源
				break;
			case 1:
				TextureManager.loadingTexture(this.mv, 5, 9);//场景界面图片资源
				break;
			case 2:
				TextureManager.loadingTexture(this.mv, 14, 16);//游戏界面图片资源
				break;
			case 3:
				TextureManager.loadingTexture(this.mv, 30, 2);//游戏帮助图片资源
				break;
			case 5:
				TextureManager.loadingTexture(this.mv, 50, 6);//天空场景图片资源
				break;
			case 6:
				TextureManager.loadingTexture(this.mv, 56, 8);//天空场景图片资源
				break;
			case 7:
				TextureManager.loadingTexture(this.mv, 64, 6);//暂停图片资源
				break;
			case 8:
				TextureManager.loadingTexture(this.mv, 70, 7);//选瓶子图片资源
				break;
			case 9:
				TextureManager.loadingTexture(this.mv, 77, 12);//选保龄球图片资源
				break;
			case 10:
				TextureManager.loadingTexture(this.mv, 89, 14);//半透明轨道
				break;
			case 11:
				TextureManager.loadingTexture(this.mv, 103, 5);//半透明轨道
				break;
			case 12:
				TextureManager.loadingTexture(this.mv, 108, 31);
				break;
			case 13:
				if(mv.gameView==null){
					mv.gameView=new GameView(this.mv);	
				}		
				break;
			case 14://创建GameView引用
				ball_model=LoadUtil.loadFromFile("ball.obj",this.mv.getResources(), mv);//初始化球的模型数据
				break;
			case 30://初始化游戏界面
				bottle_model=LoadUtil.loadFromFile("pingzi2.obj", mv.getResources(), mv);//初始化瓶子的模型数据
				break;
			case 50://加载科技馆模型
				kjg_model=LoadUtil.loadFromFile("kjg.obj", mv.getResources(), mv);//初始化科技馆的模型数据
				break;
			case 70://加载轨道模型			
				gd_model=LoadUtil.loadFromFile("guidao.obj", mv.getResources(), mv);//初始化轨道的模型数据
				break;
			case 90:
				tree2_model=LoadUtil.loadFromFile("tree2.obj",mv.getResources(),mv);//沙漠树
				break;
			case 110:
				tree3_model=LoadUtil.loadFromFile("tree3.obj", mv.getResources(),mv);//沙漠树
	            break;
			case 130:
				 smgd_model=LoadUtil.loadFromFile("smguidao.obj",mv.getResources(),mv);//沙漠轨道
				  break;
			case 150:
				smys_model=LoadUtil.loadFromFile("yanshi.obj", mv.getResources(),mv);//沙漠岩石
	            break;
			case 170:
				sky_model=LoadUtil.loadFromFile("sky.obj", mv.getResources(),mv);
				break;
			case 210:
				 skygd_model2=LoadUtil.loadFromFile("xkguidao.obj", mv.getResources(),mv);
				break;
			case 230:
				feixingwu1_model=LoadUtil.loadFromFile("feixingwu1.obj", mv.getResources(),mv);
				 break;
			case 250:
				  feixingwu2_model=LoadUtil.loadFromFile("feixingwu2.obj", mv.getResources(),mv);
				 break;
			case 270:
				 feixingwu3_model=LoadUtil.loadFromFile("feixingwu3.obj",mv.getResources(),mv);
				  break;
			case 290:
				if(mv.mainView==null){
					mv.mainView=new MainView(mv);
				}				
				 break;
			case 291:
				if(mv.SelcetpzView==null){
					mv.SelcetpzView=new SelectPZView(mv);
				}
				break;
			case 292:
				if(mv.SelcetqiuView==null){
					mv.SelcetqiuView=new SelectQiuView(mv);
				}				
				break;
			case 300:
				if(mv.optionView==null){
					mv.optionView=new OptionView(mv);
				}				
				break;
			case 301:
				if(mv.helpView==null){
					mv.helpView=new HelpView(mv);
				}				
				break;
			case 302:
				 earth=new Earth(mv,2.0f);
				break;
			case 303:
	            moon=new Moon(mv,0.5f);
	            break;
			case 304:
	            xing1=new Moon(mv,2.0f);
	            break;
			case 305:
		        cloud=new SkyCloud(mv);
		        break;
			case 306:
	            dimian_model=LoadUtil.loadFromFile("dimian.obj", mv.getResources(),mv);//沙漠地面
				break;
			case 307:
				mv.isInitOver = true;
				mv.currView=mv.mainView;
				reSetData();
				break;
		}
	}


	@Override
	public void drawView(GL10 gl) {		
		for(BN2DObject temp:al2)
		{
			temp.drawSelf();
		}
		
		if(!mv.isInitOver)
		{
			if(index>=al.size())
			{
				index=0;
			}
			al.get(index).setX(150+initIndex*2 );
			al.get(index).drawSelf();
			index++;
			
			//初始化界面资源
			initBNView(initIndex);
			if(initIndex<308)
			{
				initIndex++;//图片索引加1
			}
		}
		
	}	
	public void initSound()
	{
		if(!musicOff)
		{
			if(MainActivity.sound.mp!=null){
				MainActivity.sound.mp.pause();
				MainActivity.sound.mp=null;
			}
		}
	}
	public void reSetData()
	{
		this.initIndex=0;
		this.index=0;
		mv.isInitOver = false;
	}
}
