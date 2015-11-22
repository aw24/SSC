package gui;

import javax.swing.JFrame;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import java.awt.GridLayout;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import java.awt.Font;
import java.awt.GridBagLayout;

import javax.swing.JTextField;

import java.awt.GridBagConstraints;

import javax.swing.JLabel;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class ExtensionsFilter {

	private JFrame frame;
	private JTextField textField;
	private JTable table;
	private JPanel mainPanel;

	public JTable createTable(ArrayList<String>  extensions)
	{
		//create table headings
		String[] columnNames = {"Allowed Extensions"}; 

		Object[][] rowData = null;
		
		//add the data to the rows from the custom flags hash map
		if(extensions == null)
		{
			rowData = new Object[0][1];
		}
		else
		{
			rowData = new Object[extensions.size()][1];
			for(int i = 0; i < extensions.size();i++)
			{
				rowData[i][0] = extensions.get(i);
			}
		}
					
		//create the table using the table headings and input row data
		DefaultTableModel model = new DefaultTableModel(rowData, columnNames);
		table = new JTable( model );
		table.setBorder(new EmptyBorder(5, 5, 5, 5));
		table.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		return table;
	}
	
	public void reproduceTable(ArrayList<String> extensions)
	{
		//recreate the table with the new flags
		mainPanel.remove(1);
		JTable table = createTable(extensions);
		JScrollPane scrollPane2 = new JScrollPane(table);
		mainPanel.add(scrollPane2);
		mainPanel.revalidate();
		mainPanel.repaint();	
	}

	/**
	 * Initialize the contents of the frame.
	 */
	public void run(ArrayList<String> extensions) 
	{
		frame = new JFrame();
		frame.setSize(300, 160);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		mainPanel = new JPanel();
		frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
		mainPanel.setLayout(new GridLayout(1, 0, 0, 0));
		
		
		JPanel panel = new JPanel();
		mainPanel.add(panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		table = createTable(extensions);
		JScrollPane scrollPane = new JScrollPane(table);
		mainPanel.add(scrollPane);
		
		JLabel lblExtension = new JLabel("Extension: ");
		GridBagConstraints gbc_lblExtension = new GridBagConstraints();
		gbc_lblExtension.insets = new Insets(10, 10, 5, 5);
		gbc_lblExtension.anchor = GridBagConstraints.EAST;
		gbc_lblExtension.gridx = 0;
		gbc_lblExtension.gridy = 0;
		panel.add(lblExtension, gbc_lblExtension);
		
		textField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(10, 0, 5, 10);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 0;
		panel.add(textField, gbc_textField);
		textField.setColumns(10);
		
		JButton btnAdd = new JButton("Add");
		GridBagConstraints gbc_btnAdd = new GridBagConstraints();
		gbc_btnAdd.insets = new Insets(0, 0, 5, 0);
		gbc_btnAdd.gridwidth = 2;
		gbc_btnAdd.gridx = 0;
		gbc_btnAdd.gridy = 1;
		panel.add(btnAdd, gbc_btnAdd);
		btnAdd.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					String newExtension = textField.getText();
					if(newExtension.equals(""))
					{
						JOptionPane.showMessageDialog (null, "Not a valid extension", "Oops", JOptionPane.INFORMATION_MESSAGE);
					}
					else if(extensions.contains(newExtension))
					{
						JOptionPane.showMessageDialog (null, "There is already an extension that accounts for this!", "Oops", JOptionPane.INFORMATION_MESSAGE);
					}
					else
					{
						extensions.add(newExtension);
						reproduceTable(extensions);
					}
				}
			}
		);
		
		JButton btnDelete = new JButton("Delete");
		GridBagConstraints gbc_btnDelete = new GridBagConstraints();
		gbc_btnDelete.gridwidth = 2;
		gbc_btnDelete.insets = new Insets(0, 0, 5, 5);
		gbc_btnDelete.gridx = 0;
		gbc_btnDelete.gridy = 2;
		panel.add(btnDelete, gbc_btnDelete);
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
						String extension = (String) table.getModel().getValueAt(row, 0);
						model.removeRow(table.convertRowIndexToModel(row));
						extensions.remove(extension);
						
						table.clearSelection();	
					}
				}
			}
		);
		
		JButton btnClose = new JButton("Close");
		GridBagConstraints gbc_btnClose = new GridBagConstraints();
		gbc_btnClose.gridwidth = 2;
		gbc_btnClose.insets = new Insets(0, 0, 0, 5);
		gbc_btnClose.gridx = 0;
		gbc_btnClose.gridy = 3;
		panel.add(btnClose, gbc_btnClose);
		btnClose.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					frame.dispose();		
				}
			}
		);
	
		
		frame.setVisible(true);
	}

}
