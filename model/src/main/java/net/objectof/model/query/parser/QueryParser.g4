parser grammar QueryParser;
options { tokenVocab = QueryLexer; }

query: OPENBRACKET theQuery=query CLOSEBRACKET #bracketedQuery
     | leftQuery=query theOperator=operator rightQuery=query #compositeQuery
     | relationship #simpleQuery
     | retrieval #retrievalQuery
     ;

relationship: theField=LETTERS theRelation=relation theValue=value;

retrieval: theId=id DOT theField=LETTERS;

value: string
     | real
     | integer
     | id
     ;
relation: GT EQUAL | LT EQUAL | EQUAL | BANG EQUAL | CONTAINS | GT | LT ;

operator: AND | OR;


id: theKind=LETTERS DASH theLabel=integer;
integer : DASH? digits;
real : DASH? digits DOT digits?;
string : QUOTE theString=NONGREEDY QUOTE;

digits : DIGIT+ ;