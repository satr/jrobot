package com.googlecode.jrobot.interaction;

import lejos.nxt.Sound;

public class RobotVoice {
	public enum RobotVoiceItem{
		FrontObstacleDetected,
		FreeWayNotFound,
		FreeWayFoundOnLeft,
		FreeWayFoundOnRight,
	}

	private boolean _isMute;
	
	public void Say(boolean condition, RobotVoiceItem voiceItemTrue, RobotVoiceItem voiceItemFalse){
		Say(condition? voiceItemTrue: voiceItemFalse);
	}
	
	public void Say(RobotVoiceItem voiceItem){
		if(_isMute)
			return;
		if(voiceItem == RobotVoiceItem.FrontObstacleDetected)
			Sound.playTone(800, 50);
		else if(voiceItem == RobotVoiceItem.FreeWayFoundOnLeft)
			Sound.playTone(1000, 50);
		else if(voiceItem == RobotVoiceItem.FreeWayFoundOnRight)
			Sound.playTone(1500, 50);
		else if(voiceItem == RobotVoiceItem.FreeWayNotFound)
			Sound.playTone(500, 50);
	}

	public void mute() {
		_isMute = true;
	}
	
	public void unmute() {
		_isMute = false;
	}
}
