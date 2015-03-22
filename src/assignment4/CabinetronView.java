package assignment4;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import InventoryItems.InventoryView;
import Parts.PartView;
import Parts.PartsInventoryController;
import Parts.PartsInventoryView;
import ProductTemplates.ProductTemplateListView;

@SuppressWarnings("serial")
public class CabinetronView extends JFrame {
	private int GUIWidth;
	private int GUIHeight;
	private JMenuBar menuBar;
	private JMenu menuParts, menuTemplates;
	private JMenuItem itemParts, itemInventory;
	private int minX = 300;
	private int minY = 300;
	
	private CabinetronModel model;
	private CabinetronView thisView;
	
	private PartsInventoryView partsInventoryView;
	private InventoryView inventoryView;
	private ProductTemplateListView productTemplateListView;
	
	private JInternalFrame partListWindow;
	private JInternalFrame partDetailWindow;
	
	private PartsInventoryController partsController;
	
	private JDesktopPane desktop;
	
	public CabinetronView(CabinetronModel model) {
		super("Cabinetron");
		this.thisView = this;
		this.model = model;
		this.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width / 2) - (model.GetGUIWidth() / 2), 
				 (Toolkit.getDefaultToolkit().getScreenSize().height / 2) - (model.GetGUIHeight() / 2));
		this.setSize(model.GetGUIWidth(), model.GetGUIHeight());
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		getContentPane().setPreferredSize(new Dimension(model.GetGUIWidth(), model.GetGUIHeight()));
		getContentPane().setBackground(new Color(225, 215, 193));
		getContentPane().setLayout(new BorderLayout());

		menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);
		
		menuParts = new JMenu("Parts");
		menuTemplates = new JMenu("Templates");
		
		itemParts = new JMenuItem("Part");
		menuParts.add(itemParts);
		menuParts.addSeparator();
		
		itemInventory = new JMenuItem("Inventory");
		menuParts.add(itemInventory);
		
		menuBar.add(menuParts);
		menuBar.add(menuTemplates);
		
		desktop = new JDesktopPane();
		this.add(desktop);
		this.setVisible(true);

	
	}
	
	public void register(CabinetronController controller) {
		itemParts.addActionListener(controller);
		itemInventory.addActionListener(controller);
	}
	
	
	public PartsInventoryView ViewParts() {
		PartsInventoryView partsView = new PartsInventoryView(model.GetPartsModel(), model.GetGUIWidth() / 2, model.GetGUIHeight() / 2, minX, minY);
		partListWindow = new JInternalFrame("Parts List", true, true, true, true );
		partListWindow.setMinimumSize(new Dimension(minX, minY));
		partListWindow.addComponentListener(new ComponentListener() {
			public void componentResized(ComponentEvent e) {
				partsView.resized();
				partsController = new PartsInventoryController(model.GetPartsModel(), partsView, thisView);
				partsView.register(partsController);
			}
			public void componentMoved(ComponentEvent e) {}
			public void componentShown(ComponentEvent e) {}
			public void componentHidden(ComponentEvent e) {}
		});
		PartsInventoryController controller = new PartsInventoryController(model.GetPartsModel(), partsView, thisView);
		partsView.register(controller);
		partListWindow.add(partsView, BorderLayout.CENTER);
		partListWindow.pack();
		partListWindow.setLocation(0, 0); // child window position in desktop 
		desktop.add(partListWindow);
		partListWindow.setVisible(true);
		return partsView;
	}
	
	public PartView ViewPartDetails(String title) {
		int prevX, prevY;
		PartView partDetailView = new PartView(model.GetPartsModel(), (model.GetGUIWidth() / 5) * 2, (model.GetGUIHeight() / 5) * 2, minX, minY);
		if (partDetailWindow != null) {
			prevX = partDetailWindow.getX();
			prevY = partDetailWindow.getY();
		}
		else {  // set an initial location for the detail view window
			prevX = 20;
			prevY = 20;
		}
		partDetailWindow = new JInternalFrame(title, true, true, true, true );
		partDetailWindow.setMinimumSize(new Dimension(minX, minY));
		partDetailWindow.addComponentListener(new ComponentListener() {
			public void componentResized(ComponentEvent e) {
				partDetailView.resized();
				partDetailView.register(partsController);
			}
			public void componentMoved(ComponentEvent e) {}
			public void componentShown(ComponentEvent e) {}
			public void componentHidden(ComponentEvent e) {}
		});
		partDetailView.register(partsController);
		partDetailWindow.add(partDetailView, BorderLayout.CENTER);
		partDetailWindow.pack();
		partDetailWindow.setLocation(prevX, prevY); // child window position in desktop 
		desktop.add(partDetailWindow);
		partDetailWindow.setVisible(true);
		return partDetailView;
	}
	
	public void closePartDetailView() {
		partDetailWindow.dispose();
	}
}
