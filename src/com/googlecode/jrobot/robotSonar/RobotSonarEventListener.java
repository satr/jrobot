package com.googlecode.jrobot.robotSonar;

import java.util.EventListener;

public interface RobotSonarEventListener extends EventListener {
	void frontObstacleDetected(RobotSonarEventArg eventArg);
	void distanceMeasured(RobotSonarEventArg eventArg);
	void rotationDirectionChanged(RobotSonarEventArg eventArg);
}
