int shardCount = 50;
int imageCount = 1;
Shard[] layer1 = new Shard[shardCount];
Shard[] layer2 = new Shard[shardCount];
Shard[] layer3 = new Shard[shardCount];

void setup() {
  // fullScreen(P2D, 2);
  size(800, 600, P2D);

  for (int i = 0; i < shardCount; i++) {
    layer1[i] = new Shard("r1.png");
    layer1[i].setOpacity((int)random(128, 255));
    layer1[i].setScale(1.3);
  }

  for (int i = 0; i < shardCount; i++) {
    layer2[i] = new Shard("r2.png");
    layer2[i].setOpacity((int)random(128, 255));
  }

  for (int i = 0; i < shardCount; i++) {
    layer3[i] = new Shard("r3.png");
    layer3[i].setOpacity((int)random(128, 255));
    layer3[i].setScale(0.7);
  }

  smooth();
  frameRate(60);
}

void keyPressed() {
  if (key == 'z') {
    for (int i = 0; i < shardCount; i++) {
      layer1[i].triggerPulse();
    }
  }

  if (key == 'x') {
     for (int i = 0; i < shardCount; i++) {
      layer1[i].triggerRotate();
    }
  }

  if (key == 'c') {
     for (int i = 0; i < shardCount; i++) {
      layer1[i].resetRotate();
    }
  }

  if (key == 'v') {
     for (int i = 0; i < shardCount; i++) {
      layer1[i].setVisible(!(layer1[i].getVisible()));
    }
  }

  if (key == 'a') {
    for (int i = 0; i < shardCount; i++) {
      layer2[i].triggerPulse();
    }
  }

  if (key == 's') {
     for (int i = 0; i < shardCount; i++) {
      layer2[i].triggerRotate();
    }
  }

  if (key == 'd') {
     for (int i = 0; i < shardCount; i++) {
      layer2[i].resetRotate();
    }
  }

  if (key == 'f') {
     for (int i = 0; i < shardCount; i++) {
      layer2[i].setVisible(!(layer2[i].getVisible()));
    }
  }

  if (key == 'q') {
    for (int i = 0; i < shardCount; i++) {
      layer3[i].triggerPulse();
    }
  }

  if (key == 'w') {
     for (int i = 0; i < shardCount; i++) {
      layer3[i].triggerRotate();
    }
  }

  if (key == 'e') {
     for (int i = 0; i < shardCount; i++) {
      layer3[i].resetRotate();
    }
  }

  if (key == 'r') {
     for (int i = 0; i < shardCount; i++) {
      layer3[i].setVisible(!(layer3[i].getVisible()));
    }
  }
}

void draw(){
  background(0);

  for (int i = 0; i < shardCount; i++) {
    layer1[i].display();
  }

  for (int i = 0; i < shardCount; i++) {
    layer2[i].display();
  }

  for (int i = 0; i < shardCount; i++) {
    layer3[i].display();
  }
}