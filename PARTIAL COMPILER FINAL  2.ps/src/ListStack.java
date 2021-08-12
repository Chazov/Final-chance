public class ListStack {
    String type;
    Node head, tail;

    public ListStack() {
        this.type = null;
        this.head = null;
        this.tail = null;
    }

    public ListStack(String type) {
        head = null;
        tail = null;
        this.type = type;
    }

    public int size() {
        Node node = head;
        int size = 0;
        while (node != null) {
            size++;
            node = node.getNode();
        }
        return size;
    }

    public void saveUp(Token token) {
        Node node = new Node(token);
        if (head == null) {
            head = node;
            tail = node;
        } else {
            tail.setNode(node);
            tail = node;
        }
    }

    public void show() {
        Node node = head;
        System.out.println(this.type);
        while (node != null) {
            String s = node.toString();
            System.out.println(s);
            node = node.getNode();
        }

    }

    public Token getTokenbyId(int id) {
        Node node = head;
        while (node != null) {
            if (node.getToken().id == id) {
                return node.getToken();
            }
            node = node.getNode();
        }
        return null;

    }

    public Token getTokenbyLex(String lex) {
        Node node = head;
        while (node != null) {
            if (node.getToken().lex.equals(lex)) {
                return node.getToken();
            }
            node = node.getNode();
        }
        return null;

    }

    public boolean exists(Token itemId) {
        Node node = head;
        while (node != null) {
            if (node.getToken().id == itemId.id) {
                return true;
            }
            node = node.getNode();
        }
        return false;
    }

    public boolean existsLex(String lex) {
        Node node = head;
        while (node != null) {
            if (node.getToken().lex.equals(lex)) {
                return true;
            }
            node = node.getNode();
        }
        return false;
    }

}
