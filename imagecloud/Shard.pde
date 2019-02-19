class Shard {
  PVector target;
  PVector origin;
  PVector current;

  float jitter;

  float targetScale;
  float startScale;
  float currentScale;

  float targetRotate;
  float currentRotate;
  float rotateSpeed;

  float speed;
  PImage image;
  int imageIndex;

  Shard(PImage img) {
    origin = new PVector(width/2, height/2);
    target = new PVector(random(width), random(height));
    current = new PVector(origin.x, origin.y);
    currentScale = 1.0;
    startScale = 1.0;
    targetScale = 1.0;
    speed = 0;
    imageIndex = 0;

    targetRotate = 0;
    currentRotate = 0;

    image = img;
  }

  void triggerPulse() {
    targetScale = random(1.0);
    target = new PVector(random(width), random(height));
  }

  void triggerRotate() {
    targetRotate = random(360.0);
  }

  void resetRotate() {
    targetRotate = 0;
  }

  void setImage(PImage img) {
    image = img;
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

    pushMatrix();
    translate(current.x, current.y);
    rotate(radians(currentRotate + jitter));
    image(image, 0,  0, image.width * currentScale, image.height * currentScale);
    // image(image, current.x, current.y, image.width * currentScale, image.height * currentScale);
    popMatrix();
  }
}