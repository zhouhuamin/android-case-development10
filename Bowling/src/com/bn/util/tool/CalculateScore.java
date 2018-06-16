package com.bn.util.tool;

import static com.bn.util.constant.SourceConstant.*;

import android.annotation.SuppressLint;
import java.util.ArrayList;
import java.util.HashMap;


public class CalculateScore//计算得分工具类
{
	@SuppressLint("UseSparseArrays")
	public static HashMap<Integer,String[]> score=new HashMap<Integer,String[]>();
	public static ArrayList<String> scoreList=new ArrayList<String>();
	static int roundIndex=1;//第几轮
	static int timesIndex=1;//第几次
	
	static String[] tenScores=new String[3];//第十轮有三次机会
	static String[] otherScores=new String[2];//第1-9轮各有两次机会
	
	static String[] tenListScores=new String[3];//第十轮有三次机会
	static String[] otherListScores=new String[2];//第1-9轮各有两次机会
	
	static boolean TwiceRestartGame=false;//第十轮第一次即全部推倒 重新发球
	static boolean ThirdRestartGame=false;//第十轮第二次即全部推倒 重新发球
	public static boolean GameOver=false;//游戏结束
	
	public static String[] everyPoints=new String[10];//得到的总分
	static boolean changePoints[]={false,false,false,false,false,false,false,false,false,false};
	public static int totalPoints=0;
	
	public static void restart()//重新开始
	{
		score.clear();
		scoreList.clear();
		roundIndex=1;
		timesIndex=1;
		totalPoints=0;
		strikeCount=0;
		spareCount=0;
		TwiceRestartGame=false;
		ThirdRestartGame=false;
		GameOver=false;
		for(int i=0;i<changePoints.length;i++)
		{
			changePoints[i]=false;
			everyPoints[i]=null;
		}
		for(int i=0;i<tenScores.length;i++)
		{
			tenScores[i]=null;
			tenListScores[i]=null;
		}
		for(int i=0;i<otherScores.length;i++)
		{
			otherScores[i]=null;
			otherListScores[i]=null;
		}
	}
	
	public static void calculateScore(int ballDownNumber)
	{
		if(roundIndex!=10)//第1-9轮比赛
		{
			if(timesIndex==1)//第一次
			{
				if(ballDownNumber==10)//全部推倒
				{
					otherScores[0]="x";
					otherScores[1]="空";
					score.put(roundIndex,new String[]{otherScores[0],otherScores[1]});
					
					otherListScores[0]=roundIndex+"#"+otherScores[0];
					otherListScores[1]=roundIndex+"#"+otherScores[1];
					scoreList.add(otherListScores[0]);
					scoreList.add(otherListScores[1]);
					strikeCount++;//全中的数量加1
					timesIndex=1;
					roundIndex++;
				}else//没有全部推倒
				{
					otherScores[0]=ballDownNumber+"";
					otherListScores[0]=roundIndex+"#"+otherScores[0];
					scoreList.add(otherListScores[0]);
					timesIndex++;
				}
			}else if(timesIndex==2)//第二次
			{
				if(ballDownNumber+Integer.parseInt(otherScores[0])==10)//两次机会将球全部推倒
				{
					otherScores[1]="/";
					spareCount++;//补中的数量加1
				}else
				{
					otherScores[1]=ballDownNumber+"";
				}
				score.put(roundIndex,new String[]{otherScores[0],otherScores[1]});
				otherListScores[1]=roundIndex+"#"+otherScores[1];
				scoreList.add(otherListScores[1]);
				timesIndex=1;
				roundIndex++;
			}
		}else if(roundIndex==10)//第十轮比赛
		{
			if(timesIndex==1)//第一次游戏
			{
				if(ballDownNumber==10)
				{
					tenScores[0]="x";
					strikeCount++;//全中的数量加1
					firstStartGame=true;
					TwiceRestartGame=true;
				}else{
					tenScores[0]=ballDownNumber+"";
				}
				tenListScores[0]=roundIndex+"#"+tenScores[0];
				scoreList.add(tenListScores[0]);
				timesIndex++;
			}else if(timesIndex==2)//第二次游戏
			{
				if(TwiceRestartGame)//重新开始游戏，即已经拥有第三次机会
				{
					if(ballDownNumber==10)//全部推倒
					{
						tenScores[1]="x";
						strikeCount++;//全中的数量加1
						ThirdRestartGame=true;
						firstStartGame=true;
						timesIndex++;
					}else//没有全部推倒 也拥有第三次机会
					{
						firstStartGame=true;
						tenScores[1]=ballDownNumber+"";
						timesIndex++;
					}
					tenListScores[1]=roundIndex+"#"+tenScores[1];
					scoreList.add(tenListScores[1]);
				}else//接着第一次进行游戏
				{
					if(ballDownNumber+Integer.parseInt(tenScores[0])==10)//两次机会将球全部推倒
					{
						tenScores[1]="/";
						spareCount++;//补中的数量加1
						ThirdRestartGame=true;
						firstStartGame=true;
						tenListScores[1]=roundIndex+"#"+tenScores[1];
						scoreList.add(tenListScores[1]);
						timesIndex++;
					}else
					{
						tenScores[1]=ballDownNumber+"";
						tenScores[2]="空";
						score.put(roundIndex,new String[]{tenScores[0],tenScores[1],tenScores[2]});
						
						tenListScores[1]=roundIndex+"#"+tenScores[1];
						tenListScores[2]=roundIndex+"#"+tenScores[2];
						scoreList.add(tenListScores[1]);
						scoreList.add(tenListScores[2]);
						roundIndex++;
						GameOver=true;//游戏结束
					}
				}
			}else if(timesIndex==3)//第三次游戏 前两次必须全部推倒
			{
				if(ThirdRestartGame)//第三次如果是重新开始游戏
				{
					if(ballDownNumber==10)//全部推倒
					{
						tenScores[2]="x";
						strikeCount++;//全中的数量加1
						score.put(roundIndex,new String[]{tenScores[0],tenScores[1],tenScores[2]});

						tenListScores[2]=roundIndex+"#"+tenScores[2];
						scoreList.add(tenListScores[2]);
						roundIndex++;
						GameOver=true;//游戏结束
					}else
					{
						tenScores[2]=ballDownNumber+"";
						score.put(roundIndex,new String[]{tenScores[0],tenScores[1],tenScores[2]});
						
						tenListScores[2]=roundIndex+"#"+tenScores[2];
						scoreList.add(tenListScores[2]);
						roundIndex++;
						GameOver=true;//游戏结束
					}
				}else if(TwiceRestartGame&&!ThirdRestartGame)//如果第二次就重新开始游戏
				{
					if(ballDownNumber+Integer.parseInt(tenScores[1])==10)//两次机会将球全部推倒
					{
						tenScores[2]="/";
						score.put(roundIndex,new String[]{tenScores[0],tenScores[1],tenScores[2]});
						spareCount++;//补中的数量加1
						tenListScores[2]=roundIndex+"#"+tenScores[2];
						scoreList.add(tenListScores[2]);
						roundIndex++;
						GameOver=true;//游戏结束
					}else
					{
						tenScores[2]=ballDownNumber+"";
						score.put(roundIndex,new String[]{tenScores[0],tenScores[1],tenScores[2]});
						
						tenListScores[2]=roundIndex+"#"+tenScores[2];
						scoreList.add(tenListScores[2]);
						roundIndex++;
						GameOver=true;//游戏结束
					}
				}
			}
		}
		
		//计算本轮分数===============start===================
		TotalPoints();
		
		for(int j=0;j<=roundIndex-2;j++)//遍历已经对数组进行赋值过的内容
		{
			if(everyPoints[j]!=null&&!everyPoints[j].equals("空")&&!everyPoints[j].equals("斜"))//如果不是空或者斜
			{
				if(!changePoints[j]&&j==0)//首先将总分赋值为第一轮的分数
				{
					totalPoints=Integer.parseInt(everyPoints[j]);
					changePoints[j]=true;
				}
				if(!changePoints[j])//判断该值是否被修改过
				{
					totalPoints+=Integer.parseInt(everyPoints[j]);
					everyPoints[j]=totalPoints+"";//将分数与前面的分数进行相加
					changePoints[j]=true;
				}
			}
		}
		//计算本轮分数===============end=====================
	}
	
	public static void TotalPoints()//计算本轮分数
	{
		String[] points=new String[3];
		if(score.size()>0)
		{
			if(roundIndex!=11)//前九轮比赛的每轮比赛成绩
			{
				points=score.get(roundIndex-1);//取出每次比赛的成绩
				if(points[0].equals("x"))//如果第一次就全部推倒
				{
					everyPoints[roundIndex-2]="空";
				}else if(!points[0].equals("x"))
				{
					if(points[1].equals("/"))//如果第二次全部推倒
					{
						everyPoints[roundIndex-2]="斜";
					}else if(!points[1].equals("/"))//如果都没有推倒
					{
						if(!changePoints[roundIndex-2])
						{
							everyPoints[roundIndex-2]=Integer.parseInt(points[0])+Integer.parseInt(points[1])+"";
						}
					}
				}
				for(int i=0;i<=(roundIndex-2);i++)//遍历数组 对空字符串进行赋值
				{
					if(everyPoints[i].equals("空"))
					{
						if(everyPoints[i+1]!=null)//如果下一位字符串有内容
						{
							if(everyPoints[i+1].equals("空"))//如果下一位字符串内容为空
							{
								if(everyPoints[i+2]!=null)//如果下两位不为空
								{
									if(everyPoints[i+2].equals("空"))//如果下一位字符串内容为空
									{
										everyPoints[i]=10+10+10+"";//全部都是10分
									}else if(!everyPoints[i+2].equals("空"))
									{
										everyPoints[i]=10+10+Integer.parseInt(points[0])+"";
									}
								}
							}else if(!everyPoints[i+1].equals("空"))//如果下一位字符串为数字
							{
								if(everyPoints[i+1].contains("斜"))//下一位中含有斜字
								{
									everyPoints[i]=10+10+"";//则加上满分10分
								}else
								{
									everyPoints[i]=10+Integer.parseInt(everyPoints[i+1])+"";//否则直接加上得分
								}
							}
						}
					}else if(everyPoints[i].equals("斜"))//如果当前字符串为斜
					{
						if(everyPoints[i+1]!=null)
						{
							if(everyPoints[i+1].equals("空"))//下一位数字为空
							{
								everyPoints[i]=10+10+"";//则加上满分10分
							}else if(!everyPoints[i+1].equals("空"))
							{
								everyPoints[i]=10+Integer.parseInt(points[0])+"";//否则直接加上得分
							}
						}
					}
				}
			}else if(roundIndex==11)//第十轮比赛
			{
				points=score.get(roundIndex-1);//取出每次比赛的成绩
				if(points[0].equals("x"))//如果第一次就全部推倒
				{
					if(points[1].equals("x"))//第二次全部推倒
					{
						if(points[2].equals("x"))//第三次全部推倒
						{
							everyPoints[roundIndex-2]=10+10+10+"";//则获得满分30分
						}else//第三次没有全部推倒
						{
							everyPoints[roundIndex-2]=10+10+Integer.parseInt(points[2])+"";//直接加上第三次得分
						}
					}else//第二次没有重新开始游戏
					{
						if(points[2].equals("/"))//第二次将球全部推倒
						{
							everyPoints[roundIndex-2]=10+10+"";//则获得10分
						}else
						{
							everyPoints[roundIndex-2]=10+Integer.parseInt(points[1])+Integer.parseInt(points[2])+"";
						}
					}
				}else if(!points[0].equals("x"))//如果第一次没有全部推倒
				{
					if(points[1].equals("/"))//如果第二次全部推倒
					{
						if(points[2].equals("x"))
						{
							everyPoints[roundIndex-2]=10+10+"";
						}else
						{
							everyPoints[roundIndex-2]=10+Integer.parseInt(points[2])+"";
						}
					}else if(!points[1].equals("/"))//如果都没有推倒
					{
						everyPoints[roundIndex-2]=Integer.parseInt(points[0])+Integer.parseInt(points[1])+"";
					}
				}
				
				for(int i=0;i<everyPoints.length;i++)
				{
					if(everyPoints[i].equals("空"))
					{
						if(i+1<9)//前8轮有字符串为空的内容
						{
							if(everyPoints[i+1]!=null)
							{
								if(everyPoints[i+1].equals("空"))
								{
									if(everyPoints[i+2]!=null)//如果下两位不为空
									{
										if(everyPoints[i+2].equals("空"))
										{
											everyPoints[i]=10+10+10+"";//全部都是10分
										}else
										{
											if(points[0].equals("x"))
											{
												everyPoints[i]=10+10+10+"";//全部都是10分
											}else if(!points[0].equals("x"))
											{
												everyPoints[i]=10+10+Integer.parseInt(points[0])+"";//全部都是10分
											}
										}
									}
								}
							}
						}else//第9轮有字符串为空的内容 则加上第十轮的前两个球的分数
						{
							if(points[0].equals("x"))
							{
								if(points[1].equals("x"))
								{
									everyPoints[i]=10+10+10+"";//全部都是10分
								}else
								{
									everyPoints[i]=10+10+Integer.parseInt(points[1])+"";//全部都是10分
								}
							}else
							{
								if(points[1].equals("/"))
								{
									everyPoints[i]=10+10+"";//全部都是10分
								}else
								{
									everyPoints[i]=10+Integer.parseInt(points[0])+Integer.parseInt(points[1])+"";//全部都是10分
								}
							}
						}
					}
				}
			}
		}
	}
}
