import java.util.HashMap;
import java.util.Map;

public class CuboSemantico {
    private final int[][][] cubo = {
        {
            {0,1,4,4},
            {1,1,4,4},
            {4,4,4,4},
            {4,4,4,4}
        },
        {
            {0,1,4,4},
            {1,1,4,4},
            {4,4,4,4},
            {4,4,4,4}
        },
        {
            {0,1,4,4},
            {1,1,4,4},
            {4,4,2,4},
            {4,4,4,4}
        },
        {
            {0,1,4,4},
            {1,1,4,4},
            {4,4,4,4},
            {4,4,4,4}
        },
        {
            {3,3,4,4},
            {3,3,4,4},
            {4,4,4,4},
            {4,4,4,4}
        },
        {
            {3,3,4,4},
            {3,3,4,4},
            {4,4,4,4},
            {4,4,4,4}
        },
        {
            {3,3,4,4},
            {3,3,4,4},
            {4,4,3,4},
            {4,4,4,3}
        },
        {
            {0,4,4,4},
            {4,1,4,4},
            {4,4,2,4},
            {4,4,4,3}
        }
    };

    private final String[] traductorIdToString = {"int", "float", "string", "bool", "ERROR"};

    private static final Map<String, Integer> traductorStringToId = new HashMap<>();

    private static final Map<String, Integer> traductorOperToId = new HashMap<>();
    
    static {
        traductorStringToId.put("int", 0);
        traductorStringToId.put("float", 1);
        traductorStringToId.put("string", 2);
        traductorStringToId.put("bool", 3);

        traductorOperToId.put("*", 0);
        traductorOperToId.put("/", 1);
        traductorOperToId.put("+", 2);
        traductorOperToId.put("-", 3);
        traductorOperToId.put("<", 4);
        traductorOperToId.put(">", 5);
        traductorOperToId.put("!=", 6);
        traductorOperToId.put("=", 7);
    }

    public int getTipoId(String tipo_s) {
        return traductorStringToId.get(tipo_s);
    }

    public int getOperId(String tipo_s) {
        return traductorOperToId.get(tipo_s);
    }
    
    public String getTipo(int op, int opdo1, int opdo2) {
        return traductorIdToString[cubo[op][opdo1][opdo2]];
    };

    public int checkError(int op, int opdo1, int opdo2) {
        return cubo[op][opdo1][opdo2];
    }

}