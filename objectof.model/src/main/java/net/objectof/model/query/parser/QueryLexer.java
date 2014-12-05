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
		QUOTE=1, NONGREEDY=2, EQUAL=3, BANG=4, DASH=5, AND=6, OR=7, CONTAINS=8, 
		DIGIT=9, OPENBRACKET=10, CLOSEBRACKET=11, GT=12, LT=13, DOT=14, LETTER=15, 
		LETTERS=16, WS=17;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"'\\u0000'", "'\\u0001'", "'\\u0002'", "'\\u0003'", "'\\u0004'", "'\\u0005'", 
		"'\\u0006'", "'\\u0007'", "'\b'", "'\t'", "'\n'", "'\\u000B'", "'\f'", 
		"'\r'", "'\\u000E'", "'\\u000F'", "'\\u0010'", "'\\u0011'"
	};
	public static final String[] ruleNames = {
		"QUOTE", "NONGREEDY", "EQUAL", "BANG", "DASH", "AND", "OR", "CONTAINS", 
		"DIGIT", "OPENBRACKET", "CLOSEBRACKET", "GT", "LT", "DOT", "LETTER", "LETTERS", 
		"WS"
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\23]\b\1\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\3\2\3\2\3\3\7\3)\n\3\f\3\16\3,\13\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3"+
		"\7\3\7\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\13\3"+
		"\13\3\f\3\f\3\r\3\r\3\16\3\16\3\17\3\17\3\20\3\20\3\21\6\21S\n\21\r\21"+
		"\16\21T\3\22\6\22X\n\22\r\22\16\22Y\3\22\3\22\3*\2\23\3\3\5\4\7\5\t\6"+
		"\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23\3"+
		"\2\5\3\2\62;\4\2C\\c|\5\2\13\f\17\17\"\"_\2\3\3\2\2\2\2\5\3\2\2\2\2\7"+
		"\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2"+
		"\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2"+
		"\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\3%\3\2\2\2\5*\3\2\2\2\7"+
		"-\3\2\2\2\t/\3\2\2\2\13\61\3\2\2\2\r\63\3\2\2\2\17\67\3\2\2\2\21:\3\2"+
		"\2\2\23C\3\2\2\2\25E\3\2\2\2\27G\3\2\2\2\31I\3\2\2\2\33K\3\2\2\2\35M\3"+
		"\2\2\2\37O\3\2\2\2!R\3\2\2\2#W\3\2\2\2%&\7$\2\2&\4\3\2\2\2\')\13\2\2\2"+
		"(\'\3\2\2\2),\3\2\2\2*+\3\2\2\2*(\3\2\2\2+\6\3\2\2\2,*\3\2\2\2-.\7?\2"+
		"\2.\b\3\2\2\2/\60\7#\2\2\60\n\3\2\2\2\61\62\7/\2\2\62\f\3\2\2\2\63\64"+
		"\7c\2\2\64\65\7p\2\2\65\66\7f\2\2\66\16\3\2\2\2\678\7q\2\289\7t\2\29\20"+
		"\3\2\2\2:;\7e\2\2;<\7q\2\2<=\7p\2\2=>\7v\2\2>?\7c\2\2?@\7k\2\2@A\7p\2"+
		"\2AB\7u\2\2B\22\3\2\2\2CD\t\2\2\2D\24\3\2\2\2EF\7*\2\2F\26\3\2\2\2GH\7"+
		"+\2\2H\30\3\2\2\2IJ\7@\2\2J\32\3\2\2\2KL\7>\2\2L\34\3\2\2\2MN\7\60\2\2"+
		"N\36\3\2\2\2OP\t\3\2\2P \3\2\2\2QS\5\37\20\2RQ\3\2\2\2ST\3\2\2\2TR\3\2"+
		"\2\2TU\3\2\2\2U\"\3\2\2\2VX\t\4\2\2WV\3\2\2\2XY\3\2\2\2YW\3\2\2\2YZ\3"+
		"\2\2\2Z[\3\2\2\2[\\\b\22\2\2\\$\3\2\2\2\6\2*TY\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}