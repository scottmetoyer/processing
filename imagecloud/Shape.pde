abstract class Shape {
  PVector targetPosition;
  PVector startPosition;
  PVector currentPosition;

  float targetScale;
  float startScale;
  float currentScale;

  float targetRotation;
  float currentRotation;


  int opacity;
  float movementSpeed;
  float rotationSpeed;
  boolean visible;

  PImage images[];
  int imageIndex;
  int imageCount;

  Shape(String[] imagePaths, int imageCnt) {
    int spread = 25;
    // origin = new PVector(width/2 + random(spread * -1, spread), height/2 + random(spread * -1, spread));

    startPosition = new PVector(width/2, height/2);
    targetPosition = new PVector(startPosition.x, startPosition.y);
    currentPosition = new PVector(startPosition.x, startPosition.y);

    currentScale = 1.0;
    startScale = 1.0;
    targetScale = 1.0;

    movementSpeed = 0;
    rotationSpeed = 0;

    imageIndex = 0;
    opacity = 255;
    visible = true;

    targetRotation = 0;
    currentRotation = 0;

    imageCount = imageCnt;
    imageIndex = 0;
    images = new PImage[imageCnt];

    for (int i = 0; i < imageCount; i++) {
      setImage(imagePaths[i], i);
    }
  }

  void triggerPulse() {
    targetScale = random(2.0);
  }

  void triggerMove() {
    targetPosition = new PVector(random(width), random(height));
  }

  void triggerRotation() {
    targetRotation = random(-360.0, 360.0);
  }

  void reset() {
    targetRotation = 0;
    targetScale = 1.0;
    targetPosition = new PVector(startPosition.x, startPosition.y);
  }

  void setVisible(boolean isVisible) {
    visible = isVisible;
  }

  boolean getVisible() {
    return visible;
  }

  void setOpacity(int newOpacity) {
    opacity = newOpacity;
  }

  int getOpacity() {
    return opacity;
  }

  void setSize(float newSize) {
    startScale = newSize;
  }

  float getScale() {
    return startScale;
  }

  void setImage(String imagePath, int idx) {
    images[idx] = loadImage(imagePath);
    PImage image = images[idx];
    image.resize(width/3, 0);
  }

  void setImageIndex(int newIndex) {
    imageIndex = newIndex;
  }

  void update() {
    // Override this in the inherited class and update every frame as needed. Check for movement boundaries, etc.
  }

  void display() {
    // Calculate some jitter
    float jitter = random(-1, 1);
    // jitter = 0;

    imageMode(CENTER);
    movementSpeed += 0.1;

    // Compute the new position and scale
    currentPosition.x = lerp(currentPosition.x, targetPosition.x, sin(movementSpeed));
    currentPosition.y = lerp(currentPosition.y, targetPosition.y, sin(movementSpeed));
    currentScale = lerp(currentScale, targetScale, sin(movementSpeed));

    // Compute the new rotation
    rotationSpeed += 0.2;
    currentRotation = lerp(currentRotation, targetRotation, sin(rotationSpeed));

    if (currentRotation == targetRotation) {
      rotationSpeed = 0;
    }

    if (currentPosition.x == startPosition.x || currentPosition.y == startPosition.y) {
      movementSpeed = 0;
    }

    if (visible) {
      pushMatrix();
      translate(currentPosition.x, currentPosition.y);
      rotate(radians(currentRotation + jitter));
      tint(255, opacity);
      image(images[imageIndex], 0,  0, images[imageIndex].width * currentScale, images[imageIndex].height * currentScale);
      popMatrix();
    }
  }
}