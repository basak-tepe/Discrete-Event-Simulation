import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Queue;

public class ACC {
	/**
	 * enter and exit point for flights
	 * identified wih a 4 digit code eg "LTAA"
	 * 
	 */
	//DATA FIELDS-ATTRIBUTES
	
	private int time;
	private String areaCode;
	private ArrayList<Airport> airports;
	private ArrayList<Flight> flights;
	PriorityQueue SimulationReadyQueue; //flights
	boolean control;
	private ATC[] ATCcodes; //hash table
	ArrayList<Operation> operationsOfFlight;
	
	//CONSTRUCTORS
	public ACC(String areaCode, ArrayList<Airport> airports) {
		this.areaCode = areaCode;
		this.airports = airports;
		this.ATCcodes = new ATC[1000];
		SimulationReadyQueue = new PriorityQueue<Flight>(new ACCComparator());
	}
	
	public ACC(String areaCode) {
		this.areaCode = areaCode;
		this.airports = null;
		this.ATCcodes = new ATC[1000];
		SimulationReadyQueue = new PriorityQueue<Flight>(new ACCComparator());
	}
	
	//METHODS
	public String getCode() {
		return this.areaCode;
	}
	
	public void setAirports(ArrayList<Airport> airports) {
		this.airports = airports;
	}
	
	public void setFlights(ArrayList<Flight> flights) {
		this.flights = flights;
	}
	
	public ArrayList<Flight> getFlights() {
		return this.flights;
	}
	
	public ArrayList<Airport> getAirports() {
		return this.airports;
	}
	
	public ATC[] getATCs() {
		return this.ATCcodes;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public void setQueue(PriorityQueue readyQueue) {
		this.SimulationReadyQueue = readyQueue;
	}
	
}
