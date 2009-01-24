/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sim;

import java.util.Collection;

/**
 *
 * @author Stephen
 */
public abstract class Component implements SimItem {

    public abstract String getDescription();

    public abstract Collection<InputPin> getInputs();

    public abstract String getName();
    
    public abstract Collection<OutputPin> getOutputs();

    public abstract void setDescription(String description);

}
