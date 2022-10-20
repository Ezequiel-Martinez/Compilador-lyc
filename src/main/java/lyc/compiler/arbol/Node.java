package lyc.compiler.arbol;

public class Node {
    String value;
    Node left;
    Node right;

    public Node () {
        this.value = null;
        right = null;
        left = null;
    }

    public Node (String value) {
        this.value = value;
        right = null;
        left = null;
    }

    public Node (String value, Node left, Node right) {
        this.value = value;
        this.right = right;
        this.left = left;
    }
}
