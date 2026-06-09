package com.aar.videostream.service;

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
    public void realtimeAsciiArt(VideoCapture camera, int scale_y, int scale_x) {
    	if(!camera.isOpened()) throw new RuntimeException("Camera is not Opened");
    	int x = -1;
    	int diffScale = 5;
    	Mat frame = new Mat();
    	Mat grayFrame = new Mat();
    	Mat resizedFrame = new Mat();
    	Mat display = new Mat();
    	
    	int fontFace = opencv_imgproc.FONT_HERSHEY_SIMPLEX;
	    double fontScale = 0.1;
	    Scalar fontColor = new Scalar(255, 255, 255, 0); // White color (B, G, R, A)
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
    		opencv_highgui.imshow("realtime ascii art", display);
    		x = opencv_highgui.waitKey(1);
    		if(x!=-1) break;
    	}
    	opencv_highgui.destroyWindow("realtime ascii art");
    	camera.release();
    }
}
