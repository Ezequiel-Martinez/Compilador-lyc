package lyc.compiler.files;

import lyc.compiler.arbol.ArbolGenerator;

import java.io.FileWriter;
import java.io.IOException;

public class IntermediateCodeGenerator implements FileGenerator {

    @Override
    public void generate(FileWriter fileWriter) throws IOException {
        ArbolGenerator.printTree("", ArbolGenerator.root, false);
        fileWriter.write("El nodo de más arriba representa al hijo izquierdo y el inferior al hijo derecho.\n\n");
        fileWriter.write(ArbolGenerator.printedTree);
    }
}
