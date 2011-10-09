package com.googlecode.jrobot.sensors;


public class RobotSonarEventArg {

	private final RobotSonar _robotSonar;

	public RobotSonarEventArg(RobotSonar robotSonar) {
		_robotSonar = robotSonar;
	}

	public RobotSonar getRobotSonar() {
		return _robotSonar;
	}
	
}
