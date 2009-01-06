/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import ui.tools.SelectableComponent;

/**
 *
 * @author matt
 */
public class PreviewPanel extends JPanel{
    private String componentName = null;
    private Editor editor;

    public PreviewPanel(Editor editor){
        this.editor = editor;
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        SelectableComponent sc = null;
        if(componentName!=null){
            try {
                sc = (SelectableComponent) editor.getNetlistComponent(componentName).getConstructor(Point.class).newInstance((Point)null);
            } catch (InstantiationException ex) {
                Logger.getLogger(CircuitPanel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(CircuitPanel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(CircuitPanel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(CircuitPanel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchMethodException ex) {
                Logger.getLogger(CircuitPanel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                Logger.getLogger(CircuitPanel.class.getName()).log(Level.SEVERE, null, ex);
            }

            g.setColor(UIConstants.CIRCUIT_BACKGROUND_COLOUR);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.translate((int)(getWidth() - sc.getWidth())/2, (int)(getHeight() - sc.getHeight())/2);
            sc.draw((Graphics2D) g, null);
        }
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }
}
