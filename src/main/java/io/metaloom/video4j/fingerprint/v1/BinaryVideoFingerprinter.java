package io.metaloom.video4j.fingerprint.v1;

import io.metaloom.video4j.fingerprint.VideoFingerprinter;

public interface BinaryVideoFingerprinter extends VideoFingerprinter<BinaryFingerprint> {

	/**
	 * Set the skip factor which will influence what section of the video should be chosen for fingerprinting.
	 * 
	 * @param skipFactor
	 */
	void setSkipFactor(double skipFactor);

}
