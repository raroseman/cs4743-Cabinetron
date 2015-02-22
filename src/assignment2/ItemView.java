package assignment2;

import java.awt.Color;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class ItemView extends JFrame {
	private JPanel partFrame;
	private JButton cancel, ok, edit, save;
	private JLabel ID, partID, itemQuantity, partLocationType, errorMessage;
	private JTextField partField, idField, quantityField;
	private JComboBox<String> locationUnitTypeField;	
	private int viewWidth, viewHeight, errorW, errorH, buttonW, buttonH, buttonLeft, buttonBottom,
				labelW, labelH, labelTop, labelLeft, fieldW, fieldH, fieldLeft, fieldTop;
	
	public ItemView(InventoryItemModel model, String title) {
		super(title);
		
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
		
		partField = new JTextField();
		partField.setBounds(fieldLeft, fieldTop + (fieldH * 1), fieldW, fieldH);
		partFrame.add(partField);
		
		quantityField = new JTextField();
		quantityField.setBounds(fieldLeft, fieldTop + (fieldH * 2), fieldW, fieldH);
		partFrame.add(quantityField);
		
		locationUnitTypeField = new JComboBox<String>();
		for (String unitType : model.getValidLocationTypes()) {
			locationUnitTypeField.addItem(unitType);
		}
		locationUnitTypeField.setBounds(fieldLeft, fieldTop + (fieldH * 3), fieldW, fieldH);
		partFrame.add(locationUnitTypeField);
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
			i = Integer.parseInt(partField.getText().trim());
			return i;
		}
		catch (NumberFormatException nfe) {
			throw new NumberFormatException("Error: item id must be in the form of an integer.");
		}
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
	
	public String getQuantityUnitType() {
		int index = locationUnitTypeField.getSelectedIndex();
		return locationUnitTypeField.getItemAt(index);
	}
	
	public void setErrorMessage(String error) {
		errorMessage.setText(error);
	}
	
	public void setItemID(Integer item) {
		partField.setText(String.valueOf(item));
	}
	
	public void setID(Integer id) {
		idField.setText(String.valueOf(id));
	}
	
	public void setQuantity(Integer quantity) {
		quantityField.setText(String.valueOf(quantity));
	}
	
	public void setLocationType(String quantityUnitType) {
		locationUnitTypeField.setSelectedItem(quantityUnitType);
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
		locationUnitTypeField.setEnabled(false);
	}
	
	public void enableEditable() {
		save.setVisible(true);
		partField.setEnabled(true);
		quantityField.setEnabled(true);
		locationUnitTypeField.setEnabled(true);
	}
}