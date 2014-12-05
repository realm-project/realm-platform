lexer grammar QueryLexer;

//Order MATTERS!
QUOTE : '\"';
NONGREEDY : (.)*?;
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


LETTER : [a-zA-Z];
LETTERS : LETTER+;
WS : [ \t\r\n]+ -> skip;
