package com.test;

import java.util.TimerTask;

public class Task extends TimerTask {

	@Override
	public void run() {
		System.out.println("执行了一次");
	}
}
