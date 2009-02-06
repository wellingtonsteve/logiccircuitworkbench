package sim.pin;


/**
 * A wire joins two 'Joinable's
 * 
 * @author Stephen
 */
public class Wire implements Joinable {

    private Joinable connectedI, connectedO;

    public boolean connect(Joinable joinable){
        if(joinable instanceof Pin){
            if(joinable instanceof InputPin){
                connectedI = joinable;
                if(connectedO != null) ((InputPin) connectedI).connectToOutput((OutputPin) connectedO);
                return true;
            }
            else{
                connectedO = joinable;
                if(connectedI != null) ((InputPin) connectedI).connectToOutput((OutputPin) connectedO);
                return true;
            }
        }
        else{
            /*
             *
             *
             *
             */
            return false;
        }
    }

}
