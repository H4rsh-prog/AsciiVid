package com.aar.videostream.service;

import java.util.ArrayList;

import org.bytedeco.javacpp.indexer.UByteIndexer;
import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.global.opencv_highgui;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Point;
import org.bytedeco.opencv.opencv_core.Scalar;
import org.bytedeco.opencv.opencv_core.Size;
import org.bytedeco.opencv.opencv_videoio.VideoCapture;
import org.springframework.stereotype.Service;

@Service
public class AARService {
    final public char GRADIENT_ARRAY[] = " .,:;+*?%S#@".toCharArray();
    
    public char[][] generateAsciiArt(Mat image) {
    	int rows = image.rows();
    	int cols = image.cols();
    	char[][] ascii = new char[rows][cols];
    	try (UByteIndexer indexer = image.createIndexer()) {
    		for(int y=0;y<rows;y++) {
    			for(int x=0;x<cols;x++) {
    				int gradientIndx = (indexer.get(y, x) * (GRADIENT_ARRAY.length-1)) / 255;
    				ascii[y][x] = GRADIENT_ARRAY[gradientIndx];
    			}
    		}
    	}
    	return ascii;
    }
    public void processVideoStream(VideoCapture camera, int scale_y, int scale_x, int diffScale) {
    	if(!camera.isOpened()) throw new RuntimeException("Camera is not Opened");
    	int x = -1;
    	Mat frame = new Mat();
    	Mat grayFrame = new Mat();
    	Mat resizedFrame = new Mat();
    	Mat display = new Mat();
		FPSCounter fpsCounter = new FPSCounter();
    	
    	int fontFace = opencv_imgproc.FONT_HERSHEY_SIMPLEX;
	    double fontScale = 0.1;
	    Scalar fontColor = new Scalar(255, 255, 255, 0);
	    int thickness = 1;
	    int lineType = opencv_imgproc.LINE_8;
		while(!camera.read(frame)) continue;
	    
    	while(camera.read(frame) && !frame.empty()) {
    		opencv_imgproc.cvtColor(frame, grayFrame, opencv_imgproc.COLOR_BGR2GRAY);
    		opencv_imgproc.resize(grayFrame, resizedFrame, new Size(scale_x, scale_y));
    		char[][] result = generateAsciiArt(resizedFrame);
    		display = new Mat(scale_y*diffScale, scale_x*diffScale, opencv_core.CV_8UC1, new Scalar(0,0,0,0));
    		for(int i=0;i<scale_y;i++) {
    			for(int j=0;j<scale_x;j++) {
    				opencv_imgproc.putText(display, String.valueOf(result[i][j]), new Point(j*diffScale, i*diffScale), fontFace, fontScale, fontColor, thickness, lineType, false);
    			}
    		}
			double fps = fpsCounter.getFPS(System.nanoTime());
			opencv_imgproc.putText(display, String.format("FPS : %f", fps), new Point(10, 30), fontFace, fontScale*8, fontColor);
    		opencv_highgui.imshow("ascii_stream", display);
    		x = opencv_highgui.waitKey(1);
    		if(x!=-1) break;
    	}
    	opencv_highgui.destroyWindow("ascii_stream");
    	camera.release();
    }
    
    public ArrayList<char[][]> processVideoObject(VideoCapture camera, int scale_y, int scale_x) {
    	if(!camera.isOpened()) throw new RuntimeException("Camera is not Opened");
    	int x = -1;
    	ArrayList<char[][]> response = new ArrayList<>();
    	Mat frame = new Mat();
    	Mat grayFrame = new Mat();
    	Mat resizedFrame = new Mat();
    	
    	while(!camera.read(frame)) continue;
	    int count = 0;
    	while(camera.read(frame) && !frame.empty()) {
    		opencv_imgproc.cvtColor(frame, grayFrame, opencv_imgproc.COLOR_BGR2GRAY);
    		opencv_imgproc.resize(grayFrame, resizedFrame, new Size(scale_x, scale_y));
        	Mat display = new Mat(100, 200, opencv_core.CV_8UC1, new Scalar(0,0,0,0));
        	opencv_imgproc.putText(display, "Press any key to stop video processing", new Point(0, 24), 1, 1, new Scalar(255,255,255,0));
        	opencv_imgproc.putText(display, "frames processed {"+count+"}", new Point(0, 12), 1, 1, new Scalar(255,255,255,0));
    		char[][] result = generateAsciiArt(resizedFrame);
    		response.add(result);
    		opencv_highgui.imshow("ascii_stream", display);
    		x = opencv_highgui.waitKey(1);
    		if(x!=-1) break;
    		count++;
    	}
    	opencv_highgui.destroyWindow("ascii_stream");
    	camera.release();
    	return response;
    }
    
    public void playProcessedObject(ArrayList<char[][]> processedObject) {
    	int scale_x = processedObject.get(0)[0].length;
    	int scale_y = processedObject.get(0).length;
    	int x = -1;
    	int diffScale = 5;
    	
    	int fontFace = opencv_imgproc.FONT_HERSHEY_SIMPLEX;
	    double fontScale = 0.1;
	    Scalar fontColor = new Scalar(255, 255, 255, 0);
	    int thickness = 1;
	    int lineType = opencv_imgproc.LINE_8;
		for(char[][] frame : processedObject) {
			Mat display = new Mat(scale_y*diffScale, scale_x*diffScale, opencv_core.CV_8UC1, new Scalar(0,0,0,0));
    		for(int i=0;i<scale_y;i++) {
    			for(int j=0;j<scale_x;j++) {
    				opencv_imgproc.putText(display, String.valueOf(frame[i][j]), new Point(j*diffScale, i*diffScale), fontFace, fontScale, fontColor, thickness, lineType, false);
    			}
    		}
    		opencv_highgui.imshow("ascii_stream", display);
    		x = opencv_highgui.waitKey(1);
    		if(x!=-1) break;
    	}
    	opencv_highgui.destroyWindow("ascii_stream");
    }
}
