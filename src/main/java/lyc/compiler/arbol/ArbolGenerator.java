package lyc.compiler.arbol;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Stack;

public class ArbolGenerator {

    public static String aux;
    public static boolean asig = false;
    public static boolean isIF = false;
    public static boolean andIzq = false;
    public static boolean caseOr = false;
    public static boolean neg = false;
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
            fileWriter.write("FSTP " + node.value + " \n");
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
                        case "+" -> fileWriter.write("ADD" + " \n");
                        case "-" -> fileWriter.write("SUB" + " \n");
                        case "*" -> fileWriter.write("MUL" + " \n");
                        case "/" -> fileWriter.write("DIV" + " \n");
                    }
                    fileWriter.write("FFREE 0" + " \n");
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
                            System.out.println(cont2 + " AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
                            if(cont2 == 0)
                                changeNeg();
                            cont2 = 0;
                            //???????????????????????????
                            printPostorder(node.left, fileWriter);
                            changeNeg();
                            andIzq = false;
                            aux = etiqetas.pop();
                            contarOrs(node);
                            System.out.println(cont2 + " AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
                            if(cont2 >= 1){
                                if(specialNumber == 0){
                                    specialNumber = cont - 2;
                                }
                                ant = cont;
                                cont = specialNumber;
                                System.out.println(ant + " " + specialNumber + " WWWWWWWWWWWWWW");
                                printPostorder(node.right, fileWriter);
                                //cont = ant;
                                //cont--;
                                //cont--;
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
                        changeNeg();
                        printPostorder(node.left, fileWriter);
                        changeNeg();
                        aux = etiqetas.pop();
                        if(specialNumber != 0)
                            cont = ant;
                        printPostorder(node.right, fileWriter);
                        fileWriter.write(aux + " \n");
                        caseOr = false;
                    }
                    else{
                        if(andIzq == true){
                            andIzq = false;
                            printPostorder(node.left, fileWriter);
                            changeNeg();
                            //andIzq = true; ??????????
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
                    fileWriter.write("FXCH" + " \n" + "FCOM" + " \n" + "FSTSW AX" + " \n" + "SAHF" + " \n");
                    switch (node.value) {
                        case "<":
                            if(neg == false)
                                fileWriter.write("JAE Etiq" + cont + " \n");
                            else
                                fileWriter.write("JB Etiq" + cont + " \n");
                            break;
                        case ">":
                            if(neg == false)
                                fileWriter.write("JBE Etiq" + cont + " \n");
                            else
                                fileWriter.write("JA Etiq" + cont + " \n");
                            break;
                        case "<=":
                            if(neg == false)
                                fileWriter.write("JA Etiq" + cont + " \n");
                            else
                                fileWriter.write("JBE Etiq" + cont + " \n");
                            break;
                        case ">=":
                            if(neg == false)
                                fileWriter.write("JB Etiq" + cont + " \n");
                            else
                                fileWriter.write("JAE Etiq" + cont + " \n");
                            break;
                        case "==":
                            if(neg == false)
                                fileWriter.write("JNE Etiq" + cont + " \n");
                            else
                                fileWriter.write("JE Etiq" + cont + " \n");
                            break;
                        case "!=":
                            if(neg == false)
                                fileWriter.write("JE Etiq" + cont + " \n");
                            else
                                fileWriter.write("JNE Etiq" + cont + " \n");
                            break;
                    }
                    etiqetas.push("Etiq" + cont);
                    cont++;
                    break;
                case "IF":
                    printPostorder(node.left, fileWriter);
                    specialNumber = 0;
                    andIzq = false;
                    neg = false;
                    if(node.right.value.equals("Cuerpo")) {
                        isIF = true;
                        printPostorder(node.right, fileWriter);
                    }
                    else {
                        printPostorder(node.right, fileWriter);
                        fileWriter.write(etiqetas.pop() + " \n");
                    }
                    break;
                case "Cuerpo":
                    if(isIF == true){
                        isIF = false;
                        printPostorder(node.left, fileWriter);
                        fileWriter.write("JMP Etiq" + cont + " \n");
                        fileWriter.write(etiqetas.pop() + " \n");
                        etiqetas.push("Etiq" + cont);
                        cont++;
                        printPostorder(node.right, fileWriter);
                        fileWriter.write(etiqetas.pop() + " \n");
                    }
                    break;
                case "WHILE":
                    fileWriter.write("Etiq" + cont + " \n");
                    etiqetas.push("Etiq" + cont);
                    cont++;
                    printPostorder(node.left, fileWriter);
                    specialNumber = 0;
                    andIzq = false;
                    neg = false;
                    printPostorder(node.right, fileWriter);
                    aux = etiqetas.pop();
                    fileWriter.write("JMP " + etiqetas.pop() + " \n");
                    fileWriter.write(aux + " \n");
                    break;
                case "WRITE", "READ":
                    fileWriter.write("mov dx,OFFSET " + node.left.value + "\nmov ah,9 \nint 21h" + " \n");
                    break;
                case "Bloque":
                    printPostorder(node.left, fileWriter);
                    printPostorder(node.right, fileWriter);
                    break;
                case "REPEAT", "DOCASE":
                    break;
                default:
                    printPostorder(node.left, fileWriter);
                    printPostorder(node.right, fileWriter);
                    fileWriter.write("FLD " + node.value + " \n");
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