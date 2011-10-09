package com.googlecode.jrobot.sensors;

import java.util.ArrayList;
import java.util.Timer;

import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;

public class RobotSonar {
    private static final int RotationSpeed = 120;
    private static final int TimerInterval = 10;
	public static final int MaxAngle = 100;
	public static final int MinAngle = MaxAngle * (-1);

	private final NXTRegulatedMotor _motor;

	private final ArrayList<RobotSonarEventListener> _eventListeners = new ArrayList<RobotSonarEventListener>();
	private final RobotSonarEventArg _eventArg = new RobotSonarEventArg(this);
	private Timer _timer;
	private UltrasonicSensor _sonicSensor;
	private RobotSonarRotationDirection _rotationDirection = RobotSonarRotationDirection.None;

	private int _distance, _angle, _obstacleMinDistance;
	private int _validWayAngle;
	private int _validWayPositiveHalfAngle;
	private int _validWayNegativeHalfAngle;

	public RobotSonar(NXTRegulatedMotor motor) {
		super();
		_motor = motor;
	}

	public int getDistance() {
		return _distance;
	}

	public int getAngle() {
		return _angle;
	}

	public RobotSonar addListener(RobotSonarEventListener listener) {
		if(!_eventListeners.contains(listener))
			_eventListeners.add(listener);
		return this;
	}

	public RobotSonar removeListener(RobotSonarEventListener listener) {
		if(_eventListeners.contains(listener))
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

	public void setDistance() {
		setDistance(getSonic().getDistance());
		setAngle(_motor.getTachoCount());
		fireDistanceMeasuredEvent();
		if(getDistance() <= getObstacleMinDistance()
				&& getAngle() < _validWayPositiveHalfAngle && getAngle() > _validWayNegativeHalfAngle)
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

	public void dispose() {
		_motor.stop();
		stopTimer();
		_eventListeners.clear();
	}

	public void run() {
		runTimer();
        _motor.setSpeed(RotationSpeed);
        if (getRotationDirection() == RobotSonarRotationDirection.Negative) {
			_motor.backward();
			return;
		} 
		_motor.forward();
		setRotationDirection(RobotSonarRotationDirection.Positive);
	}

	public void stop() {
		_motor.stop();
	}

	private void runTimer() {
		if(_timer != null)
			return;
		_timer = new Timer();
	    _timer.schedule(new RobotSonarTimerHandler(this), TimerInterval, TimerInterval);
	}

	private void stopTimer() {
		if(_timer == null)
			return;
		_timer.cancel();
	}

	public boolean validateMinMaxAngle() {
		int angle = _motor.getTachoCount();
		RobotSonarRotationDirection rotationDirection = getRotationDirection();
		return !(rotationDirection == RobotSonarRotationDirection.Positive && angle > RobotSonar.MaxAngle)
			&& !(rotationDirection == RobotSonarRotationDirection.Negative && angle < RobotSonar.MinAngle);
	}

	public void changeRotationDirection() {
		if(getRotationDirection() == RobotSonarRotationDirection.None)
			return;
		if(getRotationDirection() == RobotSonarRotationDirection.Positive){
			_motor.backward();
			setRotationDirection(RobotSonarRotationDirection.Negative);
		}
		else if (getRotationDirection() == RobotSonarRotationDirection.Negative){
			_motor.forward();
			setRotationDirection(RobotSonarRotationDirection.Positive);
		}
	}
	
	public void setRotationDirection(RobotSonarRotationDirection rotationDirection) {
		if(_rotationDirection == rotationDirection)
			return;
		_rotationDirection = rotationDirection;
		fireRotationDirectionChangedEvent();
	}

	private RobotSonarRotationDirection getRotationDirection() {
		return _rotationDirection;
	}

	public boolean isRunning() {
		return getRotationDirection() != RobotSonarRotationDirection.None;
	}

	public UltrasonicSensor getSonic() {
		if(_sonicSensor == null){
			_sonicSensor = new UltrasonicSensor(SensorPort.S1);
			_sonicSensor.continuous();
		}
		return _sonicSensor;
	}

	public void setValidWayAngle(int angle) {
		_validWayAngle = angle;
		_validWayPositiveHalfAngle = angle / 2;
		_validWayNegativeHalfAngle = _validWayPositiveHalfAngle * (-1);
	}

	public int getValidWayAngle() {
		return _validWayAngle;
	}

	public boolean validateCurrentDirectionIsFree() {
		return getDistance() > getObstacleMinDistance();
	}
}
