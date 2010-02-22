package interfazaod.actions;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ProgressMonitor;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import main.AODBuilder;
import main.AODBuilderRunner;

import org.apache.log4j.Logger;

import util.ExceptionUtil;
import util.Log4jConfigurator;
import util.ResourceLoader;
import analyser.SentenceAnalizer;
import beans.aodprofile.AODProfileBean;
import constants.Constants;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI
 * Builder, which is free for non-commercial use. If Jigloo is being used
 * commercially (ie, by a corporation, company or business for any purpose
 * whatever) then you should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details. Use of Jigloo implies
 * acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN
 * PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR
 * ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class FileSelector extends javax.swing.JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8682306540428689255L;


	{
		// Set Look & Feel
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
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
	private JLabel jLabel3;
	private String fileName;
	private static FileSelector fileSelector;
	Object[] aodClasses;
	Object[] aodAspects;
	Logger logger = Log4jConfigurator.getLogger();

	public static FileSelector getInstance() {
		if (fileSelector == null) {
			fileSelector = new FileSelector();
		}
		return fileSelector;
	}

	protected FileSelector() {
		super();
		initGUI();
	}

	public Welcome getWelcome() {
		return this.welcome;
	}

	public void setWelcome(Welcome welcome) {
		this.welcome = welcome;
	}

	private void initGUI() {
		try {
			logger.info("Starting FileSelector initGUI...");
			this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			this.setTitle("Use Case File Selector");
			JFrame.setDefaultLookAndFeelDecorated(true);

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
					jLabel1
							.setText("Select the XMI file you want to use as Use Case input");
					jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
					jLabel1.setBounds(67, 18, 318, 12);
					jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11));
				}
				{
					jLabel3 = new JLabel();
					jPanelTop.add(jLabel3);
					jLabel3.setBounds(10, 11, 41, 41);
					jLabel3.setIcon(new ImageIcon(getClass().getClassLoader()
							.getResource("images/Settings.png")));
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
					jButtonPrevious.setLayout(null);
					jButtonPrevious.setText("Previous");
					jButtonPrevious.setBounds(15, 56, 101, 28);
					jButtonPrevious
							.setIcon(new ImageIcon(getClass().getClassLoader()
									.getResource("images/001_23.png")));
					jButtonPrevious.setPreferredSize(new java.awt.Dimension(93,
							28));
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
					jNext.setIcon(new ImageIcon(getClass().getClassLoader()
							.getResource("images/001_21.png")));
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
					jCancel.setIcon(new ImageIcon(getClass().getClassLoader()
							.getResource("images/block_16.png")));
					jCancel.setPreferredSize(new java.awt.Dimension(93, 28));
					jCancel.addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent evt) {
							jCancelMouseClicked(evt);
						}
					});
				}
			}
			this.setIconImage(new ImageIcon(getClass().getClassLoader()
					.getResource("images/001_16.png")).getImage());
			pack();
			this.setSize(406, 225);
			this.setLocationRelativeTo(null);
			this.setFocusTraversalKeysEnabled(false);
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			this.setVisible(true);
			logger.info("FileSelector initGUI ended.");
		} catch (Exception e) {
			logger.error(ExceptionUtil.getTrace(e));
		}
	}

	private void jButtonPreviousMouseClicked(MouseEvent evt) {
		welcome.setVisible(true);
		this.setVisible(false);
	}

	private void jButtonBrowseMouseClicked(MouseEvent evt) {
		logger.info("Choosing input file...");
		JFileChooser fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"XMI Files", "xmi");
		fileChooser.setFileFilter(filter);
		int seleccion = fileChooser.showOpenDialog(this);

		if (seleccion == JFileChooser.APPROVE_OPTION) {
			File useCaseFile = fileChooser.getSelectedFile();
			jTextField1.setText(useCaseFile.getName());
			fileName = useCaseFile.getAbsolutePath();
			logger.info("File choosen: " + fileName);
		}
	}

	private void jNextMouseClicked(MouseEvent evt) {
		//Disable buttons
		disableButtons();
		
		//Create and set up the content pane.
        JPanel newContentPane = new MyProgressMonitor(fileName);
        jPanelTop = newContentPane;

	}

	private void disableButtons() {
		this.jButtonPrevious.setEnabled(false);
		this.jCancel.setEnabled(false);
		this.jNext.setEnabled(false);
		this.jButtonBrowse.setEnabled(false);
	}

	private void enableButtons() {
		this.jButtonPrevious.setEnabled(true);
		this.jCancel.setEnabled(true);
		this.jNext.setEnabled(true);
		this.jButtonBrowse.setEnabled(true);
	}

	public void endLoading() {
		logger.info("End loading.");
		ProposedSolution proposedSolution = ProposedSolution.getInstance(aodClasses);
		proposedSolution.setPreviousFrame(this);
		proposedSolution.setVisible(true);
		this.setVisible(false);
	}

	private void jCancelMouseClicked(MouseEvent evt) {
		Integer value = JOptionPane.showConfirmDialog(null,
				"Are you sure you want to quit?", "Confirm exit",
				JOptionPane.YES_NO_OPTION);
		if (value == JOptionPane.OK_OPTION) {
			logger.info("Exiting FileSelector.");
			this.dispose();
		}
	}

	public String getFileName() {
		return fileName;
	}


	public class MyProgressMonitor extends JPanel implements ActionListener,
			PropertyChangeListener {

		/**
		 * 
		 */
		private static final long serialVersionUID = 6301668128751298649L;
		private ProgressMonitor progressMonitor;
		private AODBuilderRunner task;
		private String fileName;

		public MyProgressMonitor(String fileName) {
			super(new BorderLayout());
			this.fileName = fileName;
			load();
		}

		/**
		 * Invoked when the user presses the start button.
		 */
		public void load() {
			progressMonitor = new ProgressMonitor(MyProgressMonitor.this,
					"Loading solution...", "", 0, 100);
			progressMonitor.setProgress(0);
			task = AODBuilderRunner.getInstance(fileName);
			task.addPropertyChangeListener(this);

			 setParserResource(SentenceAnalizer.PARSER_ENGLISH);
			
			task.execute();
		}

		@SuppressWarnings("deprecation")
		private void setParserResource(String resourceName) {
			URL nativeURL;
			try {
				nativeURL = org.eclipse.core.runtime.Platform.resolve(
								ResourceLoader.getResourceURL(resourceName));
				Constants.PARSER_ENGLISH_RESOURCE_URL = nativeURL;
			} catch (IOException e) {
				logger.error(ExceptionUtil.getTrace(e));
			}
		}

		/**
		 * Invoked when task's progress property changes.
		 */
		public void propertyChange(PropertyChangeEvent evt) {
			if ("progress" == evt.getPropertyName()) {
				int progress = (Integer) evt.getNewValue();
				progressMonitor.setProgress(progress);
				String message = String.format("Completed %d%%.\n", progress);
				progressMonitor.setNote(message);
				if (progressMonitor.isCanceled() || task.isDone()) {
					Toolkit.getDefaultToolkit().beep();
					if (progressMonitor.isCanceled()) {
						task.cancel(true);
						AODBuilderRunner.destroy();
						enableButtons();
					}
					else{
						Map<String, AODProfileBean> map = AODBuilder.getInstance().getMap();
						if (map!=null && map.values().size()>0)
							aodClasses = map.values().toArray();
						AODBuilderRunner.destroy();
						enableButtons();
						endLoading();
					}
				}
			}

		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// load();

		}
	}

}
