package csula.cs4660.graphs.searches;

import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.Graph;
import csula.cs4660.graphs.Node;
import java.util.ArrayList;
import java.util.List;

/**
 * Depth first search
 */
public class DFS implements SearchStrategy {

    List<Edge> temp;

    @Override
    public List<Edge> search(Graph graph, Node source, Node dist) {

        temp = null;
        List<Edge> path = null;

        recruseSearch(graph, source, dist, new ArrayList<Node>());

        if (temp != null) {
            path = new ArrayList<Edge>();
            for(Edge e: temp) path.add(e);
            temp = null;
        }

        return path;
    }

    public void recurseSearch(Graph graph, Node source, Node target , List<Node> route) {

        route.add(source);

        if (source.equals(target) && temp == null) {

            temp = new ArrayList<Edge>();

            for(int index = 0; index < route.size() - 1; index++) {

                int distance = graph.distance(route.get(index), route.get(index + 1));
                Edge edge = new Edge(route.get(index), route.get(index + 1), distance);
                temp.add(edge);

            }
        }

        for(Node child : graph.neighbors(source)) {
            recurseSearch(graph, child, target, route);
            route.remove(child);
        }
    }
}