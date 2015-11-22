package gui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;

import fd.FileDownloader;
import fd.FileUrls;

import java.awt.BorderLayout;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import java.awt.GridBagLayout;

import javax.swing.JLabel;

import java.awt.GridBagConstraints;

import javax.swing.JTextField;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.table.DefaultTableModel;

/**
 * The main method class containing the main window gui. All of the action is triggered from this window
 * @author Ashley Wyatt
 *
 */

public class FDMain {

	private JFrame frame;
	private JTable table;
	private JTextField urlTF;
	private JTextField locationTF;
	private JTextField threadsTF;
	private DefaultTableModel model;
	private ArrayList<String> fileTypes;
	private JPanel mainPanel;
	ArrayList<String> sources;

	/**
	 * Launch the application.
	 */
	
	public static void main(String[] args) {
			SwingUtilities.invokeLater(new Runnable(){
				public void run()
				{
					try {
						FDMain window = new FDMain();
						window.run();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
	}
		

	/**
	 * Create the application.
	 * @throws IOException 
	 */
	
	public FDMain() throws IOException 
	{
		//add sensible default filter to avoid dodgy links
		fileTypes = new ArrayList<String>();
		fileTypes.add("html");
		fileTypes.add("jpg");
		fileTypes.add("png");
		fileTypes.add("pdf");
	}
	
	/**
	 * Create the initial empty JTable of downloads
	 */

	public void constructTable()
	{
		//make the table uneditable
		String[] columnNames = {"Name", "Type", "Size", "Status"};
		Object[][] rowData = null;
		
		model = new DefaultTableModel(rowData, columnNames) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		table = new JTable(model);
		
		table.getColumn("Status").setCellRenderer(new ProgressRenderer(0,100));
		
	}
	
	/**
	 * Completely wipe the JTable containing downloads and create a new empty JTable
	 */
	
	public void reconstructTable()
	{
		mainPanel.remove(1);
		constructTable();
		JScrollPane scrollPane = new JScrollPane(table);
		mainPanel.add(scrollPane);
		mainPanel.revalidate();
		mainPanel.repaint();	
	}
	
	/**
	 * Add listeners to the buttons
	 * @param btnFilter 
	 * @param btnBrowse 
	 * @param btnClearListing
	 * @param btnGo
	 */
	
	public void addListeners(JButton btnFilter, JButton btnBrowse, JButton btnClearListing, JButton btnGo)
	{
		//open the extensions filter class where the allowed extensions can be modified
		btnFilter.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					ExtensionsFilter getExtensions = new ExtensionsFilter();
					getExtensions.run(fileTypes);
				}
			}
		);
		
		//Opens up a JFileChooser and lets user to select a file location
		btnBrowse.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					//create file chooser
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					int returnValue = fileChooser.showOpenDialog(null);
					if (returnValue == JFileChooser.APPROVE_OPTION) 
					{
						//get the selected file
						File file = fileChooser.getSelectedFile();
						String folder = file.getAbsolutePath();
						locationTF.setText(folder);
					}
				}
			}
		);
		
		// Resets the JTable
		btnClearListing.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					reconstructTable();
				}
			}
		);

		//Proceeds to download the files from the url
		btnGo.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					//get input url
					String url = urlTF.getText();
					
					//get input file location
					String location = locationTF.getText();
					
					//get input number of threads
					int threads = Integer.parseInt(threadsTF.getText());
					
					//get the list of links
					FileUrls fu = new FileUrls(url, fileTypes);
		
					try {
						sources = fu.fetchImageSources();
						System.out.println("Got file links! There are " + sources.size());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						System.out.println("Error whilst getting file links");
					}
					
					for(int i = 0; i < sources.size(); i++)
					{
						System.out.println(sources.get(i));
					}
					
					
					Runnable download = new Runnable() {
						public void run()
						{
							//actually download the files (whilst updating table)
							FileDownloader fd = new FileDownloader(model, threads);
							fd.downloadFiles(location, sources);
							JOptionPane.showMessageDialog (null, "Downloading Complete", "Message", JOptionPane.INFORMATION_MESSAGE);
							fd.shutDown();
						}
					};
		
					Thread worker = new Thread(download);
					worker.start();
					
				}
			}
		);
	}

	/**
	 * Initialize the contents of the frame. Generated mostly by WindowBuilder
	 * @throws IOException 
	 */
	
	public void run() throws IOException 
	{	
		frame = new JFrame();
		frame.setSize(600,400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		mainPanel = new JPanel();
		frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
				
		constructTable();
		mainPanel.setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		mainPanel.add(panel, BorderLayout.NORTH);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0, 0, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE, 0.0};
		panel.setLayout(gbl_panel);
		
		JLabel lblUrl = new JLabel("URL:");
		GridBagConstraints gbc_lblUrl = new GridBagConstraints();
		gbc_lblUrl.insets = new Insets(10, 10, 5, 5);
		gbc_lblUrl.anchor = GridBagConstraints.EAST;
		gbc_lblUrl.gridx = 0;
		gbc_lblUrl.gridy = 0;
		panel.add(lblUrl, gbc_lblUrl);
		
		urlTF = new JTextField();
		GridBagConstraints gbc_urlTF = new GridBagConstraints();
		gbc_urlTF.gridwidth = 3;
		gbc_urlTF.insets = new Insets(10, 0, 5, 10);
		gbc_urlTF.fill = GridBagConstraints.HORIZONTAL;
		gbc_urlTF.gridx = 1;
		gbc_urlTF.gridy = 0;
		panel.add(urlTF, gbc_urlTF);
		urlTF.setColumns(10);
		
		JLabel lblLocation = new JLabel("Location:");
		GridBagConstraints gbc_lblLocation = new GridBagConstraints();
		gbc_lblLocation.anchor = GridBagConstraints.EAST;
		gbc_lblLocation.insets = new Insets(0, 10, 5, 5);
		gbc_lblLocation.gridx = 0;
		gbc_lblLocation.gridy = 2;
		panel.add(lblLocation, gbc_lblLocation);
		
		JLabel lblNoThreads = new JLabel("No. Threads:");
		GridBagConstraints gbc_lblNoThreads = new GridBagConstraints();
		gbc_lblNoThreads.anchor = GridBagConstraints.EAST;
		gbc_lblNoThreads.insets = new Insets(0, 10, 5, 5);
		gbc_lblNoThreads.gridx = 0;
		gbc_lblNoThreads.gridy = 1;
		panel.add(lblNoThreads, gbc_lblNoThreads);
		
		locationTF = new JTextField();
		GridBagConstraints gbc_LocationTF = new GridBagConstraints();
		gbc_LocationTF.gridwidth = 2;
		gbc_LocationTF.insets = new Insets(0, 0, 5, 10);
		gbc_LocationTF.fill = GridBagConstraints.HORIZONTAL;
		gbc_LocationTF.gridx = 1;
		gbc_LocationTF.gridy = 2;
		panel.add(locationTF, gbc_LocationTF);
		locationTF.setColumns(10);
		
		threadsTF = new JTextField();
		GridBagConstraints gbc_threadsTF = new GridBagConstraints();
		gbc_threadsTF.gridwidth = 3;
		gbc_threadsTF.insets = new Insets(0, 0, 5, 10);
		gbc_threadsTF.fill = GridBagConstraints.HORIZONTAL;
		gbc_threadsTF.gridx = 1;
		gbc_threadsTF.gridy = 1;
		panel.add(threadsTF, gbc_threadsTF);
		threadsTF.setColumns(10);
		
		JButton btnBrowse = new JButton("Browse");
		GridBagConstraints gbc_btnBrowse = new GridBagConstraints();
		gbc_btnBrowse.insets = new Insets(0, 0, 5, 10);
		gbc_btnBrowse.gridx = 3;
		gbc_btnBrowse.gridy = 2;
		panel.add(btnBrowse, gbc_btnBrowse);
	
		
		JButton btnClearListing = new JButton("Clear Listing");
		GridBagConstraints gbc_btnClearListing = new GridBagConstraints();
		gbc_btnClearListing.insets = new Insets(0, 10, 10, 5);
		gbc_btnClearListing.gridx = 0;
		gbc_btnClearListing.gridy = 3;
		panel.add(btnClearListing, gbc_btnClearListing);
		
		JButton btnFilter = new JButton("Filter");
		GridBagConstraints gbc_btnFilter = new GridBagConstraints();
		gbc_btnFilter.insets = new Insets(0, 10, 10, 5);
		gbc_btnFilter.gridx = 2;
		gbc_btnFilter.gridy = 3;
		panel.add(btnFilter, gbc_btnFilter);
		
		JButton btnGo = new JButton("Go");
		GridBagConstraints gbc_btnGo = new GridBagConstraints();
		gbc_btnGo.insets = new Insets(0, 0, 10, 10);
		gbc_btnGo.gridx = 3;
		gbc_btnGo.gridy = 3;
		panel.add(btnGo, gbc_btnGo);
		
		JScrollPane scrollPane = new JScrollPane(table);
		mainPanel.add(scrollPane);
		addListeners(btnFilter, btnBrowse, btnClearListing, btnGo);
		frame.setVisible(true);
	}
}
