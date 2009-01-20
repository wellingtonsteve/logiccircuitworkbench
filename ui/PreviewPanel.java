/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import netlist.standard.Wire;
import ui.tools.SelectableComponent;
import ui.tools.SelectionState;

/**
 *
 * @author matt
 */
public class PreviewPanel extends JPanel{
    private SelectableComponent sc = null;
    private Editor editor;

    public PreviewPanel(Editor editor){
        this.editor = editor;
    }
    
    @Override
    public void paintComponent(Graphics g){      
        super.paintComponent(g);
        
        if(!(sc instanceof Wire)){
            g.setColor(UIConstants.CIRCUIT_BACKGROUND_COLOUR);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.translate(-sc.getOrigin().x, -sc.getOrigin().y);
            g.translate((int)(getWidth() - sc.getWidth())/2, (int)(getHeight() - sc.getHeight())/2);

            sc.draw((Graphics2D) g);

            g.translate(-(int)(getWidth() - sc.getWidth())/2, (int)-(getHeight() - sc.getHeight())/2);
            g.translate(sc.getOrigin().x, sc.getOrigin().y);
        }
    }

    public void setComponent(SelectableComponent sc) {
        this.sc = sc.copy();
        this.sc.setSelectionState(SelectionState.DEFAULT);
    }
}
