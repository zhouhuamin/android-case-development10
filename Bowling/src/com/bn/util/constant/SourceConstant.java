package com.bn.util.constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import android.annotation.SuppressLint;
import com.bn.object.Earth;
import com.bn.object.GameObject;
import com.bn.object.LoadedObjectVertexNormalTexture;
import com.bn.object.Moon;
import com.bn.object.SkyCloud;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;

@SuppressLint("UseSparseArrays")
public class SourceConstant {
	
	//================GameView   start=============
	public static ArrayList<RigidBody> Bottles=new ArrayList<RigidBody>();//存放保龄球列表	
	public static ArrayList<RigidBody> deleteBottles=new ArrayList<RigidBody>();//存放保龄球列表
	public static Queue<float[]> qf=new LinkedList<float[]>();//球速度的队列
	public static Queue<float[][]> bTransform=new LinkedList<float[][]>();//瓶子姿态位置数据
	public static Queue<float[]> ballTransform=new LinkedList<float[]>();//姿态位置数据
	//每局十轮，每轮两次机会，每个map对象记录每轮每球的得分
	public static Map<String,Float> mf=new HashMap<String,Float>();//记录每轮的得分	
	
//	public static GameObject ball=null;//球绘制对象
	public static RigidBody wall;
	public static Map<Integer,GameObject>  hm=new HashMap<Integer,GameObject>();
	public static ArrayList<GameObject>  alGBall=new ArrayList<GameObject> ();

	public static LoadedObjectVertexNormalTexture ball_model;//球
	public static LoadedObjectVertexNormalTexture bottle_model;//瓶子
	public static LoadedObjectVertexNormalTexture kjg_model;//科技馆
	public static LoadedObjectVertexNormalTexture gd_model;//轨道
	public static LoadedObjectVertexNormalTexture sm_model;//沙漠
	public static LoadedObjectVertexNormalTexture xk_model;//星空
	public static LoadedObjectVertexNormalTexture dimian_model;//沙漠地面
	public static LoadedObjectVertexNormalTexture tree2_model;//沙漠树
	public static LoadedObjectVertexNormalTexture tree3_model;//沙漠树
	public static LoadedObjectVertexNormalTexture smgd_model;//沙漠轨道
	public static LoadedObjectVertexNormalTexture smys_model;//沙漠岩石
	public static LoadedObjectVertexNormalTexture sky_model;//天空模型
	public static LoadedObjectVertexNormalTexture skygd_model1;//天空半圆形轨轨道
	public static LoadedObjectVertexNormalTexture skygd_model2;//天空轨道
	public static LoadedObjectVertexNormalTexture feixingwu1_model;//天空飞行物
	public static LoadedObjectVertexNormalTexture feixingwu2_model;//天空飞行物
	public static LoadedObjectVertexNormalTexture feixingwu3_model;//天空飞行物
	
	public static int kjgId;//科技馆纹理
	public static int gdId;//轨道纹理
	public static int tree1Id;//沙漠树纹理
	public static int tree2Id;//沙漠树纹理
	public static int desertId;//沙漠沙滩纹理
	public static int smgdId;//沙漠轨道纹理
	public static  int smysId;//沙漠岩石纹理
	public static  int cloudId;////沙漠云纹理
	public static int skyId;//星空背景纹理
	public static int skygdId;//星空轨道纹理
	public static int feixingwu1Id;//飞行物1纹理
	public static int feixingwu2Id;//飞行物2纹理
	public static int textureIdEarth;//系统分配的地球纹理id
	public static int textureIdEarthNight;//系统分配的地球夜晚纹理id
	public static int textureIdMoon;//系统分配的月球纹理id    
	public static int xingId;//星球纹理
    public static int BallId;//球的纹理id
    public static int BottleId;//瓶子的纹理id
	public static int btmgdId;//半透明轨道纹理
	public static int btmsmgdId;//半透明轨道纹理
	public static int btmskygdId;//半透明星空轨道纹理
	
	public static SkyCloud cloud;//沙漠——云绘制者	
//	public static TextureWorld tw;//网
	public static Earth earth;//地球
	public static Moon moon;//月球
	public static Moon xing1;//月球
	
	
	public static int ballCount=0;//滑球次数
	public static boolean isMoveFlag=false;//球是否移动
	public static boolean isMoveGuiDaoFlag=true;//是否移动轨道的标识
	public static boolean isPause=false;
	public static boolean isDrawScore = true;//是否绘制分数框
	public static boolean isDrawCurrentScore=false;//是否绘制当前分数
	public static boolean isScoreDown=true;//分数框缓缓落下
	public static boolean isCrashWall=false;//是否碰到墙
	public static boolean firstStartGame=true;//第一次开始游戏
	public static boolean updateRound=false;//开始下一轮游戏
	public static int particleIndex = 0;//0---画火焰 1---画碰撞
	public static boolean isDrawFire=false;//是否绘制火焰
	public static boolean isDrawFireWorks=false;//是否绘制烟火
	public static float[] crashBall=new float[3];
	public static boolean isDrawFireOver=false;
	public static boolean isGamePlay=true;//游戏是否进行
	public static boolean drawCameraTip=false;//是否绘制上传录像提示框
	public static boolean ShangChuanView=false;//是否处于上传界面
	public static boolean isDrawAN=true;
	public static boolean isCameraMove=true;//摄像机是否移动
	public static boolean isCastBall=false;//是否投球
	
	public static boolean inGameOverView=false;//处于游戏结束 显示分数界面
	
	public static int strikeCount=0;//全中的数量
	public static int spareCount=0;//补中的数量
	public static int luoGouCount=0;//落沟球的数量
	
	public static float CAMERA_START_LEFT= 0;
	public static float CAMERA_START_RIGHT= 130;
	public static float CAMERA_START_TOP= 1635;
	public static float CAMERA_START_BOTTOM= 1765;
	
	public static float CAMERA_TIP_NO_LEFT= 240;
	public static float CAMERA_TIP_NO_RIGHT= 420;
	public static float CAMERA_TIP_NO_TOP= 1070;
	public static float CAMERA_TIP_NO_BOTTOM= 1190;
	
	public static float CAMERA_TIP_YES_LEFT= 605;
	public static float CAMERA_TIP_YES_RIGHT= 810;
	public static float CAMERA_TIP_YES_TOP= 1070;
	public static float CAMERA_TIP_YES_BOTTOM= 1190;
	//================GameView   end=============
	
	//================物理世界   start=============
	public static DiscreteDynamicsWorld dynamicsWorld;//物理世界对象
	public static CollisionShape capsuleShape;//共用的胶囊形状
	public static CollisionShape sphereShape;//共用的球体形状
	public static CollisionShape boxShape;//共用的立方体
	public static CollisionShape planeShape;//共用的平面形状
	public static CollisionShape[] csa=new CollisionShape[3];//圆柱、立方体组合
	
	public static CollisionShape capsuleShape2;//胶囊形状
	public static CollisionShape boxShape2;//立方体
	public static CollisionShape boxShape3;//立方体
	//================物理世界   end=============
	
	//================MainView   start===================
	public static float BOTTON_LEFT=165;//单人游戏按钮
	public static float BOTTON_RIGHT=915;//单人游戏按钮
	public static float DANREN_GAME_UP=930;//单人游戏按钮
	public static float DANREN_GAME_DOWN=1070;//单人游戏按钮
	
	public static float OPTION_UP=1130;//单人游戏按钮
	public static float DOPTION_DOWN=1270;//单人游戏按钮
	
	public static float HELP_UP=1330;//单人游戏按钮
	public static float HELP_DOWN=1470;//单人游戏按钮
	//================MainView   end===================
	
	
	//================HelpView   start===================
	public static float BACK_HELP_LEFT=25;
	public static float BACK_HELP_RIGHT=175;
	public static float BACK_HELP_UP=1605;
	public static float BACK_HELP_DOWN=1755;
	//================HelpView   end===================

	//=============暂停界面start==============
    public static float isPause_left=950;
    public static float isPause_right=1300;
    public static float isPause_top=700;
    public static float isPause_bottom=1600;
	
	
    public static float Pause_left=900;//暂停按钮
    public static float Pause_right=1400;
    public static float Pause_top=1650;
    public static float Pause_bottom=1780;

	public static float Selectpz_Left=960;//选择瓶子按钮
	public static float Selectpz_Right=1210;
	public static float Selectpz_Top=755;
	public static float Selectpz_Bottom=930;
	
	public static float Selectqiu_Left=960;//选择保龄球按钮
	public static float Selectqiu_Right=1210;
	public static float Selectqiu_Top=1000;
	public static float Selectqiu_Bottom=1115;
	
	public static float Pauseshezhi_Left=960;//设置按钮
	public static float Pauseshezhi_Right=1210;
	public static float Pauseshezhi_Top=1200;
	public static float Pauseshezhi_Bottom=1315;
	
	public static float PauseBackMain_Left=960;//返回主界面按钮
	public static float PauseBackMain_Right=1210;
	public static float PauseBackMain_Top=1350;
	public static float PauseBackMain_Bottom=1550;
	
	public static float BackMainfou_Left=200;//取消会主界面按钮
	public static float BackMainfou_Right=400;
	public static float BackMainfou_Top=850;
	public static float BackMainfou_Bottom=1050;
	
	public static float BackMainqueren_Left=650;//确认返回主界面按钮
	public static float BackMainqueren_Right=850;
	public static float BackMainqueren_Top=850;
	public static float BackMainqueren_Bottom=1050;
	
	//=============设置界面start==============	
	public static float SheZhiback_Left=100;//返回游戏场景按钮
	public static float SheZhiback_Right=280;
	public static float SheZhiback_Top=1150;
	public static float SheZhiback_Bottom=1350;
	
	public static float SheZhiyinyue_Left=650;//设置音乐
	public static float SheZhiyinyue_Right=950;
	public static float SheZhiyinyue_Top=600;
	public static float SheZhiyinyue_Bottom=800;
	
	
	public static float SheZhiyinxiao_Left=650;//设置音效
	public static float SheZhiyinxiao_Right=950;
	public static float SheZhiyinxiao_Top=810;
	public static float SheZhiyinxiao_Bottom=950;
	
	public static float SheZhishexiangji_Left=650;//设置摄像机
	public static float SheZhishexiangji_Right=950;
	public static float SheZhishexiangji_Top=950;
	public static float SheZhishexiangji_Bottom=1100;
	//=============选择瓶子界面start==============	
	public static float Selectpzback_Left=160;//返回游戏场景
	public static float Selectpzback_Right=300;
	public static float Selectpzback_Top=1000;
	public static float Selectpzback_Bottom=1400;
	
	public static float Selectpz1_Left=160;//选择瓶子1
	public static float Selectpz1_Right=350;
	public static float Selectpz1_Top=400;
	public static float Selectpz1_Bottom=650;
	
	public static float Selectpz2_Left=400;//选择瓶子2
	public static float Selectpz2_Right=600;
	public static float Selectpz2_Top=400;
	public static float Selectpz2_Bottom=650;
	
	public static float Selectpz3_Left=650;//选择瓶子3
	public static float Selectpz3_Right=900;
	public static float Selectpz3_Top=400;
	public static float Selectpz3_Bottom=650;
	
	public static float Selectpz4_Left=160;//选择瓶子4
	public static float Selectpz4_Right=350;
	public static float Selectpz4_Top=700;
	public static float Selectpz4_Bottom=1100;
	
	public static float Selectpz5_Left=360;//选择瓶子5
	public static float Selectpz5_Right=600;
	public static float Selectpz5_Top=700;
	public static float Selectpz5_Bottom=1100;
	
	public static float Selectpz6_Left=650;//选择瓶子6
	public static float Selectpz6_Right=1000;
	public static float Selectpz6_Top=700;
	public static float Selectpz6_Bottom=1100;
	
	//=============选择保龄球界面start==============		
	public static float Selectqiuback_Left=160;//返回游戏界面
	public static float Selectqiuback_Right=300;
	public static float Selectqiuback_Top=1000;
	public static float Selectqiuback_Bottom=1400;
	
	public static float Selectqiu1_Left=160;//选择球1
	public static float Selectqiu1_Right=350;
	public static float Selectqiu1_Top=400;
	public static float Selectqiu1_Bottom=650;
	
	public static float Selectqiu2_Left=400;//选择球2
	public static float Selectqiu2_Right=600;
	public static float Selectqiu2_Top=400;
	public static float Selectqiu2_Bottom=650;
	
	public static float Selectqiu3_Left=650;//选择球3
	public static float Selectqiu3_Right=900;
	public static float Selectqiu3_Top=400;
	public static float Selectqiu3_Bottom=650;
	
	public static float Selectqiu4_Left=160;//选择球4
	public static float Selectqiu4_Right=350;
	public static float Selectqiu4_Top=700;
	public static float Selectqiu4_Bottom=1100;
	
	public static float Selectqiu5_Left=360;//选择球5
	public static float Selectqiu5_Right=600;
	public static float Selectqiu5_Top=700;
	public static float Selectqiu5_Bottom=1100;
	
	public static float Selectqiu6_Left=650;//选择球6
	public static float Selectqiu6_Right=1000;
	public static float Selectqiu6_Top=700;
	public static float Selectqiu6_Bottom=1100;
}
