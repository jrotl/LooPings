import java.io.*;
import java.lang.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.awt.event.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.io.*;
import java.lang.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.filechooser.*;
import javax.swing.tree.*;
import java.text.*;
import java.util.*;
import java.util.List;
import java.util.ArrayList;;

//OWL API librairies
import org.semanticweb.owlapi.apibinding.*;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.*;

import edu.cmu.lti.lexical_db.ILexicalDatabase;
import edu.cmu.lti.lexical_db.NictWordNet;
import edu.cmu.lti.ws4j.RelatednessCalculator;
import edu.cmu.lti.ws4j.impl.HirstStOnge;
import edu.cmu.lti.ws4j.impl.JiangConrath;
import edu.cmu.lti.ws4j.impl.LeacockChodorow;
import edu.cmu.lti.ws4j.impl.Lesk;
import edu.cmu.lti.ws4j.impl.Lin;
import edu.cmu.lti.ws4j.impl.Path;
import edu.cmu.lti.ws4j.impl.Resnik;
import edu.cmu.lti.ws4j.impl.WuPalmer;
import edu.cmu.lti.ws4j.util.WS4JConfiguration;


import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;


public class LOOPINGS {
	
	protected static int taskId;

	private static ILexicalDatabase db = new NictWordNet();	
	
	private static RelatednessCalculator[] rcs = {
		new Path(db), new LeacockChodorow(db), new WuPalmer(db), 
		new JiangConrath(db), new Lin(db)
	};
	
	private static boolean check(String word) throws IOException {
		BufferedReader reader = new BufferedReader( new FileReader ("stopwords.txt"));
		String line = null;
		while( ( line = reader.readLine() ) != null ) {
			if (line.equals(word.toLowerCase())) {return true;}
		}
		reader.close();
		return false;		
	}	
	
	public static IRI iri = IRI.create("http://www.semanticweb.org/loopings_base#Description");
	public static IRI iriwit1 = IRI.create("http://www.semanticweb.org/loopings_witness#Witness1");	
	public static IRI iriwit2 = IRI.create("http://www.semanticweb.org/loopings_witness#Witness2");
	public static IRI irid = IRI.create("http://www.semanticweb.org/loopings_base#Id");
	public static IRI irid2 = IRI.create("http://www.semanticweb.org/loopings_witness#Id");
	
	public IRI iriq = IRI.create("http://www.semanticweb.org/desctop#Query");
	public IRI irisparql = IRI.create("http://www.semanticweb.org/desctop#Sparql");

	protected static String _experience, _sentence1, _sentence2, _casebase, _witness, _computation;

	
	public static void main(String[] args) {

		if (!parseArgs(args)) {printHelp();System.exit(1);}

		launch(_casebase,_witness,_experience);
			
    }

	public static boolean parseArgs(String[] args) {
        int i = 0;
        if (args.length == 0) {return false;}
        while (i < args.length) {
// Among avg, med, minmax and wei
			if (args[i].equals("-experience") || args[i].equals("-e")) {
				_experience = args[i + 1];
                i += 2;
            }
            
			else if (args[i].equals("-sentence1") || args[i].equals("-s1")) {
				_sentence1 = args[i + 1];
                i += 2;
            }

			else if (args[i].equals("-sentence2") || args[i].equals("-s2")) {
				_sentence2 = args[i + 1];
                i += 2;
            }

			else if (args[i].equals("-casebase") || args[i].equals("-cb")) {
				_casebase = args[i + 1];
                i += 2;
            }

			else if (args[i].equals("-witness") || args[i].equals("-w")) {
				_witness = args[i + 1];
                i += 2;
            }            
            
            else {
				System.out.println("There is one mistake in the command line.");
				return false;
            }
		}
				
        return true;
	}


	public static void launch(String casebase, String witness, String experience) {
		try {			

OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
OWLDataFactory dataFactory = manager.getOWLDataFactory();
OWLOntology ontology = manager.loadOntologyFromOntologyDocument(new File(casebase));

OWLOntologyManager manager2 = OWLManager.createOWLOntologyManager();
OWLDataFactory dataFactory2 = manager2.getOWLDataFactory();
OWLOntology ontology2 = manager2.loadOntologyFromOntologyDocument(new File(witness));
//manager.addAxioms(ontology, ontology2.getAxioms());

		HashMap<Integer,OWLIndividual> listcase = new HashMap<Integer,OWLIndividual>();

		for (OWLIndividual in : ontology.getIndividualsInSignature()) {
			OWLLiteral owlit  = (OWLLiteral)in.getDataPropertyValues(dataFactory.getOWLDataProperty(irid),ontology).toArray()[0];
			Integer inte = (Integer)Integer.parseInt(owlit.getLiteral());
			listcase.put(inte,in);
		}	
					
		Collection<OWLIndividual> listcaseordered = new TreeMap<Integer,OWLIndividual>(listcase).values();

			for (OWLIndividual in2 : ontology2.getIndividualsInSignature()) {

				OWLLiteral owlitA  = (OWLLiteral)in2.getDataPropertyValues(dataFactory2.getOWLDataProperty(iriwit1),ontology2).toArray()[0];
				String sentenceA = (String)owlitA.getLiteral();
				
				
				OWLLiteral owlitB  = (OWLLiteral)in2.getDataPropertyValues(dataFactory2.getOWLDataProperty(iriwit2),ontology2).toArray()[0];
				String sentenceB = (String)owlitB.getLiteral();


				
				OWLLiteral owlitid2  = (OWLLiteral)in2.getDataPropertyValues(dataFactory2.getOWLDataProperty(irid2),ontology2).toArray()[0];				
				String id2 = (String)owlitid2.getLiteral();

//A
				
				BufferedWriter bwA = new BufferedWriter(new FileWriter("/home/jr/Documents/CBR4DESCTOP/expe_" + experience + "_" +id2 + "_A.txt",false));	
				StanfordLemmatizer slem2A = new StanfordLemmatizer();
				String s2lA = "";
				Iterator itr2A = slem2A.lemmatize(sentenceA).iterator();
				while(itr2A.hasNext()) {
					s2lA = s2lA + itr2A.next() + " ";
				}
								
				String s2A = s2lA.replaceAll("\\p{Punct}","");
				String w2A[] = s2A.split(" ");
				String s2pA = "";
				for (String word : w2A) {if (!check(word)) s2pA = s2pA + word + " ";}
				System.out.println("Lemmatization #A : " + s2pA);
				String w2pA[] = s2pA.split(" ");
				
//B				

				BufferedWriter bwB = new BufferedWriter(new FileWriter("/home/jr/Documents/CBR4DESCTOP/expe_" + experience + "_" +id2 + "_B.txt",false));	
				StanfordLemmatizer slem2B = new StanfordLemmatizer();
				String s2lB = "";
				Iterator itr2B = slem2B.lemmatize(sentenceB).iterator();
				while(itr2B.hasNext()) {
					s2lB = s2lB + itr2B.next() + " ";
				}
								
				String s2B = s2lB.replaceAll("\\p{Punct}","");
				String w2B[] = s2B.split(" ");
				String s2pB = "";
				for (String word : w2B) {if (!check(word)) s2pB = s2pB + word + " ";}
				System.out.println("Lemmatization #B : " + s2pB);
				String w2pB[] = s2pB.split(" ");


				
					for (int counter = 0; counter < rcs.length; counter++) {								
						String ScorelineA = "";
						String ScorelineB = "";						
						for (OWLIndividual in : listcaseordered) {							

							OWLLiteral owlit1  = (OWLLiteral)in.getDataPropertyValues(dataFactory.getOWLDataProperty(iri),ontology).toArray()[0];
							String sentence1 = (String)owlit1.getLiteral();
							System.out.println("ALLO " + sentence1);
							StanfordLemmatizer slem1 = new StanfordLemmatizer();
							String s1l = "";
							Iterator itr1 = slem1.lemmatize(sentence1).iterator();
							while(itr1.hasNext()) {
								s1l = s1l + itr1.next() + " ";
							}
							
							String s1 = s1l.replaceAll("\\p{Punct}","");
							String w1[] = s1.split(" ");
							String s1p = "";
							for (String word : w1) {if (!check(word)) s1p = s1p + word + " ";}
								System.out.println("Lemmatization #1 : " + s1p);
								String w1p[] = s1p.split(" ");
								double maxcaseA = ((double)(Math.round(run(w2pA, w1p, counter,experience)*100))) / 100.000; 
								ScorelineA = ScorelineA + ";" + Double.toString(maxcaseA).replace(".", ",");
								System.out.println("COMPUTED SCORE FOR THE CASE: " + Double.toString(maxcaseA));
								
								double maxcaseB = ((double)(Math.round(run(w2pB, w1p, counter,experience)*100))) / 100.000; 
								ScorelineB = ScorelineB + ";" + Double.toString(maxcaseB).replace(".", ",");
								System.out.println("COMPUTED SCORE FOR THE CASE: " + Double.toString(maxcaseB));														

								System.out.println(" ");						
							}
							bwA.write(ScorelineA.substring(1));
							bwA.newLine();
							
							bwB.write(ScorelineB.substring(1));
							bwB.newLine();
								
						}
						bwA.close();
						bwB.close();												
					}
		} catch (Exception e) {System.err.println(e.getMessage());}	
	}

	public static double run( String[] word1, String[] word2, int counter, String experience ) {
		WS4JConfiguration.getInstance().setMFS(true);
		double[][] d = new double[word1.length][word2.length];
		double score = 0.0;
		RelatednessCalculator rc = rcs[counter];
		d = rc.getNormalizedSimilarityMatrix(word1, word2);



///////////////////////////////////////////////////////// MOYENNE / AVERAGE /////////////////////////////////////////////////////////

		if (experience.equals("avg")) {
			double dividende = 0.0;
			int diviseur = 0;

			for(int i = 0; i < d.length; i++){
				String line = "";
				for(int j = 0; j < d[i].length; j++){
					line = line + d[i][j] + " ";   
					if (d[i][j] != 0.0) {dividende = dividende + d[i][j];diviseur++;}
				
				}
			System.out.println("Line = " + line);   
			System.out.println("\n");
			}
			
			if (diviseur == 0) {score = 0;} else {score = (dividende / diviseur);}
		}


///////////////////////////////////////////////////////// MEDIANE / MEDIAN /////////////////////////////////////////////////////////


		if (experience.equals("med")) {
//			double dividende = 0.0;
//			int diviseur = 0;

			HashMap<Double,Double> vecteur = new HashMap<Double,Double>();	

			//listcase.put(inte,in);
							
			for(int i = 0; i < d.length; i++){
				String line = "";
				for(int j = 0; j < d[i].length; j++){
					line = line + d[i][j] + " ";   
					if (d[i][j] != 0.0) {
//						dividende = dividende + d[i][j];diviseur++;
						double scorecase = d[i][j];
						while (vecteur.containsKey(scorecase)) {
							scorecase = scorecase + 0.001;								
						}
						vecteur.put(scorecase,scorecase);
					}
				}
			
				System.out.println("Line = " + line);   
				System.out.println("\n");
			}
			
			Collection<Double> orderedvecteur = new TreeMap<Double,Double>(vecteur).values();
		
			if (orderedvecteur.toArray().length == 0) {
				  System.out.println("0");
				  score = 0;
			}

			else {

				if (orderedvecteur.toArray().length == 1) {score = (double)orderedvecteur.toArray()[0];}


				else if (orderedvecteur.toArray().length % 2 == 0) {
					  System.out.println("Pair");
					  score = ((double)orderedvecteur.toArray()[(orderedvecteur.toArray().length)/2] + (double)orderedvecteur.toArray()[(orderedvecteur.toArray().length+1)/2])/2;
				 
				}
				else if (orderedvecteur.toArray().length % 2 != 0) {
					  System.out.println("Impair");
					  score = (double)orderedvecteur.toArray()[(orderedvecteur.toArray().length+1)/2];

				}

			}

		}

///////////////////////////////////////////////////////// MIN MAX /////////////////////////////////////////////////////////


		if (experience.equals("minmax")) {

			HashMap<Double,Double> vecteur = new HashMap<Double,Double>();	

			//listcase.put(inte,in);
							
			for(int i = 0; i < d.length; i++){
				String line = "";
				for(int j = 0; j < d[i].length; j++){
					line = line + d[i][j] + " ";   
					if (d[i][j] != 0.0) {
//						dividende = dividende + d[i][j];diviseur++;
						double scorecase = d[i][j];
						while (vecteur.containsKey(scorecase)) {
							scorecase = scorecase + 0.001;								
						}
						vecteur.put(scorecase,scorecase);
					}
				}
			
				System.out.println("Line = " + line);   
				System.out.println("\n");
			}
			
			Collection<Double> orderedvecteur = new TreeMap<Double,Double>(vecteur).values();
		
			if (orderedvecteur.toArray().length == 0) {
				  System.out.println("0");
				  score = 0;
			}

			else {
				score = (double)orderedvecteur.toArray()[0];
			}

		}
		
		
///////////////////////////////////////////////////////// MOYENNE PONDEREE / WEIGHTED AVERAGE /////////////////////////////////////////////////////////

		if (experience.equals("wei")) {
			double dividende = 0.0;
			int diviseur = 0;

			for(int i = 0; i < d.length; i++){
				String line = "";
				for(int j = 0; j < d[i].length; j++){
					line = line + d[i][j] + " ";   
					if (d[i][j] != 0.0) {dividende = dividende + d[i][j];diviseur++;}
				
				}
			System.out.println("Line = " + line);   
			System.out.println("\n");
			}
			double diffcarre = Math.pow(d.length - d[0].length, 2);
			System.out.println("le carré de la diff = " + diffcarre);
			
			if (diviseur == 0) {score = 0;}
			else {

				score = (dividende / diviseur);
				score = (((1 - score) * Math.pow(score,2)) * ((Math.pow(d.length - d[0].length, 2)) / (Math.pow(d.length - d[0].length, 2) + 1))) + score;

			}
		}
		
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			                        
		System.out.println("\n");
		System.out.println("Score = " + score);   
		System.out.println("\n");
		return score;                
	}

	public static void printHelp() {

		String line = null;
		try {
		BufferedReader br = new BufferedReader(new FileReader(LOOPINGS.class.getClassLoader().getResource("parameters.trove").getPath()));
		while ((line = br.readLine()) != null) {
			if ( line.indexOf("trove:help") != -1) {break;}
		}	
		while ((line = br.readLine()) != null) {
			System.out.println(line);	
		}				
		br.close();
		}		
		catch(FileNotFoundException exc) { System.out.println("File not found");}
		catch(IOException ioe) { System.out.println("Erreur IO" );}		
	}	    


public static class StanfordLemmatizer {

		protected StanfordCoreNLP pipeline;

		public StanfordLemmatizer() {
			// Create StanfordCoreNLP object properties, with POS tagging
			// (required for lemmatization), and lemmatization
			Properties props;
			props = new Properties();
			props.put("annotators", "tokenize, ssplit, pos, lemma");

			/*
			 * This is a pipeline that takes in a string and returns various analyzed linguistic forms. 
			 * The String is tokenized via a tokenizer (such as PTBTokenizerAnnotator), 
			 * and then other sequence model style annotation can be used to add things like lemmas, 
			 * POS tags, and named entities. These are returned as a list of CoreLabels. 
			 * Other analysis components build and store parse trees, dependency graphs, etc. 
			 * 
			 * This class is designed to apply multiple Annotators to an Annotation. 
			 * The idea is that you first build up the pipeline by adding Annotators, 
			 * and then you take the objects you wish to annotate and pass them in and 
			 * get in return a fully annotated object.
			 * 
			 *  StanfordCoreNLP loads a lot of models, so you probably
			 *  only want to do this once per execution
			 */
			this.pipeline = new StanfordCoreNLP(props);
		}

		public List<String> lemmatize(String documentText) {
			List<String> lemmas = new LinkedList<String>();
			// Create an empty Annotation just with the given text
			Annotation document = new Annotation(documentText);
			// run all Annotators on this text
			this.pipeline.annotate(document);
			// Iterate over all of the sentences found
			List<CoreMap> sentences = document.get(SentencesAnnotation.class);
			for(CoreMap sentence: sentences) {
				// Iterate over all tokens in a sentence
				for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
					// Retrieve and add the lemma for each word into the
					// list of lemmas
					lemmas.add(token.get(LemmaAnnotation.class));
				}
			}
			return lemmas;
		}
	}	

}
