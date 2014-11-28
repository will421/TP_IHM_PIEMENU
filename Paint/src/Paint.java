//////////////////////////////////////////////////////////////////////////////
// file    : Paint.java
// content : basic painting app
//////////////////////////////////////////////////////////////////////////////


/* imports *****************************************************************/

import static java.lang.Math.*;

import java.util.Vector;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Shape;
import java.awt.Point;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.event.*;

import javax.swing.event.*;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.AbstractAction;
import javax.swing.JToolBar;
import javax.swing.OverlayLayout;
import javax.swing.SwingUtilities;


/* paint *******************************************************************/

class Paint extends JFrame implements MouseInputListener {
	Vector<Shape> shapes = new Vector<Shape>();
	Vector<Color> colors = new Vector<Color>();
	Color currentColor = Color.BLACK;

	class ColorPicker extends AbstractAction
	{
		public ColorPicker(String name) {
			super(name);
		}
		@Override
		public void actionPerformed(ActionEvent arg0) {
			currentColor= JColorChooser.showDialog(null, "Choose a color", null);
		}
	}

	
	
	class Tool extends AbstractAction
	           implements MouseInputListener {
	   Point o;
		Shape shape;
		String name;
		public Tool(String name) { super(name); this.name = name; }
		public void actionPerformed(ActionEvent e) {
			System.out.println("using tool " + this);
			panel.removeMouseListener(tool);
			panel.removeMouseMotionListener(tool);
			tool = this;
			panel.addMouseListener(tool);
			panel.addMouseMotionListener(tool);
		}
		public void mouseClicked(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mousePressed(MouseEvent e) {if(e.getButton()==1) o = e.getPoint(); }
		public void mouseReleased(MouseEvent e) {if(e.getButton()==1) shape = null; }
		public void mouseDragged(MouseEvent e) {}
		public void mouseMoved(MouseEvent e) {}
		@Override
			public String toString() {
				return name;
			}
	}
	
	Tool tools[] = {
		new Tool("pen") {
			public void mouseDragged(MouseEvent e) {
				Path2D.Double path = (Path2D.Double)shape;
				if(path == null) {
					path = new Path2D.Double();
					path.moveTo(o.getX(), o.getY());
					shapes.add(shape = path);
					colors.add(currentColor);
				}
				path.lineTo(e.getX(), e.getY());
				panel.repaint();
			}
		},
		new Tool("rect") {
			public void mouseDragged(MouseEvent e) {
				Rectangle2D.Double rect = (Rectangle2D.Double)shape;
				if(rect == null) {
					rect = new Rectangle2D.Double(o.getX(), o.getY(), 0, 0);
					shapes.add(shape = rect);
					colors.add(currentColor);
				}
				
				rect.setRect(min(e.getX(), o.getX()), min(e.getY(), o.getY()),
				             abs(e.getX()- o.getX()), abs(e.getY()- o.getY()));
				panel.repaint();
			}
		},
		new Tool("oval") {
			public void mouseDragged(MouseEvent e){
				Ellipse2D.Double ell = (Ellipse2D.Double)shape;
				if(ell == null) {
					ell = new Ellipse2D.Double(o.getX(), o.getY(), 0, 0);
					shapes.add(shape = ell);
					colors.add(currentColor);
				}
				ell.setFrame(min(e.getX(), o.getX()), min(e.getY(), o.getY()),
			             abs(e.getX()- o.getX()), abs(e.getY()- o.getY()));
				panel.repaint();
			}
		}
	};
	Tool tool;

	JPanel panel;
	
	Component menu;
	
	public Paint(String title) {
		super(title);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(800, 600));
		add(new JToolBar() {{
			for(AbstractAction tool: tools) {
				add(tool);
			}
			add(new ColorPicker("Color"));
		}}, BorderLayout.NORTH);
		add(panel = new JPanel() {
			{
			    this.setLayout(null);
			    JButton button = new JButton("Small");
			    button.setMaximumSize(new Dimension(25, 25));
			    button.setBackground(Color.white);
			    this.add(button);
			    
			    /*
			    button = new JButton("Medium");
			    button.setMaximumSize(new Dimension(50, 50));
			    button.setBackground(Color.gray);
			    this.add(button);
			    
			    button = new JButton("Large");
			    button.setMaximumSize(new Dimension(100, 100));
			    button.setBackground(Color.black);
			    this.add(button);  */  
			}
			public void paintComponent(Graphics g) {
				super.paintComponent(g);	
				Graphics2D g2 = (Graphics2D)g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
				                    RenderingHints.VALUE_ANTIALIAS_ON);
		
				g2.setColor(Color.WHITE);
				g2.fillRect(0, 0, getWidth(), getHeight());
				
				//g2.setColor(color);
				int i =0;
				for(Shape shape: shapes) {
					g2.setColor(colors.get(i));
					g2.draw(shape);
					i++;
				}
			}
		});

		panel.addMouseListener(this);
		pack();
		setVisible(true);
	}


/* main *********************************************************************/

	public static void main(String argv[]) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Paint paint = new Paint("paint");
			}
		});
	}


	@Override
	public void mouseClicked(MouseEvent arg0) {}
	@Override
	public void mouseEntered(MouseEvent arg0) {}
	@Override
	public void mouseExited(MouseEvent arg0) {}
	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getButton()!=3)
			return;
		System.out.println("Button "+e.getButton()+" pressed");
		/*RoundButton button = new RoundButton(new ImageIcon("http://png-2.findicons.com/files/icons/1620/crystal_project/16/mini_circle.png"));
		//button.setMaximumSize(new Dimension(16, 16));
		button.setBounds(e.getX(),e.getY(),16,16);
		panel.add(button);*/
		JButton button = new JButton("Large");
	    //button.setMaximumSize(new Dimension(100, 100));
	    //button.setBackground(Color.black);
		int height = 10;
		int width = 10;
	    button.setBounds(e.getX()-height/2, e.getY()-width/2, 10, 10);
	    panel.add(menu =button);
	    //pack();
	    repaint();
	    
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		System.out.println("r");
		if(e.getButton()!=3)
			return;
		panel.remove(menu);
	}
	@Override
	public void mouseDragged(MouseEvent e) {
		System.out.println("d");
	}
	@Override
	public void mouseMoved(MouseEvent arg0) {}
}