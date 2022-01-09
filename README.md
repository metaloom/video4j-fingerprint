# Video4j - Fingerprint

This project contains a [Digital video fingerprinting](https://en.wikipedia.org/wiki/Digital_video_fingerprinting) implementation.

Details on the actual fingerprinting process can be found in this dedicated [Blog post](https://metaloom.io/blog/video-fingerprinting/).

The actual fingerprinting process is relatively fast as it does not require complex transformation or edge detection.
Downside of the used stacking approach is however that there is no robustness against image rotation or mirroring of the image. Other solutions like `pHash` might be better if this is a problematic limitation.

![Example Process](examples/processing.gif)

## Maven

```xml
<dependency>
  <groupId>io.metaloom.video</groupId>
  <artifactId>video4j-fingerprint</artifactId>
  <version>1.0.0-SNAPSHOT</version>
</dependency>
```

## Usage

```java
// Open the video using the Video4j API
try (Video video = Videos.open("video.mp4"))) {
  // Create a fingerprinter for the video
  DefaultVideoFingerprinter hasher = new DefaultVideoFingerprinter();

  // Run the actual hashing process
  String hash = hasher.hash().hex();
  // Resulting hash = 038008e00ef0bff0bdf0bdf0fdf0fde0fef07cf8bf13bf00d002f4f0fff8dfb001
}
```