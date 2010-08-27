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

import java.awt.Point;
import java.io.PrintStream;
import java.util.Vector;

public class State {

    private int[][] state;
    private int dimension;

    public State(){
        this.dimension = Utils.BOARD_SIZE;
        state = new int[this.dimension][this.dimension];
        
        this.state[3][4] = Utils.BLACK;
        this.state[4][3] = Utils.BLACK;
        this.state[3][3] = Utils.WHITE;
        this.state[4][4] = Utils.WHITE;       
    }

    public State(int dimension){
        this.dimension = dimension;
        state = new int[this.dimension][this.dimension];

        //this.fillBoardDummy2();
    }

    public State(State original){
        this.dimension = original.getDimension();
        state = new int[this.dimension][this.dimension];
        for(int i=0; i<this.dimension; i++){
            for(int j=0; j<this.dimension; j++){
                this.state[i][j] = original.get(i, j);
            }
        }
    }

    //Used for tests:
    public void fillBoardDummy(){
        for (int i = 0; i < this.dimension; i++) {
            for (int j = 0; j < this.dimension; j++) {
                double rand = Math.random();
                int val = -1;
                //if(rand>=0.5){val=1;}
                //else{val=-1;}
                if((i!=7)||(j!=0)){
                    this.state[i][j] = val;
                }
            }
        }
        this.state[3][4]=1;
    }

    //Used for tests:
    public void fillBoardDummy2(){
        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state.length; j++) {
                this.state[i][j] = Utils.BLACK;
            }
        }

        for (int i = 0; i < state.length; i++) {
            this.state[0][i]=Utils.WHITE;
            this.state[i][0]=Utils.WHITE;
            if(i<5){
                this.state[i][7]=Utils.WHITE;
                this.state[7][i]=Utils.WHITE;
            }
        }
        this.state[2][3]=Utils.WHITE;
        this.state[2][4]=Utils.WHITE;
        this.state[2][5]=Utils.WHITE;
        this.state[4][2]=Utils.WHITE;
        this.state[5][2]=Utils.WHITE;
        this.state[6][1]=Utils.WHITE;
        this.state[6][3]=Utils.WHITE;
        this.state[6][4]=Utils.WHITE;
        this.state[6][5]=Utils.WHITE;
        this.state[1][6]=Utils.WHITE;
        this.state[3][6]=Utils.WHITE;
        this.state[4][6]=Utils.WHITE;
        this.state[5][6]=Utils.WHITE;
        this.state[6][6]=Utils.WHITE;

        this.state[5][7]=Utils.EMPTY;
        this.state[7][7]=Utils.EMPTY;


    }

    public int getDimension(){
        return this.dimension;
    }

    public int get(int x, int y){
        return this.state[x][y];
    }

    public void set(int x, int y, int value){
        this.state[x][y] = value;
    }

    public void put(int x, int y, int value){
        this.state[x][y] = value;
        this.flip(x, y, value);
    }

    public Point getScore(){
        int totalBlack = 0;
        int totalWhite = 0;
        int val = 0;
        for (int i = 0; i < this.dimension; i++) {
            for (int j = 0; j < this.dimension; j++) {
                val = this.state[i][j];
                if (val == Utils.BLACK) {
                    totalBlack++;
                } else if (val == Utils.WHITE) {
                    totalWhite++;
                }
            }
        }
        return new Point(totalBlack, totalWhite);
    }

    public boolean checkGameOver() {
        int black = 0;
        int white = 0;
        int empty = 0;
        int legalBlack = 0;
        int legalWhite = 0;
        int val = 0;
        for (int i = 0; i < this.dimension; i++) {
            for (int j = 0; j < this.dimension; j++) {
                val = this.state[i][j];
                if (val == Utils.BLACK) {
                    black++;
                } else if (val == Utils.WHITE) {
                    white++;
                }
                else{
                    empty++;
                }
                if (checkPosition(i, j, Utils.BLACK)) {
                    legalBlack++;
                }
                if (checkPosition(i, j, Utils.WHITE)) {
                    legalWhite++;
                }
            }
        }
        if( (empty==0) || (black==0) || (white==0) || (      (legalBlack==0)&&(legalWhite==0)      ) ){
            return true;
        }
        return false;
    }

    public Vector<Move> getValidMoves(int playerColour) {
        int val = 0;
        Vector<Move> results = new Vector<Move>();
        for (int j = 0; j < this.dimension; j++) {
            for (int i = 0; i < this.dimension; i++) {
                if (checkPosition(i, j, playerColour)) {
                    Move m = new Move(i, j, playerColour);
                    results.add(m);
                }
            }
        }
        return results;
    }

    public State getChildState(Move move){
        if(checkPosition(move.getX(), move.getY(), move.getColour())){
            State child = new State(this);
            child.put(move.getX(), move.getY(), move.getColour());
            return child;
        }
        return null;
    }

    // <editor-fold defaultstate="collapsed" desc="Private methods to check if a position is valid.">
    private boolean checkLeftUp(int x, int y, int colour) {
        if ((y == 0) || (x == 0)) {
            return false;
        }

        int opponentsColour = Utils.getOpponentsColour(colour);

        if (this.state[x - 1][y - 1] != opponentsColour) {
            return false;
        } else {
            int j = y - 2;
            int i = x - 2;
            int value = 0;
            boolean contiguous = true;
            while ((j >= 0) && (i >= 0) && (contiguous)) {
                value = this.state[i][j];
                if (value != opponentsColour) {
                    contiguous = false;
                    if (value == colour) {
                        return true;
                    }
                } else {
                    j--;
                    i--;
                }
            }
            return false;
        }

    }

    private boolean checkLeftDown(int x, int y, int colour) {
        int max = this.dimension - 1;
        if ((y == max) || (x == 0)) {
            return false;
        }

        int opponentsColour = Utils.getOpponentsColour(colour);

        if (this.state[x - 1][y + 1] != opponentsColour) {
            return false;
        } else {
            int j = y + 2;
            int i = x - 2;
            int value = 0;
            boolean contiguous = true;
            while ((j <= max) && (i >= 0) && (contiguous)) {
                value = this.state[i][j];
                if (value != opponentsColour) {
                    contiguous = false;
                    if (value == colour) {
                        return true;
                    }
                } else {
                    j++;
                    i--;
                }
            }
            return false;
        }

    }

    private boolean checkRightUp(int x, int y, int colour) {
        int max = this.dimension - 1;
        if ((y == 0) || (x == max)) {
            return false;
        }

        int opponentsColour = Utils.getOpponentsColour(colour);

        if (this.state[x + 1][y - 1] != opponentsColour) {
            return false;
        } else {
            int j = y - 2;
            int i = x + 2;
            int value = 0;
            boolean contiguous = true;
            while ((j >= 0) && (i <= max) && (contiguous)) {
                value = this.state[i][j];
                if (value != opponentsColour) {
                    contiguous = false;
                    if (value == colour) {
                        return true;
                    }
                } else {
                    j--;
                    i++;
                }
            }
            return false;
        }

    }

    private boolean checkRightDown(int x, int y, int colour) {
        int max = this.dimension - 1;
        if ((y == max) || (x == max)) {
            return false;
        }

        int opponentsColour = Utils.getOpponentsColour(colour);

        if (this.state[x + 1][y + 1] != opponentsColour) {
            return false;
        } else {
            int j = y + 2;
            int i = x + 2;
            int value = 0;
            boolean contiguous = true;
            while ((j <= max) && (i <= max) && (contiguous)) {
                value = this.state[i][j];
                if (value != opponentsColour) {
                    contiguous = false;
                    if (value == colour) {
                        return true;
                    }
                } else {
                    j++;
                    i++;
                }
            }
            return false;
        }

    }

    private boolean checkUp(int x, int y, int colour) {
        if (y == 0) {
            return false;
        }

        int opponentsColour = Utils.getOpponentsColour(colour);

        if (this.state[x][y - 1] != opponentsColour) {
            return false;
        } else {
            int j = y - 2;
            int value = 0;
            boolean contiguous = true;
            while ((j >= 0) && (contiguous)) {
                value = this.state[x][j];
                if (value != opponentsColour) {
                    contiguous = false;
                    if (value == colour) {
                        return true;
                    }
                } else {
                    j--;
                }
            }
            return false;
        }

    }

    private boolean checkDown(int x, int y, int colour) {
        int max = this.dimension - 1;
        if (y == max) {
            return false;
        }

        int opponentsColour = Utils.getOpponentsColour(colour);

        if (this.state[x][y + 1] != opponentsColour) {
            return false;
        } else {
            int j = y + 2;
            int value = 0;
            boolean contiguous = true;
            while ((j <= max) && (contiguous)) {
                value = this.state[x][j];
                if (value != opponentsColour) {
                    contiguous = false;
                    if (value == colour) {
                        return true;
                    }
                } else {
                    j++;
                }
            }
            return false;
        }

    }

    private boolean checkLeft(int x, int y, int colour) {
        if (x == 0) {
            return false;
        }

        int opponentsColour = Utils.getOpponentsColour(colour);

        if (this.state[x - 1][y] != opponentsColour) {
            return false;
        } else {
            int i = x - 2;
            int value = 0;
            boolean contiguous = true;
            while ((i >= 0) && (contiguous)) {
                value = this.state[i][y];
                if (value != opponentsColour) {
                    contiguous = false;
                    if (value == colour) {
                        return true;
                    }
                } else {
                    i--;
                }
            }
            return false;
        }

    }

    private boolean checkRight(int x, int y, int colour) {
        int max = this.dimension - 1;
        if (x == max) {
            return false;
        }

        int opponentsColour = Utils.getOpponentsColour(colour);

        if (this.state[x + 1][y] != opponentsColour) {
            return false;
        } else {
            int i = x + 2;
            int value = 0;
            boolean contiguous = true;
            while ((i <= max) && (contiguous)) {
                value = this.state[i][y];
                if (value != opponentsColour) {
                    contiguous = false;
                    if (value == colour) {
                        return true;
                    }
                } else {
                    i++;
                }
            }
            return false;
        }

    }

    private boolean checkHorizontal(int x, int y, int colour) {
        if (checkLeft(x, y, colour)) {
            return true;
        }
        if (checkRight(x, y, colour)) {
            return true;
        }
        return false;
    }

    private boolean checkVertical(int x, int y, int colour) {
        if (checkUp(x, y, colour)) {
            return true;
        }
        if (checkDown(x, y, colour)) {
            return true;
        }
        return false;
    }

    private boolean checkDiagonal(int x, int y, int colour) {
        if (checkLeftUp(x, y, colour)) {
            return true;
        }
        if (checkLeftDown(x, y, colour)) {
            return true;
        }
        if (checkRightUp(x, y, colour)) {
            return true;
        }
        if (checkRightDown(x, y, colour)) {
            return true;
        }
        return false;
    }
    // </editor-fold>

    public boolean checkPosition(int x, int y, int colour) {
        if (this.state[x][y] != Utils.EMPTY) {
            return false;
        }
        if (this.checkHorizontal(x, y, colour)) {
            return true;
        }
        if (this.checkVertical(x, y, colour)) {
            return true;
        }
        if (this.checkDiagonal(x, y, colour)) {
            return true;
        }
        return false;
    }

    // <editor-fold defaultstate="collapsed" desc="Private methods to flip all the contiguous opponent's discs.">
    private void tryFlipUp(int x, int y, int colour) {
        if (this.checkUp(x, y, colour)) {
            int i = y - 1;
            int value = 0;
            boolean contiguous = true;
            while ((i >= 0) && (contiguous)) {
                value = this.state[x][i];
                if (value == colour) {
                    contiguous = false;
                } else {
                    this.state[x][i] = colour;
                    i--;
                }
            }
        }
    }

    private void tryFlipDown(int x, int y, int colour) {
        int max = this.dimension - 1;
        if (this.checkDown(x, y, colour)) {
            int i = y + 1;
            int value = 0;
            boolean contiguous = true;
            while ((i <= max) && (contiguous)) {
                value = this.state[x][i];
                if (value == colour) {
                    contiguous = false;
                } else {
                    this.state[x][i] = colour;
                    i++;
                }
            }
        }
    }

    private void tryFlipRight(int x, int y, int colour) {
        int max = this.dimension - 1;
        if (this.checkRight(x, y, colour)) {
            int i = x + 1;
            int value = 0;
            boolean contiguous = true;
            while ((i <= max) && (contiguous)) {
                value = this.state[i][y];
                if (value == colour) {
                    contiguous = false;
                } else {
                    this.state[i][y] = colour;
                    i++;
                }
            }
        }
    }

    private void tryFlipLeft(int x, int y, int colour) {
        if (this.checkLeft(x, y, colour)) {
            int i = x - 1;
            int value = 0;
            boolean contiguous = true;
            while ((i >= 0) && (contiguous)) {
                value = this.state[i][y];
                if (value == colour) {
                    contiguous = false;
                } else {
                    this.state[i][y] = colour;
                    i--;
                }
            }
        }
    }

    private void tryFlipLeftUp(int x, int y, int colour) {
        if (this.checkLeftUp(x, y, colour)) {
            int j = y - 1;
            int i = x - 1;
            int value = 0;
            boolean contiguous = true;
            while ((j >= 0) && (i >= 0) && (contiguous)) {
                value = this.state[i][j];
                if (value == colour) {
                    contiguous = false;
                } else {
                    this.state[i][j] = colour;
                    j--;
                    i--;
                }
            }
        }
    }

    private void tryFlipLeftDown(int x, int y, int colour) {
        if (this.checkLeftDown(x, y, colour)) {
            int max = this.dimension - 1;
            int j = y + 1;
            int i = x - 1;
            int value = 0;
            boolean contiguous = true;
            while ((j <= max) && (i >= 0) && (contiguous)) {
                value = this.state[i][j];
                if (value == colour) {
                    contiguous = false;
                } else {
                    this.state[i][j] = colour;
                    j++;
                    i--;
                }
            }
        }
    }

    private void tryFlipRightUp(int x, int y, int colour) {
        if (this.checkRightUp(x, y, colour)) {
            int max = this.dimension - 1;
            int j = y - 1;
            int i = x + 1;
            int value = 0;
            boolean contiguous = true;
            while ((j >= 0) && (i <= max) && (contiguous)) {
                value = this.state[i][j];
                if (value == colour) {
                    contiguous = false;
                } else {
                    this.state[i][j] = colour;
                    j--;
                    i++;
                }
            }
        }
    }

    private void tryFlipRightDown(int x, int y, int colour) {
        if (this.checkRightDown(x, y, colour)) {
            int max = this.dimension - 1;
            int j = y + 1;
            int i = x + 1;
            int value = 0;
            boolean contiguous = true;
            while ((j <= max) && (i <= max) && (contiguous)) {
                value = this.state[i][j];
                if (value == colour) {
                    contiguous = false;
                } else {
                    this.state[i][j] = colour;
                    j++;
                    i++;
                }
            }
        }
    }
    // </editor-fold>

    public void flip(int x, int y, int colour) {
        this.tryFlipLeft(x, y, colour);
        this.tryFlipRight(x, y, colour);
        this.tryFlipUp(x, y, colour);
        this.tryFlipDown(x, y, colour);
        this.tryFlipLeftUp(x, y, colour);
        this.tryFlipLeftDown(x, y, colour);
        this.tryFlipRightUp(x, y, colour);
        this.tryFlipRightDown(x, y, colour);
    }

    public void print(PrintStream out) {
        String c = "    | ";
        int val = 0;
        int totalWhite = 0;
        int totalBlack = 0;
        for (int j = 0; j < this.dimension; j++) {
            for (int i = 0; i < this.dimension; i++) {
                val = this.state[i][j];
                if (val == Utils.BLACK) {
                    c = " b  | ";
                    totalBlack++;
                } else if (val == Utils.WHITE) {
                    c = " w  | ";
                    totalWhite++;
                } else {

                    c = "    | ";

                }
                if (i == 0) {
                    c = "|" + c;
                }//first column left bar
                out.print(c);
            }
            out.println("");
            out.println("-----------------------------------------------");
        }
        out.println("Black : " + totalBlack + " White: " + totalWhite);
    }

}
