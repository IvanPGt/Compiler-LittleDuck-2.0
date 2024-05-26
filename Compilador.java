import java.util.*;

class Compilador {

    public ArrayList<Variable> globalVar = new ArrayList<>();
    public ArrayList<String> dirFunc = new ArrayList<>();
    public boolean isLocalVar = false;

    public void add_DirFunc(String ID) {
        if ( dirFunc.contains(ID) ) {
            System.err.println("ERROR: Double definition for function: "+ID );
        }

        dirFunc.add(ID);
        System.out.println("\n\nFUNCION: "+ID+"\n");
    }

    public void imprimir(String str) {
        System.out.println(str);
    }
    

    public void validar_def_vars(ArrayList<String> pendigIds, String type) {

        for(int i = 0; i < pendigIds.size(); i++){
            Variable _tupla = new Variable(pendigIds.get(i), type);
            if ( globalVar.contains(_tupla) ) {
                System.err.println("ERROR: Double definition for global variable: "+pendigIds.get(i) );
            }

            globalVar.add(_tupla);
            System.out.println("Variable global: "+pendigIds.get(i)+" Tipo: "+type+"\n");
            
        }

    }

    public void validar_def_vars_locales(ArrayList<String> pendigIds, ArrayList<Variable> localVar, String type) {
  
        for(int i = 0; i < pendigIds.size(); i++){
            Variable _tupla = new Variable(pendigIds.get(i), type);

            if ( localVar.contains(_tupla) || globalVar.contains(_tupla) ) {
                System.err.println("ERROR: Double definition for local variable: "+pendigIds.get(i) );
            }

            localVar.add(_tupla);
            System.out.println("Variable funcion: "+pendigIds.get(i)+" Tipo: "+type+"\n");

        }

    }

    public void validar_def_parametros(ArrayList<Variable> localVar, String ID, String type) {
  
        Variable _tupla = new Variable(ID, type);

        if ( localVar.contains(_tupla) || globalVar.contains(_tupla) ) {
            System.err.println("ERROR: Double definition for local variable parameter: "+ID );
        }

        localVar.add(_tupla);
        System.out.println("Parametro funcion: "+ID+" Tipo: "+type+"\n");

    }

    public void validar_ID_locales(String ID, ArrayList<Variable> localVar) {
        Variable _tupla = new Variable(ID, null);
        if ( !globalVar.contains(_tupla) && !localVar.contains(_tupla) ) {
            System.err.println("ERROR: Local Variable "+ID+" not exist" );
        }

    }

    public void validar_ID(String ID) {
        Variable _tupla = new Variable(ID, null);
        if ( !globalVar.contains(_tupla) ) {
            System.err.println("ERROR: Global variable "+ID+" not exist" );
        }
    }

    public void validar_func(String ID) {
        if ( !dirFunc.contains(ID) ) {
            System.err.println("ERROR: Function "+ID+" not exist" );
        }
    }


}