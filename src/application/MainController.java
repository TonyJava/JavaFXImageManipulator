package application;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import utils.CanvasUtils;
import utils.ImageProcessor;
import utils.ImageViewUtils;
import utils.TensorflowUtils;

public class MainController implements Initializable, ControllerInterface {
	
	@FXML private Label filePath;
	@FXML private Label classRes;
	@FXML private ImageView iv;
	@FXML private Button roiBtn;
	@FXML private Canvas canvas;
	
	private ImageProcessor imageProcessor = new ImageProcessor();
	private Image image;
	GraphicsContext gc;
	private boolean drawingROI = false;
	private String currFilePath;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		gc = canvas.getGraphicsContext2D();
		
		iv.toFront();
//		canvas.toFront();
	}
	
	public void openFileChooser(ActionEvent ae) {
		FileChooser fc = new FileChooser();
		fc.getExtensionFilters().addAll(
				new ExtensionFilter("JPG images", "*.jpg"));
		
		File selectedFile = fc.showOpenDialog(null);
		
		if (selectedFile == null)
			showAlert("File is not chosen", 
					"File is not chosen,",
					"Please try again.");
		else {
			currFilePath = selectedFile.getAbsolutePath();
			filePath.setText("File: " + currFilePath);
			image = new Image("file:" + currFilePath);
			iv.setImage(image);
			iv.setPreserveRatio(true);
			imageProcessor.load(currFilePath);
			ImageViewUtils.enableImageViewScalingAndPanning(iv, image);
		}
			
	}
	
	private void showAlert(String title, String header, String content) { 
	    Alert alert = new Alert(Alert.AlertType.INFORMATION);
	    alert.setTitle(title);
	    alert.setHeaderText(header);
	    alert.setContentText(content);
	    alert.showAndWait();
	}
	
	public void imageDragOver(final DragEvent de) {
		Dragboard board = de.getDragboard();
	      if (board.hasFiles()) {
	        de.acceptTransferModes(TransferMode.ANY);
	      }
	}
	
	public void imageDrop(final DragEvent e) {
		final Dragboard db = e.getDragboard();
        boolean success = false;
        if (db.hasFiles()) {
            success = true;
            // Only get the first file from the list
            final File file = db.getFiles().get(0);
            
            if (!file.getAbsolutePath().endsWith(".jpg")) {
            		showAlert("Wrong Type", 
            				"Only .jpg file is accpeted.", 
            				"Plaese choose another file.");
            		e.setDropCompleted(false);
            		e.consume();
            		return;
            }
            
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    System.out.println(file.getAbsolutePath());
                    filePath.setText("File: " + file.getAbsolutePath());
                    image = new Image("file:" + file.getAbsolutePath());
                    iv.setImage(image);
                		imageProcessor.load(file.getAbsolutePath());
                		iv.setPreserveRatio(true);
                		ImageViewUtils.enableImageViewScalingAndPanning(iv, image);
                }
            });
        }
        e.setDropCompleted(success);
        e.consume();
	}
	
	public void zoomIn(ActionEvent ae) {
		if (image != null)
			ImageViewUtils.zoom(iv, image, 0.67);
	}
	
	public void zoomOut(ActionEvent ae) {
		if (image != null)
			ImageViewUtils.zoom(iv, image, 1.5);
	}
	
	public void arrowBtn(ActionEvent ae) {
		String btn = ((Button) ae.getSource()).getId();
		if (image != null)
			switch (btn) {
			case "up": ImageViewUtils.shift(iv, new Point2D(0.0, 10.0)); break;
			case "down": ImageViewUtils.shift(iv, new Point2D(0.0, -10.0)); break;
			case "left": ImageViewUtils.shift(iv, new Point2D(10.0, 0.0)); break;
			case "right": ImageViewUtils.shift(iv, new Point2D(-10.0, 0.0)); break;
			}
	}
	
	public void grayScale(ActionEvent ae) {
		iv.setImage(imageProcessor.grayScale());
	}
	
	public void blackWhite(ActionEvent ae) {
		iv.setImage(imageProcessor.binarization());
	}
	
	public void contrast(ActionEvent ae) {
		iv.setImage(imageProcessor.contrastEnhancement());
	}
	
	public void original(ActionEvent ae) {
		iv.setImage(image);
	}
	
	public void reset(ActionEvent ae) {
		if (image != null) {
			System.out.println("Reset!");
			iv.setViewport(new Rectangle2D(0,0,image.getWidth(),image.getHeight()));
		}
	}
	
	public void exit(ActionEvent ae) {
		System.exit(0);
	}
	
	public void about(ActionEvent ae) {
		showAlert("About",
				"Made by Ji Jiahao",
				"with insights learnt from kind people");
	}
	
	public void clickROI(ActionEvent ae) {
		if (drawingROI) {
			exitDrawingMode();
		} else {
			CanvasUtils.startDrawing(canvas, gc);
			drawingROI = true;
			roiBtn.setText("stop");
		}
	}
	
	private void exitDrawingMode() {
		if (drawingROI) {
			CanvasUtils.stopDrawing(canvas);
			drawingROI = false;
			roiBtn.setText("ROI");
		}
	}
	
	public void clearROI(ActionEvent ae) {
		exitDrawingMode();
		CanvasUtils.clear(canvas, gc);
		// TODO: clear the list
	}

	@Override
	public void classify(ActionEvent ae) {
		if (currFilePath != null) {
			classRes.setText(TensorflowUtils.classify(currFilePath));
		} 
	}

}
