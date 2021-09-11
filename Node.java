//Jude Hayes
//Project 2
//8/7/2021

import java.util.ArrayList;

public class Node {

    int parent;//stores parent to the node
    ArrayList<Integer> children = new ArrayList<>();//stores any children to the current node

    Node(int node) {
        parent = node;
    }//constructor

    void addChild(int child) {//Adds child to union
        children.add(child);
    }

    int getChild(int index) { //returns child in union at index
        return children.get(index);
    }

    ArrayList<Integer> getChildren() { //returns all children
        return children;
    }

    void clearChildren() {//Removes all children from Union
        children = new ArrayList<>(0);
    }

    void importChildren(ArrayList<Integer> newChildren) {//adds an array of children to the union
        children.addAll(newChildren);
    }

    void setParent(int newParent) {//Updates parent
        parent = newParent;
    }

    int getParent() {//return parent
        return parent;
    }

    int getUnionSize() {//returns size of the union
        return children.size();
    }
}