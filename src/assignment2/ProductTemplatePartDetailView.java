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
public class ProductTemplatePartDetailView extends JFrame {
	private JPanel partFrame;
	private JButton cancel, ok, edit, save;


	private JLabel productTemplateID, productPartID, productQuantity, errorMessage;
	private JTextField productTemplateIDField, productPartIDField, productQuantityField;	
	private int viewWidth, viewHeight, errorW, errorH, buttonW, buttonH, buttonLeft, buttonBottom,
				labelW, labelH, labelTop, labelLeft, fieldW, fieldH, fieldLeft, fieldTop;
	
	public ProductTemplatePartDetailView(ProductTemplatePartModel model, String title) {
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
		
		productTemplateID = new JLabel("Product Template ID");
		productTemplateID.setBounds(labelLeft, labelTop + (labelH * 0), labelW, labelH);
		partFrame.add(productTemplateID);
		
		productPartID = new JLabel("Part ID");
		productPartID.setBounds(labelLeft, labelTop + (labelH * 1), labelW, labelH);
		partFrame.add(productPartID);
		
		productQuantity = new JLabel("Quantity");
		productQuantity.setBounds(labelLeft, labelTop + (labelH * 2), labelW, labelH);
		partFrame.add(productQuantity);
		
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
		
		productTemplateIDField = new JTextField();
		productTemplateIDField.setBounds(fieldLeft, fieldTop + (fieldH * 0), fieldW, fieldH);
		partFrame.add(productTemplateIDField);
		
		productPartIDField = new JTextField();
		productPartIDField.setBounds(fieldLeft, fieldTop + (fieldH * 1), fieldW, fieldH);
		partFrame.add(productPartIDField);
		
		productQuantityField = new JTextField();
		productQuantityField.setBounds(fieldLeft, fieldTop + (fieldH * 2), fieldW, fieldH);
		partFrame.add(productQuantityField);
		
	}
	
	public void register(ProductTemplatePartDetailController controller) {
		ok.addActionListener(controller);
		cancel.addActionListener(controller);
		edit.addActionListener(controller);
		save.addActionListener(controller);
		this.addWindowFocusListener(controller);	
	}
	
	public Integer getProductTemplateID() throws NumberFormatException {
		Integer i = 0;
		try {
			i = Integer.parseInt(productTemplateIDField.getText().trim());
			return i;
		}
		catch (NumberFormatException nfe) {
			throw new NumberFormatException("Error: template id must be in the form of an integer.");
		}
	}
	
	public Integer getPartID() throws NumberFormatException {
		Integer i = 0;
		try {
			i = Integer.parseInt(productPartIDField.getText().trim());
			return i;
		}
		catch (NumberFormatException nfe) {
			throw new NumberFormatException("Error: id must be in the form of an integer.");
		}
	}
	
	public Integer getQuantity() throws NumberFormatException {
		Integer i = 0;
		try {
			i = Integer.parseInt(productQuantityField.getText().trim());
			return i;
		}
		catch (NumberFormatException nfe) {
			throw new NumberFormatException("Error: id must be in the form of an integer.");
		}
	}
	
	public void setErrorMessage(String error) {
		errorMessage.setText(error);
	}
	
	public void setTemplateID(Integer id) {
		productTemplateIDField.setText(String.valueOf(id));
	}
	
	public void setPartID(Integer partID) {
		productPartIDField.setText(String.valueOf(partID));
	}
	
	public void setQuantity(Integer quantity) {
		productQuantityField.setText(String.valueOf(quantity));
	}
	
	public void hideID() {
		productPartIDField.setVisible(false);
		productPartIDField.setVisible(false);
	}
	
	public void hideEditButton() {
		edit.setVisible(false);
	}
	
	public void hideSaveButton() {
		save.setVisible(false);
	}
	
	public void disableIDEdit() {
		productPartIDField.setEnabled(false);
	}
	
	public void disableEditable() {
		ok.setVisible(false);
		save.setVisible(false);
		productPartIDField.setEnabled(false);
		productPartIDField.setEnabled(false);
		productQuantityField.setEnabled(false);
	}
	
	public void enableEditable() {
		save.setVisible(true);
		productPartIDField.setEnabled(true);
		productQuantityField.setEnabled(true);
	}
}
