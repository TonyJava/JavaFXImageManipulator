package utils;

import java.io.ByteArrayInputStream;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javafx.scene.image.Image;

public class ImageProcessor {
	private Mat inputImg;
	
	public void load(String filePath) {
		inputImg = Imgcodecs.imread(filePath);
	}
	
	public Image grayScale(){
        if (inputImg == null) return null;
        
		Mat output = new Mat();
        Imgproc.cvtColor(inputImg,output, Imgproc.COLOR_RGB2GRAY);
        return matToImage(output);
    }

    public Image contrastEnhancement() {
    		if (inputImg == null) return null;
    	
        Mat output = new Mat();
        inputImg.convertTo(output, CvType.CV_8UC1, 2.0, 50.0);
        return matToImage(output);
    }
    
    public Image binarization() {
    		if (inputImg == null) return null;
    		
        Mat output = new Mat();
        Imgproc.cvtColor(inputImg,output, Imgproc.COLOR_RGB2GRAY);
        Imgproc.threshold(output, output, 120.0, 250.0, Imgproc.THRESH_BINARY);
        return matToImage(output);
    }
    
    private Image matToImage(Mat input) {
    		MatOfByte buffer = new MatOfByte();
    		Imgcodecs.imencode(".bmp", input, buffer);
    		return new Image(new ByteArrayInputStream(buffer.toArray()));
    }
}
