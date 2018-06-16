package com.bn.util.constant;

import java.util.ArrayList;
import com.bn.object.BN2DObject;
import com.bn.util.manager.ShaderManager;
import com.bn.util.manager.TextureManager;

public class MyHHData {
	
	public static ArrayList<BN2DObject> selectpzView()//����ѡƿ�ӽ����Ԫ��
	{
		ArrayList<BN2DObject> pzData=new ArrayList<BN2DObject>();//���ѡƿ�ӽ��������Ԫ��
		pzData.add(new BN2DObject(540,960,1080,1920,TextureManager.getTextures("Back.png"),
				ShaderManager.getShader(0)));
		pzData.add(new BN2DObject(540,200,800,150,TextureManager.getTextures("pingzi0.png"),
				ShaderManager.getShader(0)));
		pzData.add(new BN2DObject(200,580,300,300,TextureManager.getTextures("pingzi1.png"),
				ShaderManager.getShader(0)));
		pzData.add(new BN2DObject(540,580,300,300,TextureManager.getTextures("pingzi2.png"),
				ShaderManager.getShader(0)));
		pzData.add(new BN2DObject(880,580,300,300,TextureManager.getTextures("pingzi3.png"),
				ShaderManager.getShader(0)));
		pzData.add(new BN2DObject(200,900,300,300,TextureManager.getTextures("pingzi4.png"),
				ShaderManager.getShader(0)));
		pzData.add(new BN2DObject(540,900,300,300,TextureManager.getTextures("pingzi5.png"),
				ShaderManager.getShader(0)));
		pzData.add(new BN2DObject(880,900,300,300,TextureManager.getTextures("pingzi6.png"),
				ShaderManager.getShader(0)));
		pzData.add(new BN2DObject(180,1200,150,150,TextureManager.getTextures("backto.png"),
				ShaderManager.getShader(0)));
	
		return pzData;
	}
	
	public static ArrayList<BN2DObject> selectqiuView()
	{
		ArrayList<BN2DObject> qiuData=new ArrayList<BN2DObject>();//���ѡ����������Ԫ��
		qiuData.add(new BN2DObject(540,960,1080,1920,TextureManager.getTextures("Back.png"),
				ShaderManager.getShader(0)));
		qiuData.add(new BN2DObject(540,200,800,150,TextureManager.getTextures("qiu0.png"),
				ShaderManager.getShader(0)));
		qiuData.add(new BN2DObject(200,580,300,300,TextureManager.getTextures("qiu1.png"),
				ShaderManager.getShader(0)));
		qiuData.add(new BN2DObject(540,580,300,300,TextureManager.getTextures("qiu22.png"),
				ShaderManager.getShader(0)));
		qiuData.add(new BN2DObject(880,580,300,300,TextureManager.getTextures("qiu3.png"),
				ShaderManager.getShader(0)));
		qiuData.add(new BN2DObject(200,900,300,300,TextureManager.getTextures("qiu4.png"),
				ShaderManager.getShader(0)));
		qiuData.add(new BN2DObject(540,900,300,300,TextureManager.getTextures("qiu5.png"),
				ShaderManager.getShader(0)));
		qiuData.add(new BN2DObject(880,900,300,300,TextureManager.getTextures("qiu6.png"),
				ShaderManager.getShader(0)));
		qiuData.add(new BN2DObject(180,1200,150,150,TextureManager.getTextures("backto.png"),
				ShaderManager.getShader(0)));
		return qiuData;
	}
	public static ArrayList<BN2DObject> pauseView()//������ͣ�����Ԫ��
	{
		ArrayList<BN2DObject> pauseData=new ArrayList<BN2DObject>();//�����ͣ���������Ԫ��
		pauseData.add(
				new BN2DObject(
						960,1280,250,1100, 
						TextureManager.getTextures("zanting.png"),
						ShaderManager.getShader(0)
						));

		return pauseData;
	}
}
