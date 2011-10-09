package com.googlecode.jrobot.sensors;

import java.util.TimerTask;
import lejos.nxt.Button;


public final class RobotSonarTimerHandler extends TimerTask {
	private final RobotSonar _robotSonar;

	public RobotSonarTimerHandler(RobotSonar radar) {
		super();
		_robotSonar = radar;
	}

	@Override
	public void run() {
		if(Button.ESCAPE.isPressed())
			System.exit(0);
		if(!_robotSonar.isRunning())
		{
			Thread.yield();
			return;
		}
		if(!_robotSonar.validateMinMaxAngle())
			_robotSonar.changeRotationDirection();
		_robotSonar.setDistance();
		Thread.yield();
	}
}