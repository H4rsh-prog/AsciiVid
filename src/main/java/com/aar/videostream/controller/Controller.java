package com.aar.videostream.controller;

import java.io.File;
import java.util.ArrayList;

import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_videoio.VideoCapture;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aar.videostream.service.AARService;

@RestController
public class Controller {
	final AARService aarService;

	Controller(AARService aarService) {
		this.aarService = aarService;
	}
	
	@GetMapping("/realtime/{diffScale}")
	public Object realtime(@PathVariable("diffScale") int diffScale) throws InterruptedException {
		VideoCapture vidCap = new VideoCapture(0);
		try {
			while(!vidCap.isOpened()) continue;
			aarService.processVideoStream(vidCap, 50, 50, diffScale);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			vidCap.release();
		}
		return null;
	}
	@GetMapping("/stream")
	public void vidStream(@RequestParam("file") String vidPath) {
		VideoCapture vidCap = new VideoCapture(vidPath);
		try {
			while(!vidCap.isOpened()) continue;
			aarService.processVideoStream(vidCap, 100, 200, 5);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			vidCap.release();
		}
	}
	
	@GetMapping("/process/vid")
	public void vidFile(@RequestParam("file") String vidPath, @RequestParam("diff") double diff) {
		VideoCapture vidCap = new VideoCapture(vidPath);
		try {
			if(!new File(vidPath).isFile()) throw new RuntimeException("FILE DOES NOT EXIST");
			while(!vidCap.isOpened()) continue;
			Mat frame = new Mat();
			if(!vidCap.read(frame) && frame.empty()) throw new RuntimeException("COULD NOT READ FRAME");
			int rows = frame.rows();
			int cols = frame.cols();
			vidCap.release();
			vidCap.open(vidPath);
			while(!vidCap.isOpened()) continue;
			ArrayList<char[][]> result = aarService.processVideoObject(vidCap, (int) (rows*diff), (int) (cols*diff));
			aarService.playProcessedObject(result);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			vidCap.release();
		}
	}
}
