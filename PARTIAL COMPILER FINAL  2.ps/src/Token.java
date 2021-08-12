public class Token {
    int id;
    int rep;
    String token;
    String type;
    String lex;
    String line;

    public Token(int id, String token, String type, String lex, String line) {
        this.id = id;
        this.token = token;
        this.type = type;
        this.lex = lex;
        this.line = line;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRep() {
        return rep;
    }

    public void setRep(int rep) {
        this.rep = rep;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLex() {
        return lex;
    }

    public void setLex(String lex) {
        this.lex = lex;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    @Override
    public String toString() {
        return "::" +
                "id:" + id + ", rep:" + rep + ", token:" + token + ", type:" + type + ", lex:" + lex + ", line:" + line + "::";
    }
}
