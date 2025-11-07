import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

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
    public void writeToFile(String filename) throws IOException {
        PrintWriter out = new PrintWriter(new FileWriter(filename));
        Queue<DecisionTree> nodes = new LinkedList<>();
        Queue<String> paths = new LinkedList<>();
        nodes.add(this);
        paths.add("");

        while (!nodes.isEmpty()) {
            DecisionTree node = nodes.remove();
            String path = paths.remove();
            out.println((path.isEmpty() ? " " : path + " ") + node.getData());

            if (node.getLeft() != null) {
                nodes.add((DecisionTree) node.getLeft());
                paths.add(path + "Y");
            }
            if (node.getRight() != null) {
                nodes.add((DecisionTree) node.getRight());
                paths.add(path + "N");
            }
        }

        out.close();
    }

    //read tree from file
    public static DecisionTree readFromFile(String filename) throws IOException {
        Scanner in = new Scanner(new File(filename));
        DecisionTree root = null;

        while (in.hasNextLine()) {
            String line = in.nextLine();
            int split = line.indexOf(' ');
            String path = line.substring(0, split).trim();
            String data = line.substring(split + 1);

            if (path.isEmpty()) {
                root = new DecisionTree(data);
            } else {
                String parentPath = path.substring(0, path.length() - 1);
                char direction = path.charAt(path.length() - 1);
                DecisionTree parent = root.followPath(parentPath);
                DecisionTree child = new DecisionTree(data);
                if (direction == 'Y') parent.attachLeft(child);
                else parent.attachRight(child);
            }
        }

        in.close();
        return root;
    }
}

