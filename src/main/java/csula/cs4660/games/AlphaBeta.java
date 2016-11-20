package csula.cs4660.games;
import csula.cs4660.games.models.MiniMaxState;
import csula.cs4660.graphs.Graph;
import csula.cs4660.graphs.Node;

public class AlphaBeta {
    public static Node getBestMove(Graph graph, Node source, Integer depth, Integer alpha, Integer beta, Boolean max) {
        int min = Integer.MIN_VALUE;
        int max = Integer.MAX_VALUE;

        if( depth ==0 )
            return source;

        if( max ){
            Node<MiniMaxState> best = new Node<>(new MiniMaxState(min, min));

            for(Node<MiniMaxState> neighbor: graph.neighbors(source)){
                int deft = depth -1;
                Node<MiniMaxState> temp = getBestMove(graph, neighbor, deft, alpha, beta, false);

                if(temp.getData().getValue()>((MiniMaxState)best.getData()).getValue())
                    best = temp;

                ((Node<MiniMaxState>)graph.getNode(root).get()).getData().setValue((temp.getData()).getValue());

                if(temp.getData().getValue() >= beta)
                    return temp;

                if(temp.getData().getValue() >= alpha)
                    alpha = temp.getData().getValue();
            }
            return best;
        }

        else{
            Node<MiniMaxState> best = new Node<>(new MiniMaxState(max, max));

            for(Node<MiniMaxState> neighbor: graph.neighbors(source)){
                int deft = depth -1;
                Node<MiniMaxState> temp = getBestMove(graph, neighbor, deft, alpha, beta, true);

                if(temp.getData().getValue() < ((MiniMaxState)best.getData()).getValue())
                    best = temp;

                ((Node<MiniMaxState>)graph.getNode(root).get()).getData().setValue(((MiniMaxState)temp.getData()).getValue());
                if(temp.getData().getValue() <= alpha)
                    return temp;

                if(temp.getData().getValue() <= beta)
                    beta = temp.getData().getValue();
            }

            return best;
        }

    }
}