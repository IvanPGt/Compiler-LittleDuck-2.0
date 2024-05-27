import java.util.*;

class Compilador {

    public ArrayList<Variable> globalVar = new ArrayList<>();
    public ArrayList<Variable> localVar = new ArrayList<>();
    public ArrayList<String> dirFunc = new ArrayList<>();
    public boolean isLocalVar = false;

    public CuboSemantico cuboSemantico = new CuboSemantico();
    public ArrayList<Quad> quads = new ArrayList<>();
    public int quad_cont = 0;

    public Stack<Integer> PilaO = new Stack<>();
    public Stack<Integer> PilaT = new Stack<>();
    public Stack<Integer> POper = new Stack<>();
    public int dir = 0;
    public int avail = 0;

    public Stack<Integer> PJumps = new Stack<>();

    public ArrayList<Cte> ctes = new ArrayList<>();

    public Object[] memoria;

    public int dir_int = 0;
    public int dir_float= 0;
    public int dir_string = 0;
    public int dir_bool = 0;


    //validar que no se pase de los rangos
    public int assign_dir(int type) {
        int assigned_dir;
        switch (type) {
            case 0:
                assigned_dir = dir_int;
                dir_int++;
                return assigned_dir;
            case 1:
                assigned_dir = dir_float + 10000;
                dir_float++;
                return assigned_dir;
            case 2:
                assigned_dir = dir_string + 20000;
                dir_string++;
                return assigned_dir;
            case 3:
                assigned_dir = dir_bool + 30000;
                dir_bool++;
                return assigned_dir;
            default:
                throw new AssertionError();
        }
    }

    public void add_DirFunc(String ID) {
        if ( dirFunc.contains(ID) ) {
            System.err.println("ERROR: Double definition for function: "+ID );
        }

        dirFunc.add(ID);
        System.out.println("\n\nFUNCION: "+ID+"\n");
    }


    public void imprimir_quads() {
        System.out.println(quads);
        System.out.println(dir);
    }

    public void validar_def_vars_globales(ArrayList<String> pendigIds, String type) {

        for(int i = 0; i < pendigIds.size(); i++){
            Variable _tupla = new Variable(pendigIds.get(i), type, dir);
            dir++;
            if ( globalVar.contains(_tupla) ) {
                System.err.println("ERROR: Double definition for global variable: "+pendigIds.get(i) );
            } else {
                globalVar.add(_tupla);
                System.out.println("Variable global: "+pendigIds.get(i)+" Tipo: "+type+"\n");
            }
 
        }

    }

    public void validar_def_vars_locales(ArrayList<String> pendigIds, String type) {
  
        for(int i = 0; i < pendigIds.size(); i++){
            Variable _tupla = new Variable(pendigIds.get(i), type, dir);
            dir++;

            if ( localVar.contains(_tupla) || globalVar.contains(_tupla) ) {
                System.err.println("ERROR: Double definition for local variable: "+pendigIds.get(i) );
            } else {
                localVar.add(_tupla);
                System.out.println("Variable funcion: "+pendigIds.get(i)+" Tipo: "+type+"\n");
            }

        }

    }

    public void validar_def_vars(ArrayList<String> pendigIds, String type) {
        if(isLocalVar) {
            validar_def_vars_locales(pendigIds, type);
        } else {
            validar_def_vars_globales(pendigIds, type);
        }
    }

    public void validar_def_parametros(String ID, String type) {
  
        Variable _tupla = new Variable(ID, type, dir);
        dir++;

        if ( localVar.contains(_tupla) || globalVar.contains(_tupla) ) {
            System.err.println("ERROR: Double definition for local variable parameter: "+ID );
        } else {
            localVar.add(_tupla);
            System.out.println("Parametro funcion: "+ID+" Tipo: "+type+"\n");
        }

    }

    public void validar_ID_locales(String ID) {
        Variable _tupla = new Variable(ID, null, dir);

        if ( !globalVar.contains(_tupla) && !localVar.contains(_tupla) ) {
            System.err.println("ERROR: Local Variable "+ID+" not exist" );
        }

    }

    public void validar_ID_globales(String ID) {
        Variable _tupla = new Variable(ID, null,dir);

        if ( !globalVar.contains(_tupla) ) {
            System.err.println("ERROR: Global variable "+ID+" not exist" );
        }
    }

    public void validar_ID(String ID) {
        if(isLocalVar) {
            validar_ID_locales(ID);
        } else {
            validar_ID_globales(ID);
        }
    }

    public void validar_func(String ID) {
        if ( !dirFunc.contains(ID) ) {
            System.err.println("ERROR: Function "+ID+" not exist" );
        }
    }


    public static Variable findVarById(ArrayList<Variable> list, String id) {
        for (Variable var : list) {
            if (var.getId().equals(id)) {
                return var;
            }
        }
        return null; // Retorna null si no se encuentra el _id
    }

    public Variable findVar(String ID) {
        if(isLocalVar) {
            return findVarById(localVar, ID);
        } else {
            return findVarById(globalVar, ID);
        }
    }

    public void add_quad_assign(String ID) {
        Variable _var = findVar(ID);
        if( cuboSemantico.getId(7, cuboSemantico.getTipoId(_var.getTipo()), PilaT.peek()) != 4 ) {
            Quad _quad = new Quad(7, PilaO.pop(), 0, _var.getDir());
            quads.add(_quad);
            quad_cont++;
            System.out.println(_quad);
        } else {
            System.err.println("ERROR: Assign Type mismatch");
        } 
    }

    public void do_push_ID(String ID) {
        Variable _var = findVar(ID);
        PilaO.push(_var.getDir());
        PilaT.push(cuboSemantico.getTipoId(_var.getTipo()));
    }

    public void do_push_Oper(String Oper) {
        POper.add(cuboSemantico.getOperId(Oper));
    }

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

            System.out.println(_quad);

        } else {
            System.err.println("ERROR: Type mismatch resolving expretion");
        }
    }

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

            Quad _quad = new Quad(0, L_O, -1, _tvar.getDir());
            quads.add(_quad);
            quad_cont++;

            System.out.println(_quad);

        }
    }

    public void do_push_CTE(Object value, int type) {
        Cte cte = new Cte(value, dir);

        PilaO.push(dir);
        PilaT.push(type);

        dir++;
        ctes.add(cte);
    }

    public void add_quad_top_pila() {

        PilaT.pop();
        Quad _quad = new Quad(8, 0, 0, PilaO.pop());
        quads.add(_quad);
        quad_cont++;

    }

    public void add_quad_if(int exp_type) {
        if ( !(exp_type == 3) ) {
            System.err.println("ERROR: Type mismatch: if exprestion is not type bool");
        } else {
            Quad _quad = new Quad(10, PilaO.pop(), 0, -1);
            quads.add(_quad);
            quad_cont++;
            System.out.println(_quad);
            PJumps.push(quad_cont-1);
        }
    }

    public void fill_quad_if(int exp_type) {
        if ( exp_type == 3 ) {
            quads.get(PJumps.pop()).setRes(quad_cont);
        }
    }

    public void add_quad_else(int exp_type) {
        if ( "bool".equals(exp_type) ) {

            System.out.println("ENTRE ELSE");
            Quad _quad = new Quad(9, 0, 0, 0);
            quads.add(_quad);
            quad_cont++;
            System.out.println(_quad);
            quads.get(PJumps.pop()).setRes(quad_cont);
            PJumps.push(quad_cont-1);
        }
    }

    public void add_quad_while(int exp_type) {
        if ( !(exp_type == 3) ) {
                System.err.println("ERROR: Type mismatch: while exprestion is not type bool: type: "+exp_type);
            } else {
                Quad _quad = new Quad(11, PilaO.pop(), 0, PJumps.pop());
                quads.add(_quad);
                quad_cont++;
                System.out.println(_quad);
            }
    }

    public void gen_memoria() {
        this.memoria = new Object[dir-1];

        for (Cte cte : ctes) {
            this.memoria[cte.getDir()] = cte.getValue();
        }
    }

    public void run_VM() {
        int it = 0;
        while(it != dir)
        switch (quads.get(it).getOper()) {
            case 0:

                break;
            case 1:
                
                break;
            case 2:
                
                break;
            case 3:
                
                break;
            case 4:
                
                break;
            case 5:
                
                break;
            case 6:
                
                break;
            case 7:
                
                break;
            case 8:
                
                break;
            case 9:
                
                break;
            case 10:
                
                break;
            case 11:
                
                break;

            default:
                // Bloque de código si ninguno de los casos anteriores es verdadero
        }
    }


}