package lyc.compiler.arbol;

import lyc.compiler.table.DataType;

import java.util.Objects;

public class Node {
    String value;
    String data_type;
    Node left;
    Node right;

    public Node () {
        this.value = null;
        right = null;
        left = null;
    }

    public Node (String value, String data_type) {
        this.value = value;
        this.data_type = data_type;
        right = null;
        left = null;
    }

    public Node (String value, Node left) {
        this.value = value;
        this.data_type = null;
        right = null;
        this.left = left;
    }

    // Este constructor se usa para comparar tipos de datos
    public Node (String value, Node left, Node right) throws Exception {
        this.value = value;
        this.right = right;
        this.left = left;

        if (this.left == null || this.right == null)
            this.data_type = null;
        else if (this.left.data_type == null || this.right.data_type == null)
            this.data_type = null;
        else if (this.left.data_type.equals(this.right.data_type))
            this.data_type = this.left.data_type;
        else if (this.left.data_type.equals(DataType.INTEGER_TYPE.toString()))
        {
            if(Objects.equals(this.value, "="))
                throw new Exception("Conversión de tipos inválida, no se puede asignar un " + this.right.data_type + " a un Int");
            else {
                switch (this.right.data_type) {
                    case "Long" -> this.data_type = "Long";
                    case "FLOAT_TYPE", "Double" -> this.data_type = "Double";
                    default -> throw new Exception("Conversión de tipos inválida, no se puede convertir Int a " + this.right.data_type);
                }
            }
        }
        else if (this.left.data_type.equals("Long"))
        {
            if(Objects.equals(this.value, "=")) {
                if ("INTEGER_TYPE".equals(this.right.data_type)) {
                    this.data_type = "Long";
                } else {
                    throw new Exception("Conversión de tipos inválida, no se puede asignar un " + this.right.data_type + " a un Long");
                }
            }
            else {
                switch (this.right.data_type) {
                    case "INTEGER_TYPE" -> this.data_type = "Long";
                    case "FLOAT_TYPE", "Double" -> this.data_type = "Double";
                    default -> throw new Exception("Conversión de tipos inválida, no se puede convertir Long a " + this.right.data_type);
                }
            }
        }
        else if (this.left.data_type.equals(DataType.FLOAT_TYPE.toString()))
        {
            if(Objects.equals(this.value, "="))
                throw new Exception("Conversión de tipos inválida, no se puede asignar un " + this.right.data_type + " a un Float");
            else {
                switch (this.right.data_type) {
                    case "INTEGER_TYPE", "Long", "Double" -> this.data_type = "Double";
                    default -> throw new Exception("Conversión de tipos inválida, no se puede convertir Float a " + this.right.data_type);
                }
            }
        }
        else if (this.left.data_type.equals("Double"))
        {
            switch (this.right.data_type) {
                case "Long", "FLOAT_TYPE", "INTEGER_TYPE" -> this.data_type = "Double";
                default -> throw new Exception("Conversión de tipos inválida, no se puede convertir Double a " + this.right.data_type);
            }
        }
        else
            throw new Exception("Conversión de tipos inválida, no se puede convertir String (" + this.left.value + ") a " + this.right.data_type + " (" + this.right.value + ")");

    }

    public Node (String value, Node left, Node right, String data_type) {
        this.value = value;
        this.right = right;
        this.left = left;
        this.data_type = data_type;
    }
}