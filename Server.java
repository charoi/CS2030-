package cs2030.simulator;

import java.util.Comparator;
import java.util.function.Supplier;

/**
 * Servers represent the servers in a {@link Shop}.
 */
public class Server implements Comparator<Server> {

    /**
     * Immutable fields that track status of a Server.
     */
    private final int identifier;
    private final boolean isAvailable;
    private final boolean isResting;
    private final double nextAvailableTime;
    private final int customersInQueue;
    private final Supplier<RandomGenerator> rand;

    /**
     * Initialises a server's status.
     * @param identifier         Gives his server ID (or tag).
     * @param isAvailable        Whether Server is free for immediate service.
     * @param isResting          Whether Server is resting
     *                           in line.
     * @param nextAvailableTime  Gives the time a server is done serving.
     *                           his current customer, or when he is available
     *                           to waiting customers.
     * @param inQueue            Represents number of servers in queue.
     * @param rand               Random generator to create the properties of a server,
     *                           ie. the serve time, probability of resting, and
     *                           rest rate.
     */
    public Server(int identifier, boolean isAvailable, boolean isResting,
                  double nextAvailableTime, int inQueue, Supplier<RandomGenerator> rand) {
        this.identifier = identifier;
        this.isAvailable = isAvailable;
        this.isResting = isResting;
        this.nextAvailableTime = nextAvailableTime;
        this.customersInQueue = inQueue;
        this.rand = rand;
    }

    /**
     * Initialises a server's status (For project 1, and lower levels).
     * @param identifier         Gives his server ID (or tag).
     * @param isAvailable        Whether Server is free for immediate service.
     * @param nextAvailableTime  Gives the time a server is done serving.
     *                           his current customer, or when he is available
     *                           to waiting customers.
     */
    public Server(int identifier, boolean isAvailable,
                  boolean hasWaitingCustomer, double nextAvailableTime) {
        this.identifier = identifier;
        this.isAvailable = isAvailable;
        this.isResting = false;
        this.nextAvailableTime = nextAvailableTime;
        this.customersInQueue = hasWaitingCustomer ? 1 : 0;
        this.rand = null;
    }

    /**
     * Method to create a server.
     * @param identifier         Gives the server ID
     * @param isAvailable        Whether Self-checkout is free for immediate service.
     * @param isResting          Gives whether the server is resting.
     * @param nextAvailableTime  Gives the time a server is done serving
     *                           his current customer, or when he is available
     *                           to waiting customers.
     * @param inQueue            Gives the common queue length in self-checkout queue.
     * @param rand               RandomGenerator to determine service time.
     * @return                   A Server.
     */
    public Server of(int identifier, boolean isAvailable,
                              boolean isResting, double nextAvailableTime,
                              int inQueue, Supplier<RandomGenerator> rand) {
        return new Server(identifier, isAvailable,
                isResting, nextAvailableTime,
                inQueue, rand);
    }

    /**
     * Getter method that eagerly evaluates the random rest probability.
     * @return Probability of resting.
     */
    public double probRest() {
        if (this.rand == null) {
            return 0.0;
        }
        return this.rand.get().genRandomRest();
    }

    /**
     * Getter method that eagerly evaluates the random rest timing.
     * @return Duration of rest.
     */
    public double restTime() {
        if (this.rand == null) {
            return 0.0;
        }
        return this.rand.get().genRestPeriod();
    }

    /**
     * Getter methods.
     */
    public int getCustomersInQueue() {
        return this.customersInQueue;
    }

    public double getNextAvailTime() {
        return this.nextAvailableTime;
    }

    public Supplier<RandomGenerator> getRand() {
        return this.rand;
    }

    public int getServerIdentifier() {
        return this.identifier;
    }

    public boolean isAvailable() {
        return this.isAvailable;
    }

    public boolean isResting() {
        return this.isResting;
    }

    /**
     * Formats available time as a String.
     * @return nextAvailableTime formatted with 3 decimal places.
     */
    public String availTime() {
        return String.format("%.3f", this.nextAvailableTime);
    }

    /**
     * Overridden compare method used to compare queue length.
     * @param s1    Server 1's queue length.
     * @param s2    Server 2's queue length.
     * @return      Negative if s1 is less than s2, 
     *              Zero if same queue length, and
     *              Positive if s1 is more than s2.
     */
    @Override
    public int compare(Server s1, Server s2) {
        return s1.customersInQueue - s2.customersInQueue;
    }

    /**
     * Reflects current status of a server, so as to simplify
     * representation of toString().
     * @return String representing availability of server
     */
    public String currentState() {
        if (isAvailable) {
            return " is available";
        } else {
            if (this.customersInQueue == 0) {
                return " is busy; available at " + availTime();
            } else {
                return " is busy; waiting customer to be served at " + availTime();
            }
        }
    }

    @Override
    public String toString() {
        return this.identifier + this.currentState();
    }

}
