package spengergasse.model;

import java.awt.Color;

public class Pixel {
	private int r,g,b;
	public Pixel() {
		r=255;
		g=255;
		b=255;
	}
	public Pixel(int r, int g, int b){
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	public int getB() {
		return b;
	}
	public int getG() {
		return g;
	}
	public int getR() {
		return r;
	}
	public Color getColor(){
		return new Color(r,g,b);
	}
}
