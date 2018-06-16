package com.bn.view;

import java.util.ArrayList;
import java.util.List;
import static com.bn.util.constant.Constant.*;
import javax.microedition.khronos.opengles.GL10;

import com.bn.bowling.MainActivity;
import com.bn.bowling.MySurfaceView;
import com.bn.object.BN2DObject;

import com.bn.util.constant.Constant;

import static com.bn.util.constant.Constant.effictOff;
import static com.bn.util.constant.SourceConstant.*;

import com.bn.util.constant.MyHHData;
import com.bn.util.manager.ShaderManager;
import com.bn.util.manager.TextureManager;

import android.view.MotionEvent;

public class SelectPZView extends BNAbstractView
{
	MySurfaceView mv;
	BN2DObject  bn;//ѡ��ͼ��
	List<BN2DObject> selectView=new ArrayList<BN2DObject>();//ѡƿ�ӽ���Ԫ��
	Object lock=new Object();
	public SelectPZView(MySurfaceView mv)
	{
		this.mv=mv;
		initView();
	}
	public void initView() 
	{
		selectView=MyHHData.selectpzView();
		 bn=new BN2DObject(600,920,150,150,TextureManager.getTextures("gou2.png"),
					ShaderManager.getShader(0));  //Ĭ������Ϊping5
	}

	public boolean onTouchEvent(MotionEvent e) {
		float x=Constant.fromRealScreenXToStandardScreenX(e.getX());//����ǰ��Ļ����ת��Ϊ��׼��Ļ����
		float y=Constant.fromRealScreenYToStandardScreenY(e.getY());
		switch (e.getAction()){
			case MotionEvent.ACTION_DOWN:
				if(x>Selectqiuback_Left&&x<Selectqiuback_Right&&
						y>Selectqiuback_Top&&y<Selectqiuback_Bottom)
				{   //����������
					isPause=false;
					mv.currView=mv.gameView;
				}
				if(countTurn==10){
					break;
				}
				if(x>Selectpz1_Left&&x<Selectpz1_Right&&
						y>Selectpz1_Top&&y<Selectpz1_Bottom)
				{  
				    	 bn=new BN2DObject(240,600,150,150,
				    		TextureManager.getTextures("gou2.png"),ShaderManager.getShader(0));
						  if(!effictOff){
				    			MainActivity.sound.playMusic(Constant.BUTTON_PRESS, 0);
				    		}
				}else if(x>Selectpz2_Left&&x<Selectpz2_Right&&
						y>Selectpz2_Top&&y<Selectpz2_Bottom)
				{
						  bn=new BN2DObject(580,600,150,150,
							TextureManager.getTextures("gou2.png"),ShaderManager.getShader(0));
						  if(!effictOff){
				    			MainActivity.sound.playMusic(Constant.BUTTON_PRESS, 0);
				    		}
				}else if(x>Selectpz3_Left&&x<Selectpz3_Right&&
						y>Selectpz3_Top&&y<Selectpz3_Bottom)
				{
						  bn=new BN2DObject(920,580,150,150,
							TextureManager.getTextures("gou2.png"),
							ShaderManager.getShader(0));	
						  if(!effictOff){
				    			MainActivity.sound.playMusic(Constant.BUTTON_PRESS, 0);
				    		}
				}
				else if(x>Selectpz4_Left&&x<Selectpz4_Right&&
						y>Selectpz4_Top&&y<Selectpz4_Bottom)
				{				
						  bn=new BN2DObject(240,920,150,150,
								  TextureManager.getTextures("gou2.png"),ShaderManager.getShader(0));
						  if(!effictOff){
				    			MainActivity.sound.playMusic(Constant.BUTTON_PRESS, 0);
				    		}
				}else if(x>Selectpz5_Left&&x<Selectpz5_Right&&
						y>Selectpz5_Top&&y<Selectpz5_Bottom)
				{
						  bn=new BN2DObject(600,920,150,150,
								  TextureManager.getTextures("gou2.png"),ShaderManager.getShader(0));
						  if(!effictOff){
				    			MainActivity.sound.playMusic(Constant.BUTTON_PRESS, 0);
				    		}
				}else if(x>Selectpz6_Left&&x<Selectpz6_Right&&
						y>Selectpz6_Top&&y<Selectpz6_Bottom)
				{
						  bn=new BN2DObject(900,920,150,150,
								  TextureManager.getTextures("gou2.png"),ShaderManager.getShader(0));
						  if(!effictOff){
				    			MainActivity.sound.playMusic(Constant.BUTTON_PRESS, 0);
				    		}
				}
				break;
			case MotionEvent.ACTION_UP:
				if(countTurn==10){
					return false;
				}
				else if(x>Selectpz1_Left&&x<Selectpz1_Right&&
						y>Selectpz1_Top&&y<Selectpz1_Bottom)
				{  
					BottleId=TextureManager.getTextures("ping1.png");
				}else if(x>Selectpz2_Left&&x<Selectpz2_Right&&
						y>Selectpz2_Top&&y<Selectpz2_Bottom)
				{
					BottleId=TextureManager.getTextures("ping2.png");
				}else if(x>Selectpz3_Left&&x<Selectpz3_Right&&
						y>Selectpz3_Top&&y<Selectpz3_Bottom)
				{
					BottleId=TextureManager.getTextures("ping3.png");
				}
				else if(x>Selectpz4_Left&&x<Selectpz4_Right&&
						y>Selectpz4_Top&&y<Selectpz4_Bottom)
				{
					BottleId=TextureManager.getTextures("ping4.png");
					   
				}else if(x>Selectpz5_Left&&x<Selectpz5_Right&&
						y>Selectpz5_Top&&y<Selectpz5_Bottom)
				{
					BottleId=TextureManager.getTextures("ping5.png");
						
				}else if(x>Selectpz6_Left&&x<Selectpz6_Right&&
						y>Selectpz6_Top&&y<Selectpz6_Bottom)
				{
					BottleId=TextureManager.getTextures("ping6.png");
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
