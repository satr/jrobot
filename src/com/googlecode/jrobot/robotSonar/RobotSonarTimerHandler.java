package com.googlecode.jrobot.robotSonar;
import java.util.TimerTask;

import lejos.nxt.Button;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;


public final class RobotSonarTimerHandler extends TimerTask {
	private final UltrasonicSensor _sonic;
	private final NXTRegulatedMotor _motor;
	private final RobotSonar _robotSonar;

	public RobotSonarTimerHandler(NXTRegulatedMotor motor, RobotSonar radar) {
		super();
		_motor = motor;
		_robotSonar = radar;
		_sonic = new UltrasonicSensor(SensorPort.S1);
		_sonic.continuous();
	}

	@Override
	public void run() {
		if(Button.ESCAPE.isPressed())
			System.exit(0);
		if(!_motor.isMoving())
			return;
		int angle = _motor.getTachoCount();
		int rotationSpeed = _motor.getRotationSpeed();
		if(rotationSpeed > 0 && angle > RobotSonar.MaxAngle)
		{	
			_motor.backward();
			_robotSonar.changeDirection(-1);
		}
		else if (rotationSpeed < 0 && angle < RobotSonar.MinAngle)
		{
			_motor.forward();
			_robotSonar.changeDirection(1);
		}
		_robotSonar.setDistance(_sonic.getDistance(), angle);
	}
}