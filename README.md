# algorithm-writer
A simple program to create algorithms from an already present `JAVA` program. This might sound really strange, but where I am from, the students need to write algorithms (somewhat like pseudocode, but not really) for every program they write in their projects. Most of them, if not all, are not really interested in writing algorithms. I was one of those students. So I decided to create a program that would write algorithms for me. It is *far* from perfect, and is mostly designed to suit my own style of writing code, so it might not really be useful to you. But if you follow the basic points listed below, this program will create an algorithm for your code. Further, if you make slight changes to the algorithm, you will have what you need! 

## Keep in mind
* This program is made with java, for java.
* The algorithms (*pseudocode*) are in no way standard, and in fact, are written in my own style.
* This was a project I made for fun, so not everything in the language is handled.
* This is not a **standard** pseudocode writer. But the result produced might be considered a pseudocode.
* There are **lots** of broken things in this. So do not expect it to work very well.

## Basic points to follow
* Always enclose the loops, conditional statements and other such blocks in braces (even the single lines).
* Put all your braces in a separate line and **NOT** like `if(someCondition) { //Do something } `.
* Do **NOT** write two statements in a single line.
* Even with `case` statements, the code should look like :
```
case 1 :
System.out.println("Hello World!")
break;
```
* The comments should not be written in line with a statement. Not even multiple-line comments.
* Obviously, the code should be syntactically correct (language-wise).

## Example
This is an example code (Notice the syntax and format. The indentation is not required, but the statements should be on separate lines.):
```
public class test
{
    public static void main(String[] args)
    {
        String string = "Hello world!";
        int l = string.length();
        String other = "";
        for(int i=0; i<l; i++)
        {
            if((i+1)%2==0)
            {
                continue;
            }
            other += string.charAt(i);
        }
        l = other.length();
        switch(l)
        {
            case 3 :
                System.out.println("LENGTH is 3 :)");
                break;
            case 5 :
                System.out.println("LENGTH is 5 :(");
                break;
            default :
                System.out.println("LENGTH is "+l+" :|");
                break;
        }
    }
}
```
This is the algorithm written by the algorithm-writer (The indentation is made automatically):
```
STEP 1 : A PUBLIC class test is created.
    STEP 2 : A method PUBLIC STATIC VOID main(String[] args) is created.
        STEP 3 : STRING variable string is instantiated to value "Hello world!".
        STEP 4 : INT variable l is instantiated to value string.length().
        STEP 5 : STRING variable other is instantiated to value "".
        STEP 6 : FOR int i=0 i<l i++.
            STEP 7 : IF (i+1)%2==0.
                STEP 8 : CONTINUE.
            STEP 9 : other is added with the value given by string.charAt(i).
        STEP 10 : l ← other.length().
        STEP 11 : SWITCH l.
            STEP 12 : CASE 3.
            STEP 13 : PRINTLN "LENGTH is 3 :)".
            STEP 14 : BREAK.
            STEP 15 : CASE 5.
            STEP 16 : PRINTLN "LENGTH is 5 :(".
            STEP 17 : BREAK.
            STEP 18 : DEFAULT.
            STEP 19 : PRINTLN "LENGTH is "+l+" :|".
            STEP 20 : BREAK.
```
## Syntax of result
The syntax of the result, which might be considered a pseudocode to some extent, is somewhat similar to that used in the book `Introduction to Algorithms`. It may be explained as:

* Indentation indicates block structure. For example, in the above example, the steps 7, 8 and 9 are inside the `for` loop started in step 6, but step 9 is out of it. Similarly, step 8 is inside the `if` statement in step 7, but step 9 is out of it.
* The symbol `←` indicates only assignment, and not initialization, which is specified by words. So, the statement `l ← other.length()` means that the value `other.length()` is assigned to the variable `l`.
* The access modifiers, if any, are mentioned in the result. So, if a variable is declared as : `public boolean l`, the result is `A variable PUBLIC BOOLEAN l is declared`.

And for other conventions, you will have to try different things!

## How to use
Clone or download the repository. Then, navigate to the src folder. Compile the `java` files and then run the `AlgorithmWriter` class. You will be asked:
`Enter the path to the java file for your program:`
Enter the path to your java file. For example, if you have a `hello.java` file in a directory called `Hello`, which is located in `/home/foo`, then enter the string `/home/foo/Hello/hello.java`. This is the path to your `java` file, for which your algorithm will be written.
* If you enter `./hello.java`, the program will look for `hello.java` in the directory from which the program was started.
* If you enter `~/hello.java`, the program will look for `hello.java` in your `home` directory, which, for linux, is `/home/username`.
If there was an error, you will be told so. **An exception may also be thrown**. If the algorithm was written successfully, the `.txt` file will be saved in the same directory as the `hello.java` file. After completion, a message will be printed and the text file will be opened with the default program for viewing text files.
Voila! You have an algorithm for your code!
However, as the message printed after completion says:
```
Algorithm Writing was successful!
It may not be perfect but if you edit it in some places, you are sure to have a working algorithm!
Here is your txt file : /home/foo/Hello/helloAlgo.txt!
```
You may need to edit the algorithm to suit your needs.
