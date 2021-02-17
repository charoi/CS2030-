import cs2030.simulator.EventRunner;

/**
 * Driver class which runs the cs2030.simulator package to execute events.
 *
 * <p>Argument length varies based on inputs, and the parameters will vary accordingly.
 */
class Main {

    /**
     * Driver class.
     * @param args the inputs for which to determine the fields passed into EventRunner.
     */
    public static void main(String[] args) {
        int numOfArgs = args.length; 
        int seed = Integer.parseInt(args[0]);
        int numOfServers = Integer.parseInt(args[1]);
        int selfCheckout;
        int maxQueue;
        int numOfCustomers;
        double lambda;
        double miu;
        double rho;
        double probRest;
        double probGreedy;
        if (numOfArgs == 5) {  //level 3
            selfCheckout = 0;
            maxQueue = 1;
            numOfCustomers = Integer.parseInt(args[2]);
            lambda = Double.parseDouble(args[3]);
            miu = Double.parseDouble(args[4]);
            rho = 1;
            probRest = 0.0;
            probGreedy = 0.0;
        } else if (numOfArgs == 6) {  //level 4
            selfCheckout = 0;
            maxQueue = Integer.parseInt(args[2]);
            numOfCustomers = Integer.parseInt(args[3]);
            lambda = Double.parseDouble(args[4]);
            miu = Double.parseDouble(args[5]);
            rho = 1;
            probRest = 0.0;
            probGreedy = 0.0;
        } else if (numOfArgs == 8) {  //level 5
            selfCheckout = 0;
            maxQueue = Integer.parseInt(args[2]);
            numOfCustomers = Integer.parseInt(args[3]);
            lambda = Double.parseDouble(args[4]);
            miu = Double.parseDouble(args[5]);
            rho = Double.parseDouble(args[6]);
            probRest = Double.parseDouble(args[7]);
            probGreedy = 0.0;
        } else if (numOfArgs == 9) { //level 6
            selfCheckout = Integer.parseInt(args[2]);;
            maxQueue = Integer.parseInt(args[3]);
            numOfCustomers = Integer.parseInt(args[4]);
            lambda = Double.parseDouble(args[5]);
            miu = Double.parseDouble(args[6]);
            rho = Double.parseDouble(args[7]);
            probRest = Double.parseDouble(args[8]);
            probGreedy = 0.0;
        } else { //level 7
            selfCheckout = Integer.parseInt(args[2]);;
            maxQueue = Integer.parseInt(args[3]);
            numOfCustomers = Integer.parseInt(args[4]);
            lambda = Double.parseDouble(args[5]);
            miu = Double.parseDouble(args[6]);
            rho = Double.parseDouble(args[7]);
            probRest = Double.parseDouble(args[8]);
            probGreedy = Double.parseDouble(args[9]);
        }
        //runs EventRunner to simulate discrete events
        EventRunner simulator = new EventRunner(numOfCustomers, numOfServers, seed,
                lambda, miu, rho, maxQueue, probRest, probGreedy, selfCheckout);
        simulator.run();
    }

}
