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

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

// Java libraries
import java.io.*;
import java.lang.*;
import java.util.*;
import java.text.*;




public class LOOPINGS_Panel extends JPanel {

private static String[] rcsname = {
		"PLR", "LCH", "WUP", 
		"JCN", "LIN"
};
	
public JTabbedPane ordertab;


public JButton [] jbls = new JButton[5];
public JButton [] jbrs = new JButton[5];
	
public JPanel [] order = new JPanel[5];
public Dessin [] dessins = new Dessin[5];

public String[] SCORE = new String[5];
public String[] IDENTIFIANT = new String[5];


public String[] SetOfScore;
public String[] SetOfId;

public RoundedPanel serie1;

public boolean loaded;

public int COUNT = 0;
		
//	public LOcKinG_Panel(String[] SCORE,String[] IDENTIFIANT, String[] EXPEIDS) {
	public LOOPINGS_Panel() throws Exception {		

		Color Blue = new Color(128,208,208);
			
		this.setBackground(Blue);
		RoundedBorder border = new RoundedBorder(Color.BLACK, 20, 20);
		this.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH; // components grow in both dimensions
		c.insets = new Insets(5, 5, 5, 5); // 5-pixel margins on all sides


//		this.add(Box.createRigidArea(new Dimension(0,10)));
			
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = c.weighty = 1.0;	

		serie1 = new RoundedPanel();
		serie1.setBorder(BorderFactory.createLineBorder(Color.ORANGE));
		serie1.setBorder(BorderFactory.createTitledBorder(border, "No current Input"));



		ordertab = new JTabbedPane();
		ordertab.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		
		for (int counter=0; counter<5; counter++) {

			order[counter] = new JPanel();
			GridLayout experimentLayout = new GridLayout(1,1);
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
//						if (loaded) {
		



//}
//loaded = false;

		
//			Dessin dessin = new Dessin();		
//			dessin.setOpaque(false);


		BorderLayout blbtab = new BorderLayout();
		blbtab.setVgap(1);
		serie1.setLayout(blbtab);				
		serie1.add(ordertab);

		this.add(serie1, c);
		

//			this.add(Box.createRigidArea(new Dimension(0,10)));	


	}
	
	public void load(String[] _score, String[] _identifiant, String _title) throws IOException, InterruptedException {

		SCORE = _score;
		IDENTIFIANT = _identifiant;
		loaded = true;
		//this.revalidate();
		
		
				for(int inc=0; inc<5; inc++) {

					SetOfScore = SCORE[inc].split(",");
					SetOfId = IDENTIFIANT[inc].split("/");	

					order[inc].removeAll();
										
					dessins[inc] = new Dessin(SCORE[inc].split(","),IDENTIFIANT[inc].split("/"),COUNT);
					
//					dessins[inc] = new Dessin2(inc);					
					
					dessins[inc].setOpaque(true);
					//dessins[inc].setBackground(Color.RED);

					JPanel jpt1 = new JPanel();
					JPanel jpt2 = new JPanel();

					//dessins[inc].setMaximumSize(new Dimension(1100, 120));
					
					//order[inc].add(jpt1);
					
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
					//order[inc].add(jpt2);				
					RoundedBorder border = new RoundedBorder(Color.BLACK, 20, 20);
					
					serie1.setBorder(BorderFactory.createTitledBorder(border, "Input : " + _title));									

					serie1.revalidate();
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
		SetOfId = IDENTIFIANT[_frame].split("/");	

		order[_frame].removeAll();

		dessins[_frame] = new Dessin(SCORE[_frame].split(","),IDENTIFIANT[_frame].split("/"),COUNT);
					
					
//					dessins[inc] = new Dessin2(inc);					
					
					dessins[_frame].setOpaque(true);
					//dessins[inc].setBackground(Color.RED);

					JPanel jpt1 = new JPanel();
					JPanel jpt2 = new JPanel();

					//dessins[inc].setMaximumSize(new Dimension(1100, 120));
					
					//order[inc].add(jpt1);
					
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
					//order[inc].add(jpt2);				
										
					serie1.revalidate();
		
		}
	}


	public class Dessin extends JPanel {
    
		public String[] pSCORE, pIDENTIFIANT;
		public String pEXPEIDS;
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
			
					//System.out.println(" PARAM :" + );
					
				Graphics2D g2d =(Graphics2D)g;
				g2d.translate(0, ordertab.getSize().height - 125);	

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

				for (int j = 0; j < pSCORE.length; j++) {
					int scoretoput = Integer.parseInt(pSCORE[j]);
					
						scoretoput = scoretoput + 35 -2;
						g2d.setColor(Color.BLACK);
						g2d.fillOval(scoretoput,43,4,4);
	
				}

				g2d.rotate(5.495f);

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
							
				int lastscreen = Integer.parseInt(pSCORE[pcount]);

				for (int j = pcount; j < pSCORE.length; j++) {

					double idtoput = (Math.sqrt(2)/2) * (Integer.parseInt(pSCORE[j]) + 35);	
					String strtoput = Double.toString(idtoput);
					strtoput = strtoput.substring(0,strtoput.lastIndexOf("."));
					
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
					double idtoput = (Math.sqrt(2)/2) * (Integer.parseInt(pSCORE[j]) + 35);	
					String strtoput = Double.toString(idtoput);
					strtoput = strtoput.substring(0,strtoput.lastIndexOf("."));
					
					if ((j == 0) && (firstscreen - Integer.parseInt(pSCORE[j]) > 11)) {g2d.drawString(pIDENTIFIANT[0],Integer.parseInt(strtoput) -10, Integer.parseInt(strtoput)+ 30);}

					else { 
						if ((Integer.parseInt(pSCORE[j])-lastscreen > 11) && (firstscreen - Integer.parseInt(pSCORE[j]) > 11)) {
							g2d.drawString(pIDENTIFIANT[j],Integer.parseInt(strtoput) -10 , Integer.parseInt(strtoput)+ 30);						
							lastscreen = Integer.parseInt(pSCORE[j]);
						}
					}		
				}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			//System.out.println("TU RENTRES en" + _inc);
		}
	}
}
