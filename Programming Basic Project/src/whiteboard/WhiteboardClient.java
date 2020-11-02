package whiteboard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JToolBar;

public class WhiteboardClient extends JFrame implements ActionListener {

	private WhiteboardCanvas canvas;
	private JButton line, oval, clear, rect, save, load, print;

	public WhiteboardClient(String host, int port) {
		super("화이트보드 클라이언트");
		JToolBar tools = new JToolBar();
		line = new JButton("	선	");
		line.addActionListener(this);
		oval = new JButton("	원	");
		oval.addActionListener(this);
		rect = new JButton("사각형");
		rect.addActionListener(this);
		clear = new JButton("지우기");
		clear.addActionListener(this);
		save = new JButton("저장");
		save.addActionListener(this);
		load = new JButton("불러오기");
		load.addActionListener(this);
		print = new JButton("출력");
		print.addActionListener(this);
		tools.add(line);
		tools.add(oval);
		tools.add(rect);
		tools.addSeparator();
		tools.add(clear);
		tools.addSeparator();
		tools.add(save);
		tools.add(load);
		tools.addSeparator();
		tools.add(print);

		canvas = new WhiteboardCanvas(host, port);

		getContentPane().add("North", tools);
		getContentPane().add("Center", canvas);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(800, 600);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object c = e.getSource();
		if(c == line) {
			canvas.setMode(WhiteboardCanvas.LINE);
		} else if( c == oval) {
			canvas.setMode(WhiteboardCanvas.CIRCLE);
		} else if( c== rect) {
			canvas.setMode(WhiteboardCanvas.RECT);
		} else if( c == clear) {
			canvas.send("!x");
		} else if( c == save) {
			canvas.send("!save");
		} else if( c == load ) {
			canvas.send("!load");
		} else if( c == print) {
			canvas.send("!print");
		}
	}

	public static void main(String[] args) {
		new WhiteboardClient("localhost", 9850);
	}
}
