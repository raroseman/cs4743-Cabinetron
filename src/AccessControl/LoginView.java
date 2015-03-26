package AccessControl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import Parts.PartsInventoryModel;
import ProductTemplateParts.ProductTemplatePartDetailController;
import ProductTemplateParts.ProductTemplatePartModel;

public class LoginView extends JPanel{
	private JPanel partFrame;
	private JButton cancel, login;
	private Integer templateID;
	
	private JLabel username, password, errorMessage;
	private JTextField productTemplateIDField, productPartIDField, productQuantityField;	
	private int viewWidth, viewHeight, errorW, errorH, buttonW, buttonH, buttonX, buttonY, buttonLeft, buttonBottom,
				labelW, labelH, labelTop, labelLeft, fieldW, fieldH, fieldLeft, fieldTop;
	private int minX, minY;
	
	public LoginView(int width, int height, int minX, int minY) {
		this.minX = minX;
		this.minY = minY;
		this.setSize(new Dimension(width, height));
		createPanel();
	}
	
	private void createPanel() {
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
		this.setLayout(new BorderLayout());
		
		partFrame = new JPanel();
		partFrame.setSize(viewWidth, viewHeight);
		partFrame.setBackground(Color.LIGHT_GRAY);
		partFrame.setBorder(new EmptyBorder(5, 5, 5, 5));
		partFrame.setLayout(null);
		
		username = new JLabel("Username");
		username.setBounds(labelLeft, labelTop + (labelH * 0), labelW, labelH);
		partFrame.add(username);
		
		password = new JLabel("Password");
		password.setBounds(labelLeft, labelTop + (labelH * 1), labelW, labelH);
		partFrame.add(password);
		
		errorMessage = new JLabel("");
		errorMessage.setForeground(Color.red);
		errorMessage.setBounds(labelLeft, labelTop + (labelH * 5), errorW, errorH);
		partFrame.add(errorMessage);
		
		cancel = new JButton("Cancel");
		cancel.setBounds((buttonX * 1) - (buttonW / 2), buttonY, buttonW, buttonH);
		partFrame.add(cancel);
		
		login = new JButton("Login");
		login.setBounds((buttonX * 2) - (buttonW / 2), buttonY, buttonW, buttonH);
		partFrame.add(login);
		
		productTemplateIDField = new JTextField();
		//productTemplateIDField.setText(model.getProductTemplateID().toString());
		productTemplateIDField.setBounds(fieldLeft, fieldTop + (fieldH * 0), fieldW, fieldH);
		partFrame.add(productTemplateIDField);
		
		productPartIDField = new JTextField();
		productPartIDField.setBounds(fieldLeft, fieldTop + (fieldH * 1), fieldW, fieldH);
		partFrame.add(productPartIDField);
		
		this.add(partFrame, BorderLayout.CENTER);
		
	}
	
	private void resizePanel() {
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
		partFrame.setSize(viewWidth, viewHeight);
		username.setBounds(labelLeft, labelTop + (labelH * 0), labelW, labelH);
		password.setBounds(labelLeft, labelTop + (labelH * 1), labelW, labelH);
		errorMessage.setBounds(labelLeft, labelTop + (labelH * 5), errorW, errorH);
		cancel.setBounds((buttonX * 1) - (buttonW / 2), buttonY, buttonW, buttonH);
		login.setBounds((buttonX * 2) - (buttonW / 2), buttonY, buttonW, buttonH);
		//edit.setBounds((buttonX * 2) - (buttonW / 2), buttonY, buttonW, buttonH);
		//save.setBounds((buttonX * 2) - (buttonW / 2), buttonY, buttonW, buttonH);
		productTemplateIDField.setBounds(fieldLeft, fieldTop + (fieldH * 0), fieldW, fieldH);
		productPartIDField.setBounds(fieldLeft, fieldTop + (fieldH * 1), fieldW, fieldH);
	}
	
	public void register(LoginController controller) {
		login.addActionListener(controller);
		cancel.addActionListener(controller);
	}
	
	public Integer getProductTemplateID() {
		return this.templateID;
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
	}
/*	
	public void hideTemplateID() {
		productTemplateIDField.setVisible(false);
		productTemplateID.setVisible(false);
	}
	
	public void hideEditButton() {
		edit.setVisible(false);
	}
	
	public void hideSaveButton() {
		save.setVisible(false);
	}
*/	
	public void disableIDEdit() {
		productPartIDField.setEnabled(false);
	}
	
	public void disableTemplateIDEdit() {
		productTemplateIDField.setEnabled(false);
	}
/*	
	public void disableEditable() {
		ok.setVisible(false);
		save.setVisible(false);
		productTemplateIDField.setEnabled(false);
		productPartIDField.setEnabled(false);
		productQuantityField.setEnabled(false);
	}
	
	public void enableEditable() {
		save.setVisible(true);
		productPartIDField.setEnabled(true);
		productQuantityField.setEnabled(true);
	}
*/	
	public void resized() {
		resizePanel();
	}
}