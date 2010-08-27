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

import reversi.exceptions.InvalidMoveException;
import java.awt.Point;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.Vector;

public class Board {
    
    private State state;
    private int boardDimension;
    private Vector<BoardListener> views;
    private boolean gameOver;

    public Board() {
        this.boardDimension = Utils.BOARD_SIZE;
        this.state = new State(this.boardDimension);
        this.initStateValues();

        this.views = new Vector<BoardListener>();
        this.gameOver = false;

        //Used for tests:
       //this.state.fillBoardDummy();
    }

    private void initStateValues(){
        this.state.set(3,4, Utils.BLACK);
        this.state.set(4,3, Utils.BLACK);
        this.state.set(3,3, Utils.WHITE);
        this.state.set(4,4, Utils.WHITE);
    }

    public State getState() {
        return this.state;
    }

    public void setState(State s){
        if(s!=null){
            this.state = s;
            this.gameOver = this.checkGameOver();
            this.updateViews();
        }
    }

    public int getBoardDimension() {
        return this.boardDimension;
    }

    public int getBlackValue() {
        return Utils.BLACK;
    }

    public int getWhiteValue() {
        return Utils.WHITE;
    }

    public int getEmptyValue() {
        return Utils.EMPTY;
    }

    public void put(int x, int y, int colour) throws InvalidMoveException {
        if (this.state.checkPosition(x, y, colour)) {
            this.state.put(x, y, colour);
            this.gameOver = this.checkGameOver();
            this.updateViews();            
        } else {
            throw new InvalidMoveException(x, y, colour);
        }
    }

    public Point getScore(){
        return this.state.getScore();
    }

    public boolean isTheGameOver(){
        this.gameOver = this.checkGameOver();
        return this.gameOver;
    }

    private boolean checkGameOver() {
        return this.state.checkGameOver();
    }

    

    public boolean checkPosition(int x, int y, int colour) {
        this.state.checkPosition(x,y,colour);
        return false;
    }

    private void flip(int x, int y, int colour) {
        this.state.flip(x,y,colour);
        this.updateViews();
    }

    public Vector<Move> getValidMoves(int playerColour) {
        return this.state.getValidMoves(playerColour);
    }

    public void addView(BoardListener view) {
        this.views.add(view);
    }

    public void removeView(BoardListener view) {
        this.views.remove(view);
    }

    public Vector<BoardListener> getViews() {
        return this.views;
    }

    private void updateViews() {
        for (Iterator<BoardListener> it = views.iterator(); it.hasNext();) {
            BoardListener boardView = it.next();
            boardView.notifyBoardChanged(this);
        }
    }

    public void printBoard(PrintStream out) {
        this.state.print(out);
    }
}
