package com.test;

import java.util.Timer;

public class TimerTask {

	public static void main(String[] args) {
		Timer timer = new Timer();
		timer.schedule(new Task(), 0, 2*1000);
	}
}