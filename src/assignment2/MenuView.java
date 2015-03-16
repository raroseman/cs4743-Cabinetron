package assignment2;

import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

@SuppressWarnings("serial")
public class MenuView extends JFrame {
	private int viewWidth, viewHeight;
	private JMenuBar jmb;
	private JMenu menuParts, menuTemplates;
	private JMenuItem itemParts, itemInventory;
	
	public MenuView() {
		super("Cabinetron");
		
		viewHeight = 800;
		viewWidth = 500;
		
		this.setSize(viewWidth, viewHeight);
		
		this.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width / 2) - (viewWidth / 2) + 50, 
				 (Toolkit.getDefaultToolkit().getScreenSize().height / 2) - (viewHeight / 2));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		jmb = new JMenuBar();
		this.setJMenuBar(jmb);
		
		menuParts = new JMenu("Parts");
		jmb.add(menuParts);
		
		menuTemplates = new JMenu("Templates");
		jmb.add(menuTemplates);
		
		itemParts = new JMenuItem("Part");
		menuParts.add(itemParts);
		menuParts.addSeparator();
		
		itemInventory = new JMenuItem("Inventory");
		menuParts.add(itemInventory);
		
		this.setVisible(true);
	}
	
	public void register(MenuController controller) {
		itemParts.addActionListener(controller);
		itemInventory.addActionListener(controller);
	}
}
