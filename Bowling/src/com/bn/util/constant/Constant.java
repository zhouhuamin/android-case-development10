package com.bn.util.constant;

import com.bn.util.screenscale.ScreenScaleResult;

public class Constant 
{
	//声音
	public static final int BALL_BATTLE_BEATER = 1;//球撞击瓶子
	public static final int BALL_WALL_SOUND= 2;//球撞击墙
	public static final int BUTTON_PRESS=3;//点击按钮
	public static final int APPLAUSE=4;//鼓掌
	public static final int BALL_ROLL=5;//球滚动
	public static final int INIT_BOTTLE=6;//初始化十个瓶子
	//音乐和音效的开关
	public static boolean musicOff = false;
	public static boolean effictOff = false;
	
	//台子
    public static float GUIDAO_HALF_X=4.0f;//4.4
    public static float GUIDAO_HALF_Y=1.9f;
    public static float GUIDAO_HALF_Z=57;//74.0f;
    public static float GUIDAO_MAX_RIGHT=3.56f;
    public static float GUIDAO_MIN_LEFT=-3.36f;
    //网
    public static float WANG_HALF_X=10;
    public static float WANG_HALF_Y=16;
    public static float WANG_HALF_Z=1.5f;
	
	public static int sceneIndex=1;//场景选择 场景1---太空 场景2---科幻 场景3---沙漠
	public static int countTurn=1;//第几轮
	
    public static float skyX=25f;
    public static float skyY=0f;
    public static float eAngle=0;//地球自转角度    
    public static float cAngle=0;//天球自转的角度
    public static boolean threadFlag=true;//星空旋转标识
    
	public static int status=-1;//状态标志位
	public static int BALL_SUM=10;//瓶子数量
	public static float TOUCH_DIS=0.5f;//触控的阈值
    public final static float FLOOR_Y=-2*GUIDAO_HALF_Y;//地板的高度值
    public static int[] ballNumber=new int[10];//球的编号
    public static float BOWLING_BALL_Z=16.25f;//保龄球的z坐标
	//===========物理世界中的相关数据====start================//
    public final static float UNIT_SIZE=0.5f;//基本尺寸单元
    public final static float TIME_STEP=1.0f/60;//模拟的周期
    public final static int MAX_SUB_STEPS=5;//最大的子步数
    //===========物理世界中的相关数据====end================//
    
    //===========摄像机相关数据======start==========//
    public static float EYE_X=0;//观察者的位置x
    public static float EYE_Y=6;//观察者的位置y4
    public static float EYE_Z=0;//0;//观察者的位置z3
    public static float TARGET_X=0;//目标的位置X
    public static float TARGET_Y=0;//目的位置Y
    public static float TARGET_Z=-24f;//目标的位置Z
	public static float UP_X=0;//摄像机up向量x值
	public static float UP_Y=0.9728f;//摄像机up向量y值0.6f
	public static float UP_Z=-0.233f;//摄像机up向量z值-0.8f
	
	public static float CAMERA_LIMIT1_MIN=-25f;//摄像机开始减速的位置
	public static float CAMERA_LIMIT2_MIN=-35f;//摄像机观察者可移动的边界最小值
	public static float CAMERA_LIMIT_SPAN=2;//阈值
	public static float CAMERA_A_SPAN=0.001f;//间距变化步进
	public static float CAMERA_LIMIT_MAX=24f;//摄像机观察者可移动的边界最大值
	public static float CAMERA_SPAN=0.24f;//摄像机后退的步进
    //===========摄像机相关数据======end=============//
    
    //程序所用锁
    public static Object lockAdd = new Object();//向物理世界添加刚体的锁
    public static Object lockDelete = new Object();//从物理世界删除刚体的锁
    public static Object lockAll = new Object();//拷贝瓶子
    public static Object lockV = new Object();//速度更新用锁
    public static Object lockTransform=new Object();//瓶子姿态位置用锁
    public static Object lockBallTransform=new Object();//瓶子姿态位置用锁
    public static Object lockBall=new Object();//球
    //=======屏幕自适应数据=======start=======================================================//
	public static float SCREEN_WIDTH_STANDARD = 1080;//720;//屏幕标准宽度	
	public static float SCREEN_HEIGHT_STANDARD = 1920;//1280;//屏幕标准高度
	public static float RATIO = SCREEN_WIDTH_STANDARD/SCREEN_HEIGHT_STANDARD;//屏幕标准比例--即透视投影的比例
	public static ScreenScaleResult ssr;//屏幕自适应对象
	//=======屏幕自适应数据=======end=========================================================//
	
	//=======摄像机坐标系中坐标的相关数据=======start===============//
	public static float LEFT=RATIO;//视角left值
	public static float RIGHT=RATIO;//视角right值
	public static float TOP=1.0f;//视角top值
	public static float BOTTOM=1.0f;//视角bottom值
	public static float NEAR=2.0f;//视角near值
	public static float FAR=500.0f;//视角far值
	//=======摄像机坐标系中坐标的相关数据=======end===============//
    public static float BALL_RADISU=0.44f;//0.427f;//球的半径
    public static float BALL_Z_MIN=-52.5f;//球的边界值，用于设置status的值
    public static float BALL_QUALITY=7.4f*64;//球中7.4kg
    //=============瓶子的相关变量=======start===================//
	public static float BOTTLE_QUALITY=1.53f;//瓶子中1.64kg
    public static float BOX_HALF_WIDTH=0.255f;//长方体盒子的宽度
    public static float BOX_HALF_HEIGHT=0.027f;//长方体盒子的高度    
    public static float BOTTOM_BALL_RADIUS=0.29f;//0.29f;///瓶子球的半径    
    public static float CAPSULE_RADIUS=0.14f;//瓶子胶囊半径
    public static float CAPSULE_HEIGHT=0.625f;//瓶子胶囊高度 
	public static float RANGE_MAX=0.434f;//瓶子倒下的高度上限	
	//物理世界中球瓶和球的高度比为1.748	
    public static float CAPSULE_RADIUS2=0.35f;//胶囊半径
    public static float CAPSULE_HEIGHT2=74;//胶囊高度        
	public static float BALL_TO_EYE=8;//球到观察者的距离
	public static float BALL_TO_TARGET=17;//球到目标点的距离	
    public static float BATTLE_ORIGN_Z=-48.67f;//瓶子z坐标的起始值 
    public static float BATTLE_ORIGN_X=-0.575f;//-0.6f;//瓶子x坐标的起始值  
    public static float BATTLE_SPAN_X=1.15f;//1.1f;//1.2f;//x坐标的间距
    public static float BATTLE_SPAN_Z=0.996f;//0.97f;//1.04f;//0.953f;//z坐标的间距
    //=============瓶子的相关变量=======end===================//
    
    public static float fromPixSizeToNearSize(float size)
	{
		return size*2/SCREEN_HEIGHT_STANDARD;
	}
	//屏幕x坐标到视口x坐标
	public static float fromScreenXToNearX(float x)
	{
		return (x-SCREEN_WIDTH_STANDARD/2)/(SCREEN_HEIGHT_STANDARD/2);
	}
	//屏幕y坐标到视口y坐标
	public static float fromScreenYToNearY(float y)
	{
		return -(y-SCREEN_HEIGHT_STANDARD/2)/(SCREEN_HEIGHT_STANDARD/2);
	}
	//实际屏幕x坐标到标准屏幕x坐标
	public static float fromRealScreenXToStandardScreenX(float rx)
	{
		return (rx-ssr.lucX)/ssr.ratio;
	}
	//实际屏幕y坐标到标准屏幕y坐标
	public static float fromRealScreenYToStandardScreenY(float ry)
	{
		return (ry-ssr.lucY)/ssr.ratio;
	}
	//从标准屏幕到实际屏幕x坐标
	public static float fromStandardScreenXToRealScreenX(float tx)
	{
		return tx*ssr.ratio+ssr.lucX;
	}
	//从标准屏幕到实际屏幕y坐标
	public static float fromStandardScreenYToRealScreenY(float ty)
	{
		return ty*ssr.ratio+ssr.lucY;
	}
	public static float fromStandardScreenSizeToRealScreenSize(float size)
	{
		return size*ssr.ratio;
	}
}
