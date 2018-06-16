package com.bn.util.tool;

import java.util.ArrayList;

import com.bn.object.BN2DObject;
import com.bn.util.constant.SourceConstant;
import com.bn.util.manager.ShaderManager;
import com.bn.util.manager.TextureManager;

public class GetNumberBN2D//获取游戏总分、每轮比赛和每次比赛的分数绘制对象
{
	public ArrayList<BN2DObject[]> numberData=new ArrayList<BN2DObject[]>();//不同颜色的数字列表
	public ArrayList<BN2DObject[]> currBlueNumber=new ArrayList<BN2DObject[]>();//每轮比赛成绩的数字绘制对象列表
	public ArrayList<BN2DObject> everyTimeScore=new ArrayList<BN2DObject>();//每次成绩的数字绘制对象列表
	public ArrayList<BN2DObject> everyRound=new ArrayList<BN2DObject>();
	public BN2DObject[] totalPointsBN2D=new BN2DObject[3];//总分的绘制对象
	public BN2DObject[] allPointsBN2D=new BN2DObject[3];//总分的绘制对象
	public BN2DObject[] strike=new BN2DObject[2];//全中的绘制对象
	public BN2DObject[] spare=new BN2DObject[2];//补中的绘制对象
	public BN2DObject[] luoGou=new BN2DObject[2];//补中的绘制对象
	BN2DObject xieBN2D;// /
	BN2DObject chaBN2D;// X
	
	BN2DObject strikeBN2D;// strike
	BN2DObject spareBN2D;// spare
	public BN2DObject showBN2D;
	int start=0;//遍历开始索引值
	int end=0;//遍历结束索引值
	public boolean isShow=true;
	public GetNumberBN2D()
	{
		numberData=initBN2DObject();//初始化数字列表
	}
	
	public ArrayList<BN2DObject[]> initBN2DObject()//初始化不同颜色的数字列表
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
	//获取每一轮比赛成绩和总分的数字绘制对象列表
	public void getNumberBN2D()
	{	
		BN2DObject[] numberBN2D=new BN2DObject[3];
		int score=0;
		for(int i=0;i<CalculateScore.everyPoints.length;i++)
		{
			if(CalculateScore.everyPoints[i]==null||CalculateScore.everyPoints[i].equals("空")
					||CalculateScore.everyPoints[i].equals("斜"))
			{
				break;
			}
			if(CalculateScore.everyPoints[i]!=null&&!CalculateScore.everyPoints[i].equals("不再取了"))
			{
				score=Integer.parseInt(CalculateScore.everyPoints[i]);//得到分数
				CalculateScore.everyPoints[i]="不再取了";
			if(score/100!=0)//如果分数大于100
			{
				numberBN2D[0]=numberData.get(0)[score/100];//则将其分离成三个数字
				int extraNumber=score%100;
				numberBN2D[1]=numberData.get(0)[extraNumber/10];
				numberBN2D[2]=numberData.get(0)[extraNumber%10];
				totalPointsBN2D[0]=numberData.get(1)[score/100];
				totalPointsBN2D[1]=numberData.get(1)[extraNumber/10];
				totalPointsBN2D[2]=numberData.get(1)[extraNumber%10];
				
				allPointsBN2D[0]=numberData.get(4)[score/100];
				allPointsBN2D[1]=numberData.get(4)[extraNumber/10];
				allPointsBN2D[2]=numberData.get(4)[extraNumber%10];
			}else if(score/100==0)//如果分数小于100
			{
				numberBN2D[0]=null;//百位设为空
				totalPointsBN2D[0]=null;
				allPointsBN2D[0]=null;
				if(score/10!=0)//如果分数大于10
				{
					numberBN2D[1]=numberData.get(0)[score/10];//将分数分离成两个数字
					numberBN2D[2]=numberData.get(0)[score%10];
					totalPointsBN2D[1]=numberData.get(1)[score/10];
					totalPointsBN2D[2]=numberData.get(1)[score%10];
					
					allPointsBN2D[1]=numberData.get(4)[score/10];
					allPointsBN2D[2]=numberData.get(4)[score%10];
				}else//分数小于10
				{
					numberBN2D[1]=null;//只需要绘制一个数字对象
					numberBN2D[2]=numberData.get(0)[score%10];
					totalPointsBN2D[1]=null;
					totalPointsBN2D[2]=numberData.get(1)[score%10];
					
					allPointsBN2D[1]=null;
					allPointsBN2D[2]=numberData.get(4)[score%10];
				}
			}
			currBlueNumber.add(new BN2DObject[]{numberBN2D[0],numberBN2D[1],numberBN2D[2]});//十位数
			}
		}
	}
	
	public void getStrikeAndSpareCount()//获取全中和补中数量
	{
		int count=SourceConstant.strikeCount;//获得全中数量
		if(count/10!=0)
		{
			strike[0]=numberData.get(4)[count/10];
			strike[1]=numberData.get(4)[count%10];
		}else
		{
			strike[0]=null;
			strike[1]=numberData.get(4)[count%10];
		}
		count=SourceConstant.spareCount;//获得补中数量
		if(count/10!=0)
		{
			spare[0]=numberData.get(4)[count/10];
			spare[1]=numberData.get(4)[count%10];
		}else
		{
			spare[0]=null;
			spare[1]=numberData.get(4)[count%10];
		}
		count=SourceConstant.luoGouCount;//获得落沟球数量
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
	
	public void getEveryTimeScore()//获取每一次比赛的分数
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
			}else if(group[1].equals("空"))
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
	
	public void restart()//重新开始
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
