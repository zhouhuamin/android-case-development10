package com.bn.util.manager;

import java.util.HashMap;

import com.bn.bowling.MySurfaceView;
import com.bn.util.tool.ShaderUtil;

import android.annotation.SuppressLint;

@SuppressLint("UseSparseArrays")
public class ShaderManager 
{
	static String[][] programs={
		{"vertex_2d.sh","frag_2d.sh"},//0
		{"vertex_snow.sh","frag_snow.sh"},//1
		{"vertex_earth.sh","frag_earth.sh"},//2
		{"vertex_moon.sh","frag_moon.sh"},//3
		{"vertex_shadow.sh","frag_shadow.sh"},//4
		};//������ɫ��������
	static HashMap<Integer,Integer> list=new HashMap<Integer,Integer>();
	public static void loadingShader(MySurfaceView mv)//������ɫ��
	{
		for(int i=0;i<programs.length;i++)
		{
			//���ض�����ɫ���Ľű�����
			String mVertexShader=ShaderUtil.loadFromAssetsFile(programs[i][0], mv.getResources());
			//����ƬԪ��ɫ���Ľű�����
			String mFragmentShader=ShaderUtil.loadFromAssetsFile(programs[i][1],mv.getResources());
			//���ڶ�����ɫ����ƬԪ��ɫ����������
			int mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
			list.put(i, mProgram);
		}  
	}
	public static int getShader(int index)//���ĳ�׳���
	{
		int result=0;
		if(list.get(index)!=null)//����б����д��׳���
		{
			result=list.get(index);
		}
		return result;
	}
}
