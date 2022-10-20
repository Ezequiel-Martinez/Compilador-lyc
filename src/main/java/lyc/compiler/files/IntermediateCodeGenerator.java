package lyc.compiler.files;

import lyc.compiler.arbol.ArbolGenerator;

import java.io.FileWriter;
import java.io.IOException;

public class IntermediateCodeGenerator implements FileGenerator {

    @Override
    public void generate(FileWriter fileWriter) throws IOException {
        ArbolGenerator.printTree("", ArbolGenerator.root, false);
        fileWriter.write(ArbolGenerator.printedTree);
    }
}
