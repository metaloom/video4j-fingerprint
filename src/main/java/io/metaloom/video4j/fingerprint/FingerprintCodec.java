package io.metaloom.video4j.fingerprint;

public interface FingerprintCodec {

	byte[] encode(Fingerprint fingerprint);

	Fingerprint decode(byte[] bytes);

}
