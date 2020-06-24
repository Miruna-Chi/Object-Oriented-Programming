package planeBoarding;

/**
 * Class for managing each Passenger's attributes
 */
public abstract class Passenger {
    private String id;
    private Ticket ticket;
    private Benefits benefits;
    private int priority = 0;

    /**
     * Default constructor
     * (has to create instances of the Ticket and Benefits classes)
     */
    public Passenger() {
        ticket = new Ticket();
        benefits = new Benefits();
    }

    /**
     * Constructor
     * (has to create instances of the Ticket and Benefits classes)<p>
     * Sets the class' parameters as follows:
     *
     * @param id                char 'f', 'g' or 's' (family, group or single) + number
     * @param ticket            characters: 'b', 'p', 'e' (ticket type: Business, Premium, Economic)
     * @param boarding_priority true or false
     * @param special_needs     true or false
     */
    public Passenger(String id, char ticket, boolean boarding_priority, boolean special_needs) {

        this.ticket = new Ticket(ticket);
        benefits = new Benefits(boarding_priority, special_needs);
        setId(id);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTicket(char ticket) {
        this.ticket.setTicket(ticket);
    }

    public char getTicket() {
        return ticket.getTicket();
    }

    public void setBoardingPriority(boolean bp) {
        benefits.setBoardingPriority(bp);
    }

    public boolean getBoardingPriority() {
        return benefits.getBoardingPriority();
    }

    public void setSpecialNeeds(boolean special_needs) {
        benefits.setSpecialNeeds(special_needs);
    }

    public boolean getSpecialNeeds() {
        return benefits.getSpecialNeeds();
    }


    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    /**
     * abstract method - implemented in subclass Person
     *
     * @return boarding priority based on all attributes, except id
     */
    public abstract int calculatePriority();

}

class Ticket {
    private char ticket;

    Ticket() {

    }

    Ticket(char ticket) {
        this.ticket = ticket;
    }

    public void setTicket(char ticket) {
        this.ticket = ticket;
    }

    public char getTicket() {
        return ticket;
    }
}

class Benefits {
    private boolean boarding_priority;
    private boolean special_needs;

    public Benefits() {

    }

    public Benefits(boolean boarding_priority, boolean special_needs) {
        this.boarding_priority = boarding_priority;
        this.special_needs = special_needs;
    }

    public void setBoardingPriority(boolean boarding_priority) {
        this.boarding_priority = boarding_priority;
    }

    public boolean getBoardingPriority() {
        return boarding_priority;
    }

    public void setSpecialNeeds(boolean sn) {
        special_needs = sn;
    }

    public boolean getSpecialNeeds() {
        return special_needs;
    }
}
