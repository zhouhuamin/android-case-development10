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
	List<BN2DObject> al=new ArrayList<BN2DObject>();//���BNObject����
	List<BN2DObject> al2=new ArrayList<BN2DObject>();//���BNObject����
	int initIndex=0;
	int index=0;
	public LoadView(MySurfaceView mv)
	{
		this.mv=mv;
		initView();
	}
	@Override
	public void initView() {
		TextureManager.loadingTexture(this.mv, 32, 18);//��Ϸ���ؽ���ͼƬ��Դ
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
				TextureManager.loadingTexture(this.mv, 0, 5);//������ͼƬ��Դ
				break;
			case 1:
				TextureManager.loadingTexture(this.mv, 5, 9);//��������ͼƬ��Դ
				break;
			case 2:
				TextureManager.loadingTexture(this.mv, 14, 16);//��Ϸ����ͼƬ��Դ
				break;
			case 3:
				TextureManager.loadingTexture(this.mv, 30, 2);//��Ϸ����ͼƬ��Դ
				break;
			case 5:
				TextureManager.loadingTexture(this.mv, 50, 6);//��ճ���ͼƬ��Դ
				break;
			case 6:
				TextureManager.loadingTexture(this.mv, 56, 8);//��ճ���ͼƬ��Դ
				break;
			case 7:
				TextureManager.loadingTexture(this.mv, 64, 6);//��ͣͼƬ��Դ
				break;
			case 8:
				TextureManager.loadingTexture(this.mv, 70, 7);//ѡƿ��ͼƬ��Դ
				break;
			case 9:
				TextureManager.loadingTexture(this.mv, 77, 12);//ѡ������ͼƬ��Դ
				break;
			case 10:
				TextureManager.loadingTexture(this.mv, 89, 14);//��͸�����
				break;
			case 11:
				TextureManager.loadingTexture(this.mv, 103, 5);//��͸�����
				break;
			case 12:
				TextureManager.loadingTexture(this.mv, 108, 31);
				break;
			case 13:
				if(mv.gameView==null){
					mv.gameView=new GameView(this.mv);	
				}		
				break;
			case 14://����GameView����
				ball_model=LoadUtil.loadFromFile("ball.obj",this.mv.getResources(), mv);//��ʼ�����ģ������
				break;
			case 30://��ʼ����Ϸ����
				bottle_model=LoadUtil.loadFromFile("pingzi2.obj", mv.getResources(), mv);//��ʼ��ƿ�ӵ�ģ������
				break;
			case 50://���ؿƼ���ģ��
				kjg_model=LoadUtil.loadFromFile("kjg.obj", mv.getResources(), mv);//��ʼ���Ƽ��ݵ�ģ������
				break;
			case 70://���ع��ģ��			
				gd_model=LoadUtil.loadFromFile("guidao.obj", mv.getResources(), mv);//��ʼ�������ģ������
				break;
			case 90:
				tree2_model=LoadUtil.loadFromFile("tree2.obj",mv.getResources(),mv);//ɳĮ��
				break;
			case 110:
				tree3_model=LoadUtil.loadFromFile("tree3.obj", mv.getResources(),mv);//ɳĮ��
	            break;
			case 130:
				 smgd_model=LoadUtil.loadFromFile("smguidao.obj",mv.getResources(),mv);//ɳĮ���
				  break;
			case 150:
				smys_model=LoadUtil.loadFromFile("yanshi.obj", mv.getResources(),mv);//ɳĮ��ʯ
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
	            dimian_model=LoadUtil.loadFromFile("dimian.obj", mv.getResources(),mv);//ɳĮ����
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
			
			//��ʼ��������Դ
			initBNView(initIndex);
			if(initIndex<308)
			{
				initIndex++;//ͼƬ������1
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
