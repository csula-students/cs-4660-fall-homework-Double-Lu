package csula.cs4660.quizes;

import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.Graph;
import csula.cs4660.graphs.Node;
import csula.cs4660.graphs.representations.Representation;
import csula.cs4660.graphs.searches.BFS;
import csula.cs4660.graphs.searches.DijkstraSearch;
import csula.cs4660.quizes.models.State;

import java.util.*;

/**
 * Here is your quiz entry point and your app
 */
public class App {
    public static void main(String[] args) {
        // to get a state, you can simply call `Client.getState with the id`
        State initialState = Client.getState("10a5461773e8fd60940a56d2e9ef7bf4").get();
        State finalState = Client.getState("e577aa79473673f6158cc73e0e5dc122").get();
        Graph graph = new Graph(Representation.of(Representation.STRATEGY.ADJACENCY_LIST));
        Queue<Node> nodes = new LinkedList<Node>();
        Set<String> exploredSet = new HashSet<String>();
        Node parentState = new Node(initialState);
        graph.addNode(parentState);
        nodes.add(parentState);
        int nodeCount = 0;
        while (!nodes.isEmpty()) {
            parentState = nodes.poll();
            State parent = (State) parentState.getData();
            if (exploredSet.contains(parent.getId()))
                continue;
            exploredSet.add(parent.getId());
            nodeCount++;
            for (State childState : parent.getNeighbors()) {
                if (exploredSet.contains(childState.getId()))
                    continue;
                Node child = new Node(Client.getState(childState.getId()).get());
                graph.addNode(child);
                nodes.add(child);
                int cost = -1 * Client.stateTransition(initialState.getId(), childState.getId()).get().getEvent().getEffect();
                cost += 100;
                Edge edge = new Edge(parentState, child, cost);
                graph.addEdge(edge);
            }
            if (nodeCount % 50 == 0)
                System.out.println(nodeCount);
        }
        System.out.println(nodeCount);
        Node start = new Node(initialState);
        Node end = new Node(finalState);
        List<Edge> path = graph.search(new BFS(), start, end);
        System.out.println("BFS Path: ");
        int cost = 0;
        for (int index = 0; index < path.size(); index++) {
            State from = (State) path.get(index).getFrom().getData();
            State to = (State) path.get(index).getTo().getData();
            cost = path.get(index).getValue() - 100;
            cost *= -1;
            System.out.println(from.getLocation().getName() + " : " + to.getLocation().getName() + " : " + cost);
        }
        path = graph.search(new DijkstraSearch(), start, end);
        System.out.println("\nDijkstra Path: ");
        int cost = 0;
        for (int index = 0; index < path.size(); index++) {
            State from = (State) path.get(index).getFrom().getData();
            State to = (State) path.get(index).getTo().getData();
            cost = path.get(index).getValue() - 100;
            cost *= -1;
            System.out.println(from.getLocation().getName() + " : " + to.getLocation().getName() + " : " + cost);
        }
        System.out.println();
    }
}