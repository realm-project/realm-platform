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
		EQUAL=1, UNEQUAL=2, CONTAINS=3, GT=4, LT=5, GTE=6, LTE=7, MATCH=8, NOTMATCH=9, 
		AND=10, OR=11, TRUE=12, FALSE=13, DASH=14, POUND=15, DIGIT=16, OPENBRACKET=17, 
		CLOSEBRACKET=18, DOT=19, STRING=20, LETTER=21, LETTERS=22, WS=23;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"'\\u0000'", "'\\u0001'", "'\\u0002'", "'\\u0003'", "'\\u0004'", "'\\u0005'", 
		"'\\u0006'", "'\\u0007'", "'\b'", "'\t'", "'\n'", "'\\u000B'", "'\f'", 
		"'\r'", "'\\u000E'", "'\\u000F'", "'\\u0010'", "'\\u0011'", "'\\u0012'", 
		"'\\u0013'", "'\\u0014'", "'\\u0015'", "'\\u0016'", "'\\u0017'"
	};
	public static final String[] ruleNames = {
		"EQUAL", "UNEQUAL", "CONTAINS", "GT", "LT", "GTE", "LTE", "MATCH", "NOTMATCH", 
		"AND", "OR", "TRUE", "FALSE", "DASH", "POUND", "DIGIT", "OPENBRACKET", 
		"CLOSEBRACKET", "DOT", "STRING", "LETTER", "LETTERS", "WS"
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\31\u009a\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\3\2"+
		"\3\2\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\6\3\6\3"+
		"\7\3\7\3\7\3\b\3\b\3\b\3\t\3\t\3\t\3\n\3\n\3\n\3\13\3\13\3\13\3\13\3\f"+
		"\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\5\rc\n\r\3\16"+
		"\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16"+
		"\5\16t\n\16\3\17\3\17\3\20\3\20\3\21\3\21\3\22\3\22\3\23\3\23\3\24\3\24"+
		"\3\25\3\25\3\25\3\25\7\25\u0086\n\25\f\25\16\25\u0089\13\25\3\25\3\25"+
		"\3\26\3\26\3\27\6\27\u0090\n\27\r\27\16\27\u0091\3\30\6\30\u0095\n\30"+
		"\r\30\16\30\u0096\3\30\3\30\2\2\31\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n"+
		"\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30"+
		"/\31\3\2\6\3\2\62;\4\2$$^^\4\2C\\c|\5\2\13\f\17\17\"\"\u00a1\2\3\3\2\2"+
		"\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3"+
		"\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2"+
		"\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2"+
		"\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\3\61\3\2"+
		"\2\2\5\63\3\2\2\2\7\66\3\2\2\2\t?\3\2\2\2\13A\3\2\2\2\rC\3\2\2\2\17F\3"+
		"\2\2\2\21I\3\2\2\2\23L\3\2\2\2\25O\3\2\2\2\27S\3\2\2\2\31b\3\2\2\2\33"+
		"s\3\2\2\2\35u\3\2\2\2\37w\3\2\2\2!y\3\2\2\2#{\3\2\2\2%}\3\2\2\2\'\177"+
		"\3\2\2\2)\u0081\3\2\2\2+\u008c\3\2\2\2-\u008f\3\2\2\2/\u0094\3\2\2\2\61"+
		"\62\7?\2\2\62\4\3\2\2\2\63\64\7#\2\2\64\65\7?\2\2\65\6\3\2\2\2\66\67\7"+
		"e\2\2\678\7q\2\289\7p\2\29:\7v\2\2:;\7c\2\2;<\7k\2\2<=\7p\2\2=>\7u\2\2"+
		">\b\3\2\2\2?@\7@\2\2@\n\3\2\2\2AB\7>\2\2B\f\3\2\2\2CD\7@\2\2DE\7?\2\2"+
		"E\16\3\2\2\2FG\7>\2\2GH\7?\2\2H\20\3\2\2\2IJ\7?\2\2JK\7\u0080\2\2K\22"+
		"\3\2\2\2LM\7#\2\2MN\7\u0080\2\2N\24\3\2\2\2OP\7c\2\2PQ\7p\2\2QR\7f\2\2"+
		"R\26\3\2\2\2ST\7q\2\2TU\7t\2\2U\30\3\2\2\2VW\7v\2\2WX\7t\2\2XY\7w\2\2"+
		"Yc\7g\2\2Z[\7V\2\2[\\\7t\2\2\\]\7w\2\2]c\7g\2\2^_\7V\2\2_`\7T\2\2`a\7"+
		"W\2\2ac\7G\2\2bV\3\2\2\2bZ\3\2\2\2b^\3\2\2\2c\32\3\2\2\2de\7h\2\2ef\7"+
		"c\2\2fg\7n\2\2gh\7u\2\2ht\7g\2\2ij\7H\2\2jk\7c\2\2kl\7n\2\2lm\7u\2\2m"+
		"t\7g\2\2no\7H\2\2op\7C\2\2pq\7N\2\2qr\7U\2\2rt\7G\2\2sd\3\2\2\2si\3\2"+
		"\2\2sn\3\2\2\2t\34\3\2\2\2uv\7/\2\2v\36\3\2\2\2wx\7%\2\2x \3\2\2\2yz\t"+
		"\2\2\2z\"\3\2\2\2{|\7*\2\2|$\3\2\2\2}~\7+\2\2~&\3\2\2\2\177\u0080\7\60"+
		"\2\2\u0080(\3\2\2\2\u0081\u0087\7$\2\2\u0082\u0086\n\3\2\2\u0083\u0084"+
		"\7^\2\2\u0084\u0086\t\3\2\2\u0085\u0082\3\2\2\2\u0085\u0083\3\2\2\2\u0086"+
		"\u0089\3\2\2\2\u0087\u0085\3\2\2\2\u0087\u0088\3\2\2\2\u0088\u008a\3\2"+
		"\2\2\u0089\u0087\3\2\2\2\u008a\u008b\7$\2\2\u008b*\3\2\2\2\u008c\u008d"+
		"\t\4\2\2\u008d,\3\2\2\2\u008e\u0090\5+\26\2\u008f\u008e\3\2\2\2\u0090"+
		"\u0091\3\2\2\2\u0091\u008f\3\2\2\2\u0091\u0092\3\2\2\2\u0092.\3\2\2\2"+
		"\u0093\u0095\t\5\2\2\u0094\u0093\3\2\2\2\u0095\u0096\3\2\2\2\u0096\u0094"+
		"\3\2\2\2\u0096\u0097\3\2\2\2\u0097\u0098\3\2\2\2\u0098\u0099\b\30\2\2"+
		"\u0099\60\3\2\2\2\t\2bs\u0085\u0087\u0091\u0096\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}