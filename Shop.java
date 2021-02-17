package cs2030.simulator;

import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * This class represents a Shop(like an F n B restaurant) 
 * that has human and self-checkout {@link Server} types.
 */
public class Shop {

    /**
     * Immutable parameters that store the properties of the shop.
     * These properties are useful in determining the servers in the
     * shop. limits of queues, probability to compare with randomly
     * generated probability after each service, for resting between
     * services, and number of human servers.
     */
    private final List<Server> servers;
    private final int maxQueue;
    private final double probRest;
    private final int humans;

    /**
     * Constructor to initialise a shop.
     * @param n     integer that represents number of human servers to generate.
     */
    public Shop(int n) {
        //this constructor is for lower levels
        this.maxQueue = 1;
        this.probRest = 0.0;
        this.humans = n;
        if (n == 0) {
            this.servers = new ArrayList<>();
        } else {
            this.servers = IntStream.rangeClosed(1,n).boxed()
                    .map(x -> new Server(x,true,false,0, 0, () -> null))
                    .collect(Collectors.toList());
        }
    }

    /**
     * Constructor to initialise a shop.
     * @param s     List of servers to initialise a shop with.
     */
    public Shop(List<Server> s) {
        this.servers = new ArrayList<>(s);
        this.maxQueue = 1;
        this.probRest = 0.0;
        this.humans = s.size();
    }

    /**
     * Constructor to initialise a shop.
     * @param s         List of servers to initialise a shop with.
     * @param humans    Number of human servers in the shop.
     *                  Can be used to infer number of self-checkout
     *                  counters with this.
     * @param maxQueue  The maximum number of customers waiting in any queue.
     * @param probRest  Probability to determine whether a human server rests,
     *                  after his/her service.
     */
    public Shop(List<Server> s, int humans, int maxQueue, double probRest) {
        this.servers = s;
        this.humans = humans;
        this.maxQueue = maxQueue;
        this.probRest = probRest;
    }

    /**
     * Getter methods.
     */
    public int getMaxQueue() {
        return this.maxQueue;
    }

    public double getProbRest() {
        return this.probRest;
    }

    public List<Server> getServers() {
        return this.servers;
    }

    public int numOfHumans() {  //represents number of human servers
        return this.humans;
    }

    /**
     * Used for finding the first server that passes a given Predicate.
     * @param pred      Condition for which to look for a server.
     * @return          An Optional representing the server found, or
     *                  empty if no such server exists.
     */
    public Optional<Server> find(Predicate<Server> pred) {
        return this.servers.stream().filter(pred).findFirst();
    }

    /**
     * Used to find the queue with shortest length. This is primarily for
     * greedy customers.
     * @return          Server with the shortest queue.
     */
    public Optional<Server> findShortestQueue() {
        return this.servers.stream()
                .filter(x -> x.getCustomersInQueue() < maxQueue)
                .min(servers.get(0));
    }

    /**
     * Replaces a current {@link Server} with an updated server, based on server ID.
     * @param s     The new server to replace the old server with.
     * @return      An updated shop with updated servers.
     */
    public Shop replace(Server s) {
        Optional<Server> res = this.find(x -> x.getServerIdentifier() == s.getServerIdentifier());
        if (res.isEmpty()) {
            return this;
        } else {
            int position = this.servers.indexOf(res.get());
            List<Server> temp = new ArrayList<>(this.servers);
            temp.set(position, s);
            return new Shop(temp, this.humans, this.maxQueue, this.probRest);
        }
    }

    /**
     * Updates all self checkout counter queues when a customer waits or is served.
     * @param add       The integer value to apply to all self-checkout counters,
     *                  so as to update their shared queue length.
     * @return          An updated shop with updated self-checkout queue.
     */
    public Shop updateSelfQueues(int add) {
        //stream of self-checkout counters
        List<Server> temp = new ArrayList<>(this.servers);
        Stream<Server> humanStream = temp.stream()
                .filter(x -> x.getServerIdentifier() <= this.humans);
        Stream<Server> selfCheck = temp.stream()
                .filter(x -> x.getServerIdentifier() > this.humans)
                .map(y -> new SelfCheckout(y.getServerIdentifier(),
                        y.isAvailable(), y.getNextAvailTime(),
                        y.getCustomersInQueue() + add, y.getRand()));
        List<Server> res = Stream.concat(humanStream, selfCheck).collect(Collectors.toList());
        return new Shop(res, this.humans, this.maxQueue, this.probRest);
    }

    @Override
    public String toString() {
        return this.servers.toString();
    }

}
