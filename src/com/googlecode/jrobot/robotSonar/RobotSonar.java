package com.googlecode.jrobot.robotSonar;

import java.util.ArrayList;
import java.util.Timer;

import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;

public class RobotSonar {
    private static final int RotationSpeed = 120;
    private static final int TimerInterval = 20;
	public static final int MaxAngle = 90;
	public static final int MinAngle = MaxAngle * (-1);

	private final NXTRegulatedMotor _motor;

	private final ArrayList<RobotSonarEventListener> _eventListeners = new ArrayList<RobotSonarEventListener>();
	private final RobotSonarEventArg _eventArg = new RobotSonarEventArg(this);
	private Timer _timer;
	private UltrasonicSensor _sonic;
	private RobotSonarRotationDirection _rotationDirection = RobotSonarRotationDirection.None;

	private int _distance, _angle, _obstacleMinDistance;
	private int angle;
	private int _obstacleAngle;

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

	public void setDistance() {
		setDistance(getSonic().getDistance());
		setAngle(_motor.getTachoCount());
		fireDistanceMeasuredEvent();
		if(getDistance() <= getObstacleMinDistance()
				&& getAngle() < _obstacleAngle && getAngle() > (_obstacleAngle * (-1)))
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
		if(_sonic == null){
			_sonic = new UltrasonicSensor(SensorPort.S1);
			_sonic.continuous();
		}
		return _sonic;
	}

	public void setObstacleAngle(int angle) {
		_obstacleAngle = angle / 2;
	}
}
