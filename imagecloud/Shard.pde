class Shard {
  PVector target;
  PVector origin;
  PVector current;

  float jitter;
  int opacity;
  boolean visible;

  float targetScale;
  float startScale;
  float currentScale;

  float targetRotate;
  float currentRotate;
  float rotateSpeed;

  float speed;
  PImage image;
  int imageIndex;

  Shard(String imagePath) {
    int spread = 25;
    origin = new PVector(width/2 + random(spread * -1, spread), height/2 + random(spread * -1, spread));
    target = new PVector(random(width), random(height));
    current = new PVector(origin.x, origin.y);
    currentScale = 1.0;
    startScale = 1.0;
    targetScale = 1.0;
    speed = 0;
    imageIndex = 0;
    opacity = 255;
    visible = true;

    targetRotate = 0;
    currentRotate = 0;

    setImage(imagePath);
  }

  void triggerPulse() {
    targetScale = random(2.0);
    target = new PVector(random(width), random(height));
  }

  void triggerRotate() {
    targetRotate = random(-360.0, 360.0);
    targetScale = random(0.8, 1.2);
  }

  void resetRotate() {
    targetRotate = 0;
  }

  void setVisible(boolean isVisible) {
    visible = isVisible;
  }

  boolean getVisible() {
    return visible;
  }

  void setImage(String imagePath) {
    image = loadImage(imagePath);
    image.resize(width/3, 0);

    // Create a mask and draw a random triangle on it
    PGraphics mask = createGraphics(image.width, image.height);
    mask.beginDraw();
    mask.triangle(random(mask.height), random(mask.width), random(mask.height), random(mask.width), random(mask.height), random(mask.width));
    mask.endDraw();

    image.mask(mask);
  }

  void setOpacity(int newOpacity) {
    opacity = newOpacity;
  }
  void setScale(float newScale) {
    startScale = newScale;
  }

  void display() {
    imageMode(CENTER);
    speed += 0.1;

    // Calculate some jitter
    jitter = random(-1, 1);
    // jitter = 0;

    // Compute the new positions
    current.x = lerp(current.x, target.x, sin(speed));
    current.y = lerp(current.y, target.y, sin(speed));
    currentScale = lerp(currentScale, targetScale, sin(speed));

    if (current.x == target.x || current.y == target.y) {
      target = new PVector(origin.x, origin.y);
      targetScale = startScale;
      speed = 0;
    }

    if (current.x == origin.x || current.y == origin.y) {
      speed = 0;
    }

    // Compute the new rotate
    rotateSpeed += 0.2;
    currentRotate = lerp(currentRotate, targetRotate, sin(rotateSpeed));

    if (currentRotate == targetRotate) {
      rotateSpeed = 0;
    }

    if (visible) {
      pushMatrix();
      translate(current.x, current.y);
      rotate(radians(currentRotate + jitter));
      tint(255, opacity);
      image(image, 0,  0, image.width * currentScale, image.height * currentScale);
      popMatrix();
    }
  }
}