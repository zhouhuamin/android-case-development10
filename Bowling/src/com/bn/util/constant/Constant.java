package com.bn.util.constant;

import com.bn.util.screenscale.ScreenScaleResult;

public class Constant 
{
	//����
	public static final int BALL_BATTLE_BEATER = 1;//��ײ��ƿ��
	public static final int BALL_WALL_SOUND= 2;//��ײ��ǽ
	public static final int BUTTON_PRESS=3;//�����ť
	public static final int APPLAUSE=4;//����
	public static final int BALL_ROLL=5;//�����
	public static final int INIT_BOTTLE=6;//��ʼ��ʮ��ƿ��
	//���ֺ���Ч�Ŀ���
	public static boolean musicOff = false;
	public static boolean effictOff = false;
	
	//̨��
    public static float GUIDAO_HALF_X=4.0f;//4.4
    public static float GUIDAO_HALF_Y=1.9f;
    public static float GUIDAO_HALF_Z=57;//74.0f;
    public static float GUIDAO_MAX_RIGHT=3.56f;
    public static float GUIDAO_MIN_LEFT=-3.36f;
    //��
    public static float WANG_HALF_X=10;
    public static float WANG_HALF_Y=16;
    public static float WANG_HALF_Z=1.5f;
	
	public static int sceneIndex=1;//����ѡ�� ����1---̫�� ����2---�ƻ� ����3---ɳĮ
	public static int countTurn=1;//�ڼ���
	
    public static float skyX=25f;
    public static float skyY=0f;
    public static float eAngle=0;//������ת�Ƕ�    
    public static float cAngle=0;//������ת�ĽǶ�
    public static boolean threadFlag=true;//�ǿ���ת��ʶ
    
	public static int status=-1;//״̬��־λ
	public static int BALL_SUM=10;//ƿ������
	public static float TOUCH_DIS=0.5f;//���ص���ֵ
    public final static float FLOOR_Y=-2*GUIDAO_HALF_Y;//�ذ�ĸ߶�ֵ
    public static int[] ballNumber=new int[10];//��ı��
    public static float BOWLING_BALL_Z=16.25f;//�������z����
	//===========���������е��������====start================//
    public final static float UNIT_SIZE=0.5f;//�����ߴ絥Ԫ
    public final static float TIME_STEP=1.0f/60;//ģ�������
    public final static int MAX_SUB_STEPS=5;//�����Ӳ���
    //===========���������е��������====end================//
    
    //===========������������======start==========//
    public static float EYE_X=0;//�۲��ߵ�λ��x
    public static float EYE_Y=6;//�۲��ߵ�λ��y4
    public static float EYE_Z=0;//0;//�۲��ߵ�λ��z3
    public static float TARGET_X=0;//Ŀ���λ��X
    public static float TARGET_Y=0;//Ŀ��λ��Y
    public static float TARGET_Z=-24f;//Ŀ���λ��Z
	public static float UP_X=0;//�����up����xֵ
	public static float UP_Y=0.9728f;//�����up����yֵ0.6f
	public static float UP_Z=-0.233f;//�����up����zֵ-0.8f
	
	public static float CAMERA_LIMIT1_MIN=-25f;//�������ʼ���ٵ�λ��
	public static float CAMERA_LIMIT2_MIN=-35f;//������۲��߿��ƶ��ı߽���Сֵ
	public static float CAMERA_LIMIT_SPAN=2;//��ֵ
	public static float CAMERA_A_SPAN=0.001f;//���仯����
	public static float CAMERA_LIMIT_MAX=24f;//������۲��߿��ƶ��ı߽����ֵ
	public static float CAMERA_SPAN=0.24f;//��������˵Ĳ���
    //===========������������======end=============//
    
    //����������
    public static Object lockAdd = new Object();//������������Ӹ������
    public static Object lockDelete = new Object();//����������ɾ���������
    public static Object lockAll = new Object();//����ƿ��
    public static Object lockV = new Object();//�ٶȸ�������
    public static Object lockTransform=new Object();//ƿ����̬λ������
    public static Object lockBallTransform=new Object();//ƿ����̬λ������
    public static Object lockBall=new Object();//��
    //=======��Ļ����Ӧ����=======start=======================================================//
	public static float SCREEN_WIDTH_STANDARD = 1080;//720;//��Ļ��׼���	
	public static float SCREEN_HEIGHT_STANDARD = 1920;//1280;//��Ļ��׼�߶�
	public static float RATIO = SCREEN_WIDTH_STANDARD/SCREEN_HEIGHT_STANDARD;//��Ļ��׼����--��͸��ͶӰ�ı���
	public static ScreenScaleResult ssr;//��Ļ����Ӧ����
	//=======��Ļ����Ӧ����=======end=========================================================//
	
	//=======���������ϵ��������������=======start===============//
	public static float LEFT=RATIO;//�ӽ�leftֵ
	public static float RIGHT=RATIO;//�ӽ�rightֵ
	public static float TOP=1.0f;//�ӽ�topֵ
	public static float BOTTOM=1.0f;//�ӽ�bottomֵ
	public static float NEAR=2.0f;//�ӽ�nearֵ
	public static float FAR=500.0f;//�ӽ�farֵ
	//=======���������ϵ��������������=======end===============//
    public static float BALL_RADISU=0.44f;//0.427f;//��İ뾶
    public static float BALL_Z_MIN=-52.5f;//��ı߽�ֵ����������status��ֵ
    public static float BALL_QUALITY=7.4f*64;//����7.4kg
    //=============ƿ�ӵ���ر���=======start===================//
	public static float BOTTLE_QUALITY=1.53f;//ƿ����1.64kg
    public static float BOX_HALF_WIDTH=0.255f;//��������ӵĿ��
    public static float BOX_HALF_HEIGHT=0.027f;//��������ӵĸ߶�    
    public static float BOTTOM_BALL_RADIUS=0.29f;//0.29f;///ƿ����İ뾶    
    public static float CAPSULE_RADIUS=0.14f;//ƿ�ӽ��Ұ뾶
    public static float CAPSULE_HEIGHT=0.625f;//ƿ�ӽ��Ҹ߶� 
	public static float RANGE_MAX=0.434f;//ƿ�ӵ��µĸ߶�����	
	//������������ƿ����ĸ߶ȱ�Ϊ1.748	
    public static float CAPSULE_RADIUS2=0.35f;//���Ұ뾶
    public static float CAPSULE_HEIGHT2=74;//���Ҹ߶�        
	public static float BALL_TO_EYE=8;//�򵽹۲��ߵľ���
	public static float BALL_TO_TARGET=17;//��Ŀ���ľ���	
    public static float BATTLE_ORIGN_Z=-48.67f;//ƿ��z�������ʼֵ 
    public static float BATTLE_ORIGN_X=-0.575f;//-0.6f;//ƿ��x�������ʼֵ  
    public static float BATTLE_SPAN_X=1.15f;//1.1f;//1.2f;//x����ļ��
    public static float BATTLE_SPAN_Z=0.996f;//0.97f;//1.04f;//0.953f;//z����ļ��
    //=============ƿ�ӵ���ر���=======end===================//
    
    public static float fromPixSizeToNearSize(float size)
	{
		return size*2/SCREEN_HEIGHT_STANDARD;
	}
	//��Ļx���굽�ӿ�x����
	public static float fromScreenXToNearX(float x)
	{
		return (x-SCREEN_WIDTH_STANDARD/2)/(SCREEN_HEIGHT_STANDARD/2);
	}
	//��Ļy���굽�ӿ�y����
	public static float fromScreenYToNearY(float y)
	{
		return -(y-SCREEN_HEIGHT_STANDARD/2)/(SCREEN_HEIGHT_STANDARD/2);
	}
	//ʵ����Ļx���굽��׼��Ļx����
	public static float fromRealScreenXToStandardScreenX(float rx)
	{
		return (rx-ssr.lucX)/ssr.ratio;
	}
	//ʵ����Ļy���굽��׼��Ļy����
	public static float fromRealScreenYToStandardScreenY(float ry)
	{
		return (ry-ssr.lucY)/ssr.ratio;
	}
	//�ӱ�׼��Ļ��ʵ����Ļx����
	public static float fromStandardScreenXToRealScreenX(float tx)
	{
		return tx*ssr.ratio+ssr.lucX;
	}
	//�ӱ�׼��Ļ��ʵ����Ļy����
	public static float fromStandardScreenYToRealScreenY(float ty)
	{
		return ty*ssr.ratio+ssr.lucY;
	}
	public static float fromStandardScreenSizeToRealScreenSize(float size)
	{
		return size*ssr.ratio;
	}
}
