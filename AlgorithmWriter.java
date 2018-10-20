import java.awt.*;
import java.io.*;
import java.util.Scanner;
public class AlgorithmWriter {
    private int tabs = 0;
    private int step = 1;
    public static void main(String[] args) {
        AlgorithmWriter algorithmWriter = new AlgorithmWriter();
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter the path to the the java file for your program:");
        String path = scan.nextLine();
        String className = path.substring(path.lastIndexOf(File.separator)+1, path.lastIndexOf('.'));
        if(path.startsWith(".")) {
            String dir = System.getProperty("user.dir");
            path = path.substring(1);
            path = dir+path;
        }
        if(path.startsWith("~")) {
            String home = System.getProperty("user.home");
            path = path.replace("~",home);
        }
        File file = new File(path);
        File txtAlgo = new File(file.getParent()+File.separator+className+"Algo.txt");
        try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
            //PrintWriter for writing to txt file.
            PrintWriter writer = new PrintWriter(txtAlgo);
            String line = reader.readLine();
            while(line!=null) {
                if(line.contains("{")) {
                    algorithmWriter.openBrace();
                    line = line.replace("{","");
                }
                if(line.contains("}")) {
                    algorithmWriter.closeBrace();
                    line = line.replace("}","");
                }
                line = line.trim();
                if(line.equals("")) {
                    line = reader.readLine();
                    continue;
                }
                //Line object of this read line
                Line thisLine = new Line(line, className);
                String processed = thisLine.process();
                //If the process line is blank, skip adding it to the file.
                if(processed.equals("")) {
                    line = reader.readLine();
                    continue;
                }
                processed = algorithmWriter.getTabs()+"STEP "+
                        algorithmWriter.getStep()+" : "+processed;
                //If there is no full stop at the end of the processed line.
                if(!processed.endsWith(".")) {
                    //Add it.
                    processed += ".";
                }
                writer.println(processed);
                algorithmWriter.nextStep();
                line = reader.readLine();
            }
            writer.close();
            System.out.println("Algorithm Writing was successful!");
            System.out.println("It may not be perfect but if you edit"+
                    " it in some places, you are sure to have a working algorithm!");
            System.out.println("Here is your txt file : "+txtAlgo.getAbsolutePath());
            Desktop desktop = Desktop.getDesktop();
            desktop.open(txtAlgo);
        }
        catch(IOException e) {
            System.out.println("The algorithm writing was not successful...");
            System.out.println("Are you positive that there is a "+file.getName()+
                    " file in that directory?");
        }
    }
    private String getTabs() {
        StringBuilder tab = new StringBuilder();
        for(int i=0; i<tabs; i++)
            tab.append("\t");
        return tab.toString();
    }
    private void openBrace() {
        tabs++;
    }
    private void closeBrace() {
        tabs--;
    }
    private void nextStep() {
        step++;
    }
    private int getStep() {
        return step;
    }
}