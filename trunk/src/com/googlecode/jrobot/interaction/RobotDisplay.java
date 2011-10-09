package com.googlecode.jrobot.interaction;
import javax.microedition.lcdui.Graphics;
import lejos.nxt.LCD;

import com.googlecode.jrobot.sensors.RobotSonar;
import com.googlecode.jrobot.sensors.RobotSonarEventArg;
import com.googlecode.jrobot.sensors.RobotSonarEventListener;


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
		showDistanceStatus(String.valueOf(distance));
		LCD.asyncRefresh();
	}

	@Override
	public void rotationDirectionChanged(RobotSonarEventArg eventArg) {
		_g.clear();
	}

	public void showDistanceStatus(String message) {
//		writeMessage(message, 4);
	}

	public void showRobotStatus(String message) {
//		writeMessage(message, 5);
	}

	private void writeMessage(String message, int lineNumber) {
		LCD.asyncRefreshWait();
		LCD.drawString(message, 0, lineNumber);
		LCD.asyncRefresh();
	}

	public void showTestCounter(int counter) {
		writeMessage("   ", 6);
		writeMessage(String.valueOf(counter), 6);
	}
}
