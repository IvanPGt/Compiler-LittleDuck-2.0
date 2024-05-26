grammar LittleDuck;

programa locals [Compilador comp = new Compilador()]
    :   'program' ID { $programa::comp.add_DirFunc($ID.text); } ';' vars funcs 'main' body 'end' ;
vars:   md_vars
    | 
    ;
md_vars
    :   'var' def_vars ;
def_vars locals [ArrayList<String> pendigIds = new ArrayList<String>()]
    :   list_ids ':' type { 
        
        if($programa::comp.isLocalVar) {
            $programa::comp.validar_def_vars_locales($def_vars::pendigIds, $funcs::localVar, $type.text);
        } else {
            $programa::comp.validar_def_vars($def_vars::pendigIds, $type.text);
        }
        
        }';' def_vars_ ;
def_vars_
    :   def_vars
    |
    ;
list_ids
    :   ID { $def_vars::pendigIds.add($ID.text); } list_ids_ ;
list_ids_
    :   ',' list_ids
    |   
    ;
type:   'int'
    |   'float'
    ;
funcs locals [ArrayList<Variable> localVar = new ArrayList<Variable>()]
    :   {$programa::comp.isLocalVar = true;} md_funcs md_funcs_ {$programa::comp.isLocalVar = false;}
    |   
    ;
md_funcs_
    :   funcs
    |
    ;
md_funcs
    :   'void' ID { $programa::comp.add_DirFunc($ID.text); } '(' ids_funcs ')' '[' vars body ']' ';';
ids_funcs
    :   ID ':' type { $programa::comp.validar_def_parametros($funcs::localVar, $ID.text, $type.text); } ids_funcs_
    |   
    ;
ids_funcs_
    :   ',' ids_funcs
    |
    ;
body:   '{' md_body '}' ;
md_body
    :   statement md_body_
    |
    ;
md_body_
    :   md_body
    |
    ;
statement
    :   assign
    |   condition
    |   cicle
    |   f_call
    |   print
    ;
assign
    :   ID {

        if($programa::comp.isLocalVar) {
            $programa::comp.validar_ID_locales($ID.text, $funcs::localVar);
        } else {
            $programa::comp.validar_ID($ID.text);
        }
        
        }'=' expresion ';' ;
expresion
    :   exp md_exp ;
md_exp
    :   expresion_op exp
    |
    ;
expresion_op
    :   '>'
    |   '<'
    |   '!='
    ;
exp
    :   termino termino_ ;
termino_
    :   termino_op exp
    |   
    ;
termino_op
    :   '+'
    |   '-'
    ;
termino
    :   factor factor_ ;
factor_
    :   factor_op termino
    |
    ;
factor_op
    :   '*'
    |   '/'
    ;
factor
    :   '(' expresion ')'
    |   factor_op_ factor_cte
    ;
factor_op_
    :   termino_op
    |
    ;
factor_cte
    :   ID { if($programa::comp.isLocalVar) {
            $programa::comp.validar_ID_locales($ID.text, $funcs::localVar);
        } else {
            $programa::comp.validar_ID($ID.text);
        }
    }
    |   cte
    ;
cte
    :   CTE_INT
    |   CTE_FLOAT
    ;
condition
    :   'if' '(' expresion ')' body condition_else ';' ;
condition_else
    :   'else' body
    |
    ;
cicle
    :   'do' body 'while' '(' expresion ')' ';' ;
f_call
    :   ID { $programa::comp.validar_func($ID.text); }'(' md_f_call ')' ';' ;
md_f_call
    :   expresion md_f_call_
    |
    ;
md_f_call_
    :   ',' md_f_call
    |
    ;
print
    :   'print''(' md_print ')' ';';
md_print
    :   CTE_STRING md_print_
    |   expresion md_print_
    ;
md_print_
    :   ',' md_print
    |
    ;
ID      : [a-zA-Z_]+[a-zA-Z0-9]* ;
CTE_INT     : [0-9]+ ; 
CTE_FLOAT   : [0-9]+(.[0-9]+)?;
CTE_STRING  : '"'~["]*'"';
NEWLINE : [\r\n\t]+ ->skip;
WHITESPACE : ' ' ->skip;