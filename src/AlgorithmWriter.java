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
                //If the line contains an inline multiple line comment.
                if(line.contains("/*") && line.contains("*/")) {
                    line = algorithmWriter.removeMultipleLineComment(line);
                }
                //If the line is a single line comment
                if(line.startsWith("//")) {
                    line = reader.readLine();
                    continue;
                }
                //If this is the start of a new multiple line comment
                if(line.startsWith("/*")) {
                    algorithmWriter.commentIsOn = true;
                }
                //If a multiple line comment ends here,
                if(line.endsWith("*/")) {
                    algorithmWriter.commentIsOn = false;
                }
                if(line.equals("*/") || line.equals("/*")) {
                    line = reader.readLine();
                    continue;
                }
                //If a multiple line comment is going on, then do not process this line.
                if(algorithmWriter.commentIsOn) {
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
                //Remove any single line comments
                line = algorithmWriter.removeSingleLineComment(line);
                line = line.trim();
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
            System.out.println("[!] "+e.getClass());
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
    private String removeSingleLineComment(String line) {
        int length = line.length();
        int numberOfBrackets = 0;
        int numberOfQuotes = 0;
        StringBuilder result = new StringBuilder();
        for(int i=0; i<length; i++) {
            char ch = line.charAt(i);
            if(ch=='(') {
                numberOfBrackets++;
            }
            else if(ch==')') {
                numberOfBrackets--;
            }
            if(ch=='\"' && numberOfQuotes==0) {
                if(line.charAt(i-1)!='\\') {
                    numberOfQuotes++;
                }
            }
            else if(ch=='\"') {
                if(line.charAt(i-1)!='\\') {
                    numberOfQuotes--;
                }
            }
            if(numberOfBrackets==0 && numberOfQuotes==0 && ch=='/') {
                //If this character is the last character
                if(i==length-1) {
                    continue;
                }
                //If the next character is also a slash. Then, only,
                //it will be a single line comment.
                if(line.charAt(i+1)=='/') {
                    return result.toString();
                }
            }
            result.append(ch);
        }
        return result.toString();
    }
    private String removeMultipleLineComment(String line) {
        int length = line.length();
        int numberOfQuotes = 0;
        boolean commentOn = false;
        StringBuilder result = new StringBuilder();
        for(int i=0; i<length; i++) {
            char ch = line.charAt(i);
            //This condition is for dealing with the meaningless comments
            //like /*/*/. But this might cause some problems. If I come
            //across such bug, I will try to fix it.
            if(numberOfQuotes==0 && ch=='/' && i!=0 && i!=length-1
                    && line.charAt(i-1)=='*' && line.charAt(i+1)=='/') {
                continue;
            }
            if(numberOfQuotes==0 && ch=='*' && i!=0 && line.charAt(i-1)=='/') {
                commentOn = true;
                continue;
            }
            //If comment is on and the comment seems to end here
            if(commentOn && ch=='/') {
                boolean stillOn = true;
                //If the previous character was an asterisk.
                if(line.charAt(i-1)=='*') {
                    stillOn = false;
                }
                commentOn = stillOn;
                continue;
            }
            //If a comment is on.
            if(commentOn) {
                continue;
            }
            if(ch=='\"' && numberOfQuotes==0) {
                if(i!=0 && line.charAt(i-1)!='\\') {
                    numberOfQuotes++;
                }
            }
            else if(ch=='\"') {
                if(line.charAt(i-1)!='\\') {
                    numberOfQuotes--;
                }
            }
            //If all quotes are closed and a slash is encountered.
            if(numberOfQuotes==0 && ch=='/') {
                if(i==length-1) {
                    continue;
                }
                if(line.charAt(i+1)=='*') {
                    commentOn = true;
                    continue;
                }
            }
            result.append(ch);
        }
        return result.toString();
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
