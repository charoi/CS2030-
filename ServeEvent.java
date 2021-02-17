package cs2030.simulator;

/**
 * An event where a customer gets served.
 */
public class ServeEvent extends Event {

    /**
     * Immutable fields here, used for toString method.
     */
    private final int serverID;
    private final Customer customer;

    /**
     * Initialises the Service Event. Executing this Event should reduce the number
     * of customers in queue.
     * @param customer         Customer to serve
     * @param serverID         the identity of the serving server
     *                         which comes from either ARRIVE or
     *                         WAIT events
     */
    public ServeEvent(Customer customer, int serverID) { 
       super(customer, x -> {
           Server s  = x.find(y -> y.getServerIdentifier() == serverID).get();
           /* have a check to see if server can serve, if can serve then go to serve,
           else goto wait further
            */
           if (s.isResting() || !s.isAvailable()) {
               Customer d = customer.freeze(s.getNextAvailTime());
               return Pair.of(x, new ServeEvent(d,serverID));
           } else {
               double timeStart = customer.serviceStarts();
               double serveTime = s.getRand().get().genServiceTime();
               double doneTime = serveTime + timeStart;
               Customer c = customer.freeze(doneTime);
               if (serverID > x.numOfHumans()) {
                   //indicates self checkout
                   SelfCheckout a = (SelfCheckout) s;
                   SelfCheckout b = a.of(serverID, false, false, doneTime,
                           a.getCustomersInQueue(), a.getRand());
                   return Pair.of(x.replace(b).updateSelfQueues(-1), new DoneEvent(c,serverID));
               }
               Server n = s.of(serverID, false, false, doneTime,
                       s.getCustomersInQueue() - 1, s.getRand());
               return Pair.of(x.replace(n), new DoneEvent(c, serverID));
           } 
       }, customer.serviceStarts(), EventType.SERVE, serverID);
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
        return String.format("%.3f", super.eventStartTime()) + " " +
                    this.customer.getCustomerID() + greedy + " served by " + type + this.serverID;

    }

}
