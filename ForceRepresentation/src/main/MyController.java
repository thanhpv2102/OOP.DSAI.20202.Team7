package main;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class MyController implements Initializable {

	private int BACKGROUND_WIDTH = 1653;
	private static ParallelTransition parallelTransition;
	private static TranslateTransition translateTransition;
	private static TranslateTransition translateTransition2;
	
	@FXML
	private Slider forceSlider;

	@FXML
	private ImageView bg1;

	@FXML
	private ImageView bg2;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		translateTransition = new TranslateTransition(Duration.millis(10000), bg1);
		translateTransition.setFromX(0);
		translateTransition.setToX(-1 * BACKGROUND_WIDTH);
		translateTransition.setInterpolator(Interpolator.LINEAR);
		
		translateTransition2 = new TranslateTransition(Duration.millis(10000), bg2);
		translateTransition2.setFromX(0);
		translateTransition2.setToX(-1 * BACKGROUND_WIDTH);
		translateTransition2.setInterpolator(Interpolator.LINEAR);

		parallelTransition = new ParallelTransition(translateTransition, translateTransition2);
		parallelTransition.setCycleCount(Animation.INDEFINITE);
		
		forceSlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				setBackgroundTransitionRate((int) (500000 / Math.abs(arg2.floatValue())));
			}
		});
	}
	
	public static void setBackgroundTransitionRate(int speed) {
		translateTransition.setDuration(Duration.millis(speed));
		translateTransition2.setDuration(Duration.millis(speed));
		parallelTransition.playFromStart();
	}

}