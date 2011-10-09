package com.googlecode.jrobot;


import java.util.Timer;

import com.googlecode.jrobot.robotSonar.RobotSonar;
import com.googlecode.jrobot.robotSonar.RobotSonarEventArg;
import com.googlecode.jrobot.robotSonar.RobotSonarEventListener;
import com.googlecode.jrobot.robotSonar.RobotSonarTimerHandler;

import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;

public class Robot implements RobotSonarEventListener {
	private static final int TimerInterval = 10;
    private static final int RotationSpeed = 70;
	
    private final NXTRegulatedMotor _motor = Motor.B;
	private final RobotSonar _robotSonar = new RobotSonar();
	private final Timer _timer = new Timer();
	private final RobotDisplay _display = new RobotDisplay();

	public Robot() {
		super();
		_robotSonar.addFrontObstacleListener(this)
				   .addDistanceMeasuredListener(_display);
	}

	public void Run() {
	    _timer.schedule(new RobotSonarTimerHandler(_motor, _robotSonar), 0, TimerInterval);
		_motor.setSpeed(RotationSpeed);
	    _motor.forward();
		while(!Button.ESCAPE.isPressed())
			Thread.yield();
	}

	@Override
	public void perform(RobotSonarEventArg eventArg) {
		// TODO Auto-generated method stub
		
	}

	public void dispose() {
		_timer.cancel();
		_motor.stop();
		_robotSonar.removeFrontObstacleListener(this)
				   .removeDistanceMeasuredListener(_display)
				   .dispose();
	}

}
