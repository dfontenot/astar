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

import com.fontenot.astar.*;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.BoxLayout;

import java.awt.Dimension;
import java.awt.Component;

/* Entry point is contained in this class. 
 * This class launches the gui and adds the listeners. */
public class AStar extends JFrame {
    //fields that concern the find the path button
    private JButton pButton;
    private PathButtonState pButtonState;
    private NodeGraphPanel ngp;
    
    public AStar() {
        super("A* search algorithm (gui (c) David Fontenot 2010)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //thanks to this site for this:
        //http://www.java-tips.org/java-se-tips/javax.swing/how-to-use-swing-boxlayout.html
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        
        //add in the view of the graph
        ngp = new NodeGraphPanel(this);
        add(ngp);
        
        //set up the starting button
        pButton = new JButton();
        pButtonState = new PathButtonState(pButton, this, ngp);
        pButton.addActionListener(pButtonState);
        add(pButton);
        
        setResizable(false);
        setVisible(true);
        pack();
    }
    
    /* methods called by NodeGraphPanel.java */
    public int getpButtonState() { return pButtonState.getState(); }
    public void setpButtonState(int state) { pButtonState.setState(state); }
    /* end */
    
    public static void main(String[] args) { new AStar(); }
}
