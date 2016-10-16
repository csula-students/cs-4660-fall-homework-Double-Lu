package csula.cs4660.graphs.searches;

import com.google.common.collect.Lists;
import csula.cs4660.games.models.Tile;
import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.Graph;
import csula.cs4660.graphs.Node;
import csula.cs4660.graphs.utils.NodeComparator;

import java.util.*;

/**
 * Perform A* search
 */
public class AstarSearch implements SearchStrategy {

    // D is a scale value for you to adjust performance vs accuracy
    private final double D = 1.5;

    @Override
    public List<Edge> search(Graph graph, Node source, Node dist) {

        Queue<Node> pathing = new PriorityQueue<>(new NodeComparator());
        Set<Node> exploredSet = new HashSet<Node>();
        Node srcNode = null;

        Collection<Node> nodeCollection = graph.getNodes();

        // initialize g, h, parent for all nodes
        for (Node node : nodeCollection) {

            node.parent = null;
            node.g = Double.POSITIVE_INFINITY;
            node.h = heuristic(node, dist);

            // set source g value
            if (node.equals(source)) {
                srcNode = node;
                node.g = 0;
            }
        }

        // add source to frontier
        pathing.add(srcNode);

        Node parent = null;
        double tempGScore;

        while (!pathing.isEmpty()) {

            // remove node from frontier and add to explored set
            parent = pathing.remove();
            exploredSet.add(parent);

            // return path found
            if (parent.equals(dist)) {
                List<Edge> path = new ArrayList<Edge>();
                while (endNode.parent != null) {
                    path.add(new Edge(endNode.parent, endNode, graph.distance(endNode.parent, endNode)));
                    endNode = endNode.parent;
                }
                return Lists.reverse(path);
            }

            for (Node child : graph.neighbors(parent)) {

                // skips previously explored paths
                if (exploredSet.contains(child)) {
                    continue;
                }

                Tile childTile = (Tile) child.getData();
                String data = childTile.getType();

                // if current child is a wall adds to explored set
                if (data.equals("##")) {
                    exploredSet.add(child);
                    continue;
                }

                tempGScore = parent.g + graph.distance(parent, child);

                if (tempGScore >= child.g) {
                    continue;// skip because we are at the worse path
                } else if (!pathing.contains(child)) {
                    child.parent = parent;
                    child.g = tempGScore;
                    pathing.add(child);
                }
            }
        }
        return null;
    }

    // uses manhatten distance
    private double heuristic(Node source, Node goal) {

        Tile tileS = (Tile) source.getData();
        Tile tileG = (Tile) goal.getData();

        int dx = Math.abs(tileS.getX() - tileG.getX());
        int dy = Math.abs(tileS.getY() - tileG.getY());

        return D * (dx + dy);
    }
}