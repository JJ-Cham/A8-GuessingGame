import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class DecisionTree extends BinaryTree<String> {

    /** This constructor creates a leaf node */
    /** 
     * Constructs a leaf node with the given data.
     * @param data The question or animal name stored at this node.
     */
    public DecisionTree(String data) {
        super(data);
    }

    /** This constructor creates a branch node */
    /** 
     * Constructs a branch node with a left and right subtree.
     * @param data The question or animal name stored at this node.
     * @param left The left child (yes branch).
     * @param right The right child (no branch).
     */
    public DecisionTree(String data, DecisionTree left, DecisionTree right) {
        super(data, left, right);
    }

    /**deep copy constructor*/
    /**
     * Constructs a deep copy of another DecisionTree.
     * @param tree The tree to copy.
     */
    public DecisionTree(DecisionTree tree) {
        super(tree);
    }

    // //make tree building easier
    // public void attachLeft(DecisionTree left){
    //     setLeft(left);
    // }
    // public void attachRight(DecisionTree right){
    //     setRight(right);
    // }

    //iteratively follow path to return node
    // //follow the tree path, if Y goes left, N goes right
    // public DecisionTree followPath(String path){
    //     DecisionTree current = this;
    //     for(int i = 0; i < path.length(); i++){
    //         char step = path.charAt(i);
    //         if(step == 'Y'){
    //             current = (DecisionTree) current.getLeft();
    //         } else if(step == 'N'){
    //             current = (DecisionTree) current.getRight();
    //         } else {
    //             throw new IllegalArgumentException("Path can only contain 'Y' or 'N'");
    //         }
    //     }
    //     return current;
    // }

    //recurives version of followPath
    /**
     * followPath - recursively follows a Y/N path through the tree
     * @param path A string like "YNNY" (Y = left, N = right)
     * @return The DecisionTree node reached after following the path
     */
    public DecisionTree followPath(String path){
        //base case
        if (path.isEmpty() || path ==null){
            return this;
        }

        char step = path.charAt(0);
        String remainingPath = path.substring(1);

        if(step == 'Y'){
            if (this.getLeft() == null){
                return null;
            }
            return ((DecisionTree) this.getLeft()).followPath(remainingPath);
        } else if(step == 'N'){
            if (this.getRight() == null){
                return null;
            }
            return ((DecisionTree) this.getRight()).followPath(remainingPath);
        } else {
            throw new IllegalArgumentException("Path can only contain 'Y' or 'N'");
        }
    }

    
    /** Save the tree to a file using preorder traversal */
    public void save(PrintWriter out) {
        if (isLeaf()) {
            out.println("A:" + getData());
        } else {
            out.println("Q:" + getData());
            ((DecisionTree)getLeft()).save(out);
            ((DecisionTree)getRight()).save(out);
        }
    }

    /** Load a tree from a file using preorder traversal */
    public static DecisionTree load(Scanner in) {
        if (!in.hasNextLine()) return null;

        String line = in.nextLine().trim();
        if (line.startsWith("Q:")) {
            String question = line.substring(2);
            DecisionTree left = load(in);
            DecisionTree right = load(in);
            return new DecisionTree(question, left, right);
        } else if (line.startsWith("A:")) {
            String answer = line.substring(2);
            return new DecisionTree(answer);
        } else {
            return null; // unexpected format
        }
    }

    /** Helper to check if node is a leaf */
    public boolean isLeaf() {
        return (getLeft() == null && getRight() == null);
    }
    //mainly for testing

    public static void main(String[] args) {
        // Build a small test tree manually
        DecisionTree tree = new DecisionTree("Is it a mammal?");
        tree.setLeft(new DecisionTree("Does it have hooves?"));
        tree.setRight(new DecisionTree("Is it a reptile?"));

        tree.getLeft().setLeft(new DecisionTree("Cow"));
        tree.getLeft().setRight(new DecisionTree("Horse"));
        tree.getRight().setLeft(new DecisionTree("Crocodile"));
        tree.getRight().setRight(new DecisionTree("Mosquito"));

        // Test direct access (sanity check)
        System.out.println("Left-Right data: " + tree.getLeft().getRight().getData());
        // Output: Horse

        // Test followPath method
        System.out.println("Path 'YY' -> " + tree.followPath("YY").getData()); // should be Cow
        System.out.println("Path 'YN' -> " + tree.followPath("YN").getData()); // should be Horse
        System.out.println("Path 'NY' -> " + tree.followPath("NY").getData()); // should be Crocodile
        System.out.println("Path 'NN' -> " + tree.followPath("NN").getData()); // should be Mosquito
    }
}

