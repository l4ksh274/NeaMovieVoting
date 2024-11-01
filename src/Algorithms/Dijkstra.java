package Algorithms;

import DataStructures.AdjacencyList.AdjacencyList;
import DataStructures.HashMap.Hashmap;
import DataStructures.Nodes.Pairs;
import DataStructures.PriorityQueue.Queue;

public class Dijkstra<T>{
    private final Queue<T> queue = new Queue<>();

    public Hashmap<T, Float> search(T startKey, int numberOfMovies, AdjacencyList<T> graph){ // Should Be valid

        Hashmap<T, Float> visited = new Hashmap<>(numberOfMovies + 1);

        queue.add(new Pairs<>(startKey, 0f));

        while (visited.length() < numberOfMovies + 1){
            if (queue.isEmpty()){
                return visited;
            }
            Pairs<T, Float> current = queue.pop();
            visited.add(current);

            for (Pairs<T, Float> neighbour : graph.item(current.getKey())){
                if (!queue.contain(neighbour) && !visited.contains(neighbour.getKey())){
                    neighbour.setValue(neighbour.getValue() + current.getValue());
                    queue.add(neighbour);
                }
            }
        }

        return visited;

    }
}
