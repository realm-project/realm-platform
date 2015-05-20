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
		POUND=15, LETTER=21, OR=11, UNEQUAL=2, EQUAL=1, LT=5, NOTMATCH=9, DOT=19, 
		TRUE=12, CONTAINS=3, GT=4, DASH=14, DIGIT=16, OPENBRACKET=17, LETTERS=22, 
		AND=10, GTE=6, STRING=20, FALSE=13, CLOSEBRACKET=18, LTE=7, WS=23, MATCH=8;
	public static final String[] tokenNames = {
		"<INVALID>", "'='", "'!='", "'contains'", "'>'", "'<'", "'>='", "'<='", 
		"'=~'", "'!~'", "'and'", "'or'", "TRUE", "FALSE", "'-'", "'#'", "DIGIT", 
		"'('", "')'", "'.'", "STRING", "LETTER", "LETTERS", "WS"
	};
	public static final int
		RULE_query = 0, RULE_relationship = 1, RULE_retrieval = 2, RULE_value = 3, 
		RULE_relation = 4, RULE_operator = 5, RULE_id = 6, RULE_integer = 7, RULE_real = 8, 
		RULE_bool = 9, RULE_string = 10, RULE_digits = 11;
	public static final String[] ruleNames = {
		"query", "relationship", "retrieval", "value", "relation", "operator", 
		"id", "integer", "real", "bool", "string", "digits"
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
			setState(31);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				{
				_localctx = new BracketedQueryContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(25); match(OPENBRACKET);
				setState(26); ((BracketedQueryContext)_localctx).theQuery = query(0);
				setState(27); match(CLOSEBRACKET);
				}
				break;

			case 2:
				{
				_localctx = new SimpleQueryContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(29); relationship();
				}
				break;

			case 3:
				{
				_localctx = new RetrievalQueryContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(30); retrieval();
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(39);
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
					setState(33);
					if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
					setState(34); ((CompositeQueryContext)_localctx).theOperator = operator();
					setState(35); ((CompositeQueryContext)_localctx).rightQuery = query(4);
					}
					} 
				}
				setState(41);
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
			setState(42); ((RelationshipContext)_localctx).theField = match(LETTERS);
			setState(43); ((RelationshipContext)_localctx).theRelation = relation();
			setState(44); ((RelationshipContext)_localctx).theValue = value();
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
			setState(46); ((RetrievalContext)_localctx).theId = id();
			setState(47); match(DOT);
			setState(48); ((RetrievalContext)_localctx).theField = match(LETTERS);
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
		public BoolContext bool() {
			return getRuleContext(BoolContext.class,0);
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
			setState(55);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(50); string();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(51); real();
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(52); integer();
				}
				break;

			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(53); id();
				}
				break;

			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(54); bool();
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
		public TerminalNode MATCH() { return getToken(QueryParser.MATCH, 0); }
		public TerminalNode GTE() { return getToken(QueryParser.GTE, 0); }
		public TerminalNode LT() { return getToken(QueryParser.LT, 0); }
		public TerminalNode GT() { return getToken(QueryParser.GT, 0); }
		public TerminalNode LTE() { return getToken(QueryParser.LTE, 0); }
		public TerminalNode NOTMATCH() { return getToken(QueryParser.NOTMATCH, 0); }
		public TerminalNode UNEQUAL() { return getToken(QueryParser.UNEQUAL, 0); }
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
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(57);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << EQUAL) | (1L << UNEQUAL) | (1L << CONTAINS) | (1L << GT) | (1L << LT) | (1L << GTE) | (1L << LTE) | (1L << MATCH) | (1L << NOTMATCH))) != 0)) ) {
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
			setState(59);
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
		public TerminalNode POUND() { return getToken(QueryParser.POUND, 0); }
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
			setState(67);
			switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(61); ((IdContext)_localctx).theKind = match(LETTERS);
				setState(62); match(DASH);
				setState(63); ((IdContext)_localctx).theLabel = integer();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(64); ((IdContext)_localctx).theKind = match(LETTERS);
				setState(65); match(POUND);
				setState(66); ((IdContext)_localctx).theLabel = integer();
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
			setState(70);
			_la = _input.LA(1);
			if (_la==DASH) {
				{
				setState(69); match(DASH);
				}
			}

			setState(72); digits();
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
			setState(75);
			_la = _input.LA(1);
			if (_la==DASH) {
				{
				setState(74); match(DASH);
				}
			}

			setState(77); digits();
			setState(78); match(DOT);
			setState(80);
			switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
			case 1:
				{
				setState(79); digits();
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

	public static class BoolContext extends ParserRuleContext {
		public Token theValue;
		public TerminalNode FALSE() { return getToken(QueryParser.FALSE, 0); }
		public TerminalNode TRUE() { return getToken(QueryParser.TRUE, 0); }
		public BoolContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_bool; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof QueryParserListener ) ((QueryParserListener)listener).enterBool(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof QueryParserListener ) ((QueryParserListener)listener).exitBool(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QueryParserVisitor ) return ((QueryParserVisitor<? extends T>)visitor).visitBool(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BoolContext bool() throws RecognitionException {
		BoolContext _localctx = new BoolContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_bool);
		try {
			setState(84);
			switch (_input.LA(1)) {
			case TRUE:
				enterOuterAlt(_localctx, 1);
				{
				setState(82); ((BoolContext)_localctx).theValue = match(TRUE);
				}
				break;
			case FALSE:
				enterOuterAlt(_localctx, 2);
				{
				setState(83); ((BoolContext)_localctx).theValue = match(FALSE);
				}
				break;
			default:
				throw new NoViableAltException(this);
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
		public Token theValue;
		public TerminalNode STRING() { return getToken(QueryParser.STRING, 0); }
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
		enterRule(_localctx, 20, RULE_string);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(86); ((StringContext)_localctx).theValue = match(STRING);
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
		enterRule(_localctx, 22, RULE_digits);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(89); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(88); match(DIGIT);
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(91); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\31`\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t\13\4"+
		"\f\t\f\4\r\t\r\3\2\3\2\3\2\3\2\3\2\3\2\3\2\5\2\"\n\2\3\2\3\2\3\2\3\2\7"+
		"\2(\n\2\f\2\16\2+\13\2\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5"+
		"\3\5\5\5:\n\5\3\6\3\6\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\b\5\bF\n\b\3\t\5\t"+
		"I\n\t\3\t\3\t\3\n\5\nN\n\n\3\n\3\n\3\n\5\nS\n\n\3\13\3\13\5\13W\n\13\3"+
		"\f\3\f\3\r\6\r\\\n\r\r\r\16\r]\3\r\2\3\2\16\2\4\6\b\n\f\16\20\22\24\26"+
		"\30\2\4\3\2\3\13\3\2\f\r`\2!\3\2\2\2\4,\3\2\2\2\6\60\3\2\2\2\b9\3\2\2"+
		"\2\n;\3\2\2\2\f=\3\2\2\2\16E\3\2\2\2\20H\3\2\2\2\22M\3\2\2\2\24V\3\2\2"+
		"\2\26X\3\2\2\2\30[\3\2\2\2\32\33\b\2\1\2\33\34\7\23\2\2\34\35\5\2\2\2"+
		"\35\36\7\24\2\2\36\"\3\2\2\2\37\"\5\4\3\2 \"\5\6\4\2!\32\3\2\2\2!\37\3"+
		"\2\2\2! \3\2\2\2\")\3\2\2\2#$\f\5\2\2$%\5\f\7\2%&\5\2\2\6&(\3\2\2\2\'"+
		"#\3\2\2\2(+\3\2\2\2)\'\3\2\2\2)*\3\2\2\2*\3\3\2\2\2+)\3\2\2\2,-\7\30\2"+
		"\2-.\5\n\6\2./\5\b\5\2/\5\3\2\2\2\60\61\5\16\b\2\61\62\7\25\2\2\62\63"+
		"\7\30\2\2\63\7\3\2\2\2\64:\5\26\f\2\65:\5\22\n\2\66:\5\20\t\2\67:\5\16"+
		"\b\28:\5\24\13\29\64\3\2\2\29\65\3\2\2\29\66\3\2\2\29\67\3\2\2\298\3\2"+
		"\2\2:\t\3\2\2\2;<\t\2\2\2<\13\3\2\2\2=>\t\3\2\2>\r\3\2\2\2?@\7\30\2\2"+
		"@A\7\20\2\2AF\5\20\t\2BC\7\30\2\2CD\7\21\2\2DF\5\20\t\2E?\3\2\2\2EB\3"+
		"\2\2\2F\17\3\2\2\2GI\7\20\2\2HG\3\2\2\2HI\3\2\2\2IJ\3\2\2\2JK\5\30\r\2"+
		"K\21\3\2\2\2LN\7\20\2\2ML\3\2\2\2MN\3\2\2\2NO\3\2\2\2OP\5\30\r\2PR\7\25"+
		"\2\2QS\5\30\r\2RQ\3\2\2\2RS\3\2\2\2S\23\3\2\2\2TW\7\16\2\2UW\7\17\2\2"+
		"VT\3\2\2\2VU\3\2\2\2W\25\3\2\2\2XY\7\26\2\2Y\27\3\2\2\2Z\\\7\22\2\2[Z"+
		"\3\2\2\2\\]\3\2\2\2][\3\2\2\2]^\3\2\2\2^\31\3\2\2\2\13!)9EHMRV]";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}