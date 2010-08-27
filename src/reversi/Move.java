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

public class Move extends ai.Move{

    private int x;
    private int y;
    private int colour;

    public Move(int x, int y, int colour){
        this.x = x;
        this.y = y;
        this.colour = colour;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public int getColour(){
        return this.colour;
    }

    @Override
    public String toString(){
        return (x+1)+","+(y+1)+": "+colour;
    }

    @Override
    public boolean equals(Object obj){
        if( ! (obj instanceof Move) ){
            return false;
        }
        else{
            Move m = (Move) obj;
            if ( this.hashCode() == m.hashCode() ){
                return true;
            }
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 13 * hash + this.x;
        hash = 13 * hash + this.y;
        hash = 13 * hash + this.colour;
        return hash;
    }

}
