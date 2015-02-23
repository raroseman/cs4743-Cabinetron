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
public class PartView extends JFrame {
	private JPanel partFrame;
	private JButton cancel, ok, edit, save;


	private JLabel partName, partNumber, partVendor, partQuantityUnitType, partID, externalPartNumber, errorMessage;
	private JTextField nameField, numberField, vendorField, idField, externalField;
	private JComboBox<String> quantityUnitTypeField;	
	private int viewWidth, viewHeight, errorW, errorH, buttonW, buttonH, buttonLeft, buttonBottom,
				labelW, labelH, labelTop, labelLeft, fieldW, fieldH, fieldLeft, fieldTop;
	
	public PartView(PartsInventoryModel model, String title) {
		super(title);
		
		viewWidth = 400;
		viewHeight = 350;
		labelW = viewWidth / 3;
		labelH = 32;
		labelTop = 15;
		labelLeft = 15;
		errorW = viewWidth - (labelLeft * 2);
		errorH = 32;
		buttonW = viewWidth / 4;
		buttonH = 32;
		buttonLeft = viewWidth / 3 - buttonW / 3;
		buttonBottom = viewHeight - buttonH - 64;
		fieldW = viewWidth / 2;
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
		
		partName = new JLabel("Name");
		partName.setBounds(labelLeft, labelTop + (labelH * 0), labelW, labelH);
		partFrame.add(partName);
		
		partID = new JLabel("ID");
		partID.setBounds(labelLeft, labelTop + (labelH * 1), labelW, labelH);
		partFrame.add(partID);
		
		partNumber = new JLabel("Part Number");
		partNumber.setBounds(labelLeft, labelTop + (labelH * 2), labelW, labelH);
		partFrame.add(partNumber);
		
		externalPartNumber = new JLabel("External Part Number");
		externalPartNumber.setBounds(labelLeft, labelTop + (labelH * 3), labelW, labelH);
		partFrame.add(externalPartNumber);
		
		partVendor = new JLabel("Vendor");
		partVendor.setBounds(labelLeft, labelTop + (labelH * 4), labelW, labelH);
		partFrame.add(partVendor);
	
		partQuantityUnitType = new JLabel("Unit Type");
		partQuantityUnitType.setBounds(labelLeft, labelTop + (labelH * 5), labelW, labelH);
		partFrame.add(partQuantityUnitType);
		
		errorMessage = new JLabel("");
		errorMessage.setForeground(Color.red);
		errorMessage.setBounds(labelLeft, labelTop + (labelH * 6), errorW, errorH);
		partFrame.add(errorMessage);
		
		cancel = new JButton("Cancel");
		cancel.setBounds(buttonLeft, buttonBottom, buttonW, buttonH);
		partFrame.add(cancel);
		
		ok = new JButton("OK");
		ok.setBounds(buttonLeft * 2, buttonBottom, buttonW, buttonH);
		partFrame.add(ok);
		
		edit = new JButton("Edit");
		edit.setBounds(buttonLeft * 2, buttonBottom, buttonW, buttonH);
		partFrame.add(edit);
		
		save = new JButton("Save");
		save.setBounds(buttonLeft * 2, buttonBottom, buttonW, buttonH);
		partFrame.add(save);
		
		nameField = new JTextField();
		nameField.setBounds(fieldLeft, fieldTop + (fieldH * 0), fieldW, fieldH);
		partFrame.add(nameField);
		
		idField = new JTextField();
		idField.setBounds(fieldLeft, fieldTop + (fieldH * 1), fieldW, fieldH);
		partFrame.add(idField);
		
		numberField = new JTextField();
		numberField.setBounds(fieldLeft, fieldTop + (fieldH * 2), fieldW, fieldH);
		partFrame.add(numberField);
		
		externalField = new JTextField();
		externalField.setBounds(fieldLeft, fieldTop + (fieldH * 3), fieldW, fieldH);
		partFrame.add(externalField);
		
		vendorField = new JTextField();
		vendorField.setBounds(fieldLeft, fieldTop + (fieldH * 4), fieldW, fieldH);
		partFrame.add(vendorField);
		
		quantityUnitTypeField = new JComboBox<String>();
		try {
			for (String unitType : model.getQuantityUnitTypes()) {
				quantityUnitTypeField.addItem(unitType);
			}
		} catch (SQLException e) {
			errorMessage.setText(e.getMessage());
		}
		quantityUnitTypeField.setSelectedItem("Unknown"); // default Unknown; if not in list, defaults to first item in list
		quantityUnitTypeField.setBounds(fieldLeft, fieldTop + (fieldH * 5), fieldW, fieldH);
		partFrame.add(quantityUnitTypeField);
	}
	
	public void register(PartsInventoryController controller) {
		ok.addActionListener(controller);
		cancel.addActionListener(controller);
		edit.addActionListener(controller);
		save.addActionListener(controller);
		this.addWindowFocusListener(controller);
	}
	
	public String getName() {
		return nameField.getText();
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
	public String getNumber() {
		return numberField.getText();
	}
	
	public String getExternalPartNumber() {
		return externalField.getText();
	}
	
	public String getVendor() {
		return vendorField.getText();
	}
	
	public String getQuantityUnitType() {
		int index = quantityUnitTypeField.getSelectedIndex();
		return quantityUnitTypeField.getItemAt(index);
	}
	
	public void setErrorMessage(String error) {
		errorMessage.setText(error);
	}
	
	public void setName(String name) {
		nameField.setText(name);
	}
	
	public void setID(Integer id) {
		idField.setText(String.valueOf(id));
	}
	
	public void setNumber(String number) {
		numberField.setText(number);
	}
	
	public void setExternalNumber(String externalNumber) {
		externalField.setText(externalNumber);
	}
	
	public void setVendor(String vendor) {
		vendorField.setText(vendor);
	}
	
	public void setQuantityUnitType(String quantityUnitType) {
		quantityUnitTypeField.setSelectedItem(quantityUnitType);
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
		nameField.setEnabled(false);
		idField.setEnabled(false);
		numberField.setEnabled(false);
		externalField.setEnabled(false);
		vendorField.setEnabled(false);
		quantityUnitTypeField.setEnabled(false);
	}
	
	public void enableEditable() {
		save.setVisible(true);
		nameField.setEnabled(true);
		numberField.setEnabled(true);
		externalField.setEnabled(true);
		vendorField.setEnabled(true);
		quantityUnitTypeField.setEnabled(true);
	}
}
