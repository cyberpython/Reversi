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

import ai.Node;
import java.util.Iterator;
import java.util.Vector;
import reversi.Move;
import reversi.State;
import reversi.Utils;

public class ReversiNode extends Node {

    private State state;
    private int currentPlayer;

    public ReversiNode(State s, int currentPlayer) {
        super();
        init(s, currentPlayer);
    }

    public ReversiNode(ReversiNode parent, Move m) {
        super(parent, m);
        init(parent.getState().getChildState(m), Utils.getOpponentsColour(parent.getCurrentPlayer()));
    }

    private void init(State s, int currentPlayer) {
        this.setState(s);
        this.setCurrentPlayer(currentPlayer);
    }

    public State getState() {
        return this.state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public int getCurrentPlayer() {
        return this.currentPlayer;
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void expand() {
        Vector<Move> availableMoves = this.getState().getValidMoves(this.currentPlayer);
        for (Iterator<Move> it = availableMoves.iterator(); it.hasNext();) {
            Move move = it.next();
            ReversiNode child = new ReversiNode(this, move);
            this.addChild(child);
        }
        this.setExpanded(true);
    }

    public boolean isEndGameNode() {
        return this.getState().checkGameOver();
    }
}
