
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
     * This static method takes any two Joinables and attempts to connect them together.  This fails
     * if doing so would result in directly or indirectly connecting two OutputPins together.
     */
    public static void connect(Joinable a, Joinable b){
        //Call the canConnect method below to ensure the connection is legal
        if(canConnect(a, b)){
            //If so, add the Joinables to each other's connected set
            a.connectedToSet.add(b);
            b.connectedToSet.add(a);
            /**
             * One of the two Joinables that we are connecting may be an OutputPin or may be
             * indirectly connected to one.  If this is the case for one of the Joinables, its
             * outputSource reference will be non null, and we pass that reference to the
             * setOutputSource method of the other Joinable.  This starts a depth first traversal of
             * the graph of connections, updating all Joinables that are indirectly connected to the
             * OutputPin to point to it.
             */
            if(a.outputSource != null){
                b.setOutputSource(a.outputSource);
            } else if(b.outputSource != null){
                a.setOutputSource(b.outputSource);
            }
        } else {
            ErrorHandler.newError("Simulation Error", "Cannot connect two outputs together.");
        }
    }

    /**
     * This static method disconnects two Joinables from each other. 
     */
    public static void disconnect(Joinable a, Joinable b){
        //Remove the Joinables from each other's connected set.
        a.connectedToSet.remove(b);
        b.connectedToSet.remove(a);
        /**
         * a now needs to see if it is still connected to its Output Source - if b was part of the
         * path to that Output Source, a is no longer connected to it. The canFind method below does
         * a depth first search to look for the Output Source.  If this connection is lost, a must
         * call setOutputSource on itself to set the Output Source reference of it and its connected
         * nodes to null.
         */
        if(!a.canFind(a.outputSource)) {
            a.setOutputSource(null);
        }
        if(!b.canFind(b.outputSource)) {
            b.setOutputSource(null);
        }
    }

    /**
     * canConnect is a static method that returns true if the connection of two Joinables will not
     * result in two OutputPins being directly or indirectly connected.
     * In other words, we return true if either of the two Joinable's outputSource references is
     * null, or if they are equal.
     */
    public static boolean canConnect(Joinable a, Joinable b){
        return a.outputSource == null || b.outputSource == null || a.outputSource == b.outputSource;
    }

    /**
     * canFind does a depth first search over the graph of connected Joinables, starting at *this*
     * and looking for the joinable parameter.
     */
    public boolean canFind(Joinable joinable) {return canFind(joinable, new ArrayList<Joinable>());}
    public boolean canFind(Joinable joinable, ArrayList<Joinable> visited){
        //return true if this is the Joinable we are looking for
        if(this == joinable) return true;
        //otherwise this to the list of already searched Joinables...
        visited.add(this);
        boolean found = false;
        //..and iterate through each unvisited Joinable in the connected set
        for(Joinable connectedJoinable : connectedToSet){
            if(!visited.contains(connectedJoinable)){
                found = found || connectedJoinable.canFind(joinable, visited);
            }
        }
        return found;
    }

    /**
     * setOutputSource does a depth first traversal of the graph of connected Joinables, starting at
     * *this* and setting the Output Source of each Joinable visited to joinable.
     */
    public void setOutputSource(OutputPin joinable) { setOutputSource(joinable, new ArrayList<Joinable>());}
    public void setOutputSource(OutputPin joinable, ArrayList<Joinable> visited){
        //set the Output Source of this to joinable
        outputSource = joinable;
        //and then add this to the list of already visited Joinables...
        visited.add(this);
        //..and iterate through each unvisited Joinable in the connected set
        for(Joinable connectedJoinable : connectedToSet){
            if(!visited.contains(connectedJoinable)){
                connectedJoinable.setOutputSource(joinable, visited);
            }
        }
    }
}
