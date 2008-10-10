/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import javax.imageio.ImageIO;
import javax.swing.JPanel;


/**
 *
 * @author Matt
 */
class CircuitPanel extends JPanel {
    
    private int circuitX, circuitY, frameOriginX, frameOriginY;
    private BufferedImage bi;
    private Map<String,String> componentImageLoc = new HashMap<String,String>();
    private Stack<SelectableComponent> drawnComponents = new Stack<SelectableComponent>();
    private String currentDrawingComponent = "Select";
    private SelectableComponent selectedComponent;
    private boolean nowDraging = false;
    
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
                
                if(bi!=null && !currentDrawingComponent.equals("Select")){
                   
                    repaint();
                    
                } else if (bi!=null && currentDrawingComponent.equals("Select")){
                                        
                    if(!nowDraging){
                        selectedComponent = null;
                        for(SelectableComponent sc: drawnComponents){
                        
                            if(!sc.getSelectionType().equals(SelectionType.ACTIVE)){
                                sc.setSelectionType(SelectionType.DEFAULT);
                            }

                            if(sc.getArea().contains(circuitX, circuitY)){
                                selectedComponent = sc;
                            }
                        }       
                    
                        if(selectedComponent != null && !selectedComponent.getSelectionType().equals(SelectionType.ACTIVE)){

                            selectedComponent.setSelectionType(SelectionType.SELECTED);
                        }                        
                    }
                   
                    repaint();
                                       
                } 
            }                   
            
            @Override
            public void mouseDragged(MouseEvent e) {
                
                circuitX = e.getX()-frameOriginX;                    
                circuitY = e.getY()-frameOriginY;

                if(UIConstants.SNAP_TO_GRID){
                  circuitX = (circuitX / UIConstants.GRID_DOT_SPACING) * UIConstants.GRID_DOT_SPACING;
                  circuitY = (circuitY / UIConstants.GRID_DOT_SPACING) * UIConstants.GRID_DOT_SPACING;
                }           
                
                if(bi!=null && !currentDrawingComponent.equals("Select")){
                    
                    repaint();
                    
                } else if(currentDrawingComponent.equals("Select") && selectedComponent != null){

                    if(!nowDraging){
                        drawnComponents.remove(selectedComponent);
                        nowDraging = true;
                    }                   
                    
                    repaint();
                }    
                         
            }
        });
        
        addMouseListener(new MouseListener(){

            public void mouseClicked(MouseEvent e) {
               
            }

            public void mousePressed(MouseEvent e) {
                if(selectedComponent != null){
                   if(selectedComponent.getSelectionType().equals(SelectionType.ACTIVE)){
                       selectedComponent.setSelectionType(SelectionType.SELECTED);
                   } else {
                       
                       for(SelectableComponent sc: drawnComponents){
                            if(sc.getSelectionType().equals(SelectionType.ACTIVE)){
                                sc.setSelectionType(SelectionType.DEFAULT);
                            }
                        }  
                       
                       selectedComponent.setSelectionType(SelectionType.ACTIVE);       
                       
                       System.out.println(selectedComponent.getArea().toString());
                   }
               }
               repaint();
            }

            public void mouseReleased(MouseEvent e) {
                circuitX = e.getX()-frameOriginX;
                circuitY = e.getY()-frameOriginY;

                if(UIConstants.SNAP_TO_GRID){
                  circuitX = (circuitX / UIConstants.GRID_DOT_SPACING) * UIConstants.GRID_DOT_SPACING;
                  circuitY = (circuitY / UIConstants.GRID_DOT_SPACING) * UIConstants.GRID_DOT_SPACING;
                }
                
                // TODO: Replace null here with actual component object
                if(!currentDrawingComponent.equals("Select")){
                    if(!nowDraging){
                        drawnComponents.push(new SelectableComponent(null, circuitX-(bi.getWidth()/2), circuitY-(bi.getHeight()/2), bi));
                    }                         
                } else if(nowDraging){
                    drawnComponents.push(selectedComponent);
                    selectedComponent.setPos(circuitX-(selectedComponent.getWidth()/2),circuitY-(selectedComponent.getHeight()/2));
                    selectedComponent.setSelectionType(SelectionType.ACTIVE);
                    nowDraging = false;
                }
                
                repaint();
            }

            public void mouseEntered(MouseEvent e) {
                
            }

            public void mouseExited(MouseEvent e) {
                
            }
            
        });
        
        
        registerComponents();
        
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
         
        int w = UIConstants.SELECTION_MARKER_WIDTH;
                
        // Draw previous components
        for(SelectableComponent sc: drawnComponents){
            g.drawImage(sc.getBufferedImage(), sc.getX(),  sc.getY(), this);
            
            // Highlighted component? 
             if(sc.getSelectionType().equals(SelectionType.ACTIVE)){    
                g.setColor(Color.BLUE);
             } if (sc.getSelectionType().equals(SelectionType.SELECTED)){    
                g.setColor(Color.RED);
             }             
             if (!sc.getSelectionType().equals(SelectionType.DEFAULT)){

                // Draw Highlight 
                g.drawRect(sc.getX(), sc.getY(), sc.getWidth(), sc.getHeight());
                g.fillRect((int) sc.getArea().getMinX()-(w/2), (int) sc.getArea().getMinY()-(w/2), w, w);
                g.fillRect((int) sc.getArea().getMaxX()-(w/2), (int) sc.getArea().getMinY()-(w/2), w, w);
                g.fillRect((int) sc.getArea().getMinX()-(w/2), (int) sc.getArea().getMaxY()-(w/2), w, w);
                g.fillRect((int) sc.getArea().getMaxX()-(w/2), (int) sc.getArea().getMaxY()-(w/2), w, w); 
                
            }
        }
        
        // Draw current temp component      
        if(bi!=null && !currentDrawingComponent.equals("Select")){
        
            g.translate(-bi.getWidth()/2, -bi.getHeight()/2);            
            g.drawImage(bi, circuitX , circuitY , this);            
            g.translate(bi.getWidth()/2, bi.getHeight()/2); 
            
        } else if (nowDraging){
            g.translate(-selectedComponent.getWidth()/2, -selectedComponent.getHeight()/2);          
            g.drawImage(selectedComponent.getBufferedImage(), circuitX , circuitY , this);  
            g.translate(-selectedComponent.getWidth()/2, -selectedComponent.getHeight()/2);          
        }
        
                
    }
    
    public void addComponent(String name, String loc){
        componentImageLoc.put(name, loc);
    }
    
    private void registerComponents(){
        addComponent("Select","build/classes/ui/images/components/select.png");
        addComponent("AndGate","build/classes/ui/images/components/andgate.png");
        // Etc...
    }
    
    public void selectComponent(String name){
        this.currentDrawingComponent = name;
        String loc = componentImageLoc.get(name); 
        if(loc!=null){
            try {
                bi = ImageIO.read(new File(loc));
            } catch (IOException ex) {
                //Logger.getLogger(CircuitPanel.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
            }
        }
    }
    

}
