# algorithm-writer
A simple program to create algorithms from an already present `JAVA` program. This might sound really strange, but where I am from, the students need to write algorithms for every program they write in their projects. Most of them, if not all, are not really interested in writing algorithms. I was one of those students. So I decided to create a program that would write algorithms for me. It is *far* from perfect, and is mostly designed to suit my own style of writing code, so it might not really be useful to you. But if you follow the basic points listed below, this program will create an algorithm for your code. Further, if you make slight changes to the algorithm, you will have what you need! 

### Keep in mind
1. This program is made with java, for java.
2. The algorithms are in no way standard, and in fact, are written in my own style.
3. This was a project I made for fun, so not everything in the language is handled.
4. This is not a **pseudocode** writer.

### Basic points to follow
* Always enclose the loops, conditional statements and other such blocks in braces (even the single lines).
* Put all your braces in a separate line and **NOT** like `if(someCondition) { //Do something } `.
* Do not write two statements in a single line.
* Even with `case` statements, the code should look like :
```
case 1 :
System.out.println("Hello World!")
break;
```
* Obviously, the code should be syntactically correct (language-wise).

### Example
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
