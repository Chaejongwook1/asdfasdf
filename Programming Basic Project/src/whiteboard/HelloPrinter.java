package whiteboard;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


public class HelloPrinter extends JFrame implements Printable {

	private JPanel contentPane;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				try {
					HelloPrinter frame = new HelloPrinter();
					frame.setVisible(true);
				} catch(Exception e) {
					e.printStackTrace();
				}
				
			}
		});
	}
	
	public int print(Graphics g, PageFormat pf, int page) throws PrinterException {
		if(page > 0) {
			return NO_SUCH_PAGE;
		}
		Graphics2D g2d = (Graphics2D)g;
		g2d.translate(pf.getImageableX(), pf.getImageableY());
		
		g.drawString("Hello ¿ùµå", 100, 100);
		return PAGE_EXISTS;
	}
	
	public HelloPrinter() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JButton btnNewButton = new JButton("Print");
		btnNewButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				PrinterJob job = PrinterJob.getPrinterJob();
				job.setPrintable(HelloPrinter.this);
				boolean ok = job.printDialog();
				if(ok) {
					try {
					job.print();
				} catch (PrinterException ex) {
				
				}
			}
			}
		});
		contentPane.add(btnNewButton,BorderLayout.SOUTH);
	}
}
