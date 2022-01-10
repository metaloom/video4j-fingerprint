package io.metaloom.video4j.fingerprint;

import io.metaloom.video4j.Video;

/**
 * Fingerprinter which can be used to generate media fingerprints.
 */
public interface VideoFingerprinter<T extends Fingerprint> {

	/**
	 * Hash the video and return the fingerprint.
	 * 
	 * @param video
	 * @return
	 * @throws InterruptedException
	 */
	T hash(Video video) throws InterruptedException;

	/**
	 * Hash the given video and use the preview handler to hook into the hashing processes.
	 * 
	 * @param video
	 * @param handler
	 * @return
	 * @throws InterruptedException
	 */
	T hash(Video video, PreviewHandler handler) throws InterruptedException;

	/**
	 * Set the skip factor which will influence what section of the video should be chosen for fingerprinting.
	 * 
	 * @param skipFactor
	 */
	void setSkipFactor(double skipFactor);

	/**
	 * Set the stacking factor which will influence the stacking of frames.
	 * 
	 * @param factor
	 */
	void setStackFactor(double factor);

}
