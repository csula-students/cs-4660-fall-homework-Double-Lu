package csula.cs4660.games;
import csula.cs4660.games.models.MiniMaxState;
import csula.cs4660.graphs.Graph;
import csula.cs4660.graphs.Node;
import java.util.ListIterator;

public class MiniMax {
    public static Node getBestMove(Graph graph, Node root, Integer depth, Boolean max) {
        int min = Integer.MIN_VALUE;
        int max = Integer.MAX_VALUE;

        if( depth == 0 )
            return root;

        if( max ){
            Node best = new Node<>(new MiniMaxState(min, min));

            for(Node<MiniMaxState> neighbor: graph.neighbors(root)){
                int deft = depth -1;
                Node<MiniMaxState> temp = getBestMove(graph, neighbor, deft, false);

                if(temp.getData().getValue()>((MiniMaxState)best.getData()).getValue()){
                    ((Node<MiniMaxState>)graph.getNode(root).get()).getData().setValue((temp.getData()).getValue());
                    best = temp;
                }
            }
            return best;
        }

        else{
            Node best = new Node<>(new MiniMaxState(max, max));
            for(Node<MiniMaxState> neighbor: graph.neighbors(root)){
                int deft = depth -1;
                Node<MiniMaxState> temp = getBestMove(graph, neighbor, deft, true);

                if(temp.getData().getValue()<((MiniMaxState)best.getData()).getValue()){
                    ((Node<MiniMaxState>)graph.getNode(root).get()).getData().setValue(((MiniMaxState)temp.getData()).getValue());
                    best = temp;
                }
            }
            return best;
        }
    }
}
