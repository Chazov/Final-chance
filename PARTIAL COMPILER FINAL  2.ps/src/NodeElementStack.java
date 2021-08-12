public class NodeElementStack<T> {
    private NodeElement<T> head;

    public boolean isEmpty() {
        return head == null;
    }

    public void push(T element) {
        NodeElement<T> nodeElement = new NodeElement<T>(element);
        nodeElement.next = head;
        head = nodeElement;
    }

    public T pop() {
        if (head == null) {
            return null;
        }
        NodeElement<T> nodeElement = head;
        T element = nodeElement.getElement();

        head = head.next;
        nodeElement = null;
        return element;
    }

    public T top() {
        if (head == null) {
            return null;
        }
        return head.getElement();
    }
}
