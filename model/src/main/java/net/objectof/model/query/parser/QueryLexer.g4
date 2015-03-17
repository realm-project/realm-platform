lexer grammar QueryLexer;

//Order MATTERS!
QUOTE : '"';
EQUAL : '=';
BANG : '!';
DASH : '-';
AND : 'and';
OR : 'or';
CONTAINS: 'contains';
DIGIT : [0-9] ;
OPENBRACKET : '(';
CLOSEBRACKET : ')';
GT : '>';
LT : '<';
DOT : '.';
ESCAPE : '\\';


LETTER : [a-zA-Z];
LETTERS : LETTER+;
WS : [ \t\r\n]+ -> skip;
