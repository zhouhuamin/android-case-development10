package com.bn.object;

import com.bn.util.matrix.MatrixState3D;
import com.bn.util.tool.AABB3;

/*
 * 可以被触控到的抽象类，
 * 物体继承了该类可以被触控到
 */
public abstract class TouchableObject {
	AABB3 preBox;//仿射变换之前的包围盒
    float[] m = new float[16];//仿射变换的矩阵    
	float size = 1.0f;;//放大的尺寸
	//获得中心点位置和长宽高的方法
    public AABB3 getCurrBox(){
    	return preBox.setToTransformedBox(m);//获取变换后的包围盒
    
    }
    //触控后的动作，根据需要要做相应改动
	public void changeOnTouch(boolean flag){
		if (flag) {
			size = 3f;
		} else {
			size = 1.5f;
		}	
	}
    //复制变换矩阵
    public void copyM(){
    	for(int i=0;i<16;i++){
    		m[i]=MatrixState3D.getMMatrix()[i];
    	}
    }	
}
