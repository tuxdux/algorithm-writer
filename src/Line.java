//This class, constructor and the process method are all
//package private.
class Line {
    private String line;
    private String className;
    private String[] dataTypes = {"int", "char", "float", "double", "byte", "boolean", "short",
            "String", "long", "void"};
    private String[] accessModifiers = {"public", "private", "protected", "volatile"};
    private boolean semicolon = false;
    Line(String line, String className) {
        //Remove any single line comments
        if(line.contains("//")) {
            int index = line.indexOf('/');
            //The line without the // comment
            line = line.substring(0,index);
        }
        //Remove any multiple line comments within the line itself
        //Example : A multiple line comment can be inserted in a statement as :
        //          if(/*Hello*/n==10)
        if(line.contains("/*")) {
            int start = line.indexOf('/');
            int end = line.lastIndexOf('/');
            //This is the comment without the last '/'. Suppose the comment
            //was written as /*Hello there*/, then this string stores the
            //value "/*Hello there*"
            String comment = line.substring(start,end);
            //Thus, we add the last '/' explicitly.
            comment = comment+"/";
            //Then, we remove the comment
            line = line.replace(comment, "");
        }
        //If the line contains semicolon, it is a statement.
        if(line.contains(";")) {
            semicolon = true;
            if(line.startsWith("for")) {
                //Replace the semicolon with comma for clarity.
                line = line.replace(";", ",");
            }
            else {
                //Remove the semicolon.
                line = line.replace(";", "");
            }
        }
        //Remove any braces in the declaration.
        line = line.replace("{", "");
        //Trim the line
        line = line.trim();
        //Finally, store the line in the class variable.
        this.line = line;
        this.className = className;
    }
    String process() {
        String string;
        //We begin by checking if the line contains the package keyword.
        if(line.contains("package ")) {
            line = line.replace("package", "PACKAGE");
            string = "This class is in "+line;
        }
        else if(line.contains("import ")) {
            line = line.replace("import", "IMPORT");
            string = line;
        }
        //If this line is for throwing an exception.
        //As in, throw new MyCustomException();
        else if(line.contains("throw ")) {
            string = line.replace("throw", "THROW");
        }
        else if(line.contains("System.out.print")) {
            int firstBracket = line.indexOf("(");
            int lastBracket = line.lastIndexOf(")");
            String toPrint = line.substring(firstBracket+1,lastBracket);
            if(toPrint.equals("")) {
                toPrint = "\'Blank Line\'";
            }
            if(line.contains(".println"))
                string = "PRINTLN "+toPrint;
            else
                string = "PRINT "+toPrint;
        }
        //Handling the void return.
        else if(line.equals("return")) {
            string = "RETURN";
        }
        else if(line.contains("return ")) {
            string = "RETURN ";
            String[] words = line.split("\\s+");
            StringBuilder value = new StringBuilder();
            for(String word : words) {
                if(!word.equals("return"))
                    value.append(word);
            }
            string += value.toString();
        }
        else if(line.startsWith("switch(") || line.startsWith("switch (")) {
            string = "SWITCH";
            int firstBracket = line.indexOf('(');
            int secondBracket = line.lastIndexOf(')');
            String variable = line.substring(firstBracket+1, secondBracket);
            string = string.concat(" ").concat(variable);
        }
        else if(line.startsWith("case ")) {
            string = line.replace("case", "CASE");
            string = string.replace(":", "").trim();
        }
        else if(line.startsWith("default ")) {
            string = line.replace("default", "DEFAULT");
            string = string.replace(":", "").trim();
        }
        //The break with a space afterwards is for breaking out of tagged
        //loops like so : break outerLoop;
        else if(line.startsWith("break") || line.startsWith("break ")) {
            string = line.replace("break","BREAK");
        }
        //Same for continue statement.
        else if(line.equals("continue") || line.startsWith("continue ")) {
            string = line.replace("continue","CONTINUE");
        }
        //This is for handling @Override and others.
        else if(line.startsWith("@")) {
            string = line.toUpperCase();
        }
        //If the line was not a case or default statement, then this is
        //tagging statement.
        else if(line.endsWith(":")) {
            String tag = line.substring(0, line.indexOf(':')).trim();
            string = "Tagged as "+tag+" :";
        }
        else if(line.equals("finally")){
            string = "FINALLY";
        }
        else if(isClassLine()) {
            string = processClass();
        }
        //Check if the line is a constructor declaration.
        else if(isConstructor()) {
            string = processConstructor();
        }
        //If line is not a constructor, check if it is
        //the statement for a loop.
        else if(isLoop()) {
            string = processLoop();
        }
        //If it is not even a loop, check if it is start of a conditional
        //statement.
        else if(isCondition()) {
            string = processCondition();
        }
        //If the statement is the start of a try statement, just replace the
        //'try' keyword with 'TRY'.
        else if(isTry()) {
            string = line.replace("try", "TRY");
        }
        //If the statement is a catch statement
        else if(isCatch()) {
            string = line.replace("catch", "CATCH");
        }
        //If it is not even a condition statement, check if it is a method
        //definition.
        else if(isMethod()) {
            string = processMethod();
        }
        else if(isShorthand()) {
            string = processShorthand();
        }
        else if(isCommaStatement()) {
            StringBuilder result = new StringBuilder();
            String[] words = line.split("\\s+");
            for(String word : words) {
                if(isDataType(word))
                    result.append(word.toUpperCase()).append(" ");
                else if(word.equals(","))
                    result.append("and ");
                else
                    result.append(word).append(" ");
            }
            string = result.toString();
        }
        //Lastly, after all the above checks, if nothing is true, process
        //the line as a normal statement
        else {
            string = processStatement();
        }
        return string.trim();
    }

    private boolean isShorthand() {
        int index = line.indexOf("=");
        if(index==-1)
            return false;
        char beforeEqual = line.charAt(index-1);
        //If the character before the equal to sign is a letter or
        //a digit, then there is a variable name before the equal to
        //sign. But if is a special character, then it is almost certainly
        //a shorthand java operator, which we will process later.
        return  beforeEqual!=' ' && !Character.isLetterOrDigit(beforeEqual);
    }
    private boolean isClassLine() {
        return line.contains("class ");
    }
    private boolean isConstructor() {
        return line.contains(className+"(") && !line.contains("=");
    }
    private boolean isTry() {
        return line.equals("try") || line.startsWith("try(") || line.startsWith("try (");
    }
    private boolean isCatch() {
        return line.startsWith("catch(") || line.startsWith("catch (");
    }
    private boolean isMethod() {
        //Since the method check is done after all other checks, it is
        //easy to determine if the line is a method definition. The line
        //simply needs to contain '(' and should not contain '=' or '.' or ';',
        //otherwise, it can also be an object initialization or a function call.
        return !line.contains("=") && !line.contains(".") && !semicolon && line.contains("(");
    }
    private boolean isCondition() {
        //If the line starts with "if", "else if" or "else", the line
        //is a conditional statement.
        return line.startsWith("if") || line.startsWith("else");
    }
    private boolean isLoop() {
        //If the line contains 'for' or 'while' or 'do' the line is
        //a loop statement.
        return line.startsWith("for") || line.startsWith("while") || line.startsWith("do");
    }
    private boolean isCommaStatement() {
        //There can be a method call like method(parameter1, parameter2);
        //There can also be initialization or declaration of objects like
        //Class obj = new Class(parameter1, parameter2);
        //Thus, we check if the statement contains a comma and does not
        //contain a bracket. If it does contain bracket, it will be processed
        //like a normal statement.
        return line.contains(",") && !line.contains("(");
    }
    private String processClass() {
        line = line.replace("class","");
        StringBuilder result = new StringBuilder("A ");
        String[] words = line.split("\\s+");
        for (String word : words) {
            if (isAccessModifier(word)) {
                result.append(word.toUpperCase());
            } else if (word.equals(className)) {
                result.append(" class ").append(className);
            } else if (word.equals("extends")) {
                result.append(" which inherits from class ");
            } else if (word.equals("implements")) {
                result.append(" which implements an interface ");
            }
            //If the above conditions are false, then this word is either
            //the super class or an interface.
            else{
                result.append(word);
            }
        }
        result.append(" is created.");
        return result.toString();
    }
    private String processConstructor() {
        //The index of the first bracket. The constructor line
        //is guaranteed to have brackets.
        int firstBracket = line.indexOf('(');
        //The substring of line after the bracket (inclusive)
        String parameters = line.substring(firstBracket);
        return "A constructor "+className+parameters+" is created.";
    }
    private String processMethod() {
        StringBuilder result = new StringBuilder("A method");
        //If the line contains no spaces, this means that the line is for
        //calling a method and not for creating one. For example, the line :
        //setFact() calls the method setFact() instead of creating it.
        if(line.indexOf(' ')==-1) {
            //This is to empty the string builder first.
            result = new StringBuilder();
            result.append("The method ").append(line).append(" is called.");
            return result.toString();
        }
        //The index of the first bracket.
        int firstBracket = line.indexOf('(');
        int lastBracket = line.lastIndexOf(')');
        //The substring of the line before the start of the brackets.
        String methodStatement = line.substring(0,firstBracket).trim();
        //The substring of the line after the start of the brackets (inclusive)
        //till the bracket is closed (also inclusive).
        String parameters = line.substring(firstBracket, lastBracket+1).trim();
        String[] words = methodStatement.split("\\s+");
        //This loop capitalizes every word before the method name and
        //adds it to the result. For example, if the statement was
        //public static void main()
        //Then, this would make the result "A method PUBLIC STATIC VOID"
        for(int i=0; i<words.length-1; i++) {
                result.append(" ").append(words[i].toUpperCase());
        }
        String methodName = words[words.length-1];
        result.append(" ").append(methodName).append(parameters).append(" is created");
        //Replace the commas with 'and'.
        line = line.replace(",", " and ");
        //Also, fix any consecutive spaces.
        line = line.replaceAll("\\s+", " ");
        //If this method throws an exception.
        int flag = 0;
        if(line.contains("throws ")) {
            result.append(", which throws");
            words = line.split("\\s+");
            for(String word : words) {
                if(flag==1)
                    result.append(" ").append(word);
                if(word.equals("throws"))
                    flag = 1;
            }
        }
        return result.toString();
    }
    private String processShorthand() {
        //Remove all spaces in the line so that they are not
        //included in either of the three builders below.
        line = line.replace(" ","");
        StringBuilder result = new StringBuilder();
        StringBuilder variable = new StringBuilder();
        StringBuilder value = new StringBuilder();
        //This is to get the shorthand operator used.
        boolean equalToCrossed = false;
        StringBuilder operate = new StringBuilder();
        for(int i=0; i<line.length(); i++) {
            char ch = line.charAt(i);
            //If the character ch is not a letter nor a digit,
            //and equal-to has not been crossed yet, then ch
            //is part of the operator.
            if(!Character.isLetterOrDigit(ch) && !equalToCrossed)
                operate.append(ch);
            //If the character is not part of the operator
            //and equal-to has not been crossed yet, then this
            //is part of the variable name.
            else if(!equalToCrossed)
                variable.append(ch);
            //Else, the character is part of value
            else
                value.append(ch);
            if(ch=='=')
                equalToCrossed = true;
        }
        String operator = operate.toString();
        result.append(variable);
        switch(operator) {
            case "+=" : result.append(" is added with "); break;
            case "-=" : result.append(" is decreased by "); break;
            case "*=" : result.append(" is multiplied with "); break;
            case "/=" : result.append(" is divided by "); break;
            case "^=" : result.append(" is XOR'ed with "); break;
            case "|=" : result.append(" is OR'ed with "); break;
            case "&=" : result.append(" is AND'ed with "); break;
            case "<<=" : result.append(" is signed left-bit-shifted by "); break;
            case ">>=" : result.append(" is signed left-bit-shifted by "); break;
            case ">>>=" : result.append(" is unsigned right-bit-shifted by "); break;
            default : result.append(" ").append(operator).append(" "); break;
        }
        result.append("the value given by ").append(value);
        return result.toString();
    }
    private String processLoop() {
        int firstBracket = line.indexOf('(');
        int secondBracket = line.lastIndexOf(')');
        String condition;
        String loopType;
        //The condition string is the statement of the loop without the
        //brackets and the keyword, which is calculated only if there
        //are brackets present, that is, the statement is of a for loop
        //or a while loop.
        if(firstBracket!=-1 && secondBracket!=-1) {
            condition = line.substring(firstBracket + 1, secondBracket).trim();
            loopType = line.substring(0, firstBracket).trim();
        }
        //If there are no brackets present, it means that the line
        //is the start of a do loop.
        else {
            condition = "\b";
            loopType = "do";
        }
        return loopType.toUpperCase()+" "+condition;
    }
    private String processCondition() {
        if(line.equals("else"))
            return "ELSE";
        //The index of the first opening bracket, which is definitely there
        //since the condition of the statement being an else statement has
        //already been covered with the above 'if' block.
        int firstBracket = line.indexOf('(');
        int lastBracket = line.lastIndexOf(')');
        //This is the type of if statement (simple if or else if)
        String type = line.substring(0,firstBracket).trim();
        //This is the actual condition, even without the brackets.
        String condition = line.substring(firstBracket+1, lastBracket).trim();
        return type.toUpperCase()+" "+condition;
    }
    private String processStatement() {
        //Result has been made a StringBuilder because IntelliJ was bugging
        //me about String concatenations in loop.
        StringBuilder result = new StringBuilder();
        //If the line is an instantiation or assignment statement
        if(line.contains("=")) {
            //We get the parts of string before and after the '=' sign.
            String[] parts = line.split("=");
            //Now we get the first and second part in separate strings.
            String firstPart = parts[0].trim();
            String secondPart = parts[1].trim();
            //Split the parts further into single words.
            String[] firstPartWords = firstPart.split("\\s+");

            //Now, process the first part of the line
            for(int i=0; i<firstPartWords.length; i++) {
                String word = firstPartWords[i];
                //Boolean value to check if this word is the last word
                boolean isLast = i==firstPartWords.length-1;
                //Now process each word
                processWordInFirstPart(word, result, isLast);
            }

            //Boolean value to check if this statement has anything to
            //do with objects. If secondPart contains 'new'.
            boolean relatedWithObject = secondPart.contains("new ");
            //Now adjust the value of secondPart for assigning.
            secondPart = secondPart.replace("new", "");
            secondPart = secondPart.trim();

            //Now we have the result after processing the first part of
            //this statement.
            //If there are no spaces in the firstPart string, then this
            //is an assignment statement.
            if(firstPart.indexOf(' ')==-1) {
                if(relatedWithObject) {
                    //If the second part of the string contains square and not round
                    //brackets, this is not an object but an array.
                    if(!secondPart.contains("(") && secondPart.contains("["))
                        result.insert(0,"The array ").append(" is initialized to value ").
                                append(secondPart);
                    //Else, this is obviously an object.
                    else
                        result.insert(0, "The object ").append(" is initialized to value ").
                                append(secondPart);
                }
                else {
                    //Now we format the assignment statement.
                    //Here, \u2190 is the left arrow in UTF-8.
                    result.append(" ").append('\u2190').append(" ").append(secondPart);
                }
            }
            //If this statement is not an assignment statement, then this
            //is an initialization statement.
            else {
                if(relatedWithObject) {
                    result.append(" is created with value ").append(secondPart).append(".");
                }
                //If the second part (the value) has something to do
                //with arrays, and not objects.
                else if(hasArrayAsMajorValue(secondPart)) {
                    //Then process the second part according to array values.
                    result.append(" is instantiated to value in ").append(processArrayValue(secondPart));
                }
                else {
                    result.append(" is instantiated to value ").append(secondPart).append(".");
                }
            }
        }
        //If line does not contain "=" sign.
        else {
            //Incrementing statement.
            if(line.contains("++")) {
                int index = line.indexOf('+');
                String variable = line.substring(0,index);
                result.append("Variable ").append(variable).append(" is Incremented.");
            }
            //Decrementing statement.
            else if(line.contains("--")) {
                int index = line.indexOf('-');
                String variable = line.substring(0,index);
                result.append("Variable ").append(variable).append(" is Decremented.");
            }
            //If line has a dot operator, it means this line calls a function.
            else if(line.contains(".")) {
                String[] parts = line.split("\\.");
                String object = parts[0].trim();
                String method = parts[1].trim();
                result.append("The function ").append(method).append(" from object ").
                        append(object).append(" is called.");
            }
            //If the line contains a bracket, it is calling a method.
            else if(line.contains("(")) {
                result.append("The method ").append(line).append(" is called.");
            }
            else {
                String[] words = line.split("\\s+");
                for(int i=0; i<words.length; i++) {
                    String word = words[i];
                    boolean isLast = i==words.length-1;
                    processWordInFirstPart(word, result, isLast);
                }
                result.append(" is declared.");
            }
        }
        return result.toString();
    }
    private void processWordInFirstPart(String word, StringBuilder result, boolean isLast) {
        if(isAccessModifier(word)) {
            //If result is blank, that is, there was no keyword
            //before this, make the result such that if the word
            //was public, result is "A PUBLIC"
            if(result.toString().equals(""))
                result.append("A ").append(word.toUpperCase());
                //If there was already a keyword before this one
            else
                result.append(" ").append(word.toUpperCase());
        }
        //If word is final keyword
        else if(word.equals("final")) {
            //If result is blank, that is, there was no keyword before this
            if(result.toString().equals(""))
                result.append("A FINAL");
            else
                result.append(" FINAL");
        }
        //If word is static keyword
        else if(word.equals("static")) {
            //If result is blank, that is, there was no keyword before this
            if(result.toString().equals(""))
                result.append("A STATIC");
            else
                result.append(" STATIC");
        }
        //If word is a data type.
        else if(isDataType(word)) {
            //If there was no keyword before this
            if(result.toString().equals(""))
                result.append(word.toUpperCase()).append(" variable");
            else
                result.append(" ").append(word.toUpperCase()).append(" variable");
        }
        //If this word is the not the last word but all the above conditions
        //were false, then this is a statement for making an object of a class
        //or for making an array.
        else if(!isLast) {
            //If the word contains double square brackets, this line has
            //something to do with arrays.
            if(word.contains("[]")) {
                //We count the number of brackets in this word so as
                //to calculate the dimension of the array.
                int dimension = 0;
                for(int i=0; i<word.length(); i++) {
                    char ch = word.charAt(i);
                    if(ch=='[')
                        dimension++;
                }
                //If the result is still empty, this means this is an
                //initialization statement.
                //These appends to the builder makes the result as :
                //A 2D array 'word'  ||  2D array 'word'
                if(result.toString().equals(""))
                    result.append("A ").append(dimension).append("D array ").append(word);
                else
                    result.append(" ").append(dimension).append("D array ").append(word);
            }
            //If there were no brackets, then this word is the name of a
            //class, an object of which is being modified in this line.
            else {
                //If result is still blank
                if (result.toString().equals(""))
                    result.append("An object of class ").append(word);
                else
                    result.append(" object of class ").append(word);
            }
        }
        //If this word contains a dot operator, as with 'this' or any other
        //created object, then of course, there was nothing before this,
        //since while using a dot operator, we can only assign value.
        else if(word.contains(".")) {
            String[] parts = word.split("\\.");
            String object = parts[0];
            String variable = parts[1];
            //If the object is 'this' keyword
            if(object.equals("this")) {
                result.append("The member variable ").append(variable);
            }
            else {
                result.append("The variable ").append(variable).
                        append(" of object ").append(object);
            }
        }
        //This is for processing the variable name, since if all the above
        //conditions were false, there can only be a variable name left to
        //process in the first part of the statement.
        else {
            //If the result is still blank
            if(result.toString().equals(""))
                result.append(word);
            else
                result.append(" ").append(word);
        }
    }
    private String processArrayValue(String value) {
        int length = value.length();
        int dimension = 0;
        //The 'the' is lowercase because this method will not be called
        //if the array was in first part. So to match the case, this
        //'the' is made lowercase.
        StringBuilder result = new StringBuilder("the ");
        for(int i=0; i<length; i++) {
            char ch = value.charAt(i);
            if(ch=='[')
                dimension++;
        }
        //The index of the first bracket in the line.
        int firstBracket = value.indexOf('[');
        //The identifier of the array whose element is asked for.
        String variable = value.substring(0, firstBracket);
        //If variable contains a . operator, it means the array is
        //actually stored in some object.
        if(variable.contains(".")) {
            //Split the variable from the '.'
            String[] parts = variable.split("\\.");
            //Now, first part is the object,
            String object = parts[0];
            //and second part is the identifier of the actual array.
            String array = parts[1];
            variable = array+" of object "+object;
        }
        if(dimension==1) {
            int lastBracket = value.indexOf(']');
            //The index of the element which is asked.
            String index = value.substring(firstBracket+1, lastBracket);
            result.append("element ").append(index).append(" of array ").append(variable);
            return result.toString();
        }
        else if(dimension==2) {
            int secondBracket = value.lastIndexOf('[');
            int firstCloseBracket = value.indexOf(']');
            int lastCloseBracket = value.lastIndexOf(']');
            String row = value.substring(firstBracket+1,firstCloseBracket);
            String column = value.substring(secondBracket+1, lastCloseBracket);
            result.append("element at row ").append(row).append(" and column ").
                    append(column).append(" of array ").append(variable);
            return result.toString();
        }
        else {
            return value;
        }
    }
    private boolean hasArrayAsMajorValue(String value) {
        int squareBracketIndex = value.indexOf('[');
        int roundBracketIndex = value.indexOf('(');
        if(squareBracketIndex==-1)
            return false;
        //If there are no round brackets, then this obviously
        //is an array value.
        if(roundBracketIndex==-1)
            return true;
        //If the round bracket comes before the square bracket,
        //then this means that this is an object and not an array.
        //Thus we check if the index of square bracket is less than
        //that of round bracket.
        return squareBracketIndex<roundBracketIndex;
    }
    private boolean isAccessModifier(String word) {
        for(String modifier : accessModifiers) {
            if(word.equals(modifier))
                return true;
        }
        return false;
    }
    private boolean isDataType(String word) {
        for(String type : dataTypes) {
            if(word.equals(type)) {
                return true;
            }
        }
        return false;
    }
}
