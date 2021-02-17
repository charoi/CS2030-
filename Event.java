package cs2030.simulator;

import java.util.function.Function;

/**
 * Abstract event class to serve as a template for its subclasses, which include ArriveEvent, 
 * ServeEvent, WaitEvent, LeaveEvent, DoneEvent, RestEvent, BackEvent, and EndEvent.
 *
 * <p>Has methods to allow for ease of getting certain values such as customers and 
 * servers, used in the various subclasses.
 */
public abstract class Event {

    /**
     * Immutable customer and List of servers, useful for the subclasses to keep track of 
     * customer and state of servers.
     */
    private final Customer customer;
    private final double eventTime;
    private final Function<Shop, Pair<Shop, Event>> func;
    private final EventType et;
    private final int serverID;

    /**
     * Event class constructor used to keep track of customers and list of servers.
     * 
     * <p>Since this is abstract, Event constructor is only a template for subclasses.
     * @param customer  Initialises the customer that is being focused on.
     * @param func      Represents the function used in execute for transition of states.
     * @param eventTime Represents the start time of the event.
     * @param et        Reflects state of the Event.
     */
    public Event(Customer customer, Function<Shop, Pair<Shop, Event>> func,
                 double eventTime, EventType et) {
        this.func = func;
        this.customer = customer;
        this.eventTime = eventTime;
        this.et = et;
        this.serverID = 0;
    }

    /**
     * Event class constructor used to keep track of customers and list of servers.
     *
     * <p>Since this is abstract, Event constructor is only a template for subclasses.
     * @param customer  Initialises the customer that is being focused on.
     * @param func      Represents the function used in execute for transition of states.
     * @param eventTime Represents the start time of the event.
     * @param et        Reflects state of the Event.
     * @param serverID  Represents the ID of the server concerned with the event.
     */
    public Event(Customer customer, Function<Shop, Pair<Shop, Event>> func, 
            double eventTime, EventType et, int serverID) {
        this.func = func;
        this.customer = customer;
        this.eventTime = eventTime;
        this.et = et;
        this.serverID = serverID;
    }

    /**
     * Getter methods.
     */
    public Customer getCustomer() {
        return this.customer;
    }

    public EventType getEventType() {
        return this.et;
    }

    public int getServerID() {
        return this.serverID;
    }

    public double eventStartTime() {
        return this.eventTime;
    }

    /**
     * The method which transitions one event to another state.
     * @param shop  Given shop for which the events occur in.
     * @return      The next event that happens give the state of the shop
     *              and the customers.
     */
    final Pair<Shop, Event> execute(Shop shop) {
        return this.func.apply(shop);
    }

}
