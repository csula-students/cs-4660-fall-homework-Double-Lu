package csula.cs4660.graphs.searches;

import com.google.common.collect.Lists;
import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.Graph;
import csula.cs4660.graphs.Node;

import java.util.*;

/**
 * Breadth first search
 */
public class BFS implements SearchStrategy {

    @Override
    public List<Edge> search(Graph graph, Node source, Node dist) {

        //Get graph nodes credentials
        List<Edge> path = new ArrayList<Edge>();
        Collection<Node> nodeCollection = graph.getNodes();
        Queue<Node> nodes = new LinkedList<Node>();

        //empty
        for(Node n: nodeCollection) {
            n.g = Integer.MAX_VALUE;
            n.h = 0;
            n.parent = null;
        }

        //Finds starting node
        if (nodeCollection.contains(source)) {
            for(Node n: nodeCollection) {
                if (n.equals(source)) {
                    n.g = 0;
                    n.parent = null;
                    nodes.add(source);
                    break;
                }
            }
        }

        Node parent = null;
        Node endNode = null;

        //making path
        while(!nodes.isEmpty()) {
            parent = nodes.remove();
            for(Node child: graph.neighbors(parent)) {
                nodes.add(child);
                if (child.g == Integer.MAX_VALUE) {
                    child.g = graph.distance(parent, child);
                    child.parent = parent;
                }
                if (child.equals(dist)) {
                    endNode = child;
                }
            }
        }

        while (node.parent != null) {
            path.add(new Edge(node.parent, node, graph.distance(node.parent, node)));
            node = node.parent;
        }

        return Lists.reverse(path);
    }
}


