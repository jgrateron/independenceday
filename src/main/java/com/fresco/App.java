package com.fresco;

import java.awt.BorderLayout;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;

public class App {

	private JDesktopPane desk;
	private JInternalFrame frame1, frame2, frame3, frame4;
	private JFrame frame;

	public static void main(String[] args) {
		new App();
	}

	public App() {
		init();
	}

	public void init() {
		frame = new JFrame("Independence Day");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		desk = new JDesktopPane();
		frame1 = new JInternalFrame("...", true, true, true, true);
		frame1.setBounds(15, 20, 430, 700);
		frame1.setVisible(true);
		var panelSenal = new PanelSenal();
		frame1.setLayout(new BorderLayout());
		frame1.add(panelSenal);
		frame2 = new JInternalFrame("...", true, true, true, true);
		frame2.setBounds(450, 20, 430, 700);
		frame2.setVisible(true);
		var panelBanda = new PanelBanda();
		frame2.setLayout(new BorderLayout());
		frame2.add(panelBanda);
		frame3 = new JInternalFrame("...", true, true, true, true);
		frame3.setBounds(880, 20, 430, 700);
		frame3.setVisible(true);
		frame4 = new JInternalFrame("Time Remaining", true, true, true, true);
		frame4.setBounds(20, 80, 1285, 300);
		frame4.setLayout(new BorderLayout());
		var panelTemp = new PanelTemporizador();
		frame4.add(panelTemp);
		frame4.setVisible(true);
		desk.add(frame4);
		desk.add(frame1);
		desk.add(frame2);
		desk.add(frame3);
		frame.add(desk);
		frame.setSize(1320, 768);
		frame.setVisible(true);
		var thread = new Thread(() -> {
			sleep(5000);
			panelBanda.start();
			panelTemp.start();
			panelSenal.start();
		});
		thread.start();
	}

	private void sleep(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException ex) {
			System.out.println(ex);
		}
	}
}
