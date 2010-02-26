package interfazaod.gui.frames;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import org.apache.log4j.Logger;

import aodbuilder.util.ExceptionUtil;
import aodbuilder.util.Log4jConfigurator;








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
public class Welcome extends javax.swing.JFrame {

	private JPanel jPanelTop;

	private JPanel jPanelBottom;
	private JButton jButtonExit;
	private JLabel jLabel3;
	private JLabel jLabel2;
	private JButton jButtonNext;
	private JLabel jLabel1;
	private FileSelector fileSelector;
	private static Welcome welcome;
	public static Welcome getInstance() {
		if (welcome==null)
			welcome = new Welcome();
		return welcome;
	}
	
	Logger logger;

	{
		//Set Look & Feel
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	protected Welcome() {
		super();
		try {
			Log4jConfigurator.getLog4JProperties();
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger = Log4jConfigurator.getLogger();
		initGUI();
	}
	
	private void initGUI() {
		try {
			logger.info("Init Welcome GUI...");
			
			this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			this.setTitle("AOD Builder");
			JFrame.setDefaultLookAndFeelDecorated(true);
			this.setResizable(false);
			{
				jPanelTop = new JPanel();
				getContentPane().add(jPanelTop, BorderLayout.NORTH);
				jPanelTop.setPreferredSize(new java.awt.Dimension(435, 93));
				jPanelTop.setLayout(null);
				{
					jLabel3 = new JLabel();
					jPanelTop.add(jLabel3);
					jLabel3.setPreferredSize(new java.awt.Dimension(51, 45));
					jLabel3.setVisible(true);
					jLabel3.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/Settings.png")));
					jLabel3.setBounds(73, 5, 51, 45);
				}
				{
					jLabel1 = new JLabel();
					jPanelTop.add(jLabel1);
					jLabel1.setText("Welcome to the AOD Builder tool");
					jLabel1.setPreferredSize(new java.awt.Dimension(234, 37));
					jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
					jLabel1.setFont(new java.awt.Font("Arial",1,12));
					jLabel1.setBounds(129, 9, 234, 37);
				}
				{
					jLabel2 = new JLabel();
					jPanelTop.add(jLabel2);
					jLabel2.setText("This wizard will guide you through the design process of your system");
					jLabel2.setBounds(54, 68, 329, 14);
				}
			}
			{
				jPanelBottom = new JPanel();
				FlowLayout jPanelBottomLayout = new FlowLayout();
				jPanelBottom.setLayout(jPanelBottomLayout);
				getContentPane().add(jPanelBottom, BorderLayout.CENTER);
				jPanelBottom.setPreferredSize(new java.awt.Dimension(433, 61));
				{
					jButtonExit = new JButton();
					jPanelBottom.add(jButtonExit);
					jButtonExit.setText("Exit");
					jButtonExit.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/block_16.png")));
					jButtonExit.setPreferredSize(new java.awt.Dimension(93, 28));
					jButtonExit.addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent evt) {
							jButtonExitMouseClicked(evt);
						}
					});
				}
				{
					jButtonNext = new JButton();
					jPanelBottom.add(jButtonNext);
					jButtonNext.setText("Next");
					jButtonNext.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/001_21.png")));
					jButtonNext.setPreferredSize(new java.awt.Dimension(93, 28));
					jButtonNext.addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent evt) {
							jButtonNextMouseClicked(evt);
						}
					});
				}
			}
			this.setFocusCycleRoot(false);
			this.setIconImage(new ImageIcon(getClass().getClassLoader().getResource("images/001_17.png")).getImage());
			pack();
			setLocationRelativeTo(null);
			setVisible(true);

			logger.info("Welcome initGUI ended.");
		} catch (Exception e) {
			logger.error(ExceptionUtil.getTrace(e));
		}
	}
	
	private void jButtonExitMouseClicked(MouseEvent evt) {
		Integer value = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "Confirm exit", JOptionPane.YES_NO_OPTION);
		if (value == JOptionPane.OK_OPTION) {
			logger.info("Exiting program.");
			this.dispose();
		}
	}
	
	private void jButtonNextMouseClicked(MouseEvent evt) {
		fileSelector = FileSelector.getInstance();
		fileSelector.setWelcome(this);
		fileSelector.setVisible(true);
		this.setVisible(false);
	}

}
