/*
 * Copyright (c) 2010 Georgios Migdos <cyberpython@gmail.com>, Filia Dova
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package ai;

import java.util.Vector;

public abstract class Node {

    public enum NodeType {

        MAX, MIN
    }
    private boolean visited;
    private boolean expanded;
    private Node parent;
    private Vector<Node> children;
    private int nextChildIndex;
    private Move moveLeadingHere;
    private Move nextMove;
    private int currentPlayer;
    private NodeType type;
    private int depth;
    private Integer value;
    public static long NextNodeId;
    private long nodeId;

    public Node() {
        init(null, null);
    }

    public Node(Node parent, Move m) {
        init(parent, m);
    }

    private void init(Node parent, Move m) {
        this.setParent(parent);
        if (parent == null) {
            this.setType(NodeType.MAX);
            this.children = new Vector<Node>();
            this.setMoveLeadingHere(null);
            this.setNextMove(null);
            this.setDepth(0);
            this.setValue(null);
            this.setVisited(false);
            this.setExpanded(false);
            Node.NextNodeId = 0;

        } else {
            this.setParent(parent);
            this.children = new Vector<Node>();
            this.setMoveLeadingHere(m);
            this.setNextMove(null);
            this.setDepth(this.parent.getDepth() + 1);
            this.setValue(null);
            this.setVisited(false);
            this.setExpanded(false);
            this.setType(this.invertType(parent.getType()));
        }

        this.nextChildIndex = 0;
        this.nodeId = Node.NextNodeId;
        Node.NextNodeId++;
    }

    public Node getParent() {
        return this.parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Move getMoveLeadingHere() {
        return this.moveLeadingHere;
    }

    public void setMoveLeadingHere(Move moveLeadingHere) {
        this.moveLeadingHere = moveLeadingHere;
    }

    public boolean isRoot() {
        return (this.parent == null);
    }

    public boolean hasBeenVisited() {
        return this.visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public boolean hasBeenExpanded() {
        return this.expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public NodeType getType() {
        return this.type;
    }

    public void setType(NodeType type) {
        this.type = type;
    }

    public int getDepth() {
        return this.depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public void addChild(Node child){
        this.children.add(child);
    }

    public abstract void expand();

    public Vector<Node> getChildren() {
        if (!this.hasBeenExpanded()) {
            this.expand();
        }
        return this.children;
    }

    public Node getNextChild() {
        Node result = null;
        Vector<Node> nodeChildren = this.getChildren();
        int maxChildIndex = nodeChildren.size() - 1;
        if (this.nextChildIndex <= maxChildIndex) {
            result = nodeChildren.get(this.nextChildIndex);
            this.nextChildIndex++;
        }
        return result;
    }

    public boolean hasMoreChildren() {
        if (!this.hasBeenExpanded()) {
            this.expand();
        }
        return (this.nextChildIndex <= this.getChildren().size() - 1);
    }

    public Move getNextMove() {
        return this.nextMove;
    }

    public void setNextMove(Move m) {
        this.nextMove = m;
    }

    public abstract boolean isEndGameNode();/*{
    return this.getState().checkGameOver();
    }*/


    public boolean isTerminal(int maxDepth) {
        if ((isEndGameNode()) || (getDepth() == maxDepth)) {
            return true;
        }
        return false;
    }

    public NodeType invertType(NodeType type) {
        if (type == NodeType.MAX) {
            return NodeType.MIN;
        }
        return NodeType.MAX;
    }

    public Integer getValue() {
        return this.value;
    }

    public boolean setValue(Integer newValue) {
        Integer currentValue = this.getValue();
        if (currentValue == null) {
            this.value = newValue;
            return true;
        } else {
            if (this.getType() == NodeType.MAX) {
                if (newValue > currentValue) {
                    this.value = newValue;
                    return true;
                } else {
                    return false;
                }
            } else {
                if (newValue < currentValue) {
                    this.value = newValue;
                    return true;
                } else {
                    return false;
                }
            }
        }
    }

    @Override
    public String toString() {
        String T = "MAX";
        if (this.getType() == NodeType.MIN) {
            T = "MIN";
        }
        Integer mmv = this.getValue();
        String V = "null";
        if (mmv != null) {
            V = mmv.toString();
        }
        return this.nodeId + " (V: " + V + ",T: " + T + ", D: " + this.getDepth() + ")";
    }
}
