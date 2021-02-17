package cs2030.simulator;

/**
 * Generic Pair class serves to hold two objects of different types together.
 *
 * <p>Used in execute method in {@link Event}, and in the
 * canWait method in {@link Customer}.</p>
 * @param <T>   Type of first item.
 * @param <U>   Type of second item.
 */
public class Pair<T,U> {

    /**
     * Immutable fields representing the first and second generic items in this Pair class.
     */
    private final T t;
    private final U u;

    /**
     * Initialises the Pair class.
     * @param t     Generic first item in pair.
     * @param u     Generic second item in pair.
     */
    private Pair(T t, U u) {
        this.t = t;
        this.u = u;
    }

    /**
     * Public method that calls the private constructor.
     * @param t     Generic first item in pair.
     * @param u     Generic second item in pair.
     * @param <T>   Type of first item.
     * @param <U>   Type of second item.
     * @return      New Pair object.
     */
    public static <T,U> Pair<T,U> of(T t, U u) {
        return new Pair<>(t,u);
    }

    /**
     * Getter method.
     * @return     First item in pair.
     */
    public T first() {
        return this.t;
    }

    /**
     * Getter method.
     * @return     Second item in pair.
     */
    public U second() {
        return this.u;
    }

}
