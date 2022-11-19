package lyc.compiler.assembler;

import lyc.compiler.arbol.ArbolGenerator;

import java.io.FileWriter;
import java.io.IOException;

public class Assembler {

    public static void generarAssembler(FileWriter fileWriter) throws IOException {
        escribirCabecera();
        escribirDatosTablaSimbolos();

        generarCODE(fileWriter);

        escribirFinal();
    }

    public static void escribirCabecera()
    {

    }

    public static void escribirDatosTablaSimbolos()
    {


    }

    public static void escribirFinal()
    {

    }

    public static void generarCODE(FileWriter fileWriter) throws IOException {
        // Hardcodear el START
        ArbolGenerator.printPostorder(ArbolGenerator.root, fileWriter);
    }

}
