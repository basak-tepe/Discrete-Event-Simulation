import java.util.Comparator;

public class ATCComparator implements Comparator<Flight>{

	
	public int compare(Flight f1, Flight f2) {
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
