package DataStructures.HashMap;

import DataStructures.LinkedList.LinkedList;
import DataStructures.Nodes.Node;
import DataStructures.Nodes.Pairs;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Hashmap <K, V> implements Iterable<Pairs<K, V>>{ // TODO Completed test working

    private final LinkedList<Pairs<K, V>>[] arr; // Stores unneeded data optimise it.
    private final int capacity;
    private int length;

    public Hashmap(int initialCapacity){
        this.capacity = initialCapacity;
        arr = new LinkedList[initialCapacity];
        length = 0;

        for(int i = 0; i < initialCapacity; i++){ // Init the hashmap
            arr[i] = new LinkedList<>();
        }

    }


    public void add(K key, V value){
        add(new Pairs<K, V>(key, value));
    }

    public void add(Pairs<K,V> pair) {
        if (contains(pair.getKey())){
            throw new IllegalArgumentException("Key already exists");
        }
        arr[getHash(pair.getKey())].append(pair);
        length++;
    }

    public Pairs<K, V> itemPairs(K Key) {
        LinkedList<Pairs<K, V>> sublist = arr[getHash(Key)];
        return sublist.get(getListIndex(Key)).getData();
    }

    public boolean contains(K key){
        try{
            item(key);
            return true;
        } catch (NoSuchElementException e){
            return false;
        }
    }

    public V item(K Key) {
        return itemPairs(Key).getValue();
    }

    public void delete(K key){
        LinkedList<Pairs<K, V>> sublist = arr[getHash(key)];
        sublist.remove(getListIndex(key));

        length--;
    }


    private int getHash(K key){
        return key.hashCode() % capacity;
    }
    private int getListIndex(K key) {
        LinkedList<Pairs<K, V>> sublist = arr[getHash(key)];
        int index = 0;
        for (Node<Pairs<K, V>>element : sublist){
            if (element.getData().getKey().equals(key)){
                return index;
            }
            index++;
        }
        throw new NoSuchElementException("The element is not in the hashmap");
    }

    public boolean isEmpty() {
        return length == 0;
    }
    public int length() {
        return length;
    }

    protected LinkedList<Pairs<K, V>>[] getArr(){
        return arr;
    }

    @Override
    public String toString() { // Check working
        StringBuilder sb = new StringBuilder();
        int count = 0;
        sb.append("{");

        for (LinkedList<Pairs<K, V>> sublist: arr){
            for (Node<Pairs<K, V>> pair : sublist){
                sb.append(pair.toString()).append(count != length - 1 ? ", " : "");
                count++;
            }
        }

        sb.append("}");
        return sb.toString();
    }

    @Override
    public Iterator<Pairs<K, V>> iterator() {
        return new HashMapIterator();
    }

    class HashMapIterator implements Iterator<Pairs<K, V>>{
        private int currentRow = 0;
        private Iterator<Node<Pairs<K, V>>> sublistIterator = arr[currentRow].iterator();

        @Override
        public boolean hasNext() {
            if (sublistIterator.hasNext()){ // Adds the next item in the current List
                return true;
            }

            while (currentRow < capacity - 1 && !arr[currentRow + 1].iterator().hasNext()){ // Moves to the next populated list if the current list is exhausted.
                currentRow++;
            }

            return currentRow < capacity - 1 && arr[currentRow + 1].iterator().hasNext(); // Have all the rows or less been looked at and does the currentRow still have available values

        }

        @Override
        public Pairs<K, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No remaining elements");
            }

            if (sublistIterator.hasNext()){
                return sublistIterator.next().getData();
            }

            currentRow++;
            sublistIterator = arr[currentRow].iterator();

            return sublistIterator.next().getData();

        }
    }

}
