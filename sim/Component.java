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

	List<Pin> getInputPins(){
		return inputPins;
	}
	
	int getNoOfInputPins(){
		return inputPins.size();
	}
	
	List<Pin> getOutputPins(){
		return outputPins;
	}
	
	int getNoOfOutputPins(){
		return outputPins.size();
	}
}
