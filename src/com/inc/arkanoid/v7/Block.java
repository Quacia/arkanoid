package com.inc.arkanoid.v7;

import java.awt.Color;
import java.awt.Point;
import java.util.Vector;

public class Block {
	//블록 배치 및 레벨, 점수 정보.
	public static final int BLOCK_W = 50;
	public static final int BLOCK_H = 20;
	
	private static final int ROWS = 6;
	private static final int COLS = 10;
	
	//public Vector<Point> pointList = new Vector<>();
	public static Vector<Block> blockList = new Vector<>();
	
	private Point p;
	private Color c;
	
	public Block(int level) {
		switch(level) {
			case 0 : level0(); break;
			case 1 : level1(); break;
			case 2 : level2(); break;
			case 3 : level3(); break;
		}
	}
	
	public Block(Point p, Color c) {
		this.p = p;
		this.c = c;
	}
	
	private void level0() {
		Color c = Color.black;
		Point p = new Point(100, 200);
		Block b = new Block(p, c);
		blockList.add(b);
	}
	
	private void level1() {
		int color = 0; //컬러값 바꿔주는 변수
		for(int row = 0 ; row < ROWS ; row++) {
			for(int col = 0 ; col < COLS ; col++) {
				if(color % 3 == 0) {
					c = Color.MAGENTA;
				}else if(color % 3 == 1){
					c = Color.pink;
				}else {
					c = Color.YELLOW;
				}
				Point p = new Point(col * BLOCK_W, row * BLOCK_H + 70);
				Block b = new Block(p, c);
				
				blockList.add(b);
				color++;
			}
		}
	}
	
	private void level2() {
		int color = 1;
		for(int row = 0 ; row < COLS ; row++) {
			for(int col = 0 ; col < row + 1; col++) {
				if((col+color) % 3 == 0) {
					c = Color.GREEN;
				}else if((col+color) % 3 == 1){
					c = Color.CYAN;
				}else {
					c = Color.ORANGE;
				}
				Point p = new Point(col * BLOCK_W, row * BLOCK_H + 50);
				Block b = new Block(p, c);
				
				blockList.add(b);
				
			}
			color++;
		}
	}
	
	private void level3() {
		int k = 4; //공백길이
		//공백이 4,3,2,1,0,1,2,3,4 로 변화함.
		int color = 0;
		for(int row = 0 ; row < 9; row++) {
			for(int col = 0 ; col < 9 ; col++) {
				if(color % 4 == 0) {
					c = Color.PINK;
				}else if(color % 4 == 1){
					c = Color.CYAN;
				}else if(color % 4 == 2){
					c = Color.orange;
				}else {
					c = Color.GREEN;
				}
				if(col >= k && col <= 8-k) {
					Point p = new Point(col * BLOCK_W + 25, row * BLOCK_H + 100);
					Block b = new Block(p, c);
					
					blockList.add(b);
					color++;
				}
				color++;
			}
			
			if(row < 4) {
				k--;
			}else {
				k++;
			}
		}
	}

	public Point getP() {
		return p;
	}

	public Color getC() {
		return c;
	}
	
	

}
