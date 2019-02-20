import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

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
int imageCount = 1;
Shard[] layer1 = new Shard[shardCount];
Shard[] layer2 = new Shard[shardCount];
Shard[] layer3 = new Shard[shardCount];

public void setup() {
  // fullScreen(P2D, 2);
  

  for (int i = 0; i < shardCount; i++) {
    layer1[i] = new Shard("r1.png");
    layer1[i].setOpacity((int)random(128, 255));
    layer1[i].setScale(1.3f);
  }

  for (int i = 0; i < shardCount; i++) {
    layer2[i] = new Shard("r2.png");
    layer2[i].setOpacity((int)random(128, 255));
  }

  for (int i = 0; i < shardCount; i++) {
    layer3[i] = new Shard("r3.png");
    layer3[i].setOpacity((int)random(128, 255));
    layer3[i].setScale(0.7f);
  }

  
  frameRate(60);
}

public void keyPressed() {
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

public void draw(){
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
  PImage image;
  int imageIndex;

  Shard(String imagePath) {
    int spread = 25;
    origin = new PVector(width/2 + random(spread * -1, spread), height/2 + random(spread * -1, spread));
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

    setImage(imagePath);
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

  public void setImage(String imagePath) {
    image = loadImage(imagePath);
    image.resize(width/3, 0);

    // Create a mask and draw a random triangle on it
    PGraphics mask = createGraphics(image.width, image.height);
    mask.beginDraw();
    mask.triangle(random(mask.height), random(mask.width), random(mask.height), random(mask.width), random(mask.height), random(mask.width));
    mask.endDraw();

    image.mask(mask);
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
      image(image, 0,  0, image.width * currentScale, image.height * currentScale);
      popMatrix();
    }
  }
}
  public void settings() {  size(800, 600, P2D);  smooth(); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "imagecloud" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
