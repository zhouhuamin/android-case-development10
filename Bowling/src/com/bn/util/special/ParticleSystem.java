package com.bn.util.special;

import java.util.*;

import com.bn.util.constant.Constant;
import com.bn.util.constant.SourceConstant;
import com.bn.util.matrix.MatrixState3D;
import com.bn.view.GameView;

import android.opengl.GLES20;
import static com.bn.util.constant.ParticleDataConstant.*;

public class ParticleSystem implements Comparable<ParticleSystem> 
{
	//���ڴ�����е�����
	public ArrayList<ParticleSingle> alFsp=new ArrayList<ParticleSingle>();
	//-���ڴ����Ҫɾ��������
	ArrayList<ParticleSingle> alFspForDel=new ArrayList<ParticleSingle>();
	//����ת�����е����ӣ�ÿ�ζ�Ҫ���
	public ArrayList<ParticleSingle> alFspForDraw=new ArrayList<ParticleSingle>();
	//���ڻ��Ƶ���������
	public ArrayList<ParticleSingle> alFspForDrawTemp=new ArrayList<ParticleSingle>();
	//��Դ��
	Object lock=new Object();
	//��ʼ��ɫ
	public float[] startColor;
	//��ֹ��ɫ
	public float[] endColor;
	//Դ�������
	public int srcBlend;
	//Ŀ��������
	public int dstBlend;
	//��Ϸ�ʽ
	public int blendFunc;
	//�������������
	public float maxLifeSpan;
	//���������ڲ���
	public float lifeSpanStep;
	//���Ӹ����߳�����ʱ����
	public int sleepSpan;
	//ÿ���緢����������
	public int groupCount;
	//���������
	public float sx;
	public float sy;
	//����λ��
	float positionX;
	float positionY;
	float positionZ;
	//�����仯��Χ
	public float xRange;
	public float yRange;
	//���ӷ�����ٶ�
	public float vx;
	public float vy;
	//��ת�Ƕ�
	float yAngle=0;
	//������
	ParticleForDraw fpfd;
	//������־λ
	boolean flag=true;
	
    public ParticleSystem(float positionx,float positiony,float positionz,ParticleForDraw fpfd)
    {
    	this.positionX=positionx;
    	this.positionY=positiony;
    	this.positionZ=positionz;
    	this.startColor=START_COLOR[CURR_INDEX];
    	this.endColor=END_COLOR[CURR_INDEX];
    	this.srcBlend=SRC_BLEND[CURR_INDEX];
    	this.dstBlend=DST_BLEND[CURR_INDEX];
    	this.blendFunc=BLEND_FUNC[CURR_INDEX];
    	this.maxLifeSpan=MAX_LIFE_SPAN[CURR_INDEX];
    	this.lifeSpanStep=LIFE_SPAN_STEP[CURR_INDEX];
    	this.groupCount=GROUP_COUNT[CURR_INDEX];
    	this.sleepSpan=THREAD_SLEEP[CURR_INDEX];
    	this.sx=0;
    	this.sy=0;
    	this.xRange=X_RANGE[CURR_INDEX];
    	this.yRange=Y_RANGE[CURR_INDEX];
    	this.vx=0;
    	this.vy=VY[CURR_INDEX];
    	this.fpfd=fpfd;
    	
    	new Thread()
    	{
    		public void run()
    		{
    			while(flag)
    			{
    				update();
    				try 
    				{
						Thread.sleep(sleepSpan);
					}catch (InterruptedException e) 
					{
						e.printStackTrace();
					}
    			}
    		}
    	}.start();
    }
    
    public void drawSelf()
    {
        //��Ϊÿ�ν��л������ӵĸ����Ѿ������ǲ��ϱ仯�ģ�������Ҫ���ϵظ���
    	alFspForDrawTemp.clear();
    	synchronized(lock)
    	{
    	   //������Ŀ����Ϊ�˱�֤��Ӻ������ͬʱ���У�Ҳ���Ǳ�֤��ÿ��add�����ж���
    	   for(int i=0;i<alFspForDraw.size();i++)
    	   {
    		   alFspForDrawTemp.add(alFspForDraw.get(i));
    	   }
    	}
    	MatrixState3D.pushMatrix();
    	
    	//�������
        GLES20.glEnable(GLES20.GL_BLEND);
        //���û�Ϸ�ʽ
         GLES20.glBlendEquation(blendFunc);
        //���û������
        GLES20.glBlendFunc(srcBlend,dstBlend);
    	
        if(SourceConstant.particleIndex==1)
        {
        	MatrixState3D.translate(positionX, positionY, positionZ);
    		MatrixState3D.rotate(-60, 1, 0, 0);
        }
    	for(ParticleSingle fsp:alFspForDrawTemp)
    	{
    		fsp.drawSelf(startColor,endColor,maxLifeSpan);
    	}
    	//�رջ��
        GLES20.glDisable(GLES20.GL_BLEND);
        MatrixState3D.popMatrix();
    }
    
    public void update()
    {
    	//�緢������
    	for(int i=0;i<groupCount;i++)
    	{
    		//�����ĸ��������������ӵ�λ��------**/
    		if(SourceConstant.particleIndex==1)
    		{
    			//����������ӵķ�λ�Ǽ�����
    			double elevation=Math.random()*Math.PI/12+Math.PI*2/12;//����
    			double direction=Math.random()*Math.PI*2;//��λ��
    			//�����������XYZ�᷽����ٶȷ���
    			float vy=(float)(2f*Math.sin(elevation));	
    			float vx=(float)(2f*Math.cos(elevation)*Math.cos(direction));	
    			float vz=(float)(2f*Math.cos(elevation)*Math.sin(direction));
    			
    			ParticleSingle fsp=new ParticleSingle(vx,vy,vz,fpfd);
    			alFsp.add(fsp);
    		}else if(SourceConstant.particleIndex==0)
    		{
    			if(GameView.ballA!=null&&GameView.ballA.length>0)
    			{
    				sx=GameView.ballA[0];
    				sy=GameView.ballA[2]+Constant.BALL_RADISU;
    			}
    			if(sx==0&&sy==0)
    			{
    				return;
    			}
    			//�����ĸ��������������ӵ�λ��------**/
        		float px=(float) (sx+xRange*(Math.random()*2-1.0f));
        		float py=(float)(sy+yRange*(Math.random()*2-1.0f));
        		float vx=(sx-px)/150;
                //x������ٶȺ�С,���ԾͲ����������Ļ�������
                ParticleSingle fsp=new ParticleSingle(px,py,vx,vy,fpfd);
                alFsp.add(fsp);
    		}
    	}
    	//��ջ���������б����б���Ҫ�洢��Ҫɾ��������
    	alFspForDel.clear();
    	for(ParticleSingle fsp:alFsp)
    	{
    		//��ÿ������ִ���˶�����
    		fsp.go(lifeSpanStep);
    		//��������Ѿ����ڵ�ʱ���Ѿ��㹻�ˣ��Ͱ�����ӵ���Ҫɾ���������б�
    		if(fsp.lifeSpan>this.maxLifeSpan)
    		{
    			alFspForDel.add(fsp);
    		}
    	}
    	//ɾ����������
    	for(ParticleSingle fsp:alFspForDel)
    	{
    		alFsp.remove(fsp);
    	}
    	//alFsp�б��д�������е����Ӷ����������б�Ϊ�����������������ӣ�ͬʱҲ����ɾ��ĳЩ���ڵ����Ӷ���
    	//���»����б� 
    	synchronized(lock)
    	{
    		alFspForDraw.clear();
    		for(int i=0;i<alFsp.size();i++)
    		{
    			alFspForDraw.add(alFsp.get(i));
    		}
    	}
    }

	@Override
	public int compareTo(ParticleSystem arg0) 
	{
		return 0;
	}
}
