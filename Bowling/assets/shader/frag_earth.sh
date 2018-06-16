//������ɫ��
precision mediump float;//�������㾭��
varying vec2 vTextureCoord;//���մӶ�����ɫ������������������Ϣ
varying vec4 vAmbient;//���մӶ�����ɫ�������Ļ���������ǿ��
varying vec4 vDiffuse;//���մӶ�����ɫ��������ɢ�������ǿ��
varying vec4 vSpecular;//���մӶ�����ɫ�������ľ��������ǿ��
uniform sampler2D sTextureDay;//������������
uniform sampler2D sTextureNight;//������������
void main()                         
{
  vec4 finalColorDay;   //�Ӱ��������в�������ɫֵ
  vec4 finalColorNight;   //��ҹ�������в�������ɫֵ
  
  finalColorDay= texture2D(sTextureDay, vTextureCoord);//�����������������ɫֵ
  finalColorDay = finalColorDay*vAmbient+finalColorDay*vSpecular+finalColorDay*vDiffuse;
  finalColorNight = texture2D(sTextureNight, vTextureCoord); //������ҹ���������ɫֵ
  finalColorNight = finalColorNight*vec4(0.5,0.5,0.5,1.0);//�������ƬԪ��ҹ����ɫֵ
  
  if(vDiffuse.x>0.21)
  {//��ɢ����������0.21ʱ
    gl_FragColor=finalColorDay;//���ð�������
  } 
  else if(vDiffuse.x<0.05)
  {//��ɢ������С��0.05ʱ��
     gl_FragColor=finalColorNight;//����ҹ������
  }
  else
  {
     float t=(vDiffuse.x-0.05)/0.16;//�����������Ӧռ������Ƚ׶εİٷֱ�
     gl_FragColor=t*finalColorDay+(1.0-t)*finalColorNight;//������Ƚ׶ε���ɫֵ
  }  
}