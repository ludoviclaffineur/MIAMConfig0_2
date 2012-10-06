class RoundedSquare {
  int width=200;
  int diameter=100;
  int length=100, fadeout=0;
  int x=100, y=100, centerX, centerY;
  color fill=#6699FF;
  String name;
  boolean isFadeIn=true;
  PImage img;
  PFont font;
   PFont fontA;
  float alphaFill=0;

  RoundedSquare() {
    drawRoundedSquare();
  }

  RoundedSquare(int width, int length, int diameter, int x, int y, color fill, PImage im, String name) {
    this.width=width;
    this.font=font;
    this.length=length;
    this.diameter=diameter;
    this.x=x;
    this.y=y;
    this.fill=fill;
    this.img=im;
    this.name=name;
    centerX=x+(width/2);
    centerY=y+(length/2);
    font = createFont("SansSerif", 6);
    //fontA = loadFont("Ziggurat-HTF-Black-32.vlw");
     

    //  drawRoundedSquare();
  }

  void draw() {
    drawRoundedSquare();
  }
  
  boolean contains(float xPoint, float yPoint){
   
   return((xPoint < this.x+this.length) && (xPoint >this.x) && (yPoint >this.y) && (yPoint< this.y + this.width));
     
    
  }

  void drawRoundedSquare() {
    centerX=x+(width/2);
    centerY=y+(length/2);
    stroke(fill,255);
    fill(fill,255);
    rect( x, y+diameter/2, length, width-diameter);
    rect( x+diameter/2, y, length-diameter, width);
    ellipse(x+diameter/2, y+diameter/2, diameter, diameter);
    ellipse(x-diameter/2+length, y+diameter/2, diameter, diameter);
    ellipse(x+diameter/2, y-diameter/2+width, diameter, diameter);
    ellipse(x-diameter/2+length, y-diameter/2+width, diameter, diameter);
  //  stroke(#B4AF91,255);
   // fill(#B4AF91,255);
    rect( x+10, y+diameter/2+10, length-20, width-diameter-20);
    rect( x+diameter/2+10, y+10, length-diameter-20, width-20);
    ellipse(x+diameter/2+10, y+diameter/2+10, diameter, diameter);
    ellipse(x-diameter/2+length-10, y+diameter/2+10, diameter, diameter);
    ellipse(x+diameter/2+10, y-diameter/2+width-10, diameter, diameter);
    ellipse(x-diameter/2+length-10, y-diameter/2-10+width, diameter, diameter);
    textFont(font, 10);
    fill(#E1E6FA,255);
    textAlign(CENTER, CENTER);
    text(name, x, y+width/2-15, 100,30);
    pushMatrix();
    
  /*  image(img, x+diameter/2, y+diameter/2);
      tint(255,alphaFill);*/
    
    popMatrix();
  }
}

