package interfazaod.actions;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;


/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class FileSelector extends javax.swing.JFrame {

	{
		//Set Look & Feel
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	private JPanel jPanelTop;
	private JButton jCancel;
	private JButton jNext;
	private JButton jButtonPrevious;
	private JPanel jPanelButtons;
	private JPanel jPanelBottom;
	private JButton jButtonBrowse;
	private JTextField jTextField1;
	private JLabel jLabel2;
	private JLabel jLabel1;
	private Welcome welcome;
	private ProposedSolution proposedSolution;
	private JLabel jLabel3;
	private static FileSelector fileSelector;
	
	public static FileSelector getInstance () {
		if (fileSelector==null) {
			fileSelector = new FileSelector();
		}
		return fileSelector;
	}
	protected FileSelector() {
		super();
		initGUI();
	}
	
	public Welcome getWelcome () {
		return this.welcome;
	}
	
	public void setWelcome (Welcome welcome) {
		this.welcome = welcome;
	}
	
	

	private void initGUI() {
		try {
			this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			this.setTitle("Use Case File Selector");
			this.setDefaultLookAndFeelDecorated(true);
			
			this.setResizable(false);
			{
				jPanelTop = new JPanel();
				jPanelTop.setLayout(null);
				getContentPane().add(jPanelTop, BorderLayout.NORTH);
				jPanelTop.setPreferredSize(new java.awt.Dimension(400, 63));
				{
					jLabel1 = new JLabel();
					jPanelTop.add(jLabel1, "0, 1, 3, 1");
					jLabel1.setLayout(null);
					jLabel1.setText("Select the XMI file you want to use as Use Case input");
					jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
					jLabel1.setBounds(67, 18, 318, 12);
					jLabel1.setFont(new java.awt.Font("Tahoma",1,11));
				}
				{
					jLabel3 = new JLabel();
					jPanelTop.add(jLabel3);
					jLabel3.setBounds(10, 11, 41, 41);
					jLabel3.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/Settings.png")));
					jLabel3.setSize(51, 45);
				}
			}
			{
				jPanelBottom = new JPanel();
				getContentPane().add(jPanelBottom, BorderLayout.CENTER);
				jPanelBottom.setPreferredSize(new java.awt.Dimension(394, 188));
				jPanelBottom.setLayout(null);
				{
					jLabel2 = new JLabel();
					jPanelBottom.add(jLabel2);
					jLabel2.setText("File Name: ");
					jLabel2.setBounds(15, 12, 53, 14);
				}
				{
					jTextField1 = new JTextField();
					jPanelBottom.add(jTextField1);
					jTextField1.setBounds(72, 9, 235, 21);
				}
				{
					jButtonBrowse = new JButton();
					jPanelBottom.add(jButtonBrowse);
					jButtonBrowse.setText("Browse");
					jButtonBrowse.setBounds(313, 9, 77, 21);
					jButtonBrowse.addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent evt) {
							jButtonBrowseMouseClicked(evt);
						}
					});
				}
			}
			{
				jPanelButtons = new JPanel();
				getContentPane().add(jPanelButtons, BorderLayout.SOUTH);
				jPanelButtons.setPreferredSize(new java.awt.Dimension(400, 67));
				{
					jButtonPrevious = new JButton();
					jPanelButtons.add(jButtonPrevious);
					BoxLayout jButtonPreviousLayout = new BoxLayout(jButtonPrevious, javax.swing.BoxLayout.X_AXIS);
					jButtonPrevious.setLayout(null);
					jButtonPrevious.setText("Previous");
					jButtonPrevious.setBounds(15, 56, 101, 28);
					jButtonPrevious.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/001_23.png")));
					jButtonPrevious.setPreferredSize(new java.awt.Dimension(93, 28));
					jButtonPrevious.addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent evt) {
							jButtonPreviousMouseClicked(evt);
						}
					});
				}
				{
					jNext = new JButton();
					jPanelButtons.add(jNext);
					jNext.setText("Next");
					jNext.setBounds(154, 56, 101, 28);
					jNext.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/001_21.png")));
					jNext.setPreferredSize(new java.awt.Dimension(93, 28));
					jNext.addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent evt) {
							jNextMouseClicked(evt);
						}
					});
				}
				{
					jCancel = new JButton();
					jPanelButtons.add(jCancel);
					jCancel.setText("Quit");
					jCancel.setBounds(289, 56, 101, 28);
					jCancel.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/block_16.png")));
					jCancel.setPreferredSize(new java.awt.Dimension(93, 28));
					jCancel.addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent evt) {
							jCancelMouseClicked(evt);
						}
					});
				}
			}
			this.setIconImage(new ImageIcon(getClass().getClassLoader().getResource("images/001_16.png")).getImage());
			pack();
			this.setSize(406, 225);
			this.setLocationRelativeTo(null);
			this.setFocusTraversalKeysEnabled(false);
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			this.setVisible(true);
	} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void jButtonPreviousMouseClicked(MouseEvent evt) {
		welcome.setVisible(true);
		this.setVisible(false);
		//TODO add your code for jButtonPrevious.mouseClicked
	}
	
	private void jButtonBrowseMouseClicked(MouseEvent evt) {
		JFileChooser fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("XMI Files","xmi");
		fileChooser.setFileFilter(filter);
		int seleccion = fileChooser.showOpenDialog(this);
		
		if (seleccion == JFileChooser.APPROVE_OPTION)
		{
		   File useCaseFile = fileChooser.getSelectedFile();
		   jTextField1.setText(useCaseFile.getName());

		}
	}
	
	private void jNextMouseClicked(MouseEvent evt) {
		proposedSolution = ProposedSolution.getInstance();
		proposedSolution.setFileSelector(this);
		proposedSolution.setVisible(true);
		this.setVisible(false);
	}

	private void jCancelMouseClicked(MouseEvent evt) {
		Integer value = JOptionPane.showConfirmDialog(null, "Are you sure you want to quit?", "Confirm exit", JOptionPane.YES_NO_OPTION);
		if (value == JOptionPane.OK_OPTION) {
			this.dispose();
			System.exit(NORMAL);
		}
	}

}
