package whiteboard;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JPanel;


public class WhiteboardCanvas extends JPanel implements Runnable, MouseListener, MouseMotionListener {

	public static final int NONE = 0;
	public static final int LINE = 1;
	public static final int CIRCLE = 2;
	public static final int RECT = 3;

	private PrintWriter o;
	private BufferedReader i;
	private int mode;
	private Vector pictures;
	private int tempX, tempY;
	private Thread listener;
	
	public WhiteboardCanvas(String host, int port) {
		pictures = new Vector();
		addMouseListener(this);
		addMouseMotionListener(this);
		try {
			Socket s = new Socket(host, port);
			o = new PrintWriter(s.getOutputStream(), true);
			i = new BufferedReader(new InputStreamReader(s.getInputStream()));
			listener = new Thread(this);
			listener.start();
		} catch (Exception e) {
			PrintDebugMessage.print(e);
		}
	}

	public void paint(Graphics g) {
		super.paint(g);

		int n = pictures.size();

		for (int i = 0; i < n; i++) {
			Drawable d = (Drawable) pictures.elementAt(i);
			d.paint(g);
		}
	}

	public void run() {
		try {
			while (true) {
				String line = i.readLine();
				if(line.equals("!x")) {
					clear();
				} else if( line.equals("!save")) {
					save();
				} else if( line.equals("!load")) {
					load();
				} else if( line.equals("!print")) {
					 new Printer(pictures);
				} else if (line != null) {
					draw(line);
				}
			}
		} catch (Exception e) {
			PrintDebugMessage.print(e);
		}
	}
	

	public void send(String msg) {
		o.println(msg);
	}

	public void clear() {
		pictures.removeAllElements();
		repaint();
	}

	public void draw(String data) {
		PrintDebugMessage.print(data);
		
		int d[] = new int[5];
		StringTokenizer st = new StringTokenizer(data, ":", false);
		int index = 0;
		while (st.hasMoreTokens()) {
			d[index] = Integer.parseInt(st.nextToken());
			index++;
		}
		switch (d[0]) {
		case NONE:
			break;
		case LINE:
			pictures.addElement(new Line(d[1], d[2], d[3], d[4]));
			break;
		case CIRCLE:
			pictures.addElement(new Circle(d[1], d[2], d[3], d[4]));
			break;
		case RECT:
			pictures.addElement(new Rect(d[1], d[2], d[3], d[4]));
			break;
		}
		repaint();
	}

	public void setMode(int m) {
		mode = m;
	}
	
	public void save() {
		try {
			OutputStream output = new FileOutputStream("a.txt");
			for (int i = 0; i < pictures.size(); i++) {
				if(pictures.elementAt(i) instanceof Line) {
					String str = pictures.elementAt(i).toString() + "\n";
					byte[] by = str.getBytes();
					output.write(by);
				} else if(pictures.elementAt(i) instanceof Circle ) {
					String str = pictures.elementAt(i).toString() + "\n";
					byte[] by = str.getBytes();
					output.write(by);
				} else if(pictures.elementAt(i) instanceof Rect ) {
					String str = pictures.elementAt(i).toString() + "\n";
					byte[] by = str.getBytes();
					output.write(by);
				}
			}
			output.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void load() {
		try{
			clear();
            //파일 객체 생성
            File file = new File("a.txt");
            //입력 스트림 생성
            FileReader filereader = new FileReader(file);
            //입력 버퍼 생성
            BufferedReader bufReader = new BufferedReader(filereader);
            String line = "";
            while((line = bufReader.readLine()) != null){
        		int d[] = new int[5];
        		StringTokenizer st = new StringTokenizer(line, ",", false);
        		int index = 0;
        		while (st.hasMoreTokens()) {
        			d[index] = Integer.parseInt(st.nextToken());
        			index++;
        		}
        		if(d[0] == 1) {
        			pictures.addElement(new Line(d[1], d[2], d[3], d[4]));
        		} else if(d[0] == 2) {
        			pictures.addElement(new Circle(d[1], d[2], d[3], d[4]));
        		} else if(d[0] == 3) {
        			pictures.addElement(new Rect(d[1], d[2], d[3], d[4]));
        		}
            }
            //.readLine()은 끝에 개행문자를 읽지 않는다.            
            bufReader.close();
            repaint();
        }catch (FileNotFoundException e) {
            // TODO: handle exception
        }catch(IOException e){
            System.out.println(e);
        }
	}
	

	@Override
	public void mouseDragged(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		String msg;
		switch(mode) {
		case NONE:
			return;
		case LINE:
			msg = LINE + ":" + tempX + ":" + tempY + ":" + x + ":" + y;
			send(msg);
			tempX = x;
			tempY = y;
			break;
		default:
			return;
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		String msg;
		switch (mode) {
		default:
			return;
		case LINE:
			tempX = x;
			tempY = y;
			break;
		case CIRCLE:
			msg = CIRCLE + ":" + x + ":" + y + ":" + "10" + ":" + "10";
			send(msg);
			break;
		case RECT:
			msg = RECT + ":" + x + ":" + y + ":" + "10" + ":" + "10";
			send(msg);
			break;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}
}
