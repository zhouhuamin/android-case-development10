package com.bn.util.tool;

//���ڴ洢�����������
public class Vector3f3D
{
	float x;
	float y;
	float z;
	public Vector3f3D(){}
	public Vector3f3D(float x,float y,float z)
	{
		this.x=x;
		this.y=y;
		this.z=z;
	}
	public Vector3f3D(float[] v)
	{
		this.x=v[0];
		this.y=v[1];
		this.z=v[2];
	}
	//�ӷ�
	public Vector3f3D add(Vector3f3D v){
		return new Vector3f3D(this.x+v.x,this.y+v.y,this.z+v.z);
	}
	//����
	public Vector3f3D minus(Vector3f3D v){
		return new Vector3f3D(this.x-v.x,this.y-v.y,this.z-v.z);
	}

	//�볣�����
	public Vector3f3D multiK(float k){
		return new Vector3f3D(this.x*k,this.y*k,this.z*k);
	}
	//���
	public void normalize(){
		float mod=module();
		x /= mod;
		y /= mod;
		z /= mod;
	}
	//ģ
	public float module(){
		return (float) Math.sqrt(x*x+y*y+z*z);
	}
}
