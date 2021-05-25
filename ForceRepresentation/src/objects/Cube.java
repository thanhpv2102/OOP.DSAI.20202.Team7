package objects;

import force.*;
import java.lang.Math;

import exception.InvalidInputException;

public class Cube extends ActedObject {
	private double sideLength;
	private static final double MAX_SIDE_LENGTH = 60.0;

	public double getSideLength() {
		return sideLength;
	}


	public Cube(double mass, double sideLength, ChangeableForce actorForce, Surface surface) throws InvalidInputException {
		super(mass, 0, sideLength/2, actorForce, surface);
		if (sideLength <= 0) {
			throw new InvalidInputException("Side length of cube must be positive");
		} else if (sideLength > MAX_SIDE_LENGTH) {
			throw new InvalidInputException("Maximum side length is 60m");
		}
		this.sideLength = sideLength;
		// TODO Auto-generated constructor stub
	}


	public void updateFrictionalForce() {
		double magnitude;
		if ( Math.abs(this.actorForce.getMagnitude()) <= Math.abs(this.normalForce.getMagnitude()) * this.surface.getStaticFrictionCoef()
			&& Math.abs(this.getVelocity()) <= 0.01) {
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
