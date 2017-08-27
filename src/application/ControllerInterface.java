package application;

import javafx.event.ActionEvent;
import javafx.scene.input.DragEvent;

public interface ControllerInterface {
	// menu
	public void openFileChooser(ActionEvent ae);
	public void exit(ActionEvent ae);
	public void about(ActionEvent ae);
	
	// drag and drop
	public void imageDragOver(final DragEvent de);
	public void imageDrop(final DragEvent e);
	
	// buttons
	public void zoomIn(ActionEvent ae);
	public void zoomOut(ActionEvent ae);
	public void arrowBtn(ActionEvent ae);
	public void grayScale(ActionEvent ae);
	public void blackWhite(ActionEvent ae);
	public void contrast(ActionEvent ae);
	public void original(ActionEvent ae);
	public void reset(ActionEvent ae);
	public void clickROI(ActionEvent ae);
	public void clearROI(ActionEvent ae);
	public void classify(ActionEvent ae);
}
