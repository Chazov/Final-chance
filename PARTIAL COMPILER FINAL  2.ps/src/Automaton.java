class Automaton {
    private String[] terminal, noTerminal;
    private String[][] prods;
    private String initSymbols;
    private ReadFile file = new ReadFile();
    private NodeListStack<String> noTerm;

    Automaton() {
        file.prepareFile("keywords.txt");
        System.out.println(" << No terminal >>");
        noTerminal = getNoTerminal();
        System.out.println();
        System.out.println(" << Productions >>");
        prods = getProds();
        System.out.println();
        System.out.println(" << Terminal >>");
        terminal = getTerminal();
        System.out.println();

        initSymbols = prepareInitialSymbols();

    }

    private String leftSide(String content) {
        int index = 0;
        char state = 'i';
        StringBuilder r = new StringBuilder();

        while (index < content.length()) {
            char ch = content.charAt(index);
            switch (state) {
                case 'i':
                    if (ch != '-' && ch != ' ' && ch != '\t') {
                        state = 'n';
                    } else {
                        state = 'e';
                    }
                    break;
                case 'n':
                    if (ch == '-') {
                        state = '-';
                    } else if (ch == ' ' || ch == '\t') {
                        state = 's';
                    } else {
                        state = 'n';
                    }
                    break;
                case '-':
                    if (ch == ' ' || ch == '\t') {
                        state = 'e';
                    } else if (ch == '>') {
                        state = '>';
                    } else {
                        state = 'n';
                    }
                    break;
                case 's':
                    if (ch == ' ' || ch == '\t') {
                        state = 's';
                    } else if (ch == '-') {
                        state = 't';
                    } else {
                        state = 'e';
                    }
                    break;
                case 't':
                    if (ch == '>') {
                        state = '>';
                    } else {
                        state = 'e';
                    }
                    break;
            }

            if (state == 'e') {
                return null;
            }
            if (ch != ' ' && ch != '\t') {
                r.append(ch);
            }
            if (state == '>') {
                r = new StringBuilder(r.substring(0, r.length() - 2));
                return r.toString();

            }
            index++;
        }
        return null;
    }

    private String[] rightSide(String content) {
        NodeListStack<String> productions = new NodeListStack<>();
        StringBuilder symbol = new StringBuilder();
        int index = 0;
        char state = 'i';

        while (index < content.length()) {
            char ch = content.charAt(index);
            switch (state) {
                case 'i':
                    if (ch == '-') {
                        state = '-';
                    }
                    break;
                case '-':
                    if (ch == '>') {
                        state = '>';
                    }
                    break;
                case '>':
                    if (ch == ' ' || ch == '\t') {
                        if (index == content.length() - 1) {
                            if (productions.size() == 0) {
                                System.out.println("  ");
                                return new String[]{""};
                            }
                        }
                    } else {
                        symbol.append(ch);
                        state = 'n';
                    }
                    break;
                case 'n':
                    if (ch == ' ' || ch == '\t') {
                        productions.addLast(symbol.toString());
                        symbol = new StringBuilder();
                        state = '>';
                    } else {
                        symbol.append(ch);
                    }
                    break;
            }
            index++;
        }
        if (state == 'n') {
            productions.addLast(symbol.toString());
        }
        if (productions.size() == 0) {
            return new String[]{""};
        } else {
            String[] rightS = new String[productions.size()];
            for (int i = 0; i < rightS.length; i++) {
                rightS[i] = productions.getElement(i);
                System.out.print(rightS[i] + " ");
            }
            System.out.println();
            return rightS;

        }
    }

    private String[] getNoTerminal() {
        String[] content = file.getContent();
        noTerm = new NodeListStack<>();

        for (String s : content) {
            String currentLine = s;
            if (!currentLine.equals("")) {
                currentLine = leftSide(currentLine);
                if (currentLine != null) {
                    if (!noTerm.exists(currentLine)) {
                        noTerm.addLast(currentLine);
                    }
                }
            }
        }
        content = new String[noTerm.size()];
        for (int i = 0; i < content.length; i++) {
            content[i] = noTerm.getElement(i);
            System.out.println(content[i]);
        }

        return content;
    }

    private String[][] getProds() {
        String[] content = file.getContent();
        NodeListStack<String[]> nodeListStack = new NodeListStack<>();
        for (String currentLine : content) {
            if (!currentLine.equals("")) {
                String[] production = rightSide(currentLine);
                if (!nodeListStack.exists(production)) {
                    nodeListStack.addLast(production);
                }
            }
        }
        String[][] contents = new String[nodeListStack.size()][0];

        for (int i = 0; i < content.length; i++) {
            contents[i] = nodeListStack.getElement(i);
        }
        return contents;
    }

    private String[] getTerminal() {
        NodeListStack<String> terms = new NodeListStack<>();
        for (String[] production : prods) {
            for (String s : production) {
                if (!s.equals("")
                        && !terms.exists(s)
                        && !noTerm.exists(s)) {
                    terms.addLast(s);
                }
            }
        }

        String[] terminals = new String[terms.size()];
        for (int i = 0; i < terminals.length; i++) {
            terminals[i] = terms.getElement(i);
            System.out.println(terminals[i]);
        }
        return terminals;
    }

    private String prepareInitialSymbols() {
        NodeListStack<String> production = new NodeListStack<>();

        for (String[] strings : this.prods) {
            for (String string : strings) {
                production.addFirst(string);
            }
        }

        for (String s : this.noTerminal) {
            if (!production.exists(s)) {
                return s;
            }
        }

        return "";

    }

    String[] symbolsTerminal() {
        return terminal;
    }

    String[] symbolsNoTerminal() {
        return noTerminal;
    }

    String[][] productions() {
        return prods;
    }

    String initialSymbol() {
        return initSymbols;
    }

}
