//地球着色器
precision mediump float;//给出浮点经度
varying vec2 vTextureCoord;//接收从顶点着色器过来的纹理坐标信息
varying vec4 vAmbient;//接收从顶点着色器过来的环境光最终强度
varying vec4 vDiffuse;//接收从顶点着色器过来的散射光最终强度
varying vec4 vSpecular;//接收从顶点着色器过来的镜面光最终强度
uniform sampler2D sTextureDay;//纹理内容数据
uniform sampler2D sTextureNight;//纹理内容数据
void main()                         
{
  vec4 finalColorDay;   //从白天纹理中采样出颜色值
  vec4 finalColorNight;   //从夜晚纹理中采样出颜色值
  
  finalColorDay= texture2D(sTextureDay, vTextureCoord);//采样出白天纹理的颜色值
  finalColorDay = finalColorDay*vAmbient+finalColorDay*vSpecular+finalColorDay*vDiffuse;
  finalColorNight = texture2D(sTextureNight, vTextureCoord); //采样出夜晚纹理的颜色值
  finalColorNight = finalColorNight*vec4(0.5,0.5,0.5,1.0);//计算出该片元的夜晚颜色值
  
  if(vDiffuse.x>0.21)
  {//当散射光分量大于0.21时
    gl_FragColor=finalColorDay;//采用白天纹理
  } 
  else if(vDiffuse.x<0.05)
  {//当散射光分量小于0.05时，
     gl_FragColor=finalColorNight;//采用夜晚纹理
  }
  else
  {
     float t=(vDiffuse.x-0.05)/0.16;//计算白天纹理应占纹理过度阶段的百分比
     gl_FragColor=t*finalColorDay+(1.0-t)*finalColorNight;//计算过度阶段的颜色值
  }  
}