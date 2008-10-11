/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ui.tools;

import sim.Component;

/**
 *
 * @author Matt
 */
public class Wire extends SelectableComponent {

    public Wire(Component component, int x, int y) {
        super(component, x, y);
    }

    @Override
    protected void setDefaultImage() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void setSelectedImage() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void setActiveImage() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
