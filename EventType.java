package cs2030.simulator;

/**
 * Enum class which keeps track of state of an {@link Event},
 * useful for {@link EventRunner} class when run() is used.
 */
public enum EventType {
    
    /**
     * The 8 constants used to indicate the state of an event.
     */
    ARRIVE(1),
    SERVE(2),
    WAIT(3),
    LEAVE(4),
    DONE(5),
    REST(6),
    BACK(7),
    END(8);
    
    private final int value;
    
    /**
     * Initialises the format for all EventType constants.
     * @param value indicates the integer value tagged to the state
     */
    EventType(int value) {
        this.value = value;
    }
    
    /**
     * Getter method. 
     */
    public int getValue() {
        return this.value;
    }

}
