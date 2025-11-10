import java.io.*;
import java.util.*;
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
    private static void playGame(DecisionTree tree){
        System.out.println("Welcome to the Animal Guessing Game!");
        boolean playAgain = true;
        while(playAgain){
            DecisionTree current = tree;
            //traverse the tree
            while(current.getLeft() != null && current.getRight() != null){
                boolean answer = askYesNo(current.getData());
                if(answer){
                    current = (DecisionTree) current.getLeft();
                } else {
                    current = (DecisionTree) current.getRight();
                }
            }
            //make a guess
            boolean correct = askYesNo("Is it a " + current.getData() + "?");
            if(correct){
                System.out.println("Yay! I guessed your animal!");
            } else {
                //learn new animal
                String userAnimal = prompt("What animal were you thinking of?");
                String question = prompt("Please give me a question that would distinguish a " + userAnimal + " from a " + current.getData() + ".");
                boolean answerForUserAnimal = askYesNo("For a " + userAnimal + ", what is the answer to your question?");
                //create new nodes
                DecisionTree userAnimalNode = new DecisionTree(userAnimal);
                DecisionTree oldAnimalNode = new DecisionTree(current.getData());
                //update current node to be the question
                current.setData(question);
                if(answerForUserAnimal){
                    current.setLeft(userAnimalNode);
                    current.setRight(oldAnimalNode);
                } else {
                    current.setLeft(oldAnimalNode);
                    current.setRight(userAnimalNode);
                }
                System.out.println("Thanks! I've learned something new.");
            }
            playAgain = askYesNo("Would you like to play again?");
        }
        System.out.println("Thanks for playing!");
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
