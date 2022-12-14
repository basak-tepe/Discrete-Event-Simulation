public class Airport {
	
		//DATA FIELDS-ATTRIBUTES
		String airportCode;
		ACC area; //where an airport belongs
		ATC airportsATC;
		
		
		//CONSTRUCTORS
		
		Airport(String airportCode, ACC area){
			this.airportCode = airportCode;
			this.area = area;
		}
		//METHODS
		
		public String getCode() {
			return this.airportCode;
		}

		public ATC getAirportsATC() {
			return airportsATC;
		}

		public void setAirportsATC(ATC airportsATC) {
			this.airportsATC = airportsATC;
		}
		
		
}
