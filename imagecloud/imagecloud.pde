int shardCount = 100;
int imageCount = 1;
PImage[] images = new PImage[imageCount];
PGraphics mask;
Shard[] shards = new Shard[shardCount];

void setup() {
  // fullScreen(P3D, 2);
  size(800, 600, P2D);

  for (int i = 0; i < shardCount; i++) {
    shards[i] = new Shard("square.png");
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
      shards[i].setImage("square.png");
    }
  }
}

void draw(){
  background(0);

  for (int i = 0; i < shardCount; i++) {
    shards[i].display();
  }
}