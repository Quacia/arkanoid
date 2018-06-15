package com.inc.arkanoid.v2;

import java.awt.Color;
import java.awt.Point;

public class Pad {
	//Pad 사이즈, 위치 정보
	public static final int PAD_W = 60;
	public static final int PAD_H = 10;
	
	
	private Point padPoint;

	
	public Point getPadPoint() {
		return padPoint;
	}
	public void setPadPoint(Point padPoint) {
		this.padPoint = padPoint;
	}
	
	public void moveL() {
		padPoint.x -= 15;
		setPadPoint(padPoint);
	}
	
	public void moveR() {
		padPoint.x += 15;
		setPadPoint(padPoint);
	}
	
	

}
