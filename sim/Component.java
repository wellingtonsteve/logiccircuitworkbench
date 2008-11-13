package sim;

import java.util.List;

public abstract class Component {

	private final String type;
	private String decription;

	private List<Pin> inputPins;
	private List<Pin> outputPins;

	public Component(String type){
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
		
	public String getDecription() {
		return decription;
	}

	public void setDecription(String decription) {
		this.decription = decription;
	}

	public List<Pin> getInputPins(){
		return inputPins;
	}
	
	public int getNoOfInputPins(){
		return inputPins.size();
	}
	
	public List<Pin> getOutputPins(){
		return outputPins;
	}
	
	public int getNoOfOutputPins(){
		return outputPins.size();
	}
}
