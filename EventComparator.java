package cs2030.simulator;

import java.util.Comparator;

/**
 * Compares {@link Event} subclasses by their timing and gives order for Priority Queue
 * in {@link EventRunner}.
 */
public class EventComparator implements Comparator<Event> {

    /**
     * Does comparison of events by their timings, if
     * same timing, compares for the smaller customer number.
     * @param event1 gives first event 
     * @param event2 gives second event to compare with
     * @return an integer that can be positive, negative, or
     *         zero which indicates order in Priority Queue
     *         in EventRunner class
     */
    @Override
    public int compare(Event event1, Event event2) {
        double timeDiff = event1.eventStartTime() - event2.eventStartTime();
        if (timeDiff == 0) {
            return event1.getCustomer().getCustomerID() - event2.getCustomer().getCustomerID();
        } else if (timeDiff < 0) {
            return -1;
        } else {
            return 1;
        }
    }

}
