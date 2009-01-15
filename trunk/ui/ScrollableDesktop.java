/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.Hashtable;
import javax.swing.*;

/**
 *  From: http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4129109
 *
 */
public class ScrollableDesktop extends JDesktopPane
  {
    
    private Hashtable listeners = new Hashtable();
 
    /**
     * Set the preferred size of the desktop to the right-bottom-corner of the
     * internal-frame with the &quot;largest&quot; right-bottom-corner.
     *
     * @return The preferred desktop dimension.
     */
    @Override
    public Dimension getPreferredSize()
    {
      JInternalFrame [] array = this.getAllFrames();
      int maxX = 0;
      int maxY = 0;
      for (int i = 0; i < array.length; i++)
      {
        int x = array[i].getX() + array[i].getWidth();
        if (x > maxX) { maxX = x; }
        int y = array[i].getY() + array[i].getHeight();
        if (y > maxY) { maxY = y; }
      }
      return new Dimension(maxX, maxY);
    }
    /**
     * Add an internal-frame to the desktop. Sets a component-listener on it,
     * which resizes the desktop if a frame is resized.
     */
    @Override
    @SuppressWarnings("unchecked")
    public void add(Component comp, Object constraints)
    {
      super.add(comp, constraints);
      ComponentListener listener = new ComponentListener()
        {
          public void componentResized(ComponentEvent e)
          {                               // Layout the JScrollPane
            if(getParent() != null && getParent().getParent() != null){ // Bug workaround
                getParent().getParent().validate();
            }
          }
          public void componentMoved(ComponentEvent e)
          {
            componentResized(e);
          }
          public void componentShown(ComponentEvent e) {}
          public void componentHidden(ComponentEvent e) {}
        };
      comp.addComponentListener(listener);
      if(listeners==null){ listeners = new Hashtable();}
      listeners.put(comp, listener);
    }
    /**
     * Remove an internal-frame from the desktop. Removes the
component-listener
     * and resizes the desktop.
     */
    @Override
    public void remove(Component comp)
    {
      comp.removeComponentListener((ComponentListener) listeners.get(comp));
      super.remove(comp);
      getParent().getParent().validate(); // Layout the JScrollPane
    }
  }
