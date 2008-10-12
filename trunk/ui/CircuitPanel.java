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
    
    private int circuitX, circuitY, frameOriginX, frameOriginY;
    private BufferedImage bi;
    private Stack<SelectableComponent> drawnComponents = new Stack<SelectableComponent>();
    private UITool currentTool = UITool.Select;
    private SelectableComponent selectedComponent;
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
                
                circuitX = e.getX()-frameOriginX;                    
                circuitY = e.getY()-frameOriginY;

                if(UIConstants.SNAP_TO_GRID){
                  circuitX = (circuitX / UIConstants.GRID_DOT_SPACING) * UIConstants.GRID_DOT_SPACING;
                  circuitY = (circuitY / UIConstants.GRID_DOT_SPACING) * UIConstants.GRID_DOT_SPACING;
                }
                
                // Moving a non-fixed new component around
                if(bi!=null && !getCurrentTool().equals(UITool.Select)){
                   
                    repaint();
                    
                // Hover highlights    
                } else if (bi!=null && getCurrentTool().equals(UITool.Select)){
                                        
                    if(!nowDraging){
                        selectedComponent = null;
                        
                        // Determine which component the mouse lies in
                        for(SelectableComponent sc: drawnComponents){
                        
                            if(sc.getArea().contains(circuitX, circuitY)){
                                selectedComponent = sc;
                            }
                            
                            // Reset non-active components
                            if(!sc.getSelectionType().equals(SelectionType.ACTIVE)){
                                sc.setSelectionType(SelectionType.DEFAULT);
                            }
                        }       
                    
                        // Mark the selected component if not already active
                        if(selectedComponent != null && !selectedComponent.getSelectionType().equals(SelectionType.ACTIVE)){

                            selectedComponent.setSelectionType(SelectionType.SELECTED);
                        }                        
                    }
                   
                    repaint();
                                       
                } 
            }                   
            
            @Override
            public void mouseDragged(MouseEvent e) {
                // TODO: Use Point instead of individual co-ordinates
                circuitX = e.getX()-frameOriginX;                    
                circuitY = e.getY()-frameOriginY;

                if(UIConstants.SNAP_TO_GRID){
                  circuitX = (circuitX / UIConstants.GRID_DOT_SPACING) * UIConstants.GRID_DOT_SPACING;
                  circuitY = (circuitY / UIConstants.GRID_DOT_SPACING) * UIConstants.GRID_DOT_SPACING;
                }           
                
                if(bi!=null && !getCurrentTool().equals(UITool.Select)){
                    
                    repaint();
                    
                } else if(getCurrentTool().equals(UITool.Select) && selectedComponent != null){

                    if(!nowDraging){
                        drawnComponents.remove(selectedComponent);
                        nowDraging = true;
                    }                   
                    
                    repaint();
                } else if(getCurrentTool().equals(UITool.Wire)){
                    drawingWire = true;       
                    wireEnd = snapPointToGrid(e.getPoint());
                    
                    repaint();
                }
                         
            }
        });
        
        addMouseListener(new MouseListener(){

            public void mouseClicked(MouseEvent e) {
                
                // Area we clicking empty space?
                boolean clickingEmptySpace = true;
                for(SelectableComponent sc: drawnComponents){

                    if(sc.getArea().contains(circuitX, circuitY)){
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
                
                circuitX = e.getX()-frameOriginX;                    
                circuitY = e.getY()-frameOriginY;
                
                if(selectedComponent != null){
                   
                   if(selectedComponent.getSelectionType().equals(SelectionType.ACTIVE)){
                       
                       selectedComponent.setSelectionType(SelectionType.SELECTED);
                       selectedComponent = null;
                       
                   } else {

                       for(SelectableComponent sc: drawnComponents){
                            if(sc.getSelectionType().equals(SelectionType.ACTIVE)){
                                sc.setSelectionType(SelectionType.DEFAULT);
                            }
                        }  

                       selectedComponent.setSelectionType(SelectionType.ACTIVE);       
                   }
                
                   repaint();
                }
                
                if(currentTool.equals(UITool.Wire)){
                    wireStart = snapPointToGrid(e.getPoint());
                }

                
               
            }

            public void mouseReleased(MouseEvent e) {
                circuitX = e.getX()-frameOriginX;
                circuitY = e.getY()-frameOriginY;

                if(UIConstants.SNAP_TO_GRID){
                  circuitX = (circuitX / UIConstants.GRID_DOT_SPACING) * UIConstants.GRID_DOT_SPACING;
                  circuitY = (circuitY / UIConstants.GRID_DOT_SPACING) * UIConstants.GRID_DOT_SPACING;
                }
                
                if(!currentTool.equals(UITool.Select) && !nowDraging){
                    switch(currentTool){
                        case Wire:
                            drawnComponents.push(
                                    new Wire(null, wireStart, wireEnd));    
                            break;
                        case AndGate2Input:
                            drawnComponents.push(
                                    new AndGate2Input(null, circuitX-(bi.getWidth()/2), circuitY-(bi.getHeight()/2)));    
                            break;
                        default:
                            break;
                    }
                                     
                } else if(nowDraging){
                    drawnComponents.push(selectedComponent);
                    selectedComponent.setPos(circuitX-(selectedComponent.getWidth()/2),circuitY-(selectedComponent.getHeight()/2));
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
                        
            // Highlighted component? 
            switch(sc.getSelectionType()){
                case ACTIVE:
                    g.drawImage(sc.getActiveImage(), sc.getX(),  sc.getY(), this);
                    break;
                case SELECTED:
                    g.drawImage(sc.getSelectedImage(), sc.getX(),  sc.getY(), this);
                    break;
                default:
                    g.drawImage(sc.getDefaultImage(), sc.getX(),  sc.getY(), this);
            }
        }
        
        // Draw current temporary component      
        if(bi!=null && !getCurrentTool().equals(UITool.Select)){
        
            g.translate(-bi.getWidth()/2, -bi.getHeight()/2);            
            g.drawImage(bi, circuitX , circuitY , this);            
            g.translate(bi.getWidth()/2, bi.getHeight()/2); 
            
        } else if (nowDraging){
            
            g.translate(-selectedComponent.getWidth()/2, -selectedComponent.getHeight()/2);          
            g.drawImage(selectedComponent.getActiveImage(), circuitX , circuitY , this);  
            g.translate(-selectedComponent.getWidth()/2, -selectedComponent.getHeight()/2); 
            
        }
        
        // Draw temporary wire
        if(drawingWire){
            g.setColor(UIConstants.WIRE_COLOUR);
            g.drawLine(wireStart.x, wireStart.y, wireStart.x, wireEnd.y);
            g.drawLine(wireStart.x, wireEnd.y, wireEnd.x, wireEnd.y);
        }
                        
    }
            
    public void selectTool(UITool tool){
        try {
            switch(tool){
                case Wire:
                    bi = null;
                    break;
                case AndGate2Input:
                    bi = ImageIO.read(new File("build/classes/ui/images/components/default_andgate.png"));
                    break;
                default:
                    break;
            }
            this.currentTool = tool;
            
        } catch (IOException ex) {
            Logger.getLogger(AndGate2Input.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public UITool getCurrentTool(){
        return this.currentTool;
    }
    
    public boolean hasActiveSelection(){
        boolean retval = false;
        for(SelectableComponent sc: drawnComponents){
            if(sc.getSelectionType().equals(SelectionType.ACTIVE)){
                retval = true;
            }
        }
        
        return retval;
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
