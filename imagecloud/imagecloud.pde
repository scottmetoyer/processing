int shardCount = 50;
int imageCount = 1;
int layerCount = 3;
Shard[][] layers = new Shard[layerCount][shardCount];

void setup() {
  // fullScreen(P2D, 2);
  size(800, 600, P2D);

  for (int i = 0; i < shardCount; i++) {
    layers[0][i] = new Shard("b3.png");
    layers[0][i].setOpacity((int)random(128, 255));
    layers[0][i].setScale(1.3);
  }

  for (int i = 0; i < shardCount; i++) {
    layers[1][i] = new Shard("b3.png");
    layers[1][i].setOpacity((int)random(128, 255));
  }

  for (int i = 0; i < shardCount; i++) {
    layers[2][i] = new Shard("b3.png");
    layers[2][i].setOpacity((int)random(128, 255));
    layers[2][i].setScale(0.7);
  }

  smooth();
  frameRate(60);
}

void pulseRandomShards() {
  int layer = (int)random(layerCount);
  int shards = (int)random(shardCount);

  for (int i = 0; i < shards; i++) {
     layers[layer][i].triggerPulse();
  }
}

void pulseAllShards(int layer) {
  for (int i = 0; i < shardCount; i++) {
     layers[layer][i].triggerPulse();
  }
}

void rotateRandomLayer() {
  int layer = (int)random(layerCount);
    for (int i = 0; i < shardCount; i++) {
      layers[layer][i].triggerRotate();
    }
}

void rotateAllLayers() {
  for (int j = 0; j < layerCount; j++) {
    for (int i = 0; i < shardCount; i++) {
      layers[j][i].triggerRotate();
    }
  }
}

void keyPressed() {
  // Pulse random number of shards on random layer
  if (key == 'z') {
    pulseRandomShards();
  }

  // Pulse all shards layer 1
  if (key == 'x') {
    pulseAllShards(0);
  }

  // Pulse all shards layer 2
  if (key == 'c') {
    pulseAllShards(1);
  }

  // Pulse all shards layer 3
  if (key == 'v') {
    pulseAllShards(2);
  }

  // Rotate random layer
  if (key == 'a') {
    rotateRandomLayer();
  }

  // Rotate all layers
  if (key == 's') {
    rotateAllLayers();
  }

  // Toggle layer visible
  if (key == 'd') {
    // toggleVisibility()?
  }
  // Reset rotation?


  /*
  if (key == 'r') {
     for (int i = 0; i < shardCount; i++) {
      layer3[i].setVisible(!(layer3[i].getVisible()));
    }
  }*/
}

void draw(){
  background(0);

  for (int j = 0; j < layerCount; j++) {
    for (int i = 0; i < shardCount; i++) {
      layers[j][i].display();
    }
  }
}