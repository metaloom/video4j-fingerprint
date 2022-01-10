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
Video4j.init();

// Create a fingerprinter for the video
BinaryVideoFingerprinter gen = new BinaryVideoFingerprinter();

// Open the video using the Video4j API
try (Video video = Videos.open("video.mp4")) {

  // Run the actual hashing process
  BinaryFingerprint fingerprint = gen.hash(video);
  String hex = fingerprint.hex();
  // hex = 0001000100ff060006000f002e001d0084000600e40076d172c07c84ffcefffffefff8fffdff
}
```