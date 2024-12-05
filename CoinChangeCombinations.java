import java.util.*;

public class CoinChangeCombinations {

    // Function to find all combinations and count the number of ways
    public static List<List<Integer>> findCombinations(int[] coins, int targetSum) {
        List<List<Integer>>[] dp = new List[targetSum + 1];

        // Initialize DP array
        for (int i = 0; i <= targetSum; i++) {
            dp[i] = new ArrayList<>();
        }
        dp[0].add(new ArrayList<>()); // Base case: One way to make sum 0 (no coins)

        // Compute combinations for each coin
        for (int coin : coins) {
            for (int j = coin; j <= targetSum; j++) {
                for (List<Integer> combination : dp[j - coin]) {
                    List<Integer> newCombination = new ArrayList<>(combination);
                    newCombination.add(coin);
                    dp[j].add(newCombination);
                }
            }
        }

        return dp[targetSum];
    }

    // Validate user input
    public static int[] validateAndGetInput() {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.print("Enter the number of denominations (N): ");
            int n = scanner.nextInt();
            if (n <= 0)
                throw new IllegalArgumentException("The number of denominations must be positive.");

            System.out.print("Enter the target sum: ");
            int targetSum = scanner.nextInt();
            if (targetSum < 0)
                throw new IllegalArgumentException("The target sum must be non-negative.");

            System.out.println("Enter the denominations as space-separated integers: ");
            int[] coins = new int[n];
            for (int i = 0; i < n; i++) {
                coins[i] = scanner.nextInt();
                if (coins[i] <= 0)
                    throw new IllegalArgumentException("Denominations must be positive integers.");
            }

            // Combine inputs into one array for processing
            int[] inputs = new int[n + 1];
            System.arraycopy(coins, 0, inputs, 0, n);
            inputs[n] = targetSum;
            return inputs;

        } catch (Exception e) {
            System.out.println("Invalid input: " + e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) {
        int[] inputs = validateAndGetInput();
        if (inputs != null) {
            int n = inputs.length - 1;
            int targetSum = inputs[n];
            int[] coins = new int[n];
            System.arraycopy(inputs, 0, coins, 0, n);

            List<List<Integer>> combinations = findCombinations(coins, targetSum);
            System.out.println("Output: " + combinations.size());
            System.out.println("Explanation: The possible ways are:");
            for (List<Integer> combination : combinations) {
                System.out.println(combination);
            }
        } else {
            System.out.println("Exiting due to invalid input.");
        }
    }
}
