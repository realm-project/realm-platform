// Generated from QueryParser.g4 by ANTLR 4.3
package net.objectof.model.query.parser;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class QueryParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		QUOTE=1, LETTER=15, OR=7, NONGREEDY=2, EQUAL=3, BANG=4, LT=13, DOT=14, 
		CONTAINS=8, DASH=5, DIGIT=9, GT=12, OPENBRACKET=10, LETTERS=16, AND=6, 
		CLOSEBRACKET=11, WS=17;
	public static final String[] tokenNames = {
		"<INVALID>", "'\"'", "NONGREEDY", "'='", "'!'", "'-'", "'and'", "'or'", 
		"'contains'", "DIGIT", "'('", "')'", "'>'", "'<'", "'.'", "LETTER", "LETTERS", 
		"WS"
	};
	public static final int
		RULE_query = 0, RULE_relationship = 1, RULE_retrieval = 2, RULE_value = 3, 
		RULE_relation = 4, RULE_operator = 5, RULE_id = 6, RULE_integer = 7, RULE_real = 8, 
		RULE_string = 9, RULE_digits = 10;
	public static final String[] ruleNames = {
		"query", "relationship", "retrieval", "value", "relation", "operator", 
		"id", "integer", "real", "string", "digits"
	};

	@Override
	public String getGrammarFileName() { return "QueryParser.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public QueryParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class QueryContext extends ParserRuleContext {
		public QueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_query; }
	 
		public QueryContext() { }
		public void copyFrom(QueryContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class BracketedQueryContext extends QueryContext {
		public QueryContext theQuery;
		public TerminalNode OPENBRACKET() { return getToken(QueryParser.OPENBRACKET, 0); }
		public QueryContext query() {
			return getRuleContext(QueryContext.class,0);
		}
		public TerminalNode CLOSEBRACKET() { return getToken(QueryParser.CLOSEBRACKET, 0); }
		public BracketedQueryContext(QueryContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof QueryParserListener ) ((QueryParserListener)listener).enterBracketedQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof QueryParserListener ) ((QueryParserListener)listener).exitBracketedQuery(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QueryParserVisitor ) return ((QueryParserVisitor<? extends T>)visitor).visitBracketedQuery(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class CompositeQueryContext extends QueryContext {
		public QueryContext leftQuery;
		public OperatorContext theOperator;
		public QueryContext rightQuery;
		public OperatorContext operator() {
			return getRuleContext(OperatorContext.class,0);
		}
		public List<QueryContext> query() {
			return getRuleContexts(QueryContext.class);
		}
		public QueryContext query(int i) {
			return getRuleContext(QueryContext.class,i);
		}
		public CompositeQueryContext(QueryContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof QueryParserListener ) ((QueryParserListener)listener).enterCompositeQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof QueryParserListener ) ((QueryParserListener)listener).exitCompositeQuery(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QueryParserVisitor ) return ((QueryParserVisitor<? extends T>)visitor).visitCompositeQuery(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class RetrievalQueryContext extends QueryContext {
		public RetrievalContext retrieval() {
			return getRuleContext(RetrievalContext.class,0);
		}
		public RetrievalQueryContext(QueryContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof QueryParserListener ) ((QueryParserListener)listener).enterRetrievalQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof QueryParserListener ) ((QueryParserListener)listener).exitRetrievalQuery(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QueryParserVisitor ) return ((QueryParserVisitor<? extends T>)visitor).visitRetrievalQuery(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SimpleQueryContext extends QueryContext {
		public RelationshipContext relationship() {
			return getRuleContext(RelationshipContext.class,0);
		}
		public SimpleQueryContext(QueryContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof QueryParserListener ) ((QueryParserListener)listener).enterSimpleQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof QueryParserListener ) ((QueryParserListener)listener).exitSimpleQuery(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QueryParserVisitor ) return ((QueryParserVisitor<? extends T>)visitor).visitSimpleQuery(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QueryContext query() throws RecognitionException {
		return query(0);
	}

	private QueryContext query(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		QueryContext _localctx = new QueryContext(_ctx, _parentState);
		QueryContext _prevctx = _localctx;
		int _startState = 0;
		enterRecursionRule(_localctx, 0, RULE_query, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(29);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				{
				_localctx = new BracketedQueryContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(23); match(OPENBRACKET);
				setState(24); ((BracketedQueryContext)_localctx).theQuery = query(0);
				setState(25); match(CLOSEBRACKET);
				}
				break;

			case 2:
				{
				_localctx = new SimpleQueryContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(27); relationship();
				}
				break;

			case 3:
				{
				_localctx = new RetrievalQueryContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(28); retrieval();
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(37);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new CompositeQueryContext(new QueryContext(_parentctx, _parentState));
					((CompositeQueryContext)_localctx).leftQuery = _prevctx;
					pushNewRecursionContext(_localctx, _startState, RULE_query);
					setState(31);
					if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
					setState(32); ((CompositeQueryContext)_localctx).theOperator = operator();
					setState(33); ((CompositeQueryContext)_localctx).rightQuery = query(4);
					}
					} 
				}
				setState(39);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class RelationshipContext extends ParserRuleContext {
		public Token theField;
		public RelationContext theRelation;
		public ValueContext theValue;
		public RelationContext relation() {
			return getRuleContext(RelationContext.class,0);
		}
		public ValueContext value() {
			return getRuleContext(ValueContext.class,0);
		}
		public TerminalNode LETTERS() { return getToken(QueryParser.LETTERS, 0); }
		public RelationshipContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_relationship; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof QueryParserListener ) ((QueryParserListener)listener).enterRelationship(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof QueryParserListener ) ((QueryParserListener)listener).exitRelationship(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QueryParserVisitor ) return ((QueryParserVisitor<? extends T>)visitor).visitRelationship(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RelationshipContext relationship() throws RecognitionException {
		RelationshipContext _localctx = new RelationshipContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_relationship);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(40); ((RelationshipContext)_localctx).theField = match(LETTERS);
			setState(41); ((RelationshipContext)_localctx).theRelation = relation();
			setState(42); ((RelationshipContext)_localctx).theValue = value();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RetrievalContext extends ParserRuleContext {
		public IdContext theId;
		public Token theField;
		public TerminalNode DOT() { return getToken(QueryParser.DOT, 0); }
		public IdContext id() {
			return getRuleContext(IdContext.class,0);
		}
		public TerminalNode LETTERS() { return getToken(QueryParser.LETTERS, 0); }
		public RetrievalContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_retrieval; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof QueryParserListener ) ((QueryParserListener)listener).enterRetrieval(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof QueryParserListener ) ((QueryParserListener)listener).exitRetrieval(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QueryParserVisitor ) return ((QueryParserVisitor<? extends T>)visitor).visitRetrieval(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RetrievalContext retrieval() throws RecognitionException {
		RetrievalContext _localctx = new RetrievalContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_retrieval);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(44); ((RetrievalContext)_localctx).theId = id();
			setState(45); match(DOT);
			setState(46); ((RetrievalContext)_localctx).theField = match(LETTERS);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ValueContext extends ParserRuleContext {
		public IntegerContext integer() {
			return getRuleContext(IntegerContext.class,0);
		}
		public RealContext real() {
			return getRuleContext(RealContext.class,0);
		}
		public StringContext string() {
			return getRuleContext(StringContext.class,0);
		}
		public IdContext id() {
			return getRuleContext(IdContext.class,0);
		}
		public ValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_value; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof QueryParserListener ) ((QueryParserListener)listener).enterValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof QueryParserListener ) ((QueryParserListener)listener).exitValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QueryParserVisitor ) return ((QueryParserVisitor<? extends T>)visitor).visitValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValueContext value() throws RecognitionException {
		ValueContext _localctx = new ValueContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_value);
		try {
			setState(52);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(48); string();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(49); real();
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(50); integer();
				}
				break;

			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(51); id();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RelationContext extends ParserRuleContext {
		public TerminalNode EQUAL() { return getToken(QueryParser.EQUAL, 0); }
		public TerminalNode CONTAINS() { return getToken(QueryParser.CONTAINS, 0); }
		public TerminalNode LT() { return getToken(QueryParser.LT, 0); }
		public TerminalNode GT() { return getToken(QueryParser.GT, 0); }
		public TerminalNode BANG() { return getToken(QueryParser.BANG, 0); }
		public RelationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_relation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof QueryParserListener ) ((QueryParserListener)listener).enterRelation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof QueryParserListener ) ((QueryParserListener)listener).exitRelation(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QueryParserVisitor ) return ((QueryParserVisitor<? extends T>)visitor).visitRelation(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RelationContext relation() throws RecognitionException {
		RelationContext _localctx = new RelationContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_relation);
		try {
			setState(64);
			switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(54); match(GT);
				setState(55); match(EQUAL);
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(56); match(LT);
				setState(57); match(EQUAL);
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(58); match(EQUAL);
				}
				break;

			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(59); match(BANG);
				setState(60); match(EQUAL);
				}
				break;

			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(61); match(CONTAINS);
				}
				break;

			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(62); match(GT);
				}
				break;

			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(63); match(LT);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class OperatorContext extends ParserRuleContext {
		public TerminalNode AND() { return getToken(QueryParser.AND, 0); }
		public TerminalNode OR() { return getToken(QueryParser.OR, 0); }
		public OperatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_operator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof QueryParserListener ) ((QueryParserListener)listener).enterOperator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof QueryParserListener ) ((QueryParserListener)listener).exitOperator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QueryParserVisitor ) return ((QueryParserVisitor<? extends T>)visitor).visitOperator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OperatorContext operator() throws RecognitionException {
		OperatorContext _localctx = new OperatorContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_operator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(66);
			_la = _input.LA(1);
			if ( !(_la==AND || _la==OR) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IdContext extends ParserRuleContext {
		public Token theKind;
		public IntegerContext theLabel;
		public IntegerContext integer() {
			return getRuleContext(IntegerContext.class,0);
		}
		public TerminalNode LETTERS() { return getToken(QueryParser.LETTERS, 0); }
		public TerminalNode DASH() { return getToken(QueryParser.DASH, 0); }
		public IdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_id; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof QueryParserListener ) ((QueryParserListener)listener).enterId(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof QueryParserListener ) ((QueryParserListener)listener).exitId(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QueryParserVisitor ) return ((QueryParserVisitor<? extends T>)visitor).visitId(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdContext id() throws RecognitionException {
		IdContext _localctx = new IdContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_id);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(68); ((IdContext)_localctx).theKind = match(LETTERS);
			setState(69); match(DASH);
			setState(70); ((IdContext)_localctx).theLabel = integer();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IntegerContext extends ParserRuleContext {
		public DigitsContext digits() {
			return getRuleContext(DigitsContext.class,0);
		}
		public TerminalNode DASH() { return getToken(QueryParser.DASH, 0); }
		public IntegerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_integer; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof QueryParserListener ) ((QueryParserListener)listener).enterInteger(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof QueryParserListener ) ((QueryParserListener)listener).exitInteger(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QueryParserVisitor ) return ((QueryParserVisitor<? extends T>)visitor).visitInteger(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IntegerContext integer() throws RecognitionException {
		IntegerContext _localctx = new IntegerContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_integer);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(73);
			_la = _input.LA(1);
			if (_la==DASH) {
				{
				setState(72); match(DASH);
				}
			}

			setState(75); digits();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RealContext extends ParserRuleContext {
		public TerminalNode DOT() { return getToken(QueryParser.DOT, 0); }
		public List<DigitsContext> digits() {
			return getRuleContexts(DigitsContext.class);
		}
		public DigitsContext digits(int i) {
			return getRuleContext(DigitsContext.class,i);
		}
		public TerminalNode DASH() { return getToken(QueryParser.DASH, 0); }
		public RealContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_real; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof QueryParserListener ) ((QueryParserListener)listener).enterReal(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof QueryParserListener ) ((QueryParserListener)listener).exitReal(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QueryParserVisitor ) return ((QueryParserVisitor<? extends T>)visitor).visitReal(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RealContext real() throws RecognitionException {
		RealContext _localctx = new RealContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_real);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(78);
			_la = _input.LA(1);
			if (_la==DASH) {
				{
				setState(77); match(DASH);
				}
			}

			setState(80); digits();
			setState(81); match(DOT);
			setState(83);
			switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
			case 1:
				{
				setState(82); digits();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StringContext extends ParserRuleContext {
		public Token theString;
		public TerminalNode NONGREEDY() { return getToken(QueryParser.NONGREEDY, 0); }
		public List<TerminalNode> QUOTE() { return getTokens(QueryParser.QUOTE); }
		public TerminalNode QUOTE(int i) {
			return getToken(QueryParser.QUOTE, i);
		}
		public StringContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_string; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof QueryParserListener ) ((QueryParserListener)listener).enterString(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof QueryParserListener ) ((QueryParserListener)listener).exitString(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QueryParserVisitor ) return ((QueryParserVisitor<? extends T>)visitor).visitString(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StringContext string() throws RecognitionException {
		StringContext _localctx = new StringContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_string);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(85); match(QUOTE);
			setState(86); ((StringContext)_localctx).theString = match(NONGREEDY);
			setState(87); match(QUOTE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DigitsContext extends ParserRuleContext {
		public TerminalNode DIGIT(int i) {
			return getToken(QueryParser.DIGIT, i);
		}
		public List<TerminalNode> DIGIT() { return getTokens(QueryParser.DIGIT); }
		public DigitsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_digits; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof QueryParserListener ) ((QueryParserListener)listener).enterDigits(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof QueryParserListener ) ((QueryParserListener)listener).exitDigits(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QueryParserVisitor ) return ((QueryParserVisitor<? extends T>)visitor).visitDigits(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DigitsContext digits() throws RecognitionException {
		DigitsContext _localctx = new DigitsContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_digits);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(90); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(89); match(DIGIT);
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(92); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 0: return query_sempred((QueryContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean query_sempred(QueryContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0: return precpred(_ctx, 3);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\23a\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t\13\4"+
		"\f\t\f\3\2\3\2\3\2\3\2\3\2\3\2\3\2\5\2 \n\2\3\2\3\2\3\2\3\2\7\2&\n\2\f"+
		"\2\16\2)\13\2\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5\5\5\67\n"+
		"\5\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\5\6C\n\6\3\7\3\7\3\b\3\b\3"+
		"\b\3\b\3\t\5\tL\n\t\3\t\3\t\3\n\5\nQ\n\n\3\n\3\n\3\n\5\nV\n\n\3\13\3\13"+
		"\3\13\3\13\3\f\6\f]\n\f\r\f\16\f^\3\f\2\3\2\r\2\4\6\b\n\f\16\20\22\24"+
		"\26\2\3\3\2\b\te\2\37\3\2\2\2\4*\3\2\2\2\6.\3\2\2\2\b\66\3\2\2\2\nB\3"+
		"\2\2\2\fD\3\2\2\2\16F\3\2\2\2\20K\3\2\2\2\22P\3\2\2\2\24W\3\2\2\2\26\\"+
		"\3\2\2\2\30\31\b\2\1\2\31\32\7\f\2\2\32\33\5\2\2\2\33\34\7\r\2\2\34 \3"+
		"\2\2\2\35 \5\4\3\2\36 \5\6\4\2\37\30\3\2\2\2\37\35\3\2\2\2\37\36\3\2\2"+
		"\2 \'\3\2\2\2!\"\f\5\2\2\"#\5\f\7\2#$\5\2\2\6$&\3\2\2\2%!\3\2\2\2&)\3"+
		"\2\2\2\'%\3\2\2\2\'(\3\2\2\2(\3\3\2\2\2)\'\3\2\2\2*+\7\22\2\2+,\5\n\6"+
		"\2,-\5\b\5\2-\5\3\2\2\2./\5\16\b\2/\60\7\20\2\2\60\61\7\22\2\2\61\7\3"+
		"\2\2\2\62\67\5\24\13\2\63\67\5\22\n\2\64\67\5\20\t\2\65\67\5\16\b\2\66"+
		"\62\3\2\2\2\66\63\3\2\2\2\66\64\3\2\2\2\66\65\3\2\2\2\67\t\3\2\2\289\7"+
		"\16\2\29C\7\5\2\2:;\7\17\2\2;C\7\5\2\2<C\7\5\2\2=>\7\6\2\2>C\7\5\2\2?"+
		"C\7\n\2\2@C\7\16\2\2AC\7\17\2\2B8\3\2\2\2B:\3\2\2\2B<\3\2\2\2B=\3\2\2"+
		"\2B?\3\2\2\2B@\3\2\2\2BA\3\2\2\2C\13\3\2\2\2DE\t\2\2\2E\r\3\2\2\2FG\7"+
		"\22\2\2GH\7\7\2\2HI\5\20\t\2I\17\3\2\2\2JL\7\7\2\2KJ\3\2\2\2KL\3\2\2\2"+
		"LM\3\2\2\2MN\5\26\f\2N\21\3\2\2\2OQ\7\7\2\2PO\3\2\2\2PQ\3\2\2\2QR\3\2"+
		"\2\2RS\5\26\f\2SU\7\20\2\2TV\5\26\f\2UT\3\2\2\2UV\3\2\2\2V\23\3\2\2\2"+
		"WX\7\3\2\2XY\7\4\2\2YZ\7\3\2\2Z\25\3\2\2\2[]\7\13\2\2\\[\3\2\2\2]^\3\2"+
		"\2\2^\\\3\2\2\2^_\3\2\2\2_\27\3\2\2\2\n\37\'\66BKPU^";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}