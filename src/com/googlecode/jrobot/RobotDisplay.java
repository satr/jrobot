package com.googlecode.jrobot;
import javax.microedition.lcdui.Graphics;

import com.googlecode.jrobot.robotSonar.RobotSonar;
import com.googlecode.jrobot.robotSonar.RobotSonarEventArg;
import com.googlecode.jrobot.robotSonar.RobotSonarEventListener;

import lejos.nxt.LCD;


public class RobotDisplay implements RobotSonarEventListener {

	private final Graphics _g = new Graphics();

	public RobotDisplay() {
		super();
		LCD.setAutoRefresh(false);
		LCD.clear();
		_g.clear();
	}

	private void setColorOn() {
		_g.setColor(0,0,0);
	}

	private void setColorOff() {
		_g.setColor(255,255,255);
	}

	public void dispose() {
		_g.clear();
	}

	@Override
	public void frontObstacleDetected(RobotSonarEventArg eventArg) {
	}

	@Override
	public void distanceMeasured(RobotSonarEventArg eventArg) {
		LCD.asyncRefreshWait();
		RobotSonar robotSonar = eventArg.getRobotSonar();
		int distance = robotSonar.getDistance();
		int angle = robotSonar.getAngle();
		int x = (angle + RobotSonar.MaxAngle)/2; 
		int y = (distance > LCD.SCREEN_HEIGHT)? LCD.SCREEN_HEIGHT: distance;
		setColorOff();
		_g.drawLine(x,0, x, y);
		setColorOn();
		_g.drawLine(x,y, x, LCD.SCREEN_HEIGHT);
//		LCD.drawString("  ", 10, 5);
//		LCD.drawString(String.valueOf(distance), 10, 5);
		LCD.asyncRefresh();
	}

	@Override
	public void rotationDirectionChanged(RobotSonarEventArg eventArg) {
		_g.clear();
	}
}
