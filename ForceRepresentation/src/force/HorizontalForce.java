package force;

public abstract class HorizontalForce extends Force{

	public HorizontalForce(double rootX, double rootY, double magnitude) {
		super(rootX, rootY, magnitude);
		// TODO Auto-generated constructor stub
	}
	
	public HorizontalForce(double rootX, double rootY) {
		super(rootX, rootY);
	}

	public void setMagnitude(double magnitude) {
		this.magnitude = magnitude;
	}
}
