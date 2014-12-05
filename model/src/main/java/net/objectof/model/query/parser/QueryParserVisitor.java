// Generated from QueryParser.g4 by ANTLR 4.3
package net.objectof.model.query.parser;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link QueryParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface QueryParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by the {@code bracketedQuery}
	 * labeled alternative in {@link QueryParser#query}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBracketedQuery(@NotNull QueryParser.BracketedQueryContext ctx);

	/**
	 * Visit a parse tree produced by {@link QueryParser#string}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitString(@NotNull QueryParser.StringContext ctx);

	/**
	 * Visit a parse tree produced by {@link QueryParser#integer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInteger(@NotNull QueryParser.IntegerContext ctx);

	/**
	 * Visit a parse tree produced by {@link QueryParser#real}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReal(@NotNull QueryParser.RealContext ctx);

	/**
	 * Visit a parse tree produced by {@link QueryParser#operator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperator(@NotNull QueryParser.OperatorContext ctx);

	/**
	 * Visit a parse tree produced by {@link QueryParser#relation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelation(@NotNull QueryParser.RelationContext ctx);

	/**
	 * Visit a parse tree produced by the {@code compositeQuery}
	 * labeled alternative in {@link QueryParser#query}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompositeQuery(@NotNull QueryParser.CompositeQueryContext ctx);

	/**
	 * Visit a parse tree produced by {@link QueryParser#retrieval}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRetrieval(@NotNull QueryParser.RetrievalContext ctx);

	/**
	 * Visit a parse tree produced by {@link QueryParser#digits}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDigits(@NotNull QueryParser.DigitsContext ctx);

	/**
	 * Visit a parse tree produced by {@link QueryParser#id}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitId(@NotNull QueryParser.IdContext ctx);

	/**
	 * Visit a parse tree produced by {@link QueryParser#relationship}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelationship(@NotNull QueryParser.RelationshipContext ctx);

	/**
	 * Visit a parse tree produced by the {@code retrievalQuery}
	 * labeled alternative in {@link QueryParser#query}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRetrievalQuery(@NotNull QueryParser.RetrievalQueryContext ctx);

	/**
	 * Visit a parse tree produced by {@link QueryParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue(@NotNull QueryParser.ValueContext ctx);

	/**
	 * Visit a parse tree produced by the {@code simpleQuery}
	 * labeled alternative in {@link QueryParser#query}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleQuery(@NotNull QueryParser.SimpleQueryContext ctx);
}