package com.inc.arkanoid.v1;

import java.awt.Color;
import java.awt.Point;

public class Pad {
	//Pad 사이즈, 위치 정보
	public static final int PAD_W = 70;
	public static final int PAD_H = 12;
	
	
	private Point padPoint;
	private Color c;
	
	public Point getPadPoint() {
		return padPoint;
	}
	public void setPadPoint(Point padPoint) {
		this.padPoint = padPoint;
	}
	public Color getC() {
		return c;
	}
	public void setC(Color c) {
		this.c = c;
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
