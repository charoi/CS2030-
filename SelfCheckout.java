package cs2030.simulator;

import java.util.function.Supplier;

/**
 * SelfCheckout represent the self-checkout counters in a {@link Shop}.
 */
public class SelfCheckout extends Server {

    /**
     * Initialises a server's status.
     * @param identifier         gives its server ID
     * @param isAvailable        whether Self-checkout is free for immediate service.
     * @param nextAvailableTime  gives the time a server is done serving
     *                           his current customer, or when he is available
     *                           to waiting customers.
     * @param inQueue            Gives the common queue length in self-checkout queue
     * @param rand               RandomGenerator to determine service time
     */
    public SelfCheckout(int identifier, boolean isAvailable,
                       double nextAvailableTime,
                       int inQueue, Supplier<RandomGenerator> rand) {
        super(identifier, isAvailable, false, nextAvailableTime,
                inQueue, rand);
    }

    /**
     * Method to create a self-checkout counter.
     * @param identifier         Gives its server ID
     * @param isAvailable        Whether Self-checkout is free for immediate service.
     * @param nextAvailableTime  Gives the time a server is done serving
     *                           its current customer, or when it is available
     *                           to waiting customers.
     * @param inQueue            Gives the common queue length in self-checkout queue
     * @param rand               RandomGenerator to determine service time
     * @return                   A SelfCheckout counter
     */
    @Override
    public SelfCheckout of(int identifier, boolean isAvailable,
                          boolean isResting, double nextAvailableTime,
                          int inQueue, Supplier<RandomGenerator> rand) {
        return new SelfCheckout(identifier, isAvailable,
                nextAvailableTime,
                inQueue, rand);
    }

    /**
     * Getter methods.
     */
    @Override
    public double probRest() {
        return 1.0;
    }

    @Override
    public double restTime() {
        return 0.0;
    }

}
