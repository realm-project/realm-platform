lexer grammar QueryLexer;

//Order MATTERS!

//Relations
EQUAL : '=';
UNEQUAL : '!=';
CONTAINS: 'contains';
GT : '>';
LT : '<';
GTE : '>=';
LTE : '<=';
MATCH : '=~';
NOTMATCH : '!~';

//Operators
AND : 'and';
OR : 'or';

//Boolean
TRUE: 'true' | 'True' | 'TRUE';
FALSE: 'false' | 'False' | 'FALSE';

//etc...
DASH : '-';
POUND: '#';
DIGIT : [0-9] ;
OPENBRACKET : '(';
CLOSEBRACKET : ')';
DOT : '.';
STRING : '"' (~[\\"] | '\\' [\\"])* '"';
LETTER : [a-zA-Z];
LETTERS : LETTER+;
WS : [ \t\r\n]+ -> skip;
