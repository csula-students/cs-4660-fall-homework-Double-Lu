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

        String parentState = "10a5461773e8fd60940a56d2e9ef7bf4";
        String finalState = "e577aa79473673f6158cc73e0e5dc122";

        Graph graph = new Graph(Representation.of(Representation.STRATEGY.ADJACENCY_LIST));
        Queue<String> nodes = new LinkedList<String>();
        Set<String> exploredSet = new HashSet<String>();
        nodes.add(parentState);
        int count  = 0;
        while (!nodes.isEmpty()) {
            parentState = nodes.remove();
            if (exploredSet.contains(parentState))
                continue;
            Node p = new Node(parentState);
            if (!graph.getNodes().contains(p))
                graph.addNode(p);
            exploredSet.add(parentState);
            count++;
            State parent = Client.getState(parentState).get();

            for (State child: parent.getNeighbors()) {
                String childState = child.getId();
                if (!exploredSet.contains(childState))
                    nodes.add(childState);
                Node child = new Node(childState);
                if (!graph.getNodes().contains(child))
                    graph.addNode(child);
                int cost = Client.stateTransition(parentState, childState).get().getEvent().getEffect();
                Edge edge = new Edge(p, child, cost);
                graph.addEdge(edge);
            }
        }
        Node start = new Node(parentState);
        Node end = new Node(finalState);
        List<Edge> path = graph.search(new BFS(), start, end);
        System.out.println("BFS Path: ");
        int cost = 0;
        for(int index = 0; index < path.size(); index++) {
            String fromState = (String)path.get(index).getFrom().getData();
            String toState = (String)path.get(index).getTo().getData();
            String from = Client.getState(fromState).get().getLocation().getName();
            String to = Client.getState(toState).get().getLocation().getName();
            cost = path.get(index).getValue();
            System.out.println(from + " to " +  to + ": " + cost + "\nfrom(" + fromState + ") to(" + toState + ")");
        }
        System.out.println();
        path = graph.search(new DijkstraSearch(), start, end);
        System.out.println("Dijkstra Path: ");
        int cost = 0;
        for(int index = 0; index < path.size(); index++) {
            String fromState = (String)path.get(index).getFrom().getData();
            String toState = (String)path.get(index).getTo().getData();
            String from = Client.getState(fromState).get().getLocation().getName();
            String to = Client.getState(toState).get().getLocation().getName();
            cost = path.get(index).getValue();
            System.out.println(from + " to " +  to + ": " + cost + "\nfrom(" + fromState + ") to(" + toState + ")");
        }
        System.out.println();
    }
}