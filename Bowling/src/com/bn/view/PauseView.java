package com.bn.view;

import java.util.ArrayList;
import java.util.List;

import com.bn.bowling.MainActivity;
import com.bn.object.BN2DObject;

import com.bn.util.constant.Constant;
import com.bn.util.constant.MyHHData;
import com.bn.util.manager.ShaderManager;
import com.bn.util.manager.TextureManager;
import static com.bn.util.constant.SourceConstant.*;

import android.view.MotionEvent;

public class PauseView
{
	GameView gv;
	public List<BN2DObject> pauseView=new ArrayList<BN2DObject>();//暂停界面元素
	List<BN2DObject> backmainView=new ArrayList<BN2DObject>();//返回界面元素

	List<BN2DObject> SheZhiViewal=new ArrayList<BN2DObject>();//设置界面元素
	
	Object lock=new Object();
	Object myLock=new Object();
	
	public boolean isbackmain=false;
	public boolean isSheZhi=false;
	public boolean isyinyue=true;
	public boolean isyinxiao=true;
	public boolean isshexiangji=true;
	public int distance=24;
	public PauseView(GameView gv)
	{
		this.gv=gv;
		initView();
	}
	public void initView() 
	{
		pauseView=MyHHData.pauseView();
		
		SheZhiViewal.add(new BN2DObject(540,960,900,800,TextureManager.getTextures("shezhi.png"),ShaderManager.getShader(0)));
		SheZhiViewal.add(new BN2DObject(795,760,150,100,TextureManager.getTextures("lugou1.png"),ShaderManager.getShader(0)));
		SheZhiViewal.add(new BN2DObject(795,890,150,100,TextureManager.getTextures("lugou1.png"),ShaderManager.getShader(0)));
		SheZhiViewal.add(new BN2DObject(795,1030,150,100,TextureManager.getTextures("lugou1.png"),ShaderManager.getShader(0)));
		SheZhiViewal.add(new BN2DObject(210,1250,150,150,TextureManager.getTextures("backto.png"),ShaderManager.getShader(0)));
	}

	public boolean onTouchEvent(MotionEvent e) {
		float x=Constant.fromRealScreenXToStandardScreenX(e.getX());//将当前屏幕坐标转换为标准屏幕坐标
		float y=Constant.fromRealScreenYToStandardScreenY(e.getY());
		switch (e.getAction()){
			case MotionEvent.ACTION_UP:
				if(x>Selectpz_Left&&x<Selectpz_Right&&
						y>Selectpz_Top&&y<Selectpz_Bottom)
				{//选择瓶子界面
					gv.Pause=new BN2DObject(980, 1700, 130, 130, TextureManager.getTextures("pause.png"), 
							ShaderManager.getShader(0));
					gv.viewManager.currView=gv.viewManager.SelcetpzView;
				}else if(x>Selectqiu_Left&&x<Selectqiu_Right&&
						y>Selectqiu_Top&&y<Selectqiu_Bottom)
				{//选择保龄球界面
				    gv.Pause=new BN2DObject(980, 1700, 130, 130, TextureManager.getTextures("pause.png"), 
							ShaderManager.getShader(0));
					gv.viewManager.currView=gv.viewManager.SelcetqiuView;
				}else if(x>Pauseshezhi_Left&&x<Pauseshezhi_Right&&
						y>Pauseshezhi_Top&&y<Pauseshezhi_Bottom)
				{//设置界面
					 isSheZhi=true;
					 isDrawAN=false;
					 isDrawScore=false;
					 synchronized(lock){
						 pauseView.remove(0);			
					 }			
				}else if(x>PauseBackMain_Left&&x<PauseBackMain_Right&&
						y>PauseBackMain_Top&&y<PauseBackMain_Bottom)
				{//返回主界面
					isbackmain=true;	
					 synchronized(lock){
						 pauseView.remove(0);			
					 }	
				    isDrawAN=false;
					backmainView.add(0,new BN2DObject(540,800,800,600,						
							TextureManager.getTextures("queren.png"),				
							ShaderManager.getShader(0)));
				}
				if(isbackmain&&x>BackMainqueren_Left&&x<BackMainqueren_Right&&
						y>BackMainqueren_Top&&y<BackMainqueren_Bottom)
				{//点击“是”
					isbackmain=false;
					isPause=false;
					pauseView=MyHHData.pauseView();
					isDrawAN=true;
					gv.Pause=new BN2DObject(980, 1700, 130, 130, TextureManager.getTextures("pause.png"), 
							ShaderManager.getShader(0));
					gv.pausecount=0;
					OptionView ov=gv.viewManager.optionView;
					ov.initSound();
					gv.viewManager.currView=ov;
					ov=null;
					gv.phc.initAllObject(gv.viewManager);
					gv.reSetData();
				}else if(isbackmain&&x>BackMainfou_Left&&x<BackMainfou_Right&&
						y>BackMainfou_Top&&y<BackMainfou_Bottom)
				{//点击“否”
				    isbackmain=false;
					isPause=false;
					 isDrawAN=true;
					pauseView=MyHHData.pauseView();
					gv.Pause=new BN2DObject(980, 1700, 130, 130, TextureManager.getTextures("pause.png"), 
							ShaderManager.getShader(0));
				}
				if(isSheZhi&&x>SheZhiback_Left&&x<SheZhiback_Right&&
						y>SheZhiback_Top&&y<SheZhiback_Bottom)
				{//从设置界面返回游戏场景
					isSheZhi=false;
					isPause=false;
					 isDrawAN=true;
					 isDrawScore=true;
					 isScoreDown=true;
					 gv.distance=0;
					gv.Pause=new BN2DObject(980, 1700, 130, 130, TextureManager.getTextures("pause.png"), 
							ShaderManager.getShader(0));
					pauseView=MyHHData.pauseView();
				}
				if(isSheZhi)
				{
					if(x>SheZhiyinyue_Left&&x<SheZhiyinyue_Right&&
							y>SheZhiyinyue_Top&&y<SheZhiyinyue_Bottom)
					{  
						isyinyue=!isyinyue;
						if(isyinyue)
						{//开启音乐
							BN2DObject bn=new BN2DObject(795,760,150,100,
									TextureManager.getTextures("lugou1.png")
									,ShaderManager.getShader(0));
							synchronized(myLock)
							{
								SheZhiViewal.remove(1);
								SheZhiViewal.add(1,bn);
							}
							Constant.musicOff=false;
							gv.initSound();
						}else
						{//关闭音乐
							BN2DObject bn=new BN2DObject(795,760,150,100,
									TextureManager.getTextures("hongcha1.png"),
									ShaderManager.getShader(0));
							synchronized(myLock)
							{
								SheZhiViewal.remove(1);
								SheZhiViewal.add(1,bn);
							}
							Constant.musicOff=true;
							if(MainActivity.sound.mp!=null){
								MainActivity.sound.mp.pause();
								MainActivity.sound.mp=null;
							}
						}
					}else if(x>SheZhiyinxiao_Left&&x<SheZhiyinxiao_Right&&
							y>SheZhiyinxiao_Top&&y<SheZhiyinxiao_Bottom)
					{
						isyinxiao=!isyinxiao;
						if(isyinxiao)
						{
							BN2DObject bn=new BN2DObject(795,890,150,100,
									TextureManager.getTextures("lugou1.png"),
									ShaderManager.getShader(0));
							synchronized(myLock)
							{
								SheZhiViewal.remove(2);
								SheZhiViewal.add(2,bn);
							}
							Constant.effictOff=false;
						}else
						{//关闭音效					
							BN2DObject bn=new BN2DObject(795,890,150,100,
									TextureManager.getTextures("hongcha1.png"),
									ShaderManager.getShader(0));
							synchronized(myLock)
							{
								SheZhiViewal.remove(2);
								SheZhiViewal.add(2,bn);
							}
							Constant.effictOff=true;
						}
					}else if(x>SheZhishexiangji_Left&&x<SheZhishexiangji_Right&&
							y>SheZhishexiangji_Top&&y<SheZhishexiangji_Bottom)
					{
						isshexiangji=!isshexiangji;
						if(isshexiangji)
						{					
							BN2DObject bn=new BN2DObject(795,1030,150,100,
									TextureManager.getTextures("lugou1.png"),
									ShaderManager.getShader(0));
							synchronized(myLock)
							{
								SheZhiViewal.remove(3);
								SheZhiViewal.add(3,bn);
							}
							
							//设置摄像机标志位
							isCameraMove=true;
						
						}else{
							BN2DObject bn=new BN2DObject(795,1030,150,100,
									TextureManager.getTextures("hongcha1.png"),
									ShaderManager.getShader(0));
							synchronized(myLock)
							{
								SheZhiViewal.remove(3);
								SheZhiViewal.add(3,bn);
							}
							
							//设置摄像机标志位
							isCameraMove=false;
						}
					  }
					}
				break;
		}	
		return true;
	}
	
	public void drawView() 
	{
		synchronized(lock)
		{
			for(BN2DObject pause:pauseView)			
			{
				pause.drawSelf(960+10*distance,1280);
			}
    	}
		if(distance>0)
		{
			distance--;
		}
		drawShezhiView();
		drawBackMainView();
	}
	public void drawShezhiView()
	{
		if(isSheZhi)
		{
			synchronized(myLock)
			{
				for(BN2DObject pause:SheZhiViewal)
				{
					pause.drawSelf();
				}
			}
		}
	}
	
	public void drawBackMainView()
	{
		if(isbackmain)
		{
			synchronized(lock)
			{
				for(BN2DObject pause:backmainView)
				{
					pause.drawSelf();
				}
			}
		}
	}
}
