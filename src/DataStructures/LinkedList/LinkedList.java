package DataStructures.LinkedList;

import DataStructures.Nodes.Node;

import java.util.Iterator;
import java.util.NoSuchElementException;


public class LinkedList<T> implements Iterable<Node<T>>{ // TODO Check Compatibility without pairs

    private Node<T> head, tail;
    private int size = 0;

    public void append(T value){
        Node<T> n = new Node<>(value);
        size++;

        if (head == null){
            head = n;
            tail = n;
            return;
        }

        n.setPrevious(tail);
        tail.setNext(n);
        tail = n;

    }

    public void insert(T value, int index){
        Node<T> n = new Node<>(value);
        if (index == size){
            append(value);
            return;
        }

        size++;

        if (index == 0){
            if (head == null){
                head = n;
                tail = n;
                return;
            }
            n.setNext(head);
            head.setPrevious(n);
            head = n;
            return;
        }

        Node<T> temp = head;

        for(int i = 0; i < index; i++){
            temp = temp.getNext();
        }

        n.setPrevious(temp.getPrevious());
        n.setNext(temp);

        temp.getPrevious().setNext(n);
        temp.setPrevious(n);
    }

    public Node<T> remove(int index){
        if (index == 0) {
            Node<T> removedNode = head;
            head = head.getNext();
            if (head != null) {
                head.setPrevious(null);
            }
            size--;
            return removedNode;
        }

        if (index == size - 1) {
            Node<T> removedNode = tail;
            tail = tail.getPrevious();
            if (tail != null) {
                tail.setNext(null);
            }
            size--;
            return removedNode;
        }

        Node<T> temp = get(index);
        temp.getNext().setPrevious(temp.getPrevious());
        temp.getPrevious().setNext(temp.getNext());
        size--;
        return temp;
    }

    public void remove(T p){
        Node<T> temp = head;

        for (int i = 0; i < size; i++){
            if (temp.getData().equals(p)){
                remove(i);
                return;
            }
            temp = temp.getNext();
        }

        throw new IllegalArgumentException("Pair not found");

    }

    public Node<T> peek(){
        return head;
    }

    public int getSize(){
        return size;
    }

    public boolean isEmpty(){
        return size == 0;
    }

    public Node<T>[] asArray(){
        Node<T> temp = head;
        Node<T>[] arr = new Node[size];

        for (int i = 0; i < size; i++){
            arr[i] = temp;
            temp = temp.getNext();
        }
        return arr;
    }

    public boolean contain(T p){
        Node<T> temp = head;
        for (int i = 0; i < size; i++){
            if (temp.getData().equals(p)){
                return true;
            }
            temp = temp.getNext();
        }

        return false;
    }

    public Node<T> get(Node<T> p){
        Node<T> temp = head;

        for (int i = 0; i < size; i++){
            if (temp.equals(p)){
                return temp;
            }
            temp = temp.getNext();
        }

        throw new IllegalArgumentException("Element not found");

    }

    public Node<T> get (int index){
        if (index > size - 1){
            throw new IllegalArgumentException("Invalid Index");
        }

        Node<T> temp = head;

        for (int i = 0; i < index; i++){
            temp = temp.getNext();
        }

        return temp;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (Node<T> current : this) {
            sb.append(current.toString());
            if (iterator().hasNext()) {
                sb.append(", ");
            }
        }
        if (sb.length() > 1) // Removes unnesscary space and comma.
            sb.delete(sb.length() - 2, sb.length());
        sb.append("]");
        return sb.toString();
    }

    @Override
    public Iterator<Node<T>> iterator() {
        return new LinkedListIterator();
    }

    class LinkedListIterator implements Iterator<Node<T>>{

        private Node<T> current = head;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Node<T> next() {
            if (!hasNext()){
                throw new NoSuchElementException("No remaining elements");
            }
            Node<T> temp = current;
            current = current.getNext();
            return temp;
        }
    }

}
