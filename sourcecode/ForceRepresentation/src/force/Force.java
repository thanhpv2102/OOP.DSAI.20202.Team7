package force;
public class Force {
	//negative magnitude means opposite direction with the base direction of the force
	protected double magnitude;
	private double rootX;
	private double rootY;
	
	public double getMagnitude() {
		return magnitude;
	}
	public double getRootX() {
		return rootX;
	}
	public double getRootY() {
		return rootY;
	}

	
	public void setRootX(double rootX) {
		this.rootX = rootX;
	}
	public void setRootY(double rootY) {
		this.rootY = rootY;
	}
	
	
	public Force(double rootX, double rootY, double magnitude) {
		super();
		this.magnitude = magnitude;
		this.rootX = rootX;
		this.rootY = rootY;
	}
	
	public Force(double rootX, double rootY) {
		super();
		this.rootX = rootX;
		this.rootY = rootY;
	}
	
}
