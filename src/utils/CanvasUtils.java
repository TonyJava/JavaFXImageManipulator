package utils;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class CanvasUtils {
	public static void startDrawing(Canvas canvas, GraphicsContext gc) {
		gc.setStroke(Color.RED);
		
		ObjectProperty<Point2D> mouseDownCoor = new SimpleObjectProperty<>();
		
		canvas.setOnMousePressed(e -> {
			mouseDownCoor.set(new Point2D(e.getX(), e.getY())); 
		});
		
		canvas.setOnMouseDragged(e -> {
			gc.strokeRect(
					Math.min(e.getX(), mouseDownCoor.get().getX()), 
					Math.min(e.getY(), mouseDownCoor.get().getY()), 
					Math.abs(e.getX() - mouseDownCoor.get().getX()),
					Math.abs(e.getY() - mouseDownCoor.get().getY()));
		});
		
	}
	
	public static void stopDrawing(Canvas canvas) {
		canvas.setOnMousePressed(null);
		canvas.setOnMouseDragged(null);
	}
	
	public static void clear(Canvas canvas, GraphicsContext gc) {
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
	}
}
