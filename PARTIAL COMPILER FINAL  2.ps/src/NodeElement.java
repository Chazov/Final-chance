public class NodeElement<T> {
    NodeElement<T> next;
    private T element;

    public NodeElement(T element) {
        this.element = element;
    }

    public T getElement() {
        return element;
    }

    public void setElement(T element) {
        this.element = element;
    }
}
