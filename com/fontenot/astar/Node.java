/*
 *      Copyright 2010, David Fontenot. All rights reserved.
 *      Redistribution and use in source and binary forms, with or without
 *      modification, are permitted provided that the following conditions are
 *      met:
 *      
 *      * Redistributions of source code must retain the above copyright
 *        notice, this list of conditions and the following disclaimer.
 *      * Redistributions in binary form must reproduce the above
 *        copyright notice, this list of conditions and the following disclaimer
 *        in the documentation and/or other materials provided with the
 *        distribution.
 *      * Neither the name of the  nor the names of its
 *        contributors may be used to endorse or promote products derived from
 *        this software without specific prior written permission.
 *      
 *      THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 *      "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 *      LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 *      A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 *      OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *      SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *      LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 *      DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 *      THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *      (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 *      OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.fontenot.astar;

import com.fontenot.astar.gui.NodeGraphPanel;

import java.awt.geom.Rectangle2D;

/* A basic representation of a node */
public class Node implements java.lang.Comparable {
    public static final int OPEN = 0;
    public static final int WALL = 1;
    public static final int START = 2;
    public static final int END = 3;
    
    private Node parent;
    private int type;
    
    //used for when NodeGraphPanel draws each node
    private Rectangle2D.Double drawRect;
    
    private double gScore;
    private double hScore;
    private double fScore;
    
    private int w;
    private int h;
    
    public Node(int w, int h) {
        parent = null;
        type = OPEN;
        
        drawRect = new Rectangle2D.Double(w * NodeGraphPanel.SIZEFIELDRATIO, 
        h * NodeGraphPanel.SIZEFIELDRATIO, 
        NodeGraphPanel.SIZEFIELDRATIO, 
        NodeGraphPanel.SIZEFIELDRATIO);
        
        //signifies uninitialized
        gScore = -1;
        hScore = -1;
        fScore = -1;
        
        this.w = w;
        this.h = h;
    }
    
    //removes all information generated from the search and resets type to open
    public void reset() {
        fScore = -1;
        hScore = -1;
        gScore = -1;
        type = OPEN;
        parent = null;
        //drawRect = null;
    }
    
    public int getType() { return type; }
    public double getGScore() { return gScore; }
    public double getHScore() { return hScore; }
    public double getFScore() { return fScore; }
    public int getW() { return w; }
    public int getH() { return h; }
    public Node getParent() { return parent; }
    public Rectangle2D.Double getDrawRect() { return drawRect; }
    
    public void setCosts(double g, double h) {
        gScore = g;
        hScore = h;
        fScore = g + h;
    }
    
    public void setParent(Node newParent) {
        parent = newParent;
    }
    
    public void setType(int type) {
        if(type == OPEN) {
            this.type = OPEN;
        }
        else if(type == WALL) {
            this.type = WALL;
        }
        else if(type == START) {
            this.type = START;
        }
        else if(type == END) {
            this.type = END;
        }
        else {
            System.out.println("error in Node.class: can't set type to " + type);
        }
    }
    
    //compares by the fScore
    public int compareTo(Object o) {
        Node other = (Node)o;
        if(fScore > other.getFScore()) {
            return 1;
        }
        else if(fScore < other.getFScore()) {
            return -1;
        }
        else {
            return 0;
        }
    }
}
