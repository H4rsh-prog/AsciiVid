package com.aar.videostream.controller;

import org.bytedeco.opencv.opencv_videoio.VideoCapture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aar.videostream.service.AARService;

@RestController
public class Controller {
	@Autowired AARService aarService;
	
	@GetMapping("/")
	public Object base() throws InterruptedException {
		VideoCapture vidCap = new VideoCapture(0);
		try {
			while(!vidCap.isOpened()) continue;
			aarService.realtimeAsciiArt(vidCap, 50, 50);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			vidCap.release();
		}
		return null;
	}
}
