uniform int isShadow;					//��Ӱ���Ʊ�־
uniform mat4 uMVPMatrix; 				//�ܱ任����
uniform mat4 uMMatrix; 					//�任����
uniform mat4 uMProjCameraMatrix; 		//ͶӰ���������Ͼ���
uniform vec3 uLightLocation;			//��Դλ��
uniform vec3 uCamera;					//�����λ��
uniform float shadowPosition;			//����ͶӰ��λ��
attribute vec3 aPosition;  				//����λ��
attribute vec3 aNormal;    				//���㷨����
attribute vec2 aTexCoor;    			//������������
varying vec4 ambient;
varying vec4 diffuse;
varying vec4 specular; 
varying vec2 vTextureCoord;
//��λ����ռ���ķ���
void pointLight(						//��λ����ռ���ķ���
  in vec3 normal,						//������
  inout vec4 ambient,					//����������ǿ��
  inout vec4 diffuse,					//ɢ�������ǿ��
  inout vec4 specular,					//���������ǿ��
  in vec3 lightLocation,				//��Դλ��
  in vec4 lightAmbient,					//������ǿ��
  in vec4 lightDiffuse,					//ɢ���ǿ��
  in vec4 lightSpecular					//�����ǿ��
){
  ambient=lightAmbient;									//ֱ�ӵó������������ǿ��  
  vec3 normalTarget=aPosition+normal;					//����任��ķ�����
  vec3 newNormal=(uMMatrix*vec4(normalTarget,1)).xyz-(uMMatrix*vec4(aPosition,1)).xyz;
  newNormal=normalize(newNormal); 						//�Է��������
  //����ӱ���㵽�����������
  vec3 eye= normalize(uCamera-(uMMatrix*vec4(aPosition,1)).xyz);  
  //����ӱ���㵽��Դλ�õ�����vp
  vec3 vp= normalize(lightLocation-(uMMatrix*vec4(aPosition,1)).xyz);  
  vp=normalize(vp);										//��ʽ��vp
  vec3 halfVector=normalize(vp+eye);					//����������ߵİ�����    
  float shininess=50.0;									//�ֲڶȣ�ԽСԽ�⻬
  float nDotViewPosition=max(0.0,dot(newNormal,vp)); 	//��������vp�ĵ����0�����ֵ
  diffuse=lightDiffuse*nDotViewPosition;				//����ɢ��������ǿ��
  float nDotViewHalfVector=dot(newNormal,halfVector);	//������������ĵ�� 
  float powerFactor=max(0.0,pow(nDotViewHalfVector,shininess)); 	//���淴���ǿ������
  specular=lightSpecular*powerFactor;    				//���㾵��������ǿ��
}

void main()     
{
   if(isShadow==1){										//���Ʊ�Ӱ��������Ӱ����λ��
      vec3 A=vec3(0.0,shadowPosition,0.0);
      vec3 n=vec3(0.0,1.0,0.0);							//ͶӰƽ�淨����
      vec3 S=uLightLocation; 							//��Դλ��     
      vec3 V=(uMMatrix*vec4(aPosition,1)).xyz;  		//����ƽ�ƺ���ת�任��ĵ������    
      vec3 VL=S+(V-S)*(dot(n,(A-S))/dot(n,(V-S)));		//��õ�ͶӰ������
      gl_Position = uMProjCameraMatrix*vec4(VL,1); 		//�����ܱ任�������˴λ��ƴ˶���λ��   
      pointLight(normalize(aNormal),ambient,diffuse,specular,uLightLocation,
      			vec4(0.3,0.3,0.3,0.3),vec4(0.7,0.7,0.7,0.3),vec4(0.3,0.3,0.3,0.3));
   }
   else
   {
	  gl_Position = uMVPMatrix * vec4(aPosition,1); 	//�����ܱ任�������˴λ��ƴ˶���λ��
	  pointLight(normalize(aNormal),ambient,diffuse,specular,uLightLocation,
	  		vec4(0.3,0.3,0.3,1.0),vec4(0.7,0.7,0.7,1.0),vec4(0.3,0.3,0.3,1.0));
   } 
   vTextureCoord = aTexCoor;							//�����յ��������괫�ݸ�ƬԪ��ɫ��
}                      