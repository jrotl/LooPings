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

// Java libraries
import java.io.*;
import java.lang.*;
import java.util.*;
import java.text.*;


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


public class LOOPINGS_Panel2 extends JPanel {

	public LOOPINGS_Panel lp  = new LOOPINGS_Panel();
	
	public JLabel jlcl1;
	public JButton jb1,jb2;
	public boolean loaded = false;
	public boolean del = true;
	
	public JPopupMenu popup;
	
	private static ILexicalDatabase db = new NictWordNet();	

	private static RelatednessCalculator[] rcs = {
		new Path(db), new LeacockChodorow(db), new WuPalmer(db), 
		new JiangConrath(db), new Lin(db)
	};
	

	private static String[] rcsname = {
		"PLR", "LCH", "WUP", 
		"JCN", "LIN"
	};
		
	public CustomizedJTextArea jtf,jtf2;
	public JScrollPane js, jsp;
	public DefaultMutableTreeNode topindiv = new DefaultMutableTreeNode("┬ ");
	public JTree treeindiv  = new JTree(topindiv);

	public OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
	public OWLDataFactory dataFactory = manager.getOWLDataFactory();
	
	public String ontopath = LOOPINGS_Panel2.class.getClassLoader().getResource("").getPath()+"Queries_Base.owl";
		
	public OWLOntology ontology = manager.loadOntologyFromOntologyDocument(new File(ontopath));
	
	public IRI iri = IRI.create("http://www.semanticweb.org/loopings_base#Description");
	public IRI iriq = IRI.create("http://www.semanticweb.org/loopings_base#Query");
	public IRI irid = IRI.create("http://www.semanticweb.org/loopings_base#Id");		
	public IRI irisparql = IRI.create("http://www.semanticweb.org/loopings_base#Sparql");
	public String inputstring = "";
	public RoundedPanel orderg;
	public JTabbedPane ordertab;
	public int COUNT = 0;


	public JButton [] jbls = new JButton[rcs.length];
	public JButton [] jbrs = new JButton[rcs.length];
	
	public JPanel [] order = new JPanel[rcs.length];
	
	public String[] SetOfScore;
	public String[] SetOfId;
	
	public int tabselected = 0;
	
	public Dessin [] dessins = new Dessin[rcs.length];	

	public static double run( String[] word1, String[] word2, int counter ) {
		WS4JConfiguration.getInstance().setMFS(true);
		double[][] d = new double[word1.length][word2.length];
		double score = 0.0;
		RelatednessCalculator rc = rcs[counter];
			d = rc.getNormalizedSimilarityMatrix(word1, word2);

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
			score = (dividende / diviseur);					                        
			System.out.println("\n");
			System.out.println("Score = " + score);   
			System.out.println("\n");
		return score;                
	}
        
	private static boolean check(String word) throws IOException {
		BufferedReader reader = new BufferedReader( new FileReader ("stopwords.txt"));
		String line = null;
		while( ( line = reader.readLine() ) != null ) {
			if (line.equals(word.toLowerCase())) {return true;}
		}
		reader.close();
		return false;		
	}

	public String gSCORE;
	public String gIDENTIFIANT;
	public String gEXPEIDS;
	
	public String[] SCORE = new String[5];
	public String[] IDENTIFIANT = new String[5];
	public String[] DESCRIPTION = new String[5];
	
				
				
	public List<String> rows = new ArrayList<String>();
	public int inc = 0;
	public PrefixManager pm = new DefaultPrefixManager("http://www.semanticweb.org/LOOPINGS_base");

	public LOOPINGS_Panel2() throws Exception, OWLOntologyCreationException {			

		Color Blue = new Color(128,208,208);
			
		this.setBackground(Blue);
		RoundedBorder border = new RoundedBorder(Color.BLACK, 20, 20);
		this.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH; // components grow in both dimensions
		c.insets = new Insets(5, 5, 5, 5); // 5-pixel margins on all sides

		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		c.gridheight = 1;
		c.weightx = c.weighty = 0.5;	

		RoundedPanel inputs = new RoundedPanel();
		inputs.setBorder(BorderFactory.createLineBorder(Color.ORANGE));
		inputs.setBorder(BorderFactory.createTitledBorder(border, "Input a new query"));

		jtf2 = new CustomizedJTextArea();
		jtf2.addMouseListener(new Inputtext());
		jtf2.setEnabled(false);

		jtf2.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				inputstring = jtf2.getText();
				try {del=false;indiv4jtree();del=true;} catch (OWLOntologyCreationException ex) {}
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				inputstring = jtf2.getText();
				try {del=false;indiv4jtree();del=true;} catch (OWLOntologyCreationException ex) {}
			}

			@Override
			public void changedUpdate(DocumentEvent arg0) {
				inputstring = jtf2.getText();
				try {del=false;indiv4jtree();del=true;} catch (OWLOntologyCreationException ex) {}
			}
		});
  

		Font font3 = new Font("MONOSPACED", Font.PLAIN  , 24);
		jtf2.setFont(font3);

		JScrollPane js2 = new JScrollPane(jtf2);
		


		js2.setBorder(null); 

		jb1  = new JButton("Similarity");
		jb1.addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent e) {}

			public void mouseEntered(MouseEvent e) {}

			public void mouseExited(MouseEvent e) {}

			public void mousePressed(MouseEvent e) {jb2.setEnabled(false);}

			public void mouseReleased(MouseEvent e) {}
		});
		
		jb1.addActionListener(new SimComputationListener());
				
		jb2  = new JButton("Add Case");
		
		jb2.addActionListener(new AddCaseListener());
							
		JPanel ppm = new JPanel(new BorderLayout());
		ppm.add(jb1, BorderLayout.NORTH);
		ppm.add(jb2, BorderLayout.SOUTH);
		ppm.setBorder(new EmptyBorder(5, 5, 0, 5) );

		BorderLayout bl3 = new BorderLayout();
		JPanel psp2 = new JPanel(bl3);		
		psp2.setBorder(new EmptyBorder(5, 5, 5, 5) );
		psp2.setOpaque(false);
		
		psp2.add(js2,BorderLayout.CENTER);
		psp2.add(ppm,BorderLayout.EAST);		

		GridLayout gl3 = new GridLayout(1, 1);
		inputs.setLayout(gl3);
		gl3.setHgap(0);
		gl3.setVgap(0);
		
		inputs.add(psp2);
		
		this.add(inputs, c);

		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = c.weighty = 1.5;	

		RoundedPanel nlps = new RoundedPanel();
		nlps.setBorder(BorderFactory.createLineBorder(Color.ORANGE));
		nlps.setBorder(BorderFactory.createTitledBorder(border, "List of the queries"));

		jlcl1 = new JLabel();
		jlcl1.setFont(new Font("Arial",Font.BOLD,10));

		BorderLayout bl = new BorderLayout();
		bl.setVgap(5);
		JPanel jp1 = new JPanel(bl);
		jp1.setPreferredSize(new Dimension(0, 0));
		jp1.setBorder(new EmptyBorder(5, 5, 5, 5) );
		jp1.setOpaque(false);

		ImageIcon leafIconbla = new ImageIcon("owl-bla.png");

		if (leafIconbla != null) {
			DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
			renderer.setLeafIcon(leafIconbla);
			renderer.setOpenIcon(leafIconbla);
			renderer.setClosedIcon(leafIconbla);
			treeindiv.setCellRenderer(renderer);
		}



    popup = new JPopupMenu();

    JMenuItem menuItem = new JMenuItem("Remove");
    
    menuItem.addActionListener(new Remove());
    popup.add(menuItem);

MouseListener ml = new MouseAdapter() {
public void mouseClicked(MouseEvent e) {

    if (SwingUtilities.isRightMouseButton(e)) {

        int row = treeindiv.getClosestRowForLocation(e.getX(), e.getY());
        treeindiv.setSelectionRow(row);
 
        popup.show(e.getComponent(), e.getX(), e.getY());
        popup.setLabel(Integer.toString(row));
    }
}

};
treeindiv.addMouseListener(ml);

		treeindiv.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
if (del) {
				if (e.getNewLeadSelectionPath().getLastPathComponent().toString().substring(0,e.getNewLeadSelectionPath().getLastPathComponent().toString().indexOf(':')).equals("New")) jb2.setEnabled(true);
				else jb2.setEnabled(false);
				
				for (OWLIndividual in : ontology.getIndividualsInSignature()) {
						OWLLiteral olid = (OWLLiteral)in.getDataPropertyValues(dataFactory.getOWLDataProperty(irid),ontology).toArray()[0];
						System.out.println(e.getNewLeadSelectionPath().getLastPathComponent().toString().substring(0,e.getNewLeadSelectionPath().getLastPathComponent().toString().indexOf(':')));
					if (olid.getLiteral().equals(e.getNewLeadSelectionPath().getLastPathComponent().toString().substring(0,e.getNewLeadSelectionPath().getLastPathComponent().toString().indexOf(':')))) {
						OWLLiteral olsparql = (OWLLiteral)in.getDataPropertyValues(dataFactory.getOWLDataProperty(irisparql),ontology).toArray()[0];
						jtf.setText(olsparql.getLiteral());
						jtf.setCaretPosition(0);
					}
				}
			}
			}
		});
				
		treeindiv.setRootVisible(false);
   	    treeindiv.setShowsRootHandles(true);

		treeindiv.addTreeSelectionListener(new TreeSelectionListener() {
		   public void valueChanged(TreeSelectionEvent tse) {
		   // ta.append("Tree Selection changed\n");
		   }
		});
  
		treeindiv.addFocusListener(new FocusListener() {
		   public void focusGained(FocusEvent fe) {
			//ta.append("Tree focus gained\n");
		   }
		   public void focusLost(FocusEvent fe) {
			//ta.append("Tree focus lost\n");
		   }
		});
   	    
		indiv4jtree();

		jsp = new JScrollPane(treeindiv);

//jsp.getViewport().setViewPosition(

//jsp.setViewportView(treeindiv);
//		jsp.scrollPathToVisible(treeindiv);
		
		jsp.setPreferredSize(new Dimension(280, 110));

		jp1.add(jlcl1, BorderLayout.NORTH);
		jp1.add(jsp, BorderLayout.CENTER);

		GridLayout gl1 = new GridLayout(1, 1);
		nlps.setLayout(gl1);
		gl1.setHgap(0);
		gl1.setVgap(0);
		
		nlps.add(jp1);


		this.add(nlps, c);
		
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = c.weighty = 1.5;	

		RoundedPanel sparqls = new RoundedPanel();
		sparqls.setBorder(BorderFactory.createLineBorder(Color.ORANGE));
		sparqls.setBorder(BorderFactory.createTitledBorder(border, "SPARQL by Qakis"));

		jtf = new CustomizedJTextArea();

		Font font2 = new Font("MONOSPACED", Font.PLAIN  , 12);
		jtf.setFont(font2);

		js = new JScrollPane(jtf);
		js.setBorder(null); 
		js.setPreferredSize(new Dimension(420, 105));

		BorderLayout bl2 = new BorderLayout();
		bl2.setVgap(5);
		JPanel psp = new JPanel(bl2);		
		psp.setBorder(new EmptyBorder(5, 5, 5, 5) );
		psp.setPreferredSize(new Dimension(0, 0));
		psp.setOpaque(false);		

		psp.add(js,BorderLayout.CENTER);

		GridLayout gl2 = new GridLayout(1, 1);
		sparqls.setLayout(gl2);
		gl2.setHgap(0);
		gl2.setVgap(0);
		
		sparqls.add(psp);				
		this.add(sparqls, c);

		
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 2;
		c.gridheight = 1;
		c.weightx = c.weighty = 1.0;	

		orderg = new RoundedPanel();
		orderg.setBorder(BorderFactory.createLineBorder(Color.ORANGE));
		orderg.setBorder(BorderFactory.createTitledBorder(border, "Ordering"));

		ordertab = new JTabbedPane();
		ordertab.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));


		ordertab.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {

				System.out.println("Tab Selected: " + ordertab.getSelectedIndex());

				COUNT = 0;
				tabselected =  ordertab.getSelectedIndex();
				if (loaded) {
					for(int i=0; i<rows.size(); i++) {
						if (rows.get(i).equals(IDENTIFIANT[tabselected].split(",")[IDENTIFIANT[tabselected].split(",").length -1])) 
						{
						treeindiv.setSelectionRow(i+1);
						//jsp.getVerticalScrollBar().setValue((i+1)*15); 
						treeindiv.scrollRowToVisible(i+1);
						}
					}
				}
			}
		});
		
		for (int counter=0; counter<rcs.length; counter++) {

			order[counter] = new JPanel();
			GridLayout experimentLayout = new GridLayout(3,1);
			order[counter].setLayout(experimentLayout);					

			ordertab.add(rcsname[counter], order[counter]);
			jbls[counter] = new JButton("<");
			jbrs[counter] = new JButton(">");
	
			Font fontplus = new Font("MONOSPACED", Font.PLAIN  , 12);
			jbls[counter].setBorder(null);
			jbls[counter].setPreferredSize(new Dimension(20, 12));
			jbls[counter].setFont(fontplus);
			
			jbrs[counter].setBorder(null);
			jbrs[counter].setPreferredSize(new Dimension(20, 12));
			jbrs[counter].setFont(fontplus);
			
			jbls[counter].addActionListener(new ButtonSense(counter, "left"));
			jbrs[counter].addActionListener(new ButtonSense(counter, "right"));

		}

		BorderLayout blbtab = new BorderLayout();
		blbtab.setVgap(1);
		orderg.setLayout(blbtab);				
		orderg.add(ordertab);

		this.add(orderg, c);
						
	}


class Remove implements ActionListener {

    public void actionPerformed(ActionEvent e)  {
//				del = true;
		System.out.println(popup.getLabel());
		TreeNode root = (TreeNode)treeindiv.getModel().getRoot();
		System.out.println(root.getChildAt(Integer.parseInt(popup.getLabel())));
			

		if (!root.getChildAt(Integer.parseInt(popup.getLabel())).toString().substring(0, root.getChildAt(Integer.parseInt(popup.getLabel())).toString().indexOf(":")).equals("New")) {
			String id = root.getChildAt(Integer.parseInt(popup.getLabel())).toString().substring(0, root.getChildAt(Integer.parseInt(popup.getLabel())).toString().indexOf(":"));
			OWLIndividual indiv = dataFactory.getOWLNamedIndividual("#Q"+  id, pm);
            
            for (OWLAxiom ax : ontology.getAxioms()) {
                if (ax.getSignature().contains(indiv)) {
					manager.removeAxiom(ontology,ax);
                }
            }

            
           try {
			   
			            				File newfile = new File(ontopath);
				manager.saveOntology(ontology, IRI.create(newfile.toURI()));   
			  del = false;
			  indiv4jtree();
			  del = true;
           
           } catch(Exception ex) {ex.printStackTrace();} 
		}
//    		
    }
}


	class Inputtext implements MouseListener{

		public void mouseClicked(MouseEvent arg0) {
			jtf2.setEnabled(true);
			treeindiv.setSelectionRow(0);
		}

		public void mouseEntered(MouseEvent arg0) {
			jtf2.setEnabled(true);		
		}

		public void mouseExited(MouseEvent arg0) {
		}

		public void mousePressed(MouseEvent arg0) {
		}

		public void mouseReleased(MouseEvent arg0) {
		}	
	}

	class AddCaseListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			try{
				
				OWLNamedIndividual selectedindiv = dataFactory.getOWLNamedIndividual("#Q"+  ++inc, pm);
				
				OWLClass selectedclass = dataFactory.getOWLClass(iriq);
				OWLClassAssertionAxiom classAssertion = dataFactory.getOWLClassAssertionAxiom(selectedclass, selectedindiv);			
				
				manager.addAxiom(ontology, classAssertion);

				OWLDataProperty owldp = dataFactory.getOWLDataProperty("#Sparql", pm);
				OWLLiteral owlit = dataFactory.getOWLLiteral(jtf.getText()); 
				OWLDataPropertyAssertionAxiom dpropertyAssertion = dataFactory.getOWLDataPropertyAssertionAxiom(owldp, selectedindiv, owlit);
				manager.addAxiom(ontology, dpropertyAssertion);

				owldp = dataFactory.getOWLDataProperty("#Description", pm);
				owlit = dataFactory.getOWLLiteral(jtf2.getText()); 
				dpropertyAssertion = dataFactory.getOWLDataPropertyAssertionAxiom(owldp, selectedindiv, owlit);
				manager.addAxiom(ontology, dpropertyAssertion);
							
				owldp = dataFactory.getOWLDataProperty("#Id", pm);
				owlit = dataFactory.getOWLLiteral(inc); 
				dpropertyAssertion = dataFactory.getOWLDataPropertyAssertionAxiom(owldp, selectedindiv, owlit);
				manager.addAxiom(ontology, dpropertyAssertion);

				File newfile = new File(ontopath);
				manager.saveOntology(ontology, IRI.create(newfile.toURI()));
			  del = false;
				indiv4jtree();  
							  del = true;     
						
			} catch(Exception ex) {ex.printStackTrace();} 
		}
	}

	class SimComputationListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			try{

				jb2.setEnabled(false);
					
				loaded = true;
				
				for (int counter=0; counter<rcs.length; counter++) {
			
					HashMap<Double,String> pair = new HashMap<Double,String>();
					//HashMap<String,Double> inversepair = new HashMap<String,Double>();
					HashMap<Double,String> pairp = new HashMap<Double,String>();
					//HashMap<String,Double> inversepairp = new HashMap<String,Double>();

					for (OWLIndividual in : ontology.getIndividualsInSignature()) {

						OWLLiteral owlit1  = (OWLLiteral)in.getDataPropertyValues(dataFactory.getOWLDataProperty(iri),ontology).toArray()[0];
						String s1 = (String)owlit1.getLiteral();
						StanfordLemmatizer slem1 = new StanfordLemmatizer();
						String s1l = "";
						Iterator itr1 = slem1.lemmatize(s1).iterator();
						while(itr1.hasNext()) {
							s1l = s1l + itr1.next() + " ";
						}
						s1 = s1l.replaceAll("\\p{Punct}","");
						String w1[] = s1.split(" ");
						String s1p = "";
						for (String word : w1) {if (!check(word)) s1p = s1p + word + " ";}
						System.out.println("Lemmatization #1 : " + s1p);
						String w1p[] = s1p.split(" ");					

						String s2 = jtf2.getText();
						StanfordLemmatizer slem2 = new StanfordLemmatizer();
						String s2l = "";
						Iterator itr2 = slem2.lemmatize(s2).iterator();
						while(itr2.hasNext()) {
							s2l = s2l + itr2.next() + " ";
						}
						s2 = s2l.replaceAll("\\p{Punct}","");
						String w2[] = s2.split(" ");
						String s2p = "";
						for (String word : w2) {if (!check(word)) s2p = s2p + word + " ";}
						System.out.println("Lemmatization #2 : " + s2p);
						String w2p[] = s2p.split(" ");
					
						double maxcase = ((double)(Math.round(run(w2p, w1p, counter)*100))) / 100.000; 
										
						OWLLiteral owlit2  = (OWLLiteral)in.getDataPropertyValues(dataFactory.getOWLDataProperty(irid),ontology).toArray()[0];
						OWLLiteral owlit3  = (OWLLiteral)in.getDataPropertyValues(dataFactory.getOWLDataProperty(iri),ontology).toArray()[0];
						
						String idofcase = (String)owlit2.getLiteral();
						String deofcase = (String)owlit3.getLiteral();						
							

						System.out.println("COMPUTED SCORE FOR THE CASE "+idofcase+": " + Double.toString(maxcase));
									
						while (pair.containsKey(maxcase)) {
							maxcase = maxcase + 0.001;								
						}

						pair.put(maxcase,idofcase);
						pairp.put(maxcase,deofcase+"/");
						
						//inversepair.put(idofcase,maxcase);
						//inversepairp.put(deofcase,maxcase);						

						System.out.println(" ");
						
						
					}
		
					TreeMap pair2 = new TreeMap<Double,String>(pair);
					TreeMap pair2p = new TreeMap<Double,String>(pairp);					
				
					//Set ids = pair.keySet();
					//Set idsp = pairp.keySet();
					
					//Object[] ids2 = pair.keySet().toArray();
					//Object[] ids2p = pairp.keySet().toArray();					
					
					//int[] idsorted = new int[ids2.length];
					//int[] idsortedp = new int[ids2p.length];					
					
					//Iterator it = ids.iterator();
					//Iterator itp = idsp.iterator();					
					
			
					System.out.println("IDS/SCORE (raw) for the serie" + pair);
					System.out.println("IDS/SCORE (order) for the serie" + pair2.toString());					
		
					String series = pair2.toString();
					String seriesp = pair2p.toString();


					String[] seriebeforeparsing = series.split(",");
				
					String[] seriebeforeparsingp = seriesp.split("/,");

					String thescores = "";
					String theids = "";
					String thedes = "";					
					
					
					for (int tabcc=0; tabcc<seriebeforeparsing.length; tabcc++) {
						String couple = seriebeforeparsing[tabcc].trim();
						String couplep = seriebeforeparsingp[tabcc];						
						
						String score = couple.substring(0, couple.lastIndexOf("=")).replace("{","");
						String id = couple.substring(couple.lastIndexOf("=")+1).replace("}","");
						String des = couplep.substring(couplep.lastIndexOf("=")+1).replace("}","");						


						score = score + "00";
						

						if (score.substring(0,1).equals("1")) {score = "1000";}
						else {score = score.substring(score.lastIndexOf(".")+1,score.lastIndexOf(".")+4);}
									
						thescores = thescores + "," + score;
						theids = theids + 	"," + id;	
						thedes = thedes + "/" + des;		
					}
								
					SCORE[counter] = thescores.substring(1);
					
					IDENTIFIANT[counter] = theids.substring(1);

					DESCRIPTION[counter] = thedes.substring(1);

				}									

				//String[] EXPEIDS = new String[1];	
				//BufferedReader readerexpeids = new BufferedReader( new FileReader ("expeIds.txt"));
				//String lineexpe = null;
				//int counterexpe = 0;
				//while((lineexpe = readerexpeids.readLine()) != null) {
					//EXPEIDS[counterexpe] = lineexpe;
					//counterexpe++;
				//}				
				//gEXPEIDS = EXPEIDS[0];									


				
				for(int inc=0; inc<rcs.length; inc++) {

					SetOfScore = SCORE[inc].split(",");
					SetOfId = IDENTIFIANT[inc].split(",");	

					order[inc].removeAll();
										
					dessins[inc] = new Dessin(SCORE[inc].split(","),IDENTIFIANT[inc].split(","),COUNT);
					
					dessins[inc].setOpaque(true);
//					dessins[inc].setBackground(Color.RED);

					JPanel jpt1 = new JPanel();
					JPanel jpt2 = new JPanel();

					dessins[inc].setMaximumSize(new Dimension(1100, 120));
					
					order[inc].add(jpt1);
					
					JPanel ppm = new JPanel();
					BorderLayout bl = new BorderLayout();
					ppm.setLayout(bl);
    
					ppm.add("West",jbls[inc]);

					JPanel jptc = new JPanel();
					
					jptc.setLayout(new BoxLayout(jptc, BoxLayout.Y_AXIS));

					jptc.add(dessins[inc]);

					ppm.add("Center",jptc);		
					ppm.add("East",jbrs[inc]);
								
					order[inc].add(ppm);
					order[inc].add(jpt2);				
										
					orderg.revalidate();
				}

				for(int i=0; i<rows.size(); i++) {
					if (rows.get(i).equals(IDENTIFIANT[tabselected].split(",")[IDENTIFIANT[tabselected].split(",").length -1])) 
					{
						treeindiv.setSelectionRow(i+1);
						treeindiv.scrollRowToVisible(i+1);
						jtf.setCaretPosition(0);
					}
				}
				
			lp.load(SCORE, DESCRIPTION, jtf2.getText());
			lp.repaint();								
				 
			} catch(Exception ex) {ex.printStackTrace();} 
		}
	}
	
		
	public class Dessin extends JPanel {
    
		public String[] pSCORE, pIDENTIFIANT;
		//public String pEXPEIDS;
		public int pcount;		
		
		public Dessin(String[] Score, String[] Identifiant, int count) {
			if (loaded) {
				pSCORE = Score;
				pIDENTIFIANT = Identifiant;
				pcount = count;
			}
		}
		 
		public void paintComponent(Graphics g) {
			super.paintComponent(g);

			if (loaded) {

				Graphics2D g2d =(Graphics2D)g;		
				g2d.drawLine(35, 45, 1035, 45);
							
				g2d.drawString("0", 35 , 65);
				g2d.drawLine(35, 25, 35, 45);
				g2d.drawLine(135, 35, 135, 45);
				g2d.drawLine(235, 35, 235, 45);
				g2d.drawLine(335, 35, 335, 45);
				g2d.drawLine(435, 35, 435, 45);
				g2d.drawLine(535, 35, 535, 45);
				g2d.drawLine(635, 35, 635, 45);
				g2d.drawLine(735, 35, 735, 45);
				g2d.drawLine(835, 35, 835, 45);                                                        
				g2d.drawLine(935, 35, 935, 45);
				g2d.drawLine(1035, 25, 1035, 45);        		
				g2d.drawString("1", 1035 , 65);
				
			
				//pEXPEIDS = gEXPEIDS;

				for (int j = 0; j < pSCORE.length; j++) {
					int size = 0;
					int scoretoput = Integer.parseInt(pSCORE[j]);
					
					//for (int k = 0; k < pEXPEIDS.split(",").length; k++) {
						//if (pEXPEIDS.split(",")[k].equals(pIDENTIFIANT[j])) {size = k+1;}
					//}
					
					if (size == 0 ) {
						scoretoput = scoretoput + 35 -2;
						g2d.setColor(Color.BLACK);
						g2d.fillOval(scoretoput,43,4,4);
					}
					//if (size == 1 ) {
						//scoretoput = scoretoput + 35 -3;
						//g2d.setColor(Color.GREEN);
						//g2d.fillOval(scoretoput,42,6,6);
						//g2d.setColor(Color.BLACK);					
					//}
					//if (size == 2 ) {
					//	scoretoput = scoretoput + 35 -4;
					//	g2d.setColor(Color.ORANGE);
					//	g2d.fillOval(scoretoput,41,8,8);
					//	g2d.setColor(Color.BLACK);					
					//}
					//if (size == 3 ) {
					//	scoretoput = scoretoput + 35 -5;			
					//	g2d.setColor(Color.RED);
					//	g2d.fillOval(scoretoput,40,10,10);
					//	g2d.setColor(Color.BLACK);						
					//}	
				}

				g2d.rotate(5.495f);

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
							
				int lastscreen = Integer.parseInt(pSCORE[pcount]);

				for (int j = pcount; j < pSCORE.length; j++) {
					int size = 0;
					double idtoput = (Math.sqrt(2)/2) * (Integer.parseInt(pSCORE[j]) + 35);	
					String strtoput = Double.toString(idtoput);
					strtoput = strtoput.substring(0,strtoput.lastIndexOf("."));
				
					//for (int k = 0; k < pEXPEIDS.split(",").length; k++) {
						//if (pEXPEIDS.split(",")[k].equals(pIDENTIFIANT[j])) {
							//size = k+1;
						//}
					//}				
					//if (size == 1 ) {
						//g2d.setColor(Color.GREEN);
						//g2d.drawString(pIDENTIFIANT[j],Integer.parseInt(strtoput) -70, Integer.parseInt(strtoput)+ 40);
						//g2d.setColor(Color.BLACK);	
					//}
					//if (size == 2 ) {
					//	g2d.setColor(Color.ORANGE);
					//	g2d.drawString(pIDENTIFIANT[j],Integer.parseInt(strtoput) -70, Integer.parseInt(strtoput)+ 40);
					//	g2d.setColor(Color.BLACK);
					//}
					//if (size == 3 ) {
					//	g2d.setColor(Color.RED);
					//	g2d.drawString(pIDENTIFIANT[j],Integer.parseInt(strtoput) -70, Integer.parseInt(strtoput)+ 40);
					//	g2d.setColor(Color.BLACK);
					//}
					
					if (j == pcount) {g2d.drawString(pIDENTIFIANT[pcount],Integer.parseInt(strtoput) -10, Integer.parseInt(strtoput)+ 30);}

					else { 
						if (Integer.parseInt(pSCORE[j])-lastscreen > 11) {
							g2d.drawString(pIDENTIFIANT[j],Integer.parseInt(strtoput) -10 , Integer.parseInt(strtoput)+ 30);						
							lastscreen = Integer.parseInt(pSCORE[j]);
						}
					}		
				}
				
				lastscreen = Integer.parseInt(pSCORE[0]);
				int firstscreen = Integer.parseInt(pSCORE[pcount]);

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

				for (int j = 0; j < pcount; j++) {
					int size = 0;
					double idtoput = (Math.sqrt(2)/2) * (Integer.parseInt(pSCORE[j]) + 35);	
					String strtoput = Double.toString(idtoput);
					strtoput = strtoput.substring(0,strtoput.lastIndexOf("."));
				
					//for (int k = 0; k < pEXPEIDS.split(",").length; k++) {
					//	if (pEXPEIDS.split(",")[k].equals(pIDENTIFIANT[j])) {
					//		size = k+1;
					//	}
					//}
					
					//if (size == 1 ) {
					//	g2d.setColor(Color.GREEN);
					//	g2d.drawString(pIDENTIFIANT[j],Integer.parseInt(strtoput) -70, Integer.parseInt(strtoput)+ 40);
					//	g2d.setColor(Color.BLACK);	
					//}
					//if (size == 2 ) {
					//	g2d.setColor(Color.ORANGE);
					//	g2d.drawString(pIDENTIFIANT[j],Integer.parseInt(strtoput) -70, Integer.parseInt(strtoput)+ 40);
					//	g2d.setColor(Color.BLACK);
					//}
					//if (size == 3 ) {
					//	g2d.setColor(Color.RED);
					//	g2d.drawString(pIDENTIFIANT[j],Integer.parseInt(strtoput) -70, Integer.parseInt(strtoput)+ 40);
					//	g2d.setColor(Color.BLACK);
					//}
					
					if ((j == 0) && (firstscreen - Integer.parseInt(pSCORE[j]) > 11)) {g2d.drawString(pIDENTIFIANT[0],Integer.parseInt(strtoput) -10, Integer.parseInt(strtoput)+ 30);}

					else { 
						if ((Integer.parseInt(pSCORE[j])-lastscreen > 11) && (firstscreen - Integer.parseInt(pSCORE[j]) > 11)) {
							g2d.drawString(pIDENTIFIANT[j],Integer.parseInt(strtoput) -10 , Integer.parseInt(strtoput)+ 30);						
							lastscreen = Integer.parseInt(pSCORE[j]);
						}
					}		
				}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////				   
			}
		}
	}

	class ButtonSense implements ActionListener{
		public int _frame;
		public String _sense;		
		
		public ButtonSense(int frame, String sense) {_frame = frame;_sense = sense;}
		
		public void actionPerformed(ActionEvent e) {
		
		if (_sense.equals("right")) COUNT++;
		if (_sense.equals("left")) COUNT--;		
		
		
		if (COUNT >= SCORE[_frame].split(",").length) {COUNT = 0;}
		if (COUNT<0) {COUNT = SCORE[_frame].split(",").length - 1;}


		SetOfScore = SCORE[_frame].split(",");
		SetOfId = IDENTIFIANT[_frame].split(",");	

		order[_frame].removeAll();
					
		dessins[_frame] = new Dessin(SCORE[_frame].split(","),IDENTIFIANT[_frame].split(","),COUNT);
					
		dessins[_frame].setOpaque(true);
//		dessins[_frame].setBackground(Color.RED);

		JPanel jpt1 = new JPanel();
		JPanel jpt2 = new JPanel();

		dessins[_frame].setMaximumSize(new Dimension(1100, 120));
					
		order[_frame].add(jpt1);

		JPanel ppm = new JPanel();
		BorderLayout bl = new BorderLayout();
		ppm.setLayout(bl);
    
		ppm.add("West",jbls[_frame]);

		JPanel jptc = new JPanel();
        jptc.setLayout(new BoxLayout(jptc, BoxLayout.Y_AXIS));
		jptc.add(dessins[_frame]);


		ppm.add("Center",jptc);		
		ppm.add("East",jbrs[_frame]);
					
		order[_frame].add(ppm);
		
		order[_frame].add(jpt2);				
										
		orderg.revalidate();
		
		}
	}
	

	public void indiv4jtree() throws OWLOntologyCreationException {
	
		topindiv.removeAllChildren();

		DefaultMutableTreeNode indintree;
		indintree = new DefaultMutableTreeNode("New" + ":" + inputstring);
		topindiv.add(indintree);
		rows.clear();
		
		HashMap<Integer,OWLIndividual> listofq = new HashMap<Integer,OWLIndividual>();

		for (OWLIndividual in : ontology.getIndividualsInSignature()) {
System.out.println(in.getDataPropertyValues(dataFactory.getOWLDataProperty(irid),ontology));
			OWLLiteral owlit  = (OWLLiteral)in.getDataPropertyValues(dataFactory.getOWLDataProperty(irid),ontology).toArray()[0];
			Integer inte = (Integer)Integer.parseInt(owlit.getLiteral());
			listofq.put(inte,in);
		}	
					
		Collection<OWLIndividual> listofqt = new TreeMap<Integer,OWLIndividual>(listofq).values();
			
		for (OWLIndividual in : listofqt) {
			OWLLiteral ol = (OWLLiteral)in.getDataPropertyValues(dataFactory.getOWLDataProperty(iri),ontology).toArray()[0];
			OWLLiteral olid = (OWLLiteral)in.getDataPropertyValues(dataFactory.getOWLDataProperty(irid),ontology).toArray()[0];
			rows.add(olid.getLiteral());

			indintree = new DefaultMutableTreeNode(olid.getLiteral() +":"+ ol.getLiteral());
			topindiv.add(indintree);

			if (Integer.parseInt(olid.getLiteral()) > inc) inc++;
		}
								
		((DefaultTreeModel) treeindiv.getModel()).setRoot(topindiv);
		((DefaultTreeModel) treeindiv.getModel()).reload();
	}


	public class StanfordLemmatizer {

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
