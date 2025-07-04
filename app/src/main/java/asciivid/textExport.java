package asciivid;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class textExport {
    public void genBuffer(ArrayList<String[]> strArr) throws Exception{
        System.out.println("Creating text buffer");
        FileWriter fw;
        int ctn  = 0;
        for(String[] arr : strArr){
            ctn++;
            fw = new FileWriter(new File("C:\\Users\\User\\OneDrive\\Programmers_Point\\SelfProjects\\__ProperProjects\\AsciiVid\\app\\bin\\rsrc\\textBuff\\"+ctn+".txt"));
            for(String str : arr){
                fw.write(str+"\n");
            }
        }
        System.out.println("buffer created");
    }
    public static void main(String[] args){
        try {
            ObjectInputStream in;
            in = new ObjectInputStream(new FileInputStream(new File("C:\\Users\\User\\OneDrive\\Programmers_Point\\SelfProjects\\__ProperProjects\\AsciiVid\\app\\bin\\rsrc\\Objects\\genArr.ser")));
            ArrayList<String[]> strArr = (ArrayList<String[]>) in.readObject();
            new textExport().genBuffer(strArr);
        } catch (Exception ex) {
        }
            
    }
}
