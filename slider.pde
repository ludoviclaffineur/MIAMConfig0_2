class slider {

  int val_min, val_max, increment, posX, posY, currentPos,sWidth;

  String name;
  slider(  int val_min, int val_max, int increment, int posX, int posY, String name, int sWidth)
  {
    this.val_min=val_min;
    this.val_max=val_max;
    this.increment=increment;
    this.posX=posX;
    this.sWidth=sWidth;
    this.posY=posY;
    this.name=name;
    this.currentPos=posX;
  }

  void draw() {
    strokeWeight(10);
    stroke(#E1E6FA);
    line (posX, posY, posX+sWidth, posY);
    strokeWeight(1);
    fill(#E1E6FA);
    text(val_min, posX, posY+15);
    for (int i=1;i<4;i++)
      text((int)((val_min+val_max)*i/4),(int)((posX)+sWidth*i/4),posY+15 );
    text(val_max, posX+sWidth, posY+15);
    stroke(#375D81);
    strokeWeight(1);
    fill(#375D81);
    ellipse(currentPos, posY, 20, 20);
  }

  boolean isOnSlider(int x, int y) {
    return (x<posX+sWidth && x>posX-10 && y<posY+10 && y>posY-10);
  }
}

