//Jude Hayes
//Project 2
//8/7/2021

import java.util.Comparator;

public class GraphWeightCompare implements Comparator<Edge> {

    @Override
    public int compare(Edge firInst, Edge secInst) {//Finds which edge has a greater weight in order to sort the graph
        if (firInst.weight > secInst.weight) return 1;
        else if (firInst.weight < secInst.weight) return -1;
        else return 0;
    }
}

