package DataStructures.PriorityQueue;

import DataStructures.LinkedList.LinkedList;
import DataStructures.Nodes.Node;
import DataStructures.Nodes.Pairs;

public class Queue<K> extends LinkedList<Pairs<K, Float>>{ // TODO Check Implementation

    public void add(Pairs<K, Float> node){
        Node<Pairs<K, Float>>[] queueArr = asArray();

        for(int i = 0; i < getSize(); i++){
            if (node.getValue() < queueArr[i].getData().getValue()){
                insert(node, i);
                return;
            }
        }
        append(node);

    }

    public Pairs<K, Float> pop(){
        return remove(0).getData();
    }

}
