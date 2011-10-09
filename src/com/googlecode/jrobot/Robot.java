package com.googlecode.jrobot;


import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import com.googlecode.jrobot.analytics.*;
import com.googlecode.jrobot.interaction.*;
import com.googlecode.jrobot.sensors.*;

public class Robot implements RobotSonarEventListener {
    private static final int MotorSpeed = 100;
	private final NXTRegulatedMotor _motorL = Motor.A;
    private final NXTRegulatedMotor _motorR = Motor.C;
	private final RobotSonar _sonar = new RobotSonar(Motor.B);
	private final RobotDisplay _display = new RobotDisplay();
	private final RobotVoice _voice = new RobotVoice();
	private final RobotWayExplorer _wayExplorer;

	public Robot() {
		super();
		_wayExplorer = new RobotWayExplorer(this);
		_sonar.addListener(this)
				   .addListener(_display)
				   .addListener(_wayExplorer);
		_sonar.setObstacleMinDistance(15);
		_sonar.setValidWayAngle(40);
		_sonar.run();
//		_voice.mute();//to avoid noise
	}

	public void run() {
		_motorL.setSpeed(MotorSpeed);
		_motorR.setSpeed(MotorSpeed);
		_motorL.backward();
		_motorR.backward();
	}

	public void dispose() {
		stop();
		_sonar.stop();
		_sonar.dispose();
	}

	private void stop() {
		_motorL.stop();
		_motorR.stop();
	}

	@Override
	public void frontObstacleDetected(RobotSonarEventArg eventArg) {
		stop();
		_voice.Say(RobotVoice.RobotVoiceItem.FrontObstacleDetected);
		if(_wayExplorer.findFreeWay(RobotWayExplorer.PreferableWay.Any))
			_display.showRobotStatus("ExploreFreeWay...");
	}

	@Override
	public void distanceMeasured(RobotSonarEventArg eventArg) {
	}

	@Override
	public void rotationDirectionChanged(RobotSonarEventArg eventArg) {
	}

	public void notifyFrontFreeWayNotFound() {
		//turn back and find other way...
		_voice.Say(RobotVoice.RobotVoiceItem.FreeWayNotFound);
		_display.showRobotStatus("FreeWayNotFound");
	}

	public void notifyFreeWayFound(int directionAngle) {
		_voice.Say(directionAngle > 0, RobotVoice.RobotVoiceItem.FreeWayFoundOnRight, RobotVoice.RobotVoiceItem.FreeWayFoundOnLeft);
		_display.showRobotStatus("FreeWayFound,angle:" + String.valueOf(directionAngle));
	}

}
