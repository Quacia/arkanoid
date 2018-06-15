package com.inc.arkanoid.v1;

import java.awt.Color;
import java.awt.Point;
import java.util.Vector;

public class Block {
	//블록 배치 및 레벨, 점수 정보.
	public static final int BLOCK_W = 50;
	public static final int BLOCK_H = 20;
	
	private static final int ROWS = 6;
	private static final int COLS = 10;
	
	public Vector<Point> pointList = new Vector<>();
	
	
	private Color c;
	
	public Block(int level) {
		switch(level) {
			case 1 : level1(); break;
		}
	}
	
	private void level1() {
		for(int row = 1 ; row < ROWS ; row++) {
			for(int col = 0 ; col < COLS ; col++) {
				Point p = new Point(col * BLOCK_W, row * BLOCK_H);
				synchronized (pointList) {
					pointList.add(p);
				}
				
			}
		}
	}

}
