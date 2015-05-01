// Generated from QueryParser.g4 by ANTLR 4.3
package net.objectof.model.query.parser;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link QueryParser}.
 */
public interface QueryParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by the {@code bracketedQuery}
	 * labeled alternative in {@link QueryParser#query}.
	 * @param ctx the parse tree
	 */
	void enterBracketedQuery(@NotNull QueryParser.BracketedQueryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code bracketedQuery}
	 * labeled alternative in {@link QueryParser#query}.
	 * @param ctx the parse tree
	 */
	void exitBracketedQuery(@NotNull QueryParser.BracketedQueryContext ctx);

	/**
	 * Enter a parse tree produced by {@link QueryParser#bool}.
	 * @param ctx the parse tree
	 */
	void enterBool(@NotNull QueryParser.BoolContext ctx);
	/**
	 * Exit a parse tree produced by {@link QueryParser#bool}.
	 * @param ctx the parse tree
	 */
	void exitBool(@NotNull QueryParser.BoolContext ctx);

	/**
	 * Enter a parse tree produced by {@link QueryParser#string}.
	 * @param ctx the parse tree
	 */
	void enterString(@NotNull QueryParser.StringContext ctx);
	/**
	 * Exit a parse tree produced by {@link QueryParser#string}.
	 * @param ctx the parse tree
	 */
	void exitString(@NotNull QueryParser.StringContext ctx);

	/**
	 * Enter a parse tree produced by {@link QueryParser#integer}.
	 * @param ctx the parse tree
	 */
	void enterInteger(@NotNull QueryParser.IntegerContext ctx);
	/**
	 * Exit a parse tree produced by {@link QueryParser#integer}.
	 * @param ctx the parse tree
	 */
	void exitInteger(@NotNull QueryParser.IntegerContext ctx);

	/**
	 * Enter a parse tree produced by {@link QueryParser#real}.
	 * @param ctx the parse tree
	 */
	void enterReal(@NotNull QueryParser.RealContext ctx);
	/**
	 * Exit a parse tree produced by {@link QueryParser#real}.
	 * @param ctx the parse tree
	 */
	void exitReal(@NotNull QueryParser.RealContext ctx);

	/**
	 * Enter a parse tree produced by {@link QueryParser#operator}.
	 * @param ctx the parse tree
	 */
	void enterOperator(@NotNull QueryParser.OperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link QueryParser#operator}.
	 * @param ctx the parse tree
	 */
	void exitOperator(@NotNull QueryParser.OperatorContext ctx);

	/**
	 * Enter a parse tree produced by {@link QueryParser#relation}.
	 * @param ctx the parse tree
	 */
	void enterRelation(@NotNull QueryParser.RelationContext ctx);
	/**
	 * Exit a parse tree produced by {@link QueryParser#relation}.
	 * @param ctx the parse tree
	 */
	void exitRelation(@NotNull QueryParser.RelationContext ctx);

	/**
	 * Enter a parse tree produced by the {@code compositeQuery}
	 * labeled alternative in {@link QueryParser#query}.
	 * @param ctx the parse tree
	 */
	void enterCompositeQuery(@NotNull QueryParser.CompositeQueryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code compositeQuery}
	 * labeled alternative in {@link QueryParser#query}.
	 * @param ctx the parse tree
	 */
	void exitCompositeQuery(@NotNull QueryParser.CompositeQueryContext ctx);

	/**
	 * Enter a parse tree produced by {@link QueryParser#retrieval}.
	 * @param ctx the parse tree
	 */
	void enterRetrieval(@NotNull QueryParser.RetrievalContext ctx);
	/**
	 * Exit a parse tree produced by {@link QueryParser#retrieval}.
	 * @param ctx the parse tree
	 */
	void exitRetrieval(@NotNull QueryParser.RetrievalContext ctx);

	/**
	 * Enter a parse tree produced by {@link QueryParser#digits}.
	 * @param ctx the parse tree
	 */
	void enterDigits(@NotNull QueryParser.DigitsContext ctx);
	/**
	 * Exit a parse tree produced by {@link QueryParser#digits}.
	 * @param ctx the parse tree
	 */
	void exitDigits(@NotNull QueryParser.DigitsContext ctx);

	/**
	 * Enter a parse tree produced by {@link QueryParser#id}.
	 * @param ctx the parse tree
	 */
	void enterId(@NotNull QueryParser.IdContext ctx);
	/**
	 * Exit a parse tree produced by {@link QueryParser#id}.
	 * @param ctx the parse tree
	 */
	void exitId(@NotNull QueryParser.IdContext ctx);

	/**
	 * Enter a parse tree produced by {@link QueryParser#relationship}.
	 * @param ctx the parse tree
	 */
	void enterRelationship(@NotNull QueryParser.RelationshipContext ctx);
	/**
	 * Exit a parse tree produced by {@link QueryParser#relationship}.
	 * @param ctx the parse tree
	 */
	void exitRelationship(@NotNull QueryParser.RelationshipContext ctx);

	/**
	 * Enter a parse tree produced by the {@code retrievalQuery}
	 * labeled alternative in {@link QueryParser#query}.
	 * @param ctx the parse tree
	 */
	void enterRetrievalQuery(@NotNull QueryParser.RetrievalQueryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code retrievalQuery}
	 * labeled alternative in {@link QueryParser#query}.
	 * @param ctx the parse tree
	 */
	void exitRetrievalQuery(@NotNull QueryParser.RetrievalQueryContext ctx);

	/**
	 * Enter a parse tree produced by {@link QueryParser#value}.
	 * @param ctx the parse tree
	 */
	void enterValue(@NotNull QueryParser.ValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link QueryParser#value}.
	 * @param ctx the parse tree
	 */
	void exitValue(@NotNull QueryParser.ValueContext ctx);

	/**
	 * Enter a parse tree produced by the {@code simpleQuery}
	 * labeled alternative in {@link QueryParser#query}.
	 * @param ctx the parse tree
	 */
	void enterSimpleQuery(@NotNull QueryParser.SimpleQueryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code simpleQuery}
	 * labeled alternative in {@link QueryParser#query}.
	 * @param ctx the parse tree
	 */
	void exitSimpleQuery(@NotNull QueryParser.SimpleQueryContext ctx);
}