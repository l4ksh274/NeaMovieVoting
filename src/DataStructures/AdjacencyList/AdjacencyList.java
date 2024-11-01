package DataStructures.AdjacencyList;

import DataStructures.HashMap.Hashmap;
import DataStructures.Nodes.Pairs;

public class AdjacencyList<K> extends Hashmap<K, Hashmap<K, Float>> {

    public AdjacencyList(int initialSize){
        super(initialSize);
    }

    public AdjacencyList(){
        super(5);
    }

    public void addNode(K Node, int connections){
        add(Node, new Hashmap<>(connections));
    }

    public void addLink(K startNode, Pairs<K, Float> endNode){
        if (!contains(startNode) || !contains(endNode.getKey())){
            throw new IllegalArgumentException("A Node Doesn't exist");
        }
        item(startNode).add(endNode);
    }

    @Override
    public String toString() {
        int count = 0;
        StringBuilder str = new StringBuilder("Graph = {\n");
        for (Pairs<K, Hashmap<K, Float>> node : this){
            str.append(node.getKey()).append(node.getValue().toString()).append(count != super.length() - 1 ? ",\n" : "");
            count++;
        }

        str.append("\n}");
        return str.toString();
    }
}
