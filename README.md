# LooPings
Lexical and ontological observations to Plot ingathering similarities
- OVERVIEW:
	
	LOOPINGS uses the following libraries:

	- WORDNET:SIMILARITY (WS4J): ws4j-1.0.1.jar, released under Open Source license (GNU GPL v2)
	The WORDNET:SIMILARITY (WS4J) is a Java API for several published Semantic Relatedness/Similarity algorithms.
	- STANFORD CORENLP: stanford-corenlp-full-2014-06-16/, released under the GNU General Public License (v3 or later; in 		general Stanford NLP code is GPL v2+, but CoreNLP uses several Apache-licensed libraries, and so the composite is v3+). 
	The Stanford CoreNLP a set of natural language analysis tools which can take raw text input and give the base forms of 		words written in Java.		
	- OWLAPI DISTRIBUTION 3.4.* or above, released under Open Source licenses (LGPL and Apache). 
	The OWL API is a Java API for creating, manipulating and serialising OWL Ontologies. 

	  
- USAGE:

	The executables of LOOPINGS (and possibly some shared libraries) are located in the './bin ' directory. 
	The sources of LOOPINGS are located in the './src' directory. To use the library make sure that Java finds owlapi-		distribution-3.4.5.jar,ws4j-1.0.1.jar and stanford-corenlp-full-2014-06-16/* in the class path.


	For example:
	java -cp .:ws4j-1.0.1.jar:owlapi-distribution-3.4.5.jar:stanford-corenlp-full-2014-06-16/*:. LOOPINGS_GUI


- REQUIREMENTS and INSTALLATION:

	LOOPINGS requires WORDNET:SIMILARITY (WS4J) 1.0.1 [1] and STANFORD CORENLP 2014-06-16 [2] and OWLAPI DISTRIBUTION 3.4.5 or above [3]. If they are not included  in the release, then you should install them manually on your system. 
		


- CHANGELOG:

	LooPings version Fall 2016:

[1] https://code.google.com/p/ws4j/
[2] http://nlp.stanford.edu/software/corenlp.shtml
[3] http://owlapi.sourceforge.net/
