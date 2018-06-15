package com.inc.arkanoid.v5;

import java.awt.Point;

import javax.swing.JOptionPane;


public class Ball{
	public static final int BALL_SIZE = 10;
	private Point p;
	
	
	private boolean start = false; //쓰레드 시작 여부
	
	public Point getP() {
		return p;
	}
	public void setP(Point p) {
		this.p = p;
	}
	
	public void moveR() {
		p.x++;
	}
	
	public void moveL() {
		p.x--;
	}
	
	public void moveUp() {
		p.y--;
	}
	
	public void moveDown() {
		p.y++;
	}
	
	public boolean isStart() {
		return start;
	}
	public void setStart(boolean start) {
		this.start = start;
	}
	

}
