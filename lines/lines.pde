import java.util.List;
import java.util.Collections;
import java.util.UUID;

float rad3Mezzi = sqrt(3)/2;

ArrayList<Point> points;
ArrayList<Middle> middlesToMove;

boolean moving;

float SPEED = 1;
float TO_TOLLERANCE = 0.5;
float KOCH_FACTOR = 3;

void setup () {
  size(800, 800);
  init();
  moving = false;
}

void draw () {
  background(180);
  drawShape();

  if(moving)
    move();
  else
    calculate();

  if(points.size() > 100000)
    noLoop();
}


void init () {
  points = new ArrayList<Point>();

  Point p1 = new Point(new PVector(width/8, height/4));
  Point p2 = new Point(new PVector(width - p1.pos.x, p1.pos.y));
  float L = dist(p1.pos.x, p1.pos.y, p2.pos.x, p2.pos.y);
  Point p3 = new Point(new PVector(width/2, p2.pos.y + rad3Mezzi*L));

  points.add(p1);
  points.add(p2);
  points.add(p3);
}

void calculate () {
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

void move () {
  for(Middle m : middlesToMove) {
    m.move();
  }
}

void drawShape () {
  noFill();
  strokeWeight(0.5);
  beginShape();

  for(Point p : points) {
    vertex(p.pos.x, p.pos.y);
  }

  endShape(CLOSE);
}

void mouseClicked() {
  saveFrame("screens/"+UUID.randomUUID().toString());
}