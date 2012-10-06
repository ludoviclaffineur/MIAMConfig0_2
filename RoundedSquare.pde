class RoundedSquare {
  int width=200;
  int diameter=100;
  int length=100, fadeout=0;
  int x=100, y=100, centerX, centerY;
  color fill=#6699FF;
  String name;
  String linkPicture;
  boolean isFadeIn=true, selected=false;
  boolean isDisable=false;
  PImage img;
  PFont font;
  color shapeColor=#000000;
  PFont fontA;
  int numberDispo;
  boolean noChangeTextColor=false;
  float alphaFill=0;
  hp hpLink;
  RoundedSquare() {
    drawRoundedSquare();
  }

  RoundedSquare(int width, int length, int diameter, int x, int y, color fill, String name, int numberDispo, hp hpLink ) {
    this.width=width;
    this.font=font;
    this.numberDispo=numberDispo;
    this.length=length;
    this.diameter=diameter;
    this.x=x;
    this.y=y;
    this.fill=fill;
    this.name=name;
    this.hpLink=hpLink;
    this.linkPicture=linkPicture;
    centerX=x+(width/2);
    centerY=y+(length/2);
    font = createFont("SansSerif", 6);
    //fontA = loadFont("Ziggurat-HTF-Black-32.vlw");


    //  drawRoundedSquare();
  }

  void draw() {
    drawRoundedSquare();
  }

  boolean contains(float xPoint, float yPoint) {

    return((xPoint < this.x+this.length) && (xPoint >this.x) && (yPoint >this.y) && (yPoint< this.y + this.width));
  }

  void drawRoundedSquare() {
    centerX=x+(width/2);
    centerY=y+(length/2);
    if (selected) {
      fill=#ABC8E2;
    }
    else {
      if (isDisable)
      {
        fill=#5c6f81;
      }
      else fill=#375D81;
    }
    stroke(fill, 255);
    fill(fill, 255);
    if ( noChangeTextColor) {
      stroke(shapeColor, 255);
      fill(shapeColor, 255);
    }
    rect( x, y+diameter/2, length, width-diameter);
    rect( x+diameter/2, y, length-diameter, width);
    ellipse(x+diameter/2, y+diameter/2, diameter, diameter);
    ellipse(x-diameter/2+length, y+diameter/2, diameter, diameter);
    ellipse(x+diameter/2, y-diameter/2+width, diameter, diameter);
    ellipse(x-diameter/2+length, y-diameter/2+width, diameter, diameter);
    //  stroke(#B4AF91,255);
    // fill(#B4AF91,255);

    stroke(fill, 255);
    fill(fill, 255);
    rect( x+2, y+diameter/2+2, length-4, width-diameter-4);
    rect( x+diameter/2+2, y+2, length-diameter-4, width-4);
    ellipse(x+diameter/2+2, y+diameter/2+2, diameter, diameter);
    ellipse(x-diameter/2+length-2, y+diameter/2+2, diameter, diameter);
    ellipse(x+diameter/2+2, y-diameter/2+width-2, diameter, diameter);
    ellipse(x-diameter/2+length-2, y-diameter/2-2+width, diameter, diameter);
    textFont(font, 12);

    if (selected) {
      fill(#000000);
    }
    else {
      if (isDisable) {
        fill(#3e4b57);
      }
      else fill(#E1E6FA, 255);
    }

    textAlign(CENTER, CENTER);
    text(name, x, y+width/2-15, length, width/2+10);
    pushMatrix();

    /*  image(img, x+diameter/2, y+diameter/2);
     tint(255,alphaFill);*/

    popMatrix();
  }
}

