Shard[] shards = new Shard[10];

void setup() {
  // fullScreen(P3D, 2);
  size(800, 600);

  for (int i = 0; i < 10; i++) {
    shards[i] = new Shard("square.png");
  }
  smooth();
  frameRate(60);
}

void keyPressed() {
  for (int i = 0; i < 10; i++) {
    shards[i].trigger();
  }
}

void draw(){
  background(0);

  for (int i = 0; i < 10; i++) {
    shards[i].move();
    shards[i].display();
  }

  // tint(255, 255);
  // rotate(radians(0));
  // scale(1.5);

}
