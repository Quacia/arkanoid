package com.inc.arkanoid.v6;

import java.awt.Color;
import java.awt.Point;

public class Pad {
	//Pad 사이즈, 위치 정보
	public static int PAD_W = 60;
	public static final int PAD_H = 10;
	
	
	private Point padPoint;

	
	public Point getPadPoint() {
		return padPoint;
	}
	public void setPadPoint(Point padPoint) {
		this.padPoint = padPoint;
	}
	
	public void moveL() {
		padPoint.x -= 10;
	}
	
	public void moveR() {
		padPoint.x += 10;
	}
	
	

}
