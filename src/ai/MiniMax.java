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

import java.util.ArrayDeque;
import ai.Node.NodeType;

public class MiniMax {

    public enum SearchAlgorithm{
        MINIMAX, ALPHA_BETA_PRUNING
    }

    public void apply(Node n, int maxDepth, SearchAlgorithm algorithm, Evaluator eval){
        if(algorithm == SearchAlgorithm.MINIMAX){
            minimax(n, maxDepth, eval);
        }else if(algorithm == SearchAlgorithm.ALPHA_BETA_PRUNING){
            alphaBetaPruning(n, maxDepth, eval);
        }
    }

    private void minimax(Node n, int maxDepth, Evaluator eval) {
        if (n.isEndGameNode()) {
            System.out.println("H1");
            return;
        }
        ArrayDeque<Node> searchStack = new ArrayDeque<Node>();
        searchStack.push(n);

        while (searchStack.isEmpty() == false) {
            Node node = searchStack.pop();

            Move nextMove = node.getNextMove();
            boolean isRoot = node.isRoot();

            if (node.isTerminal(maxDepth)) {
                node.setValue(eval.evaluate(node));
                if (!isRoot) {
                    Node parent = node.getParent();
                    if (parent.setValue(node.getValue())) {
                        parent.setNextMove(node.getMoveLeadingHere());
                    }
                }
            } else {
                if (node.hasMoreChildren()) {
                    searchStack.push(node);
                    searchStack.push(node.getNextChild());
                } else {
                    if (nextMove != null) {
                        if (!isRoot) {
                            Node parent = node.getParent();
                            if (parent.setValue(node.getValue())) {
                                parent.setNextMove(node.getMoveLeadingHere());
                            }
                        }
                    }
                }
            }

            node.setVisited(true);
            //print(node);
        }
    }


    private void alphaBetaPruning(Node n, int maxDepth, Evaluator eval) {
        if (n.isEndGameNode()) {
            System.out.println("H1");
            return;
        }
        ArrayDeque<Node> searchStack = new ArrayDeque<Node>();
        searchStack.push(n);

        while (searchStack.isEmpty() == false) {
            Node node = searchStack.pop();

            Move nextMove = node.getNextMove();
            boolean isRoot = node.isRoot();

            if (node.isTerminal(maxDepth)) {
                node.setValue(eval.evaluate(node));
                if (!isRoot) {
                    Node parent = node.getParent();
                    if (parent.setValue(node.getValue())) {
                        parent.setNextMove(node.getMoveLeadingHere());
                    }
                }
            } else {
                if (node.hasMoreChildren()) {
                    if (!isRoot) {
                        Integer nodeValue = node.getValue();
                        if(nodeValue!=null){
                            Node parent = node.getParent();
                            Integer parentValue = parent.getValue();
                            if(parentValue==null){
                                searchStack.push(node);
                                searchStack.push(node.getNextChild());
                            }else{
                                NodeType type = parent.getType();
                                if(type == NodeType.MAX){
                                    //Pruning:
                                    if(node.getValue()>=parentValue){
                                        searchStack.push(node);
                                        searchStack.push(node.getNextChild());
                                    }
                                }else{
                                    //Pruning:
                                    if(node.getValue()<=parentValue){
                                        searchStack.push(node);
                                        searchStack.push(node.getNextChild());
                                    }
                                }
                            }
                        }
                        else{
                            searchStack.push(node);
                            searchStack.push(node.getNextChild());
                        }
                    }else{
                        searchStack.push(node);
                        searchStack.push(node.getNextChild());
                    }
                } else {
                    if (nextMove != null) {
                        if (!isRoot) {
                            Node parent = node.getParent();
                            if (parent.setValue(node.getValue())) {
                                parent.setNextMove(node.getMoveLeadingHere());
                            }
                        }
                    }
                }
            }

            node.setVisited(true);
            //print(node);
        }
    }

    

    private void print(Node n) {
        System.out.println(n.toString());
    }
}
