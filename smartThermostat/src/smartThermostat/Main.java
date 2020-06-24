package smartThermostat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.*;

/**
 * Main class with main method<p>
 * 
 * - reads commands from file and calls the needed methods<p>
 * 
 * //See comments in code for details
 */
public class Main {

	public static void main(String[] args) throws FileNotFoundException {

		PrintStream o = new PrintStream(new File("therm.out"));
		System.setOut(o);
		
		/* 
		 * changes the output from console to the newly created file 
		 * so we can still use System.out.print
		*/
		
		File file = new File("Tema2-PublicBonusTests/in/therm11.in");
		Scanner input = new Scanner(file);

		
		int noOfRooms = 0, timestampRef = 0;
		double globalTemperature = 0, maxHumidity = 0;
		
		/*
		 * take the first line and split it (there can be 3 parameters without humidity,
		 * or 4 with humidity - 2 cases):
		 */

		String line = input.nextLine();
		String[] separated = line.split(" ");
		
		noOfRooms = Integer.parseInt(separated[0]);
		globalTemperature = Double.parseDouble(separated[1]);
		
		if (separated.length > 3) {
			maxHumidity = Double.parseDouble(separated[2]);
			timestampRef = Integer.parseInt(separated[3]);
		}
		else timestampRef = Integer.parseInt(separated[2]);

		
		ArrayList<Room> house = new ArrayList<>();
		//array with all the rooms in the house
		
		for (int i = 0; i < noOfRooms; i++) {

			// create each room, set its main attributes, add it to the array
			
			Room r = new Room(timestampRef);
			r.setName(input.next());
			r.setSensorId(input.next());
			r.setArea(input.nextDouble());

			house.add(r);

		}

		String command; 
		Integer currentStamp; //timestamp we are currently checking
		String sensorId;

		while (input.hasNext()) {
			
			command = input.next();

			if (command.equals("OBSERVE")) {
				
				/*
				 * if the command is "OBSERVE", we look for the sensor id we are given, 
				 * through all the rooms until we find it (1 sensor/room)
				 * 
				 * then, we iterate through the first map (the one with the 
				 * 24 timestamps - each h: 24h before the referenced timestamp)
				 * until we find the current timestamp's place(between a lower 
				 * and a higher timestamp). Then we add the temperature and the timestamp 
				 * in another map (which counts as value for the first map) 
				 * (sorted from lowest to highest by temperature)
				 * 
				 */
				
				sensorId = input.next();
				for (int k = 0; k < noOfRooms; k++)
					if (house.get(k).getSensorId().equals(sensorId)) {

						Set<Map.Entry<Integer, SortedMap<Double, Integer>>> set1
							= house.get(k).hourlyBucketsHeat.entrySet();
						//hourlyBucketsHeat - explained in "Room.java" and README
						
						Iterator<Map.Entry<Integer, SortedMap<Double, Integer>>> i 
							= set1.iterator();
						// Using iterator in SortedMap

						Integer time = input.nextInt();
						Double temperature = input.nextDouble();

						//time and temperature to be added to the map
						
						currentStamp = 0;
						
						while (i.hasNext()) {
							Integer key = i.next().getKey();
							if (time < key)
								currentStamp = key;
							/*
							 * the last value of key that will be attributed to currentStamp is
							 * the timestamp we need in order to know where to add the temperature
							 */
						}
						
						/*
						 * if the timestamp exceeds the timestamp reference,
						 * then this is data we don't need => we're not mapping it
						 */
						
						if (time > timestampRef || time < timestampRef - 24 * 3600)
							break;
						
						//put the pair (temperature, time) in the map
						house.get(k).hourlyBucketsHeat.get(currentStamp).put(temperature, time);
						break;
					}

			}
			else if (command.equals("OBSERVEH")) {
				
				/*
				 * Same as with the "OBSERVE" command, but with humidity instead of heat
				 * (and another map)
				 */
				
				sensorId = input.next();
				for (int k = 0; k < noOfRooms; k++)
					if (house.get(k).getSensorId().equals(sensorId)) {

						Set<Map.Entry<Integer, SortedMap<Double, Integer>>> set1
							= house.get(k).hourlyBucketsHumidity.entrySet();
						Iterator<Map.Entry<Integer, SortedMap<Double, Integer>>> i 
							= set1.iterator();

						Integer time = input.nextInt();
						Double humidity = input.nextDouble();

						currentStamp = 0;
						
						while (i.hasNext()) {
							Integer key = i.next().getKey();
							if (time < key)
								currentStamp = key;
						}
						
						if (currentStamp == 0 || time > timestampRef)
							break;
						
						house.get(k).hourlyBucketsHumidity.get(currentStamp).put(humidity, time);
						break;
					}

			}
			else if (command.equals("TRIGGER")) {
				command = command + input.next();
				Room.triggerHeat(house, globalTemperature, maxHumidity);
				
			}
			else if (command.equals("TEMPERATURE")) {
				globalTemperature = input.nextDouble();
				//change globalTemperature to what the user wants
			}
			else if (command.equals("LIST")) {
				
				/*
				 * look through the array for the room to be listed (by its name)
				 * set the extremities of the time interval and print the temperatures
				 * observed during that interval
				 */
				String roomName = input.next();
				for (int k = 0; k < noOfRooms; k++)
					if (house.get(k).getName().equals(roomName)) {
						int loTimestamp = input.nextInt();
						int hiTimestamp = input.nextInt();
						house.get(k).list(loTimestamp, hiTimestamp);
						break;
					}
				
				
				
			}

		}
		
		
		input.close();
	}
}
