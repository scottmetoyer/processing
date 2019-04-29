class Sliver extends Shape {
    Sliver(String[] imagePaths, int imageCnt) {
    super(imagePaths, imageCnt);
    setMasks();
  }

  // Shape implementation methods
  void triggerPulse() {
    targetScale = random(4.0);
  }

  void triggerMove() {
    targetPosition = new PVector(random(width), random(height));
  }

  void triggerRotation() {
    // Do nothing
  }

  void reset() {
    targetRotation = 0;
    targetPosition = new PVector(startPosition.x, startPosition.y);
  }

  void setMasks() {
    for (int i = 0; i < imageCount; i++) {
      // Create a mask and draw a random triangle on it
      PGraphics mask = createGraphics(images[i].width, images[i].height);
      mask.beginDraw();
      mask.rect(random(mask.width), random(mask.height), random(5), random(mask.height));
      mask.endDraw();
      images[i].mask(mask);
    }
  }

  void update() {
    // Check for boundaries and set target to origin
    if (currentScale == targetScale) {
      targetScale = startScale;
    }

    if (currentPosition.x == targetPosition.x || currentPosition.y == targetPosition.y) {
      movementSpeed = 0;
    }
  }
}