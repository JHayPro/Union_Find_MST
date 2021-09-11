//Jude Hayes
//Project 2
//8/7/2021

import java.util.ArrayList;
import java.util.Comparator;

public class Edge {//Object to store edges

    int currentNode, connectedNode, weight;//stores current node, the node it is connected to and the weight of the edge

    Edge(int currentNodeT, int connectedNodeT, int weightT) {//constructor for new Edge
        currentNode = currentNodeT;
        connectedNode = connectedNodeT;
        weight = weightT;
    }
}