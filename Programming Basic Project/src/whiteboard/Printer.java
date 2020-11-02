package whiteboard;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.Vector;

import javax.swing.JFrame;


public class Printer extends JFrame implements Printable {

	private Vector pictures;
	
	public int print(Graphics g, PageFormat pf, int page) throws PrinterException {
		if(page > 0) {
			return NO_SUCH_PAGE;
		}
		Graphics2D g2d = (Graphics2D)g;
		g2d.translate(pf.getImageableX(), pf.getImageableY());
		
		int n = pictures.size();

		for (int i = 0; i < n; i++) {
			Drawable d = (Drawable) pictures.elementAt(i);
			d.paint(g);
		}
		return PAGE_EXISTS;
	}
	
	public Printer(Vector pictures) {
		this.pictures = pictures;
		PrinterJob job = PrinterJob.getPrinterJob();
		job.setPrintable(Printer.this);
		boolean ok = job.printDialog();
			if(ok) {
				try {
				job.print();
			} catch (PrinterException ex) {
			
			}
		}
	}
}

