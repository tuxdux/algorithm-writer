import java.awt.*;
import java.io.*;
import java.util.Scanner;
public class AlgorithmWriter {
    private int tabs = 0;
    private int step = 1;
    private boolean commentIsOn = false;
    private int lineNumber = 0;
    public static void main(String[] args) {
        AlgorithmWriter algorithmWriter = new AlgorithmWriter();
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter the path to the java file for your program:");
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
                //Next line.
                algorithmWriter.nextLine();
                line = line.trim();
                //If the line is a single line comment
                if(line.startsWith("//")) {
                    line = reader.readLine();
                    continue;
                }
                //If this is the start of a new multiple line comment
                if(line.startsWith("/*") && !algorithmWriter.commentIsOn) {
                    algorithmWriter.commentIsOn = true;
                    line = reader.readLine();
                    continue;
                }
                //If a multiple line comment is going on, then do not process this line.
                if(!line.contains("*/") && algorithmWriter.commentIsOn) {
                    line = reader.readLine();
                    continue;
                }
                //If a multiple line comment ends here,
                if(line.contains("*/") && algorithmWriter.commentIsOn) {
                    algorithmWriter.commentIsOn = false;
                    line = reader.readLine();
                    continue;
                }
                if(line.startsWith("{")) {
                    algorithmWriter.openBrace();
                }
                if(line.startsWith("}")) {
                    algorithmWriter.closeBrace();
                }
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
                if(!processed.endsWith(".") && !processed.endsWith(":")) {
                    //Add it.
                    processed += ".";
                }
                writer.println(processed);
                algorithmWriter.nextStep();
                //If the line ends with an opening brace, as is the case with
                //most IDEs, make sure the next step is properly indented.
                if(line.endsWith("{")) {
                    algorithmWriter.openBrace();
                }
                //Same as above.
                if(line.endsWith("}")) {
                    algorithmWriter.closeBrace();
                }
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
        //Other exceptions caused most probably due to bad formatting.
        catch(Exception e) {
            System.out.println("[!] Error Occurred.");
            System.out.println("[!] "+e.getMessage());
            System.out.println("[#] Could you check line number "+algorithmWriter.lineNumber+
                    " in your java file?");
            System.out.println("[#] Format it according to the README.md");
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
    private void nextLine() {
        lineNumber++;
    }
    private int getStep() {
        return step;
    }
}
