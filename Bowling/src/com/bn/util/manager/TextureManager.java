 package com.bn.util.manager;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import com.bn.bowling.MySurfaceView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

public class TextureManager
{
	static String[] texturesName={
		//�׽���2-7---- 5��
		"jiemian.png","kuang.png","danrenyouxi.png","changjing.png","bangzhu.png",//4
		//����ѡ�����--9��
		"Back.png","kuang2.png","desert.png","science.png","space.png",
		"taikong.png","kehuan.png","shamo.png","backto.png",//13
		//��Ϸ����------16��
		"pingzi.png","guidao.png","scorekuang.png",
		"wanjia.png","zh.png","spare.png","strike.png","whitex.png",
		"whitexie.png","player.png","triangle.png","quannumbers.png","fire.png",
		"stars.png","stars2.png","showscore.png",//29
		//��������-------2��
		"helpback.png","helptext.png",//31
		//���ؽ���-------18��
		"bowling1.png","load0.png","load1.png","load2.png","load3.png",
		"load4.png","load5.png","load6.png","load7.png","load8.png",
		"load9.png","load10.png","load11.png","load12.png","load13.png",
		"load14.png","load5.png","lu.png",//49
		//ɳĮ����ͼƬ---6��
		"sand.png","sky_cloud.png","smguidao.png","tree1.png","tree3.png","yanshi.png",//55
		//��ճ���ͼƬ---8��
		"background.png","guidao2.png","feixingwu1.png","feixingwu2.png","earth.png",
		"earthn.png","moon.png","qiu2.png",//63
		//��Ϸ��������---6��
		"quanzhong.png","buzhong.png","luogouqiu.png","zongfen.png","backb.png","houseb.png",
		//��ͣ����-------7��
		"pause.png","start.png","zanting.png","queren.png","shezhi.png","lugou1.png","hongcha1.png",//82
		//ѡ��ƿ�ӽ���---12��
		"pingzi1.png","pingzi2.png","pingzi3.png","pingzi4.png","pingzi5.png","pingzi6.png",//87
		"ping1.png","ping2.png","ping3.png","ping4.png","ping5.png","ping6.png",
		//ѡ���������---14��
		"qiu0.png","qiu1.png","qiu22.png","qiu3.png","qiu4.png","qiu5.png","qiu6.png",//101
		"ball1.png","ball2.png","ball3.png","ball4.png","ball5.png","ball6.png","gou2.png",//108
		//��͸�����-------3��
		"btmguidao.png","btmguidao2.png","btmsmguidao.png",
		//¼��-------------2��
		"camera.png","camerakuang.png",
		//����-------------31��
		"white0.png","white1.png","white2.png","white3.png","white4.png",
		"white5.png","white6.png","white7.png","white8.png","white9.png",
		"green0.png","green1.png","green2.png","green3.png","green4.png",
		"green5.png","green6.png","green7.png","green8.png","green9.png",
		"yellow1.png","yellow2.png","yellow3.png","yellow4.png","yellow5.png",
		"yellow6.png","yellow7.png","yellow8.png","yellow9.png","yellow10.png",
		"pingzi0.png"
		};//����ͼ������
	
	static HashMap<String,Integer> texList=new HashMap<String,Integer>();//������ͼ���б�
	public static int initTexture(MySurfaceView mv,String texName,boolean isRepeat)//��������id
	{
		int[] textures=new int[1];
		GLES20.glGenTextures
		(
				1,//����������id������
				textures,//����id������
				0//ƫ����
		);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);//������id
		//����MAGʱΪ���Բ���
		GLES20.glTexParameterf
		(
				GLES20.GL_TEXTURE_2D,
				GLES20.GL_TEXTURE_MAG_FILTER,
				GLES20.GL_LINEAR
		);
		//����MINʱΪ��������
		GLES20.glTexParameterf
		(
				GLES20.GL_TEXTURE_2D,
				GLES20.GL_TEXTURE_MIN_FILTER, 
				GLES20.GL_NEAREST
		);
		if(isRepeat)
		{
			//����S������췽ʽΪ�ظ�����
			GLES20.glTexParameterf
			(
					GLES20.GL_TEXTURE_2D,
					GLES20.GL_TEXTURE_WRAP_S, 
					GLES20.GL_REPEAT
			);
			//����T������췽ʽΪ�ظ�����
			GLES20.glTexParameterf
			(
					GLES20.GL_TEXTURE_2D,
					GLES20.GL_TEXTURE_WRAP_T, 
					GLES20.GL_REPEAT
			);
		}else
		{
			//����S������췽ʽΪ��ȡ
			GLES20.glTexParameterf
			(
					GLES20.GL_TEXTURE_2D,
					GLES20.GL_TEXTURE_WRAP_S, 
					GLES20.GL_CLAMP_TO_EDGE
			);
			//����T������췽ʽΪ��ȡ
			GLES20.glTexParameterf
			(
					GLES20.GL_TEXTURE_2D,
					GLES20.GL_TEXTURE_WRAP_T, 
					GLES20.GL_CLAMP_TO_EDGE
			);
		}
		String path="pic/"+texName;//����ͼƬ·��
		InputStream in = null;
		try {
			in = mv.getResources().getAssets().open(path);
		}catch (IOException e) {
			e.printStackTrace();
		}
		Bitmap bitmap=BitmapFactory.decodeStream(in);//�����м���ͼƬ����
		GLUtils.texImage2D
		(
				GLES20.GL_TEXTURE_2D,//�������ͣ���OpenGL ES�б���ΪGL10.GL_TEXTURE_2D
				0,//����Ĳ�Σ�0��ʾ����ͼ��㣬�������Ϊֱ����ͼ
				bitmap,//����ͼ��
				0//����߿�ߴ�
		);
		bitmap.recycle();//������سɹ����ͷ��ڴ��е�����ͼ
		return textures[0];
	}
	
	public static void loadingTexture(MySurfaceView mv,int start,int picNum)//������������ͼ
	{
		for(int i=start;i<start+picNum;i++)
		{
			int texture=0;
			if((texturesName[i].equals("zh.png"))||(texturesName[i].equals("guidao.png"))
					||(texturesName[i].equals("ping1.png"))||(texturesName[i].equals("ping2.png"))
					||(texturesName[i].equals("ping3.png"))||(texturesName[i].equals("ping4.png"))
					||(texturesName[i].equals("ping5.png"))||(texturesName[i].equals("ping6.png"))
					||(texturesName[i].equals("sand.png"))||(texturesName[i].equals("tree1.png"))
					||(texturesName[i].equals("tree3.png"))||(texturesName[i].equals("smguidao.png"))
					||(texturesName[i].equals("yanshi.png"))||(texturesName[i].equals("sky_cloud.png"))
					||(texturesName[i].equals("background.png"))||(texturesName[i].equals("guidao2.png"))
					||(texturesName[i].equals("fexingwu1.png"))||(texturesName[i].equals("fexingwu2.png"))
					||(texturesName[i].equals("btmguidao.png"))||(texturesName[i].equals("btmguidao2.png"))
					||(texturesName[i].equals("btmsmguidao.png")))
			{
				texture=initTexture(mv,texturesName[i],true);
			}else
			{
				texture=initTexture(mv,texturesName[i],false);
			}
			texList.put(texturesName[i],texture);//�����ݼ��뵽�б���
		}
	}
	public static int getTextures(String texName)//�������ͼ
	{
		int result=0;
		if(texList.get(texName)!=null)//����б����д�����ͼ
		{
			result=texList.get(texName);//��ȡ����ͼ
		}else
		{
			result=-1;
		}
		return result;
	}
}
