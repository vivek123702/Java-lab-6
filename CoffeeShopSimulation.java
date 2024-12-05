import java.util.LinkedList;
import java.util.Queue;

class CounterEmptyException extends Exception {
    public CounterEmptyException(String message) {
        super(message);
    }
}

class CoffeeShop {
    private final int MAX_CAPACITY = 3; // Maximum cups on the counter
    private final Queue<String> counter = new LinkedList<>();

    // Barista (Producer) prepares coffee
    public synchronized void prepareCoffee(String baristaName) throws InterruptedException {
        while (counter.size() == MAX_CAPACITY) {
            System.out.println(baristaName + " is waiting. Counter is full.");
            wait(); // Wait until space becomes available
        }
        counter.add("Coffee");
        System.out.println(baristaName + " prepared coffee. Counter: " + counter.size());
        notifyAll(); // Notify consumers and reviewer
    }

    // Customer (Consumer) picks up coffee
    public synchronized void pickUpCoffee(String customerName) throws InterruptedException, CounterEmptyException {
        while (counter.isEmpty()) {
            System.out.println(customerName + " is waiting. Counter is empty.");
            wait(); // Wait until coffee is available
        }
        if (counter.poll() == null) {
            throw new CounterEmptyException("Counter is empty! Cannot pick up coffee.");
        }
        System.out.println(customerName + " picked up coffee. Counter: " + counter.size());
        notifyAll(); // Notify baristas and reviewer
    }

    // Coffee Reviewer samples coffee
    public synchronized void sampleCoffee(String reviewerName) throws InterruptedException, CounterEmptyException {
        while (counter.isEmpty()) {
            System.out.println(reviewerName + " is waiting. Counter is empty.");
            wait(); // Wait until coffee is available
        }
        if (counter.poll() == null) {
            throw new CounterEmptyException("Counter is empty! Cannot sample coffee.");
        }
        System.out.println(reviewerName + " sampled coffee. Counter: " + counter.size());
        notifyAll(); // Notify baristas and customers
    }
}

public class CoffeeShopSimulation {

    public static void main(String[] args) {
        CoffeeShop coffeeShop = new CoffeeShop();

        // Barista 1 prepares 2 coffees
        Thread barista1 = new Thread(() -> {
            try {
                coffeeShop.prepareCoffee("Barista 1");
                coffeeShop.prepareCoffee("Barista 1");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Barista 2 prepares 3 coffees
        Thread barista2 = new Thread(() -> {
            try {
                coffeeShop.prepareCoffee("Barista 2");
                coffeeShop.prepareCoffee("Barista 2");
                coffeeShop.prepareCoffee("Barista 2");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Customer 1 picks up 1 coffee
        Thread customer1 = new Thread(() -> {
            try {
                coffeeShop.pickUpCoffee("Customer 1");
            } catch (InterruptedException | CounterEmptyException e) {
                System.err.println(e.getMessage());
            }
        });

        // Customer 2 picks up 2 coffees
        Thread customer2 = new Thread(() -> {
            try {
                coffeeShop.pickUpCoffee("Customer 2");
                coffeeShop.pickUpCoffee("Customer 2");
            } catch (InterruptedException | CounterEmptyException e) {
                System.err.println(e.getMessage());
            }
        });

        // Customer 3 picks up 1 coffee
        Thread customer3 = new Thread(() -> {
            try {
                coffeeShop.pickUpCoffee("Customer 3");
            } catch (InterruptedException | CounterEmptyException e) {
                System.err.println(e.getMessage());
            }
        });

        // Coffee Reviewer samples 1 coffee
        Thread reviewer = new Thread(() -> {
            try {
                coffeeShop.sampleCoffee("Coffee Reviewer");
            } catch (InterruptedException | CounterEmptyException e) {
                System.err.println(e.getMessage());
            }
        });

        // Start the simulation
        barista1.start();
        barista2.start();
        customer1.start();
        customer2.start();
        customer3.start();
        reviewer.start();

        try {
            // Wait for all threads to complete
            barista1.join();
            barista2.join();
            customer1.join();
            customer2.join();
            customer3.join();
            reviewer.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Simulation complete.");
    }
}
