package cs2030.simulator;

/**
 * An event where a {@link Server} comes back from resting.
 */
public class BackEvent extends Event {

    /**
     * Initialises the Service Back Event. Executing this Event should make isResting in the
     * server to be false, and update his/her availability.
     * @param c                Customer used in EventComparator for priority.
     * @param serverID         the identity of the server that returned from resting.
     * @param backTime         Time the server will finish resting.
     */
    public BackEvent(Customer c, int serverID, double backTime) {
        super(c,  x -> {
            Server s = x.find(y -> y.getServerIdentifier() == serverID).get();
            Server v = s.of(s.getServerIdentifier(), true, false,
                    s.getNextAvailTime(), s.getCustomersInQueue(), s.getRand());
            return Pair.of(x.replace(v), new EndEvent(c));
        }, backTime, EventType.BACK, serverID);
    }

}