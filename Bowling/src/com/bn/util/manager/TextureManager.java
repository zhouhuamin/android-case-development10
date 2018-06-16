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
		//首界面2-7---- 5张
		"jiemian.png","kuang.png","danrenyouxi.png","changjing.png","bangzhu.png",//4
		//场景选择界面--9张
		"Back.png","kuang2.png","desert.png","science.png","space.png",
		"taikong.png","kehuan.png","shamo.png","backto.png",//13
		//游戏界面------16张
		"pingzi.png","guidao.png","scorekuang.png",
		"wanjia.png","zh.png","spare.png","strike.png","whitex.png",
		"whitexie.png","player.png","triangle.png","quannumbers.png","fire.png",
		"stars.png","stars2.png","showscore.png",//29
		//帮助界面-------2张
		"helpback.png","helptext.png",//31
		//加载界面-------18张
		"bowling1.png","load0.png","load1.png","load2.png","load3.png",
		"load4.png","load5.png","load6.png","load7.png","load8.png",
		"load9.png","load10.png","load11.png","load12.png","load13.png",
		"load14.png","load5.png","lu.png",//49
		//沙漠场景图片---6张
		"sand.png","sky_cloud.png","smguidao.png","tree1.png","tree3.png","yanshi.png",//55
		//天空场景图片---8张
		"background.png","guidao2.png","feixingwu1.png","feixingwu2.png","earth.png",
		"earthn.png","moon.png","qiu2.png",//63
		//游戏结束界面---6张
		"quanzhong.png","buzhong.png","luogouqiu.png","zongfen.png","backb.png","houseb.png",
		//暂停界面-------7张
		"pause.png","start.png","zanting.png","queren.png","shezhi.png","lugou1.png","hongcha1.png",//82
		//选择瓶子界面---12张
		"pingzi1.png","pingzi2.png","pingzi3.png","pingzi4.png","pingzi5.png","pingzi6.png",//87
		"ping1.png","ping2.png","ping3.png","ping4.png","ping5.png","ping6.png",
		//选择保龄球界面---14张
		"qiu0.png","qiu1.png","qiu22.png","qiu3.png","qiu4.png","qiu5.png","qiu6.png",//101
		"ball1.png","ball2.png","ball3.png","ball4.png","ball5.png","ball6.png","gou2.png",//108
		//半透明轨道-------3张
		"btmguidao.png","btmguidao2.png","btmsmguidao.png",
		//录像-------------2张
		"camera.png","camerakuang.png",
		//数字-------------31张
		"white0.png","white1.png","white2.png","white3.png","white4.png",
		"white5.png","white6.png","white7.png","white8.png","white9.png",
		"green0.png","green1.png","green2.png","green3.png","green4.png",
		"green5.png","green6.png","green7.png","green8.png","green9.png",
		"yellow1.png","yellow2.png","yellow3.png","yellow4.png","yellow5.png",
		"yellow6.png","yellow7.png","yellow8.png","yellow9.png","yellow10.png",
		"pingzi0.png"
		};//纹理图的名称
	
	static HashMap<String,Integer> texList=new HashMap<String,Integer>();//放纹理图的列表
	public static int initTexture(MySurfaceView mv,String texName,boolean isRepeat)//生成纹理id
	{
		int[] textures=new int[1];
		GLES20.glGenTextures
		(
				1,//产生的纹理id的数量
				textures,//纹理id的数组
				0//偏移量
		);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);//绑定纹理id
		//设置MAG时为线性采样
		GLES20.glTexParameterf
		(
				GLES20.GL_TEXTURE_2D,
				GLES20.GL_TEXTURE_MAG_FILTER,
				GLES20.GL_LINEAR
		);
		//设置MIN时为最近点采样
		GLES20.glTexParameterf
		(
				GLES20.GL_TEXTURE_2D,
				GLES20.GL_TEXTURE_MIN_FILTER, 
				GLES20.GL_NEAREST
		);
		if(isRepeat)
		{
			//设置S轴的拉伸方式为重复拉伸
			GLES20.glTexParameterf
			(
					GLES20.GL_TEXTURE_2D,
					GLES20.GL_TEXTURE_WRAP_S, 
					GLES20.GL_REPEAT
			);
			//设置T轴的拉伸方式为重复拉伸
			GLES20.glTexParameterf
			(
					GLES20.GL_TEXTURE_2D,
					GLES20.GL_TEXTURE_WRAP_T, 
					GLES20.GL_REPEAT
			);
		}else
		{
			//设置S轴的拉伸方式为截取
			GLES20.glTexParameterf
			(
					GLES20.GL_TEXTURE_2D,
					GLES20.GL_TEXTURE_WRAP_S, 
					GLES20.GL_CLAMP_TO_EDGE
			);
			//设置T轴的拉伸方式为截取
			GLES20.glTexParameterf
			(
					GLES20.GL_TEXTURE_2D,
					GLES20.GL_TEXTURE_WRAP_T, 
					GLES20.GL_CLAMP_TO_EDGE
			);
		}
		String path="pic/"+texName;//定义图片路径
		InputStream in = null;
		try {
			in = mv.getResources().getAssets().open(path);
		}catch (IOException e) {
			e.printStackTrace();
		}
		Bitmap bitmap=BitmapFactory.decodeStream(in);//从流中加载图片内容
		GLUtils.texImage2D
		(
				GLES20.GL_TEXTURE_2D,//纹理类型，在OpenGL ES中必须为GL10.GL_TEXTURE_2D
				0,//纹理的层次，0表示基本图像层，可以理解为直接贴图
				bitmap,//纹理图像
				0//纹理边框尺寸
		);
		bitmap.recycle();//纹理加载成功后释放内存中的纹理图
		return textures[0];
	}
	
	public static void loadingTexture(MySurfaceView mv,int start,int picNum)//加载所有纹理图
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
			texList.put(texturesName[i],texture);//将数据加入到列表中
		}
	}
	public static int getTextures(String texName)//获得纹理图
	{
		int result=0;
		if(texList.get(texName)!=null)//如果列表中有此纹理图
		{
			result=texList.get(texName);//获取纹理图
		}else
		{
			result=-1;
		}
		return result;
	}
}
