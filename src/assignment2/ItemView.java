package assignment2;

import java.awt.Color;
import java.awt.Toolkit;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class ItemView extends JFrame {
	private InventoryItemModel model;
	private JPanel partFrame;
	private JButton cancel, ok, edit, save;
	private JLabel ID, partID, itemQuantity, partLocationType, errorMessage;
	private JTextField idField, quantityField;
	private JComboBox<String> partField, locationField;	
	private int viewWidth, viewHeight, errorW, errorH, buttonW, buttonH, buttonLeft, buttonBottom,
				labelW, labelH, labelTop, labelLeft, fieldW, fieldH, fieldLeft, fieldTop;
	
	public ItemView(InventoryItemModel model, String title) {
		super(title);
		this.model = model;
		
		viewWidth = 400;
		viewHeight = 275;
		labelW = viewWidth / 4;
		labelH = 32;
		labelTop = 15;
		labelLeft = 15;
		errorW = viewWidth - (labelLeft * 2);
		errorH = 32;
		buttonW = 96;
		buttonH = 32;
		buttonLeft = viewWidth / 3 - buttonW / 3;
		buttonBottom = viewHeight - buttonH - 64;
		fieldW = 200;
		fieldH = 32;
		fieldLeft = labelW + 25;
		fieldTop = labelTop;
		
		this.setSize(viewWidth, viewHeight);
		this.setVisible(true);
		this.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width / 2) - (viewWidth / 2) + 50, 
				 (Toolkit.getDefaultToolkit().getScreenSize().height / 2) - (viewHeight / 2));
		
		partFrame = new JPanel();
		partFrame.setBackground(Color.LIGHT_GRAY);
		partFrame.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(partFrame);
		partFrame.setLayout(null);
		
		ID = new JLabel("ID");
		ID.setBounds(labelLeft, labelTop + (labelH * 0), labelW, labelH);
		partFrame.add(ID);
		
		partID = new JLabel("Part ID");
		partID.setBounds(labelLeft, labelTop + (labelH * 1), labelW, labelH);
		partFrame.add(partID);
		
		itemQuantity = new JLabel("Quantity");
		itemQuantity.setBounds(labelLeft, labelTop + (labelH * 2), labelW, labelH);
		partFrame.add(itemQuantity);
	
		partLocationType = new JLabel("Location");
		partLocationType.setBounds(labelLeft, labelTop + (labelH * 3), labelW, labelH);
		partFrame.add(partLocationType);
		
		errorMessage = new JLabel("");
		errorMessage.setForeground(Color.red);
		errorMessage.setBounds(labelLeft, labelTop + (labelH * 4), errorW, errorH);
		partFrame.add(errorMessage);
		
		cancel = new JButton("Cancel");
		cancel.setBounds((int) (buttonLeft), buttonBottom, buttonW, buttonH);
		partFrame.add(cancel);
		
		ok = new JButton("OK");
		ok.setBounds((int) (buttonLeft * 2), buttonBottom, buttonW, buttonH);
		partFrame.add(ok);
		
		edit = new JButton("Edit");
		edit.setBounds((int) (buttonLeft * 2), buttonBottom, buttonW, buttonH);
		partFrame.add(edit);
		
		save = new JButton("Save");
		save.setBounds((int) (buttonLeft * 2), buttonBottom, buttonW, buttonH);
		partFrame.add(save);
		
		idField = new JTextField();
		idField.setBounds(fieldLeft, fieldTop + (fieldH * 0), fieldW, fieldH);
		partFrame.add(idField);
		
		partField = new JComboBox<String>();
		try {
			for (String part : model.getParts()) {
				partField.addItem(part);
			}
		} 
		catch (SQLException e) {
			setErrorMessage(e.getMessage());
		}
		partField.setBounds(fieldLeft, fieldTop + (fieldH * 1), fieldW, fieldH);
		partFrame.add(partField);
		
		quantityField = new JTextField();
		quantityField.setBounds(fieldLeft, fieldTop + (fieldH * 2), fieldW, fieldH);
		partFrame.add(quantityField);
		
		locationField = new JComboBox<String>();
		try {
			for (String unitType : model.getLocations()) {
				locationField.addItem(unitType);
			}
		} 
		catch (SQLException e) {
			setErrorMessage(e.getMessage());
		}
		locationField.setSelectedItem("Unknown"); // default Unknown; if it is not in the list, defaults to first item
		locationField.setBounds(fieldLeft, fieldTop + (fieldH * 3), fieldW, fieldH);
		partFrame.add(locationField);
	}
	
	public void register(InventoryController controller) {
		ok.addActionListener(controller);
		cancel.addActionListener(controller);
		edit.addActionListener(controller);
		save.addActionListener(controller);
	}
	
	public Integer getPartID() {
		Integer i = 0;
		try {
			i = model.getPartIDByPartNumber(partField.getSelectedItem().toString());
		}
		catch (SQLException e) {
			errorMessage.setText(e.getMessage());
		}
		return i;
	}
	
	public String getPartNumber() {
		return partField.getSelectedItem().toString();
	}
	
	public Integer getID() throws NumberFormatException {
		Integer i = 0;
		try {
			i = Integer.parseInt(idField.getText().trim());
			return i;
		}
		catch (NumberFormatException nfe) {
			throw new NumberFormatException("Error: id must be in the form of an integer.");
		}
	}
	
	public Integer getQuantity() throws NumberFormatException {
		Integer i = 0;
		try {
			i = Integer.parseInt(quantityField.getText().trim());
			return i;
		}
		catch (NumberFormatException nfe) {
			throw new NumberFormatException("Error: quantity must be in the form of an integer.");
		}
	}
	
	public String getLocationName() {
		int index = locationField.getSelectedIndex();
		return locationField.getItemAt(index);
	}
	
	public void setErrorMessage(String error) {
		errorMessage.setText(error);
	}
	
	public void setPartNumber(String partNumber) {
		partField.setSelectedItem(partNumber);
	}
	
	public void setPartNumberByID(Integer ID) {

		try {
			partField.setSelectedItem(model.getPartNumberByID(ID));
		} 
		catch (SQLException e) {
			errorMessage.setText(e.getMessage());
		}
	}
	
	public void setID(Integer id) {
		idField.setText(String.valueOf(id));
	}
	
	public void setQuantity(Integer quantity) {
		quantityField.setText(String.valueOf(quantity));
	}
	
	public void setLocationType(String quantityUnitType) {
		locationField.setSelectedItem(quantityUnitType);
	}
	
	public void hideEditButton() {
		edit.setVisible(false);
	}
	
	public void hideSaveButton() {
		save.setVisible(false);
	}
	
	public void disableIDEdit() {
		idField.setEnabled(false);
	}
	
	public void disableEditable() {
		ok.setVisible(false);
		save.setVisible(false);
		partField.setEnabled(false);
		idField.setEnabled(false);
		quantityField.setEnabled(false);
		locationField.setEnabled(false);
	}
	
	public void enableEditable() {
		save.setVisible(true);
		partField.setEnabled(true);
		quantityField.setEnabled(true);
		locationField.setEnabled(true);
	}
}