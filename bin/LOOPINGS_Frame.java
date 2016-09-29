import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import java.awt.event.ActionEvent;

import java.awt.Color;

import javax.swing.JTabbedPane;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import javax.swing.UIManager;


import javax.swing.JScrollPane;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import java.awt.Dimension;
public class LOOPINGS_Frame extends JFrame {


//	private static String[] measurestring = {"Order"};

	private JTabbedPane onglet;

	public LOOPINGS_Frame()  throws Exception {
		Color Blue = new Color(128,208,208);

		UIManager.put("TabbedPane.selected",Blue);
		UIManager.put("TabbedPane.contentAreaColor",Blue);
		UIManager.put("TabbedPane.focus",Blue);
	//	UIManager.put("TabbedPane.darkShadow",Color.ORANGE);
	
		this.setTitle("LOOPINGS: Lexical and Ontological Observations and Plots INGathering Similarities");
		

		this.setSize(1150, 610);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		onglet = new JTabbedPane();

		LOOPINGS_Panel2 LOOPINGS_panel = new LOOPINGS_Panel2();		

		JScrollPane _scroll2 = new JScrollPane(LOOPINGS_panel);
		_scroll2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		onglet.add("Inputs", _scroll2);		


		//LOcKinG_Panel locking_panel = new LOcKinG_Panel();
		JScrollPane _scroll = new JScrollPane(LOOPINGS_panel.lp);
		_scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		onglet.add("Screen", _scroll);
									
	
		this.getContentPane().add(onglet);	
		this.setVisible(true);

	}
}
