
package sim.joinable;

import java.util.*;
import java.util.ArrayList;
import ui.error.ErrorHandler;

/**
 * Joinable is an abstract class that represents a object in the simulation can be connected to a
 * second Joinable.  The two direct subclasses of it are Pin and Wire.  This allows pins on
 * components to be connected directly, or via wires, or even for wires to be connected to each
 * other.  Joinables remember which other Joinables they are connected to, forming a graph of
 * connected Joinables.
 */
public abstract class Joinable {

    /**
     * A set of other Joinables that this Joinable is connected directly to. For example a wire W
     * might have two pins (A and B) touching it different places. Each Joinable's set of
     * connections would be as follows:
     * W - {A, B}
     * A - {W}
     * B - {W}
     * If both pins were touching the same point on the wire, A and B would also have each other in
     * their sets
     */
    public Collection<Joinable> connectedToSet = new HashSet<Joinable>();

    /**
     * A reference to an OutputPin (a subclass of Joinable), that this Joinable is indirectly
     * connected to in the graph.  In the example above, if A is an OutputPin and B and InputPin,
     * both W and B would point to A here. In a group of graph nodes that are directly or indirectly
     * connected to each other there can be only one OutputPin. If there is not one at all then
     * outputSource is null.
     */
    public OutputPin outputSource;

    /**
     * This static method takes any two Joinable and attempts to connect them together.  This fails
     * if doing so would result in directly or indirectly connecting two OutputPins together.
     */
    public static void connect(Joinable a, Joinable b){
        //Call the canConnect method below to 
        if(canConnect(a, b)){
            a.connectedToSet.add(b);
            b.connectedToSet.add(a);
            if(a.outputSource != null){
                b.setOutputSource(a.outputSource, new ArrayList<Joinable>());
            } else if(b.outputSource != null){
                a.setOutputSource(b.outputSource, new ArrayList<Joinable>());
            }
        } else {
            ErrorHandler.newError("Simulation Error", "Cannot connect two outputs together.");
        }
    }

    public static void disconnect(Joinable a, Joinable b){
        //System.out.println(a + " disconnected from " + b);
        a.connectedToSet.remove(b);
        b.connectedToSet.remove(a);
        if(!a.canFind(a.outputSource, new ArrayList<Joinable>())) {
            a.setOutputSource(null, new ArrayList<Joinable>());
        }
        if(!b.canFind(b.outputSource, new ArrayList<Joinable>())) {
            b.setOutputSource(null, new ArrayList<Joinable>());
        }
    }

    public static boolean canConnect(Joinable a, Joinable b){
        return a.outputSource == null || b.outputSource == null || a.outputSource == b.outputSource;
    }

    public boolean canFind(Joinable joinable, ArrayList<Joinable> visited){
        if(this == joinable) return true;
        visited.add(this);
        boolean found = false;
        for(Joinable connectedJoinable : connectedToSet){
            if(!visited.contains(connectedJoinable)){
                found = found || connectedJoinable.canFind(joinable, visited);
            }
        }
        return found;
    }

    public void setOutputSource(OutputPin joinable, ArrayList<Joinable> visited){
        outputSource = joinable;
        visited.add(this);
        for(Joinable connectedJoinable : connectedToSet){
            if(!visited.contains(connectedJoinable)){
                connectedJoinable.setOutputSource(joinable, visited);
            }
        }
    }
}
