public class Node {
    Token token;
    Node node;

    public Node(Token token) {
        this.token = token;
        node = null;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    @Override
    public String toString() {
        return "Node::" +
                "token:" + token.toString() +
                "::";
    }
}
