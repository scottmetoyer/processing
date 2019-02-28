interface Shape {
  void triggerPulse();
  void triggerRotate();
  void triggerMove();
  void reset();

  void setVisible(boolean);
  boolean getVisible();


  void display();
}