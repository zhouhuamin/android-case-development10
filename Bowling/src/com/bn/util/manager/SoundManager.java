package com.bn.util.manager;

import java.util.HashMap;

import com.bn.bowling.MainActivity;
import com.bn.bowling.R;
import com.bn.util.constant.Constant;
import android.annotation.SuppressLint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

@SuppressLint("UseSparseArrays")
public class SoundManager
{
	SoundPool sp ;
	HashMap<Integer	,Integer> hm ;
	MainActivity activity ;
	
	public MediaPlayer mp  ;
	public SoundManager(MainActivity activity)
	{
		this.activity = activity  ;
		initSound();
	}
	
	//���� ��ʼ��
	
	public void initSound()
	{
		sp = new SoundPool
		(4, 
		AudioManager.STREAM_MUSIC, 
		100
		);
		hm = new HashMap<Integer, Integer>();  
		hm.put(Constant.BALL_BATTLE_BEATER, sp.load(activity,R.raw.poolballhit, 1));//���ƿ����ײ
		hm.put(Constant.BALL_WALL_SOUND, sp.load(activity, R.raw.puckwallsound, 1));//���ǽ��ײ
		hm.put(Constant.BUTTON_PRESS, sp.load(activity, R.raw.bt_press, 1));//�����ť
		hm.put(Constant.APPLAUSE, sp.load(activity, R.raw.applause, 1));//����
		hm.put(Constant.BALL_ROLL, sp.load(activity, R.raw.ballroll, 1));//����
		hm.put(Constant.INIT_BOTTLE, sp.load(activity, R.raw.initbottle, 1));//��ʼ��ƿ��
	}
	
	public void playMusic(int sound,int loop)
	{
		@SuppressWarnings("static-access")
		AudioManager am = (AudioManager)activity.getSystemService(activity.AUDIO_SERVICE);
		float steamVolumCurrent = am.getStreamVolume(AudioManager.STREAM_MUSIC)  ;
		float steamVolumMax = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC)  ;
		float volum = steamVolumCurrent/steamVolumMax  ;
		sp.play(hm.get(sound), volum, volum, 1	, loop, 1)  ;//����
	}
	

	long preTimeStamp=0;
	public void playGameMusic(int sound,int loop)
	{
		long currTimeStamp=System.nanoTime();
		if(currTimeStamp-preTimeStamp<500000000L)
		{
			return;
		}
		preTimeStamp=currTimeStamp;
		@SuppressWarnings("static-access")
		AudioManager am = (AudioManager)activity.getSystemService(activity.AUDIO_SERVICE);
		float steamVolumCurrent = am.getStreamVolume(AudioManager.STREAM_MUSIC)  ;//��õ�ǰ����
		float steamVolumMax = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC)  ;//����������
		float volum = steamVolumCurrent/steamVolumMax  ;//�����������ŵ�����
		sp.play(
				hm.get(sound), //������Դid
				volum, //����������
				volum, //����������
				1	, //���ȼ�
				loop, //ѭ������ -1������Զѭ��
				1//�ط��ٶ�0.5f��2.0f֮��
				);//����
	}	
	
	public void stopGameMusic(int sound)
	{
		sp.pause(sound);
		sp.stop(sound);
		sp.setVolume(sound, 0, 0);
	}
}

