package aodbuilder.tests;

import java.net.URL;
import java.util.Collection;

import aodbuilder.util.ResourceLoader;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreePrint;
import edu.stanford.nlp.trees.TreebankLanguagePack;

class ParserDemo {
  public static void main(String[] args) {
	URL url = ResourceLoader.getResourceURL("englishPCFG.ser.gz");		
    LexicalizedParser lp = new LexicalizedParser(url.getFile());
    lp.setOptionFlags(new String[]{"-maxLength", "80", "-retainTmpSubcategories"});

    String sent = "The user enters the homepage of the site. The system asks the user to enter his username and password and validates this information. If it is correct, the system allows the user to enter to the application mainpage.";
    lp.parse(sent);
    Tree parse = lp.getBestParse();
    parse.pennPrint();
    System.out.println();

    TreebankLanguagePack tlp = new PennTreebankLanguagePack();
    GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
    GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
    Collection tdl = gs.typedDependenciesCollapsed();
    System.out.println(tdl);
    System.out.println();

    TreePrint tp = new TreePrint("penn,typedDependenciesCollapsed");
    tp.printTree(parse);
  }

}
