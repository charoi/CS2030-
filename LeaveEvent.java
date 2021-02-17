package cs2030.simulator;

/**
 * An event where the customer leaves the shop,
 * where no servers are free and all queues are full.
 */
public class LeaveEvent extends Event {

    /** 
     * Immutable fields here, used for toString.
     */
    private final Customer customer;
    private final double eventStartTime;

    /**
     * Initialises a customer leaving the establishment.
     * @param customer gives the customer that left
     */
    public LeaveEvent(Customer customer) {
        super(customer, x -> Pair.of(x, new EndEvent(customer)),
                customer.getArrivalTime(), EventType.LEAVE);
        this.customer = customer;
        this.eventStartTime = customer.getArrivalTime();
    }
    
    @Override 
    public String toString() {
        String greedy = "";
        if (customer.isGreedy()) {
            greedy = "(greedy)";
        }
        return String.format("%.3f", this.eventStartTime) + " " + 
                this.customer.getCustomerID() + greedy + " leaves";
    }

}
