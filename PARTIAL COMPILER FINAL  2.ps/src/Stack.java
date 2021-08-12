public class Stack<T> {
    private NodeElement<T> top;

    public boolean isEmpty() {
        if (top == null) {
            return true;
        }
        return false;
    }

    public void push(T element) {
        NodeElement<T> nodo = new NodeElement<T>(element);
        nodo.next = top;
        top = nodo;
    }

    public T pop() {
        if (top == null) {
            return null;
        }
        NodeElement<T> nodo = top;
        T element = nodo.getElement();

        top = top.next;
        nodo = null;
        return element;
    }

    public T top() {
        if (top == null) {
            return null;
        }
        return top.getElement();
    }
}