package com.inc.arkanoid.v7;

import java.awt.Point;

public class Bullet {
	private Point p;
	
	public Point getP() {
		return p;
	}
	public void setP(Point p) {
		this.p = p;
	}
	
	public void moveUp() {
		p.y--;
	}

}
