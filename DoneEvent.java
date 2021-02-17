package cs2030.simulator;

/**
 * Simulates a completion of service, and should only be called 
 * from {@link ServeEvent}.
 */
public class DoneEvent extends Event {

    /**
     * Immutable fields here, used for toString method.
     */
    private final int serverID; 
    private final Customer customer;
    private final double doneTime;

    /**
     * Initialises a DONE event, indicating service is over for a customer.
     * @param customer gives customer that was done being served
     * @param serverID the actual server who just finished serving
     *
     */
    DoneEvent(Customer customer, int serverID) {
        super(customer, x -> {
            Server s = x.find(y -> y.getServerIdentifier() == serverID).get();
            double probRest = s.probRest();
            if (serverID > x.numOfHumans()) { //typecasting for self checkout counter
                SelfCheckout b = (SelfCheckout) s.of(serverID, true, false,
                        customer.serviceStarts(), s.getCustomersInQueue(), s.getRand());
                return Pair.of(x.replace(b), new EndEvent(customer));
            }
            if (probRest < x.getProbRest()) { //logic here to decide if should rest
                //make v a resting server
                double restTime = s.restTime();
                Server v = s.of(serverID, false, true,
                        customer.serviceStarts(), s.getCustomersInQueue(), s.getRand());
                return Pair.of(x.replace(v), new RestEvent(customer, serverID, restTime));
            }
            //if server is human and not resting next, then go to this event.
            Server n = s.of(serverID, true, false, customer.serviceStarts(),
                    s.getCustomersInQueue(), s.getRand());
            return Pair.of(x.replace(n), new EndEvent(customer));
        }, customer.serviceStarts(), EventType.DONE, serverID);
        this.customer = customer;
        this.serverID = serverID;
        this.doneTime = customer.serviceStarts();
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
        return String.format("%.3f", this.doneTime) + " " +
                    this.customer.getCustomerID() + greedy +
                    " done serving by " + type + this.serverID;

    }

}
