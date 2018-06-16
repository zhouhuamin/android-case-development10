package com.bn.util.tool;

import static com.bn.util.constant.SourceConstant.*;

import android.annotation.SuppressLint;
import java.util.ArrayList;
import java.util.HashMap;


public class CalculateScore//����÷ֹ�����
{
	@SuppressLint("UseSparseArrays")
	public static HashMap<Integer,String[]> score=new HashMap<Integer,String[]>();
	public static ArrayList<String> scoreList=new ArrayList<String>();
	static int roundIndex=1;//�ڼ���
	static int timesIndex=1;//�ڼ���
	
	static String[] tenScores=new String[3];//��ʮ�������λ���
	static String[] otherScores=new String[2];//��1-9�ָ������λ���
	
	static String[] tenListScores=new String[3];//��ʮ�������λ���
	static String[] otherListScores=new String[2];//��1-9�ָ������λ���
	
	static boolean TwiceRestartGame=false;//��ʮ�ֵ�һ�μ�ȫ���Ƶ� ���·���
	static boolean ThirdRestartGame=false;//��ʮ�ֵڶ��μ�ȫ���Ƶ� ���·���
	public static boolean GameOver=false;//��Ϸ����
	
	public static String[] everyPoints=new String[10];//�õ����ܷ�
	static boolean changePoints[]={false,false,false,false,false,false,false,false,false,false};
	public static int totalPoints=0;
	
	public static void restart()//���¿�ʼ
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
		if(roundIndex!=10)//��1-9�ֱ���
		{
			if(timesIndex==1)//��һ��
			{
				if(ballDownNumber==10)//ȫ���Ƶ�
				{
					otherScores[0]="x";
					otherScores[1]="��";
					score.put(roundIndex,new String[]{otherScores[0],otherScores[1]});
					
					otherListScores[0]=roundIndex+"#"+otherScores[0];
					otherListScores[1]=roundIndex+"#"+otherScores[1];
					scoreList.add(otherListScores[0]);
					scoreList.add(otherListScores[1]);
					strikeCount++;//ȫ�е�������1
					timesIndex=1;
					roundIndex++;
				}else//û��ȫ���Ƶ�
				{
					otherScores[0]=ballDownNumber+"";
					otherListScores[0]=roundIndex+"#"+otherScores[0];
					scoreList.add(otherListScores[0]);
					timesIndex++;
				}
			}else if(timesIndex==2)//�ڶ���
			{
				if(ballDownNumber+Integer.parseInt(otherScores[0])==10)//���λ��Ὣ��ȫ���Ƶ�
				{
					otherScores[1]="/";
					spareCount++;//���е�������1
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
		}else if(roundIndex==10)//��ʮ�ֱ���
		{
			if(timesIndex==1)//��һ����Ϸ
			{
				if(ballDownNumber==10)
				{
					tenScores[0]="x";
					strikeCount++;//ȫ�е�������1
					firstStartGame=true;
					TwiceRestartGame=true;
				}else{
					tenScores[0]=ballDownNumber+"";
				}
				tenListScores[0]=roundIndex+"#"+tenScores[0];
				scoreList.add(tenListScores[0]);
				timesIndex++;
			}else if(timesIndex==2)//�ڶ�����Ϸ
			{
				if(TwiceRestartGame)//���¿�ʼ��Ϸ�����Ѿ�ӵ�е����λ���
				{
					if(ballDownNumber==10)//ȫ���Ƶ�
					{
						tenScores[1]="x";
						strikeCount++;//ȫ�е�������1
						ThirdRestartGame=true;
						firstStartGame=true;
						timesIndex++;
					}else//û��ȫ���Ƶ� Ҳӵ�е����λ���
					{
						firstStartGame=true;
						tenScores[1]=ballDownNumber+"";
						timesIndex++;
					}
					tenListScores[1]=roundIndex+"#"+tenScores[1];
					scoreList.add(tenListScores[1]);
				}else//���ŵ�һ�ν�����Ϸ
				{
					if(ballDownNumber+Integer.parseInt(tenScores[0])==10)//���λ��Ὣ��ȫ���Ƶ�
					{
						tenScores[1]="/";
						spareCount++;//���е�������1
						ThirdRestartGame=true;
						firstStartGame=true;
						tenListScores[1]=roundIndex+"#"+tenScores[1];
						scoreList.add(tenListScores[1]);
						timesIndex++;
					}else
					{
						tenScores[1]=ballDownNumber+"";
						tenScores[2]="��";
						score.put(roundIndex,new String[]{tenScores[0],tenScores[1],tenScores[2]});
						
						tenListScores[1]=roundIndex+"#"+tenScores[1];
						tenListScores[2]=roundIndex+"#"+tenScores[2];
						scoreList.add(tenListScores[1]);
						scoreList.add(tenListScores[2]);
						roundIndex++;
						GameOver=true;//��Ϸ����
					}
				}
			}else if(timesIndex==3)//��������Ϸ ǰ���α���ȫ���Ƶ�
			{
				if(ThirdRestartGame)//��������������¿�ʼ��Ϸ
				{
					if(ballDownNumber==10)//ȫ���Ƶ�
					{
						tenScores[2]="x";
						strikeCount++;//ȫ�е�������1
						score.put(roundIndex,new String[]{tenScores[0],tenScores[1],tenScores[2]});

						tenListScores[2]=roundIndex+"#"+tenScores[2];
						scoreList.add(tenListScores[2]);
						roundIndex++;
						GameOver=true;//��Ϸ����
					}else
					{
						tenScores[2]=ballDownNumber+"";
						score.put(roundIndex,new String[]{tenScores[0],tenScores[1],tenScores[2]});
						
						tenListScores[2]=roundIndex+"#"+tenScores[2];
						scoreList.add(tenListScores[2]);
						roundIndex++;
						GameOver=true;//��Ϸ����
					}
				}else if(TwiceRestartGame&&!ThirdRestartGame)//����ڶ��ξ����¿�ʼ��Ϸ
				{
					if(ballDownNumber+Integer.parseInt(tenScores[1])==10)//���λ��Ὣ��ȫ���Ƶ�
					{
						tenScores[2]="/";
						score.put(roundIndex,new String[]{tenScores[0],tenScores[1],tenScores[2]});
						spareCount++;//���е�������1
						tenListScores[2]=roundIndex+"#"+tenScores[2];
						scoreList.add(tenListScores[2]);
						roundIndex++;
						GameOver=true;//��Ϸ����
					}else
					{
						tenScores[2]=ballDownNumber+"";
						score.put(roundIndex,new String[]{tenScores[0],tenScores[1],tenScores[2]});
						
						tenListScores[2]=roundIndex+"#"+tenScores[2];
						scoreList.add(tenListScores[2]);
						roundIndex++;
						GameOver=true;//��Ϸ����
					}
				}
			}
		}
		
		//���㱾�ַ���===============start===================
		TotalPoints();
		
		for(int j=0;j<=roundIndex-2;j++)//�����Ѿ���������и�ֵ��������
		{
			if(everyPoints[j]!=null&&!everyPoints[j].equals("��")&&!everyPoints[j].equals("б"))//������ǿջ���б
			{
				if(!changePoints[j]&&j==0)//���Ƚ��ָܷ�ֵΪ��һ�ֵķ���
				{
					totalPoints=Integer.parseInt(everyPoints[j]);
					changePoints[j]=true;
				}
				if(!changePoints[j])//�жϸ�ֵ�Ƿ��޸Ĺ�
				{
					totalPoints+=Integer.parseInt(everyPoints[j]);
					everyPoints[j]=totalPoints+"";//��������ǰ��ķ����������
					changePoints[j]=true;
				}
			}
		}
		//���㱾�ַ���===============end=====================
	}
	
	public static void TotalPoints()//���㱾�ַ���
	{
		String[] points=new String[3];
		if(score.size()>0)
		{
			if(roundIndex!=11)//ǰ���ֱ�����ÿ�ֱ����ɼ�
			{
				points=score.get(roundIndex-1);//ȡ��ÿ�α����ĳɼ�
				if(points[0].equals("x"))//�����һ�ξ�ȫ���Ƶ�
				{
					everyPoints[roundIndex-2]="��";
				}else if(!points[0].equals("x"))
				{
					if(points[1].equals("/"))//����ڶ���ȫ���Ƶ�
					{
						everyPoints[roundIndex-2]="б";
					}else if(!points[1].equals("/"))//�����û���Ƶ�
					{
						if(!changePoints[roundIndex-2])
						{
							everyPoints[roundIndex-2]=Integer.parseInt(points[0])+Integer.parseInt(points[1])+"";
						}
					}
				}
				for(int i=0;i<=(roundIndex-2);i++)//�������� �Կ��ַ������и�ֵ
				{
					if(everyPoints[i].equals("��"))
					{
						if(everyPoints[i+1]!=null)//�����һλ�ַ���������
						{
							if(everyPoints[i+1].equals("��"))//�����һλ�ַ�������Ϊ��
							{
								if(everyPoints[i+2]!=null)//�������λ��Ϊ��
								{
									if(everyPoints[i+2].equals("��"))//�����һλ�ַ�������Ϊ��
									{
										everyPoints[i]=10+10+10+"";//ȫ������10��
									}else if(!everyPoints[i+2].equals("��"))
									{
										everyPoints[i]=10+10+Integer.parseInt(points[0])+"";
									}
								}
							}else if(!everyPoints[i+1].equals("��"))//�����һλ�ַ���Ϊ����
							{
								if(everyPoints[i+1].contains("б"))//��һλ�к���б��
								{
									everyPoints[i]=10+10+"";//���������10��
								}else
								{
									everyPoints[i]=10+Integer.parseInt(everyPoints[i+1])+"";//����ֱ�Ӽ��ϵ÷�
								}
							}
						}
					}else if(everyPoints[i].equals("б"))//�����ǰ�ַ���Ϊб
					{
						if(everyPoints[i+1]!=null)
						{
							if(everyPoints[i+1].equals("��"))//��һλ����Ϊ��
							{
								everyPoints[i]=10+10+"";//���������10��
							}else if(!everyPoints[i+1].equals("��"))
							{
								everyPoints[i]=10+Integer.parseInt(points[0])+"";//����ֱ�Ӽ��ϵ÷�
							}
						}
					}
				}
			}else if(roundIndex==11)//��ʮ�ֱ���
			{
				points=score.get(roundIndex-1);//ȡ��ÿ�α����ĳɼ�
				if(points[0].equals("x"))//�����һ�ξ�ȫ���Ƶ�
				{
					if(points[1].equals("x"))//�ڶ���ȫ���Ƶ�
					{
						if(points[2].equals("x"))//������ȫ���Ƶ�
						{
							everyPoints[roundIndex-2]=10+10+10+"";//��������30��
						}else//������û��ȫ���Ƶ�
						{
							everyPoints[roundIndex-2]=10+10+Integer.parseInt(points[2])+"";//ֱ�Ӽ��ϵ����ε÷�
						}
					}else//�ڶ���û�����¿�ʼ��Ϸ
					{
						if(points[2].equals("/"))//�ڶ��ν���ȫ���Ƶ�
						{
							everyPoints[roundIndex-2]=10+10+"";//����10��
						}else
						{
							everyPoints[roundIndex-2]=10+Integer.parseInt(points[1])+Integer.parseInt(points[2])+"";
						}
					}
				}else if(!points[0].equals("x"))//�����һ��û��ȫ���Ƶ�
				{
					if(points[1].equals("/"))//����ڶ���ȫ���Ƶ�
					{
						if(points[2].equals("x"))
						{
							everyPoints[roundIndex-2]=10+10+"";
						}else
						{
							everyPoints[roundIndex-2]=10+Integer.parseInt(points[2])+"";
						}
					}else if(!points[1].equals("/"))//�����û���Ƶ�
					{
						everyPoints[roundIndex-2]=Integer.parseInt(points[0])+Integer.parseInt(points[1])+"";
					}
				}
				
				for(int i=0;i<everyPoints.length;i++)
				{
					if(everyPoints[i].equals("��"))
					{
						if(i+1<9)//ǰ8�����ַ���Ϊ�յ�����
						{
							if(everyPoints[i+1]!=null)
							{
								if(everyPoints[i+1].equals("��"))
								{
									if(everyPoints[i+2]!=null)//�������λ��Ϊ��
									{
										if(everyPoints[i+2].equals("��"))
										{
											everyPoints[i]=10+10+10+"";//ȫ������10��
										}else
										{
											if(points[0].equals("x"))
											{
												everyPoints[i]=10+10+10+"";//ȫ������10��
											}else if(!points[0].equals("x"))
											{
												everyPoints[i]=10+10+Integer.parseInt(points[0])+"";//ȫ������10��
											}
										}
									}
								}
							}
						}else//��9�����ַ���Ϊ�յ����� ����ϵ�ʮ�ֵ�ǰ������ķ���
						{
							if(points[0].equals("x"))
							{
								if(points[1].equals("x"))
								{
									everyPoints[i]=10+10+10+"";//ȫ������10��
								}else
								{
									everyPoints[i]=10+10+Integer.parseInt(points[1])+"";//ȫ������10��
								}
							}else
							{
								if(points[1].equals("/"))
								{
									everyPoints[i]=10+10+"";//ȫ������10��
								}else
								{
									everyPoints[i]=10+Integer.parseInt(points[0])+Integer.parseInt(points[1])+"";//ȫ������10��
								}
							}
						}
					}
				}
			}
		}
	}
}
