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

import com.fontenot.astar.gui.NodeGraphPanel;
import com.fontenot.astar.gui.PathButtonState;
import com.fontenot.astar.Node;

import javax.swing.JPopupMenu;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/* StartEndListener listens for item clicks on JMenuItems that happened 
 * in its parent class (not grandparent). Two of these are made by the 
 * parent class, each one can either listen for placing start or 
 * end Nodes.
 */
public class StartEndListener implements ActionListener {
    private int type;
    private NodeGraphPanel grandParent;
    private JPopupMenu menu;
    
    private int curW;
    private int curH;
    
    public StartEndListener(NodeGraphPanel grandParent, JPopupMenu menu, int type) {
        this.grandParent = grandParent;
        this.type = type;
        this.menu = menu;
    }
    
    /* methods called from NodePlacedListener.java */
    public void setCurNode(int w, int h) {
        curW = w;
        curH = h;
    }
    /* end */
    
    public void actionPerformed(ActionEvent ae) {
        int oldW; int oldH;
        if(type == Node.START) {
            oldW = grandParent.startW;
            oldH = grandParent.startH;
            
            //remove old start
            if(oldW != -1 && oldH != -1) {
                grandParent.setNodeTypeAt(oldW, oldH, Node.OPEN);
            }
            
            //set new start
            grandParent.setNodeTypeAt(curW, curH, Node.START);
            grandParent.setStartCoords(curW, curH);
            
            //update PathButtonState
            if(grandParent.getpButtonState() == PathButtonState.NOSTART) {
                //first time this start has been placed
                if(grandParent.endW == -1 || grandParent.endH == -1) {
                    //no end placed
                    grandParent.setpButtonState(PathButtonState.NOEND);
                }
                else {
                    grandParent.setpButtonState(PathButtonState.READY);
                }
            }
        }
        else if(type == Node.END) {
            oldW = grandParent.endW;
            oldH = grandParent.endH;
            
            //remove old end
            if(oldW != -1 && oldH != -1) {
                grandParent.setNodeTypeAt(oldW, oldH, Node.OPEN);
            }
            
            //set new end
            grandParent.setNodeTypeAt(curW, curH, Node.END);
            grandParent.setEndCoords(curW, curH);
            
            //update PathButtonState
            if(grandParent.getpButtonState() == PathButtonState.NOEND) {
                //first time this end has been placed
                if(grandParent.startW == -1 || grandParent.startH == -1) {
                    //no start placed
                    grandParent.setpButtonState(PathButtonState.NOSTART);
                }
                else {
                    grandParent.setpButtonState(PathButtonState.READY);
                }
            }
        }
        else {
            System.out.println("error in StartEndListener.java");
        }
        
        menu.setVisible(false);
        grandParent.repaint();
    }
}
