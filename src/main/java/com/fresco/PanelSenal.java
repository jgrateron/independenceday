package com.fresco;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

public class PanelSenal extends JComponent {
	private static final long serialVersionUID = 1L;
	private final static int FPS = 30;
	private final static int TARGET_TIME = 1_000_000_000 / FPS;

	private Graphics2D g2;
	private BufferedImage image;
	private int widthPanel;
	private int heightPanel;
	private boolean start = true;
	private Random random;
	private LocalTime hora1, hora2;
	
	public void start() {
		random = new Random();
		widthPanel = getWidth();
		heightPanel = getHeight();
		image = new BufferedImage(widthPanel, heightPanel, BufferedImage.TYPE_INT_ARGB);
		g2 = image.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		hora1 = LocalTime.now();
		hora2 = hora1.plus(15, ChronoUnit.SECONDS);
		var thread = new Thread(() -> {
			while (start) {
				long startTime = System.nanoTime();
				drawBackGround();
				drawSenal();

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

	private void drawSenal() {
		int minx = 0;
		int maxx = widthPanel;
		int miny = 0;
		int maxy = heightPanel;
		hora1 = LocalTime.now();
		long elapsedSeconds = Duration.between(hora1, hora2).toSecondsPart();
		long elapsedMillis = Duration.between(hora1, hora2).toMillisPart();
		long puntos = elapsedSeconds * 1000 + elapsedMillis;
		if (puntos > 0) {
			for (int i = 0; i < puntos; i++) {
				int x = random.nextInt(maxx + minx) + minx;
				int y = random.nextInt(maxy + miny) + miny;
				image.setRGB(x, y, Color.GREEN.getRGB());
			}			
		}
	}

	private void drawBackGround() {
		g2.setColor(Color.BLACK);
		g2.fillRect(0, 0, widthPanel, heightPanel);
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
