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

Shard[] shards = new Shard[10];

public void setup() {
  // fullScreen(P3D, 2);
  

  for (int i = 0; i < 10; i++) {
    shards[i] = new Shard("square.png");
  }
  
  frameRate(60);
}

public void keyPressed() {
  for (int i = 0; i < 10; i++) {
    shards[i].trigger();
  }
}

public void draw(){
  background(0);

  for (int i = 0; i < 10; i++) {
    shards[i].move();
    shards[i].display();
  }

  // tint(255, 255);
  // rotate(radians(0));
  // scale(1.5);

}
class Shard {
  PVector target;
  PVector origin;
  PVector current;
  float speed;
  PImage image;

  Shard(String imagePath) {
    origin = new PVector(width/2, height/2);
    target = new PVector(random(width), random(height));
    current = new PVector(origin.x, origin.y);
    speed = 0;
    image = loadImage(imagePath);
  }

  public void display() {
    imageMode(CENTER);
    image(image, current.x, current.y);
  }

  public void trigger() {
    target = new PVector(random(width), random(height));
  }

  public void move() {
    speed += 0.1f;

     // Compute the new positions
    current.x = lerp(current.x, target.x, sin(speed));
    current.y = lerp(current.y, target.y, sin(speed));

    if (current.x == target.x || current.y == target.y) {
      target = new PVector(origin.x, origin.y);
      speed = 0;
    }

    if (current.x == origin.x || current.y == origin.y) {
      speed = 0;
    }
  }
}
  public void settings() {  size(800, 600);  smooth(); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "imagecloud" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
