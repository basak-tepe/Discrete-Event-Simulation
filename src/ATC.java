import java.util.Hashtable;
import java.util.PriorityQueue;
import java.util.Queue;

public class ATC {
	
	/**
	 * Flight manager
	 * Each Airport has one
	 * Does take off/landing operations
	 * when done, passes the control to ACC
	 * there can be multiple flights for ATC to deal
	 * these form a queue 
	 * 1 flight at a time
	 * code for an ATC is 7 digits, 4 chars and 3 numbers  e.g  "ABCD098"
	 * This number comes from hashing the airport code
	 * hash function is 31^i*ASCII a
	 */
	//DATA FIELDS-ATTRIBUTES
	
	Airport ATCsAirport;
	ACC area;
	boolean control;

	public PriorityQueue<Flight> SimulationReadyQueue;
	public PriorityQueue<Flight> SimulationRunningQueue;
	String ATCcode; //obtained with hashing the airport code
	
	
	//CONSTRUCTORS
	public ATC(String ATCcode, Airport ATCsAirport) {
		this.ATCcode = ATCcode;
		this.ATCsAirport = ATCsAirport;
		SimulationReadyQueue = new PriorityQueue<Flight>(new ATCComparator());
		SimulationRunningQueue = new PriorityQueue<Flight>(new ATCComparator());
	}
	//METHODS
	
	public String getCode() {
		return area.getCode();
	}
	
	public Airport getAirport() {
		return ATCsAirport;
	}

	public PriorityQueue<Flight> getSimulationReadyQueue() {
		return SimulationReadyQueue;
	}

	public PriorityQueue<Flight> getSimulationRunningQueue() {
		return SimulationRunningQueue;
	}
	
	
}
