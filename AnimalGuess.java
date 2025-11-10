import java.io.*;
import java.util.*;

public class AnimalGuess {

    public static void main(String[] args) {
        String filename = (args.length > 0) ? args[0] : "AnimalTree.txt";
        DecisionTree tree = null;

        // Try to read existing tree
        try {
            tree = DecisionTree.readFromFile(filename);
            System.out.println("Loaded decision tree from " + filename + ".");
        } catch (Exception e) {
            System.out.println("No existing tree found — starting fresh!");
            tree = new DecisionTree("Mouse");
        }

        Scanner sc = new Scanner(System.in);
        boolean again;

        do {
            System.out.println("\nThink of an animal.");
            System.out.println("I'll try to guess it.");
            playGame(tree, sc);
            again = getYesNo("Play again?", sc);
        } while (again);

        try {
            tree.writeToFile(filename);
            System.out.println("Tree saved to " + filename + ".");
        } catch (IOException e) {
            System.out.println("Error saving tree.");
        }

        sc.close();
    }

    public static void playGame(DecisionTree tree, Scanner sc) {
        DecisionTree current = tree;

        // Traverse until leaf (animal)
        while (!current.isLeaf()) {
            boolean answer = getYesNo(current.getData(), sc);
            if (answer) current = (DecisionTree) current.getLeft();
            else current = (DecisionTree) current.getRight();
        }

        boolean correct = getYesNo("Is your animal a " + current.getData() + "?", sc);

        if (correct) {
            System.out.println("I guessed it!");
        } else {
            learnNewAnimal(current, sc);
        }
    }

    public static void learnNewAnimal(DecisionTree leaf, Scanner sc) {
        System.out.println("I got it wrong.");
        System.out.println("Please help me to learn.");
        System.out.print("What was your animal? ");
        String newAnimal = sc.nextLine().trim();

        System.out.println("Type a yes or no question that would distinguish between a "
                + newAnimal + " and a " + leaf.getData() + ".");
        String newQuestion = sc.nextLine().trim();

        boolean answerForNew = getYesNo("Would you answer yes to this question for the " + newAnimal + "?", sc);

        DecisionTree oldAnimal = new DecisionTree(leaf.getData());
        DecisionTree newAnimalNode = new DecisionTree(newAnimal);

        leaf.setData(newQuestion);

        if (answerForNew) {
            leaf.setLeft(newAnimalNode);
            leaf.setRight(oldAnimal);
        } else {
            leaf.setLeft(oldAnimal);
            leaf.setRight(newAnimalNode);
        }

        System.out.println("Got it! I'll remember that.");
    }

    public static boolean getYesNo(String prompt, Scanner sc) {
        while (true) {
            System.out.print(prompt + " ");
            String ans = sc.nextLine().trim().toLowerCase();
            if (ans.equals("y") || ans.equals("yes")) return true;
            if (ans.equals("n") || ans.equals("no")) return false;
            System.out.println("Please answer yes or no.");
        }
    }
}
