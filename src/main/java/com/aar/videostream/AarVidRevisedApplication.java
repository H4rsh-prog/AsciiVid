package com.aar.videostream;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.opencv.global.opencv_core;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class AarVidRevisedApplication {

	static {
		Loader.load(opencv_core.class);
	}
	
	public static void main(String[] args) {
		SpringApplication.run(AarVidRevisedApplication.class, args);
	}

}
