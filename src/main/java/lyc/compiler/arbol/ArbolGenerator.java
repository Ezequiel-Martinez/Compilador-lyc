package lyc.compiler.arbol;

import java.util.Stack;

public class ArbolGenerator {

    public static Node root;
    public static String printedTree = "";

    public static Node crearHoja(String value) {
        root = new Node(value);
        return root;
    }

    public static Node crearNodo(String value, Node left, Node right) {
        root = new Node(value, left, right);
        return root;





    }


    public static void printTree(String prefix, Node node, boolean isLeft)
    {
        if (node != null) {
            printedTree += (prefix + (isLeft ? "|——> " : "\\——> ") + node.value + "\n");
            printTree(prefix + (isLeft ? "|   " : "    "), node.left, true);
            printTree(prefix + (isLeft ? "|   " : "    "), node.right, false);
        }
    }



}
