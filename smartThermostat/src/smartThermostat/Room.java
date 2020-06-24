package smartThermostat;

import java.util.*;

/**
 * Class for each room with a sensor<p>
 * 
 * hourlyBucketsHeat - SortedMap (by Unix timestamp - descending/recent to old):<br>
 * 	- key: 1h time intervals for 24h before the time reference<br>
 * 	- value: another SortedMap (by temperature - ascending):<br>
 * 		* key: temperature observed at a certain time<br>
 * 		* value: the time when the temperature was observed. This timestamp has to be less than<br>
 * the current key/timestamp of the larger Map and greater than the next key/timestamp of the<br>
 * larger Map<br>
 * 
 * hourlyBucketsHumidity - the same as hourlyBucketsHeat, only, instead of temperature,<br> 
 * we have humidity
 * 		
 */
public class Room {
	private String name;
	private String sensorId;
	private double area;
	protected SortedMap<Integer, SortedMap<Double, Integer>> hourlyBucketsHeat;
	protected SortedMap<Integer, SortedMap<Double, Integer>> hourlyBucketsHumidity;
	

	/**
	 * Default constructor
	 * @param timestamp the time reference needed in order to initialize 2 maps
	 */
	public Room(int timestamp) {
		name = null;
		sensorId = null;
		area = 0;

		hourlyBucketsHeat 
			= new TreeMap<Integer, SortedMap<Double, Integer>>(Collections.reverseOrder());
		
		hourlyBucketsHumidity 
			= new TreeMap<Integer, SortedMap<Double, Integer>>(Collections.reverseOrder());

		for (int i = 0; i <= 23; i++) {
			hourlyBucketsHeat.put(timestamp - i * 3600, new TreeMap<>());
			hourlyBucketsHumidity.put(timestamp - i * 3600, new TreeMap<>());
		}
		

	}

	/**
	 * 
	 * @param name room name
	 * @param sensorId the heat/humidity sensor's ID
	 * @param area room area
 	 * @param timestamp the time reference needed in order to initialize 2 maps
	 */
	public Room(String name, String sensorId, double area, int timestamp) {
		this.name = name;
		this.sensorId = sensorId;
		this.area = area;

		hourlyBucketsHeat 
			= new TreeMap<Integer, SortedMap<Double, Integer>>(Collections.reverseOrder());
		
		hourlyBucketsHumidity 
			= new TreeMap<Integer, SortedMap<Double, Integer>>(Collections.reverseOrder());

		for (int i = 0; i <= 23; i++) {
			hourlyBucketsHeat.put(timestamp - i * 3600, new TreeMap<>());
			hourlyBucketsHumidity.put(timestamp - i * 3600, new TreeMap<>());
		}
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setSensorId(String sensorId) {
		this.sensorId = sensorId;
	}

	public String getSensorId() {
		return sensorId;
	}

	public void setArea(double area) {
		this.area = area;
	}

	public double getArea() {
		return area;
	}

	/**
	 * Decide whether to start the heating system or not based on temperature and humidity<p>
	 * 
	 * check each room for most recent lowest temperature and highest humidity<br>
	 * (check the hourlyBucketsHeat and hourlyBucketsHumidity maps)<p>
	 * 
	 * iterate through them: the most recent interval where temperature/humidity was<br>
	 * registered is the first interval encountered that isn't an empty collection<br>
	 * (since the larger maps are sorted in descending order by timestamps)<p>
	 * 
	 * - the lowest temperature will be the first key in the map under a timestamp<br> 
	 * (highest time interval extremity) of the hourlyBucketsHeat map<br>
	 * (ascending order by temperature)<p>
	 * 
	 * - the highest humidity will be the last key in the map under a timestamp<br>
	 * (highest time interval extremity) of the hourlyBucketsHumidity map<br>
	 * (ascending order by humidity)<p>
	 * 
	 * ~ to get the weighted mean, we multiply each lowestTemperature/highestHumidity<br>
	 * found by the room's area, and add all of them to a sum. Then, we divide them by<br>
	 * the area of all the rooms taken into account<p>
	 * 
	 * @param house array of all the rooms in a house
	 * @param globalTemperature desired temperature: if the weighted arithmetic mean<br>
	 * of the most recent (last registered hour interval by the sensors) minimum temperature 
	 * observed in each room is less than the globalTemperature, the heating system starts<br>
	 * @param maxHumidity maximum humidity allowed: if the weighted arithmetic mean<br>
	 * of the most recent maximum humidity observed in each room is greater than maxHumidity,<br>
	 * the heating system won't start even if the the previous conditions are met
	 */
	public static void triggerHeat(ArrayList<Room> house, double globalTemperature, 
			double maxHumidity) {

		
		double lowestTempMean = 0, lowestTemperature, totalArea1 = 0;
		double highestHumidityMean = 0, highestHumidity, totalArea2 = 0;

		for (Room i : house) {
			lowestTemperature = 0;
			highestHumidity = 0;

			Set<Map.Entry<Integer, SortedMap<Double, Integer>>> set1 
				= i.hourlyBucketsHeat.entrySet();
			Iterator<Map.Entry<Integer, SortedMap<Double, Integer>>> j 
				= set1.iterator();
			
			Set<Map.Entry<Integer, SortedMap<Double, Integer>>> set2 
				= i.hourlyBucketsHumidity.entrySet();
			Iterator<Map.Entry<Integer, SortedMap<Double, Integer>>> k 
				= set2.iterator();

			while (j.hasNext()) {

				int hourlyTimestamp = j.next().getKey();

				if (i.hourlyBucketsHeat.get(hourlyTimestamp).isEmpty())
					continue;
				else {
					lowestTemperature = i.hourlyBucketsHeat.get(hourlyTimestamp).firstKey();
					break;
				}
			}
			
			while (k.hasNext()) {
				
				int hourlyTimestamp = k.next().getKey();

				if (i.hourlyBucketsHumidity.get(hourlyTimestamp).isEmpty())
					continue;

				else {
					highestHumidity = i.hourlyBucketsHumidity.get(hourlyTimestamp).lastKey();
					break;
				}
			}

			if (lowestTemperature != 0) {
				/*
				 * if there is a registered temperature in the room in the last 24h,
				 * take this room into account
				 */
				
				lowestTempMean += lowestTemperature * i.getArea();
				totalArea1 += i.getArea();	
			}
			
			if (highestHumidity != 0) {
				/*
				 * if there is a registered hummidity in the room in the last 24h,
				 * take this room into account
				 */
				
				highestHumidityMean += highestHumidity * i.getArea();
				totalArea2 += i.getArea();
			}
		}
		
		if (totalArea1 != 0)
			lowestTempMean /= totalArea1;
		
		if (totalArea2 != 0)
			highestHumidityMean /= totalArea2;
		
		if (highestHumidityMean > maxHumidity)
			System.out.println("NO");
		else {
			if (lowestTempMean < globalTemperature)
				System.out.println("YES");
			else
				System.out.println("NO");	
		}
		
	}
	
	/**
	 * Lists all the temperatures registered in a room in a time interval<br>
	 * (in ascending order by hourly intervals, if the time span is larger than an hour)<p>
	 * 
	 * - prints room name<br>
	 * - iterates through the hourlyBucketsHeat sorted maps of every room, takes the<br> 
	 * temperatures that have been observed at a time in between the interval extremities
	 * and prints them<br>
	 * @param loTimestamp lowest time interval extremity
	 * @param hiTimestamp highest time interval extremity
	 */
	public void list(int loTimestamp, int hiTimestamp) {

		System.out.print(getName());

		Set<Map.Entry<Integer, SortedMap<Double, Integer>>> set1 = hourlyBucketsHeat.entrySet();
		Iterator<Map.Entry<Integer, SortedMap<Double, Integer>>> i = set1.iterator();

		// Traversing map
		
		String tempInInterval = "";

		while (i.hasNext()) {
			Map.Entry<Integer, SortedMap<Double, Integer>> sensorMap 
				= (Map.Entry<Integer, SortedMap<Double, Integer>>) i.next();

			int hourlyTimestamp = (Integer) sensorMap.getKey();
			if (hourlyTimestamp < loTimestamp || hourlyTimestamp > hiTimestamp)
				continue;
			
			Set<Map.Entry<Double, Integer>> set2 
				= hourlyBucketsHeat.get(hourlyTimestamp).entrySet();
			Iterator<Map.Entry<Double, Integer>> j = set2.iterator();

			while (j.hasNext()) {
				Map.Entry<Double, Integer> sensorHourlyMap = (Map.Entry<Double, Integer>) j.next();
				double temperature = sensorHourlyMap.getKey();
				int currentTimestamp = sensorHourlyMap.getValue();

				if (currentTimestamp < loTimestamp || currentTimestamp > hiTimestamp)
					continue;

				
				tempInInterval += String.format("%.2f", temperature) + " ";
				
			}
		}

		tempInInterval = tempInInterval.trim();
		if (tempInInterval != "")
			System.out.println(" " + tempInInterval);
	}
}
