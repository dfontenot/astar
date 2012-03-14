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
import com.fontenot.astar.gui.NodeGraphPanel;
import com.fontenot.astar.AStarAlgo;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/* This class controls the state of the button as well as 
 * what actions the button takes at different states. This is where 
 * the A* algorithm is called from.
 */
public class PathButtonState implements ActionListener {
    public static final int NOSTART = 0;
    public static final int NOEND = 1;
    public static final int READY = 2;
    public static final int RENDERING = 3;
    public static final int RESET = 4;
    
    private AStar parent;
    private NodeGraphPanel sibling;
    private JButton button;
    private int state;
    
    public PathButtonState(JButton b, AStar parent, NodeGraphPanel sibling) {
        this.parent = parent;
        this.sibling = sibling;
        button = b;
        button.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        setState(NOSTART);
    }

    public void setState(int state) {
        if(state == NOSTART) {
            this.state = state;
            button.setText("No start placed");
            button.setEnabled(false);
        }
        else if(state == NOEND) {
            this.state = state;
            button.setText("No end placed");
            button.setEnabled(false);
        }
        else if(state == READY) {
            this.state = state;
            button.setText("Find path");
            button.setEnabled(true);
        }
        else if(state == RENDERING) {
            this.state = state;
            button.setText("Rendering...");
            button.setEnabled(false);
        }
        else if(state == RESET) {
            this.state = state;
            button.setText("Reset");
            button.setEnabled(true);
        }
        else {
            System.out.println("illegal state recieved: " + state);
        }
    }
    
    public int getState() { return state; }
    
    public void actionPerformed(ActionEvent ae) {
        if(state == RESET) {
            //reset all nodes
            
            sibling.resetNodes();
            sibling.repaint();
            
            setState(NOSTART);
        }
        else if(state == READY) {
            //find the path when the button is pressed
            setState(RENDERING);
            AStarAlgo alg = new AStarAlgo(sibling.getField(), sibling.startW, sibling.startH, sibling.endW, sibling.endH);
            
            alg.findPath();
            sibling.repaint();
            setState(RESET);
        }
        else {
            System.out.println("wtf from PathButtonState.java");
        }
    }
}
