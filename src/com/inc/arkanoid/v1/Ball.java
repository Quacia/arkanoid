package com.inc.arkanoid.v1;

import java.awt.Point;

import javax.swing.JOptionPane;

public class Ball{
	public static final int BALL_SIZE = 15;
	private Point p;
	
	private boolean xCheck = true; //xCheck가 true이면 오른쪽으로.
	private boolean yCheck = true; //yCheck가 true이면 위로.
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
	
	
	public boolean isxCheck() {
		return xCheck;
	}
	public void setxCheck(boolean xCheck) {
		this.xCheck = xCheck;
	}
	
	public boolean isyCheck() {
		return yCheck;
	}
	
	public void setyCheck(boolean yCheck) {
		this.yCheck = yCheck;
	}
	
	public boolean isStart() {
		return start;
	}
	public void setStart(boolean start) {
		this.start = start;
	}
	

}
