package com.bn.util.tool;

import java.util.ArrayList;

import com.bn.object.BN2DObject;
import com.bn.util.constant.SourceConstant;
import com.bn.util.manager.ShaderManager;
import com.bn.util.manager.TextureManager;

public class GetNumberBN2D//��ȡ��Ϸ�ܷ֡�ÿ�ֱ�����ÿ�α����ķ������ƶ���
{
	public ArrayList<BN2DObject[]> numberData=new ArrayList<BN2DObject[]>();//��ͬ��ɫ�������б�
	public ArrayList<BN2DObject[]> currBlueNumber=new ArrayList<BN2DObject[]>();//ÿ�ֱ����ɼ������ֻ��ƶ����б�
	public ArrayList<BN2DObject> everyTimeScore=new ArrayList<BN2DObject>();//ÿ�γɼ������ֻ��ƶ����б�
	public ArrayList<BN2DObject> everyRound=new ArrayList<BN2DObject>();
	public BN2DObject[] totalPointsBN2D=new BN2DObject[3];//�ֵܷĻ��ƶ���
	public BN2DObject[] allPointsBN2D=new BN2DObject[3];//�ֵܷĻ��ƶ���
	public BN2DObject[] strike=new BN2DObject[2];//ȫ�еĻ��ƶ���
	public BN2DObject[] spare=new BN2DObject[2];//���еĻ��ƶ���
	public BN2DObject[] luoGou=new BN2DObject[2];//���еĻ��ƶ���
	BN2DObject xieBN2D;// /
	BN2DObject chaBN2D;// X
	
	BN2DObject strikeBN2D;// strike
	BN2DObject spareBN2D;// spare
	public BN2DObject showBN2D;
	int start=0;//������ʼ����ֵ
	int end=0;//������������ֵ
	public boolean isShow=true;
	public GetNumberBN2D()
	{
		numberData=initBN2DObject();//��ʼ�������б�
	}
	
	public ArrayList<BN2DObject[]> initBN2DObject()//��ʼ����ͬ��ɫ�������б�
	{
		ArrayList<BN2DObject[]> TimerData=new ArrayList<BN2DObject[]>();
		BN2DObject data1[][]=new BN2DObject[5][10];
		
		for(int i=0;i<10;i++)
		{
			String path="green"+i+".png";
			BN2DObject bn=new BN2DObject(0,0,80,80, TextureManager.getTextures(path), 
					ShaderManager.getShader(0));
			data1[0][i]=bn;
			bn=new BN2DObject(0,0,120,120, TextureManager.getTextures(path), 
					ShaderManager.getShader(0));
			data1[1][i]=bn;
			bn=new BN2DObject(0,0,200,200, TextureManager.getTextures(path), 
					ShaderManager.getShader(0));
			data1[3][i]=bn;
			data1[4][i]=new BN2DObject(0,0,100,100, TextureManager.getTextures(path), 
					ShaderManager.getShader(0));
			path="white"+i+".png";
			bn=new BN2DObject(0,0,80,80, TextureManager.getTextures(path), 
					ShaderManager.getShader(0));
			data1[2][i]=bn;
			path="yellow"+(i+1)+".png";
			bn=new BN2DObject(0,0,90,90, TextureManager.getTextures(path), 
					ShaderManager.getShader(0));
			everyRound.add(bn);
		}
		TimerData.add(data1[0]);
		TimerData.add(data1[1]);
		TimerData.add(data1[2]);
		TimerData.add(data1[3]);
		TimerData.add(data1[4]);
		xieBN2D=new BN2DObject(0,0,80,80,TextureManager.getTextures("whitexie.png"),
				ShaderManager.getShader(0));
		chaBN2D=new BN2DObject(0,0,80,80,TextureManager.getTextures("whitex.png"),
				ShaderManager.getShader(0));
		strikeBN2D=new BN2DObject(0,0,500,300,TextureManager.getTextures("strike.png"),
				ShaderManager.getShader(0));
		spareBN2D=new BN2DObject(0,0,500,300,TextureManager.getTextures("spare.png"),
				ShaderManager.getShader(0));
		return TimerData;
	}
	//��ȡÿһ�ֱ����ɼ����ֵܷ����ֻ��ƶ����б�
	public void getNumberBN2D()
	{	
		BN2DObject[] numberBN2D=new BN2DObject[3];
		int score=0;
		for(int i=0;i<CalculateScore.everyPoints.length;i++)
		{
			if(CalculateScore.everyPoints[i]==null||CalculateScore.everyPoints[i].equals("��")
					||CalculateScore.everyPoints[i].equals("б"))
			{
				break;
			}
			if(CalculateScore.everyPoints[i]!=null&&!CalculateScore.everyPoints[i].equals("����ȡ��"))
			{
				score=Integer.parseInt(CalculateScore.everyPoints[i]);//�õ�����
				CalculateScore.everyPoints[i]="����ȡ��";
			if(score/100!=0)//�����������100
			{
				numberBN2D[0]=numberData.get(0)[score/100];//����������������
				int extraNumber=score%100;
				numberBN2D[1]=numberData.get(0)[extraNumber/10];
				numberBN2D[2]=numberData.get(0)[extraNumber%10];
				totalPointsBN2D[0]=numberData.get(1)[score/100];
				totalPointsBN2D[1]=numberData.get(1)[extraNumber/10];
				totalPointsBN2D[2]=numberData.get(1)[extraNumber%10];
				
				allPointsBN2D[0]=numberData.get(4)[score/100];
				allPointsBN2D[1]=numberData.get(4)[extraNumber/10];
				allPointsBN2D[2]=numberData.get(4)[extraNumber%10];
			}else if(score/100==0)//�������С��100
			{
				numberBN2D[0]=null;//��λ��Ϊ��
				totalPointsBN2D[0]=null;
				allPointsBN2D[0]=null;
				if(score/10!=0)//�����������10
				{
					numberBN2D[1]=numberData.get(0)[score/10];//�������������������
					numberBN2D[2]=numberData.get(0)[score%10];
					totalPointsBN2D[1]=numberData.get(1)[score/10];
					totalPointsBN2D[2]=numberData.get(1)[score%10];
					
					allPointsBN2D[1]=numberData.get(4)[score/10];
					allPointsBN2D[2]=numberData.get(4)[score%10];
				}else//����С��10
				{
					numberBN2D[1]=null;//ֻ��Ҫ����һ�����ֶ���
					numberBN2D[2]=numberData.get(0)[score%10];
					totalPointsBN2D[1]=null;
					totalPointsBN2D[2]=numberData.get(1)[score%10];
					
					allPointsBN2D[1]=null;
					allPointsBN2D[2]=numberData.get(4)[score%10];
				}
			}
			currBlueNumber.add(new BN2DObject[]{numberBN2D[0],numberBN2D[1],numberBN2D[2]});//ʮλ��
			}
		}
	}
	
	public void getStrikeAndSpareCount()//��ȡȫ�кͲ�������
	{
		int count=SourceConstant.strikeCount;//���ȫ������
		if(count/10!=0)
		{
			strike[0]=numberData.get(4)[count/10];
			strike[1]=numberData.get(4)[count%10];
		}else
		{
			strike[0]=null;
			strike[1]=numberData.get(4)[count%10];
		}
		count=SourceConstant.spareCount;//��ò�������
		if(count/10!=0)
		{
			spare[0]=numberData.get(4)[count/10];
			spare[1]=numberData.get(4)[count%10];
		}else
		{
			spare[0]=null;
			spare[1]=numberData.get(4)[count%10];
		}
		count=SourceConstant.luoGouCount;//����乵������
		if(count/10!=0)
		{
			luoGou[0]=numberData.get(4)[count/10];
			luoGou[1]=numberData.get(4)[count%10];
		}else
		{
			luoGou[0]=null;
			luoGou[1]=numberData.get(4)[count%10];
		}
	}
	
	public void getEveryTimeScore()//��ȡÿһ�α����ķ���
	{
		end=CalculateScore.scoreList.size();
		for(int i=start;i<end;i++)
		{
			BN2DObject numberBN2D;
			String str=CalculateScore.scoreList.get(i);
			String[] group=str.split("#");
			if(group[1].equals("x"))
			{
				numberBN2D=chaBN2D;
				showBN2D=strikeBN2D;
				isShow=false;
			}else if(group[1].equals("/"))
			{
				numberBN2D=xieBN2D;
				showBN2D=spareBN2D;
				isShow=false;
			}else if(group[1].equals("0"))
			{
				numberBN2D=numberData.get(2)[0];
				showBN2D=numberData.get(3)[0];
			}else if(group[1].equals("��"))
			{
				numberBN2D=null;
			}else
			{
				int kk=Integer.parseInt(group[1]);
				numberBN2D=numberData.get(2)[kk];
				showBN2D=numberData.get(3)[kk];
			}
			everyTimeScore.add(numberBN2D);
		}
		start=end;
	}
	
	public void restart()//���¿�ʼ
	{
		isShow=true;
		currBlueNumber.clear();
		everyTimeScore.clear();
		showBN2D=null;
		totalPointsBN2D[0]=null;
		totalPointsBN2D[1]=null;
		totalPointsBN2D[2]=null;
		allPointsBN2D[0]=null;
		allPointsBN2D[1]=null;
		allPointsBN2D[2]=null;
		strike[0]=null;
		strike[1]=null;
		spare[0]=null;
		spare[1]=null;
		start=0;
		end=0;
	}
}
