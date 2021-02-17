package cs2030.simulator;

import java.util.function.Supplier;
import java.util.Optional;

/**
 * Represents the customers that visit a {@link Shop}.
 */
public class Customer {

    /**
     * Immutable fields to be used within this class.
     */
    private final int customerID;
    private final double arrivalTime;
    private final Supplier<Double> servedTime;
    private final int serverType;
    private final boolean isGreedy;

    /**
     * Initialises the customer object.
     * @param customerID  An integer representing a unique customer
     * @param arrivalTime The time of arrival of a customer
     */
    public Customer(int customerID, double arrivalTime) {
        this.customerID = customerID;
        this.arrivalTime = arrivalTime;
        this.servedTime = () -> arrivalTime;
        this.serverType = 0;
        this.isGreedy = false;
    }

    /**
     * Overloaded constructor to initialise the customer object.
     * @param customerID    An integer representing a unique customer
     * @param arrivalTime   The time of arrival of a customer
     * @param isGreedy      Whether a customer is a Greedy or Typical customer
     */
    public Customer(int customerID, double arrivalTime, boolean isGreedy) {
        this.customerID = customerID;
        this.arrivalTime = arrivalTime;
        this.servedTime = () -> arrivalTime;
        this.serverType = 0;
        this.isGreedy = isGreedy;
    }

    /**
     * Overloaded constructor to initialise the customer object.
     * @param customerID    An integer representing a unique customer
     * @param arrivalTime   The time of arrival of a customer
     * @param servedTime    What time a customer MAY be served at, lazily evaluated.
     * @param isGreedy      Whether a customer is a Greedy or Typical customer
     */
    public Customer(int customerID, double arrivalTime, Supplier<Double> servedTime,
                    boolean isGreedy) {
        this.customerID = customerID;
        this.arrivalTime = arrivalTime;
        this.servedTime = servedTime;
        this.serverType = 0;
        this.isGreedy = isGreedy;
    }

    /**
     * Overloaded constructor to initialise the customer object.
     * @param customerID    An integer representing a unique customer
     * @param arrivalTime   The time of arrival of a customer
     * @param servedTime    What time a customer MAY be served at, lazily evaluated.
     * @param serverType    Whether the customer serving is human or self-checkout.
     *                      (0 representing human servers and
     *                      1 representing self-checkout servers)
     * @param isGreedy      Whether a customer is a Greedy or Typical customer
     */
    public Customer(int customerID, double arrivalTime, Supplier<Double> servedTime,
                    int serverType, boolean isGreedy) {
        this.customerID = customerID;
        this.arrivalTime = arrivalTime;
        this.servedTime = servedTime;
        this.serverType = serverType;
        this.isGreedy = isGreedy;
    }
    
    /**
     * Getter methods.
     */
    public int getCustomerID() {
        return this.customerID;
    }

    public double getArrivalTime() {
        return this.arrivalTime;
    }

    public double serviceStarts() {
        return this.servedTime.get();
    }

    public int getServerType() {
        return this.serverType;
    }

    public boolean isGreedy() {
        return this.isGreedy;
    }

    /**
     * Creates new customer and updates time finished serving.
     * @param time  The time that a customer MAY be served at.
     * @return      A new customer with a serve time lazily stored
     *              in its new instance.
     */
    public Customer freeze(double time) {
        return new Customer(this.customerID, this.arrivalTime, () -> time,
                this.serverType, this.isGreedy);
    }

    /**
     * Updates the type of server that a customer is served by/waiting for.
     * @param type  The type of server (0 representing human servers and
     *              1 representing self-checkout servers)
     * @return      A new customer with an updated serverType.
     */
    public Customer updateType(int type) {
        return new Customer(this.customerID, this.arrivalTime,
                this.servedTime, type, this.isGreedy);
    }

    /**
     * Method that handles the logic for which server to wait for, if any.
     * @param s     The Shop for which the customer arrives at.
     * @return      A Pair object describing whether a server can wait in the
     *              shop, or should leave, and which server he/she might want
     *              to wait for.
     */
    public Pair<Boolean, Optional<Server>> canWait(Shop s) {
        int limit = s.getMaxQueue();
        Optional<Server> wait = s.find(x -> (x.getCustomersInQueue() < limit));
        if (this.isGreedy) {
            wait = s.findShortestQueue();
        }
        if (wait.isPresent()) {
            return Pair.of(true,wait);
        } else {
            return Pair.of(false,wait);
        }
    }

    @Override 
    public String toString() {
        String greedy = "";
        if (this.isGreedy) {
            greedy = "(greedy)";
        }
        return this.customerID + greedy + " arrives at " + this.arrivalTime;
    }

}
