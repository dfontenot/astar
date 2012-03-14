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

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import java.awt.Point;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

/* This class listens for mouse events on the NodeGraphPanel and then 
 * changes the state of the field according to what new Node was 
 * placed there. It also controls the right click popup menu.
 */
public class NodePlacedListener implements MouseListener {
    private NodeGraphPanel parent;
    
    //popup menu stuff
    private JPopupMenu menu;
    private JMenuItem start;
    private JMenuItem end;
    private StartEndListener startListener;
    private StartEndListener endListener;
    
    public NodePlacedListener(NodeGraphPanel parent) {
        this.parent = parent;
        
        //create the jpopupmenu
        menu = new JPopupMenu("choices");
        
        //set the listeners for the jpopupmenu
        startListener = new StartEndListener(parent, menu, Node.START);
        endListener = new StartEndListener(parent, menu, Node.END);
        
        //add the items to it and set the action listeners
        start = new JMenuItem("Place start");
        start.addActionListener(startListener);
        end = new JMenuItem("Place end");
        end.addActionListener(endListener);
        
        menu.add(start);
        menu.add(end);
        menu.setVisible(false);
    }
    
    //left click = 1, wheel click = 2, right click = 3
    public void mousePressed(MouseEvent e) {
        /* There are two states from PathButtonState that 
         * do not need to use this event: RENDERING and RESET. 
         * Check to make sure it is not in either of those states.
         */
        
        /* Make the menu invisible in case the last time it was shown 
         * and was not selected from. */
        menu.setVisible(false);
        
        if(parent.getpButtonState() == PathButtonState.RENDERING || 
        parent.getpButtonState() == PathButtonState.RESET) {
            return;
        }
        
        int x = e.getX();
        int y = e.getY();
        int button = e.getButton();
        
        //check for in bounds
        if(x > NodeGraphPanel.WIDTH || y > NodeGraphPanel.HEIGHT) { return; }
        
        //determine what square the click was in
        int w = (int)Math.floor(x / NodeGraphPanel.SIZEFIELDRATIO);
        int h = (int)Math.floor(y / NodeGraphPanel.SIZEFIELDRATIO);
        
        if(button == MouseEvent.BUTTON1) {
            //left click, place wall
            
            int type = parent.getNodeTypeAt(w, h);
            if(type == Node.WALL) {
                parent.setNodeTypeAt(w, h, Node.OPEN);
            }
            else if(type == Node.START) {
                //set the start coords to uninitialized
                parent.setStartCoords(-1, -1);
                parent.setNodeTypeAt(w, h, Node.OPEN);
                
                //change the PathButtonState
                parent.setpButtonState(PathButtonState.NOSTART);
            }
            else if(type == Node.END) {
                //set the end coords to uninitialized
                parent.setEndCoords(-1, -1);
                parent.setNodeTypeAt(w, h, Node.OPEN);
                
                //change the PathButtonState
                parent.setpButtonState(PathButtonState.NOEND);
            }
            else if(type == Node.OPEN) {
                parent.setNodeTypeAt(w, h, Node.WALL);
            }
            
            parent.repaint();
        }
        else if(button == MouseEvent.BUTTON3) {
            //right click, show jpopupmenu for start or finish
            
            //move the jpopupmenu to this location and set to visible
            menuMove(x, y);
            startListener.setCurNode(w, h);
            endListener.setCurNode(w, h);
            menu.setVisible(true);
        }
        else {
            //wheel click
        }
    }
    
    //internal method to move the menu to the right place
    private void menuMove(int x, int y) {
        Point parentLocation = parent.getFrameLocation();
        Point newLocation = new Point(x + parentLocation.x, y + parentLocation.y);
        menu.setLocation(newLocation);
    }
    
    //not important methods
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
}
