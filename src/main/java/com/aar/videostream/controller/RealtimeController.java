package com.aar.videostream.controller;

import org.bytedeco.opencv.opencv_videoio.VideoCapture;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aar.videostream.service.AARService;

@RestController
public class RealtimeController {
	private final AARService aarService;
    final int default_scale_x = 50;
    final int default_scale_y = 50;
    final int default_diffScale = 5;
    final boolean default_fpsToggle = true;

	public RealtimeController(AARService aarService) {
		this.aarService = aarService;
	}

	@GetMapping(value = "/realtime/help", produces = "text/plain")
	public String realtimeHelp() {
		return "GET /realtime/{diffScale}\n"
				+ "Opens the default camera and displays its ASCII video stream.\n"
				+ "diffScale must be a positive integer and controls the display spacing and size.\n"
				+ "The video is processed at 50x50 characters; for example, diffScale=5 creates a 250x250 display.\n"
				+ "Press any key in the video window to stop playback.\n"
				+ "Example: GET /realtime/5";
	}

	@GetMapping(value = "/realtime", produces = "text/plain")
	public String realtime() {
		return realtimeHelp();
	}

	@GetMapping("/realtime")
	public void realtime(
        @RequestParam(name = "diffScale", required = false) Integer diffScale,
        @RequestParam(name = "y", required = false) Integer y,
        @RequestParam(name = "x", required = false) Integer x,
        @RequestParam(name = "fps", required = false) Boolean fps
    ) {
		VideoCapture camera = new VideoCapture(0);
		try {
			while (!camera.isOpened()) continue;
			aarService.processVideoStream(camera,
                (y==null) ? default_scale_y : y,
                (x==null) ? default_scale_x : x,
                (diffScale == null) ? default_diffScale : diffScale,
                (fps==null) ? default_fpsToggle : fps);
		} finally {
			camera.release();
		}
	}
}
