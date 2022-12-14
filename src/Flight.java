import java.util.ArrayList;
import java.util.Arrays;

public class Flight {
	/**
	 * entities that need processing
	 * each flight has a departure and landing airport
	 * 21 possible operations
	 * flights enter and exit the simulator at ACC.
	 */
	
	//DATA FIELDS-ATTRIBUTES
	boolean firstComer; 
	//during creation firstComer is true.
	//we dont need 2 data fields to check activeness.
	//This is just me making it more mnemonic.
	Airport departure; //where flight deports
	Airport landing;
	ACC area;
	ATC responsibleATC;
	public int[] operationTimes;
	ArrayList<Operation> operations;
	String flightCode;
	int admissionTime;
	int[] active = {0, 2, 3, 5, 7, 9, 10, 12, 14, 15, 17, 19, 20};
	int[] passive = {1, 4, 6, 8, 11, 14, 16, 18};
	int nextOperationIndex;
	
	
	//CONSTRUCTORS
	Flight(int admissionTime, int[] operationTimes, String flightCode, ACC area, Airport departure, Airport landing){
		this.flightCode = flightCode;
		this.area = area;
		this.departure = departure;
		this.landing = landing;
		this.operationTimes = operationTimes;
		this.admissionTime = admissionTime;

	
		//Wait indexes are: 1 4 6 8 11 14 16 18 
		//operation indexes are: 0 2 3 5 7 9 10 12 14 15 17 19 20
	}
	Flight(){
		this.departure = null;
		this.landing = null;
		int[] operationTimes = new int[21];
	}
	//METHODS
	
	public Airport getDepartureAirport() {
		return this.departure;
	}
	
	public Airport getLandingAirport() {
		return this.landing;
	}
	
	public ACC getACC() {
		return this.area;
	}
	
	public String getCode() {
		return this.flightCode;
	}
	
	
	public int getAdmissionTime() {
		return this.admissionTime;
	}
	
	public void setAdmissionTime(int admissionTime) {
		this.admissionTime = admissionTime;
	}

	public void setOperationTimes(int[] operationTimes) {
		this.operationTimes = operationTimes;
	}
	
	public int[] getOperationTimes() {
		return operationTimes;
	}
	public void setOperationTime(int index, int duration) {
		this.operationTimes[index] = duration;
	}
	public int getNextOperationIndex() {
		int nextOpIndex = 0;
		for(int time : operationTimes) {
			if(time == 0) {
				nextOpIndex++;
			}
			else {
				break;
			}
			
			if(nextOpIndex ==21) {
				return 20;
			}
		}
		
		//System.out.println(nextOpIndex);
		return nextOpIndex;
	}
	
	
	
	public void setOperations(ArrayList<Operation> operations) {
		this.operations = operations;
	}
	
	public Airport getDeparture() {
		return departure;
	}
	public Airport getLanding() {
		return landing;
	}
	public boolean isActive() {
		boolean activeness = false;
		int index = this.getNextOperationIndex();
		if(contains(this.active,index)) {
			activeness = true;
		}
		else {
		}
		return activeness;
	}
	
	public boolean isFinished() {
		int index = this.getNextOperationIndex();
		//System.out.println(index);
		if(index == 20 && Integer.compare(operationTimes[20],0) == 0) {
			return true;
		}
		else {
			return false;
		}
	}
	
	
	public static boolean contains(int[] activeIndexes, int number) {
		boolean contains = false;
		int size = activeIndexes.length;
		for(int i = 0 ;i<size;i++) {
			if(Integer.compare(activeIndexes[0],number) == 0) {
				contains = true;
				break;
			}
		}
		return contains;
	}
	
	public void setState(boolean firstComer) {
		this.firstComer = firstComer;
	}
	public boolean getState() {
		return this.firstComer;
	}

	
	
	
}
