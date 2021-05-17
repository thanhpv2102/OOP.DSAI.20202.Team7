package force;

public class ChangeableForce extends Force{

	public ChangeableForce(double rootX, double rootY, double magnitude) {
		super(rootX, rootY, magnitude);
		// TODO Auto-generated constructor stub
	}
	
	public ChangeableForce(double rootX, double rootY) {
		super(rootX, rootY);
	}

	public void setMagnitude(double magnitude) {
		this.magnitude = magnitude;
	}
}
