package com.fresco;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

public class PanelBanda extends JComponent {

	private static final long serialVersionUID = 1L;
	private final static int FPS = 30;
	private final static int TARGET_TIME = 1_000_000_000 / FPS;

	private Graphics2D g2;
	private BufferedImage image;
	private int widthPanel;
	private int heightPanel;
	private boolean start = true;
	private LocalTime hora1, hora2;

	public void start() {
		widthPanel = getWidth();
		heightPanel = getHeight();
		image = new BufferedImage(widthPanel, heightPanel, BufferedImage.TYPE_INT_ARGB);
		g2 = image.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		hora1 = LocalTime.now();
		hora2 = hora1.plus(30, ChronoUnit.SECONDS);
		var thread = new Thread(() -> {
			while (start) {
				long startTime = System.nanoTime();
				drawBackGround();
				drawBanda();

				render();
				long endTime = System.nanoTime();
				long time = endTime - startTime;
				if (time < TARGET_TIME) {
					long sleep = (TARGET_TIME - time) / 1_000_000;
					sleep(sleep);
				}
			}
		});
		thread.start();
	}

	private void drawBackGround() {
		g2.setColor(Color.BLACK);
		g2.fillRect(0, 0, widthPanel, heightPanel);
	}

	private void drawBanda() {
		hora1 = LocalTime.now();
		
		long elapsedSeconds = Duration.between(hora1, hora2).toSecondsPart();
		long elapsedMillis = Duration.between(hora1, hora2).toMillisPart();
		int y = (int)(Math.abs(elapsedSeconds - 30) * 1000  + (1000 - elapsedMillis));
		y = y  / 100;

		int height = (int) (300 - (y * 2));
		if (height > 0) {
			g2.setColor(Color.BLUE);
			g2.fillRect(0, y + 300, widthPanel, height);			
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, null);
	}

	private void sleep(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException ex) {
			System.out.println(ex);
		}
	}

	private void render() {
		SwingUtilities.invokeLater(() -> {
			repaint();	
		});
	}
}
