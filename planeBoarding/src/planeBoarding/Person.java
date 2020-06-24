package planeBoarding;

/** Class for managing a Person's attributes (besides those of a Passenger) 
 * For example: name, age
 */
public class Person extends Passenger {
	private String name;
	private int age;
	
	/**
	 * Default constructor
	 */
	public Person () {
		
	}
	
	/**
	 * Constructor<p>
	 * Sets the following:
	 * @param name
	 * @param age
	 * @param id char 'f', 'g' or 's' (family, group or single) + number
	 * @param ticket characters: 'b', 'p', 'e' (ticket type: Business, Premium, Economic)
	 * @param boarding_priority true or false
	 * @param special_needs true or false
	 */
	public Person(String name, int age, String id, char ticket, 
			boolean boarding_priority, boolean special_needs) {
		super(id, ticket, boarding_priority, special_needs);
		this.name = name;
		this.age = age;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}

	public void setAge(int age) {
		this.age = age;
	}
	
	public int getAge() {
		return age;
	}
	
	/**
	 * - Calculates a person's overall boarding priority<br>
	 * - Adds a specified number of points for certain age
	 * categories, ticket type, priority boarding and special needs
	 */
	public int calculatePriority() {
		int priority = 0;
		
		if (age < 2)
			priority += 20;
		else if (age < 5)
			priority += 10;
		else if (age < 10)
			priority += 5;
		else if (age >= 60)
			priority += 15;
		
		if (getTicket() == 'b')
			priority += 35;
		else if (getTicket() == 'p')
			priority += 20;
		
		if (getBoardingPriority() == true)
			priority += 30;
		
		if (getSpecialNeeds() == true)
			priority += 100;
		
		return priority;
	}
}