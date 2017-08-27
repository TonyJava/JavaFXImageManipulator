# JavaFXImageManipulator
A simple demo program for Image manipulation using JavaFX, OpenCV and TensorFlow

Disclaimer: A demo project for me to familiarize JavaFX and GitHub. Still learning the distribution licenses and if this repo violates any rules, I will take it down immediately.

Some functionalities are adapted from other repos. Thanks.
James-d for image panning and zooming
https://gist.github.com/james-d/ce5ec1fd44ce6c64e81a

Sample demo for integrating tensorflow in Java programme.
https://github.com/tensorflow/tensorflow

To run:

1. install e(fx)clipse, i.e. eclipse with JavaFX support.
https://www.eclipse.org/efxclipse/install.html 

2. install OpenCV as an external library
http://docs.opencv.org/3.0-beta/doc/tutorials/introduction/java_eclipse/java_eclipse.html

3. install TensorFlow as an external library
https://www.tensorflow.org/install/install_java

4. install Scene Bulider for JavaFX (Optional but highly recommended)
http://gluonhq.com/products/scene-builder/
and link up with eclipse
http://o7planning.org/en/10621/install-javafx-scene-builder-into-eclipse

Current functionalites:
Zoom and pan the image using both bottons and mouses.
Classify the image with pre-trained model.

In progress:
Some functionalies in the menu bar.
Selection of region of interest (ROI) and record down the corrdinates with respect to the original image.
Maybe something else.
