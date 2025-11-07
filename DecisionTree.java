public class DecisionTree extends BinaryTree<String> {

    /** This constructor creates a leaf node */
    public DecisionTree(String data) {
        super(data);
    }

    /** This constructor creates a branch node */
    public DecisionTree(String data, DecisionTree left, DecisionTree right) {
        super(data, left, right);
    }

    //make tree building easier
    public void attachLeft(DecisionTree left){
        setLeft(left);
    }
    public void attachRight(DecisionTree right){
        setRight(right);
    }

    //follow the tree path, if Y goes left, N goes right
    public DecisionTree followPath(String path){
        DecisionTree current = this;
        for(int i = 0; i < path.length(); i++){
            char step = path.charAt(i);
            if(step == 'Y'){
                current = (DecisionTree) current.getLeft();
            } else if(step == 'N'){
                current = (DecisionTree) current.getRight();
            } else {
                throw new IllegalArgumentException("Path can only contain 'Y' or 'N'");
            }
        }
        return current;
    }

    //write each nodes patha and data to a file 
    


}