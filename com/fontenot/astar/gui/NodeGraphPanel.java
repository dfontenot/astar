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

package com.fontenot.astar.gui;

import com.fontenot.astar.Node;
import com.fontenot.astar.gui.AStar;

import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.Color;

/* The NodeGraphPanel is what the user clicks on to place wall, 
 * start and end Nodes. All mouse events here are handeled by the 
 * NodePlacedListener class which in turn also controls a JPopupMenu 
 * and listeners on its items. When the user right clicks on a Node 
 * the popup menu is made visible and they can chose whether they want 
 * a start or end Node there.
 */
public class NodeGraphPanel extends JPanel {
    public static final int WIDTH = 640;
    public static final int HEIGHT = 640;
    public static final int FIELDW = 50;
    public static final int FIELDH = 50;
    public static final double SIZEFIELDRATIO = 12.8;
    
    private static final Color WALLCOLOR = Color.BLUE;
    private static final Color STARTCOLOR = Color.GREEN;
    private static final Color ENDCOLOR = Color.RED;
    private static final Color STROKECOLOR = Color.BLACK;
    private static final Color PATHCOLOR = Color.ORANGE;
    
    private AStar parent;
    private NodePlacedListener npl;
    private Node[][] field;
    
    //remember where the start and the finish are
    public int startW;
    public int startH;
    public int endW;
    public int endH;
    
    public NodeGraphPanel(AStar parentFrame) {
        parent = parentFrame;
        //thanks to freenode's #java channel and AMcBain
        setPreferredSize(new Dimension(640, 480));
        setFocusable(true);
        setVisible(true);
        
        //set up field
        field = new Node[FIELDW][FIELDH];
        for(int w = 0; w < FIELDW; w++) {
            for(int h = 0; h < FIELDH; h++) {
                field[w][h] = new Node(w, h);
            }
        }
        
        //give this jpanel a mouse listener
        npl = new NodePlacedListener(this);
        addMouseListener(npl);
        
        //set the start and end coords to unitialized
        startW = -1;
        startH = -1;
        endW = -1;
        endH = -1;
    }
    
    //called when the panel is ready
    public void addNotify() {
        super.addNotify();
        System.out.println("ready");
    }
    
    /* called by NodePlacedListener.java and StartEndListener.java */
    public java.awt.Point getFrameLocation() { return parent.getLocation(); }
    
    public int getpButtonState() {
        return parent.getpButtonState();
    }
    
    public void setpButtonState(int state) {
        parent.setpButtonState(state);
    }
    
    public int getNodeTypeAt(int w, int h) {
        return field[w][h].getType();
    }
    
    public void setNodeTypeAt(int w, int h, int type) {
        field[w][h].setType(type);
    }
    
    /* even though the start and end coords are public 
     * this just makes it easier to set them
     */
    public void setStartCoords(int w, int h) {
        startW = w;
        startH = h;
    }
    
    public void setEndCoords(int w, int h) {
        endW = w;
        endH = h;
    }
    /* end */
    
    /* called by PathButtonState.java */
    public Node[][] getField() {
        return field;
    }
    /* end */
    
    //a painting helper method
    private void drawCurRect(Graphics2D g, Rectangle2D.Double r, Color fill) {
        g.setColor(fill);
        g.fill(r);
        g.setColor(STROKECOLOR);
        g.draw(r);
    }
    
    //a painting helper method to draw the path
    //will exit if no path (aka endNode has no parent)
    private void drawPath(Graphics2D g) {
        if(endW != -1 && endH != -1) {
            //make sure an end has been placed
            
            Node currentNode = field[endW][endH];
            
            while(currentNode.getParent() != null) {
                if(currentNode == field[endW][endH]) { g.setColor(ENDCOLOR); }
                else { g.setColor(PATHCOLOR); }
                g.fill(currentNode.getDrawRect());
                g.setColor(STROKECOLOR);
                g.draw(currentNode.getDrawRect());
                
                currentNode = currentNode.getParent();
            }
        }
    }
    
    //calls every single node's reset function
    public void resetNodes() {
        for(int w = 0; w < FIELDW; w++) {
            for(int h = 0; h < FIELDH; h++) {
                field[w][h].reset();
            }
        }
    }
    
    public void paint(java.awt.Graphics oldGraphics) {
        //convert to graphics 2d and paint all white
        Graphics2D g = (Graphics2D)oldGraphics;
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
        
        //loop over each node and get it's rectangle and print out it's info
        for(int w = 0; w < FIELDW; w++) {
            for(int h = 0; h < FIELDH; h++) {
                Node curNode = field[w][h];
                int curType = curNode.getType();
                Rectangle2D.Double curRect = curNode.getDrawRect();
                
                if(curType == Node.OPEN) {
                    //don't color keep it white
                    drawCurRect(g, curRect, Color.WHITE);
                }
                else if(curType == Node.WALL) {
                    drawCurRect(g, curRect, WALLCOLOR);
                }
                else if(curType == Node.START) {
                    drawCurRect(g, curRect, STARTCOLOR);
                }
                else if(curType == Node.END) {
                    drawCurRect(g, curRect, ENDCOLOR);
                }
                else {
                    System.out.println("error in NodeGraphPanel.class: illegal state value " + curType);
                }
            }
        }
        
        drawPath(g);
    }
}
