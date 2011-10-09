package com.googlecode.jrobot.analytics;

import com.googlecode.jrobot.Robot;
import com.googlecode.jrobot.sensors.RobotSonar;
import com.googlecode.jrobot.sensors.RobotSonarEventArg;
import com.googlecode.jrobot.sensors.RobotSonarEventListener;

public class RobotWayExplorer implements RobotSonarEventListener {

	private static final int MaxRotationDirectionChangedCount = 4;
	private final Robot _robot;
	private int _rotationDirectionCounter = 0;
	private boolean _previousDirectionIsFree;
	private int _freeWayStartAngle;
	private boolean _isActive;
	private PreferableWay _preferableWay = PreferableWay.Any;

	public RobotWayExplorer(Robot robot) {
		_robot = robot;
	}
	
	@Override
	public void frontObstacleDetected(RobotSonarEventArg eventArg) {
	}

	@Override
	public void distanceMeasured(RobotSonarEventArg eventArg) {
		if(!_isActive)
			return;
		RobotSonar robotSonar = eventArg.getRobotSonar();
		int currentAngle = robotSonar.getAngle();
		boolean currentDirectionIsFree = robotSonar.validateCurrentDirectionIsFree();
		if((_preferableWay == PreferableWay.Left && currentAngle > 0)
			|| (_preferableWay == PreferableWay.Right && currentAngle < 0))
			currentDirectionIsFree = false;
		if(!_previousDirectionIsFree){
			_freeWayStartAngle = currentAngle;
			_previousDirectionIsFree |= currentDirectionIsFree;
		}
		if(_freeWayStartAngle == currentAngle 
		   || Math.abs(_freeWayStartAngle - currentAngle) < robotSonar.getValidWayAngle())
			return;
		_previousDirectionIsFree = false;
		_isActive = false;
		_robot.notifyFreeWayFound((_freeWayStartAngle + currentAngle) / 2);
	}

	@Override
	public void rotationDirectionChanged(RobotSonarEventArg eventArg) {
		if(!_isActive)
			return;
		if (_rotationDirectionCounter < MaxRotationDirectionChangedCount){
			_rotationDirectionCounter++;
			return;
		}
		_isActive = false;
		_robot.notifyFrontFreeWayNotFound();
	}

	public boolean findFreeWay(PreferableWay preferableWay) {
		if(_isActive)
			return false;
		_preferableWay  = preferableWay;
		_rotationDirectionCounter = 0;
		_isActive = true;
		return true;
	}
	
	public enum PreferableWay{
		Left,
		Right,
		Any,
	}
}
