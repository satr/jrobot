package com.googlecode.jrobot.robotSonar;

import java.util.ArrayList;

public class RobotSonar {
	public static final int MaxAngle = 90;
	public static final int MinAngle = MaxAngle * (-1);

	private final ArrayList<RobotSonarEventListener> _eventListeners = new ArrayList<RobotSonarEventListener>();
	private final RobotSonarEventArg _eventArg;

	private int _distance, _angle, _obstacleMinDistance, _direction;

	public RobotSonar() {
		super();
        _eventArg = new RobotSonarEventArg(this);
	}

	public int getDistance() {
		return _distance;
	}

	public int getAngle() {
		return _angle;
	}

	public RobotSonar changeDirection(int direction) {
		setDirection(direction);
		fireRotationDirectionChangedEvent();
		return this;
	}

	public RobotSonar addListener(RobotSonarEventListener listener) {
		_eventListeners.add(listener);
		return this;
	}

	public RobotSonar removeListener(RobotSonarEventListener listener) {
		_eventListeners.remove(listener);
		return this;
	}

	private void fireFrontObstacleEvent() {
		for (RobotSonarEventListener eventListener : _eventListeners)
            eventListener.frontObstacleDetected(_eventArg);
	}

	private void fireDistanceMeasuredEvent() {
		for (RobotSonarEventListener eventListener : _eventListeners)
            eventListener.distanceMeasured(_eventArg);
	}

	private void fireRotationDirectionChangedEvent() {
		for (RobotSonarEventListener eventListener : _eventListeners)
            eventListener.rotationDirectionChanged(_eventArg);
	}

	public void setDistance(int distance, int angle) {
		setDistance(distance);
		setAngle(angle);
		fireDistanceMeasuredEvent();
		if(getDistance() >= getObstacleMinDistance())
			fireFrontObstacleEvent();
	}

	private void setDistance(int distance) {
		_distance = distance;
	}

	private void setAngle(int angle) {
		_angle = angle;
	}

	public void setObstacleMinDistance(int obstacleMinDistance) {
		_obstacleMinDistance = obstacleMinDistance;
	}

	public int getObstacleMinDistance() {
		return _obstacleMinDistance;
	}

	public void setDirection(int direction) {
		_direction = direction;
	}

	public int getDirection() {
		return _direction;
	}

	public void dispose() {
		_eventListeners.clear();
	}
}
