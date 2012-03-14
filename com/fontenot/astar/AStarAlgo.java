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
import com.fontenot.astar.Node;

import java.util.PriorityQueue;
import java.util.ArrayList;

/* Runs the A* algorithm */
public class AStarAlgo {
    private static final double ORTHCOST = 1;
    private static final double DIAGCOST = Math.sqrt(2);
    
    public static final int MANHATTANHEUR = 0;
    public static final int DIAGHEUR = 1;
    
    private PriorityQueue<Node> openList;
    private ArrayList<Node> closedList;
    
    private Node[][] field;
    private Node startNode;
    private Node endNode;
    
    private int startW;
    private int startH;
    private int endW;
    private int endH;
    
    private int heuristic;
    
    public AStarAlgo(Node[][] field, int startW, int startH, int endW, int endH) {
        this.field = field;
        this.startW = startW;
        this.startH = startH;
        this.endW = endW;
        this.endH = endH;
        
        startNode = field[startW][startH];
        endNode = field[endW][endH];
        
        heuristic = MANHATTANHEUR;
        
        openList = new PriorityQueue<Node>();
        closedList = new ArrayList<Node>();
    }
    
    public void setHeuristic(int heur) {
        if(heur == MANHATTANHEUR) {
            heuristic = MANHATTANHEUR;
        }
        else if(heur == DIAGHEUR) {
            heuristic = DIAGHEUR;
        }
        else {
            System.out.println("Using default heuristic, non-existant one chosen");
        }
    }
    
    //thanks a lot to this site: http://www.policyalmanac.org/games/aStarTutorial.htm
    public void findPath() {
        //System.out.println("startW = " + startW + " startH = " + startH + " endW = " + endW + " endH = " + endH);
        startNode.setCosts(0.0, calcHeuristic(startW, startH));
        openList.add(startNode);
        
        while(openList.size() != 0 && !closedList.contains(endNode)) {
            //move the lowest f score square to the closed list
            Node currentNode = openList.poll();
            closedList.add(currentNode);
            
            int curW = currentNode.getW();
            int curH = currentNode.getH();
            for(int i = -1; i <= 1; i++) {
                for(int j = -1; j <= 1; j++) {
                    if(i == 0 && j == 0) {
                        //don't process self
                    }
                    else {
                        //process neighbor

                        processNeighbor(i, j, curW, curH, currentNode);
                    }
                }
            }
        }
    }
    
    /* HELPER METHODS FOR FINDPATH */
    private void processNeighbor(int i, int j, int curW, int curH, Node currentNode) {
        if(neighborOnBoard(i, j, curW, curH)) {
            //System.out.println("curW = " + curW + " curH = " + curH);
            Node neighbor = field[curW + i][curH + j];
            
            if(neighbor.getType() != Node.WALL) {
                if(!closedList.contains(neighbor)) {
                    isNotOnClosedList(i, j, curW, curH, currentNode, neighbor);
                }
            }
        }
    }
    
    //returns true if the neighbor is on the board
    private boolean neighborOnBoard(int i, int j, int curW, int curH) {
        int newW = curW + i;
        int newH = curH + j;
        if(newW < NodeGraphPanel.FIELDW && newW >= 0) {
            if(newH < NodeGraphPanel.FIELDH && newH >= 0) {
                return true;
            }
        }
        
        return false;
    }
    
    private void isNotOnClosedList(int i, int j, int curW, int curH, Node currentNode, Node currentNeighbor) {
        if(!openList.contains(currentNeighbor)) {
            //add to open list and record scores
             
            double newGScore = currentNode.getGScore();
            if(i != j) {
                //diagonal move
                newGScore += DIAGCOST;
            }
            else {
                //orthogonal move
                newGScore += ORTHCOST;
            }

            //add to open list
            currentNeighbor.setCosts(newGScore, calcHeuristic(curW + i, curH + j));
            currentNeighbor.setParent(currentNode);
            openList.add(currentNeighbor);
            //System.out.println("Added neighbor at w = " + (curW + i) + " h = " + (curH + j));
        }
        else {
            //check to see if this path is better
            if(currentNeighbor.getGScore() > currentNode.getGScore()) {
                //this gscore is better so remove it and add it again to resort
                //currentNode.setParent(currentNeighbor);
                currentNeighbor.setParent(currentNode);
                                            
                double newGScore = currentNeighbor.getGScore();
                if(i != j) {
                    newGScore += ORTHCOST;
                }
                else {
                    newGScore += DIAGCOST;
                }
                currentNode.setCosts(newGScore, calcHeuristic(curW + i, curH + j));
                                            
                //resort the open list
                openList.remove(currentNode);
                openList.add(currentNode);
            }
        }
    }
    /* END */
    
    private double calcHeuristic(int w, int h) {
        if(heuristic == MANHATTANHEUR) {
            return manhattanHeuristic(w, h);
        }
        else if(heuristic == DIAGHEUR) {
            return diagShortcutHeuristic(w, h);
        }
        else {
            System.out.println("error in AStarAlgo heuristic calculation");
            return 0.0;
        }
    }
    
    //thanks to these sites for: http://www.policyalmanac.org/games/heuristics.htm
    private double manhattanHeuristic(int w, int h) {
        return ORTHCOST * (Math.abs(w - endW) + Math.abs(h - endH));
    }
    
    private double diagShortcutHeuristic(int w, int h) {
        double xDist = Math.abs(w - endW);
        double yDist = Math.abs(h - endH);
        
        if(xDist > yDist) {
            return (14 * yDist) + (10 * (xDist - yDist));
        }
        else {
            return (14 * xDist) + (10 * (yDist - xDist));
        }
    }
}
