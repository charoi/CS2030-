package cs2030.simulator;

/**
 * This event represents a server resting, after {@link DoneEvent}.
 */
public class RestEvent extends Event {

    /**
     * Constructor that initialises the rest event of a server.
     * @param c         Customer's ID used in selecting priority in EventRunner.
     * @param serverID  Identifier of resting server.
     * @param restTime  The duration of rest.
     */
    public RestEvent(Customer c, int serverID, double restTime) {
        //generate rest time, then add it to the server_back event
        super(c, x -> {
            Server s = x.find(y -> y.getServerIdentifier() == serverID).get();
            Server v = s.of(s.getServerIdentifier(), false, true,
                    s.getNextAvailTime() + restTime,
                    s.getCustomersInQueue(), s.getRand());
            return Pair.of(x.replace(v), new BackEvent(c, serverID, v.getNextAvailTime()));
        }, c.serviceStarts(), EventType.REST, serverID);
    }

}
