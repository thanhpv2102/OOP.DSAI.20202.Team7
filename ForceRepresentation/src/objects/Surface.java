package objects;

public class Surface {
	
	private double staticFrictionCoef;
	private double kineticFrictionCoef;
	
	
	public double getStaticFrictionCoef() {
		return staticFrictionCoef;
	}
	public void setStaticFrictionCoef(double staticFrictionCoef) {
		this.staticFrictionCoef = staticFrictionCoef;
	}
	public double getKineticFrictionCoef() {
		return kineticFrictionCoef;
	}
	public void setKineticFrictionCoef(double kineticFrictionCoef) {
		this.kineticFrictionCoef = kineticFrictionCoef;
	}
	
	
	public Surface(double staticFrictionCoef, double kineticFrictionCoef) {
		super();
		this.staticFrictionCoef = staticFrictionCoef;
		this.kineticFrictionCoef = kineticFrictionCoef;
	}
	
	

	
}
