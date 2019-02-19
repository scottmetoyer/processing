int shardCount = 20;
int imageCount = 3;
PImage[] images = new PImage[imageCount];
Shard[] shards = new Shard[shardCount];

void setup() {
  // fullScreen(P3D, 2);
  size(800, 600, P2D);

  images[0] = loadImage("square.png");
  images[1] = loadImage("cat.png");
  images[2] = loadImage("splash.png");

  for (int i = 0; i < shardCount; i++) {
    shards[i] = new Shard(images[0]);
  }
  smooth();
  frameRate(60);
}

void keyPressed() {
  if (key == ' ') {
    for (int i = 0; i < shardCount; i++) {
      shards[i].triggerPulse();
    }
  }

  if (key == 'z') {
     for (int i = 0; i < shardCount; i++) {
      shards[i].triggerRotate();
    }
  }

  if (key == 'x') {
     for (int i = 0; i < shardCount; i++) {
      shards[i].resetRotate();
    }
  }

  if (key == 'c') {
    int index = int(random(imageCount));
    for (int i = 0; i < shardCount; i++) {
      shards[i].setImage(images[index]);
    }
  }
}

void draw(){
  background(0);

  for (int i = 0; i < shardCount; i++) {
    shards[i].display();
  }
}