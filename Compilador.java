import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

class Compilador {

    // Estructuras para tablas de variables locales y globales, directorio de funciones
    public ArrayList<Variable> globalVar = new ArrayList<>();
    public ArrayList<Variable> localVar = new ArrayList<>();
    public ArrayList<String> dirFunc = new ArrayList<>();

    // Variable para identificar si nos encontramos declarando variables dentro de un metodo
    public boolean isLocalVar = false;

    // Instancia de la clase Cubo Semantico
    public CuboSemantico cuboSemantico = new CuboSemantico();

    // Array en el que se guardaran los cuadruplos y su contador
    public ArrayList<Quad> quads = new ArrayList<>();
    public int quad_cont = 0;

    // Pilas auxiliares para generar los cuadruplos
    public Stack<Integer> PilaO = new Stack<>();
    public Stack<Integer> PilaT = new Stack<>();
    public Stack<Integer> POper = new Stack<>();

    // Asignador de direcciones
    public int dir = 1; //+1 por la constante -1 para negativos en dir 0

    // Asiganodor de variables temporales
    public int avail = 0;

    // Pila de saltos para rellenar operadores goto
    public Stack<Integer> PJumps = new Stack<>();

    // Array para almacenar las constantes int, float y string
    public ArrayList<Cte> ctes = new ArrayList<>();

    // Checa si ha habido un ERROR, si si, no genera el archivo
    public boolean ERROR = false;
  
    // Validacion y definicion para un metodo o programa
    public void add_DirFunc(String ID) {
        if ( dirFunc.contains(ID) ) {
            System.err.println("ERROR: Double definition for function: "+ID );
            ERROR = true;
        }

        dirFunc.add(ID);
    }

    // Imprimir todos los cuadruplos creados
    public void imprimir_quads() {
        System.out.println(quads);
        System.out.println(dir);
    }

    // Checa si ya se definieron las variables gobales cuando se esta definiendo mas variables
    public void validar_def_vars_globales(ArrayList<String> pendigIds, String type) {

        for(int i = 0; i < pendigIds.size(); i++){
            Variable _tupla = new Variable(pendigIds.get(i), type, dir);
            dir++;
            if ( globalVar.contains(_tupla) ) {
                System.err.println("ERROR: Double definition for global variable: "+pendigIds.get(i) );
                ERROR = true;
            } else {
                globalVar.add(_tupla);
            }
 
        }

    }

    // Checa si ya se definieron las variables locales y gobales cuando se esta definiendo mas variables
    public void validar_def_vars_locales(ArrayList<String> pendigIds, String type) {
  
        for(int i = 0; i < pendigIds.size(); i++){
            Variable _tupla = new Variable(pendigIds.get(i), type, dir);
            dir++;

            if ( localVar.contains(_tupla) || globalVar.contains(_tupla) ) {
                System.err.println("ERROR: Double definition for local variable: "+pendigIds.get(i) );
                ERROR = true;
            } else {
                localVar.add(_tupla);
            }

        }

    }

    // Checa si estamos dentro de una funcion y si es asi valida las variables local o globalmente
    public void validar_def_vars(ArrayList<String> pendigIds, String type) {
        if(isLocalVar) {
            validar_def_vars_locales(pendigIds, type);
        } else {
            validar_def_vars_globales(pendigIds, type);
        }
    }

    // Valida los parametros de un metodo
    public void validar_def_parametros(String ID, String type) {
  
        Variable _tupla = new Variable(ID, type, dir);
        dir++;

        if ( localVar.contains(_tupla) || globalVar.contains(_tupla) ) {
            System.err.println("ERROR: Double definition for local variable parameter: "+ID );
            ERROR = true;
        } else {
            localVar.add(_tupla);
        }

    }

    // Valida si las variable local que encontro existe
    public void validar_ID_locales(String ID) {
        Variable _tupla = new Variable(ID, null, dir);

        if ( !globalVar.contains(_tupla) && !localVar.contains(_tupla) ) {
            System.err.println("ERROR: Local Variable "+ID+" not exist" );
            ERROR = true;
        }

    }

    // Valida si las variable global que encontro existe
    public void validar_ID_globales(String ID) {
        Variable _tupla = new Variable(ID, null,dir);

        if ( !globalVar.contains(_tupla) ) {
            System.err.println("ERROR: Global variable "+ID+" not exist" );
            ERROR = true;
        }
    }

    // Checa si estamos dentro de una funcion y valida si exite la variable local o global
    public void validar_ID(String ID) {
        if(isLocalVar) {
            validar_ID_locales(ID);
        } else {
            validar_ID_globales(ID);
        }
    }

    // Valida si la funcion que estamos llamando si existe
    public void validar_func(String ID) {
        if ( !dirFunc.contains(ID) ) {
            System.err.println("ERROR: Function "+ID+" not exist" );
            ERROR = true;
        }
    }

    // Hace una busqueda en de una Variable dado su id
    public static Variable findVarById(ArrayList<Variable> list, String id) {
        for (Variable var : list) {
            if (var.getId().equals(id)) {
                return var;
            }
        }
        return null; // Retorna null si no se encuentra el _id
    }


    // Checa si estamos dentro de una funcion, y busca la variable con su ID en la tabla de variables locales o globales
    public Variable findVar(String ID) {
        if(isLocalVar) {
            return findVarById(localVar, ID);
        } else {
            return findVarById(globalVar, ID);
        }
    }

    // Crea cuadruplo de assignacion
    public void add_quad_assign(String ID) {
        Variable _var = findVar(ID);
        if( cuboSemantico.getId(7, cuboSemantico.getTipoId(_var.getTipo()), PilaT.peek()) != 4 ) {
            Quad _quad = new Quad(7, PilaO.pop(), 0, _var.getDir());
            quads.add(_quad);
            quad_cont++;
        } else {
            System.err.println("ERROR: Assign Type mismatch");
            ERROR = true;
        } 
    }

    // Hace push a la pila de Operandos la direccion de la variable y a la pila de Tipos el id del tipo de variable cuando encutrna un id
    public void do_push_ID(String ID) {
        Variable _var = findVar(ID);
        PilaO.push(_var.getDir());
        PilaT.push(cuboSemantico.getTipoId(_var.getTipo()));
    }

    // Cuando encuentra un operador, hace push a la pila de operadores
    public void do_push_Oper(String Oper) {
        POper.add(cuboSemantico.getOperId(Oper));
    }

    // Hace el cuadruplo con el operador y las direcciones que se encuentren en las pilas y valida que se pueda realizar la operacion
    public void add_quad_expresion() {
     
        int L_O = PilaO.pop();
        int L_O_T = PilaT.pop();

        int R_O = PilaO.pop();
        int R_O_T = PilaT.pop();

        int oper = POper.pop();

        String res_type = cuboSemantico.getTipo(oper, L_O_T, R_O_T);
        int res_id = cuboSemantico.getId(oper, L_O_T, R_O_T);

        if( res_id != 4 ) {
            String _tvar_id = "t" + avail;
            avail++;
            Variable _tvar = new Variable(_tvar_id, res_type, dir);
            dir++;

            globalVar.add(_tvar);
            PilaO.push(_tvar.getDir());
            PilaT.push(res_id);

            Quad _quad = new Quad(oper, L_O, R_O, _tvar.getDir());
            quads.add(_quad);
            quad_cont++;

        } else {
            System.err.println("ERROR: Type mismatch resolving expretion");
            ERROR = true;
        }
    }

    // Si la constante que viene esta negada, se crea un cuadruplo de multiplicacion con -1 (por eso se reserva esta constante en el espacio 0 de memoria)
    public void check_negative(String N) {
        if ("-".equals(N)) {
            int L_O = PilaO.pop();
            int L_O_T = PilaT.pop();

            
            String _tvar_id = "t" + avail;
            avail++;
            
            Variable _tvar = new Variable(_tvar_id, cuboSemantico.getIdTipo(L_O_T), dir);
            dir++;

            globalVar.add(_tvar);
            PilaO.push(_tvar.getDir());
            PilaT.push(L_O_T);

            Quad _quad = new Quad(0, L_O, 0, _tvar.getDir());
            quads.add(_quad);
            quad_cont++;

        }
    }

    // Guardamos las constantes en un arreglo para pasarselos al archivo generado
    public void do_push_CTE(Object value, int type) {
        Cte cte = new Cte(type, value, dir);

        PilaO.push(dir);
        PilaT.push(type);

        dir++;
        ctes.add(cte);
    }

    // Crea un cuadruplo con lo que hay en el tipe de la pila, para imprimir
    public void add_quad_top_pila() {

        PilaT.pop();
        Quad _quad = new Quad(8, 0, 0, PilaO.pop());
        quads.add(_quad);
        quad_cont++;

    }

    // Crea el cuadruplo GOTOF para el estatuto if
    public void add_quad_if(int exp_type) {
        if ( !(exp_type == 3) ) {
            System.err.println("ERROR: Type mismatch: if exprestion is not type bool");
            ERROR = true;
        } else {
            Quad _quad = new Quad(10, PilaO.pop(), 0, -1);
            quads.add(_quad);
            quad_cont++;
            PJumps.push(quad_cont-1);
        }
    }

    // Llena el GOTOF en if 
    public void fill_quad_if(int exp_type) {
        if ( exp_type == 3 ) {
            quads.get(PJumps.pop()).setRes(quad_cont);
        }
    }

    // Genera el cuadruplo GOTO para else
    public void add_quad_else(int exp_type) {
        if ( exp_type == 3 ) {
            Quad _quad = new Quad(9, 0, 0, 0);
            quads.add(_quad);
            quad_cont++;
            quads.get(PJumps.pop()).setRes(quad_cont);
            PJumps.push(quad_cont-1);
        }
    }

    // Crea el cuadruplo GOTOV al terminar la evaluacion del while
    public void add_quad_while(int exp_type) {
        if ( !(exp_type == 3) ) {
            System.err.println("ERROR: Type mismatch: while exprestion is not type bool: type: "+exp_type);
            ERROR = true;
        } else {
            Quad _quad = new Quad(11, PilaO.pop(), 0, PJumps.pop());
            quads.add(_quad);
            quad_cont++;
        }
    }

    // Crea el archivo que se pasara a la maquina virtual
    public void QuadsToFile() {

        if (ERROR) {
            return;
        }

        String fileName = "cruadruplos.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(Integer.toString(dir)); 
            writer.newLine();

            writer.write(Integer.toString(ctes.size()));

            writer.newLine();
            for (Cte cte : ctes) {
                writer.write(Integer.toString(cte.getType()));
                writer.newLine();
                writer.write(cte.getValue().toString());
                writer.newLine();
                writer.write(Integer.toString(cte.getDir()));
                writer.newLine();
            }

            for (Quad quad : quads) {
                writer.write(quad.getOper()+" "+quad.getOpdo1()+" "+quad.getOpdo2()+" "+quad.getRes());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
}