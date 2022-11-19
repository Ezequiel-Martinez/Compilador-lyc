package lyc.compiler.arbol;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Stack;

public class ArbolGenerator {

    public static Node root;
    public static String printedTree = "";

    public static Node crearHoja(String value, String data_type) {
        root = new Node(value, data_type);
        return root;
    }

    public static Node crearNodo(String value, Node left) {
        root = new Node(value, left);
        return root;
    }

    public static Node crearNodo(String value, Node left, Node right) throws Exception {
        root = new Node(value, left, right);
        return root;
    }

    public static Node crearNodo(String value, Node left, Node right, String data_type) {
        root = new Node(value, left, right, data_type);
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

    public static void printPostorder(Node node, FileWriter fileWriter) throws IOException {
        if (node == null)
            return;

        // first recur on left subtree
        printPostorder(node.left, fileWriter);

        // then recur on right subtree
        printPostorder(node.right, fileWriter);

        // now deal with the node
        fileWriter.write(node.value + " ");
    }



}