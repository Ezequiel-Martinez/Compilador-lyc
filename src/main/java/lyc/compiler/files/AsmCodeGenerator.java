package lyc.compiler.files;

import lyc.compiler.arbol.ArbolGenerator;
import lyc.compiler.assembler.Assembler;

import java.io.FileWriter;
import java.io.IOException;

public class AsmCodeGenerator implements FileGenerator {

    @Override
    public void generate(FileWriter fileWriter) throws IOException {
        Assembler.generarAssembler(fileWriter);
    }
}
