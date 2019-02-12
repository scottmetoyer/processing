class Shard {
  PVector target;
  PVector origin;
  PVector current;
  float speed;
  PImage image;

  Shard(String imagePath) {
    origin = new PVector(width/2, height/2);
    target = new PVector(random(width), random(height));
    current = new PVector(origin.x, origin.y);
    speed = 0;
    image = loadImage(imagePath);
  }

  void display() {
    imageMode(CENTER);
    image(image, current.x, current.y);
  }

  void trigger() {
    target = new PVector(random(width), random(height));
  }

  void move() {
    speed += 0.1;

     // Compute the new positions
    current.x = lerp(current.x, target.x, sin(speed));
    current.y = lerp(current.y, target.y, sin(speed));

    if (current.x == target.x || current.y == target.y) {
      target = new PVector(origin.x, origin.y);
      speed = 0;
    }

    if (current.x == origin.x || current.y == origin.y) {
      speed = 0;
    }
  }
}