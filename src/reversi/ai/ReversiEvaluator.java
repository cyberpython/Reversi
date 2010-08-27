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
package reversi.ai;

import ai.Evaluator;
import ai.Node;
import reversi.State;
import reversi.Utils;

/**
 *
 * @author cyberpython
 */
public class ReversiEvaluator extends Evaluator{

    public enum EvaluationMethod {
        VALID_MOVES_AND_TOTAL_SCORE, VALID_MOVES_AND_SIDES_COUNT, VALID_MOVES_AND_CORNERS
    }

     private int player;
     private EvaluationMethod evalMethod;

     public ReversiEvaluator(){
         player = Utils.BLACK;
         evalMethod = ReversiEvaluator.EvaluationMethod.VALID_MOVES_AND_CORNERS;
     }

     public ReversiEvaluator(int player, EvaluationMethod evalMethod){
         this.player = player;
         this.evalMethod = evalMethod;
     }

    /*
     * Evaluation methods:
     *
     */

    public int evaluate(Node n) {
        
        int value;

        ReversiNode node = (ReversiNode) n;
        State s = node.getState();

        //Used for tests:
        /*value = (int) Math.round(Math.random() * 10);
        if (Math.random() > 0.5) {
        value = -value;
        }*/

        if (evalMethod == EvaluationMethod.VALID_MOVES_AND_TOTAL_SCORE) {
            value = validMovesCount(s, player) + totalScore(s, player);
        } else if (evalMethod == EvaluationMethod.VALID_MOVES_AND_SIDES_COUNT) {
            value = validMovesCount(s, player)+ sidesCount(s, player);
        } else {
            value = validMovesCount(s, player) + cornerCount(s, player)*100;
        }

        //value = validMovesCount(s, player) + cornerCount(s, player)*100;

        return value;
    }

    private int playersScore(State s, int player) {
        int dimension = s.getDimension();

        int total = 0;
        int val = 0;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                val = s.get(i, j);
                if (val == player) {
                    total++;
                }
            }
        }

        return total;
    }

    private int totalScore(State s, int player) {
        int dimension = s.getDimension();
        int opponent = Utils.getOpponentsColour(player);

        int total = 0;
        int val = 0;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                val = s.get(i, j);
                if (val == player) {
                    total++;
                } else if (val == opponent) {
                    total--;
                }
            }
        }

        return total;
    }

    private int validMovesCount(State s, int player) {
        int mobility = s.getValidMoves(player).size();

        return mobility;
    }

    private int cornerCount(State s, int player) {
        int total = 0;
        int opponent = Utils.getOpponentsColour(player);
        int val = 0;
        int max = s.getDimension() - 1;

        val = s.get(0, 0);
        if (val == player) {
            total++;
        } else if (val == opponent) {
            total--;
        }

        val = s.get(max, 0);
        if (val == player) {
            total++;
        } else if (val == opponent) {
            total--;
        }

        val = s.get(0, max);
        if (val == player) {
            total++;
        } else if (val == opponent) {
            total--;
        }

        val = s.get(max, max);
        if (val == player) {
            total++;
        } else if (val == opponent) {
            total--;
        }

        return total;
    }

    private int sidesCount(State s, int player) {
        int total = 0;
        int opponent = Utils.getOpponentsColour(player);
        int max = s.getDimension() - 1;
        int val = 0;

        for (int i = 1; i < max; i++) {

            val = s.get(i, 0);
            if (val == player) {
                total++;
            } else if (val == opponent) {
                total--;
            }

            val = s.get(0, i);
            if (val == player) {
                total++;
            } else if (val == opponent) {
                total--;
            }

            val = s.get(i, max);
            if (val == player) {
                total++;
            } else if (val == opponent) {
                total--;
            }

            val = s.get(max, i);
            if (val == player) {
                total++;
            } else if (val == opponent) {
                total--;
            }
        }

        return total;
    }

}
