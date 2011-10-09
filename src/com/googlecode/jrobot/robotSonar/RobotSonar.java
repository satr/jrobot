package com.googlecode.jrobot.robotSonar;

import java.util.ArrayList;

public class RobotSonar {
	public static final int MaxAngle = 90;
	public static final int MinAngle = MaxAngle * (-1);

	private final ArrayList<RobotSonarEventListener> _frontObstacleListeners = new ArrayList<RobotSonarEventListener>();
	private final ArrayList<RobotSonarEventListener> _distanceMeasuredListeners = new ArrayList<RobotSonarEventListener>();
	private final ArrayList<RobotSonarEventListener> _changeDirectionListeners = new ArrayList<RobotSonarEventListener>();
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
		fireChangeDirectionEvent();
		return this;
	}

	public RobotSonar addFrontObstacleListener(RobotSonarEventListener listener) {
	    addListener(listener, _frontObstacleListeners);
		return this;
	}
	
	public RobotSonar removeFrontObstacleListener(RobotSonarEventListener listener) {
	    removeListener(listener, _frontObstacleListeners);
		return this;
	}

	public RobotSonar addDistanceMeasuredListener(RobotSonarEventListener listener) {
		addListener(listener, _distanceMeasuredListeners);
		return this;
	}

	public RobotSonar removeDistanceMeasuredListener(RobotSonarEventListener listener) {
		removeListener(listener, _distanceMeasuredListeners);
		return this;
	}

	public RobotSonar addChangeDirectionListener(RobotSonarEventListener listener) {
	    addListener(listener, _changeDirectionListeners);
		return this;
	}
	
	public RobotSonar removeChangeDirectionListener(RobotSonarEventListener listener) {
	    removeListener(listener, _changeDirectionListeners);
		return this;
	}

	private void addListener(RobotSonarEventListener listener, ArrayList<RobotSonarEventListener> eventListenerList) {
		eventListenerList.add(listener);
	}

	private void removeListener(RobotSonarEventListener listener, ArrayList<RobotSonarEventListener> eventListenerList) {
		eventListenerList.remove(listener);
	}

	private void fireFrontObstacleEvent() {
	    fireEvent(_frontObstacleListeners);
	}

	private void fireDistanceMeasuredEvent() {
	    fireEvent(_distanceMeasuredListeners);
	}

	private void fireChangeDirectionEvent() {
	    fireEvent(_changeDirectionListeners);
	}

	private void fireEvent(ArrayList<RobotSonarEventListener> eventListeners) {
		for (RobotSonarEventListener eventListener : eventListeners)
            eventListener.perform(_eventArg);
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
		_distanceMeasuredListeners.clear();
		_changeDirectionListeners.clear();
		_frontObstacleListeners.clear();
	}
}
