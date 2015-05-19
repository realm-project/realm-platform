package net.objectof.model.query.parser;


import net.objectof.model.Resource;
import net.objectof.model.Transaction;
import net.objectof.model.query.ICompositeQuery;
import net.objectof.model.query.ILoadQuery;
import net.objectof.model.query.IQuery;
import net.objectof.model.query.Operator;
import net.objectof.model.query.Query;
import net.objectof.model.query.Relation;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTree;


public class QueryBuilder extends QueryParserBaseVisitor<Object> {

    public static Query build(String queryText, Transaction loaderTx) {
        ANTLRInputStream input = new ANTLRInputStream(queryText);
        QueryLexer lexer = new QueryLexer(input);
        lexer.removeErrorListeners();
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        QueryParser parser = new QueryParser(tokens);
        parser.setErrorHandler(new BailErrorStrategy());
        parser.removeErrorListeners();
        ParserRuleContext tree;
        try {
            tree = parser.query();
        }
        catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
        QueryBuilder builder = new QueryBuilder(loaderTx);
        Query query = builder.buildQuery(tree);
        return query;
    }

    private Transaction tx;

    private QueryBuilder(Transaction tx) {
        this.tx = tx;
    }

    private Query buildQuery(ParseTree tree) {
        return (Query) visit(tree);
    }

    @Override
    public Operator visitOperator(@NotNull QueryParser.OperatorContext ctx) {
        switch (ctx.getText()) {
            case "and":
                return Operator.AND;
            case "or":
                return Operator.OR;
            default:
                throw new UnsupportedOperationException();
        }
    }

    @Override
    public Query visitBracketedQuery(@NotNull QueryParser.BracketedQueryContext ctx) {
        return (Query) visit(ctx.theQuery);
    }

    @Override
    public Query visitCompositeQuery(@NotNull QueryParser.CompositeQueryContext ctx) {
        Query left = (Query) visit(ctx.leftQuery);
        Query right = (Query) visit(ctx.rightQuery);
        Operator op = (Operator) visit(ctx.operator());
        return new ICompositeQuery(op, left, right);
    }

    @Override
    public Query visitRelationship(@NotNull QueryParser.RelationshipContext ctx) {
        String key = ctx.theField.getText();
        Relation relation = (Relation) visit(ctx.theRelation);
        Object value = visit(ctx.theValue);
        if (key == null || value == null || relation == null) throw new NullPointerException();
        return new IQuery(key, relation, value);
    }

    @Override
    public ILoadQuery visitRetrieval(@NotNull QueryParser.RetrievalContext ctx) {
        String key = ctx.theField.getText();
        Resource<?> res = (Resource<?>) visit(ctx.theId);
        ILoadQuery query = new ILoadQuery(res.id(), key);
        return query;
    }

    @Override
    public Relation visitRelation(@NotNull QueryParser.RelationContext ctx) {
        switch (ctx.getText()) {
            case "=":
                return Relation.EQUAL;
            case "!=":
                return Relation.UNEQUAL;
            case "contains":
                return Relation.CONTAINS;
            case "<=":
                return Relation.LTE;
            case ">=":
                return Relation.GTE;
            case "<":
                return Relation.LT;
            case ">":
                return Relation.GT;
            default:
                throw new UnsupportedOperationException();
        }
    }

    @Override
    public Resource<?> visitId(@NotNull QueryParser.IdContext ctx) {
        String kind = ctx.theKind.getText();
        String label = visit(ctx.theLabel).toString();
        return tx.retrieve(kind, label);
    }

    @Override
    public Long visitInteger(@NotNull QueryParser.IntegerContext ctx) {
        return Long.parseLong(ctx.getText());
    }

    @Override
    public Double visitReal(@NotNull QueryParser.RealContext ctx) {
        return Double.parseDouble(ctx.getText());
    }

    @Override
    public Boolean visitBool(@NotNull QueryParser.BoolContext ctx) {
        return Boolean.parseBoolean(ctx.getText());
    }

    @Override
    public String visitString(@NotNull QueryParser.StringContext ctx) {
        String text = ctx.theValue.getText();
        // get rid of quotes
        text = text.substring(1, text.length() - 1);
        // place escape-processed characters in here
        StringBuilder sb = new StringBuilder();
        boolean escaped = false;
        // process escaped characters
        for (int i = 0; i < text.length(); i++) {
            String ch = text.substring(i, i + 1);
            // if we're already escaped from the last character
            if (escaped) {
                sb.append(ch); // works for \ and "
                escaped = false;
                continue;
            }
            // check to see if this is the escape character
            escaped = ("\\".equals(ch));
            // only append this char if it isn't the escape character
            if (!escaped) {
                sb.append(ch);
            }
        }
        return sb.toString();
    }
}
