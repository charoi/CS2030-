package cs2030.simulator;

/**
 * Class which represents a {@link Customer} waiting for his turn
 * to be served.
 */
public class WaitEvent extends Event {

    /** 
     * Immutable fields here, used in toString.
     */
    private final Customer customer;
    private final int serverID;
    private final double eventStartTime;
   
    /** 
     * Initialises a WAIT event.
     * @param customer Gives the waiting customer.
     * @param serverID Indicates the server the customer is waiting for.
     */
    public WaitEvent(Customer customer, int serverID) {
        super(customer, x -> {
            Server s = x.find(y -> y.getServerIdentifier() == serverID).get();
            //indicates self checkout, for which executing wait is not supposed
            //to generate serve, hence we will store the customer in a List in EventRunner
            //and toggle to EndEvent to prevent delegation of specific counter to wait for.
            if (serverID > x.numOfHumans()) {
                SelfCheckout c = (SelfCheckout) s;
                SelfCheckout n = c.of(serverID, false, false, c.getNextAvailTime(),
                        c.getCustomersInQueue(), c.getRand());
                Customer d = customer.freeze(c.getNextAvailTime());
                return Pair.of(x.replace(n).updateSelfQueues(1), new EndEvent(d));
            }
            Server n = s.of(serverID, false, s.isResting(), s.getNextAvailTime(),
                    s.getCustomersInQueue() + 1, s.getRand());
            Customer c = customer.freeze(s.getNextAvailTime());
            return Pair.of(x.replace(n), new ServeEvent(c, serverID));
            
        }, customer.getArrivalTime(), EventType.WAIT);
        this.eventStartTime = customer.getArrivalTime();
        this.customer = customer;
        this.serverID = serverID;       
    }
   
    @Override
    public String toString() {
        String type = "server ";
        String greedy = "";
        if (customer.getServerType() == 1) {
            type = "self-check ";
        }
        if (customer.isGreedy()) {
            greedy = "(greedy)";
        }
        return String.format("%.3f", this.eventStartTime) + " " +
                this.customer.getCustomerID() + greedy +
                " waits to be served by " + type + this.serverID;
    }

}
