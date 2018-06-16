package com.bn.util.tool;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import com.bn.object.BN2DObject;
import com.bn.util.manager.ShaderManager;
import com.bn.util.manager.TextureManager;

public class GetTriangleNumber //���û�е��µı���������ֵ�Ļ��ƶ���
{
	public ArrayList<BN2DObject[]> numberData=new ArrayList<BN2DObject[]>();//�����б�
	@SuppressLint("UseSparseArrays")
	public HashMap<Integer,BN2DObject> drawBallIndex=new HashMap<Integer,BN2DObject>();//��Ҫ���Ƶ������б�
	int[] index=new int[10];//������Ƶı���������ֵ
	
	public GetTriangleNumber()
	{
		numberData=initBN2DObject();//��ʼ�������б�
	}
	ArrayList<BN2DObject[]> initBN2DObject()//��ʼ������ͼƬ�Ļ��ƶ���
	{
		ArrayList<BN2DObject[]> TimerData=new ArrayList<BN2DObject[]>();
		BN2DObject data1[]=new BN2DObject[10];
		
		for(int i=0;i<10;i++)//��ȡ���ֵĻ��ƶ���
		{
			data1[i]=new BN2DObject(i, TextureManager.getTextures("quannumbers.png"), 
					ShaderManager.getShader(0),60,60);
		}
		TimerData.add(data1);
		for(int i=0;i<index.length;i++)//��ʼ����������
		{
			index[i]=0;//ȫ����ֵΪ0
		}
		return TimerData;
	}
	
	public int[] getDrawBallIndex(int[] ballIndex)//��ȡ���Ʊ����������ֵ
	{
		for(int i=0;i<ballIndex.length;i++)//����ʮ�������������ֵ
		{
			if(ballIndex[i]!=0)//�������û�е���
			{
				BN2DObject bn=numberData.get(0)[i];//���ö�Ӧ���ֵĻ��ƶ���
				drawBallIndex.put(i, bn);//��ӵ��б���
				index[i]=ballIndex[i];//�ı����������ֵ
			}else
			{
				index[i]=ballIndex[i];//�ı����������ֵ
			}
		}
		return index;//������������
	}
	
	public void restart()
	{
		drawBallIndex.clear();
		for(int i=0;i<index.length;i++)//��ʼ����������
		{
			index[i]=0;//ȫ����ֵΪ0
		}
	}
}
