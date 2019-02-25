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

  void move() {
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
