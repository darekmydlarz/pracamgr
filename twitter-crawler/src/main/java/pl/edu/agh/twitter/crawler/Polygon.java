package pl.edu.agh.twitter.crawler;

public enum Polygon {
	England(-8.662663, 49.1626564, 1.768926, 60.86165),
	Europe(-9, 36, 30, 70);

	private double left;
	private double bottom;
	private double right;
	private double top;

	private Polygon(double left, double bottom, double right, double top) {
		this.bottom = bottom;
		this.left = left;
		this.top = top;
		this.right = right;
	}

	public double[][] getTwitter4jRepresentation() {
		return new double[][] { { left, bottom }, { right, top } };
	}
}
