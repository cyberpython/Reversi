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
package reversi;

import ai.Evaluator;
import ai.MiniMax;
import reversi.exceptions.InvalidMoveException;
import ai.MiniMax.SearchAlgorithm;
import reversi.ai.ReversiEvaluator;
import reversi.ai.ReversiEvaluator.EvaluationMethod;
import reversi.ai.ReversiNode;

public class AIPlayer extends Thread {

    private GameController gc;
    private long waitFor;
    private int d;
    private ReversiNode n;
    private SearchAlgorithm algorithm;
    private EvaluationMethod evalMethod;

    public AIPlayer(GameController gc, ReversiNode n, int maxDepth, SearchAlgorithm algorithm, EvaluationMethod evalMethod, long waitForMillis) {
        this.gc = gc;
        this.waitFor = waitForMillis;
        this.d = maxDepth;
        this.n = n;
        this.algorithm = algorithm;
        this.evalMethod = evalMethod;
    }

    @Override
    public void run() {
        long time1 = System.currentTimeMillis();
        MiniMax m = new MiniMax();
        Evaluator eval = new ReversiEvaluator(n.getCurrentPlayer(), evalMethod);
        m.apply(n, d, algorithm, eval);
        //System.out.println(n.label + " = " + n.getMiniMaxValue());

        long time2 = System.currentTimeMillis();
        long remaining = waitFor - (time2 - time1);

        if (remaining > 0) {
            try {
                Thread.sleep(remaining);
            } catch (InterruptedException ie) {
            }
        }

        try {
            gc.AIEndMove(n);
        } catch (InvalidMoveException ime) {
        }
    }
}
