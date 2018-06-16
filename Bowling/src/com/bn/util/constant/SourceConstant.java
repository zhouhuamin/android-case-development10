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
	public static ArrayList<RigidBody> Bottles=new ArrayList<RigidBody>();//��ű������б�	
	public static ArrayList<RigidBody> deleteBottles=new ArrayList<RigidBody>();//��ű������б�
	public static Queue<float[]> qf=new LinkedList<float[]>();//���ٶȵĶ���
	public static Queue<float[][]> bTransform=new LinkedList<float[][]>();//ƿ����̬λ������
	public static Queue<float[]> ballTransform=new LinkedList<float[]>();//��̬λ������
	//ÿ��ʮ�֣�ÿ�����λ��ᣬÿ��map�����¼ÿ��ÿ��ĵ÷�
	public static Map<String,Float> mf=new HashMap<String,Float>();//��¼ÿ�ֵĵ÷�	
	
//	public static GameObject ball=null;//����ƶ���
	public static RigidBody wall;
	public static Map<Integer,GameObject>  hm=new HashMap<Integer,GameObject>();
	public static ArrayList<GameObject>  alGBall=new ArrayList<GameObject> ();

	public static LoadedObjectVertexNormalTexture ball_model;//��
	public static LoadedObjectVertexNormalTexture bottle_model;//ƿ��
	public static LoadedObjectVertexNormalTexture kjg_model;//�Ƽ���
	public static LoadedObjectVertexNormalTexture gd_model;//���
	public static LoadedObjectVertexNormalTexture sm_model;//ɳĮ
	public static LoadedObjectVertexNormalTexture xk_model;//�ǿ�
	public static LoadedObjectVertexNormalTexture dimian_model;//ɳĮ����
	public static LoadedObjectVertexNormalTexture tree2_model;//ɳĮ��
	public static LoadedObjectVertexNormalTexture tree3_model;//ɳĮ��
	public static LoadedObjectVertexNormalTexture smgd_model;//ɳĮ���
	public static LoadedObjectVertexNormalTexture smys_model;//ɳĮ��ʯ
	public static LoadedObjectVertexNormalTexture sky_model;//���ģ��
	public static LoadedObjectVertexNormalTexture skygd_model1;//��հ�Բ�ι���
	public static LoadedObjectVertexNormalTexture skygd_model2;//��չ��
	public static LoadedObjectVertexNormalTexture feixingwu1_model;//��շ�����
	public static LoadedObjectVertexNormalTexture feixingwu2_model;//��շ�����
	public static LoadedObjectVertexNormalTexture feixingwu3_model;//��շ�����
	
	public static int kjgId;//�Ƽ�������
	public static int gdId;//�������
	public static int tree1Id;//ɳĮ������
	public static int tree2Id;//ɳĮ������
	public static int desertId;//ɳĮɳ̲����
	public static int smgdId;//ɳĮ�������
	public static  int smysId;//ɳĮ��ʯ����
	public static  int cloudId;////ɳĮ������
	public static int skyId;//�ǿձ�������
	public static int skygdId;//�ǿչ������
	public static int feixingwu1Id;//������1����
	public static int feixingwu2Id;//������2����
	public static int textureIdEarth;//ϵͳ����ĵ�������id
	public static int textureIdEarthNight;//ϵͳ����ĵ���ҹ������id
	public static int textureIdMoon;//ϵͳ�������������id    
	public static int xingId;//��������
    public static int BallId;//�������id
    public static int BottleId;//ƿ�ӵ�����id
	public static int btmgdId;//��͸���������
	public static int btmsmgdId;//��͸���������
	public static int btmskygdId;//��͸���ǿչ������
	
	public static SkyCloud cloud;//ɳĮ�����ƻ�����	
//	public static TextureWorld tw;//��
	public static Earth earth;//����
	public static Moon moon;//����
	public static Moon xing1;//����
	
	
	public static int ballCount=0;//�������
	public static boolean isMoveFlag=false;//���Ƿ��ƶ�
	public static boolean isMoveGuiDaoFlag=true;//�Ƿ��ƶ�����ı�ʶ
	public static boolean isPause=false;
	public static boolean isDrawScore = true;//�Ƿ���Ʒ�����
	public static boolean isDrawCurrentScore=false;//�Ƿ���Ƶ�ǰ����
	public static boolean isScoreDown=true;//�����򻺻�����
	public static boolean isCrashWall=false;//�Ƿ�����ǽ
	public static boolean firstStartGame=true;//��һ�ο�ʼ��Ϸ
	public static boolean updateRound=false;//��ʼ��һ����Ϸ
	public static int particleIndex = 0;//0---������ 1---����ײ
	public static boolean isDrawFire=false;//�Ƿ���ƻ���
	public static boolean isDrawFireWorks=false;//�Ƿ�����̻�
	public static float[] crashBall=new float[3];
	public static boolean isDrawFireOver=false;
	public static boolean isGamePlay=true;//��Ϸ�Ƿ����
	public static boolean drawCameraTip=false;//�Ƿ�����ϴ�¼����ʾ��
	public static boolean ShangChuanView=false;//�Ƿ����ϴ�����
	public static boolean isDrawAN=true;
	public static boolean isCameraMove=true;//������Ƿ��ƶ�
	public static boolean isCastBall=false;//�Ƿ�Ͷ��
	
	public static boolean inGameOverView=false;//������Ϸ���� ��ʾ��������
	
	public static int strikeCount=0;//ȫ�е�����
	public static int spareCount=0;//���е�����
	public static int luoGouCount=0;//�乵�������
	
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
	
	//================��������   start=============
	public static DiscreteDynamicsWorld dynamicsWorld;//�����������
	public static CollisionShape capsuleShape;//���õĽ�����״
	public static CollisionShape sphereShape;//���õ�������״
	public static CollisionShape boxShape;//���õ�������
	public static CollisionShape planeShape;//���õ�ƽ����״
	public static CollisionShape[] csa=new CollisionShape[3];//Բ�������������
	
	public static CollisionShape capsuleShape2;//������״
	public static CollisionShape boxShape2;//������
	public static CollisionShape boxShape3;//������
	//================��������   end=============
	
	//================MainView   start===================
	public static float BOTTON_LEFT=165;//������Ϸ��ť
	public static float BOTTON_RIGHT=915;//������Ϸ��ť
	public static float DANREN_GAME_UP=930;//������Ϸ��ť
	public static float DANREN_GAME_DOWN=1070;//������Ϸ��ť
	
	public static float OPTION_UP=1130;//������Ϸ��ť
	public static float DOPTION_DOWN=1270;//������Ϸ��ť
	
	public static float HELP_UP=1330;//������Ϸ��ť
	public static float HELP_DOWN=1470;//������Ϸ��ť
	//================MainView   end===================
	
	
	//================HelpView   start===================
	public static float BACK_HELP_LEFT=25;
	public static float BACK_HELP_RIGHT=175;
	public static float BACK_HELP_UP=1605;
	public static float BACK_HELP_DOWN=1755;
	//================HelpView   end===================

	//=============��ͣ����start==============
    public static float isPause_left=950;
    public static float isPause_right=1300;
    public static float isPause_top=700;
    public static float isPause_bottom=1600;
	
	
    public static float Pause_left=900;//��ͣ��ť
    public static float Pause_right=1400;
    public static float Pause_top=1650;
    public static float Pause_bottom=1780;

	public static float Selectpz_Left=960;//ѡ��ƿ�Ӱ�ť
	public static float Selectpz_Right=1210;
	public static float Selectpz_Top=755;
	public static float Selectpz_Bottom=930;
	
	public static float Selectqiu_Left=960;//ѡ������ť
	public static float Selectqiu_Right=1210;
	public static float Selectqiu_Top=1000;
	public static float Selectqiu_Bottom=1115;
	
	public static float Pauseshezhi_Left=960;//���ð�ť
	public static float Pauseshezhi_Right=1210;
	public static float Pauseshezhi_Top=1200;
	public static float Pauseshezhi_Bottom=1315;
	
	public static float PauseBackMain_Left=960;//���������水ť
	public static float PauseBackMain_Right=1210;
	public static float PauseBackMain_Top=1350;
	public static float PauseBackMain_Bottom=1550;
	
	public static float BackMainfou_Left=200;//ȡ���������水ť
	public static float BackMainfou_Right=400;
	public static float BackMainfou_Top=850;
	public static float BackMainfou_Bottom=1050;
	
	public static float BackMainqueren_Left=650;//ȷ�Ϸ��������水ť
	public static float BackMainqueren_Right=850;
	public static float BackMainqueren_Top=850;
	public static float BackMainqueren_Bottom=1050;
	
	//=============���ý���start==============	
	public static float SheZhiback_Left=100;//������Ϸ������ť
	public static float SheZhiback_Right=280;
	public static float SheZhiback_Top=1150;
	public static float SheZhiback_Bottom=1350;
	
	public static float SheZhiyinyue_Left=650;//��������
	public static float SheZhiyinyue_Right=950;
	public static float SheZhiyinyue_Top=600;
	public static float SheZhiyinyue_Bottom=800;
	
	
	public static float SheZhiyinxiao_Left=650;//������Ч
	public static float SheZhiyinxiao_Right=950;
	public static float SheZhiyinxiao_Top=810;
	public static float SheZhiyinxiao_Bottom=950;
	
	public static float SheZhishexiangji_Left=650;//���������
	public static float SheZhishexiangji_Right=950;
	public static float SheZhishexiangji_Top=950;
	public static float SheZhishexiangji_Bottom=1100;
	//=============ѡ��ƿ�ӽ���start==============	
	public static float Selectpzback_Left=160;//������Ϸ����
	public static float Selectpzback_Right=300;
	public static float Selectpzback_Top=1000;
	public static float Selectpzback_Bottom=1400;
	
	public static float Selectpz1_Left=160;//ѡ��ƿ��1
	public static float Selectpz1_Right=350;
	public static float Selectpz1_Top=400;
	public static float Selectpz1_Bottom=650;
	
	public static float Selectpz2_Left=400;//ѡ��ƿ��2
	public static float Selectpz2_Right=600;
	public static float Selectpz2_Top=400;
	public static float Selectpz2_Bottom=650;
	
	public static float Selectpz3_Left=650;//ѡ��ƿ��3
	public static float Selectpz3_Right=900;
	public static float Selectpz3_Top=400;
	public static float Selectpz3_Bottom=650;
	
	public static float Selectpz4_Left=160;//ѡ��ƿ��4
	public static float Selectpz4_Right=350;
	public static float Selectpz4_Top=700;
	public static float Selectpz4_Bottom=1100;
	
	public static float Selectpz5_Left=360;//ѡ��ƿ��5
	public static float Selectpz5_Right=600;
	public static float Selectpz5_Top=700;
	public static float Selectpz5_Bottom=1100;
	
	public static float Selectpz6_Left=650;//ѡ��ƿ��6
	public static float Selectpz6_Right=1000;
	public static float Selectpz6_Top=700;
	public static float Selectpz6_Bottom=1100;
	
	//=============ѡ���������start==============		
	public static float Selectqiuback_Left=160;//������Ϸ����
	public static float Selectqiuback_Right=300;
	public static float Selectqiuback_Top=1000;
	public static float Selectqiuback_Bottom=1400;
	
	public static float Selectqiu1_Left=160;//ѡ����1
	public static float Selectqiu1_Right=350;
	public static float Selectqiu1_Top=400;
	public static float Selectqiu1_Bottom=650;
	
	public static float Selectqiu2_Left=400;//ѡ����2
	public static float Selectqiu2_Right=600;
	public static float Selectqiu2_Top=400;
	public static float Selectqiu2_Bottom=650;
	
	public static float Selectqiu3_Left=650;//ѡ����3
	public static float Selectqiu3_Right=900;
	public static float Selectqiu3_Top=400;
	public static float Selectqiu3_Bottom=650;
	
	public static float Selectqiu4_Left=160;//ѡ����4
	public static float Selectqiu4_Right=350;
	public static float Selectqiu4_Top=700;
	public static float Selectqiu4_Bottom=1100;
	
	public static float Selectqiu5_Left=360;//ѡ����5
	public static float Selectqiu5_Right=600;
	public static float Selectqiu5_Top=700;
	public static float Selectqiu5_Bottom=1100;
	
	public static float Selectqiu6_Left=650;//ѡ����6
	public static float Selectqiu6_Right=1000;
	public static float Selectqiu6_Top=700;
	public static float Selectqiu6_Bottom=1100;
}
