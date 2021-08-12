import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class LexicalAnalyzer {
    private static final String END_FILE = "$";
    private final int ID_ERROR = -1;
    private final int ID_OESPECIAL = 405;
    private final int ID_SCHAR = 401;
    private final int ID_INT_NUM = 310;
    private final int ID_FLOAT_NUM = 320;
    private final int ID_EQUAL = 301;
    private final int ID_ADD = 500;
    private final int ID_SUB = 501;
    private final int ID_MULT = 502;
    private int ID_KEYWORD = 600;
    private int ID_IDENTIF = 800;


    private FileReader file;
    private int lineCount = 1;
    private ListStack errors, symbols, validTokens, keywords, simpleCharacters, integerNumbers, floatNumbers;

    private char state = '0';
    private int linePos = 0;

    private int idToken = ID_ERROR;
    private String currentLine;
    private String token = "";
    private String type = "";
    private boolean cut = false;
    private boolean error = false;
    private boolean isId = false;

    private String[] reserveWords = new String[]{"compute", "begin", "end", " Real "," Integer ",
            "Read", "Write"};
    private String[] commands = new String[]{"Calculate", "Number", "Results"};

    LexicalAnalyzer(String path) {
        file = new FileReader(path);
        this.errors = new ListStack("Errors");
        this.symbols = new ListStack("Identifiers");
        this.validTokens = new ListStack("Valid Tokens");
        this.keywords = new ListStack("Keywords");
        this.simpleCharacters = new ListStack("Simple Characters");
        this.integerNumbers = new ListStack("Int Numbers");
        this.floatNumbers = new ListStack("Float Numbers");
    }

    void readFile() {
        currentLine = file.lineReader();
        while (currentLine != null) {
            for (int i = 0; i < currentLine.length(); i++) {

                this.analyzer(i);
                if (cut || i == currentLine.length() - 1) {
                    if (idToken == -1) {
                        this.addErrors();
                    } else if (idToken == 310) {
                        this.addInts();
                    } else if (idToken == 320) {
                        this.addFloats();
                    } else if (idToken >= 800) {
                        this.addSymbol();
                    } else if (idToken >= 600) {
                        this.addKeywords();
                    } else if (idToken == 401) {
                        this.addSimpleCharacter();

                    }
                    token = "";
                    error = false;
                    cut = false;

                }

            }
            currentLine = file.lineReader();
            lineCount++;
        }
        reset();
        show();
    }

    String getToken() {

        if (currentLine == null || endLine(currentLine, linePos)) {
            linePos = 0;
            currentLine = file.lineReader();
        }
        if (currentLine == null) {
            return END_FILE;
        }

        while (!endLine(currentLine, linePos)) {

            analyzer(linePos);
            if (cut || endLine(currentLine, linePos + 1)) {
                if (error) {
                    // if it was shorted with a character then backspace and then evaluate it
                    linePos++;

                    reset();
                    return grammarValue(ID_ERROR);

                } else {
                    if (idToken == ID_ERROR) {
                        idToken = classify(state);
                    }
                    //This line is to avoid the '' character in the output
                    int ID_ACHAR = 400;
                    if (!(isBlankSpace(currentLine.charAt(linePos))
                            && idToken == ID_ACHAR)) {
                        int id = idToken;
                        linePos++;

                        if (isKeyword(token)) {
                            String _token = token;
                            reset();
                            return _token;
                        }

                        if (id == ID_OESPECIAL) {
                            String _token = token;
                            reset();
                            return _token;
                        }

                        if (id == ID_ERROR || id == ID_ACHAR) {
                            id = token.charAt(0);
                            linePos--;
                        }

                        if (id == ID_ADD
                                || id == ID_SCHAR
                                || id == ID_EQUAL
                                || id == ID_SUB
                                || id == ID_MULT) {
                            String _token = String.valueOf(grammarValue(id));
                            reset();
                            return _token;
                        }

                        reset();
                        return String.valueOf(grammarValue(id));
                    }
                }
            }
            linePos++;
        }
        reset();
        return getToken();
    }

    private void analyzer(int position) {

        char ch = currentLine.charAt(position);
        if (isBlankSpace(ch) && !this.token .equals("")) {

            cut = true;
            idToken = classify(state);
        } else {

            switch (state) {
                case '0':
                    if (ch == '0') {
                        state = 'E'; //float
                    } else if (isDot(ch)) {
                        state = 'x';//error
                    } else if (isUpperCase(ch)) {
                        state = '4';//identifiers
                    } else if (Character.isLowerCase(ch)) {
                        state = '8';//keyword
                    } else if (isCharacter(ch)) {
                        idToken = classify(state);
                        //state = 'z';//
                        //print character
                    } else if (Character.isDigit(ch)) {
                        state = '1';//digit
                    } else if (isDot(ch) && !this.token.equals("")) {
                        state = 'E'; //float
                    } else if (isColon(ch)) {
                        state = 'I';
                    } else if (isEqual(ch)) {
                        state = 'x';
                    } else {
                        error = true;
                    }
                    break;

                case '1':
                    if (Character.isDigit(ch)) {
                        state = '1';
                        idToken = classify(state);
                    } else if (isDot(ch)) {
                        state = 'E';
                    } else if (isCharacter(ch)) {
                        state = 'x';
                    } else if (isAlphabet(ch)) {
                        state = 'x';
                    } else {
                        error = true;
                    }
                    break;

                case 'E':
                    if (Character.isDigit(ch) || isDot(ch)) {
                        idToken = classify(state);
                    } else {
                        state = 'x';
                    }

                    break;

                case '4':
                case 'D':
                    if (isAlphabet(ch) || Character.isDigit(ch)) { //a-z o 0-9
                        idToken = classify(state);
                        state = '4';
                    } else if (isUnderScore(ch)) {
                        state = 'D';
                        idToken = classify(state);
                    } else {
                        state = 'x';
                    }

                    break;
                case '8':
                    if (Character.isLowerCase(ch)) {
                        state = '8';
                        idToken = classify(state);
                    } else {

                        state = 'x';
                    }
                    break;
                case 'I':
                    if (isEqual(ch)) {
                        idToken = classify(state);
                    } else {
                        state = 'x';
                    }

            }

            if (!cut) {
                token += ch;
            }
            if (state == 'z') {
                cut = true;

            } else if (state == 'x') {
                idToken = ID_ERROR;
                error = true;
                addErrors();

            }
        }
    }

    String noBlankSpaces(String line) {

        char[] charArray = line.toCharArray();
        String stringWithoutSpaces = "";

        for (char c : charArray) {
            if ((c != ' ') && (c != '\t')) {
                stringWithoutSpaces = stringWithoutSpaces + c;
            }
        }
        return stringWithoutSpaces;
    }

    private int classify(char state) {
        switch (state) {
            case 'E':
                return ID_FLOAT_NUM;
            case '1':
                return ID_INT_NUM;
            case '4':
            case 'D':
                return ID_IDENTIF;
            case '0':
                return ID_SCHAR;
            case '8':
                return ID_KEYWORD;
            case '7':
                return ID_EQUAL;
            case 'I':
                return ID_OESPECIAL;
            default:
                return ID_ERROR;
        }
    }

    private String grammarValue(int c) {
        switch (c) {
            case ID_ERROR:
                return "Lexical Error";
            case ID_INT_NUM:
                return "intliteral";
            case ID_SCHAR:
            case ID_OESPECIAL:
                return token;
            case ID_ADD:
                return "+";
            case ID_SUB:
                return "-";
            case ID_MULT:
                return "*";
            case ID_FLOAT_NUM:
                return "realliteral";

        }
        return String.valueOf((char) c);
    }

    private String charType(int c) {
        switch (c) {
            case '!':
                if (c == 33) {
                    type = "Exclamation point";
                    return type;
                }
            case '"':
                if (c == 34) {
                    type = "Quotations marks";
                    return type;
                }
            case '#':
                if (c == 35) {
                    type = "Number sign";

                    return type;
                }
            case '$':
                if (c == 36) {
                    type = "Currency symbol";

                    return type;
                }
            case '%':
                if (c == 37) {
                    type = "Percent sign";

                    return type;
                }
            case '&':
                if (c == 38) {
                    type = "And symbol";

                    return type;
                }
            case '(':
                if (c == 40) {
                    type = "Left parentheses";

                    return type;
                }
            case ')':
                if (c == 41) {
                    type = "Right parentheses";
                    return type;
                }
            case '*':
                if (c == 42) {

                    type = "Asterisks";
                    return type;
                }
            case '+':
                if (c == 43) {
                    type = "Addition sign";

                    return type;
                }
            case ',':
                if (c == 44) {
                    type = "Comma";

                    return type;
                }
            case '-':
                if (c == 45) {
                    type = "Hyphen";

                    return type;
                }
            case '.':
                if (c == 46) {
                    type = "Period";

                    return type;
                }
            case '/':
                if (c == 47) {
                    type = "Slash";

                    return type;
                }
            case ':':
                if (c == 58) {
                    type = "Colon";

                    return type;
                }
            case ';':
                if (c == 59) {
                    type = "Semicolon";

                    return type;
                }
            case '<':
                if (c == 60) {
                    type = "Less than";

                    return type;
                }
            case '=':
                if (c == 61) {
                    type = "Equals Sign";

                    return type;
                }
            case '>':
                if (c == 62) {
                    type = "Greater than";

                    return type;
                }
            case '?':
                if (c == 63) {
                    type = "Question mark";

                    return type;
                }
            case '@':
                if (c == 64) {
                    type = "At symbol";

                    return type;
                }
            case '[':
                if (c == 91) {
                    type = "Left Bracket";
                    return type;
                }
            case ']':
                if (c == 93) {
                    return "Right Bracket";
                }
            case '^':
                if (c == 94) {
                    type = "Caret";

                    return type;
                }
            case '_':
                if (c == 95) {
                    type = "Underscore";

                    return type;
                }
            case '´':
                if (c == 96) {
                    type = "Apostrophe";
                    return type;
                }
            case '{':
                if (c == 123) {
                    type = "Left Brace";
                    return type;
                }
            case '|':
                if (c == 124) {
                    type = "Vertical line";
                    return type;
                }
            case '}':
                if (c == 125) {
                    type = "Right Brace";
                    return type;
                }
            case '~':
                if (c == 33) {
                    type = "Tilde";

                    return type;
                }
                break;
        }
        return type;
    }

    boolean isCharacter(char character) {
        return ((int) character >= 33 && (int) character <= 47)
                || ((int) character >= 58 && (int) character <= 64)
                || ((int) character >= 91 && (int) character <= 96)
                || ((int) character >= 123 && (int) character <= 254);
    }

    private boolean isKeyword(String sign) {
        boolean s = false;
        for (String r : reserveWords) {
            if (r.equals(sign)) {
                s = true;
                break;
            }
        }
        return s;
    }

    boolean isIdentif(String sign) {
        boolean s = false;
        for (String r : commands) {
            if (r.equals(sign)) {
                s = true;
            }
        }
        return s;
    }

    private boolean isUnderScore(char character) {
        return character == '_';
    }

    private boolean isDot(char character) {
        return character == '.';
    }

    private boolean isColon(char character) {
        return character == ':';
    }

    private boolean isEqual(char character) {
        return character == '=';
    }

    boolean isBlankSpace(char character) {
        return (character == ' ' || character == '\t');
    }

    private boolean isAlphabet(char character) {
        return (character >= 'a' && character <= 'z');
    }

    private boolean isUpperCase(char character) {
        return (character >= 'A'
                && character <= 'Z');
    }

    private boolean isAddition(char character) {
        return character == '+';
    }

    private boolean isSub(char character) {
        return character == '-';
    }

    private boolean isMult(char character) {
        return character == '*';
    }

    private void addSymbol() {
        isId = true;
        this.addStorage(symbols, token, "Identifiers", ID_IDENTIF, "Int", "" + lineCount);
        this.addStorage(validTokens, token, "Identifiers", ID_IDENTIF, "Int", "" + lineCount);
    }

    private void addSimpleCharacter() {
        char c = token.charAt(0);

        this.addStorage(simpleCharacters, token, "Simple Character", (int) c, charType(c), "" + lineCount);
        this.addStorage(validTokens, token, "Simple Character", (int) c, charType(c), "" + lineCount);
        reset();
    }

    private void addInts() {
        this.addStorage(integerNumbers, token, "Int Numbers", ID_INT_NUM, "Int", "" + lineCount);
        this.addStorage(validTokens, token, "Int Numbers", ID_INT_NUM, "Int", "" + lineCount);
        reset();
    }

    private void addFloats() {
        this.addStorage(floatNumbers, token, "Float Numbers", ID_FLOAT_NUM, "Float", "" + lineCount);
        this.addStorage(validTokens, token, "Float Numbers", ID_FLOAT_NUM, "Float", "" + lineCount);
        reset();
    }

    private void addKeywords() {
        if (isKeyword(token)) {
            this.addStorage(keywords, token, "Keywords", ID_KEYWORD, "Boolean expression", "" + lineCount);
            this.addStorage(validTokens, token, "Keywords", ID_KEYWORD++, "Boolean expression ", "" + lineCount);
            reset();
        } else {
            this.addErrors();
        }
    }

    private void addErrors() {
        this.addStorage(errors, token, "Errors", ID_ERROR, "Error", "" + lineCount);
        reset();
    }

    private void addStorage(ListStack st, String lex, String token1, int id, String type, String line) {
        Token token = new Token(id, token1, type, lex, line);

        if (isId) {
            if (st.existsLex(lex)) {
                token = st.getTokenbyLex(lex);
                token.setRep(token.getRep() + 1);
                token.setLine(token.getLine() + line);
                isId = false;
            } else {
                token.setId(ID_IDENTIF++);
                st.saveUp(token);
                isId = false;

            }
        } else {
            if (st.exists(token)) {
                token = st.getTokenbyId(id);
                token.setRep(token.getRep() + 1);
                token.setLine(token.getLine() + line);
            } else {
                st.saveUp(token);
            }
        }

    }

    private boolean endLine(String line, int pos) {
        return line.length() <= pos;
    }

    private void reset() {
        token = "";
        error = false;
        cut = false;
        state = '0';
        idToken = ID_ERROR;
    }

    private void show() {
        System.out.println("    Lexeme    | ID  |    Token    |   Type   |  Repetitions | Line");
        this.symbols.show();
        System.out.println("\t");
        this.keywords.show();
        System.out.println("\t");
        this.integerNumbers.show();
        System.out.println("\t");
        this.floatNumbers.show();
        System.out.println("\t");
        this.simpleCharacters.show();
        System.out.println("\t");
    }

    static class FileReader {

        BufferedReader br;
        String path;

        FileReader(String path) {
            this.path = path;
            try {
                br = new BufferedReader(new java.io.FileReader(path));
            } catch (FileNotFoundException ignored) {
            }
        }

        String lineReader() {
            try {
                return br.readLine();
            } catch (IOException ignored) {
            }
            return null;
        }

    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter file name:");
        String file = scanner.nextLine();
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(file);
        lexicalAnalyzer.readFile();
    }
}
