
package sim.joinable;

import java.util.*;

/**
 *
 * @author Stephen
 */
public abstract class Joinable {

    protected Collection<Joinable> connectedTo = new HashSet<Joinable>();
    protected Joinable outputSource;

    public static void connect(Joinable a, Joinable b) throws Exception{
        if(canConnect(a, b)){
            a.connectedTo.add(b);
            b.connectedTo.add(a);
        } else {
            throw new Exception("Connecting two outputs");
        }
    }

    public static boolean canConnect(Joinable a, Joinable b){
        return !(a.hasOutputSource() && b.hasOutputSource());
    }

    public boolean hasOutputSource() {
        return this.outputSource != null;
    }


}
