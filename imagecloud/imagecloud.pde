import processing.serial.*;

int shardCount = 50;
int layerCount = 3;
int triggerCount = 0;
int triggerThreshold = 16;
Shard[][] layers = new Shard[layerCount][shardCount];

Serial serialPort;
String serialInput;
int imageCount = 3;
int currentImageIndex = 0;

void setup() {
  // fullScreen(P2D);
  size(800, 600, P2D);
  String[] images = { "b1.jpg", "b2.jpg", "b3.jpg" };

  for (int i = 0; i < shardCount; i++) {
    layers[0][i] = new Shard(images, imageCount);
    layers[0][i].setOpacity((int)random(128, 255));
    layers[0][i].setScale(1.3);
  }

  for (int i = 0; i < shardCount; i++) {
    layers[1][i] = new Shard(images, imageCount);
    layers[1][i].setOpacity((int)random(128, 255));
  }

  for (int i = 0; i < shardCount; i++) {
    layers[2][i] = new Shard(images, imageCount);
    layers[2][i].setOpacity((int)random(128, 255));
    layers[2][i].setScale(0.7);
  }

  //smooth();
  frameRate(60);

  // Open up the serial port
  String portName = Serial.list()[0];
  serialPort = new Serial(this, portName, 9600);
}

int randomExcept(int top, int exclude) {
  int number;

  do {
    number = (int)random(top);
  } while (number == exclude);

  return number;
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

void setRandomImage() {
  currentImageIndex = randomExcept(imageCount, currentImageIndex);

  for (int j = 0; j < layerCount; j++) {
    for (int i = 0; i < shardCount; i++) {
      layers[j][i].setImageIndex(currentImageIndex);
    }
  }
}

void resetRotation() {
  for (int j = 0; j < layerCount; j++) {
    for (int i = 0; i < shardCount; i++) {
      layers[j][i].resetRotate();
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
    resetRotation();
  }

  // Reset rotation?
  if (key == 'f') {
    setRandomImage();
  }

  /*
  if (key == 'r') {
     for (int i = 0; i < shardCount; i++) {
      layer3[i].setVisible(!(layer3[i].getVisible()));
    }
  }*/
}

void processInput(String input) {
  int randomChoice;

  switch(input) {
    case "trigger_1":
    if (triggerCount++ >= triggerThreshold) {
      triggerCount = 0;
      resetRotation();
    } else {
      randomChoice = (int)random(2);

      if (randomChoice == 0) {
        rotateRandomLayer();
      } else {
        rotateAllLayers();
      }
    }
    break;

    case "trigger_2":
    pulseRandomShards();

    randomChoice = (int)random(6);
    if (randomChoice == 0) {
      setRandomImage();
    }
    break;

    case "trigger_3":
    randomChoice = (int)random(6);

    if (randomChoice == 5) {
      pulseAllShards(0);
      pulseAllShards(1);
      pulseAllShards(2);
    } else if (randomChoice == 4) {
      pulseAllShards(1);
      pulseAllShards(2);
    } else if (randomChoice == 3) {
      pulseAllShards(1);
      pulseAllShards(2);
    } else {
      pulseAllShards(randomChoice);
    }
    break;
  }
}

void draw(){
  // Check for serial data
  if (serialPort.available() > 0) {
    serialInput = serialPort.readStringUntil('\n');
    processInput(serialInput);
  }

  // TODO: Act on the serial input accordingly

  background(0);

  for (int j = 0; j < layerCount; j++) {
    for (int i = 0; i < shardCount; i++) {
      layers[j][i].display();
    }
  }
}
