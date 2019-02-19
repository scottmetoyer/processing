// https://discourse.processing.org/t/tips-for-speeding-up-this-sketch/8468
int many =10;
Shard[] shards = new Shard[many];
int showid = 0;

void setup() {
  size(800, 600);
  for (int i = 0; i <many; i++) shards[i] = new Shard("square.png", i);
}

void keyPressed() {
  if (key == ' ') {
    for (int i = 0; i <many; i++)  shards[i].triggerPulse();
    showid = (int)random(many);                               // select next random picture to show
    println("new pic "+showid);
  }
}

void draw() {
  surface.setTitle("my gallery "+nf(frameRate,1,1));
  background(200, 200, 0);
  for (int i = 0; i < many; i++) shards[i].moveAndshow();
}


class Shard {
  PVector target;
  PVector origin;
  PVector current;

  float targetScale;
  float startScale;
  float currentScale;

  float speed;
  PImage image;
  int id;

  Shard(String imagePath, int id) {
    origin = new PVector(width/2, height/2);
    target = new PVector(random(width), random(height));
    current = new PVector(origin.x, origin.y);
    currentScale = 1.0;
    startScale = 1.0;
    targetScale = 1.0;
    speed = 0;
    image = loadImage(imagePath);
    this.id = id;
  }

  void display() {
    imageMode(CENTER);
    image(image, current.x, current.y, image.width * currentScale, image.height * currentScale);
    fill(0);
    text(id, width/2, height/2);
  }

  void triggerPulse() {
    target = new PVector(random(width), random(height));
    targetScale = random(2.0);
  }

  void moveAndshow() {
    if (current.x == target.x || current.y == target.y) {
      target = new PVector(origin.x, origin.y);
      targetScale = startScale;
      speed = 0;
    } else {
      speed += 0.1;
      //                                                              Compute the new positions
      current.x = lerp(current.x, target.x, sin(speed));
      current.y = lerp(current.y, target.y, sin(speed));
      currentScale = lerp(currentScale, targetScale, sin(speed));
    }

    if (current.x == origin.x || current.y == origin.y)   speed = 0;
    // show it from here, not need extra call from draw
    if ( speed > 0 )         display();                                 // show all while moving     // very slow               
    else if ( id == showid ) display();                                 // show only one random pic
  }
}
