package io.metaloom.video4j.fingerprint.ui;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;

import org.opencv.core.Mat;

import io.metaloom.utils.hash.HashUtils;
import io.metaloom.video4j.fingerprint.VideoFingerprinter;

/**
 * A small UI which contains sliders to control the individual parameters of the video fingerprinter.
 */
public class FingerprintDebugUI {

	private List<VideoFingerprinter> hashers = new ArrayList<>();
	private Map<VideoFingerprinter, FPPreviewPanel> vidsPanels = new HashMap<>();

	private JFrame frame = new JFrame("Video Title");
	private JPanel listPanel = new JPanel();

	private int blowupSize;

	public FingerprintDebugUI(int blowupSize) {
		this.blowupSize = blowupSize;
	}

	public void show() {

		JPanel controlPanel = new JPanel();
		controlPanel.add(createSkipSlider());
		controlPanel.add(createContrastSlider());
		controlPanel.add(createStackFactorSlider());
		controlPanel.add(createPlayButton());

		listPanel.setName("Video");
		frame.setLayout(new FlowLayout());
		for (VideoFingerprinter hasher : hashers) {
			FPPreviewPanel preview = new FPPreviewPanel(blowupSize);
			vidsPanels.put(hasher, preview);
			frame.add(preview);
		}

		frame.add(controlPanel);

		// frame.setContentPane(listPanel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(blowupSize * 6 + 120, (blowupSize + 10) * (hashers.size()) + 110);
		frame.setVisible(true);

	}

	private Component createPlayButton() {
		ImageIcon playButtonIcon = createImageIcon("/images/play.gif");
		JButton playButton = new JButton("Play", playButtonIcon);
		playButton.addActionListener(event -> {
			try {
				for (VideoFingerprinter hasher : hashers) {
					byte[] hash = hasher.hash((a, b, c, d, e, f) -> refresh(hasher, a, b, c, d, e, f));
					if (hash != null) {
						System.out.println("Len: " + hash.length);
						System.out.println("Hex: " + HashUtils.bytesToHex(hash));
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		return playButton;
	}

	private Component createSkipSlider() {
		JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
		slider.addChangeListener(event -> {
			double val = (double) slider.getValue();
			double factor = val / 100f;
			System.out.println("SkipFactor: " + factor);
			for (VideoFingerprinter hasher : hashers) {
				hasher.setSkipFactor(factor);
			}
		});
		slider.setName("Skip");
		slider.setMajorTickSpacing(2550);
		slider.setMinorTickSpacing(1);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
		Font font = new Font("Serif", Font.ITALIC, 15);
		slider.setFont(font);
		return slider;

	}

	private Component createContrastSlider() {
		JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 256, 128);
		slider.addChangeListener(event -> {
			int val = slider.getValue();
			double contrastLevel = (double) val / 64f;
			System.out.println("Level: " + contrastLevel);
			for (VideoFingerprinter hasher : hashers) {
				// hasher.setContrastAlpha(contrastLevel);
			}
		});

		slider.setMajorTickSpacing(50);
		slider.setMinorTickSpacing(1);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
		Font font = new Font("Serif", Font.ITALIC, 15);
		slider.setFont(font);
		return slider;
	}

	private Component createStackFactorSlider() {
		JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 256, 128);
		slider.addChangeListener(event -> {
			int val = slider.getValue();
			double factor = (double) val / 256f;
			System.out.println("StackFactor: " + factor);
			for (VideoFingerprinter hasher : hashers) {
				hasher.setStackFactor(factor);
			}
		});

		slider.setMajorTickSpacing(50);
		slider.setMinorTickSpacing(1);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
		Font font = new Font("Serif", Font.ITALIC, 15);
		slider.setFont(font);
		return slider;
	}

	protected static ImageIcon createImageIcon(String path) {
		URL imgURL = VideoFingerprinter.class.getResource(path);
		return new ImageIcon(imgURL);
	}

	public void refresh(VideoFingerprinter hasher, Mat a, Mat b, Mat c, Mat d, Mat e, Mat f) {
		FPPreviewPanel preview = vidsPanels.get(hasher);
		preview.setImages(a, b, c, d, e, f);
		preview.repaint();
	}

	public void add(VideoFingerprinter hasher) {
		hashers.add(hasher);
	}
}
