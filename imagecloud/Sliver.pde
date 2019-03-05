class Sliver extends Shape {
    Sliver(String[] imagePaths, int imageCnt) {
    super(imagePaths, imageCnt);
    setMasks();
  }

  // Shape implementation methods
  void triggerPulse() {
    targetScale = random(2.0);
    targetPosition = new PVector(random(width), random(height));
  }

  void triggerMove() {
    targetRotation = random(-360.0, 360.0);
    targetScale = random(0.8, 1.2);
  }

  void triggerRotation() {
    // Do nothing
  }

  void reset() {
    targetRotation = 0;
  }

  void setMasks() {
    for (int i = 0; i < imageCount; i++) {
      // Create a mask and draw a random triangle on it
      PGraphics mask = createGraphics(images[i].width, images[i].height);
      mask.beginDraw();
      mask.triangle(random(mask.width), random(mask.height), random(mask.width), random(mask.height), random(mask.width), random(mask.height));
      mask.endDraw();
      images[i].mask(mask);
    }
  }

  void update() {
    // Check for boundaries and set target to origin
    if (currentPosition.x == targetPosition.x || currentPosition.y == targetPosition.y) {
      // target = new PVector(origin.x, origin.y);
      // targetScale = startScale;
      movementSpeed = 0;
    }
  }
}