package tests;

import java.util.*;
import java.util.Map.Entry;

import util.TextSplitter;

import beans.nlp.NLPDependencyRelation;
import beans.nlp.NLPDependencyWord;

import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.trees.GrammaticalRelation.GrammaticalRelationAnnotation;
import edu.stanford.nlp.util.StringUtils;
import edu.stanford.nlp.ling.CoreAnnotation;
import edu.stanford.nlp.ling.CyclicCoreLabel;
import edu.stanford.nlp.ling.Label;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;

class ParserTest {
	public static void main(String[] args) {
				
		LexicalizedParser lp = new LexicalizedParser("resources/englishPCFG.ser.gz");
		lp.setOptionFlags(new String[]{"-maxLength", "80", "-retainTmpSubcategories"});

		String text = "Logging occurs before call to class display";
		String[] sentences= text.split("[.]");
		for (String sentence: sentences){
			String[] sent = TextSplitter.split(sentence);
//			Tree parse = (Tree) lp.apply(Arrays.asList(sent));
			lp.parse(sentence);
			Tree parse = lp.getBestParse();
			parse.pennPrint();
			System.out.println();
	
			TreebankLanguagePack tlp = new PennTreebankLanguagePack();
			GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
			GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
	
	
			Collection<TypedDependency> tdl = gs.typedDependenciesCollapsed();
			
			TreePrint tp = new TreePrint("penn,typedDependenciesCollapsed");
		    tp.printTree(parse);
					
//			for (TypedDependency td: tdl){
//				TreeGraphNode gov = td.gov();
//				TreeGraphNode dep = td.dep();
//				GrammaticalRelation reln = td.reln();
//				
//				NLPDependencyWord t1 = createTreeNode(gov);
//				NLPDependencyWord t2 = createTreeNode(dep);
//				NLPDependencyRelation tnr = createTreeRelationNode(reln, t1, t2);
//				
//				System.out.println(tnr);
//				
//			}
		}

//		Collection<TreeGraphNode> t = gs.getNodes();
//		HashMap<Integer,TreeNode> list= new HashMap<Integer,TreeNode>();
//
//		for (TreeGraphNode tgn: t){
//			list = createRelationList(tgn, list, null, 0);
//		}
//
//		for (Entry<Integer, TreeNode> tn : list.entrySet()){
//			System.out.println(tn.getKey());
//		}
//		
//				    System.out.println();
//
//		ArrayList<TreeNodeRelation> list2= new ArrayList<TreeNodeRelation>();
//		
//		for (Object dep: tdl){
//			String sdep = (String) dep;
//			TreeNodeRelation tnr = new TreeNodeRelation();
//			tnr.setRelationType(sdep.substring(0,sdep.indexOf("(")));
//			String[] pair = sdep.substring(sdep.indexOf("(")+1,sdep.length()-1).split(",");
//			String[] wordPos = pair[0].split("-");
//			tnr.setT1(list.get(new Integer(wordPos[1].trim())));
//			wordPos = pair[1].split("-");
//			tnr.setT2(list.get(new Integer(wordPos[1].trim())));
//			list2.add(tnr);
//		}
//		
//		for (TreeNodeRelation tn : list2){
//			System.out.println(tn);
//		}
////		System.out.println(tdl);
//
//		
////		TreePrint tp = new TreePrint("penn,typedDependenciesCollapsed");
//		//		    tp.printTree(parse);
//
	}
private static NLPDependencyRelation createTreeRelationNode(GrammaticalRelation reln, NLPDependencyWord t1, NLPDependencyWord t2) {
		NLPDependencyRelation tRelNode = new NLPDependencyRelation();
		tRelNode.setRelationType(reln.getShortName());
		tRelNode.setGovDW(t1);
		tRelNode.setDepDW(t2);		
		
		return tRelNode;
	}
//
//
//
	
	public static NLPDependencyWord createTreeNode(TreeGraphNode node){
		NLPDependencyWord tNode = new NLPDependencyWord();
		CyclicCoreLabel label = node.label();
		if (label!=null){
			for (Class key :label.keySet()){
				Object value = label.get(key);
				if (key.getName().equalsIgnoreCase("edu.stanford.nlp.ling.CoreAnnotations$ValueAnnotation"))
					tNode.setWord((String) value);
				if (key.getName().equalsIgnoreCase("edu.stanford.nlp.ling.CoreAnnotations$TagAnnotation"))
					tNode.setType((String) value);
				if (key.getName().equalsIgnoreCase("edu.stanford.nlp.ling.CoreAnnotations$IndexAnnotation"))
					tNode.setPosition((Integer)value);
			}
		}
		
		return tNode;
	}
	
	public static HashMap<Integer,NLPDependencyWord> createRelationList(Tree t, HashMap<Integer,NLPDependencyWord> list, NLPDependencyWord tNode, int lvl) {
		if (lvl==0)
			tNode = new NLPDependencyWord();
		if (lvl>1)
			return null;
		
		if (t.label() != null) {
			String[] labels = t.label().toString().split("-");
			if (lvl==0)
				tNode.setType(labels[0].trim());
			else
				tNode.setWord(labels[0].trim());
				
			tNode.setPosition(Integer.parseInt(labels[1].trim()));
		}
		Tree[] kids = t.children();
		if (kids != null) {
			for (int i = 0; i < kids.length; i++) {
				if (createRelationList(kids[i], list, tNode, lvl+1)==null)
					break;
			}
		}
		
		if (t.isLeaf() && lvl==1)
			list.put(tNode.getPosition(), tNode);
		
		
		return list;
	}
}
