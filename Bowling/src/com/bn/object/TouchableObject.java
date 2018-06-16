package com.bn.object;

import com.bn.util.matrix.MatrixState3D;
import com.bn.util.tool.AABB3;

/*
 * ���Ա����ص��ĳ����࣬
 * ����̳��˸�����Ա����ص�
 */
public abstract class TouchableObject {
	AABB3 preBox;//����任֮ǰ�İ�Χ��
    float[] m = new float[16];//����任�ľ���    
	float size = 1.0f;;//�Ŵ�ĳߴ�
	//������ĵ�λ�úͳ���ߵķ���
    public AABB3 getCurrBox(){
    	return preBox.setToTransformedBox(m);//��ȡ�任��İ�Χ��
    
    }
    //���غ�Ķ�����������ҪҪ����Ӧ�Ķ�
	public void changeOnTouch(boolean flag){
		if (flag) {
			size = 3f;
		} else {
			size = 1.5f;
		}	
	}
    //���Ʊ任����
    public void copyM(){
    	for(int i=0;i<16;i++){
    		m[i]=MatrixState3D.getMMatrix()[i];
    	}
    }	
}
