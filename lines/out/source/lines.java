import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.List; 
import java.util.Collections; 
import java.util.UUID; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class lines extends PApplet {





float rad3Mezzi = sqrt(3)/2;

ArrayList<Point> points;
ArrayList<Middle> middlesToMove;

boolean moving;

float SPEED = 1;
float TO_TOLLERANCE = 0.5f;
float KOCH_FACTOR = 3;

public void setup () {
  
  init();
  moving = false;
}

public void draw () {
  background(180);
  drawShape();

  if(moving)
    move();
  else
    calculate();

  if(points.size() > 100000)
    noLoop();
}


public void init () {

  points = new ArrayList<Point>();

  Point p1 = new Point(new PVector(width/8, height/4));
  Point p2 = new Point(new PVector(width - p1.pos.x, p1.pos.y));
  float L = dist(p1.pos.x, p1.pos.y, p2.pos.x, p2.pos.y);
  Point p3 = new Point(new PVector(width/2, p2.pos.y + rad3Mezzi*L));

  points.add(p1);
  points.add(p2);
  points.add(p3);

}

public void calculate () {

  ArrayList<Point> newPoints = new ArrayList<Point>();
  middlesToMove = new ArrayList<Middle>();

  for(int i=0; i < points.size(); i++) {

    Point current = points.get(i);
    Point next = i == points.size()-1 ? points.get(0) : points.get(i+1);

    float thisL = dist(current.pos.x, current.pos.y, next.pos.x, next.pos.y);

    PVector v = new PVector(next.pos.x - current.pos.x, next.pos.y - current.pos.y);
    v.normalize();
    v.mult(thisL/KOCH_FACTOR);
    Point p1 = new Point(current.pos.x  + v.x, current.pos.y + v.y);
    v.normalize();
    v.mult(thisL - thisL/KOCH_FACTOR);
    Point p2 = new Point(current.pos.x  + v.x, current.pos.y + v.y);
    v.normalize();
    v.mult(thisL/2);
    Middle m = new Middle(new PVector(current.pos.x  + v.x, current.pos.y + v.y), p1, p2);
    middlesToMove.add(m);

    List<Point> list = new ArrayList<Point>();
    list.add(current);
    list.add(p1);
    list.add(m);
    list.add(p2);
    list.add(next);
    Collections.shuffle(list);
    newPoints.addAll(list);

  }

  points = newPoints;

  moving = true;
}

public void move () {
  for(Middle m : middlesToMove) {
    m.move();
  }
}

public void drawShape () {
  noFill();
  strokeWeight(0.5f);
  beginShape();
  for(Point p : points) {
    vertex(p.pos.x, p.pos.y);
  }
  endShape(CLOSE);
}


public void mouseClicked() {
  saveFrame("screens/"+UUID.randomUUID().toString());
}

//////////////////////////

class Middle extends Point {

  Point p1, p2;
  Point to;

  Middle(PVector pos, Point p1, Point p2) {
    super(pos);
    this.p1 = p1;
    this.p2 = p2;

    float m = (p1.pos.y - p2.pos.y) / (p1.pos.x - p2.pos.x);

    float L = dist(p1.pos.x, p1.pos.y, p2.pos.x, p2.pos.y);

    PVector vPos = PVector.fromAngle(atan(-1/m));
    vPos.mult(rad3Mezzi*L);
    vPos.x += this.pos.x;
    vPos.y += this.pos.y;

    PVector vNeg = PVector.fromAngle(atan(-1/m));
    vNeg.mult(rad3Mezzi*L*(-1));
    vNeg.x += this.pos.x;
    vNeg.y += this.pos.y;

    if(m < 0) {
      if(p1.pos.y < p2.pos.y) { //scendendo
         this.to = new Point( vPos.x > vNeg.x ? vPos : vNeg );
      } else { //salendo
        this.to = new Point( vPos.x > vNeg.x ? vNeg : vPos );
      }
    } else {
      if(p1.pos.y > p2.pos.y) { //salendo
         this.to = new Point( vPos.x > vNeg.x ? vNeg : vPos );
      } else { //scendendo
        this.to = new Point( vPos.x > vNeg.x ? vPos : vNeg );
      }
    }
  }

  public void move() {
    PVector v = new PVector(to.pos.x - this.pos.x, to.pos.y - this.pos.y);
    v.normalize();
    v.mult(SPEED);
    this.pos.x += v.x;
    this.pos.y += v.y;
    if(dist(this.pos.x , this.pos.y, to.pos.x ,to.pos.y) < TO_TOLLERANCE) {
      moving = false;
      this.pos = to.pos;
    }
  }

}
class Point {

  PVector pos;

  Point(PVector pos) {
    this.pos = pos;
  }

  Point(float x, float y) {
    this(new PVector(x,y));
  }

  public void show() {
    fill(0);
    ellipse(pos.x,pos.y,3,3);
  }

}
  public void settings() {  size(800, 800); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "lines" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
