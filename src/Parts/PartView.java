package Parts;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class PartView extends JPanel {
	private JPanel partFrame;
	private JButton cancel, ok, edit, save;


	private JLabel partName, partNumber, partVendor, partQuantityUnitType, partID, externalPartNumber, errorMessage;
	private JTextField nameField, numberField, vendorField, idField, externalField;
	private JComboBox<String> quantityUnitTypeField;	
	private int viewWidth, viewHeight, errorW, errorH, buttonW, buttonH, buttonX, buttonY,
				labelW, labelH, labelTop, labelLeft, fieldW, fieldH, fieldLeft, fieldTop, minX, minY;
	private List<String> unitTypes;
	private String name = "", part = "", external = "", vendor = "", ID = "", unitType = "Unknown";
	private boolean inEditMode = false, inAddMode = false;
	
	public PartView(PartsInventoryModel model, int width, int height, int minX, int minY) {
	//	super(title);
		unitTypes = new ArrayList<String>();
		try {
			for (String unitType : model.getQuantityUnitTypes()) {
				unitTypes.add(unitType);
			}
		} catch (SQLException e) {
			errorMessage.setText(e.getMessage());
		}
		this.minX = minX;
		this.minY = minY;
		this.setSize(new Dimension(width, height));
		createPanel();
		
	}
	
	void createPanel() {
		viewWidth = Math.max(minX, this.getWidth());
		viewHeight = Math.max(minY, this.getHeight());
		labelW = viewWidth / 3;
		labelH = 32;
		
		labelTop = 10;
		labelLeft = 15;
		
		errorW = viewWidth - (labelLeft * 2);
		errorH = 32;
		buttonW = viewWidth / 3;
		buttonH = 32;
		buttonX = viewWidth / 3;
		buttonY = viewHeight - 100;
		fieldW = viewWidth / 2;
		fieldH = 32;
		fieldLeft = labelW + 25;
		fieldTop = labelTop;
		
		this.setSize(viewWidth, viewHeight);
		this.setVisible(true);
		this.setBackground(Color.BLUE);
		this.setLayout(new BorderLayout());
		
		partFrame = new JPanel();
		partFrame.setSize(viewWidth, viewHeight);
		partFrame.setBackground(Color.LIGHT_GRAY);
		partFrame.setBorder(new EmptyBorder(5, 5, 5, 5));
		partFrame.setLayout(null);
		
		partID = new JLabel("ID");
		partID.setBounds(labelLeft, labelTop + (labelH * 0), labelW, labelH);
		partFrame.add(partID);
		
		partName = new JLabel("Name");
		partName.setBounds(labelLeft, labelTop + (labelH * 1), labelW, labelH);
		partFrame.add(partName);
		
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
		
	
		idField = new JTextField();
		idField.setBounds(fieldLeft, fieldTop + (fieldH * 0), fieldW, fieldH);
		idField.setText(ID);
		partFrame.add(idField);
		
		nameField = new JTextField();
		nameField.setBounds(fieldLeft, fieldTop + (fieldH * 1), fieldW, fieldH);
		nameField.setText(name);
		partFrame.add(nameField);
		
		numberField = new JTextField();
		numberField.setBounds(fieldLeft, fieldTop + (fieldH * 2), fieldW, fieldH);
		numberField.setText(part);
		partFrame.add(numberField);
		
		externalField = new JTextField();
		externalField.setBounds(fieldLeft, fieldTop + (fieldH * 3), fieldW, fieldH);
		externalField.setText(external);
		partFrame.add(externalField);
		
		vendorField = new JTextField();
		vendorField.setBounds(fieldLeft, fieldTop + (fieldH * 4), fieldW, fieldH);
		vendorField.setText(vendor);
		partFrame.add(vendorField);
		
		quantityUnitTypeField = new JComboBox<String>();
		for (String unitType : unitTypes) {
			quantityUnitTypeField.addItem(unitType);
		}
		quantityUnitTypeField.setSelectedItem(unitType); // default Unknown; if not in list, defaults to first item in list
		quantityUnitTypeField.setBounds(fieldLeft, fieldTop + (fieldH * 5), fieldW, fieldH);
		partFrame.add(quantityUnitTypeField);
		
		errorMessage = new JLabel("");
		errorMessage.setForeground(Color.red);
		errorMessage.setBounds(fieldLeft, fieldTop + (fieldH * 6), errorW, errorH);
		partFrame.add(errorMessage);
		
		cancel = new JButton("Cancel");
		cancel.setBounds((buttonX * 1) - (buttonW / 2), buttonY, buttonW, buttonH);
		partFrame.add(cancel);
		
		ok = new JButton("OK");
		ok.setBounds((buttonX * 2) - (buttonW / 2), buttonY, buttonW, buttonH);
		partFrame.add(ok);
		
		edit = new JButton("Edit");
		edit.setBounds((buttonX * 2) - (buttonW / 2), buttonY, buttonW, buttonH);
		partFrame.add(edit);
		
		save = new JButton("Save");
		save.setBounds((buttonX * 2) - (buttonW / 2), buttonY, buttonW, buttonH);
		partFrame.add(save);
		
		
		this.add(partFrame, BorderLayout.CENTER);

		this.setVisible(true);
	}
	
	public void register(PartsInventoryController controller) {
		ok.addActionListener(controller);
		cancel.addActionListener(controller);
		edit.addActionListener(controller);
		save.addActionListener(controller);
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
	
	public void hideID() {
		idField.setVisible(false);
		partID.setVisible(false);
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
		inEditMode = false;
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
		inEditMode = true;
		save.setVisible(true);
		nameField.setEnabled(true);
		numberField.setEnabled(true);
		externalField.setEnabled(true);
		vendorField.setEnabled(true);
		quantityUnitTypeField.setEnabled(true);
	}
	
	public void enableAdd() {
		inAddMode = true;
		disableIDEdit();
		hideSaveButton();
		hideEditButton();
		hideID();
	}
	
	public void resized() {
		ID = idField.getText();
		name = nameField.getText();
		part = numberField.getText();
		external = externalField.getText();
		vendor = vendorField.getText();
		unitType = (String) quantityUnitTypeField.getSelectedItem();
		this.removeAll();
		createPanel();
		if (inEditMode) {
			enableEditable();
		}
		else if (inAddMode) {
			enableAdd();
		}
		else {
			disableEditable();
		}
	}
}
