package main;

import force.ChangeableForce;
import objects.*;

import java.net.URL;
import java.util.ResourceBundle;

import exception.InvalidInputException;
import javafx.animation.AnimationTimer;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.scene.transform.Scale;
import javafx.util.Callback;

public class MyController implements Initializable {

	private ChangeableForce force = new ChangeableForce(0, 0, 0);
	private Surface surface = new Surface(0.25, 0.25);
	private final LongProperty lastUpdateAnimation = new SimpleLongProperty(0);
	AnimationTimer parallaxAnimation = new ParallaxAnimation();
	private ActedObject obj;

	private double mass;
	private double sideLength;

	private boolean cubeChosen = false;
	private boolean cylinderChosen = false; 
	private boolean choiceDraggable = true;
	
	@FXML
	private Slider staticSlider;

	@FXML
	private Slider kineticSlider;

	private int BACKGROUND_WIDTH = 1653;

	@FXML
	private Slider forceSlider;

	@FXML
	private TextField forceValue;

	@FXML
	private ImageView bg1;

	@FXML
	private ImageView bg2;

	@FXML
	private ImageView ls1;

	@FXML
	private ImageView ls2;

	@FXML
	private Rectangle bgCube;

	@FXML
	private Circle bgCylinder;

	@FXML
	private RadioButton radio_cube;

	@FXML
	private RadioButton radio_cylinder;

	ToggleGroup group = new ToggleGroup();

	@FXML
	private Line horizontalLine;

	@FXML
	private Line verticalLine;

	@FXML
	private TextField angularSpeedDisplay;
	@FXML
	private TextField massDisplay;

	@FXML
	private TextField angularAccelerationDisplay;

	@FXML
	private TextField speedDisplay;

	@FXML
	private  TextField accelerationDisplay;

	@FXML
	private CheckBox forcesBox;

	@FXML
	private  CheckBox sumForcesBox;

	@FXML
	private CheckBox valuesBox;

	@FXML
	private CheckBox massBox;

	@FXML
	private CheckBox speedBox;

	@FXML
	private  CheckBox accelerationBox;

	@FXML 
	private Rectangle choiceCube;

	@FXML 
	private Circle choiceCylinder;


	@FXML 
	private Label actorForceLabel;

	@FXML 
	private Label normalForceLabel;

	@FXML 
	private Label gravitationalForceLabel;

	@FXML 
	private Label frictionalForceLabel;

	@FXML 
	private Label sumForceLabel;

	@FXML
	private Button pauseButton;

	@FXML
	private ImageView whiteactor;

	@FXML
	private ImageView actorArrow;

	@FXML
	private ImageView frictionalArrow;

	@FXML
	private ImageView sumForceArrow;

	@FXML
	private Pane actorPane;

	@FXML
	private Pane frictionPane;

	@FXML
	private Pane sumForcePane;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		resetBtnPressed();
		//Alter static friction coef with staticSlider
		staticSlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				if (staticSlider.getValue() <= kineticSlider.getValue()) {
					staticSlider.adjustValue(kineticSlider.getValue());
				}
				surface.setStaticFrictionCoef((double) staticSlider.getValue());
			}
		});

		//Alter kinetic friction coef with kineticSlider
		kineticSlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				// TODO Auto-generated method stub
				if (kineticSlider.getValue() >= staticSlider.getValue()) {
					kineticSlider.adjustValue(staticSlider.getValue());
				}
				surface.setKineticFrictionCoef((double) kineticSlider.getValue());
			}
		});

		//the user can input applied force value in this text field
		forceValue.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					try {
						Double inputValue = Double.parseDouble(forceValue.getText());
						forceSlider.adjustValue(inputValue);
					} catch (NumberFormatException e) {
						forceValue.setText(String.valueOf(forceSlider.getValue()));
					}
				}
			}
		});

		//force slider
		forceSlider.valueProperty().addListener(new ChangeListener<Number>(){
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				force.setMagnitude(forceSlider.getValue());
				forceValue.setText(String.valueOf(forceSlider.getValue()));
				if (obj != null) {
					parallaxAnimation.start();	
				}
			}
		});
	}

	@FXML
	private void resetBtnPressed() {
		frictionalArrow.setY(actorArrow.getY());
		valuesBox.setSelected(false);
		forcesBox.setSelected(false);
		sumForcesBox.setSelected(false);
		massBox.setSelected(false);
		speedBox.setSelected(false);
		accelerationBox.setSelected(false);

		frictionPane.setVisible(false);
		actorPane.setVisible(false);
		sumForcePane.setVisible(false);

		parallaxAnimation.stop();
		obj = null;
		display(obj);

		bgCube.setVisible(false);
		bgCylinder.setVisible(false);

		choiceCube.setVisible(true);
		choiceCylinder.setVisible(true);
		cubeChosen = false;
		cylinderChosen = false;
		choiceDraggable = true;

		horizontalLine.setRotate(0);
		verticalLine.setRotate(0);
		horizontalLine.setVisible(false);
		verticalLine.setVisible(false);

		forceSlider.adjustValue(0.0);
		staticSlider.adjustValue(0.25);
		kineticSlider.adjustValue(0.25);

		bg1.setX(0);
		bg2.setX(0);
		ls1.setX(0);
		ls2.setX(0);

		pauseButton.setText("PAUSE");
	}

	public void pauseHandle(ActionEvent event) {
		if (pauseButton.getText().equals("PAUSE")) {
			pauseButton.setText("RESUME");
		} else if (pauseButton.getText().equals("RESUME")) {
			pauseButton.setText("PAUSE");

		}
	}

	private class ParallaxAnimation extends AnimationTimer {
		@Override
		public void handle(long now) {
			if (lastUpdateAnimation.get() > 0) {
				long elastedNanoSecond = now - lastUpdateAnimation.get();
				if (pauseButton.getText().equals("PAUSE")) {
					display(obj);
					updateTransition(obj, elastedNanoSecond);
				}
			}
			lastUpdateAnimation.set(now);
		}
	}

	public void cubeDragged(MouseEvent event) {
		if (choiceDraggable) {
			choiceCube.setTranslateX(event.getX() + choiceCube.getTranslateX() );
			choiceCube.setTranslateY(event.getY() + choiceCube.getTranslateY() );
		}
	}

	public void cubeReleased(MouseEvent event) {
		if ( choiceCube.getTranslateY() < ( 400 - choiceCube.getLayoutY()) )   {
			showInputDialog("cube");
		} 
		choiceCube.setTranslateX(0);
		choiceCube.setTranslateY(0);
		if (cubeChosen == true) {
			choiceCylinder.setVisible( true);
			choiceCube.setVisible(false);
			choiceDraggable = false;
		}
		if (cylinderChosen == true) {
			choiceCube.setVisible( true);
			choiceCylinder.setVisible( false);
			choiceDraggable = false;
		}
	}

	public void cylinderDragged(MouseEvent event) {
		if (choiceDraggable) {
			choiceCylinder.setTranslateX(event.getX() + choiceCylinder.getTranslateX() );
			choiceCylinder.setTranslateY(event.getY() + choiceCylinder.getTranslateY() );
		}
	}

	public void cylinderReleased(MouseEvent event) {
		if ( choiceCylinder.getTranslateY() < ( 400 - choiceCylinder.getLayoutY()) )   {
			showInputDialog("cylinder");
		} 
		choiceCylinder.setTranslateX(0);
		choiceCylinder.setTranslateY(0);
		if (cubeChosen == true) {
			choiceCylinder.setVisible( true);
			choiceCube.setVisible(false);
			choiceDraggable = false;
		}
		if (cylinderChosen == true) {
			choiceCube.setVisible( true);
			choiceCylinder.setVisible( false);
			choiceDraggable = false;
		}
	}

	public void scaling(double magnitude, ImageView arrow, Pane forcePane) {

		double widthRatio = magnitude / arrow.boundsInLocalProperty().get().getWidth();

		if (magnitude >= 0) {
			double shifted = ((arrow.boundsInLocalProperty().get().getWidth() * widthRatio) -arrow.boundsInLocalProperty().get().getWidth()) /2 ;
			forcePane.setTranslateX(shifted);
		} else {
			double center = - arrow.boundsInLocalProperty().get().getWidth() / 2;
			double shifted = center  + ( (arrow.boundsInLocalProperty().get().getWidth() * widthRatio) -arrow.boundsInLocalProperty().get().getWidth() ) /2 ; ;
			forcePane.setTranslateX(shifted);
		}
		arrow.setScaleX(widthRatio);
	}

	public void frictionalscaling(double magnitude, ImageView arrow, Pane forcePane) {

		double widthRatio = magnitude / arrow.boundsInLocalProperty().get().getWidth();

		if (magnitude > 0) {
			double shifted = - ((arrow.boundsInLocalProperty().get().getWidth() * widthRatio) -arrow.boundsInLocalProperty().get().getWidth()) /2 ;
			forcePane.setTranslateX(shifted);
		} else {
			double center = arrow.boundsInLocalProperty().get().getWidth() / 2;
			double shifted = center  - ( (arrow.boundsInLocalProperty().get().getWidth() * widthRatio) -arrow.boundsInLocalProperty().get().getWidth() ) /2 ; ;
			forcePane.setTranslateX(shifted);
		}
		arrow.setScaleX(widthRatio);
	}

	public void display(ActedObject object) {
		//Phần lực phải hiện thị bằng mũi tên, nên sẽ thay thế sau

		if (forcesBox.isSelected() == true) {	
			frictionPane.setVisible(true);
			actorPane.setVisible(true);
			actorArrow.setVisible(true);
			actorForceLabel.setVisible(false);
			frictionalForceLabel.setVisible(false);
			//normalArrow.setVisible(true);
			//gravitationalArrow.setVisible(true);
			frictionalArrow.setVisible(true);		

			actorForceLabel.setVisible(true);
			//normalForceLabel.setVisible(true);
			//gravitationalForceLabel.setVisible(true);
			frictionalForceLabel.setVisible(true);
			scaling(object.getActorForceMagnitude(), actorArrow, actorPane);
			frictionalscaling(object.getFrictionalForceMagnitude(), frictionalArrow, frictionPane);

			if (cubeChosen == true) {
				actorArrow.setTranslateY(- (bgCube.getHeight() - 200)/2);
				frictionalArrow.setTranslateY(- (bgCube.getHeight() - 200)/2 );
				frictionalArrow.setX(actorArrow.getX());

				actorPane.setTranslateY(- (bgCube.getHeight() - 200)/2);
				frictionPane.setTranslateY(- (bgCube.getHeight() - 200)/2  + 50);

				actorArrow.setScaleY(2*((Cube) object).getSideLength() / actorArrow.boundsInLocalProperty().get().getHeight());
				frictionalArrow.setScaleY(2*((Cube) object).getSideLength() / actorArrow.boundsInLocalProperty().get().getHeight());
				sumForceArrow.setScaleY(2*((Cube) object).getSideLength() / actorArrow.boundsInLocalProperty().get().getHeight());
			} else if (cylinderChosen == true) {
				actorArrow.setTranslateY(- (bgCylinder.getRadius() - 100));
				frictionalArrow.setTranslateY(- (bgCylinder.getRadius() - 100));
				frictionalArrow.setX(actorArrow.getX());

				actorPane.setTranslateY(- (bgCylinder.getRadius() - 100));
				frictionPane.setTranslateY(- (bgCylinder.getRadius() - 100) + 50);

				actorArrow.setScaleY(2*((Cylinder) object).getRadius() / actorArrow.boundsInLocalProperty().get().getHeight());
				frictionalArrow.setScaleY(2*((Cylinder) object).getRadius() / frictionalArrow.boundsInLocalProperty().get().getHeight());
				sumForceArrow.setScaleY(2*((Cylinder) object).getRadius() / sumForceArrow.boundsInLocalProperty().get().getHeight());
			}
			String roundedActor = String.format("%.2f", object.getActorForceMagnitude());
			actorForceLabel.setText(roundedActor + " N");


			//frictionalArrow.setTranslateX(frictionalArrow.getTranslateX());
			String roundedFriction = String.format("%.2f", object.getFrictionalForceMagnitude());
			frictionalForceLabel.setText(roundedFriction + " N");

		} else {

			actorArrow.setVisible(false);
			actorPane.setVisible(false);
			//normalArrow.setVisible(false);
			//gravitationalArrow.setVisible(false);
			frictionalArrow.setVisible(false);	
			frictionPane.setVisible(false);

		}

		if (sumForcesBox.isSelected() == true) {
			sumForceArrow.setVisible(true);
			sumForcePane.setVisible(true);

			scaling(object.getSumForce(), sumForceArrow,sumForcePane);
			if (cubeChosen == true) {
				sumForceArrow.setTranslateY(- (bgCube.getHeight() - 200)/2);
				sumForcePane.setTranslateY(- (bgCube.getHeight() - 200)/2);
			} else {
				sumForceArrow.setTranslateY(- (bgCylinder.getRadius() - 100) + 50);
				sumForcePane.setTranslateY(- (bgCylinder.getRadius() - 100) + 50);
			}

			String roundedSum = String.format("%.2f", object.getSumForce());
			sumForceLabel.setText(roundedSum + " N");

		} else {
			sumForceArrow.setVisible(false);
			sumForcePane.setVisible(false);

		}	
		if (valuesBox.isSelected() == true) {
			if (forcesBox.isSelected() == true) {
				actorForceLabel.setVisible(true);
				frictionalForceLabel.setVisible(true);
			}
			if (sumForcesBox.isSelected()) {
				sumForceLabel.setVisible(true);
			}
		} else {
			actorForceLabel.setVisible(false);
			frictionalForceLabel.setVisible(false);
			sumForceLabel.setVisible(false);
		}
		if (massBox.isSelected() == true) {
			massDisplay.setVisible(true);
			String rounded = String.format("%.2f", object.getMass());
			massDisplay.setText(rounded + " Kg");
		} else {
			massDisplay.setVisible(false);
		}
		if (speedBox.isSelected() == true) {
			speedDisplay.setVisible(true);
			String rounded = String.format("%.2f", object.getVelocity());
			speedDisplay.setText(rounded + " m/s");
			if (cylinderChosen == true) {
				angularSpeedDisplay.setVisible(true);
				String roundedAngular = String.format("%.2f", ((Cylinder) object).getAngularVelocity());
				angularSpeedDisplay.setText(roundedAngular + " rad/s");
			}	
		} else {
			speedDisplay.setVisible(false);
			angularSpeedDisplay.setVisible(false);
		}
		if (accelerationBox.isSelected() == true) {
			accelerationDisplay.setVisible(true);
			String rounded = String.format("%.2f", object.getAcceleration());
			accelerationDisplay.setText(rounded + " m/s\u00b2");
			if (cylinderChosen == true) {
				angularAccelerationDisplay.setVisible(true);
				String roundedAngular = String.format("%.2f", ((Cylinder) object).getAngularAcceleration());
				angularAccelerationDisplay.setText(roundedAngular + " rad/s\u00b2");
			}
		} else {
			accelerationDisplay.setVisible(false);
			angularAccelerationDisplay.setVisible(false);
		}
	}

	public void updateTransition(ActedObject obj, long elastedNanoSecond) {
		double elastedSecond = elastedNanoSecond  / 1_000_000_000.0;
		double old_x;
		double oldAngularPos = 0;
		if (obj instanceof Cylinder) {
			oldAngularPos = ((Cylinder) obj).getAngularPosition();
		}
		old_x = obj.getX();
		obj.proceed(elastedSecond);
		if (ls1.getX() - (obj.getX() - old_x)*40 < -BACKGROUND_WIDTH) {
			ls1.setX(0);
			ls2.setX(0);
		} else if (ls1.getX() - (obj.getX() - old_x)*40 > 0) {
			ls1.setX(-BACKGROUND_WIDTH);
			ls2.setX(-BACKGROUND_WIDTH);
		} else {
			ls1.setX(ls1.getX() - (obj.getX() - old_x)*40);
			ls2.setX(ls2.getX() - (obj.getX() - old_x)*40);
		}
		if (bg1.getX() - (obj.getX() - old_x)*10 < -BACKGROUND_WIDTH) {
			bg1.setX(0);
			bg2.setX(0);
		} else if (bg1.getX() - (obj.getX() - old_x)*10 > 0) {
			bg1.setX(-BACKGROUND_WIDTH);
			bg2.setX(-BACKGROUND_WIDTH);
		} else {
			bg1.setX(bg1.getX() - (obj.getX() - old_x)*10);
			bg2.setX(bg2.getX() - (obj.getX() - old_x)*10);
		}

		if (obj instanceof Cylinder) {
			horizontalLine.setRotate(horizontalLine.getRotate() + (((Cylinder)obj).getAngularPosition() - oldAngularPos)*180/3.14*9);
			verticalLine.setRotate(verticalLine.getRotate() + (((Cylinder)obj).getAngularPosition() - oldAngularPos)*180/3.14*9);
		}

		if (obj.validateSpeedThreshold()) {
			forceSlider.adjustValue(0.0);
		}
	}

	private void showInputDialog(String type) {
		Dialog<String> dialog = new Dialog<>();
		dialog.setTitle("Input object information");

		DialogPane pane = dialog.getDialogPane();
		pane.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		pane.getStyleClass().add("inputDialog");

		Label labelMass = new Label("Mass (kg): ");
		Label labelSidelength = new Label("Side length (m): ");
		Label labelRadius = new Label("Radius (m): ");

		labelMass.setTextFill(Paint.valueOf("#8be9fd"));
		labelSidelength.setTextFill(Paint.valueOf("#8be9fd"));
		labelRadius.setTextFill(Paint.valueOf("#8be9fd"));

		TextField txtMass = new TextField();
		TextField txtLength = new TextField();
		TextField txtRadius = new TextField();

		txtMass.setPromptText("50.0");
		txtLength.setPromptText("35");
		txtRadius.setPromptText("15");

		GridPane grid = new GridPane();
		grid.add(labelMass, 1, 1);
		grid.add(txtMass, 2, 1);

		if (type.equals("cube")) {
			grid.add(labelSidelength, 1, 2);
			grid.add(txtLength, 2, 2);
		} else if (type.equals("cylinder")) {
			grid.add(labelRadius, 1, 2);
			grid.add(txtRadius, 2, 2);
		}


		grid.setHgap(10); 
		grid.setVgap(10);
		grid.setPadding(new Insets(10, 10, 10, 10));

		pane.setContent(grid);

		ButtonType btnConfirm = new ButtonType("Confirm", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().add(btnConfirm);

		dialog.setResultConverter(new Callback<ButtonType, String>() {
			@Override
			public String call(ButtonType b) { 
				if (b == btnConfirm) {
					try {
						if (type.equals("cube")) {
							obj = new Cube(Double.parseDouble(txtMass.getText()), Double.parseDouble(txtLength.getText()), force, surface);
							setBackgroundCube(Double.parseDouble(txtLength.getText()));
							cubeChosen = true;
						} else if (type.equals("cylinder")) {
							obj = new Cylinder(Double.parseDouble(txtMass.getText()), Double.parseDouble(txtRadius.getText()), force, surface);
							setBackgroundCylinder(Double.parseDouble(txtRadius.getText()));
							cylinderChosen = true;
						}
					} catch (NumberFormatException | InvalidInputException e) {
						if (e instanceof NumberFormatException) {
							Dialog<String> dialog = new Dialog<>();
							dialog.setTitle("Exception");

							DialogPane pane = dialog.getDialogPane();
							pane.getStylesheets().add(getClass().getResource("../main/application.css").toExternalForm());
							pane.getStyleClass().add("inputDialog");

							Label error = new Label("Input error: Not a number");
							error.setTextFill(Paint.valueOf("#8be9fd"));

							pane.setContent(error);

							ButtonType btnConfirm = new ButtonType("Ok", ButtonData.OK_DONE);
							dialog.getDialogPane().getButtonTypes().add(btnConfirm);

							dialog.showAndWait();
						}	
					}
				}
				return null;
			}
		});

		dialog.showAndWait();
	}

	public void setBackgroundCube(double sideLength) {
		cylinderChosen = false;
		cubeChosen = true;
		bgCylinder.setVisible(false);

		//this side length or radius * 5 is just to make it looks better on screen
		bgCube.setY(bgCube.getY() + (bgCube.getHeight() - sideLength*5));
		bgCube.setLayoutX(bgCube.getLayoutX() + (bgCube.getWidth() - sideLength*5)/2);
		bgCube.setHeight(sideLength*5);
		bgCube.setWidth(sideLength*5);

		bgCube.setVisible(true);
		horizontalLine.setVisible(false);
		verticalLine.setVisible(false);	
		choiceCube.setVisible(false);
		forceSlider.adjustValue(0.0);

	}

	public void setBackgroundCylinder(double radius) {
		cylinderChosen = true;
		cubeChosen = false;	
		bgCube.setVisible(false);

		//change lines too :D
		bgCylinder.setCenterY(bgCylinder.getCenterY() + (bgCylinder.getRadius() - radius*5));
		horizontalLine.setStartX(bgCylinder.getCenterX() - radius*5);
		horizontalLine.setEndX(bgCylinder.getCenterX() + radius*5);
		horizontalLine.setStartY(bgCylinder.getCenterY());
		horizontalLine.setEndY(bgCylinder.getCenterY());
		verticalLine.setStartX(bgCylinder.getCenterX());
		verticalLine.setEndX(bgCylinder.getCenterX());
		verticalLine.setStartY(bgCylinder.getCenterY() - radius*5);
		verticalLine.setEndY(bgCylinder.getCenterY() + radius*5);
		bgCylinder.setRadius(radius*5);

		bgCylinder.setVisible(true);
		horizontalLine.setVisible(true);
		verticalLine.setVisible(true);
		forceSlider.adjustValue(0.0);
	}
}