package com.aar.videostream;

import java.util.ArrayList;


import org.bytedeco.opencv.opencv_videoio.VideoCapture;
import org.junit.jupiter.api.Test;

import com.aar.videostream.service.AARService;

public class CommandLineRunnerTest {
	@Test
	public void run() {
		int AspectX = 1000-60;
		int AspectY = 325;
		AARService service = new AARService();
		String filePath = "C:\\Users\\User\\Videos\\DV\\R35 GTR EDIT_ 4K - stay. (1080p, h264, youtube).mp4";
		ArrayList<char[][]> result = service.processVideoObject(new VideoCapture(filePath), AspectY, AspectX);
		for(char[][] Arr : result){
            for(char[] str : Arr){
                System.out.println(String.valueOf(str));
            }
            System.gc();
            System.out.print("\033["+Arr.length+"A\r");     //ANSI ESCAPE CODE
        }
	}
}
