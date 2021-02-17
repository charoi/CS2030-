package cs2030.simulator;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Works like an F n B manager who runs the Priority Queue according to the input fields,
 * only called from the Main class.
 */
public class EventRunner {

    /**
     * Immutable fields that represent the various inputs fed into Main, and run by EventRunner.
     */
    private final int numOfCustomers;
    private final int numOfServers;
    private final int seed;
    private final double lambda;
    private final double miu;
    private final double rho;
    private final int maxQueue;
    private final double probRest;
    private final double probGreedy;
    private final int selfCheckout;

    /**
     * Constructor that initialises the Event Runner.
     * @param numOfCustomers  gives the number of customers that
     *                        will be arriving
     * @param numOfServers    contains the number of servers
     * @param seed            Represents the base seed for RandomGenerator
     *                        object
     * @param lambda          Represents arrival rate of customers
     * @param miu             Represents the serving rate of servers
     * @param rho             Represents the resting rate of servers
     * @param maxQueue        Contains the maximum queue length allowed in the Shop,
     *                        for all servers
     * @param probRest        Gives the probability that a server will rest after he/she
     *                        is done serving a customer
     * @param probGreedy      Gives the probability that an arriving customer is greedy
     * @param selfCheckout    Represents number of self-checkout counters in a shop
     */
    public EventRunner(int numOfCustomers, int numOfServers, int seed, double lambda, double miu,
                       double rho, int maxQueue, double probRest,
                       double probGreedy, int selfCheckout) {
        this.numOfCustomers = numOfCustomers;
        this.numOfServers = numOfServers;
        this.seed = seed;
        this.lambda = lambda;
        this.miu = miu;
        this.rho = rho;
        this.maxQueue = maxQueue;
        this.probRest = probRest;
        this.probGreedy = probGreedy;
        this.selfCheckout = selfCheckout;
    }

    /**
     * Creates an instance of the RandomGenerator class according to inputs from Main.
     * @param seed              The base seed for the RandomGenerator class
     * @param lambda            Represents arrival rate of customers
     * @param miu               Represents the serving rate of servers
     * @param rho               Represents the resting rate of servers
     * @return                  RandomGenerator class used to generate the list of customers,
     *                          servers, and to specify their associated timings.
     */
    public RandomGenerator genRandom(int seed, double lambda, double miu, double rho) {
        return new RandomGenerator(seed, lambda, miu, rho);
    }

    /**
     * Generates a list of typical and greedy customers that arrive at the shop.
     *
     * <p>Each customer has a random arrival time.
     * @param numOfCustomers    The number of customers entering a Shop
     * @param random            The RandomGenerator used to generate arrival times
     *                          and probability of a customer being greedy
     * @param greedy            Probability to decide whether each customer is greedy
     * @return                  Customer list that is used to initialise the priority queue.
     */
    public List<Customer> generateCustomersList(int numOfCustomers, RandomGenerator random,
                                                double greedy) {
        List<Double> arrivalTimes = Stream.iterate(0.0, x -> x + random.genInterArrivalTime())
            .limit(numOfCustomers).collect(Collectors.toList());
        return IntStream.rangeClosed(1, numOfCustomers)
            .mapToObj(y -> new Customer(y, arrivalTimes.get(y - 1),
                    (random.genCustomerType() < greedy))).collect(Collectors.toList());
    }

    /**
     * Creates a list of servers and adds into shop.
     * @param num           Represents number of human servers
     * @param self          Represents number of self-checkout counters
     * @param maxQueue      Determines max length of a queue
     * @param probRest      Determines the probability of a server resting
     * @param random        RandomGenerator to feed into Server class to determine
     *                      the randomised properties of a server
     * @return              a new Shop based on given parameters
     */
    public Shop generateShop(int num, int self, int maxQueue,
                             double probRest, RandomGenerator random) {
        List<Server> s;
        if (num == 0 && self == 0) {
            return new Shop(0);
        } else if (num == 0) {
            List<Server> checkOut = IntStream.rangeClosed(num + 1, num + self).boxed()
                    .map(x -> new SelfCheckout(x, true, 0,0, () -> random))
                    .collect(Collectors.toList());
            return new Shop(checkOut, num, maxQueue, probRest);
        } else if (self == 0) {
            s = IntStream.rangeClosed(1,num).boxed()
                .map(x -> new Server(x, true, false, 0, 0, () -> random))
                    .collect(Collectors.toList());
            return new Shop(s, num, maxQueue, probRest);
        } else {
            Stream<Server> humans = IntStream.rangeClosed(1,num).boxed()
                    .map(x -> new Server(x, true, false, 0, 0, () -> random));
            Stream<Server> selfServers = IntStream.rangeClosed(num + 1, num + self).boxed()
                    .map(x -> new SelfCheckout(x, true, 0,0, () -> random));
            s = Stream.concat(humans, selfServers).collect(Collectors.toList());
            return new Shop(s, num, maxQueue, probRest);
        }
    }

    /**
     * Sets the Priority Queue up with all customers that have just arrived,
     * ordered by event start time.
     * @param       customers  is the list of customers
     * @return      a Priority Queue of Events at an initial state
     */
    public PriorityQueue<Event> initialise(List<Customer> customers) {
        PriorityQueue<Event> queue = new PriorityQueue<>(new EventComparator());
        for (Customer customer : customers) {
            queue.add(new ArriveEvent(customer));
        }
        return queue;
    }

    /**
     * Runs the Discrete Event Simulation, using
     * a Priority Queue for events and a List representing customers
     * in the self-checkout queue, and prints statistics.
     *
     * <p>Each execute method updates the Shop to ensure smooth
     * execution of events.
     */
    public void run() {
        RandomGenerator random = genRandom(this.seed, this.lambda, this.miu, this.rho);
        List<Customer> customers = generateCustomersList(this.numOfCustomers,
                random, this.probGreedy);
        PriorityQueue<Event> pq = initialise(customers);
        List<Customer> selfCheck = new ArrayList<>();   //customer queue for self-checkout counters
        Shop shop = generateShop(this.numOfServers, this.selfCheckout, this.maxQueue,
                this.probRest, random);
        //statistics to track results
        int numCustomersServed = 0;
        double totalWait = 0.0;
        while (!pq.isEmpty()) {  //event runner
            Event e = pq.poll();
            //current EventType
            int currentState = e.getEventType().getValue();
            Pair<Shop, Event> result = e.execute(shop);
            shop = result.first();
            int nextState = result.second().getEventType().getValue();
            if (!(currentState == 2 && nextState == 2) && (currentState <= 5)) {
                //prints out event
                System.out.println(e);
            }
            if (currentState == 3 && e.getCustomer().getServerType() == 1) {
                //represents a wait event where the customer waits for the self checkout
                selfCheck.add(e.getCustomer());
            }
            if (currentState == 5 && e.getCustomer().getServerType() == 1
                    && !selfCheck.isEmpty()) {
                //represents a done event where the customer gets served by the next self checkout
                Customer c = selfCheck.get(0);
                Customer n = c.freeze(e.eventStartTime());
                ServeEvent serve = new ServeEvent(n, e.getServerID());
                selfCheck.remove(0);
                pq.add(serve);
                continue;
            }
            if (currentState == 2 && nextState == 5) {
                //update stats
                double timeWaited = e.eventStartTime()  
                    - e.getCustomer().getArrivalTime();
                totalWait += timeWaited;
                numCustomersServed++;
            }
            if (nextState > 1 && nextState <= 7) {
                //update the Priority Queue
                pq.add(result.second());
            }
        }
        //updates statistics
        int numLeft = customers.size() - numCustomersServed;
        double averageWait;
        if (numCustomersServed == 0) {
            averageWait = 0.0;
        } else {
            averageWait = totalWait / (double) numCustomersServed;
        }
        //prints statistics
        System.out.println("[" + String.format("%.3f", averageWait) + 
                " " + numCustomersServed + " " + numLeft + "]");
    }

}
