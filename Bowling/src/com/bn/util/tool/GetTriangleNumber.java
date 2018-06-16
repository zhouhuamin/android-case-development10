package com.bn.util.tool;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import com.bn.object.BN2DObject;
import com.bn.util.manager.ShaderManager;
import com.bn.util.manager.TextureManager;

public class GetTriangleNumber //获得没有倒下的保龄球索引值的绘制对象
{
	public ArrayList<BN2DObject[]> numberData=new ArrayList<BN2DObject[]>();//数字列表
	@SuppressLint("UseSparseArrays")
	public HashMap<Integer,BN2DObject> drawBallIndex=new HashMap<Integer,BN2DObject>();//需要绘制的数字列表
	int[] index=new int[10];//允许绘制的保龄球索引值
	
	public GetTriangleNumber()
	{
		numberData=initBN2DObject();//初始化数字列表
	}
	ArrayList<BN2DObject[]> initBN2DObject()//初始化数字图片的绘制对象
	{
		ArrayList<BN2DObject[]> TimerData=new ArrayList<BN2DObject[]>();
		BN2DObject data1[]=new BN2DObject[10];
		
		for(int i=0;i<10;i++)//获取数字的绘制对象
		{
			data1[i]=new BN2DObject(i, TextureManager.getTextures("quannumbers.png"), 
					ShaderManager.getShader(0),60,60);
		}
		TimerData.add(data1);
		for(int i=0;i<index.length;i++)//初始化数字数组
		{
			index[i]=0;//全部赋值为0
		}
		return TimerData;
	}
	
	public int[] getDrawBallIndex(int[] ballIndex)//获取绘制保龄球的索引值
	{
		for(int i=0;i<ballIndex.length;i++)//遍历十个保龄球的索引值
		{
			if(ballIndex[i]!=0)//如果该球没有倒下
			{
				BN2DObject bn=numberData.get(0)[i];//则获得对应数字的绘制对象
				drawBallIndex.put(i, bn);//添加到列表中
				index[i]=ballIndex[i];//改变索引数组的值
			}else
			{
				index[i]=ballIndex[i];//改变索引数组的值
			}
		}
		return index;//返回索引数组
	}
	
	public void restart()
	{
		drawBallIndex.clear();
		for(int i=0;i<index.length;i++)//初始化数字数组
		{
			index[i]=0;//全部赋值为0
		}
	}
}
