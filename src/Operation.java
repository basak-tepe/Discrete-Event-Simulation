
public class Operation {

	Object owner;
	int duration;
	boolean state;
	
	Operation(Object owner, int duration, boolean state){
		this.duration = duration;
		this.owner = owner;
		this.state = state;
	}
	
}
