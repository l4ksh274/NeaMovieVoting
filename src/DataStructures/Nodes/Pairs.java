package DataStructures.Nodes;

import java.util.Objects;

public class Pairs <K, V>{
    private V value;
    private K key;

    public Pairs(K key, V value){
        this.key = key;
        this.value = value;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "[" + key.toString() + " : " + value.toString() + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pairs<?, ?> pairs = (Pairs<?, ?>) o;
        return Objects.equals(key, pairs.key);
    }
}
