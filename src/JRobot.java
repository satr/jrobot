/* The robot controlled by Java application
 * Copyright (c) 2011
 * */
import lejos.nxt.Button;

import com.googlecode.jrobot.Robot;
import com.googlecode.jrobot.interaction.RobotDisplay;


public class JRobot {
	public static void main(String[] args) throws InterruptedException{
		Robot robot = new Robot();
		robot.run();
		while(!Button.ESCAPE.isPressed())
			Thread.yield();
		robot.dispose();
	}
}