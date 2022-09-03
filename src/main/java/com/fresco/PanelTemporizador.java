package com.fresco;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

public class PanelTemporizador extends JComponent  {
	private static final long serialVersionUID = 1L;
	private final static int FPS = 15;
	private final static int TARGET_TIME = 1_000_000_000 / FPS;

	private Graphics2D g2;
	private BufferedImage image;
	private int widthPanel;
	private int heightPanel;
	private boolean start = true;
	private ClassLoader classLoader = ClassLoader.getSystemClassLoader();
	private LocalTime hora1, hora2;

	public void start() {
		widthPanel = getWidth();
		heightPanel = getHeight();
		image = new BufferedImage(widthPanel, heightPanel, BufferedImage.TYPE_INT_ARGB);
		g2 = image.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		try {
			var is = classLoader.getResource("digital-7.mono.ttf").openStream();
			Font font = Font.createFont(Font.TRUETYPE_FONT, is);
			Font sizedFont = font.deriveFont(200f);
			g2.setFont(sizedFont);
		} catch (FontFormatException | IOException e) {
			System.err.println(e.getMessage());
			g2.setFont(new Font("Monospaced", Font.PLAIN, 18));
		}
		hora1 = LocalTime.now();
		hora2 = hora1.plus(15, ChronoUnit.SECONDS);
		var thread = new Thread(() -> {
			while (start) {
				long startTime = System.nanoTime();
				drawBackGround();
				drawTemporizador();

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

	private void drawTemporizador() {
		hora1 = LocalTime.now();
		long elapsedHours = Duration.between(hora1, hora2).toHoursPart();
		long elapsedMinutes = Duration.between(hora1, hora2).toMinutesPart();
		long elapsedSeconds = Duration.between(hora1, hora2).toSecondsPart();
		long elapsedMillis = Duration.between(hora1, hora2).toMillisPart() / 10;
		if (elapsedSeconds < 0) {
			elapsedHours = 0;
			elapsedMinutes = 0;
			elapsedSeconds = 0;
			elapsedMillis = 0;
		}
		var time = String.format("%02d:%02d:%02d:%02d", elapsedHours, elapsedMinutes, elapsedSeconds, elapsedMillis);
		g2.setColor(Color.GREEN);
		g2.drawString(time, 50, 200);
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

	private void drawBackGround() {
		g2.setColor(Color.BLACK);
		g2.fillRect(0, 0, widthPanel, heightPanel);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, null);
	}
}
