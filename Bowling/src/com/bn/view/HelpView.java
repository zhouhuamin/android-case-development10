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
	List<BN2DObject> al=new ArrayList<BN2DObject>();//���BNObject����
	float MAX_Y=1250;//�߽����ֵ1700
	float MIN_Y=565;//�߽���Сֵ  340
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
		//���ĵ��Լ�ͼƬ�Ŀ�Ⱥ͸߶�
		al.add(new BN2DObject(540,960,1080,1920,TextureManager.getTextures("jiemian.png"),ShaderManager.getShader(0)));
		//�������汳��ͼ
		al.add(new BN2DObject(540,1000,1080,1600,TextureManager.getTextures("helpback.png"),ShaderManager.getShader(0)));
		//����
		al.add(new BN2DObject(540,285, 500,200,TextureManager.getTextures("bangzhu.png"),ShaderManager.getShader(0)));
		//�������淵�ذ�ť
		al.add(new BN2DObject(100,1680, 150, 150, TextureManager.getTextures("backto.png"),ShaderManager.getShader(0)));
		//������������ͼƬ
		text=new BN2DObject(540,1250,1080,1720,TextureManager.getTextures("helptext.png"),ShaderManager.getShader(0));
	}

	float y2=0;
	@Override
	public boolean onTouchEvent(MotionEvent e) {
		switch(e.getAction())
		{
			case MotionEvent.ACTION_DOWN:
				x=Constant.fromRealScreenXToStandardScreenX(e.getX());//����ǰ��Ļ����ת��Ϊ��׼��Ļ����
				y=Constant.fromRealScreenYToStandardScreenY(e.getY());
		    	if(x>=BACK_HELP_LEFT&&x<=BACK_HELP_RIGHT&&y>=BACK_HELP_UP&&y<=BACK_HELP_DOWN)
		    	{//���������Ϸ��ť
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
		//�������ò���
		GLES20.glEnable(GLES20.GL_SCISSOR_TEST);
		GLES20.glScissor(//ָ���ü����ڵ����½�����(������������Ļ���½�Ϊԭ���)�ͼ��ô��ڵĴ�С
				(int)Constant.fromStandardScreenXToRealScreenX(0), 
				(int)Constant.fromStandardScreenYToRealScreenY(280),
				(int)Constant.fromStandardScreenSizeToRealScreenSize(1080),
				(int)Constant.fromStandardScreenSizeToRealScreenSize(1300)
				);
		synchronized(lock)
		{
			if(text!=null){
				text.drawSelf();//����
			}			
		}
		//�رռ��ò���
		GLES20.glDisable(GLES20.GL_SCISSOR_TEST);
	}
	
	public void reSetData()
	{
		START_Y=1250;
		x=0;
		y=0;
	}
}
