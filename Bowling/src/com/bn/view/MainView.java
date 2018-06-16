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
	List<BN2DObject> al=new ArrayList<BN2DObject>();//���BNObject����
	public MainView(MySurfaceView mv)
	{
		this.mv=mv;
		initView();
	}
	@Override
	public void initView()
	{
		//�׽��汳��ͼ
		al.add(new BN2DObject(540,960,1080,1920,TextureManager.getTextures("jiemian.png"),ShaderManager.getShader(0)));
		//�׽��濪ʼ��Ϸ��ť
		al.add(new BN2DObject(540,1000, 650,140,TextureManager.getTextures("kuang.png"),ShaderManager.getShader(0)));
		//�׽���ѡ�񳡾���ť
		al.add(new BN2DObject(540,1200, 650,140,TextureManager.getTextures("kuang.png"),ShaderManager.getShader(0)));
		//�׽��������ť
		al.add(new BN2DObject(540,1400, 650,140,TextureManager.getTextures("kuang.png"),ShaderManager.getShader(0)));
		//�׽��浥����Ϸ
		al.add(new BN2DObject(560,1010, 500,220,TextureManager.getTextures("danrenyouxi.png"),ShaderManager.getShader(0)));
		//�׽��泡
		al.add(new BN2DObject(540,1200, 440,180,TextureManager.getTextures("changjing.png"),ShaderManager.getShader(0)));
		//�׽������
		al.add(new BN2DObject(540,1400, 440,180,TextureManager.getTextures("bangzhu.png"),ShaderManager.getShader(0)));
		
		//��������
		if(!musicOff){
	 		if(MainActivity.sound.mp==null)
	 		{											
	 			MainActivity.sound.mp =  MediaPlayer.create(mv.activity, R.raw.beach);
	 			MainActivity.sound.mp.setVolume(0.2f, 0.2f);//����������������
	 			MainActivity.sound.mp.setLooping(true);//ѭ������
	 			MainActivity.sound.mp.start();
			}
		}

	}
	@Override
	public boolean onTouchEvent(MotionEvent e) 
	{
		float x=Constant.fromRealScreenXToStandardScreenX(e.getX());//����ǰ��Ļ����ת��Ϊ��׼��Ļ����
		float y=Constant.fromRealScreenYToStandardScreenY(e.getY());
		switch(e.getAction())
		{
		    case MotionEvent.ACTION_UP:
		    	if(x>=BOTTON_LEFT&&x<=BOTTON_RIGHT&&y>=DANREN_GAME_UP&&y<=DANREN_GAME_DOWN)
		    	{//���������Ϸ��ť
		    		if(!effictOff){
		    			MainActivity.sound.playMusic(Constant.BUTTON_PRESS, 0);
		    		}
		    		sceneIndex=(int)(Math.random()*2+1);
		    		mv.gameView.initSound();
		    		mv.currView=mv.gameView;
		    	}else if(x>=BOTTON_LEFT&&x<=BOTTON_RIGHT&&y>=OPTION_UP&&y<=DOPTION_DOWN)
		    	{//���������ť
		    		if(!effictOff){
		    			MainActivity.sound.playMusic(Constant.BUTTON_PRESS, 0);
		    		}
		    		mv.currView=mv.optionView;
		    	}else if(x>=BOTTON_LEFT&&x<=BOTTON_RIGHT&&y>=HELP_UP&&y<=HELP_DOWN)
		    	{//���������ť
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
	public void drawView(GL10 gl)//����
	{
		for(BN2DObject bo:al)
		{
			bo.drawSelf();
		}
	}
}
