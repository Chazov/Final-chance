import java.util.Scanner;

public class SyntaxAnalyzer {
    public static void main(String[] args) {
        System.out.println("Enter Lexical file name:");
        String name = new Scanner(System.in).nextLine();
        LexicalAnalyzer an = new LexicalAnalyzer(name);
        an.readFile();
       new LexicalDriver(name);
    }

    static class LexicalDriver {
        private LexicalAnalyzer lexer;
        private Automaton automaton;
        private Stack<String> stack;
        private int[][] matrix;
        private String s1, s2;

        LexicalDriver(String path) {
            lexer = new LexicalAnalyzer(path);
            automaton = new Automaton();
            System.out.println("--------------------------------");
            matrix = new int[][]{
                    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 3, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 6, 0, 0, 6, 0, 6, 6, 0, 0, 0},
                    {0, 0, 0, 0, 0, 8, 0, 8, 0, 0, 7, 7, 7},
                    {0, 0, 0, 10, 0, 0, 9, 0, 11, 12, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 13, 14, 15},};

            process();
        }

        private void process() {
            stack = new Stack<>();
            stack.push(automaton.initialSymbol());
            s1 = stack.top();

            s2 = lexer.getToken();

            while (!stack.isEmpty()) {
                if (isNoTerminal(s1)) {
                    int predic = pre(s1, s2);
                    if (predic != 0) {
                        replacePro(predic);
                        s1 = stack.top();
                    } else {
                        processError();
                    }
                } else {
                    if (s1.equals(s2)) {
                        stack.pop();
                        s1 = stack.top();

                        System.out.println(s1 + " " + " " + s2);

                        s2 = lexer.getToken();
                    } else {
                        processError();

                    }
                }
            }
            System.out.println();
            System.out.println("*** Analysis finalized ***");
        }

        private boolean isNoTerminal(String token) {
            String[] noTerm = automaton.symbolsNoTerminal();
            for (String s : noTerm) {
                if (token.equals(s)) {
                    return true;
                }
            }

            return false;
        }

        private int pre(String x, String a) {
            int i1 = -1, i2 = -1;
            String[] noTerm = automaton.symbolsNoTerminal();
            for (int i = 0; i < noTerm.length; i++) {
                if (x.equals(noTerm[i])) {
                    i1 = i;
                    break;
                }
            }

            String[] terminals = automaton.symbolsTerminal();
            for (int i = 0; i < terminals.length; i++) {
                if (a.equals(terminals[i])) {
                    i2 = i;
                    break;
                }
            }

            if (i2 == -1) {
                return 0;
            }
            return matrix[i1][i2];
        }

        private void replacePro(int indexP) {
            stack.pop();
            String[][] productions = automaton.productions();
            String[] production = productions[indexP - 1];
            for (int i = production.length - 1; i > -1; i--) {
                if (!production[i].equals("")) {
                    stack.push(production[i]);
                }
            }
        }

        private void processError() {
            System.out.print("Syntax error: ");
            System.out.print("Expecting: '" + s1 + "' , found: " + s2);
            System.out.println("\t");
            System.out.println("Couldn't continue. Error found.");
            System.exit(0);
            System.out.println();
            stack.pop();

            s1 = stack.top();

            s2 = lexer.getToken();

        }
    }

}
