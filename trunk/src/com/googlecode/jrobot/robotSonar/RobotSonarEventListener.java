package com.googlecode.jrobot.robotSonar;

import java.util.EventListener;

public interface RobotSonarEventListener extends EventListener {

	void perform(RobotSonarEventArg eventArg);
}
