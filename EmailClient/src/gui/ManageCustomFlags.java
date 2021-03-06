package gui;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.BorderLayout;

import javax.swing.JScrollPane;
import javax.swing.JTable;

import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;


public class ManageCustomFlags {

	private JFrame frmManageCustomFlags;
	private JTable table;
	private JPanel mainPanel;

	/**
	 * Create the application.
	 */
	public ManageCustomFlags(HashMap<String, String> customFlags) 
	{
		initialize(customFlags);
	}
	

	public JTable createTable(HashMap<String, String> flags)
	{
		//create table headings
		String[] columnNames = {"Custom Flag", "If contains"}; 

		Object[][] rowData = null;
		
		//add the data to the rows from the custom flags hash map
		if(flags == null)
		{
			rowData = new Object[0][2];
		}
		else
		{
			rowData = new Object[flags.size()][2];
			int count = 0;
			Iterator iterator = flags.entrySet().iterator();
			while(iterator.hasNext())
			{
				HashMap.Entry<String, String> pair = (HashMap.Entry<String,String>)iterator.next(); 
				rowData[count][0] = pair.getKey();
				rowData[count][1] = pair.getValue();
				count++;
			}
		}
					
		//create the table using the table headings and input row data
		DefaultTableModel model = new DefaultTableModel(rowData, columnNames);
		table = new JTable( model );
		table.setBorder(new EmptyBorder(5, 5, 5, 5));
		table.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		return table;
	}
	
	public void reproduceTable(HashMap<String, String> flags)
	{
		//recreate the table with the new flags
		mainPanel.remove(1);
		JTable table = createTable(flags);
		JScrollPane scrollPane2 = new JScrollPane(table);
		mainPanel.add(scrollPane2);
		mainPanel.revalidate();
		mainPanel.repaint();	
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(HashMap<String, String> flags) {
		frmManageCustomFlags = new JFrame();
		frmManageCustomFlags.setTitle("Manage Custom Flags");
		frmManageCustomFlags.setBounds(100, 100, 450, 300);
		frmManageCustomFlags.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmManageCustomFlags.setVisible(true);
		
		
		JPanel panel = new JPanel();
		mainPanel = new JPanel();
		
		frmManageCustomFlags.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));
		
		JPanel buttonPanel = new JPanel();
		
		GridBagLayout gbl_buttonPanel = new GridBagLayout();
		gbl_buttonPanel.columnWidths = new int[]{0, 0};
		gbl_buttonPanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_buttonPanel.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_buttonPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		buttonPanel.setLayout(gbl_buttonPanel);
			
		JButton btnDelete = new JButton("Delete");
		btnDelete.setFont(new Font("Tahoma", Font.PLAIN, 16));
		GridBagConstraints gbc_btnDelete = new GridBagConstraints();
		gbc_btnDelete.insets = new Insets(0, 10, 5, 10);
		gbc_btnDelete.gridx = 0;
		gbc_btnDelete.gridy = 2;
		buttonPanel.add(btnDelete, gbc_btnDelete);
		btnDelete.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					DefaultTableModel model = (DefaultTableModel) table.getModel();
					int row = table.getSelectedRow();
					if(row == -1)
					{
						JOptionPane.showMessageDialog (null, "You have not selected a row to delete!", "Oops", JOptionPane.INFORMATION_MESSAGE);
					}
					else
					{
						String flagName = (String) table.getModel().getValueAt(row, 0);
						model.removeRow(table.convertRowIndexToModel(row));
						flags.remove(flagName);
						
						table.clearSelection();	
					}
				}
			}
		);
		
		JButton btnClose = new JButton("Close");
		btnClose.setFont(new Font("Tahoma", Font.PLAIN, 16));
		GridBagConstraints gbc_btnClose = new GridBagConstraints();
		gbc_btnClose.insets = new Insets(0, 0, 5, 0);
		gbc_btnClose.gridx = 0;
		gbc_btnClose.gridy = 7;
		buttonPanel.add(btnClose, gbc_btnClose);
		btnClose.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					frmManageCustomFlags.dispose();		
				}
			}
		);
		
		JButton btnAdd = new JButton("Add");
		btnAdd.setFont(new Font("Tahoma", Font.PLAIN, 16));
		GridBagConstraints gbc_btnAdd = new GridBagConstraints();
		gbc_btnAdd.insets = new Insets(0, 0, 5, 0);
		gbc_btnAdd.gridx = 0;
		gbc_btnAdd.gridy = 1;
		buttonPanel.add(btnAdd, gbc_btnAdd);
		btnAdd.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					JFrame frame = new JFrame();
					frame.setSize(250,150);
					frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
					GridBagLayout gridBagLayout = new GridBagLayout();
					gridBagLayout.columnWidths = new int[]{0, 0, 0};
					gridBagLayout.rowHeights = new int[]{0, 0, 0, 0};
					gridBagLayout.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
					gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
					frame.getContentPane().setLayout(gridBagLayout);
					
					JLabel lblNameOfFlag = new JLabel("Name of flag:");
					lblNameOfFlag.setFont(new Font("Tahoma", Font.PLAIN, 16));
					GridBagConstraints gbc_lblNameOfFlag = new GridBagConstraints();
					gbc_lblNameOfFlag.anchor = GridBagConstraints.EAST;
					gbc_lblNameOfFlag.insets = new Insets(10, 10, 5, 5);
					gbc_lblNameOfFlag.gridx = 0;
					gbc_lblNameOfFlag.gridy = 0;
					frame.getContentPane().add(lblNameOfFlag, gbc_lblNameOfFlag);
					
					JTextField name = new JTextField();
					name.setFont(new Font("Tahoma", Font.PLAIN, 16));
					GridBagConstraints gbc_name = new GridBagConstraints();
					gbc_name.insets = new Insets(10, 0, 5, 10);
					gbc_name.fill = GridBagConstraints.HORIZONTAL;
					gbc_name.gridx = 1;
					gbc_name.gridy = 0;
					frame.getContentPane().add(name, gbc_name);
					name.setColumns(10);
					
					JLabel lblIfContains = new JLabel("If contains:");
					lblIfContains.setFont(new Font("Tahoma", Font.PLAIN, 16));
					GridBagConstraints gbc_lblIfContains = new GridBagConstraints();
					gbc_lblIfContains.anchor = GridBagConstraints.EAST;
					gbc_lblIfContains.insets = new Insets(0, 0, 5, 5);
					gbc_lblIfContains.gridx = 0;
					gbc_lblIfContains.gridy = 1;
					frame.getContentPane().add(lblIfContains, gbc_lblIfContains);
					
					JTextField contains = new JTextField();
					contains.setFont(new Font("Tahoma", Font.PLAIN, 16));
					GridBagConstraints gbc_contains = new GridBagConstraints();
					gbc_contains.insets = new Insets(0, 0, 5, 10);
					gbc_contains.fill = GridBagConstraints.HORIZONTAL;
					gbc_contains.gridx = 1;
					gbc_contains.gridy = 1;
					frame.getContentPane().add(contains, gbc_contains);
					contains.setColumns(10);
					
					JButton btnAdd = new JButton("Add");
					btnAdd.setFont(new Font("Tahoma", Font.PLAIN, 16));
					GridBagConstraints gbc_btnAdd = new GridBagConstraints();
					gbc_btnAdd.insets = new Insets(0, 0, 10, 5);
					gbc_btnAdd.gridx = 0;
					gbc_btnAdd.gridy = 2;
					frame.getContentPane().add(btnAdd, gbc_btnAdd);
					btnAdd.addActionListener(new ActionListener()
						{
							@Override
							public void actionPerformed(ActionEvent e) 
							{
								String customFlag = name.getText();
								String ifContains = contains.getText();
								if(flags.containsValue(ifContains))
								{
									JOptionPane.showMessageDialog (null, "There is already a flag that accounts for this!", "Oops", JOptionPane.INFORMATION_MESSAGE);
								}
								else if(flags.containsKey(customFlag))
								{
									JOptionPane.showMessageDialog (null, "There is already a flag with that name!", "Oops", JOptionPane.INFORMATION_MESSAGE);
								}
								else
								{
									flags.put(customFlag, ifContains);
									reproduceTable(flags);
									frame.dispose();
								}
							}
						}
					);
					
					JButton btnClose = new JButton("Close");
					btnClose.setFont(new Font("Tahoma", Font.PLAIN, 16));
					GridBagConstraints gbc_btnClose = new GridBagConstraints();
					gbc_btnClose.insets = new Insets(0, 0, 10, 0);
					gbc_btnClose.gridx = 1;
					gbc_btnClose.gridy = 2;
					frame.getContentPane().add(btnClose, gbc_btnClose);
					btnClose.addActionListener(new ActionListener()
						{
							@Override
							public void actionPerformed(ActionEvent e) 
							{
								frame.dispose();
							}
						}
					);
				}
			}
		);
		
		
		
		table = createTable(flags);
		JScrollPane scrollPane = new JScrollPane(table);
		mainPanel.setLayout(new BorderLayout(0, 0));
		mainPanel.add(buttonPanel, BorderLayout.EAST);
		mainPanel.add(scrollPane);
		panel.add(mainPanel, BorderLayout.CENTER);
	}

}
