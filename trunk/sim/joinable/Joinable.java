
package sim.joinable;

import java.util.*;
import java.util.ArrayList;

/**
 *
 * @author Stephen
 */
public abstract class Joinable {

    public Collection<Joinable> connectedToSet = new HashSet<Joinable>();
    public OutputPin outputSource;

    public static void connect(Joinable a, Joinable b){
        if(canConnect(a, b)){
            a.connectedToSet.add(b);
            b.connectedToSet.add(a);
            if(a.hasOutputSource()){
                b.setOutputSource(a.outputSource, new ArrayList<Joinable>());
            } else if(b.hasOutputSource()){
                a.setOutputSource(b.outputSource, new ArrayList<Joinable>());
            }
        } else {
            throw new Error("Connecting two outputs");
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
        return !(a.hasOutputSource() && b.hasOutputSource()) || a.outputSource == b.outputSource;
    }

    public boolean hasOutputSource() {
        return this.outputSource != null;
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
