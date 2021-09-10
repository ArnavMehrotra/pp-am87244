grammar SimpleLang;

program: 'program' ID;

expr: (ident | integer) Addop (ID | ConstNum);

designatorstatement: ident assignop (integer | ident) operator (integer | ident);

operator: Addop | Relop | Mulop | assignop;

ident: ID;

assignop: '=';

Addop: '+' | '-';

Relop: '==' | '!=' | '>' | '>=' | '<' | '<=';

Mulop: '*' | '/' | '%';

integer: ConstNum;

ConstNum: [0-9]+;

ID: [a-zA-Z0-9]+ ;

WS : [ \t\r\n]+ -> skip ; // skip spaces, tabs, newlines
