package io.metaloom.video4j.fingerprint;

import org.junit.BeforeClass;

import io.metaloom.video4j.Video4j;

public abstract class AbstractMediaTest {

	public static final String BIG_BUCK_BUNNY_PATH = "src/test/resources/Big_Buck_Bunny_720_10s_30MB.mp4";

	public static final String BBB_SMALL = "src/test/resources/BigBuckBunny-128x96.mp4";
	public static final String BBB_MEDIUM = "src/test/resources/BigBuckBunny-320x240.mp4";
	public static final String BBB_LARGE = "src/test/resources/BigBuckBunny-480x360.mp4";

	@BeforeClass
	public static void setup() {
		Video4j.init();
	}

}
