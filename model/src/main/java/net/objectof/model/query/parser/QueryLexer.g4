lexer grammar QueryLexer;

//Order MATTERS!
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
POUND: '#';

TRUE: 'true' | 'True' | 'TRUE';
FALSE: 'false' | 'False' | 'FALSE';


STRING : '"' (~[\\"] | '\\' [\\"])* '"';
LETTER : [a-zA-Z];
LETTERS : LETTER+;
WS : [ \t\r\n]+ -> skip;
