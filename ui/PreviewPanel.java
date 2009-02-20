package ui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import ui.components.Wire;
import ui.components.SelectableComponent;
import ui.components.SelectionState;

/**
 * This panel is simply responsible for displaying a preview of a new component
 * or a currently selected component of the active circuit.
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
        
        if(sc!=null && !(sc instanceof Wire)){
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
