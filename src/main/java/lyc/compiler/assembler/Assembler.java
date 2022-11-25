package lyc.compiler.assembler;

import lyc.compiler.arbol.ArbolGenerator;
import lyc.compiler.table.DataType;
import lyc.compiler.table.SymbolEntry;
import lyc.compiler.table.SymbolTableManager;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Assembler {

    public static void generarAssembler(FileWriter fileWriter) throws IOException {
        escribirCabecera(fileWriter);
        escribirDatosTablaSimbolos(fileWriter);

        generarCODE(fileWriter);

        escribirFinal();
    }

    public static void escribirCabecera(FileWriter fileWriter) throws IOException {
        fileWriter.write(   ".MODEL  LARGE\n" +
                            ".386\n" +
                            ".STACK 200h\n" +
                            "\n");
    }

    public static void escribirDatosTablaSimbolos(FileWriter fileWriter) throws IOException {
        fileWriter.write(   ".DATA\n" +
                            "\n");

        HashMap<String, SymbolEntry> symbolTable = SymbolTableManager.symbolTable;

        for (Map.Entry<String, SymbolEntry> set : symbolTable.entrySet()) {
            String nombre = set.getValue().getName();
            String valor = set.getValue().getValue();
            String identifier = valor == "" ? "?" : "dd";

            if (set.getValue().getDataType() == DataType.STRING_CONS && valor != "")
                valor = "\"" + valor + "\"";

            fileWriter.write("\t" + nombre + "\t" + identifier + "\t" + valor + "\n");
        }

        fileWriter.write("\n");

    }

    public static void escribirFinal()
    {

    }

    public static void generarCODE(FileWriter fileWriter) throws IOException {
        fileWriter.write("START:\n");
        ArbolGenerator.printPostorder(ArbolGenerator.root, fileWriter);
        fileWriter.write("END START");
    }

}
