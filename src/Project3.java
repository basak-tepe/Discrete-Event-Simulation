import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

public class Project3 {	
	
	/**
	 * @author Basak Tepe, Student ID:2020400117
	 * @since Date: 11.20.2022 submission
	 */

	/**
	 * How my program works
	 * Using certain indexes, running flights are sent to appropriate queues.
	 * Waiting flights are given a new admission time and sent back to the admission queue. 
	 * This new admission time is currentTime + waitingTime.
	 * @throws IOException 
	 */
	
	public static void main(String[] args) throws IOException{
		
		try {
			String inputFile = args[0];
			String outputFile = args[1];
			
			File input = new File(inputFile);
			File output = new File(outputFile);
			Scanner sc = new Scanner(input);
			BufferedWriter writer = new BufferedWriter(new FileWriter(output));
			
			
			String[] firstLine = sc.nextLine().split(" "); //contains A and F
			//A lines follow first. The number of area control centers.
			int A = Integer.parseInt(firstLine[0]);
			//F lines follow next. The number of flights.
			//These lines have the format:
			//admission time
			//flight code
			//acc code
			//departure airport code
			//arrival airport code
			//and 21 operation times.
			
			
			int F = Integer.parseInt(firstLine[1]);
			ArrayList<ACC> AreaControlCenters = new ArrayList<ACC>(); 		
			ArrayList<String> ACCnames = new ArrayList<String>();
			ArrayList<Airport> airports = new ArrayList<Airport>();
			ArrayList<Flight> flights = new ArrayList<Flight>();
		
			
					
			for(int i = 0; i<A; i++) {
				String[] nextLine = sc.nextLine().split(" ");
				String areaCode = nextLine[0];
				//creating one acc and storing it
				ACC acc = new ACC(areaCode);
				AreaControlCenters.add(acc);
				int airportCount = nextLine.length-1;
				for(int j = 1;j<airportCount+1;j++) {
					String airportCode = nextLine[j];
					Airport airport = new Airport(airportCode, acc);
					airports.add(airport);
				}
				
				//now, al data is taken on 1 acc & its flights.
				//lets update the airports for this acc.
				acc.setAirports(airports);
				//System.out.println(acc.getAirports().get(1).getCode());
				airports = new ArrayList<Airport>();//empty the airports and repeat for another acc	
			}
			
			

			
		int accCount = AreaControlCenters.size();
			
		//so far, we have done acc and relevant airport taking.
		//next we have to create flights.
			
			for(int i = 0 ;i<F; i++) {
				String[] nextLine = sc.nextLine().split(" ");
				int admissionTime = Integer.parseInt(nextLine[0]);
				String flightCode = nextLine[1];
				String acc = nextLine[2];
				String departureAdress = nextLine[3];
				String landingAdress = nextLine[4];
				int[] operationTimes = new int[21];
				Airport landingAirport = null;
				Airport departureAirport = null;
				ACC flightsACC = null;
				
				for(int j = 0; j<21;j++) {
					operationTimes[j] = Integer.parseInt(nextLine[j+5]);
				}
				//now all data is taken for a flight. 
				//we will create our flight.
				
				//choosing the acc among accs for a flight
				
				for (int j = 0;j<accCount;j++) {
					if((AreaControlCenters.get(j).getCode()).compareTo(acc) == 0) {
						flightsACC = AreaControlCenters.get(j);
					}
				}
				
				ArrayList<Airport> availableAirports = flightsACC.getAirports();
				int k = availableAirports.size();
				for(int j = 0 ; j<k;j++) {
					Airport current = availableAirports.get(j);
					if((current.getCode()).compareTo(landingAdress) == 0) {
						landingAirport = current;
					}
					else if ((current.getCode()).compareTo(departureAdress) == 0) {
						departureAirport = current;
					}
				}		
				
				Flight flight = new Flight(admissionTime, operationTimes, flightCode, flightsACC, departureAirport, landingAirport);
				flight.setState(true);
				flights.add(flight);
				
				//we are inside a loop for each flight.
				//I had took all the operation times.
				
				//now I will create operations, i.e the events of a system.
				ArrayList<Operation> operations = new ArrayList<Operation>();
				int[] accOperationIndexes = {0,1,2,10,11,12,20};
				int[] departureAirportIndexes = {3,4,5,6,7,8,9};
				int[] landingAirportIndexes = {13,14,15,16,17,18,19};
				int[] activeOperationIndexes = {0, 2, 3, 5, 7, 9, 10, 12, 14, 15, 17, 19, 20};; //running
		
				
				for(int j = 0; j<21;j++) {
					/*
					 * operation(owner,duration,state)
					 */
					
					Object owner;
					int duration = operationTimes[j];
					boolean running; //if active then true
					
					//OWNER
					if(contains(accOperationIndexes,j)) { //if j is an acc operation
						owner = acc;
					}
					else if(contains(departureAirportIndexes,j)){ //if it is an atc operation of departure
						owner = flight.getDeparture().getAirportsATC();
					}
					
					else { //landing
						owner = flight.getLanding().getAirportsATC();
					}
					
					//STATE
					if(contains(activeOperationIndexes,j)) { //if j is an acc operation
						running = true;
					}
					else {
						running = false;
					}
					Operation newOperation = new Operation(owner, operationTimes[j],running);
					operations.add(newOperation);
					
				}
				
				flight.setOperations(operations);		
			}
			 //a loop for flights ends
			
			
			int flightCount = flights.size();
			//distributing the flights among ACCs.
			for(int i = 0;i<accCount;i++) {
				ArrayList<Flight> accsFlights = new ArrayList<Flight>();
				for(int j = 0;j <flightCount ;j++) {
				if(AreaControlCenters.get(i) ==  flights.get(j).getACC()) {
						accsFlights.add(flights.get(j));
					}
				}
				AreaControlCenters.get(i).setFlights(accsFlights);
				
			}
			
			//now, we have to create atcs and hash table of atcs for each acc.
			
			for(int i = 0; i<accCount; i++) { //for each acc
				ArrayList<Flight> flightsOfACC = null;
				ACC acc = AreaControlCenters.get(i);
				ATC[] ATChashTable = acc.getATCs();
				flightsOfACC = acc.getFlights();
				
				ArrayList<Airport> ACCsAirports = acc.getAirports();  //airports of acc. each airport has one atc.
				int airportCount = ACCsAirports.size();
				for(int j = 0; j<airportCount; j++) { //for every airport
					Airport currentAirport = ACCsAirports.get(j);
					String apCode = currentAirport.getCode();
					boolean placed = false;
					
					int k  = 0; //used for probing
					
					while(!placed) { //i will use linear probing.
						int hashVal = 0;
					     for( int h = 0; h < apCode.length( ); h++ ) {
					            hashVal = hashVal + ((int)(Math.pow(31, h)) * ((int)(apCode.charAt(h))));
					     }
					     hashVal = (hashVal+ k)%1000;
					     if(ATChashTable[hashVal] == null) {
					    	String atcCode = acc.getCode() + hashVal;
					    	ATC atc = new ATC(atcCode,currentAirport);
					    	currentAirport.setAirportsATC(atc);
					     	ATChashTable[hashVal] = atc;
					     	placed = true;
					     }
					     
					     else{			    	 
					    	 k++;
					    	 continue;
					     }
					     
					}
						
				}
					
			}
			
			//now hashing is done and tables of atcs are created for each acc.
			//data taking is done.
			//everything is set.
			//we can start the simulation, it will be separate for each acc.
			
			 
			
			 
			 /**
			 * simulation is essentially making a flight wait as another one processes.
			 * accs will be given a time of 30 units to process their flights.
			 */
			

				
				
				for(int i = 0;i<A;i++) { //simulation is done once for each acc
					ACC acc = AreaControlCenters.get(i);
					ArrayList<Flight> ACCsFlights = acc.getFlights();
					int ACCflightCount = ACCsFlights.size();

					
					/**
					 * first I have to sort the flights into a queue.
					 * This will be the ready queue of ACC.
					 * Based on their admission time - if they have the same admission time then I will look into string comparison.
					 */
					
					
					/**
					 * Selection sort
					 */
					
					
					for(int j= 0;j<ACCflightCount-1;j++) {
						int minIndex = j;
						Flight reference = ACCsFlights.get(j);
						int referenceAdmission = reference.getAdmissionTime();
						int minAdmission = referenceAdmission;
						
						Flight min = reference;
						for (int k = j+1; k<ACCflightCount;k++) {
							Flight current= ACCsFlights.get(k);
							int currentAdmission = current.getAdmissionTime();
							//comparison.
							if(currentAdmission<minAdmission) {
								min = current;
								minIndex = k;
								minAdmission = currentAdmission;
								continue;
							}
							else if( currentAdmission == minAdmission ) { //if equal then string comparison
								String ref = reference.getCode();
								String cur = current.getCode();
								if(cur.compareTo(ref)<0) { //if comparison value is smaller. swap
									min = current;
									minIndex = k;
									minAdmission = currentAdmission;
									continue;
								}
							}
						}
						
						Flight temp = min;
						ACCsFlights.set(minIndex, reference);
						ACCsFlights.set(j, temp);
			
					}
					
					//Now, flights are all sorted
	
					
		
					Queue<Flight> ACCsReadyQueue = new PriorityQueue<Flight>(new ACCComparator());;
						 for(int j = 0; j< ACCflightCount ;j++) {
							 
							 ACCsReadyQueue.add(ACCsFlights.get(j));
							 
							 //add method adds to the tail.
							 //now we have smallest admission time at the front.
						 }		 
						 

				
					//these are for ACCs.
						 
					//I will only use 2 queues. Admission & Running.
					//waiting will be done in admission.
						 
						 
					//ACC QUEUES	 
					Queue<Flight> SimulationAdmissionQueue = ACCsReadyQueue; //the queue for waits (re-admissions) and first admission
					Queue<Flight> SimulationRunningQueue = new PriorityQueue<Flight>(new ACCComparator());
					
					//ATC QUEUES EXIST AS  DATA FIELDS	
					
					boolean simulation = true; 
					int time = -1;

					
					int finished = 0 ;
					
					while(time<100000) { //while(simulation)
						
						time++;
						
				
						if(finished == F) { //if I have landed all planes.
						simulation  = false;
							break;
						}
						
						
						//WAITING
						//Admission info
					
						if(!SimulationAdmissionQueue.isEmpty()) { //if there are planes waiting for admission
							
							for(Flight currentAdmittionFlight : SimulationAdmissionQueue) {
								if(time%30 ==0 ) {
									//System.out.println(""+time+" " +currentAdmittionFlight.getCode()+ "  "+ "admitted");
								
								}
								currentAdmittionFlight = SimulationAdmissionQueue.peek();
								int nexOpIndex = currentAdmittionFlight.getNextOperationIndex();
				
								if(time == currentAdmittionFlight.getAdmissionTime()) { //if admission time has come.
									//currentAdmittionFlight.setAdmissionTime(0); //admit it.
									//addition is done to the running queue because operations start with running.
									//System.out.print("transfer from admittion to running ");
									SimulationAdmissionQueue.remove(currentAdmittionFlight);
									currentAdmittionFlight.setAdmissionTime(time);
									//operation index matters: we send them to required queues. 
									
									//if it belongs to ACC.
									if(nexOpIndex<3 ||( 10 < nexOpIndex && nexOpIndex  < 13)) {
										SimulationRunningQueue.add(currentAdmittionFlight);
										//System.out.println("going to ACC running queue");
									}
									if(nexOpIndex == 3) { //if 3 send it to ATC1 ( departure)
										time++;
										currentAdmittionFlight.getDeparture().getAirportsATC().getSimulationRunningQueue().add(currentAdmittionFlight);
										//System.out.println("to be going to start of the departure ATC");
									}
									if(nexOpIndex == 10) { 
										SimulationRunningQueue.add(currentAdmittionFlight);
										//System.out.println("to be going to start of the ACC in air");
									}
									if(nexOpIndex == 13) { 
										currentAdmittionFlight.getLanding().getAirportsATC().getSimulationRunningQueue().add(currentAdmittionFlight);
										//System.out.println("to be going to start of the landing ATC");
									}
									if(nexOpIndex== 20) {
										SimulationRunningQueue.add(currentAdmittionFlight);
										//System.out.println("to be going to start of the ACC in landing");
									}
								
									if(nexOpIndex>3 && nexOpIndex<10) {
										//this is departure.
										//System.out.println("to be going to ATC departure queue");
										currentAdmittionFlight.getDeparture().getAirportsATC().getSimulationRunningQueue().add(currentAdmittionFlight);
									}
									if(nexOpIndex>13 &&nexOpIndex<20) { //else it is landing
										//System.out.println("to be going to ATC landing queue");
										currentAdmittionFlight.getLanding().getAirportsATC().getSimulationRunningQueue().add(currentAdmittionFlight);
											
										}
									}
									
									if(currentAdmittionFlight.getState() == true) { //if our flight is a first comer, it is no longer a first comer.
										currentAdmittionFlight.setState(false);
									}
							
								}
							}
						
						
						//RUNNING
						//if there are flights waiting to be run. 
						//I have to check all ATCs and the one ATC and run their operations if exist.
						//ACC has a time limit of 30t.
						
						//ACC
						if(!SimulationRunningQueue.isEmpty()) { //if the queue is not empty
							int runningTime = -100;
							
							//We only operate on the top flight.
							Flight currentFlight = SimulationRunningQueue.peek();	
							int nextOpIndex = currentFlight.getNextOperationIndex(); //next non zero operation's index.
							
							//IF IT IS A RUNNING OPERATION
							if(nextOpIndex == 0 || nextOpIndex == 2 || nextOpIndex == 10 || nextOpIndex == 12 || nextOpIndex == 20){ //if there is an active operation on the run
								if(time%30 ==0 ) {
									//System.out.println(""+time+" " +currentFlight.getCode()+ "  "+ "running on ACC");
								}
								//Flight is active - ACC running indexes.
								int runningIndex = nextOpIndex;
								//System.out.println(runningIndex);
								runningTime = currentFlight.getOperationTimes()[runningIndex];
								//System.out.println(runningTime);
								
								//if it lasts more than 30 seconds - we operate and send it back to the end of the queue.
								if(runningTime>30) {
									runningTime = runningTime-30; //running time has decreased by 1 unit.
									time+=29;
									currentFlight.setOperationTime(runningIndex, runningTime);
									SimulationRunningQueue.remove(currentFlight);
									currentFlight.setAdmissionTime(time+1);
									SimulationRunningQueue.add(currentFlight);
								}
								
								if(runningTime>=1) {
									runningTime--; //running time has decreased by 1 unit.
									currentFlight.setOperationTime(runningIndex, runningTime);	
								}
										
								//System.out.println(Arrays.toString(currentFlight.getOperationTimes()));
								
							}	
							
							//if running time has finished and our flight has entered a waiting state.
							//WAITING OPERATIONS
							nextOpIndex = currentFlight.getNextOperationIndex();
							if(nextOpIndex == 1 || nextOpIndex == 11 || nextOpIndex == 3 || nextOpIndex == 13 || runningTime == 0 ){ //if flight is in a waiting state, remove it from the running queue and put it into the waiting queue.
								if(time%30 ==0 ) {
									//System.out.println(""+time+" " +currentFlight.getCode()+ "  "+ "transfer to waiting");
								}
								//nextOpIndex = currentFlight.getNextOperationIndex();
								//if it is not a control transfer operation
								if(nextOpIndex != 3 ||nextOpIndex != 10 || nextOpIndex != 13 || nextOpIndex != 20) {
									//System.out.println("passive");
									//System.out.println("transfer from running to admission");
									SimulationRunningQueue.remove(currentFlight);
									currentFlight.setAdmissionTime(time+1);
									SimulationAdmissionQueue.add(currentFlight);
									int waitIndex = currentFlight.getNextOperationIndex();
									//System.out.println(waitIndex);
									int waitTime = currentFlight.getOperationTimes()[waitIndex];
									int admissionTime = time + waitTime; //current time + waiting
									//System.out.println(admissionTime);
									
									currentFlight.setAdmissionTime(admissionTime); //new admission time
									currentFlight.setOperationTime(waitIndex, 0); //remove the waiting time.
								}
								
								else { //if it is a control transfer, we can skip the admission queue
									SimulationRunningQueue.remove(currentFlight);
									if(nextOpIndex == 3) {
										currentFlight.setAdmissionTime(time+1);
										currentFlight.getDepartureAirport().getAirportsATC().getSimulationRunningQueue().add(currentFlight);
									}if(nextOpIndex == 10) {
										currentFlight.setAdmissionTime(time+1);
										SimulationRunningQueue.add(currentFlight);
									}if(nextOpIndex == 13) {
										currentFlight.setAdmissionTime(time+1);
										currentFlight.getLandingAirport().getAirportsATC().getSimulationRunningQueue().add(currentFlight);				
									}if(nextOpIndex == 20) {
										currentFlight.setAdmissionTime(time+1);
										SimulationRunningQueue.add(currentFlight);
									}
									
								}
								
								
								//System.out.println(Arrays.toString(currentFlight.getOperationTimes()));
								}
							
						}
						
						
						//ATCs
						//for all airports/ATCs.
						int airportCount = acc.getAirports().size();
						ArrayList<Airport> allAirportsOfACC  = acc.getAirports();
						for(int j = 0 ; j<airportCount;j++) {
							ATC currentATC = allAirportsOfACC.get(j).getAirportsATC();
							//i will only check running part because I use simulation admission queue as a line for both atc and accsç
							//i.e distribution to related running queues are done from simulations ready queue.
							PriorityQueue<Flight> ATCsRunningQueue = currentATC.getSimulationRunningQueue();
							if(!ATCsRunningQueue.isEmpty()) { //if the queue is not empty
								int runningTime = 0;
								
								//We only operate on the top flight.
								Flight currentFlight = ATCsRunningQueue.peek();	
								int nextOpIndex = currentFlight.getNextOperationIndex();
								
								//IF IT IS A RUNNING PROCESS
								if(nextOpIndex == 3 || nextOpIndex == 5  || nextOpIndex == 7  || nextOpIndex == 9  || nextOpIndex == 13  || nextOpIndex == 15  || nextOpIndex == 17  || nextOpIndex == 19 ){ //if there is an active operation on the run
									if(time%30 ==0 ) {
										//System.out.println(""+time+" " +currentFlight.getCode()+ "  "+ "running on ATC");
									}
									//running atc indexes
									int runningIndex = currentFlight.getNextOperationIndex()-1;
									//System.out.println(runningIndex);
									runningTime = currentFlight.getOperationTimes()[runningIndex];
									//System.out.println(runningTime);
									if(runningTime>=1) {
										runningTime--; //running time has decreased by 1 unit.
									}
									
									currentFlight.setOperationTime(runningIndex, runningTime);	
									//System.out.println(Arrays.toString(currentFlight.getOperationTimes()));
									
								}					
									
								//IF IT IS A WAITING PROCESS
								//if running time has finished and our flight has entered a waiting state.
								nextOpIndex = currentFlight.getNextOperationIndex()-1;
								if(nextOpIndex == 4 || nextOpIndex == 6 || nextOpIndex == 8 || nextOpIndex == 14 || nextOpIndex == 16 || nextOpIndex == 18 || nextOpIndex == 10 || nextOpIndex == 20 || runningTime == 0 ){ //if flight is in a waiting state, remove it from the running queue and put it into the waiting queue.						
									//if it is not a control transfer
									if(time%30 ==0 ) {
										//System.out.println(""+time+" " +currentFlight.getCode()+ "  "+ "transfer to waiting");
									}
									nextOpIndex = currentFlight.getNextOperationIndex();
									if(nextOpIndex != 3 ||nextOpIndex != 10 || nextOpIndex != 13 || nextOpIndex != 20) {
									//System.out.println("transfer from running to admission");
									int waitIndex = currentFlight.getNextOperationIndex();
									//System.out.println(waitIndex);
									int waitTime = currentFlight.getOperationTimes()[waitIndex];
									int admissionTime = time + waitTime; //current time + waiting
									currentFlight.setAdmissionTime(admissionTime); //new admission time
									currentFlight.setOperationTime(waitIndex, 0); //remove the waiting time.
									ATCsRunningQueue.remove(currentFlight);
									SimulationAdmissionQueue.add(currentFlight);
									//System.out.println(Arrays.toString(currentFlight.getOperationTimes()));
									
									}
									
									else { //if it is a control transfer:
										SimulationRunningQueue.remove(currentFlight);
										if(nextOpIndex == 3) {
											currentFlight.setAdmissionTime(time+1);
											currentFlight.getDepartureAirport().getAirportsATC().getSimulationRunningQueue().add(currentFlight);
										}if(nextOpIndex == 10) {
											currentFlight.setAdmissionTime(time+1);
											SimulationRunningQueue.add(currentFlight);
										}if(nextOpIndex == 13) {
											currentFlight.setAdmissionTime(time+1);
											currentFlight.getLandingAirport().getAirportsATC().getSimulationRunningQueue().add(currentFlight);				
										}if(nextOpIndex == 20) {
											currentFlight.setAdmissionTime(time+1);
											SimulationRunningQueue.add(currentFlight);
										}
										
									}
									
								}
							}
							
						}
						
						//Terminating done flights
						for(int j= 0;j<F;j++) {
							boolean done = false;
							if (flights.get(j).isFinished()) {
								done = true;
							}
							if(done) {
								finished++;
	
								SimulationAdmissionQueue.remove(flights.get(j));
								SimulationRunningQueue.remove(flights.get(j));
								
							}
						}
					}
					
					acc.setTime(time);
				}

				
			
				

				
				
				
			
			//when ends, print data for each acc.
			for(int i = 0;i<accCount;i++) {
				ACC acc = AreaControlCenters.get(i);
				ArrayList<Airport> airportsOfAcc = acc.getAirports();
				System.out.print( acc.getCode() + " "+ acc.getTime());
				writer.write(acc.getCode() + " "+ acc.getTime());
				int apCount = airportsOfAcc.size();
				//for( int j = 0; j <apCount;j++) { //now we will print airports.
					ATC[] ATCs = acc.getATCs();
					for(int k = 0; k<1000; k++) {
						if(ATCs[k] == null) {
							continue;
						}
						else {
							//filling 00s before hash table positions.
							String code;
							if(k<10) {
								code = "00" + k;
							}
							else if(k<100) {
								code = "0" + k;
							}
							else {
								code = "" + k;
							}
						
							writer.write(" "+(ATCs[k].getAirport().getCode())+code);
							
							System.out.print(" "+(ATCs[k].getAirport().getCode())+code);
						}
						
					}
					writer.newLine();
				//}
				//System.out.println();
			}
			
			writer.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	
	public static boolean contains(int[] array, int number) {
		int size = array.length;
		for(int i = 0 ;i<size;i++) {
			if(Integer.compare(array[0], number) == 0) {
				return true;
			}
		}
		return false;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}