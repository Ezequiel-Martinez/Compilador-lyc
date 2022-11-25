package lyc.compiler.arbol;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Stack;

public class ArbolGenerator {

    public static String aux;
    public static boolean asig = false;
    public static boolean isIF = false;
    public static boolean isDocase = false;
    public static boolean isIncase = false;
    public static boolean andIzq = false;
    public static boolean caseOr = false;
    public static boolean neg = false;
    public static int antDocase;
    public static int endDocase;
    public static int caso;
    public static int ant;
    public static int specialNumber = 0;
    public static int cont = 1;
    public static int cont2 = 0;
    public static Stack<String> etiqetas = new Stack<String>();
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
        if(node.value.equals("BLOQ_DEC"))
            return;
        if(node.value.equals("SS"))
            printPostorder(node.right, fileWriter);
        else if(asig == true){
            fileWriter.write("\tFSTP " + node.value + " \n");
            asig = false;
        }
        else {
            switch (node.value) {
                case "=":
                    printPostorder(node.right, fileWriter);
                    asig = true;
                    printPostorder(node.left, fileWriter);
                    break;
                case "+", "-", "*", "/":
                    printPostorder(node.left, fileWriter);
                    printPostorder(node.right, fileWriter);
                    switch (node.value) {
                        case "+" -> fileWriter.write("\tFADD" + " \n");
                        case "-" -> fileWriter.write("\tFSUB" + " \n");
                        case "*" -> fileWriter.write("\tFMUL" + " \n");
                        case "/" -> fileWriter.write("\tFDIV" + " \n");
                    }
                    fileWriter.write("\tFFREE 0" + " \n");
                    break;
                case "NEG":
                    changeNeg();
                    printPostorder(node.right, fileWriter);
                    changeNeg();
                    break;
                case "&":
                    if(caseOr == false) {
                        printPostorder(node.left, fileWriter);
                        cont--;
                        etiqetas.pop();
                        printPostorder(node.right, fileWriter);
                    }
                    else{
                        if(andIzq == true) {
                            printPostorder(node.left, fileWriter);
                            cont--;
                            etiqetas.pop();
                            if(specialNumber != 0)
                                cont = ant;
                            printPostorder(node.right, fileWriter);
                        }
                        else {
                            andIzq = true;
                            contarOrs(node);
                            if(cont2 == 0)
                                changeNeg();
                            cont2 = 0;
                            printPostorder(node.left, fileWriter);
                            changeNeg();
                            andIzq = false;
                            aux = etiqetas.pop();
                            contarOrs(node);
                            if(cont2 >= 1){
                                if(specialNumber == 0){
                                    specialNumber = cont - 2;
                                }
                                ant = cont;
                                cont = specialNumber;
                                printPostorder(node.right, fileWriter);
                                etiqetas.pop();
                            }
                            else {
                                printPostorder(node.right, fileWriter);
                            }
                            fileWriter.write(aux + " \n");
                            cont2 = 0;
                        }
                    }
                    break;
                case "||":
                    if(caseOr == false){
                        caseOr = true;
                        if(isIncase == true){
                            cont++;
                        }
                        changeNeg();
                        printPostorder(node.left, fileWriter);
                        changeNeg();
                        aux = etiqetas.pop();
                        if(specialNumber != 0)
                            cont = ant;
                        if(isIncase == true){
                            isIncase = false;
                            antDocase = cont;
                            cont = caso;
                        }
                        printPostorder(node.right, fileWriter);
                        fileWriter.write(aux + ":\n");
                        caseOr = false;
                    }
                    else{
                        if(andIzq == true){
                            andIzq = false;
                            printPostorder(node.left, fileWriter);
                            changeNeg();
                            if(specialNumber != 0)
                                cont = ant;
                            printPostorder(node.right, fileWriter);
                        }
                        else {
                            printPostorder(node.left, fileWriter);
                            cont--;
                            etiqetas.pop();
                            printPostorder(node.right, fileWriter);
                        }
                    }
                    break;
                case "<", ">", "<=", ">=", "==", "!=":
                    printPostorder(node.left, fileWriter);
                    printPostorder(node.right, fileWriter);
                    fileWriter.write("\tFXCH" + " \n" + "\tFCOM" + " \n" + "\tFSTSW AX" + " \n" + "\tSAHF" + " \n");
                    switch (node.value) {
                        case "<":
                            if(neg == false)
                                fileWriter.write("\tJAE ETIQ" + cont + " \n");
                            else
                                fileWriter.write("\tJB ETIQ" + cont + " \n");
                            break;
                        case ">":
                            if(neg == false)
                                fileWriter.write("\tJBE ETIQ" + cont + " \n");
                            else
                                fileWriter.write("\tJA ETIQ" + cont + " \n");
                            break;
                        case "<=":
                            if(neg == false)
                                fileWriter.write("\tJA ETIQ" + cont + " \n");
                            else
                                fileWriter.write("\tJBE ETIQ" + cont + " \n");
                            break;
                        case ">=":
                            if(neg == false)
                                fileWriter.write("\tJB ETIQ" + cont + " \n");
                            else
                                fileWriter.write("\tJAE ETIQ" + cont + " \n");
                            break;
                        case "==":
                            if(neg == false)
                                fileWriter.write("\tJNE ETIQ" + cont + " \n");
                            else
                                fileWriter.write("\tJE ETIQ" + cont + " \n");
                            break;
                        case "!=":
                            if(neg == false)
                                fileWriter.write("\tJE ETIQ" + cont + " \n");
                            else
                                fileWriter.write("\tJNE ETIQ" + cont + " \n");
                            break;
                    }
                    etiqetas.push("ETIQ" + cont);
                    cont++;
                    break;
                case "IF":
                    printPostorder(node.left, fileWriter);
                    specialNumber = 0;
                    andIzq = false;
                    neg = false;
                    if(node.right.value.equals("CUERPO")) {
                        isIF = true;
                        printPostorder(node.right, fileWriter);
                    }
                    else {
                        printPostorder(node.right, fileWriter);
                        fileWriter.write(etiqetas.pop() + ":\n");
                    }
                    break;
                case "CUERPO":
                    if(isIF == true){
                        isIF = false;
                        printPostorder(node.left, fileWriter);
                        fileWriter.write("\tJMP ETIQ" + cont + " \n");
                        fileWriter.write(etiqetas.pop() + ":\n");
                        etiqetas.push("ETIQ" + cont);
                        cont++;
                        printPostorder(node.right, fileWriter);
                        fileWriter.write(etiqetas.pop() + ":\n");
                    }
                    if(isDocase == true){
                        isDocase = false;
                        printPostorder(node.left, fileWriter);
                        fileWriter.write("ETIQ" + caso + ":\n");
                        printPostorder(node.right, fileWriter);
                    }
                    break;
                case "WHILE":
                    fileWriter.write("ETIQ" + cont + ":\n");
                    etiqetas.push("ETIQ" + cont);
                    cont++;
                    printPostorder(node.left, fileWriter);
                    specialNumber = 0;
                    andIzq = false;
                    neg = false;
                    printPostorder(node.right, fileWriter);
                    aux = etiqetas.pop();
                    fileWriter.write("\tJMP " + etiqetas.pop() + " \n");
                    fileWriter.write(aux + ":\n");
                    break;
                case "DOCASE":
                    endDocase = cont;
                    caso = cont;
                    cont++;
                    if(node.right.value.equals("CUERPO")) {
                        isDocase = true;
                        caso = cont;
                        cont++;
                    }
                    antDocase = cont;
                    printPostorder(node.right, fileWriter);
                    cont = antDocase;
                    fileWriter.write("ETIQ" + endDocase + ":\n");
                    break;
                case "CASE":
                    cont = caso;
                    isIncase = true;
                    printPostorder(node.left, fileWriter);
                    specialNumber = 0;
                    andIzq = false;
                    neg = false;
                    cont = antDocase;
                    etiqetas.pop();
                    isIncase = false;
                    printPostorder(node.right, fileWriter);
                    antDocase = cont;
                    fileWriter.write("\tJMP ETIQ" + endDocase + " \n");
                    break;
                case "REPEAT":
                    break;
                case "WRITE", "READ":
                    fileWriter.write("\tmov dx,OFFSET " + node.left.value + "\n\tmov ah,9 \n\tint 21h" + " \n");
                    break;
                case "Bloque", "CASES":
                    printPostorder(node.left, fileWriter);
                    printPostorder(node.right, fileWriter);
                    break;
                default:
                    printPostorder(node.left, fileWriter);
                    printPostorder(node.right, fileWriter);
                    fileWriter.write("\tFLD " + node.value + " \n");
            }
        }
    }

    private static void changeNeg(){
        if(neg == true)
            neg = false;
        else
            neg = true;
    }

    private static void contarOrs(Node node){
        if (node == null)
            return;
        if(node.value.equals("||"))
            cont2++;

        contarOrs(node.left);
        contarOrs(node.right);
    }


}