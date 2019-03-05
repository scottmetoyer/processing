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
    layers[0][i].setSize(1.3f);
  }

  for (int i = 0; i < shardCount; i++) {
    layers[1][i] = new Shard(images, imageCount);
    layers[1][i].setOpacity((int)random(128, 255));
  }

  for (int i = 0; i < shardCount; i++) {
    layers[2][i] = new Shard(images, imageCount);
    layers[2][i].setOpacity((int)random(128, 255));
    layers[2][i].setSize(0.7f);
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

public void moveRandomLayer() {
  int layer = (int)random(layerCount);
  for (int i = 0; i < shardCount; i++) {
    layers[layer][i].triggerMove();
  }
}

public void moveAllLayers() {
  for (int j = 0; j < layerCount; j++) {
    for (int i = 0; i < shardCount; i++) {
      layers[j][i].triggerMove();
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

public void resetAll() {
  for (int j = 0; j < layerCount; j++) {
    for (int i = 0; i < shardCount; i++) {
      layers[j][i].reset();
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
    moveRandomLayer();
  }

  // Rotate all layers
  if (key == 's') {
    moveAllLayers();
  }

  // Toggle layer visible
  if (key == 'd') {
    resetAll();
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
        resetAll();
      }
    } else {
      randomChoice = (int)random(2);

      if (randomChoice == 0) {
        moveRandomLayer();
      } else {
        moveAllLayers();
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

  background(0);

  for (int j = 0; j < layerCount; j++) {
    for (int i = 0; i < shardCount; i++) {
      layers[j][i].update();
      layers[j][i].display();
    }
  }
}
abstract class Shape {
  PVector targetPosition;
  PVector startPosition;
  PVector currentPosition;

  float targetScale;
  float startScale;
  float currentScale;

  float targetRotation;
  float currentRotation;


  int opacity;
  float movementSpeed;
  float rotationSpeed;
  boolean visible;

  PImage images[];
  int imageIndex;
  int imageCount;

  Shape(String[] imagePaths, int imageCnt) {
    int spread = 25;
    // origin = new PVector(width/2 + random(spread * -1, spread), height/2 + random(spread * -1, spread));

    startPosition = new PVector(width/2, height/2);
    targetPosition = new PVector(startPosition.x, startPosition.y);
    currentPosition = new PVector(startPosition.x, startPosition.y);

    currentScale = 1.0f;
    startScale = 1.0f;
    targetScale = 1.0f;

    movementSpeed = 0;
    rotationSpeed = 0;

    imageIndex = 0;
    opacity = 255;
    visible = true;

    targetRotation = 0;
    currentRotation = 0;

    imageCount = imageCnt;
    imageIndex = 0;
    images = new PImage[imageCnt];

    for (int i = 0; i < imageCount; i++) {
      setImage(imagePaths[i], i);
    }
  }

  public void triggerPulse() {
    targetScale = random(2.0f);
  }

  public void triggerMove() {
    targetPosition = new PVector(random(width), random(height));
  }

  public void triggerRotation() {
    targetRotation = random(-360.0f, 360.0f);
  }

  public void reset() {
    targetRotation = 0;
    targetScale = 1.0f;
    targetPosition = new PVector(startPosition.x, startPosition.y);
  }

  public void setVisible(boolean isVisible) {
    visible = isVisible;
  }

  public boolean getVisible() {
    return visible;
  }

  public void setOpacity(int newOpacity) {
    opacity = newOpacity;
  }

  public int getOpacity() {
    return opacity;
  }

  public void setSize(float newSize) {
    startScale = newSize;
  }

  public float getScale() {
    return startScale;
  }

  public void setImage(String imagePath, int idx) {
    images[idx] = loadImage(imagePath);
    PImage image = images[idx];
    image.resize(width/3, 0);
  }

  public void setImageIndex(int newIndex) {
    imageIndex = newIndex;
  }

  public void update() {
    // Override this in the inherited class and update every frame as needed. Check for movement boundaries, etc.
  }

  public void display() {
    // Calculate some jitter
    float jitter = random(-1, 1);
    // jitter = 0;

    imageMode(CENTER);
    movementSpeed += 0.1f;

    // Compute the new position and scale
    currentPosition.x = lerp(currentPosition.x, targetPosition.x, sin(movementSpeed));
    currentPosition.y = lerp(currentPosition.y, targetPosition.y, sin(movementSpeed));
    currentScale = lerp(currentScale, targetScale, sin(movementSpeed));

    // Compute the new rotation
    rotationSpeed += 0.2f;
    currentRotation = lerp(currentRotation, targetRotation, sin(rotationSpeed));

    if (currentRotation == targetRotation) {
      rotationSpeed = 0;
    }

    if (currentPosition.x == startPosition.x || currentPosition.y == startPosition.y) {
      movementSpeed = 0;
    }

    if (visible) {
      pushMatrix();
      translate(currentPosition.x, currentPosition.y);
      rotate(radians(currentRotation + jitter));
      tint(255, opacity);
      image(images[imageIndex], 0,  0, images[imageIndex].width * currentScale, images[imageIndex].height * currentScale);
      popMatrix();
    }
  }
}
class Shard extends Shape{
  Shard(String[] imagePaths, int imageCnt) {
    super(imagePaths, imageCnt);
    setMasks();
  }

  // Shape implementation methods
  public void triggerPulse() {
    targetScale = random(2.0f);
    targetPosition = new PVector(random(width), random(height));
  }

  public void triggerMove() {
    targetRotation = random(-360.0f, 360.0f);
    targetScale = random(0.8f, 1.2f);
  }

  public void triggerRotation() {
    // Do nothing
  }

  public void reset() {
    targetRotation = 0;
  }

  public void setMasks() {
    for (int i = 0; i < imageCount; i++) {
      // Create a mask and draw a random triangle on it
      PGraphics mask = createGraphics(images[i].width, images[i].height);
      mask.beginDraw();
      mask.triangle(random(mask.width), random(mask.height), random(mask.width), random(mask.height), random(mask.width), random(mask.height));
      mask.endDraw();
      images[i].mask(mask);
    }
  }

  public void update() {
    // Check for boundaries and set target to origin
    if (currentPosition.x == targetPosition.x || currentPosition.y == targetPosition.y) {
      targetPosition = new PVector(startPosition.x, startPosition.y);
      targetScale = startScale;
      movementSpeed = 0;
    }
  }
}
class Sliver extends Shape {
    Sliver(String[] imagePaths, int imageCnt) {
    super(imagePaths, imageCnt);
    setMasks();
  }

  // Shape implementation methods
  public void triggerPulse() {
    targetScale = random(2.0f);
    targetPosition = new PVector(random(width), random(height));
  }

  public void triggerMove() {
    targetRotation = random(-360.0f, 360.0f);
    targetScale = random(0.8f, 1.2f);
  }

  public void triggerRotation() {
    // Do nothing
  }

  public void reset() {
    targetRotation = 0;
  }

  public void setMasks() {
    for (int i = 0; i < imageCount; i++) {
      // Create a mask and draw a random triangle on it
      PGraphics mask = createGraphics(images[i].width, images[i].height);
      mask.beginDraw();
      mask.triangle(random(mask.width), random(mask.height), random(mask.width), random(mask.height), random(mask.width), random(mask.height));
      mask.endDraw();
      images[i].mask(mask);
    }
  }

  public void update() {
    // Check for boundaries and set target to origin
    if (currentPosition.x == targetPosition.x || currentPosition.y == targetPosition.y) {
      // target = new PVector(origin.x, origin.y);
      // targetScale = startScale;
      movementSpeed = 0;
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
