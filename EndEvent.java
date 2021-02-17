package cs2030.simulator;

/** 
 * END event used to finish off both LEAVE, DONE, and
 * WAIT (for self-checkout queues), which handles cases where execute is called
 * on LEAVE, DONE, and WAIT(self-checkout).
 */
public class EndEvent extends Event {

    /**
     * Initialises the END event. Protected as this constructor should only
     * be called within the package.
     * @param customer  Customer for passing into superclass constructor.
     */
    protected EndEvent(Customer customer) {
        super(customer, x -> Pair.of(x,null), 0.0, EventType.END);
    }
  
}
