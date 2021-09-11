//Jude Hayes
//Project 2
//8/7/2021

import java.util.*;
import java.io.*;

public class Project2 {

    public static void main(String[] args) {

        Project2 p2 = new Project2();//Creates instance of class and runs constructor
        p2.buildGraph();//Constructs the graph based on the input file
        p2.kruskals();//runs kruskal's algorithm
    }

    ArrayList<Node> UnionFind = new ArrayList<Node>();//Contains all children and a parent node indexed by node
    ArrayList<Edge> Graph = new ArrayList<Edge>();//Graph of all the edges indexed by weight
    int totalWeight, nodeCount, //weight of the MST and count of all nodes
            currentNode, connectedNode, currentNodeParent, connectedNodeParent,//Temp values to store nodes
            currentNodeUnionSize, connectedNodeUnionSize;//Temp values to store node sizes

    Node currentNodeC, connectedNodeC, currentNodeParentC, connectedNodeParentC; //Temp objects to store UnionFind Objects

    Scanner scanner = null;//creates instance of Scanner

    Project2() {//constructor
        totalWeight = currentNode = connectedNode = currentNodeParent = connectedNodeParent = 0;

        try {//attempts to open input file, if it fails, exit the program
            scanner = new Scanner(new File("input.txt"));
        } catch (FileNotFoundException e) {
            System.exit(1);
        }

        nodeCount = scanner.nextInt();//reads in the amount of nodes in the aug matrix
        for (int i = 0; i < nodeCount + 1; i++)//resizes unionFindA to contain all nodes in the graph
            UnionFind.add(new Node(i));
    }

    void buildGraph() {//Constructs the graph based on the input file
        int weight = 0;//Temp variable to store weight input from scanner
        for (int currentNode = 1; currentNode < nodeCount + 1; currentNode++) {//Loops through all values in the aug matrix
            scanner.nextLine();//skips to next line of text input
            for (int connectingNode = 1; connectingNode < currentNode; connectingNode++) {//takes in values from hte input file in a lower triangular pattern

                weight = scanner.nextInt();//reads in weight to the connected node

                if (weight > 0)
                    Graph.add(new Edge(connectingNode, currentNode, weight));//Constructs new Edge in graphA

            }
        }
        Collections.sort(Graph, new GraphWeightCompare());//Sorts graphA based on weight
    }

    void kruskals() {//runs kruskal's algorithm to find the MST
        for (int i = 0; i < Graph.size(); i++) {
            updateNodes(i);//Updates temp variables used to store nodes
            if (currentNodeParent != connectedNodeParent) {//Skips values with nodes which share the same union since they will form a cycle
                updateMSTValues(i);//Updates temp values used to build the MST
                if (currentNodeUnionSize == 0 && connectedNodeUnionSize == 0)
                    createNewUnion();//If both nodes parents have no children, then a new union is made between the two nodes
                else if (currentNodeUnionSize == 0)
                    addCurrentNodeToUnion();//If the current node is not in a union, but the connected node does, add it to the connected node's union
                else if (connectedNodeUnionSize == 0)
                    addConnectedNodeToUnion();//If the connected node is not in a union, but the current node does, add it to the current node's union
                else if (currentNodeUnionSize > connectedNodeUnionSize)
                    addConnectedUnionToCurrentUnion();//If the current node's union is bigger than the connected node's union, merge the smaller union into the larger union
                else
                    addCurrentUnionToConnectedUnion();//If the connected node's union is bigger than or the same as the current node's union, merge the smaller union into the larger union
            }
        }
        System.out.println(totalWeight);//Display total weight of MST
    }

    void updateNodes(int index) {//Updates temp variables used to store nodes
        currentNode = Graph.get(index).currentNode;//Assigns currentNode number
        connectedNode = Graph.get(index).connectedNode;//Assigns connected node number
        currentNodeParent = UnionFind.get(currentNode).getParent();//Assigns current node's parent
        connectedNodeParent = UnionFind.get(connectedNode).getParent();//Assigns connected node's parent
    }

    void updateMSTValues(int index) {//Updates temp values used to build the MST
        currentNodeC = UnionFind.get(currentNode);//Assigns current node's class
        connectedNodeC = UnionFind.get(connectedNode);//Assigns connected node's class
        currentNodeParentC = UnionFind.get(currentNodeParent);//Assigns the parent class of the current node
        connectedNodeParentC = UnionFind.get(connectedNodeParent);//Assigns the parent class of the connected node

        currentNodeUnionSize = currentNodeParentC.getUnionSize();//Assigns the size of the current node's union
        connectedNodeUnionSize = connectedNodeParentC.getUnionSize();//Assigns the size of the connected node's union
        int weight = Graph.get(index).weight;//Assigns the weight of the current edge

        totalWeight += weight;//Adds current edge to the total weight of the MST
        System.out.println(currentNode + " " + connectedNode + " " + weight);//prints values of the current MST edge
    }

    void createNewUnion() {//Creates a new union from two nodes with no union
        currentNodeC.addChild(connectedNode);//Adds connected node to current node's children
        connectedNodeC.setParent(currentNode);//sets connected node's parent to current node
    }

    void addCurrentNodeToUnion() {//Adds the current node to the connected node's union
        currentNodeC.setParent(connectedNodeParent);//Sets the current node's parent to the connected node's parent
        connectedNodeParentC.addChild(currentNode);//Adds the current node to the connected node's union
    }

    void addConnectedNodeToUnion() {//Adds the connected node to the current node's union
        connectedNodeC.setParent(currentNodeParent);//Sets the connected node's parent to the current node's parent
        currentNodeParentC.addChild(connectedNode);//Adds the connected node to the current node's union
    }

    void addConnectedUnionToCurrentUnion() {//Add the connected union to the current union
        currentNodeParentC.addChild(connectedNodeParent);//Add the connected node's parent to the current node's union
        currentNodeParentC.importChildren(connectedNodeParentC.getChildren());//Add the other nodes from the connected node's union to the current node's union
        connectedNodeParentC.clearChildren();//Remove the children from the connected node's union

        int newCurrentNodeUnionSize = currentNodeParentC.getUnionSize();
        for (int k = currentNodeUnionSize; k < newCurrentNodeUnionSize; k++)//Updates the parent for each of the new nodes added to the current nodes union
            UnionFind.get(currentNodeParentC.getChild(k)).setParent(currentNodeParent);
    }

    void addCurrentUnionToConnectedUnion() {//Add the current union to the connected union
        connectedNodeParentC.addChild(currentNodeParent);//Add the current node's parent to the connected node's union
        connectedNodeParentC.importChildren(currentNodeParentC.getChildren());//Add the other nodes from the current node's union to the connected node's union
        currentNodeParentC.clearChildren();//Remove the children from the current node's union

        int newConnectedNodeUnionSize = connectedNodeParentC.getUnionSize();
        for (int k = connectedNodeUnionSize; k < newConnectedNodeUnionSize; k++)//Updates the parent for each of the new nodes added to the connected nodes union
            UnionFind.get(connectedNodeParentC.getChild(k)).setParent(connectedNodeParent);
    }
}