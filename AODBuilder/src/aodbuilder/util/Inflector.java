package aodbuilder.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Transforms words (from singular to plural, from camelCase to under_score, etc.). I got bored of doing Real Work...
 * 
 * @author chuyeow
 */
public class Inflector {

    // Pfft, can't think of a better name, but this is needed to avoid the price of initializing the pattern on each call.
    private static final Pattern UNDERSCORE_PATTERN_1 = Pattern.compile("([A-Z]+)([A-Z][a-z])");
    private static final Pattern UNDERSCORE_PATTERN_2 = Pattern.compile("([a-z\\d])([A-Z])");

    private static List<RuleAndReplacement> plurals = new ArrayList<RuleAndReplacement>();
    private static List<RuleAndReplacement> singulars = new ArrayList<RuleAndReplacement>();
    private static List<RuleAndReplacement> presents = new ArrayList<RuleAndReplacement>();
    private static List<String> uncountables = new ArrayList<String>();

    private static Inflector instance; // (Pseudo-)Singleton instance.

    private Inflector() {
        // Woo, you can't touch me.
        
        initialize();
    }
    
    private void initialize() {
        plural("$", "s");
        plural("s$", "s");
        plural("(ax|test)is$", "$1es");
        plural("(octop|vir)us$", "$1i");
        plural("(alias|status)$", "$1es");
        plural("(bu)s$", "$1es");
        plural("(buffal|tomat)o$", "$1oes");
        plural("([ti])um$", "$1a");
        plural("sis$", "ses");
        plural("(?:([^f])fe|([lr])f)$", "$1$2ves");
        plural("(hive)$", "$1s");
        plural("([^aeiouy]|qu)y$", "$1ies");
        plural("([^aeiouy]|qu)ies$", "$1y");
        plural("(x|ch|ss|sh)$", "$1es");
        plural("(matr|vert|ind)ix|ex$", "$1ices");
        plural("([m|l])ouse$", "$1ice");
        plural("(ox)$", "$1en");
        plural("(quiz)$", "$1zes");

        singular("s$", "");
        singular("(n)ews$", "$1ews");
        singular("([ti])a$", "$1um");
        singular("((a)naly|(b)a|(d)iagno|(p)arenthe|(p)rogno|(s)ynop|(t)he)ses$", "$1$2sis");
        singular("(^analy)ses$", "$1sis");
        singular("([^f])ves$", "$1ve");
        singular("(hive)s$", "$1");
        singular("(tive)s$", "$1");
        singular("([lr])ves$", "$1f");
        singular("([^aeiouy]|qu)ies$", "$1y");
        singular("(s)eries$", "$1eries");
        singular("(m)ovies$", "$1ovie");
        singular("(x|ch|ss|sh)es$", "$1");
        singular("([m|l])ice$", "$1ouse");
        singular("(bus)es$", "$1");
        singular("(o)es$", "$1");
        singular("(shoe)s$", "$1");
        singular("(cris|ax|test)es$", "$1is");
        singular("([octop|vir])i$", "$1us");
        singular("(alias|status)es$", "$1");
        singular("^(ox)en", "$1");
        singular("(vert|ind)ices$", "$1ex");
        singular("(matr)ices$", "$1ix");
        singular("(quiz)zes$", "$1");
        
        irregular("person", "people");
        irregular("man", "men");
        irregular("child", "children");
        irregular("sex", "sexes");
        irregular("move", "moves");

        uncountable(new String[] {"equipment", "information", "rice", "money", "species", "series", "fish", "sheep"});

        present("aaed$","a");
        present("bbed$","b");
        present("cced$","c");
        present("dded$","d");
        present("ffed$","f");
        present("gged$","g");
        present("ied$","y");
        present("kkd$","k");
        present("lld$","l");
        present("mmd$","m");
        present("nned$","n");
        present("pped$","p");
        present("rred$","r");
        present("ssed$","s");
        present("tted$","t");
        present("zzed$","z");
        present("ed$","");
        present("ing$","");
        
        present("^awoke$", "awake");
        present("^awoken$", "awake");
        present("^was$", "be");
        present("^were$", "be");
        present("^been$", "be");
        present("^beaten$", "beat");
        present("^became$",	"become");
        present("^began$", "begin");
        present("^begun$", "begin");
        present("^bent$", "bend");
        present("^bit$", "bite");
        present("^bitten$", "bite");
        present("^blew$", "blow");
        present("^blown$", "blow");
        present("^broke$", "break");
        present("^broken$", "break");
        present("^brought$", "bring");
        present("^built$", "build");
        present("^burnt$", "burn");
        present("^bought$", "buy");
        present("^caught$", "catch");
        present("^chosen$", "choose");
        present("^came$", "come");
        present("^dug$", "dig");
        present("^did$", "do");
        present("^done$", "do");
        present("^drew$", "draw");
        present("^drawn$", "draw");
        present("^dreamt$", "dream");
        present("^drove$", "drive");
        present("^driven$", "drive");
        present("^drank$", "drink");
        present("^drunk$", "drink");
        present("^ate$", "eat");
        present("^eaten$", "eat");
        present("^fell$", "fall");
        present("^fallen$", "fall");
        present("^felt$", "feel");
        present("^fought$", "fight");
        present("^found$", "find");
        present("^flew$", "fly");
        present("^flown$", "fly");
        present("^forgot$", "forget");
        present("^forgotten$", "forget");
        present("^forgave$", "forgive");
        present("^forgiven$", "forgive");
        present("^froze$", "freeze");
        present("^frozen$", "freeze");
        present("^got$", "get");
        present("^gotten$", "get");
        present("^gave$", "give");
        present("^given$", "give");
        present("^went$", "go");
        present("^gone$", "go");
        present("^grew$", "grow");
        present("^grown$", "grow");
        present("^hung$", "hang");
        present("^had$", "have");
        present("^heard$", "hear");
        present("^hid$", "hide");
        present("^hidden$", "hide");
        present("^held$", "hold");
        present("^knew$", "know");
        present("^known$", "know");
        present("^laid$", "lay");
        present("^led$", "lead");
        present("^learnt$", "learn");
        present("^left$", "leave");
        present("^lent$", "lend");
        present("^lay$", "lie");
        present("^lain$", "lie");
        present("^lost$", "lose");
        present("^made$", "make");
        present("^meant$", "mean");
        present("^met$", "meet");
        present("^paid$", "pay");
        present("^rode$", "ride");
        present("^ridden$", "ride");
        present("^rang$", "ring");
        present("^rung$", "ring");
        present("^rose$", "rise");
        present("^risen$", "rise");
        present("^ran$", "run");
        present("^said$", "say");
        present("^saw$", "see");
        present("^seen$", "see");
        present("^sold$", "sell");
        present("^sent$", "send");
        present("^shown$", "show");
        present("^sang$", "sing");
        present("^sung$", "sing");
        present("^sat$", "sit");
        present("^slept$", "sleep");
        present("^spoke$", "speak");
        present("^spoken$", "speak");
        present("^spent$", "spend");
        present("^stood$", "stand");
        present("^swam$", "swim");
        present("^swum$", "swim");
        present("^took$", "take");
        present("^taken$", "take");
        present("^taught$", "teach");
        present("^tore$", "tear");
        present("^torn$", "tear");
        present("^told$", "tell");
        present("^thought$", "think");
        present("^threw$", "throw");
        present("^thrown$", "throw");
        present("^understood$", "understand");
        present("^woke$", "wake");
        present("^woken$", "wake");
        present("^wore$", "wear");
        present("^worn$", "wear");
        present("^won$", "win");
        present("^wrote$", "write");
        present("^written$", "write");

    }

    public static Inflector getInstance() {
        if (instance == null) {
            instance = new Inflector();
        }
        return instance;
    }

    public String underscore(String camelCasedWord) {

        // Regexes in Java are fucking stupid...
        String underscoredWord = UNDERSCORE_PATTERN_1.matcher(camelCasedWord).replaceAll("$1_$2");
        underscoredWord = UNDERSCORE_PATTERN_2.matcher(underscoredWord).replaceAll("$1_$2");
        underscoredWord = underscoredWord.replace('-', '_').toLowerCase();

        return underscoredWord;
    }

    public String pluralize(String word) {
        if (uncountables.contains(word.toLowerCase())) {
            return word;
        }
        return replaceWithFirstRule(word, plurals);
    }

    public String singularize(String word) {
        if (uncountables.contains(word.toLowerCase())) {
            return word;
        }
        return replaceWithFirstRule(word, singulars);
    }
    
    public String presentize(String word){
    	return replaceWithFirstRule(word, presents);
    }

    private String replaceWithFirstRule(String word, List<RuleAndReplacement> ruleAndReplacements) {

        for (RuleAndReplacement rar : ruleAndReplacements) {
            String rule = rar.getRule();
            String replacement = rar.getReplacement();

            // Return if we find a match.
            Matcher matcher = Pattern.compile(rule, Pattern.CASE_INSENSITIVE).matcher(word);
            if (matcher.find()) {
                return matcher.replaceAll(replacement);
            }
        }
        return word;
    }

    public String tableize(String className) {
        return pluralize(underscore(className));
    }
    
    @SuppressWarnings("unchecked")
	public String tableize(Class klass) {
        // Strip away package name - we only want the 'base' class name.
        String className = klass.getName().replace(klass.getPackage().getName()+".", "");
        return tableize(className);
    }

    public static void plural(String rule, String replacement) {
        plurals.add(0, new RuleAndReplacement(rule, replacement));
    }

    public static void singular(String rule, String replacement) {
        singulars.add(0, new RuleAndReplacement(rule, replacement));
    }

    public static void present(String rule, String replacement) {
        presents.add(new RuleAndReplacement(rule, replacement));
    }

    public static void irregular(String singular, String plural) {
        plural(singular, plural);
        singular(plural, singular);
    }

    public static void uncountable(String... words) {
        for (String word : words) {
            uncountables.add(word);
        }
    }

	public String[] singularizeStringList(String[] list) {
		if (list!=null){
			String[] steamList = new String[list.length];
			int i=0;
			for (String word: list){
				steamList[i] = singularize(word);
				i++;
			}
			
			return steamList;
		}
		return null;
	}
	
	public ArrayList<String> singularize(ArrayList<String> list) {
		if (list!=null){
			ArrayList<String> steamList = new ArrayList<String>();
			for (String word: list){
				steamList.add(singularize(word));
			}
			
			return steamList;
		}
		return null;
	}
    
//	public static void main(String[] args) {
//		String word = "ate";
//		String rule = "^ate$";
//		String replacement = "eat";
//		String ret = "";
//      
//		Matcher matcher = Pattern.compile(rule, Pattern.CASE_INSENSITIVE).matcher(word);
//        if (matcher.find()) {
//             ret = matcher.replaceAll(replacement);
//        }
//
//		System.out.println(Inflector.getInstance().presentize(word));
//	}

}


// Ugh, no open structs in Java (not-natively at least).
class RuleAndReplacement {
    private String rule;
    private String replacement;
    public RuleAndReplacement(String rule, String replacement) {
        this.rule = rule;
        this.replacement = replacement;
    }
    public String getReplacement() {
        return replacement;
    }
    public void setReplacement(String replacement) {
        this.replacement = replacement;
    }
    public String getRule() {
        return rule;
    }
    public void setRule(String rule) {
        this.rule = rule;
    }
    
}
