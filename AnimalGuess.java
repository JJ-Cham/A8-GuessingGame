import java.io.*;
// import java.util.*;
import java.util.Scanner;

public class AnimalGuess {
    private static final String FILENAME = "AnimalTree.txt";

    private static Scanner console = new Scanner(System.in);

    public static void main(String[] args) {
        DecisionTree tree = null;

        // Try loading existing tree
        try {
            Scanner fileIn = new Scanner(new File(FILENAME));
            tree = DecisionTree.load(fileIn);
            fileIn.close();
            System.out.println("Loaded previous knowledge base.");

            if (tree == null) {
                System.out.println("File was empty or invalid — starting new tree.");
                tree = buildInitialTree();
            }

        } catch (FileNotFoundException e) {
            System.out.println("No previous data found. Starting new tree.");
            tree = buildInitialTree();
        }


        // Play loop
        do {
            playGame(tree);
        } while (askYesNo("Play again?"));

        // Save updated tree
        try {
            PrintWriter out = new PrintWriter(new FileWriter(FILENAME));
            tree.save(out);
            out.close();
            System.out.println("Knowledge saved to " + FILENAME);
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }

        System.out.println("Goodbye!");
    }

    // public static void main(String[] args){
    //     DecisionTree tree = buildInitialTree();
    //     playGame(tree); 
    // }


    //load from file in phase 3 
    private static DecisionTree buildInitialTree(){
        DecisionTree root = new DecisionTree("Does it waddle?");
        root.setLeft(new DecisionTree("Is it a penguin?"));
        root.setRight(new DecisionTree("Is it a lion?"));
        return root;
    }

    //make easier to read user input
    private static String prompt(String message){
        System.out.print(message + " ");
        return console.nextLine().trim();
    }

    private static boolean askYesNo(String message){
        while(true){
            System.out.print(message + " (Y/N): ");
            String response = console.nextLine().trim().toUpperCase();
            if(response.equals("Y") || response.equals("YES")){
                return true;
            } else if (response.equals("N") || response.equals("NO")){
                return false;
            } else {
                System.out.println("Please answer Y or N.");
            }
        }
    }

    //main game loop 

    public static void playGame(DecisionTree tree) {
        DecisionTree current = tree;

        // Safety check
        if (current == null) {
            System.out.println("Error: tree is empty.");
            return;
        }

        while (!current.isLeaf()) {
            boolean answer = askYesNo(current.getData());
            if (answer) {
                if (current.getLeft() != null) {
                    current = (DecisionTree) current.getLeft();
                } else {
                    System.out.println("Error: Missing left child!");
                    return;
                }
            } else {
                if (current.getRight() != null) {
                    current = (DecisionTree) current.getRight();
                } else {
                    System.out.println("Error: Missing right child!");
                    return;
                }
            }
        }

        // We are now at a leaf node
        boolean correct = askYesNo("Is your animal a " + current.getData() + "?");
        if (correct) {
            System.out.println("I guessed it!");
        } else {
            System.out.println("I got it wrong. Please help me learn.");

            System.out.print("What was your animal? ");
            String animal = console.nextLine();

            System.out.print("Type a yes/no question that distinguishes " + animal + " from " + current.getData() + ": ");
            String question = console.nextLine();

            boolean yesForNewAnimal = askYesNo("Would the answer be 'yes' for " + animal + "?");

            // Create new subtrees
            DecisionTree newAnimal = new DecisionTree(animal);
            DecisionTree oldAnimal = new DecisionTree(current.getData());

            if (yesForNewAnimal) {
                current.setData(question);
                current.setLeft(newAnimal);
                current.setRight(oldAnimal);
            } else {
                current.setData(question);
                current.setLeft(oldAnimal);
                current.setRight(newAnimal);
            }
        }
    }


    //main 
    // public static void main(String [] args){
    //     DecisionTree tree = buildInitialTree();

    //     do{
    //     playGame(tree);
    //     } while(askYesNo("Would you like to play again?"));
    //     System.out.println("Thanks for playing!");
    // }


}
