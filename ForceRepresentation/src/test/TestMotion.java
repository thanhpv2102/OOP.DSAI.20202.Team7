package test;

import java.util.Scanner;

import exception.InvalidInputException;
import force.ChangeableForce;
import objects.ActedObject;
import objects.Cube;
import objects.Cylinder;
import objects.Surface;

public class TestMotion {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		
		System.out.println("-------SURFACE-------");
		System.out.println("Enter static friction coefficient:");
		double staticFrictionCoef = scanner.nextDouble();
		System.out.println("Enter kinetic friction coefficient:");
		double kineticFrictionCoef = scanner.nextDouble();
		
		double x = 0;
		double y = 0;
		
		Surface surface = new Surface(staticFrictionCoef, kineticFrictionCoef);
		ChangeableForce force = new ChangeableForce(x, y);
		
		System.out.println("--------FORCE--------");
		System.out.println("Enter force magnitude:");
		force.setMagnitude(scanner.nextDouble());
		
		System.out.println("--------OBJECT--------");
		System.out.println("Choose type of object:");
		System.out.println("1. Cube");
		System.out.println("2. Cyclinder");
		System.out.println("Enter:");
		
		int typeObj = scanner.nextInt();
		try {
			if (typeObj == 1) {
				System.out.println("Enter cube side length:");
				double sizeLength = scanner.nextDouble();
				System.out.println("Enter object mass:");
				double mass = scanner.nextDouble();
				Cube cube = new Cube(mass, sizeLength, force, surface);
				start(cube, force, surface);
			} else if (typeObj == 2) {
				System.out.println("Enter cyclinder radius:");
				double radius = scanner.nextDouble();
				System.out.println("Enter object mass:");
				double mass = scanner.nextDouble();
				Cylinder cyclinder = new Cylinder(mass, radius, force, surface);
				start(cyclinder, force, surface);
			}
		} catch (InvalidInputException e) {
			
		}
	}
	
	public static void start(ActedObject obj, ChangeableForce force, Surface surface) {
		if (obj instanceof Cube) {
			Cube cube = (Cube) obj;
			double t = 0;
			while (t < 10) {
				if (t > 5) {
					force.setMagnitude(1000.0);
				}
				cube.proceed(0.001);
				System.out.println("Time (s): " + t);
				System.out.println("Position (x, y): " + "(" + cube.getX() + ", " + cube.getY() + ")\n");
				System.out.println("Total force magnitude: " + cube.getSumForce());
				System.out.println("--------------------------------------");
				t += 0.001;
			}
		} else if (obj instanceof Cylinder) {
			Cylinder cyclinder = (Cylinder) obj;
			double t = 0;
			while (t < 10) {
				if (t > 5) {
					force.setMagnitude(1000.0);
				}
				cyclinder.proceed(0.001);
				System.out.println("Time (s): " + t);
				System.out.println("Position (x, y): " + "(" + cyclinder.getX() + ", " + cyclinder.getY() + ")\n");
				System.out.println("Total force magnitude: " + cyclinder.getSumForce());
				System.out.println("--------------------------------------");
				t += 0.001;
			}
		}
	}

}
