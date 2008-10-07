package sim;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Pin {
		
	private State state = State.FLOATING;
	private boolean isInput;

	private List<Pin> listConnections = new LinkedList<Pin>();

	public State getValue(){
		return state;
	}
	
	public void setValue(State state){
		this.state = state;
	}
	
	public void setIsInput(boolean isInput) {
		this.isInput = isInput;
	}
	
	public boolean getIsInput(){
		return isInput;
	}
	
	public void setIsOutput(boolean isInput) {
		this.isInput = !isInput;
	}
	
	public boolean getIsOutput(){
		return !isInput;
	}
	
	
	public void addConnection(Pin pin)
	{
		this.listConnections.add(pin);
	}
	
	public boolean removeConnection(Pin pin)
	{
		return this.listConnections.remove(pin);
	}
	
	public Iterator<Pin> getConnectionIterator()
	{
		return this.listConnections.iterator();
	}

}
