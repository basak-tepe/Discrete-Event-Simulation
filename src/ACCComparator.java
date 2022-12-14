import java.util.Comparator;

public class ACCComparator implements Comparator<Flight>{
	
	//acc is different from atc in that
	//acc gives priority to first comer flights.
	//I check this by a data field of a flight, boolean firstComer, that I set to false after the first admission.

	public int compare(Flight f1, Flight f2) {
		
		//our first priority is being a first comer
		if(f1.getState() == true && f2.getState() == false) {//if f1 is firstcomer and f2 is not 
			return 1;
		}
		else if(f2.getState() == true && f1.getState() == false) {//if f1 is firstcomer and f2 is not 
			return -1;
		}
		
		
		//if both are old flights we look at the admission time.
		else {

			if (f1.getAdmissionTime() < f2.getAdmissionTime()) {
	            return 1;
	        }
	        else if (f1.getAdmissionTime() > f2.getAdmissionTime()) {
	            return -1;
	        }
	        
	        else if (f1.getAdmissionTime() == f2.getAdmissionTime()) { //if they have the saem admission time check the codes.
	        	String code1 = f1.getCode();
	        	String code2 = f2.getCode();
	        	if(code1.compareTo(code2)<0) {
	        		return 1;
	        	}
	        	else {
	        		return -1;
	        	}
	
	        }
			return 0;
	}
	}
}
