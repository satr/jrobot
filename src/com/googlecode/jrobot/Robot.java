package com.googlecode.jrobot;


import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;

import com.googlecode.jrobot.robotSonar.RobotSonar;
import com.googlecode.jrobot.robotSonar.RobotSonarEventArg;
import com.googlecode.jrobot.robotSonar.RobotSonarEventListener;

public class Robot implements RobotSonarEventListener {
    private static final int MotorSpeed = 120;
	
    private final NXTRegulatedMotor _motorL = Motor.A;
    private final NXTRegulatedMotor _motorR = Motor.C;
	private final RobotSonar _robotSonar = new RobotSonar(Motor.B);
	private final RobotDisplay _display = new RobotDisplay();

	public Robot() {
		super();
		_robotSonar.addListener(this)
				   .addListener(_display);
		_robotSonar.setObstacleMinDistance(15);
		_robotSonar.setObstacleAngle(20);
		_robotSonar.run();
	}

	public void run() {
		_motorL.setSpeed(MotorSpeed);
		_motorR.setSpeed(MotorSpeed);
		_motorL.backward();
		_motorR.backward();
	}

	public void dispose() {
		stop();
		_robotSonar.stop();
		_robotSonar.dispose();
	}

	private void stop() {
		_motorL.stop();
		_motorR.stop();
	}

	@Override
	public void frontObstacleDetected(RobotSonarEventArg eventArg) {
		stop();
	}

	@Override
	public void distanceMeasured(RobotSonarEventArg eventArg) {
	}

	@Override
	public void rotationDirectionChanged(RobotSonarEventArg eventArg) {
	}

}
