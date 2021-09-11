grammar SimpleLang;

program: 'program' ID (constdecl | varDecl | classdecl| enumdecl | interfacedecl)* '{' (methoddecl)* '}';

constdecl: 'const' type ID '=' (ConstNum | charConst | bool) (',' ID '=' (ConstNum | charConst | bool))* ';';

enumdecl: 'enum' ID '{' ID ('=' ConstNum)* (',' ID ('=' ConstNum)*)* '}';

varDecl: type ID ('[' ']')? (',' ID ('[' ']')?)* ';' ;

classdecl: 'class' ID ('extends' type)* ('implements' type (',' type)*)* '{' (varDecl)*('{'(methoddecl)* '}')*'}';

statement
: designatorstatement ';'
| 'if' '(' condition ')' statement ('else' statement)?
| 'for' '(' (designatorstatement)? ';' (condition)? ';' (designatorstatement)? ')' statement
| 'break' ';'
| 'continue' ';'
| 'return' (expr)* ';'
| 'read' '(' designator ')' ';'
| 'print' '(' expr (',' ConstNum)? ')' ';'
| '{' (statement)? '}'
;


interfacedecl: 'interface' ID '{' (interfacemethoddecl)* '}';

interfacemethoddecl: (type | 'void') ID '(' formpars ')' ';';

methoddecl: (type | 'void') ID '(' (formpars)? ')' (varDecl)* '{' (statement)* '}';

formpars: type ID ('[' ']')? (','type ID ('[' ']')?)*;

//I have no idea what expr was supposed to be
expr: ('-')* term (addop term)*;

factor: (designator '(' actparse ')') | ConstNum | bool | ('new' type '['expr']') | '('expr')';

term: factor (mulop factor)*;

type: ID;

designatorstatement: designator ((assignop expr) | ('(' actparse ')') | '++' | '--')*;

condition: condterm ('||' condterm)*;

condterm: condfact ('&&' condfact)*;

condfact: expr (relop expr);

actparse: expr (',' expr)*;

designator: ID (('.' ID) | ('[' expr ']'))*;

operator: addop | relop | mulop | assignop | '&&' | '||' | '++' | '--';

assignop: '=';

addop: '+' | '-';

relop: '==' | '!=' | '>' | '>=' | '<' | '<=';

mulop: '*' | '/' | '%';

bool: 'true' | 'false';

charConst: '"'ID'"';

integer: ConstNum;

ConstNum: [0-9]+;

ID: [a-zA-Z0-9]+ ;

WS : [ \t\r\n]+ -> skip ; // skip spaces, tabs, newlines
