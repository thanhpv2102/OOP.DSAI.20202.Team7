package objects;

import force.*;
import java.lang.Math;

public class Cube extends ActedObject {
	private double sideLength;
	
	public double getSideLength() {
		return sideLength;
	}
	
	
	public Cube(double mass, double sideLength, ChangeableForce actorForce, Surface surface) {
		super(mass, 0, sideLength/2, actorForce, surface);
		this.sideLength = sideLength;
		// TODO Auto-generated constructor stub
	}
	
	
	public void updateFrictionalForce() {
		double magnitude;
		if ( Math.abs(this.actorForce.getMagnitude()) <= Math.abs(this.normalForce.getMagnitude()) * this.surface.getStaticFrictionCoef() 
			&& this.getVelocity() == 0) {
			magnitude = Math.abs(this.actorForce.getMagnitude());
		} else {
			magnitude = Math.abs(this.normalForce.getMagnitude() * this.surface.getKineticFrictionCoef());
		}
		
		if (this.getVelocity() >= 0) {
			this.frictionalForce.setMagnitude(magnitude);
		} else {
			this.frictionalForce.setMagnitude(-magnitude);
		}
	}
	
	public void proceed(double deltaT) {
		this.validateSpeedThreshold();
		this.updateFrictionalForce();
		this.updateAcceleration();
		this.updatePosition(deltaT);
		this.updateVelocity(deltaT);
		this.updateForcesRoots();
	}

}
