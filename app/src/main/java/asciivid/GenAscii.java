package asciivid;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class GenAscii {
    public static void main(String[] args) {
        
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(new FileInputStream(new File("C:\\Users\\User\\OneDrive\\Programmers_Point\\SelfProjects\\__ProperProjects\\AsciiVid\\app\\bin\\rsrc\\Objects\\genArr.ser")));
            ArrayList<String[]> strArr = (ArrayList<String[]>) in.readObject();
            FileInputStream fin = new FileInputStream(new File("C:\\Users\\User\\OneDrive\\Programmers_Point\\SelfProjects\\__ProperProjects\\AsciiVid\\app\\bin\\rsrc\\Objects\\genArrFPS"));
            int fps = fin.read();
            int delay = 0;
            if(fps>20 && fps<=23) delay = 25;
            else if(fps>15 && fps<=20) delay = 40;
            else if(fps>10 && fps<=15) delay = 70;
            else if(fps<10) delay = 100;
            // delay = 30;                                     //FPS OVERRIDE
            for(String[] Arr : strArr){
                for(String str : Arr){
                    System.out.println(str);
                }
                Thread.sleep(delay);
                System.gc();
                System.out.print("\033["+Arr.length+"A\r");     //ANSI ESCAPE CODE
            }
            System.out.println("Playback completed, FPS Count = "+fps+" delay = "+delay);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
            }
        }
    }
}
