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
     | bool
     ;
relation: GTE | LTE | EQUAL | UNEQUAL | CONTAINS | GT | LT | MATCH | NOTMATCH ;

operator: AND | OR;


id: theKind=LETTERS DASH theLabel=integer
  | theKind=LETTERS POUND theLabel=integer
  ;
integer : DASH? digits;
real : DASH? digits DOT digits?;

bool: theValue=TRUE
    | theValue=FALSE;
       
string :  theValue=STRING;

digits : DIGIT+ ;