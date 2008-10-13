/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ui;

import java.awt.Point;
import ui.tools.SelectionType;
import ui.tools.SelectableComponent;
import ui.tools.UITool;
import ui.tools.AndGate2Input;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import ui.tools.Wire;


/**
 *
 * @author Matt
 */
class CircuitPanel extends JPanel {
    
    private int frameOriginX, frameOriginY;
    private Point circuitPoint;
    private BufferedImage bi;
    private Stack<SelectableComponent> drawnComponents = new Stack<SelectableComponent>();
    private UITool currentTool = UITool.Select;
    private SelectableComponent selectedComponent, temporaryComponent;
    private boolean nowDraging = false;
    private boolean drawingWire;
    private Point wireStart;
    private Point wireEnd;
    
    public CircuitPanel(){
        frameOriginX = this.getX();
        frameOriginY = this.getY();               
        
        addMouseMotionListener(new MouseMotionAdapter(){  
            
            @Override
            public void mouseMoved(MouseEvent e) {
               
                // Find the location in the circuit
                circuitPoint = snapPointToGrid(new Point(e.getX()-frameOriginX,e.getY()-frameOriginY));
                
                // Moving a non-fixed new component around
                if(!drawnComponents.isEmpty() && !drawnComponents.peek().isFixed()){
                    drawnComponents.peek().translate(circuitPoint, false);
                    drawnComponents.peek().mouseMoved(e);
                    repaint();
                    
                // Hover highlights    
                } else if (bi!=null && getCurrentTool().equals(UITool.Select)){
                                        
                    if(!nowDraging){
                        selectedComponent = null;
                        
                        // Determine which component the mouse lies in
                        for(SelectableComponent sc: drawnComponents){
                        
                            if(sc.containsPoint(circuitPoint)){
                                selectedComponent = sc;
                            }
                            
                            // Reset non-active components
                            if(!sc.getSelectionType().equals(SelectionType.ACTIVE)){
                                sc.setSelectionType(SelectionType.DEFAULT);
                            }
                        }       
                    
                        // Allow the component to decide how it highlights itself
                        selectedComponent.mouseMoved(e);                       
                    }
                   
                    repaint();
                                       
                } 
            }                   
            
            @Override
            public void mouseDragged(MouseEvent e) {
                
                // Find the location in the circuit
                circuitPoint = snapPointToGrid(new Point(e.getX()-frameOriginX,e.getY()-frameOriginY));
                        
                //
                if(bi!=null && !getCurrentTool().equals(UITool.Select)){
                    
                    repaint();
                  
                //    
                } else if(getCurrentTool().equals(UITool.Select) && selectedComponent != null){

                    if(!nowDraging){
                        drawnComponents.remove(selectedComponent);
                        selectedComponent.mouseDragged(e);
                        nowDraging = true;
                    }                   
                    
                    repaint();
                }
                //} else if(getCurrentTool().equals(UITool.Wire)){
                    //drawingWire = true;       
                    //wireEnd = snapPointToGrid(e.getPoint());
                  //  
                    //repaint();
                //}
                         
            }
        });
        
        addMouseListener(new MouseListener(){

            public void mouseClicked(MouseEvent e) {
                
                // Find the location in the circuit
                circuitPoint = snapPointToGrid(new Point(e.getX()-frameOriginX,e.getY()-frameOriginY));          
                
                // Area we clicking empty space?
                boolean clickingEmptySpace = true;
                for(SelectableComponent sc: drawnComponents){

                    if(sc.containsPoint(circuitPoint)){
                        clickingEmptySpace = false;
                        break;
                    } 
                }  

                // Reset all selections
                if(clickingEmptySpace){
                    
                   for(SelectableComponent sc: drawnComponents){
                        sc.setSelectionType(SelectionType.DEFAULT);
                        selectedComponent = null;
                   }  
                   
                   repaint();
                }
            }
            public void mouseEntered(MouseEvent e) {}
            public void mouseExited(MouseEvent e) {}

            public void mousePressed(MouseEvent e) {
                
                 // Find the location in the circuit
                circuitPoint = snapPointToGrid(new Point(e.getX()-frameOriginX,e.getY()-frameOriginY));
                               
                if(selectedComponent != null){
                   
//                   if(selectedComponent.getSelectionType().equals(SelectionType.ACTIVE)){
//                       
//                       selectedComponent.setSelectionType(SelectionType.SELECTED);
//                       selectedComponent = null;
//                       
//                   } else {
//
//                       for(SelectableComponent sc: drawnComponents){
//                            if(sc.getSelectionType().equals(SelectionType.ACTIVE)){
//                                sc.setSelectionType(SelectionType.DEFAULT);
//                            }
//                        }  
//
//                       selectedComponent.setSelectionType(SelectionType.ACTIVE);       
//                   }
                    
                     selectedComponent.mousePressed(e);                          
                
                     repaint();
                }
                
//                if(currentTool.equals(UITool.Wire)){
//                    wireStart = snapPointToGrid(e.getPoint());
//                }

                
               
            }

            public void mouseReleased(MouseEvent e) {
                
                // Find the location in the circuit
                circuitPoint = snapPointToGrid(new Point(e.getX()-frameOriginX,e.getY()-frameOriginY));
                                
                if(!currentTool.equals(UITool.Select) && !nowDraging){
                    switch(currentTool){
                        case Wire:
                            //drawnComponents.push(
                            //        new Wire(null, wireStart, wireEnd));    
                            break;
                        case AndGate2Input:                    
                            drawnComponents.peek().translate(circuitPoint, true);
                            drawnComponents.peek().mouseMoved(e);
                            selectTool(UITool.AndGate2Input);
                            break;
                        default:
                            break;
                    }
                    drawnComponents.peek().translate(circuitPoint, true);
                                     
                } else if(nowDraging){
                    drawnComponents.push(selectedComponent);
                    selectedComponent.translate(circuitPoint, true);
                    selectedComponent.setSelectionType(SelectionType.ACTIVE);
                    nowDraging = false;
                }
                
                drawingWire = false;
                
                repaint();
            }
            
        });
        
                
    }

    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);        
        
        // Background Colour
        g.setColor(UIConstants.CIRCUIT_BACKGROUND_COLOUR);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        
        // Grid Snap Dots
        g.setColor(UIConstants.GRID_DOT_COLOUR);
        for(int i =  0; i< this.getWidth(); i+=UIConstants.GRID_DOT_SPACING){
            for(int j = 0; j < this.getHeight(); j+=UIConstants.GRID_DOT_SPACING){
                g.fillRect(i, j, 1, 1);
            }
        }
                
        // Draw previous components
        for(SelectableComponent sc: drawnComponents){
            sc.draw(g, this);
        }
        
        // Draw current temporary component      
//        if(bi!=null && !getCurrentTool().equals(UITool.Select)){
//        
//            g.translate(-bi.getWidth()/2, -bi.getHeight()/2);            
//            //g.drawImage(bi, (int)circuitPoint.getX(), (int)circuitPoint.getY(), this);            
//            temporaryComponent.moveBy(circuitPoint, false);
//            temporaryComponent.draw(g, this);
//            g.translate(bi.getWidth()/2, bi.getHeight()/2); 
//            
//        } else 
            
            if (nowDraging){
            
            g.translate(-selectedComponent.getWidth()/2, -selectedComponent.getHeight()/2);          
            //g.drawImage(selectedComponent.getActiveImage(), circuitX , circuitY , this);  
            selectedComponent.translate(circuitPoint, false);
            selectedComponent.draw(g, this);
            g.translate(-selectedComponent.getWidth()/2, -selectedComponent.getHeight()/2); 
            
        }
        
        // Draw temporary wire
//        if(drawingWire){
//            g.setColor(UIConstants.WIRE_COLOUR);
//            g.drawLine(wireStart.x, wireStart.y, wireStart.x, wireEnd.y);
//            g.drawLine(wireStart.x, wireEnd.y, wireEnd.x, wireEnd.y);
//        }
                        
    }
            
    public void selectTool(UITool tool){
//        try {
            switch(tool){
                case Wire:
                    drawnComponents.push(new Wire(null, circuitPoint, null));
                    break;
                case AndGate2Input:
                    drawnComponents.push(new AndGate2Input(null, circuitPoint));
                    break;
                default:
                    break;
            }
            this.currentTool = tool;            
//        } catch (IOException ex) {
//            Logger.getLogger(AndGate2Input.class.getName()).log(Level.SEVERE, null, ex);
//        }
        
    }
    
    public UITool getCurrentTool(){
        return this.currentTool;
    }
    
    public boolean hasActiveSelection(){
        for(SelectableComponent sc: drawnComponents){
            if(sc.getSelectionType().equals(SelectionType.ACTIVE)){
                return true;
            }
        }
        
        return false;
    }
    
    public String deleteActiveComponent(){
        
        SelectableComponent ac = null;
        for(SelectableComponent sc: drawnComponents){
            if(sc.getSelectionType().equals(SelectionType.ACTIVE)){
                ac = sc;
            }
        }
        
        if(ac!=null){
            drawnComponents.remove(ac);
            selectedComponent = null;
            repaint();

            return ac.getName()+" deleted.";
        } else {
            return "";
        }
    }
    
    public String resetCircuit(){
        drawnComponents.clear();
        selectedComponent = null;
        nowDraging = false;
        
        repaint();
        
        return "Circuit cleared.";
    }
    
    private Point snapPointToGrid(Point old){
        if(UIConstants.SNAP_TO_GRID){
            return new Point((int) (old.x / UIConstants.GRID_DOT_SPACING)*UIConstants.GRID_DOT_SPACING,
                    (int) (old.y / UIConstants.GRID_DOT_SPACING)*UIConstants.GRID_DOT_SPACING);
        } else {
            return old;
        }                
    }

}
