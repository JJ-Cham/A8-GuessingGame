public class AnimalGuess{
    public static void main(String[] args) {
        DecisionTree tree = new DecisionTree("Is it a mammal?");
        DecisionTree left = new DecisionTree("Is it a domestic animal?");
        DecisionTree right = new DecisionTree("Is it a reptile?");
        
        left.attachLeft(new DecisionTree("Dog"));
        left.attachRight(new DecisionTree("Lion"));
        
        right.attachLeft(new DecisionTree("Snake"));
        right.attachRight(new DecisionTree("Eagle"));
        
        tree.attachLeft(left);
        tree.attachRight(right);
        
        // Example of following a path
        DecisionTree node = tree.followPath("YN");
        System.out.println("Node data at path 'YN': " + node.getData()); // Should print "Lion"
    }

    
}