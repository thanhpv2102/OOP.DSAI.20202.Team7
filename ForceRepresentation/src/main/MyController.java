package main;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

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

	private int BACKGROUND_WIDTH = 1653;
	private static ParallelTransition parallelTransition;
	private static RotateTransition rotateTransition1;
	private static RotateTransition rotateTransition2;
	private static TranslateTransition bg_trans1;
	private static TranslateTransition bg_trans2;

	@FXML
	private Slider forceSlider;

	@FXML
	private ImageView bg1;

	@FXML
	private ImageView bg2;

	@FXML
	private Rectangle bg_cube;

	@FXML
	private Circle bg_cyclinder;

	@FXML
	private RadioButton radio_cube;

	@FXML
	private RadioButton radio_cyclinder;

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

		
		//initialize toggle group
		radio_cube.setToggleGroup(group);
		radio_cyclinder.setToggleGroup(group);

		group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			@Override
			public void changed(ObservableValue<? extends Toggle> arg0, Toggle arg1, Toggle arg2) {
				RadioButton button = (RadioButton) group.getSelectedToggle();
				if (button.getText().equals("Cube")) {
					input_radius.setVisible(false);
					input_sizeLength.setVisible(true);
					txt_radius.setVisible(false);
					txt_sizeLength.setVisible(true);
					bg_cyclinder.setVisible(false);
					bg_cube.setVisible(true);
					horizontal_line.setVisible(false);
					vertical_line.setVisible(false);
				} else if (button.getText().equals("Cyclinder")) {
					input_radius.setVisible(true);
					input_sizeLength.setVisible(false);
					txt_radius.setVisible(true);
					txt_sizeLength.setVisible(false);
					bg_cyclinder.setVisible(true);
					bg_cube.setVisible(false);
					horizontal_line.setVisible(true);
					vertical_line.setVisible(true);
				}
			}
		});

		//base animations
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

		//force slider
		forceSlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				setBackgroundTransitionRate(Math.abs(arg2.doubleValue()));
			}
		});
	}

	public static void setBackgroundTransitionRate(double speed) {	
		//slowly speed up animation from old speed to new speed
		float i = (float) parallelTransition.getRate();
		while (i <= speed) {
			parallelTransition.pause();
			parallelTransition.setRate(i / 30);
			parallelTransition.play();
			i += 0.1;
		}	
		System.out.println(parallelTransition.getRate());	
	}

}