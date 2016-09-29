import java.io.*;
import java.util.*;

import edu.stanford.nlp.io.*;
import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.util.*;

public class StanfordCoreNlpDemo {

  public static void main(String[] args) throws IOException {
    PrintWriter out;
    if (args.length > 1) {
      out = new PrintWriter(args[1]);
    } else {
      out = new PrintWriter(System.out);
    }
    PrintWriter xmlOut = null;
    if (args.length > 2) {
      xmlOut = new PrintWriter(args[2]);
    }

	Properties props = new Properties();
    props.put("annotators", "tokenize, ssplit, pos, lemma");
    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
    
    
    Annotation annotation;

      annotation = new Annotation("Kosgi Santosh sent the emails to Stanford University. He didn't get a reply.");

    pipeline.annotate(annotation);
//    pipeline.prettyPrint(annotation, out);
    if (xmlOut != null) {
      pipeline.xmlPrint(annotation, xmlOut);
    }

  }

}
