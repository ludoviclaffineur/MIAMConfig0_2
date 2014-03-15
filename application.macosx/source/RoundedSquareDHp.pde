class RoundedSquareDHp {
  int width=200;
  int diameter=100;
  int length=100, fadeout=0;
  int x=100, y=100, centerX, centerY;
  color fill=#6699FF;
  String name1, name2;
  String linkPicture; 
  boolean isFadeIn=true, selected=false;
  boolean isDisable1=false;
  boolean isDisable2=false;
  PImage img;  
  PFont font;
  PFont fontA;
  int numberDispo1, numberDispo2;
  float alphaFill=0;
  hp hpLink1, hpLink2;
  RoundedSquareDHp() {
    drawRoundedSquare();
  }

  RoundedSquareDHp(int width, int length, int diameter, int x, int y, color fill, String name1, String name2, hp hpLink1, hp hpLink2 ) {
    this.width=width;
    this.font=font;
    this.numberDispo1=1;
    this.numberDispo2=1;
    this.length=length;
    this.diameter=diameter;
    this.x=x;
    this.y=y;
    this.fill=fill;
    this.name1=name1;
    this.name2=name2;
    this.hpLink1=hpLink1;
    this.hpLink2=hpLink2;
    this.linkPicture=linkPicture;
    centerX=x+(width/2);
    centerY=y+(length/2);
    font = createFont("SansSerif", (int)(6*widthCoeff));
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
      if (isDisable1)
      {
        fill=#5c6f81;
      }
      else fill=#375D81;
    }
    stroke(fill, 255);
    fill(fill, 255);
    rect( x, y+diameter/2, length, (width-10)/2);
    rect( x+diameter/2, y, length-diameter, width/2);

    ellipse(x+diameter/2, y+diameter/2, diameter, diameter);
    ellipse(x-diameter/2+length, y+diameter/2, diameter, diameter);


    if (isDisable2)
    {
      fill=#5c6f81;
    }
    else fill=#375D81;

    stroke(fill, 255);
    fill(fill, 255);
    rect( x, y+diameter/2+(width-10)/2, length, (width-10)/2);
    rect( x+diameter/2, y+(width)/2, length-diameter, width/2);
    ellipse(x+diameter/2, y-diameter/2+width, diameter, diameter);
    ellipse(x-diameter/2+length, y-diameter/2+width, diameter, diameter);
    //  stroke(#B4AF91,255);
    // fill(#B4AF91,255);




    textAlign(CENTER, CENTER);
    textFont(font, 12);
    if (selected) {
      fill(#000000);
    }
    else {
      if (isDisable1) {
        fill(#3e4b57);
      }
      else fill(#E1E6FA, 255);
    }


    if (hpLink1 == null){
        text(name1, x, y-2, (int)(100*widthCoeff), (int)(30*heightCoeff));
    }
    else{
       text(hpLink1.nickname + hpLink1.numero, x, y-2, (int)(100*widthCoeff), (int)(30*heightCoeff));
    }
    if (isDisable2) {
      fill(#3e4b57);
    }
    else fill(#E1E6FA, 255);
     if (hpLink2 == null){
       text(name2, x, y+width/2-2, (int)(100*widthCoeff), (int)(30*heightCoeff));
    }
    else{
       text(hpLink2.nickname + hpLink2.numero, x, y+width/2-2, (int)(100*widthCoeff), (int)(30*heightCoeff));
    }
  }
}

