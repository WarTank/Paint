package spengergasse.model;

public class Canvas {
	private Pixel[][] raster;
	
	public Canvas(int width,int height) {
		raster = new Pixel[width][height];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				raster[height][width] = new Pixel();
			}
		}
	}
	
}
