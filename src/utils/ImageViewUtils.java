package utils;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageViewUtils {
	
	// adapted from https://gist.github.com/james-d/ce5ec1fd44ce6c64e81a,
	// credit to james-d
	
	private static final int MIN_PIXELS = 100;
	
	public static void enableImageViewScalingAndPanning(ImageView iv, Image image) {
		double width = image.getWidth();
        double height = image.getHeight();
		ObjectProperty<Point2D> mouseDownCoor = new SimpleObjectProperty<>();
		
		// set initial view port
		iv.setViewport(new Rectangle2D(0,0,image.getWidth(),image.getHeight()));
		
		iv.setOnMousePressed(e -> {
            Point2D mousePress = imageViewToImage(iv, new Point2D(e.getX(), e.getY()));
            mouseDownCoor.set(mousePress);
        });
		
		iv.setOnMouseDragged(e -> {
            Point2D dragPoint = imageViewToImage(iv, new Point2D(e.getX(), e.getY()));
            shift(iv, dragPoint.subtract(mouseDownCoor.get()));
            mouseDownCoor.set(imageViewToImage(iv, new Point2D(e.getX(), e.getY())));
        });
		
		iv.setOnScroll(e -> {
            double delta = e.getDeltaY();
            Rectangle2D viewport = iv.getViewport();
            
            double scale = clamp(Math.pow(1.01, delta),

                // don't scale so we're zoomed in to fewer than MIN_PIXELS in any direction:
                Math.min(MIN_PIXELS / viewport.getWidth(), MIN_PIXELS / viewport.getHeight()),

                // don't scale so that we're bigger than image dimensions:
                Math.max(width / viewport.getWidth(), height / viewport.getHeight())

            );

            Point2D mouseCoor = imageViewToImage(iv, new Point2D(e.getX(), e.getY()));

            double newWidth = viewport.getWidth() * scale;
            double newHeight = viewport.getHeight() * scale;

            // To keep the visual point under the mouse from moving, we need
            // (x - newViewportMinX) / (x - currentViewportMinX) = scale
            // where x is the mouse X coordinate in the image

            // solving this for newViewportMinX gives

            // newViewportMinX = x - (x - currentViewportMinX) * scale 

            // we then clamp this value so the image never scrolls out
            // of the imageview:

            double newMinX = clamp(mouseCoor.getX() - (mouseCoor.getX() - viewport.getMinX()) * scale, 
                    0, width - newWidth);
            double newMinY = clamp(mouseCoor.getY() - (mouseCoor.getY() - viewport.getMinY()) * scale, 
                    0, height - newHeight);

            iv.setViewport(new Rectangle2D(newMinX, newMinY, newWidth, newHeight));
        });

		// new function: when double clicked, center the image 
        iv.setOnMouseClicked(e -> {
        		if (e.getClickCount() == 2) {
        			Point2D mouseCoor = imageViewToImage(iv, new Point2D(e.getX(), e.getY()));
        			Rectangle2D view = iv.getViewport();
        			
        			double newMinX = clamp(mouseCoor.getX() - view.getWidth() / 2,
        					0, width - view.getWidth());
        			
        			double newMinY = clamp(mouseCoor.getY() - view.getHeight() / 2,
        					0, height - view.getHeight()); 
        			
        			iv.setViewport(new Rectangle2D(newMinX, newMinY, view.getWidth(), view.getHeight()));
            }
        });
		
	}
	
	public static void zoom(ImageView imageView, Image image, double scale) {
		double width = image.getWidth();
        double height = image.getHeight();
        Rectangle2D viewport = imageView.getViewport();
        
		double clampedScale = clamp(scale,
                Math.min(MIN_PIXELS / viewport.getWidth(), MIN_PIXELS / viewport.getHeight()),
                Math.max(width / viewport.getWidth(), height / viewport.getHeight())
        );
		
		double newWidth = viewport.getWidth() * clampedScale;
        double newHeight = viewport.getHeight() * clampedScale;
        
        double newMinX = clamp(viewport.getMinX() + viewport.getWidth() / 2 - (viewport.getWidth() / 2) * clampedScale, 
                0, width - newWidth);
        double newMinY = clamp(viewport.getMinY() + viewport.getHeight() / 2 - (viewport.getHeight() / 2) * clampedScale, 
                0, height - newHeight);
        
        imageView.setViewport(new Rectangle2D(newMinX, newMinY, newWidth, newHeight));
		
	}
	
	// convert mouse coordinates in the imageView to coordinates in the actual image:
    private static Point2D imageViewToImage(ImageView imageView, Point2D imageViewCoordinates) {
        double xProportion = imageViewCoordinates.getX() / imageView.getBoundsInLocal().getWidth();
        double yProportion = imageViewCoordinates.getY() / imageView.getBoundsInLocal().getHeight();

        Rectangle2D viewport = imageView.getViewport();
        return new Point2D(
                viewport.getMinX() + xProportion * viewport.getWidth(), 
                viewport.getMinY() + yProportion * viewport.getHeight());
    }
	
	public static void shift(ImageView imageView, Point2D delta) {
        Rectangle2D viewport = imageView.getViewport();

        double width = imageView.getImage().getWidth() ;
        double height = imageView.getImage().getHeight() ;

        double maxX = width - viewport.getWidth();
        double maxY = height - viewport.getHeight();
        
        double minX = clamp(viewport.getMinX() - delta.getX(), 0, maxX);
        double minY = clamp(viewport.getMinY() - delta.getY(), 0, maxY);

        imageView.setViewport(new Rectangle2D(minX, minY, viewport.getWidth(), viewport.getHeight()));
    }
	
	private static double clamp(double value, double min, double max) {
        if (value < min)
            return min;
        if (value > max)
            return max;
        return value;
    }
}
