grammar LittleDuck;

programa locals [Compilador comp = new Compilador()]
    :   'program' ID { $programa::comp.add_DirFunc($ID.text); } ';' vars funcs 'main' body 'end' {$programa::comp.imprimir_quads();};
vars:   md_vars
    | 
    ;
md_vars
    :   'var' def_vars ;
def_vars locals [ArrayList<String> pendigIds = new ArrayList<String>()]
    :   list_ids ':' type { $programa::comp.validar_def_vars($def_vars::pendigIds, $type.text); } ';' def_vars_ ;
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
funcs 
    :   {$programa::comp.isLocalVar = true;} md_funcs md_funcs_ {$programa::comp.isLocalVar = false; $programa::comp.localVar.clear();}
    |   
    ;
md_funcs_
    :   funcs
    |
    ;
md_funcs
    :   'void' ID { $programa::comp.add_DirFunc($ID.text); } '(' ids_funcs ')' '[' vars body ']' ';';
ids_funcs
    :   ID ':' type { $programa::comp.validar_def_parametros($ID.text, $type.text); } ids_funcs_
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
    :   ID { $programa::comp.validar_ID($ID.text); }'=' expresion ';' { $programa::comp.add_quad_assign($ID.text); };
expresion
    :   exp md_exp ;
md_exp
    :   expresion_op { $programa::comp.do_push_Oper($expresion_op.text); } exp {
        if(!$programa::comp.POper.empty()){
            if( $programa::comp.POper.peek() == 4 || $programa::comp.POper.peek() == 5 || $programa::comp.POper.peek() == 6 ) {
                $programa::comp.add_quad_expresion();
            }
        }
    }
    |
    ;
expresion_op
    :   '>'
    |   '<'
    |   '!='
    ;
exp
    :   termino {
        if(!$programa::comp.POper.empty()){
            if($programa::comp.POper.peek() == 2 || $programa::comp.POper.peek() == 3 ) {
                $programa::comp.add_quad_expresion();
            }
        }
    } termino_ ;
termino_
    :   termino_op { $programa::comp.do_push_Oper($termino_op.text); } exp
    |   
    ;
termino_op
    :   '+'
    |   '-'
    ;
termino
    :   factor {
        if(!$programa::comp.POper.empty()){
            if($programa::comp.POper.peek() == 0 || $programa::comp.POper.peek() == 1 ) {
                $programa::comp.add_quad_expresion();
            }
        }
    } factor_ ;
factor_
    :   factor_op { $programa::comp.do_push_Oper($factor_op.text); } termino
    |
    ;
factor_op
    :   '*'
    |   '/'
    ;
factor
    :   '(' { $programa::comp.POper.push(-1); } expresion ')' { $programa::comp.POper.pop(); }
    |   factor_op_ factor_cte { $programa::comp.check_negative($factor_op_.text); }
    ;
factor_op_
    :   termino_op
    |
    ;
factor_cte
    :   ID { $programa::comp.validar_ID($ID.text); $programa::comp.do_push_ID($ID.text); }
    |   cte
    ;
cte
    :   CTE_INT  { $programa::comp.do_push_CTE($CTE_INT.int, 0); }
    |   CTE_FLOAT  { $programa::comp.do_push_CTE(Float.parseFloat($CTE_FLOAT.text), 1); }
    ;
condition locals [ int exp_type ]
    :   'if' '(' expresion ')' {$condition::exp_type = $programa::comp.PilaT.pop(); $programa::comp.add_quad_if($condition::exp_type); } body condition_else ';' { $programa::comp.fill_quad_if($condition::exp_type); } ;
condition_else
    :   'else' { $programa::comp.add_quad_else($condition::exp_type); } body
    |
    ;
cicle
    :   'do' { $programa::comp.PJumps.push($programa::comp.quad_cont); } body 'while' '(' expresion ')' {$programa::comp.add_quad_while($programa::comp.PilaT.pop());} ';' ;
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
    :   CTE_STRING { $programa::comp.do_push_CTE($CTE_STRING.text, 2); $programa::comp.add_quad_top_pila(); } md_print_
    |   expresion { $programa::comp.add_quad_top_pila(); } md_print_
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