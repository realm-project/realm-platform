// Generated from QueryLexer.g4 by ANTLR 4.3
package net.objectof.model.query.parser;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class QueryLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		EQUAL=1, BANG=2, DASH=3, AND=4, OR=5, CONTAINS=6, DIGIT=7, OPENBRACKET=8, 
		CLOSEBRACKET=9, GT=10, LT=11, DOT=12, POUND=13, TRUE=14, FALSE=15, STRING=16, 
		LETTER=17, LETTERS=18, WS=19;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"'\\u0000'", "'\\u0001'", "'\\u0002'", "'\\u0003'", "'\\u0004'", "'\\u0005'", 
		"'\\u0006'", "'\\u0007'", "'\b'", "'\t'", "'\n'", "'\\u000B'", "'\f'", 
		"'\r'", "'\\u000E'", "'\\u000F'", "'\\u0010'", "'\\u0011'", "'\\u0012'", 
		"'\\u0013'"
	};
	public static final String[] ruleNames = {
		"EQUAL", "BANG", "DASH", "AND", "OR", "CONTAINS", "DIGIT", "OPENBRACKET", 
		"CLOSEBRACKET", "GT", "LT", "DOT", "POUND", "TRUE", "FALSE", "STRING", 
		"LETTER", "LETTERS", "WS"
	};


	public QueryLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "QueryLexer.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\25\u0085\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\5\3\5\3\6"+
		"\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3"+
		"\13\3\13\3\f\3\f\3\r\3\r\3\16\3\16\3\17\3\17\3\17\3\17\3\17\3\17\3\17"+
		"\3\17\3\17\3\17\3\17\3\17\5\17Z\n\17\3\20\3\20\3\20\3\20\3\20\3\20\3\20"+
		"\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\5\20k\n\20\3\21\3\21\3\21\3\21"+
		"\7\21q\n\21\f\21\16\21t\13\21\3\21\3\21\3\22\3\22\3\23\6\23{\n\23\r\23"+
		"\16\23|\3\24\6\24\u0080\n\24\r\24\16\24\u0081\3\24\3\24\2\2\25\3\3\5\4"+
		"\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22"+
		"#\23%\24\'\25\3\2\6\3\2\62;\4\2$$^^\4\2C\\c|\5\2\13\f\17\17\"\"\u008c"+
		"\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2"+
		"\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2"+
		"\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2"+
		"\2\2\2%\3\2\2\2\2\'\3\2\2\2\3)\3\2\2\2\5+\3\2\2\2\7-\3\2\2\2\t/\3\2\2"+
		"\2\13\63\3\2\2\2\r\66\3\2\2\2\17?\3\2\2\2\21A\3\2\2\2\23C\3\2\2\2\25E"+
		"\3\2\2\2\27G\3\2\2\2\31I\3\2\2\2\33K\3\2\2\2\35Y\3\2\2\2\37j\3\2\2\2!"+
		"l\3\2\2\2#w\3\2\2\2%z\3\2\2\2\'\177\3\2\2\2)*\7?\2\2*\4\3\2\2\2+,\7#\2"+
		"\2,\6\3\2\2\2-.\7/\2\2.\b\3\2\2\2/\60\7c\2\2\60\61\7p\2\2\61\62\7f\2\2"+
		"\62\n\3\2\2\2\63\64\7q\2\2\64\65\7t\2\2\65\f\3\2\2\2\66\67\7e\2\2\678"+
		"\7q\2\289\7p\2\29:\7v\2\2:;\7c\2\2;<\7k\2\2<=\7p\2\2=>\7u\2\2>\16\3\2"+
		"\2\2?@\t\2\2\2@\20\3\2\2\2AB\7*\2\2B\22\3\2\2\2CD\7+\2\2D\24\3\2\2\2E"+
		"F\7@\2\2F\26\3\2\2\2GH\7>\2\2H\30\3\2\2\2IJ\7\60\2\2J\32\3\2\2\2KL\7%"+
		"\2\2L\34\3\2\2\2MN\7v\2\2NO\7t\2\2OP\7w\2\2PZ\7g\2\2QR\7V\2\2RS\7t\2\2"+
		"ST\7w\2\2TZ\7g\2\2UV\7V\2\2VW\7T\2\2WX\7W\2\2XZ\7G\2\2YM\3\2\2\2YQ\3\2"+
		"\2\2YU\3\2\2\2Z\36\3\2\2\2[\\\7h\2\2\\]\7c\2\2]^\7n\2\2^_\7u\2\2_k\7g"+
		"\2\2`a\7H\2\2ab\7c\2\2bc\7n\2\2cd\7u\2\2dk\7g\2\2ef\7H\2\2fg\7C\2\2gh"+
		"\7N\2\2hi\7U\2\2ik\7G\2\2j[\3\2\2\2j`\3\2\2\2je\3\2\2\2k \3\2\2\2lr\7"+
		"$\2\2mq\n\3\2\2no\7^\2\2oq\t\3\2\2pm\3\2\2\2pn\3\2\2\2qt\3\2\2\2rp\3\2"+
		"\2\2rs\3\2\2\2su\3\2\2\2tr\3\2\2\2uv\7$\2\2v\"\3\2\2\2wx\t\4\2\2x$\3\2"+
		"\2\2y{\5#\22\2zy\3\2\2\2{|\3\2\2\2|z\3\2\2\2|}\3\2\2\2}&\3\2\2\2~\u0080"+
		"\t\5\2\2\177~\3\2\2\2\u0080\u0081\3\2\2\2\u0081\177\3\2\2\2\u0081\u0082"+
		"\3\2\2\2\u0082\u0083\3\2\2\2\u0083\u0084\b\24\2\2\u0084(\3\2\2\2\t\2Y"+
		"jpr|\u0081\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}