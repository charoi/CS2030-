package cs2030.simulator;

import java.util.Optional;

/**
 * The first event that happens, simulates a {@link Customer} arriving at a food establishment.
 */
public class ArriveEvent extends Event {

    /**
     * Immutable fields here, used for toString method.
     */
    private final Customer customer;

    /**
     * Initialises the Arrival Event.
     * @param customer represents the arriving customer
     */
    public ArriveEvent(Customer customer) {
        super(customer, x -> {
            Optional<Server> avail = x.find(y -> y.isAvailable() && !y.isResting());
            Pair<Boolean, Optional<Server>> wait = customer.canWait(x);
            if (avail.isPresent()) {
                Server s = avail.get();
                //checks if its self checkout
                if (s.getServerIdentifier() > x.numOfHumans()) {
                    Customer cust = customer.updateType(1);
                    SelfCheckout q = (SelfCheckout) s.of(s.getServerIdentifier(), true, false,
                            s.getNextAvailTime(),0,s.getRand());
                    return Pair.of(x.replace(q).updateSelfQueues(1), new ServeEvent(cust,
                            s.getServerIdentifier()));
                }
                Server n = s.of(s.getServerIdentifier(), true, false,
                        s.getNextAvailTime(),1, s.getRand());
                return Pair.of(x.replace(n), new ServeEvent(customer,
                            s.getServerIdentifier()));
            } else if (wait.first()) {
                //gets the server to wait for, since True in else if means the server is present
                Server s = wait.second().get();
                if (s.getServerIdentifier() > x.numOfHumans()) {
                    //indicates self checkout
                    Customer selfCust = customer.updateType(1);
                    SelfCheckout c = (SelfCheckout) s;
                    return Pair.of(x.replace(c), new WaitEvent(selfCust, s.getServerIdentifier()));
                } else {
                    return Pair.of(x, new WaitEvent(customer, s.getServerIdentifier()));
                }
            } else {
                return Pair.of(x, new LeaveEvent(customer));
            }
        }, customer.getArrivalTime(), EventType.ARRIVE);
        this.customer = customer;
    }

    @Override
    public String toString() {
        String greedy = "";
        if (customer.isGreedy()) {
            greedy = "(greedy)";
        }
        return String.format("%.3f", this.customer.getArrivalTime()) + " " + 
            this.customer.getCustomerID() + greedy + " arrives";
    }

}
