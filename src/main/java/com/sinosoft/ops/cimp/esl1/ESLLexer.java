package com.sinosoft.ops.cimp.esl1;
// Generated from ESL.g4 by ANTLR 4.7.1
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.LexerATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class ESLLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.7.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, RelationOp=4, LPAREN=5, RPAREN=6, LBRACK=7, RBRACK=8, 
		LBRACE=9, RBRACE=10, COMMA=11, SEMICOLON=12, ADD=13, RANGEADD=14, SUB=15, 
		MUL=16, DIV=17, EQEQ=18, NE=19, LT=20, LE=21, GT=22, GE=23, BANG=24, AND=25, 
		OR=26, NOT=27, EQ=28, IF=29, THEN=30, ELSE=31, WHILE=32, BREAK=33, READ=34, 
		WRITE=35, INT=36, REAL=37, TO=38, Identifier=39, Integer=40, STRING=41, 
		RealNumber=42, WS=43, Comment=44, LineComment=45;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"T__0", "T__1", "T__2", "RelationOp", "LPAREN", "RPAREN", "LBRACK", "RBRACK", 
		"LBRACE", "RBRACE", "COMMA", "SEMICOLON", "ADD", "RANGEADD", "SUB", "MUL", 
		"DIV", "EQEQ", "NE", "LT", "LE", "GT", "GE", "BANG", "AND", "OR", "NOT", 
		"EQ", "IF", "THEN", "ELSE", "WHILE", "BREAK", "READ", "WRITE", "INT", 
		"REAL", "TO", "Identifier", "Integer", "STRING", "RealNumber", "Digit", 
		"LetterOrUnderscore", "Letter", "FirstLetter", "WS", "Comment", "LineComment"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'T'", "'R'", "'C'", null, "'('", "')'", "'['", "']'", "'{'", "'}'", 
		"','", "';'", "'+'", "'+..+'", "'-'", "'*'", "'/'", "'=='", "'!='", "'<'", 
		"'<='", "'>'", "'>='", "'!'", "'&&'", "'||'", "'NOT'", "'='", "'if'", 
		"'then'", "'else'", "'while'", "'break'", "'read'", "'write'", "'int'", 
		"'real'", "'..'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, "RelationOp", "LPAREN", "RPAREN", "LBRACK", "RBRACK", 
		"LBRACE", "RBRACE", "COMMA", "SEMICOLON", "ADD", "RANGEADD", "SUB", "MUL", 
		"DIV", "EQEQ", "NE", "LT", "LE", "GT", "GE", "BANG", "AND", "OR", "NOT", 
		"EQ", "IF", "THEN", "ELSE", "WHILE", "BREAK", "READ", "WRITE", "INT", 
		"REAL", "TO", "Identifier", "Integer", "STRING", "RealNumber", "WS", "Comment", 
		"LineComment"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public ESLLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "ESL.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2/\u012e\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\3\2\3\2\3\3\3\3\3"+
		"\4\3\4\3\5\3\5\3\5\3\5\3\5\3\5\5\5r\n\5\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3"+
		"\t\3\n\3\n\3\13\3\13\3\f\3\f\3\r\3\r\3\16\3\16\3\17\3\17\3\17\3\17\3\17"+
		"\3\20\3\20\3\21\3\21\3\22\3\22\3\23\3\23\3\23\3\24\3\24\3\24\3\25\3\25"+
		"\3\26\3\26\3\26\3\27\3\27\3\30\3\30\3\30\3\31\3\31\3\32\3\32\3\32\3\33"+
		"\3\33\3\33\3\34\3\34\3\34\3\34\3\35\3\35\3\36\3\36\3\36\3\37\3\37\3\37"+
		"\3\37\3\37\3 \3 \3 \3 \3 \3!\3!\3!\3!\3!\3!\3\"\3\"\3\"\3\"\3\"\3\"\3"+
		"#\3#\3#\3#\3#\3$\3$\3$\3$\3$\3$\3%\3%\3%\3%\3&\3&\3&\3&\3&\3\'\3\'\3\'"+
		"\3(\3(\3(\7(\u00e2\n(\f(\16(\u00e5\13(\3)\6)\u00e8\n)\r)\16)\u00e9\3*"+
		"\3*\7*\u00ee\n*\f*\16*\u00f1\13*\3*\3*\3+\6+\u00f6\n+\r+\16+\u00f7\3+"+
		"\3+\6+\u00fc\n+\r+\16+\u00fd\3,\3,\3-\3-\5-\u0104\n-\3.\3.\3/\3/\3\60"+
		"\6\60\u010b\n\60\r\60\16\60\u010c\3\60\3\60\3\61\3\61\3\61\3\61\7\61\u0115"+
		"\n\61\f\61\16\61\u0118\13\61\3\61\3\61\3\61\3\61\3\61\3\62\3\62\3\62\3"+
		"\62\7\62\u0123\n\62\f\62\16\62\u0126\13\62\3\62\5\62\u0129\n\62\3\62\3"+
		"\62\3\62\3\62\3\u0116\2\63\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25"+
		"\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32"+
		"\63\33\65\34\67\359\36;\37= ?!A\"C#E$G%I&K\'M(O)Q*S+U,W\2Y\2[\2]\2_-a"+
		".c/\3\2\7\6\2\f\f\17\17$$^^\4\2C\\c|\b\2CDFSUUW\\aac|\5\2\13\f\17\17\""+
		"\"\4\2\f\f\17\17\2\u0139\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2"+
		"\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25"+
		"\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2"+
		"\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2"+
		"\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3"+
		"\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2"+
		"\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M\3\2\2\2\2O\3\2\2\2\2"+
		"Q\3\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2_\3\2\2\2\2a\3\2\2\2\2c\3\2\2\2\3e\3"+
		"\2\2\2\5g\3\2\2\2\7i\3\2\2\2\tq\3\2\2\2\13s\3\2\2\2\ru\3\2\2\2\17w\3\2"+
		"\2\2\21y\3\2\2\2\23{\3\2\2\2\25}\3\2\2\2\27\177\3\2\2\2\31\u0081\3\2\2"+
		"\2\33\u0083\3\2\2\2\35\u0085\3\2\2\2\37\u008a\3\2\2\2!\u008c\3\2\2\2#"+
		"\u008e\3\2\2\2%\u0090\3\2\2\2\'\u0093\3\2\2\2)\u0096\3\2\2\2+\u0098\3"+
		"\2\2\2-\u009b\3\2\2\2/\u009d\3\2\2\2\61\u00a0\3\2\2\2\63\u00a2\3\2\2\2"+
		"\65\u00a5\3\2\2\2\67\u00a8\3\2\2\29\u00ac\3\2\2\2;\u00ae\3\2\2\2=\u00b1"+
		"\3\2\2\2?\u00b6\3\2\2\2A\u00bb\3\2\2\2C\u00c1\3\2\2\2E\u00c7\3\2\2\2G"+
		"\u00cc\3\2\2\2I\u00d2\3\2\2\2K\u00d6\3\2\2\2M\u00db\3\2\2\2O\u00de\3\2"+
		"\2\2Q\u00e7\3\2\2\2S\u00eb\3\2\2\2U\u00f5\3\2\2\2W\u00ff\3\2\2\2Y\u0103"+
		"\3\2\2\2[\u0105\3\2\2\2]\u0107\3\2\2\2_\u010a\3\2\2\2a\u0110\3\2\2\2c"+
		"\u011e\3\2\2\2ef\7V\2\2f\4\3\2\2\2gh\7T\2\2h\6\3\2\2\2ij\7E\2\2j\b\3\2"+
		"\2\2kr\5%\23\2lr\5\'\24\2mr\5/\30\2nr\5+\26\2or\5-\27\2pr\5)\25\2qk\3"+
		"\2\2\2ql\3\2\2\2qm\3\2\2\2qn\3\2\2\2qo\3\2\2\2qp\3\2\2\2r\n\3\2\2\2st"+
		"\7*\2\2t\f\3\2\2\2uv\7+\2\2v\16\3\2\2\2wx\7]\2\2x\20\3\2\2\2yz\7_\2\2"+
		"z\22\3\2\2\2{|\7}\2\2|\24\3\2\2\2}~\7\177\2\2~\26\3\2\2\2\177\u0080\7"+
		".\2\2\u0080\30\3\2\2\2\u0081\u0082\7=\2\2\u0082\32\3\2\2\2\u0083\u0084"+
		"\7-\2\2\u0084\34\3\2\2\2\u0085\u0086\7-\2\2\u0086\u0087\7\60\2\2\u0087"+
		"\u0088\7\60\2\2\u0088\u0089\7-\2\2\u0089\36\3\2\2\2\u008a\u008b\7/\2\2"+
		"\u008b \3\2\2\2\u008c\u008d\7,\2\2\u008d\"\3\2\2\2\u008e\u008f\7\61\2"+
		"\2\u008f$\3\2\2\2\u0090\u0091\7?\2\2\u0091\u0092\7?\2\2\u0092&\3\2\2\2"+
		"\u0093\u0094\7#\2\2\u0094\u0095\7?\2\2\u0095(\3\2\2\2\u0096\u0097\7>\2"+
		"\2\u0097*\3\2\2\2\u0098\u0099\7>\2\2\u0099\u009a\7?\2\2\u009a,\3\2\2\2"+
		"\u009b\u009c\7@\2\2\u009c.\3\2\2\2\u009d\u009e\7@\2\2\u009e\u009f\7?\2"+
		"\2\u009f\60\3\2\2\2\u00a0\u00a1\7#\2\2\u00a1\62\3\2\2\2\u00a2\u00a3\7"+
		"(\2\2\u00a3\u00a4\7(\2\2\u00a4\64\3\2\2\2\u00a5\u00a6\7~\2\2\u00a6\u00a7"+
		"\7~\2\2\u00a7\66\3\2\2\2\u00a8\u00a9\7P\2\2\u00a9\u00aa\7Q\2\2\u00aa\u00ab"+
		"\7V\2\2\u00ab8\3\2\2\2\u00ac\u00ad\7?\2\2\u00ad:\3\2\2\2\u00ae\u00af\7"+
		"k\2\2\u00af\u00b0\7h\2\2\u00b0<\3\2\2\2\u00b1\u00b2\7v\2\2\u00b2\u00b3"+
		"\7j\2\2\u00b3\u00b4\7g\2\2\u00b4\u00b5\7p\2\2\u00b5>\3\2\2\2\u00b6\u00b7"+
		"\7g\2\2\u00b7\u00b8\7n\2\2\u00b8\u00b9\7u\2\2\u00b9\u00ba\7g\2\2\u00ba"+
		"@\3\2\2\2\u00bb\u00bc\7y\2\2\u00bc\u00bd\7j\2\2\u00bd\u00be\7k\2\2\u00be"+
		"\u00bf\7n\2\2\u00bf\u00c0\7g\2\2\u00c0B\3\2\2\2\u00c1\u00c2\7d\2\2\u00c2"+
		"\u00c3\7t\2\2\u00c3\u00c4\7g\2\2\u00c4\u00c5\7c\2\2\u00c5\u00c6\7m\2\2"+
		"\u00c6D\3\2\2\2\u00c7\u00c8\7t\2\2\u00c8\u00c9\7g\2\2\u00c9\u00ca\7c\2"+
		"\2\u00ca\u00cb\7f\2\2\u00cbF\3\2\2\2\u00cc\u00cd\7y\2\2\u00cd\u00ce\7"+
		"t\2\2\u00ce\u00cf\7k\2\2\u00cf\u00d0\7v\2\2\u00d0\u00d1\7g\2\2\u00d1H"+
		"\3\2\2\2\u00d2\u00d3\7k\2\2\u00d3\u00d4\7p\2\2\u00d4\u00d5\7v\2\2\u00d5"+
		"J\3\2\2\2\u00d6\u00d7\7t\2\2\u00d7\u00d8\7g\2\2\u00d8\u00d9\7c\2\2\u00d9"+
		"\u00da\7n\2\2\u00daL\3\2\2\2\u00db\u00dc\7\60\2\2\u00dc\u00dd\7\60\2\2"+
		"\u00ddN\3\2\2\2\u00de\u00e3\5]/\2\u00df\u00e2\5Y-\2\u00e0\u00e2\5W,\2"+
		"\u00e1\u00df\3\2\2\2\u00e1\u00e0\3\2\2\2\u00e2\u00e5\3\2\2\2\u00e3\u00e1"+
		"\3\2\2\2\u00e3\u00e4\3\2\2\2\u00e4P\3\2\2\2\u00e5\u00e3\3\2\2\2\u00e6"+
		"\u00e8\5W,\2\u00e7\u00e6\3\2\2\2\u00e8\u00e9\3\2\2\2\u00e9\u00e7\3\2\2"+
		"\2\u00e9\u00ea\3\2\2\2\u00eaR\3\2\2\2\u00eb\u00ef\7$\2\2\u00ec\u00ee\n"+
		"\2\2\2\u00ed\u00ec\3\2\2\2\u00ee\u00f1\3\2\2\2\u00ef\u00ed\3\2\2\2\u00ef"+
		"\u00f0\3\2\2\2\u00f0\u00f2\3\2\2\2\u00f1\u00ef\3\2\2\2\u00f2\u00f3\7$"+
		"\2\2\u00f3T\3\2\2\2\u00f4\u00f6\5W,\2\u00f5\u00f4\3\2\2\2\u00f6\u00f7"+
		"\3\2\2\2\u00f7\u00f5\3\2\2\2\u00f7\u00f8\3\2\2\2\u00f8\u00f9\3\2\2\2\u00f9"+
		"\u00fb\7\60\2\2\u00fa\u00fc\5W,\2\u00fb\u00fa\3\2\2\2\u00fc\u00fd\3\2"+
		"\2\2\u00fd\u00fb\3\2\2\2\u00fd\u00fe\3\2\2\2\u00feV\3\2\2\2\u00ff\u0100"+
		"\4\62;\2\u0100X\3\2\2\2\u0101\u0104\5[.\2\u0102\u0104\7a\2\2\u0103\u0101"+
		"\3\2\2\2\u0103\u0102\3\2\2\2\u0104Z\3\2\2\2\u0105\u0106\t\3\2\2\u0106"+
		"\\\3\2\2\2\u0107\u0108\t\4\2\2\u0108^\3\2\2\2\u0109\u010b\t\5\2\2\u010a"+
		"\u0109\3\2\2\2\u010b\u010c\3\2\2\2\u010c\u010a\3\2\2\2\u010c\u010d\3\2"+
		"\2\2\u010d\u010e\3\2\2\2\u010e\u010f\b\60\2\2\u010f`\3\2\2\2\u0110\u0111"+
		"\7\61\2\2\u0111\u0112\7,\2\2\u0112\u0116\3\2\2\2\u0113\u0115\13\2\2\2"+
		"\u0114\u0113\3\2\2\2\u0115\u0118\3\2\2\2\u0116\u0117\3\2\2\2\u0116\u0114"+
		"\3\2\2\2\u0117\u0119\3\2\2\2\u0118\u0116\3\2\2\2\u0119\u011a\7,\2\2\u011a"+
		"\u011b\7\61\2\2\u011b\u011c\3\2\2\2\u011c\u011d\b\61\2\2\u011db\3\2\2"+
		"\2\u011e\u011f\7\61\2\2\u011f\u0120\7\61\2\2\u0120\u0124\3\2\2\2\u0121"+
		"\u0123\n\6\2\2\u0122\u0121\3\2\2\2\u0123\u0126\3\2\2\2\u0124\u0122\3\2"+
		"\2\2\u0124\u0125\3\2\2\2\u0125\u0128\3\2\2\2\u0126\u0124\3\2\2\2\u0127"+
		"\u0129\7\17\2\2\u0128\u0127\3\2\2\2\u0128\u0129\3\2\2\2\u0129\u012a\3"+
		"\2\2\2\u012a\u012b\7\f\2\2\u012b\u012c\3\2\2\2\u012c\u012d\b\62\2\2\u012d"+
		"d\3\2\2\2\17\2q\u00e1\u00e3\u00e9\u00ef\u00f7\u00fd\u0103\u010c\u0116"+
		"\u0124\u0128\3\2\3\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}