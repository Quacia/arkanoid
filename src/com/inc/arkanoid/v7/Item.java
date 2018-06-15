package com.inc.arkanoid.v7;

import java.awt.Color;
import java.awt.Point;

public class Item {
	private Point p;
	private Color c;
	private int itemType;

	

	public Item(Point p) {
		this.p = p;
		itemType = ((int)(Math.random() * 4));
		//0~3까지의 난수 생성
		switch(itemType) {
			case 0 : c = Color.RED ; break;
			case 1 : c = Color.BLUE ; break;
			case 2 : c = Color.BLACK; break;
			case 3 : c = Color.GREEN; break;
		}
	}
		
	public Point getP() {
		return p;
	}

	public Color getC() {
		return c;
	}

	public void setC(Color c) {
		this.c = c;
	}

	public int getItemType() {
		return itemType;
	}


	public void moveDown() {
		p.y++;
	}
}
