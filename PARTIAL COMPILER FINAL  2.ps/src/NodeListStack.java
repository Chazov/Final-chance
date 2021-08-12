public class NodeListStack<T> {
    private Node<T> first, last;
    private int size;

    public int size() {
        return size;
    }

    public void addFirst(T element) {
        Node<T> n = new Node<T>(element);
        if (first == null) {
            last = n;
        } else {
            n.next = first;
        }
        first = n;
        size++;
    }

    //Add an element to the end of the list
    void addLast(T element) {
        Node<T> n = new Node<T>(element);
        if (first == null) {
            first = n;
        } else {
            last.next = n;
        }
        last = n;
        size++;
    }

    // Get first element
    public T peek() {
        if (first == null) {
            return null;
        }
        return first.info;
    }

    // Get last element
    public T getLast() {
        if (first == null) {
            return null;
        }
        return last.info;
    }

    // Get element at index
    public T getElement(int index) {
        if (index >= size || index < 0) {
            return null;
        }
        Node<T> itd = first;
        for (int i = 0; i < index; i++) {
            itd = itd.next;
        }
        return itd.info;
    }

    // Remove first element
    public void pop() {
        if (first != null) {
            Node<T> remove = first;
            first = first.next;
            if (remove.equals(last)) {
                last = null;
            }
            size--;
        }
    }

    // remove last element
    public void removeLast() {
        if (first != null) {
            if (last.equals(first)) {
                first = null;
                last = null;
            } else {
                Node<T> itd = first;
                while (itd.next.next != null) {
                    itd = itd.next;
                }
                last = itd;
                itd.next = null;
            }
            size--;
        }
    }

    // Remove element
    public void remove(T element) {
        if (first == null) ;
        else if (first.info.equals(element)) {
            pop();
        } else if (last.info.equals(element)) {
            removeLast();
        } else {
            Node<T> itd = first;
            while (itd.next != null) {
                T element_actual = itd.next.info;
                if (element_actual.equals(element)) {
                    Node<T> remove = itd.next;
                    itd.next = remove.next;
                    size--;
                    break;
                }
                itd = itd.next;
            }
        }
    }

    public boolean empty() {
        return size == 0;
    }

    public boolean exists(T element) {
        boolean found = false;
        Node<T> itd = first;

        while (itd != null) {
            T element_actual = itd.info;
            if (element_actual.equals(element)) {
                found = true;
                break;
            }
            itd = itd.next;
        }
        return found;
    }

    private class Node<T> {
        T info;
        Node<T> next;

        Node(T info) {
            this.info = info;
        }
    }
}