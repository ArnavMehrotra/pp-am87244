grammar SimpleLang;

program: 'program' ident (constdecl | vardecl | classdecl| enumdecl | interfacedecl)* '{' (methoddecl)* '}';

constdecl: 'const' type ident '=' (ConstNum | char | bool) (',' ident '=' (ConstNum | char | bool))* ';';

enumdecl: 'enum' ident '{' ident ('=' ConstNum)* (',' ident ('=' ConstNum)*)* '}';

vardecl: type ident ('[' ']')* (',' ident ('[' ']')*)* ';';

classdecl: 'class' ident ('extends' type)* ('implements' type (',' type)*)* '{' {vardecl}('{'(methoddecl)* '}')*'}';

statement: 
| designatorstatement ';'
| 'if' '(' condition ')' statement ('else' statement)*
| 'for' '(' (designatorstatement)* ';' (condition)* ';' (designatorstatement)* ')' statement
| 'break' ';'
| 'continue' ';'
| 'return' (expr)* ';'
| 'read' '(' designator ')' ';'
| 'print' '(' expr (',' ConstNum)? ')' ';'
| '{' (statement)? '}'
;


interfacedecl: 'interface' ident '{' (interfacemethoddecl)* '}';

interfacemethoddecl: (type | 'void') ident '(' formpars ')' ';';

methoddecl: (type | 'void') ident '(' (formpars)? ')' (vardecl)* '{' (statement)* '}';

formpars: type ident ('[' ']')* (','type ident ('[' ']')*)*;

//I have no idea what expr was supposed to be
expr: ('-')* term (addop term)*;

factor: (designator '(' actparse ')') | ConstNum | bool | ('new' type '['expr']') | '('expr')';

term: factor (mulop factor)*;

type: ident;

designatorstatement: designator ((assignop expr) | ('(' actparse ')') | '++' | '--')*;

condition: condterm ('||' condterm)*;

condterm: condfact ('&&' condfact)*;

condfact: expr (relop expr);

actparse: expr (',' expr)*;

designator: ident (('.' ident) | ('[' expr ']'))*;

operator: addop | relop | mulop | assignop | '&&' | '||' | '++' | '--';

ident: ID;

assignop: '=';

addop: '+' | '-';

relop: '==' | '!=' | '>' | '>=' | '<' | '<=';

mulop: '*' | '/' | '%';

bool: 'true' | 'false';

char: '"'ID'"';

integer: ConstNum;

ConstNum: [0-9]+;

ID: [a-zA-Z0-9]+ ;

WS : [ \t\r\n]+ -> skip ; // skip spaces, tabs, newlines
