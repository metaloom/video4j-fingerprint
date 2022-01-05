package io.metaloom.video4j.fingerprint.ui;

import static io.metaloom.video4j.opencv.CVUtils.blowUp;
import static io.metaloom.video4j.opencv.CVUtils.mat2BufferedImage;

import javax.swing.JPanel;

import org.opencv.core.Mat;

import io.metaloom.video4j.ui.ImagePanel;

/**
 * Panel which contains a column of images which represent the individual steps within the fingerprinting process.
 */
public class FPPreviewPanel extends JPanel {

	private static final long serialVersionUID = 5897870530238447030L;

	private ImagePanel sourcePanel;
	private ImagePanel step1Panel;
	private ImagePanel step2Panel;
	private ImagePanel step3Panel;
	private ImagePanel step4Panel;
	private ImagePanel resultPanel;

	private int blowupSize;

	public FPPreviewPanel(int blowupSize) {
		this.blowupSize = blowupSize;
		sourcePanel = new ImagePanel(blowupSize, blowupSize);
		step1Panel = new ImagePanel(blowupSize, blowupSize);
		step2Panel = new ImagePanel(blowupSize, blowupSize);
		step3Panel = new ImagePanel(blowupSize, blowupSize);
		step4Panel = new ImagePanel(blowupSize, blowupSize);
		resultPanel = new ImagePanel(blowupSize, blowupSize);
		add(sourcePanel);
		add(step1Panel);
		add(step2Panel);
		add(step3Panel);
		add(step4Panel);
		add(resultPanel);
	}

	public void setImages(Mat source, Mat step1, Mat step2, Mat step3, Mat step4, Mat result) {
		if (source != null && !source.empty()) {
			sourcePanel.setImage(blowUp(mat2BufferedImage(source), blowupSize, blowupSize));
		}

		if (step1 != null && !step1.empty()) {
			step1Panel.setImage(blowUp(mat2BufferedImage(step1), blowupSize, blowupSize));
		}

		if (step2 != null && !step2.empty()) {
			step2Panel.setImage(blowUp(mat2BufferedImage(step2), blowupSize, blowupSize));
		}

		if (step3 != null && !step3.empty()) {
			step3Panel.setImage(blowUp(mat2BufferedImage(step3), blowupSize, blowupSize));
		}

		if (step4 != null && !step4.empty()) {
			step4Panel.setImage(blowUp(mat2BufferedImage(step4), blowupSize, blowupSize));
		}

		if (result != null && !result.empty()) {
			resultPanel.setImage(blowUp(mat2BufferedImage(result), blowupSize, blowupSize));
		}
	}
}
