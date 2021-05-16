package main;

import force.ActorForce;
import objects.*;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Scanner;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class MyController implements Initializable {

	private ActorForce force = new ActorForce(0, 0, 0);
	private Surface surface = new Surface(0.25, 0.25);
	private Cube cube;
	private Cylinder cylinder;

	@FXML
    private Slider staticSlider;
   
    @FXML
    private Slider kineticSlider;

	private int BACKGROUND_WIDTH = 1653;
	private static ParallelTransition parallelTransition;
	private static RotateTransition rotateTransition1;
	private static RotateTransition rotateTransition2;
	private static TranslateTransition bg_trans1;
	private static TranslateTransition bg_trans2;
	private static ParallelTransition parallelTransitionBack;
	private static RotateTransition rotateTransition1Back;
	private static RotateTransition rotateTransition2Back;
	private static TranslateTransition bg_trans1Back;
	private static TranslateTransition bg_trans2Back;

	@FXML
	private Slider forceSlider;

	@FXML
	private ImageView bg1;

	@FXML
	private ImageView bg2;

	@FXML
	private Rectangle bg_cube;

	@FXML
	private Circle bg_cylinder;

	@FXML
	private RadioButton radio_cube;

	@FXML
	private RadioButton radio_cylinder;

	ToggleGroup group = new ToggleGroup();

	@FXML
	private TextField input_mass;

	@FXML
	private TextField input_radius;

	@FXML
	private TextField input_sizeLength;

	@FXML
	private Text txt_radius;

	@FXML
	private Text txt_sizeLength;

	@FXML
	private Line horizontal_line;

	@FXML
	private Line vertical_line;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		cube = new Cube(Double.parseDouble(input_mass.getText()), Double.parseDouble(input_sizeLength.getText()), force, surface);
		cylinder = new Cylinder(Double.parseDouble(input_mass.getText()), Double.parseDouble(input_radius.getText()), force, surface);
	
		//Alter static friction coef with staticSlider
	   staticSlider.valueProperty().addListener(new ChangeListener<Number>() {

		@Override
		public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
			if (staticSlider.getValue() >= kineticSlider.getValue()) {
				staticSlider.adjustValue(kineticSlider.getValue());
			}
			surface.setStaticFrictionCoef((double) staticSlider.getValue());
//			System.out.println("static:" + surface.getStaticFrictionCoef());
		}
	   });
	   
	   //Alter kinetic friction coef with kineticSlider
	   kineticSlider.valueProperty().addListener(new ChangeListener<Number>() {

		@Override
		public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
			// TODO Auto-generated method stub
			if (kineticSlider.getValue() <= staticSlider.getValue()) {
				kineticSlider.adjustValue(staticSlider.getValue());
			}
			surface.setKineticFrictionCoef((double) kineticSlider.getValue());
//			System.out.println("kinetic:" + surface.getKineticFrictionCoef());
		}
		   
	   });
	   
		//initialize toggle group
		radio_cube.setToggleGroup(group);
		radio_cylinder.setToggleGroup(group);

		group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			@Override
			public void changed(ObservableValue<? extends Toggle> arg0, Toggle arg1, Toggle arg2) {
				RadioButton button = (RadioButton) group.getSelectedToggle();
				if (button.getText().equals("Cube")) {
					input_radius.setVisible(false);
					input_sizeLength.setVisible(true);
					txt_radius.setVisible(false);
					txt_sizeLength.setVisible(true);
					bg_cylinder.setVisible(false);
					bg_cube.setVisible(true);
					horizontal_line.setVisible(false);
					vertical_line.setVisible(false);
				} else if (button.getText().equals("Cyclinder")) {
					input_radius.setVisible(true);
					input_sizeLength.setVisible(false);
					txt_radius.setVisible(true);
					txt_sizeLength.setVisible(false);
					bg_cylinder.setVisible(true);
					bg_cube.setVisible(false);
					horizontal_line.setVisible(true);
					vertical_line.setVisible(true);
				}
			}
		});

		//base animations FORWARD
		bg_trans1 = new TranslateTransition(Duration.millis(10000), bg1);
		bg_trans1.setFromX(0);
		bg_trans1.setToX(-1 * BACKGROUND_WIDTH);
		bg_trans1.setInterpolator(Interpolator.LINEAR);

		bg_trans2 = new TranslateTransition(Duration.millis(10000), bg2);
		bg_trans2.setFromX(0);
		bg_trans2.setToX(-1 * BACKGROUND_WIDTH);
		bg_trans2.setInterpolator(Interpolator.LINEAR);

		rotateTransition1 = new RotateTransition(Duration.millis(10000), horizontal_line); 
		rotateTransition1.setByAngle(720);
		rotateTransition2 = new RotateTransition(Duration.millis(10000), vertical_line); 
		rotateTransition2.setByAngle(720);
		
		rotateTransition1.setInterpolator(Interpolator.LINEAR);
		rotateTransition2.setInterpolator(Interpolator.LINEAR);

		parallelTransition = new ParallelTransition(bg_trans1, bg_trans2, rotateTransition1, rotateTransition2);
		parallelTransition.setCycleCount(Animation.INDEFINITE);
		
		//base animations BACKWARD
		bg_trans1Back = new TranslateTransition(Duration.millis(10000), bg1);
		bg_trans1Back.setFromX(-1 * BACKGROUND_WIDTH);
		bg_trans1Back.setToX(0);
		bg_trans1Back.setInterpolator(Interpolator.LINEAR);

		bg_trans2Back = new TranslateTransition(Duration.millis(10000), bg2);
		bg_trans2Back.setFromX(-1 * BACKGROUND_WIDTH);
		bg_trans2Back.setToX(0);
		bg_trans2Back.setInterpolator(Interpolator.LINEAR);

		rotateTransition1Back = new RotateTransition(Duration.millis(10000), horizontal_line); 
		rotateTransition1Back.setByAngle(-720);
		rotateTransition2Back = new RotateTransition(Duration.millis(10000), vertical_line); 
		rotateTransition2Back.setByAngle(-720);
		
		rotateTransition1Back.setInterpolator(Interpolator.LINEAR);
		rotateTransition2Back.setInterpolator(Interpolator.LINEAR);

		parallelTransitionBack = new ParallelTransition(bg_trans1Back, bg_trans2Back, rotateTransition1Back, rotateTransition2Back);
		parallelTransitionBack.setCycleCount(Animation.INDEFINITE);

		//force slider
		forceSlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				force.setMagnitude(forceSlider.getValue());
				System.out.println("force:" + force.getMagnitude());
				if (force.getMagnitude() < 0) {
					if (radio_cube.isSelected()) {
						while ((int) cube.getSumForce() != 0 && (int) force.getMagnitude() != 0) {
							cube.proceed(1);
							//setBackgroundTransitionRateBack(cube.getVelocity());
							if ((int) cube.getVelocity() == 0) {
								continue;	
							} else {
								System.out.println("velo:" + cube.getVelocity());	
							}
						}
					} else {
						while ((int) cylinder.getSumForce() != 0 && (int) force.getMagnitude() != 0) {
							cylinder.proceed(1);
							//setBackgroundTransitionRateBack(cylinder.getVelocity());
							if ((int) cylinder.getVelocity() == 0) {
								continue;	
							} else {
								System.out.println("velo:" + cylinder.getVelocity());	
							}
						}
					}
				} else {
					if (radio_cube.isSelected()) {
						while ((int) cube.getSumForce() != 0 && (int) force.getMagnitude() != 0) {
							cube.proceed(1);
							//setBackgroundTransitionRate(cube.getVelocity());
							if ((int) cube.getVelocity() == 0) {
								continue;	
							} else {
								System.out.println("velo:" + cube.getVelocity());	
							}
						}
					} else {
						while ((int) cylinder.getSumForce() != 0 && (int) force.getMagnitude() != 0) {
							cylinder.proceed(1);
							//setBackgroundTransitionRate(cylinder.getVelocity());
							if ((int) cylinder.getVelocity() == 0) {
								continue;	
							} else {
								System.out.println("velo:" + cylinder.getVelocity());	
							}
						}
					}
				}
			}
		});
	}

	public static void setBackgroundTransitionRate(double speed) {	
		//slowly speed up animation from old speed to new speed
		parallelTransitionBack.pause();
		parallelTransition.pause();
		parallelTransition.setRate(speed);
		parallelTransition.play();
	}
	
	public static void setBackgroundTransitionRateBack(double speed) {	
		//slowly speed up animation from old speed to new speed
		parallelTransition.pause();
		parallelTransitionBack.pause();
		parallelTransitionBack.setRate(speed);
		parallelTransitionBack.play();
	}

}