import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import processing.serial.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class imagecloud extends PApplet {



int shardCount = 50;
int layerCount = 3;
int triggerCount = 0;
int triggerThreshold = 16;
Shard[][] layers = new Shard[layerCount][shardCount];

Serial serialPort;
String serialInput;
int imageCount = 3;
int currentImageIndex = 0;

public void setup() {
  //fullScreen(P2D);
  
  String[] images = { "b1.jpg", "b2.jpg", "b3.jpg" };

  for (int i = 0; i < shardCount; i++) {
    layers[0][i] = new Shard(images, imageCount);
    layers[0][i].setOpacity((int)random(128, 255));
    layers[0][i].setScale(1.3f);
  }

  for (int i = 0; i < shardCount; i++) {
    layers[1][i] = new Shard(images, imageCount);
    layers[1][i].setOpacity((int)random(128, 255));
  }

  for (int i = 0; i < shardCount; i++) {
    layers[2][i] = new Shard(images, imageCount);
    layers[2][i].setOpacity((int)random(128, 255));
    layers[2][i].setScale(0.7f);
  }

  //smooth();
  frameRate(60);

  // Open up the serial port
  String portName = Serial.list()[1];
  serialPort = new Serial(this, portName, 9600);
}

public int randomExcept(int top, int exclude) {
  int number;

  do {
    number = (int)random(top);
  } while (number == exclude);

  return number;
}
public void pulseRandomShards() {
  int layer = (int)random(layerCount);
  int shards = (int)random(shardCount);

  for (int i = 0; i < shards; i++) {
     layers[layer][i].triggerPulse();
  }
}

public void pulseAllShards(int layer) {
  for (int i = 0; i < shardCount; i++) {
     layers[layer][i].triggerPulse();
  }
}

public void rotateRandomLayer() {
  int layer = (int)random(layerCount);
  for (int i = 0; i < shardCount; i++) {
    layers[layer][i].triggerRotate();
  }
}

public void rotateAllLayers() {
  for (int j = 0; j < layerCount; j++) {
    for (int i = 0; i < shardCount; i++) {
      layers[j][i].triggerRotate();
    }
  }
}

public void setRandomImage() {
  currentImageIndex = randomExcept(imageCount, currentImageIndex);

  for (int j = 0; j < layerCount; j++) {
    for (int i = 0; i < shardCount; i++) {
      layers[j][i].setImageIndex(currentImageIndex);
    }
  }
}

public void resetRotation() {
  for (int j = 0; j < layerCount; j++) {
    for (int i = 0; i < shardCount; i++) {
      layers[j][i].resetRotate();
    }
  }
}

public void keyPressed() {
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

public void processInput(String input) {
  int randomChoice;

  switch(input) {
    case "trigger_1\n":
    if (triggerCount++ >= triggerThreshold) {
      randomChoice = (int)random(2);

      if (randomChoice == 0) {
        triggerCount = 0;
        resetRotation();
      }
    } else {
      randomChoice = (int)random(2);

      if (randomChoice == 0) {
        rotateRandomLayer();
      } else {
        rotateAllLayers();
      }
    }
    break;

    case "trigger_2\n":
    pulseRandomShards();

    randomChoice = (int)random(6);
    if (randomChoice == 0) {
      setRandomImage();
    }
    break;

    case "trigger_3\n":
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

public void draw(){
  // Check for serial data
  if (serialPort.available() > 0) {
    serialInput = serialPort.readStringUntil('\n');
    print(serialInput);
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
  PImage images[];
  int imageIndex;
  int imageCount;

  Shard(String[] imagePaths, int imageCnt) {
    int spread = 25;
    // origin = new PVector(width/2 + random(spread * -1, spread), height/2 + random(spread * -1, spread));
    origin = new PVector(width/2, height/2);
    target = new PVector(random(width), random(height));
    current = new PVector(origin.x, origin.y);

    currentScale = 1.0f;
    startScale = 1.0f;
    targetScale = 1.0f;
    speed = 0;
    imageIndex = 0;
    opacity = 255;
    visible = true;

    targetRotate = 0;
    currentRotate = 0;

    imageCount = imageCnt;
    imageIndex = 0;
    images = new PImage[imageCnt];

    for (int i = 0; i < imageCount; i++) {
      setImage(imagePaths[i], i);
    }
  }

  public void triggerPulse() {
    targetScale = random(2.0f);
    target = new PVector(random(width), random(height));
  }

  public void triggerRotate() {
    targetRotate = random(-360.0f, 360.0f);
    targetScale = random(0.8f, 1.2f);
  }

  public void resetRotate() {
    targetRotate = 0;
  }

  public void setVisible(boolean isVisible) {
    visible = isVisible;
  }

  public boolean getVisible() {
    return visible;
  }

  public void setImage(String imagePath, int idx) {
    images[idx] = loadImage(imagePath);
    PImage image = images[idx];
    image.resize(width/3, 0);

    // Create a mask and draw a random triangle on it
    PGraphics mask = createGraphics(image.width, image.height);
    mask.beginDraw();
    mask.triangle(random(mask.width), random(mask.height), random(mask.width), random(mask.height), random(mask.width), random(mask.height));
    mask.endDraw();

    image.mask(mask);
  }

  public void setImageIndex(int newIndex) {
    imageIndex = newIndex;
  }

  public void setOpacity(int newOpacity) {
    opacity = newOpacity;
  }
  public void setScale(float newScale) {
    startScale = newScale;
  }

  public void display() {
    imageMode(CENTER);
    speed += 0.1f;

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
    rotateSpeed += 0.2f;
    currentRotate = lerp(currentRotate, targetRotate, sin(rotateSpeed));

    if (currentRotate == targetRotate) {
      rotateSpeed = 0;
    }

    if (visible) {
      pushMatrix();
      translate(current.x, current.y);
      rotate(radians(currentRotate + jitter));
      tint(255, opacity);
      image(images[imageIndex], 0,  0, images[imageIndex].width * currentScale, images[imageIndex].height * currentScale);
      popMatrix();
    }
  }
}
  public void settings() {  size(800, 600, P2D); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "imagecloud" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
