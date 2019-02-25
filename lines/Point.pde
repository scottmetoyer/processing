class Point {

  PVector pos;

  Point(PVector pos) {
    this.pos = pos;
  }

  Point(float x, float y) {
    this(new PVector(x,y));
  }

  void show() {
    fill(0);
    ellipse(pos.x,pos.y,3,3);
  }

}
