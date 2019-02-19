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

int shardCount = 20;
Shard[] shards = new Shard[shardCount];

public void setup() {
  // fullScreen(P3D, 2);
  

  for (int i = 0; i < shardCount; i++) {
    shards[i] = new Shard("square.png");
  }
  
  frameRate(60);
}

public void keyPressed() {
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
}

public void draw(){
  background(0);

  for (int i = 0; i < shardCount; i++) {
    shards[i].display();
  }
}
class Shard {
  PVector target;
  PVector origin;
  PVector current;

  float targetScale;
  float startScale;
  float currentScale;

  float targetRotate;
  float currentRotate;
  float rotateSpeed;

  float speed;
  PImage image;

  Shard(String imagePath) {
    origin = new PVector(width/2, height/2);
    target = new PVector(random(width), random(height));
    current = new PVector(origin.x, origin.y);
    currentScale = 1.0f;
    startScale = 1.0f;
    targetScale = 1.0f;
    speed = 0;

    targetRotate = 0;
    currentRotate = 0;

    image = loadImage(imagePath);
  }

  public void triggerPulse() {
    targetScale = random(1.0f);
    target = new PVector(random(width), random(height));
  }

  public void triggerRotate() {
    targetRotate = random(360.0f);
  }

  public void resetRotate() {
    targetRotate = 0;
  }

  public void display() {
    imageMode(CENTER);
    speed += 0.1f;

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
    rotateSpeed += 0.1f;
    currentRotate = lerp(currentRotate, targetRotate, sin(rotateSpeed));

    if (currentRotate == targetRotate) {
      rotateSpeed = 0;
    }

    pushMatrix();
    translate(current.x, current.y);
    rotate(radians(currentRotate));
    image(image, 0,  0, image.width * currentScale, image.height * currentScale);
    // image(image, current.x, current.y, image.width * currentScale, image.height * currentScale);
    popMatrix();
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
