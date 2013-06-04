class hp {
  String type;
  boolean hasSymetric;
  boolean rotationActivated=false;
  boolean openingScale=false;
  boolean isSelected = false;
  PImage imageHp;
  int[] position = new int [3]; //x,y and Z. Z in { 0=low,1=middle, 2=high}
  color fillColor=#6699FF;
  color strokeColor = #000000;
  int xLength=25;
  int coordCorners [][]=new int[10][2];
  int yLength=50;
  int arrowLength=25;
  String linkPicture;
  int idHp;
  float pi=3.1415927;
  float angleCorners=pi/3;
  float opening=pi/6;
  float rotation=pi/2.0;
  int numero;
  int placement;
  int frequence_rep_min;
  String nickname;
  int frequence_rep_max; 
  float impedance_nominale; 
  int puissance_rms;
  int puissance_crete;
  String piste;
  int sortie=0;
  int curseur=0;
  boolean OnFocus=false;

  boolean useToBeTwo;
  PFont font_hp;
  int xRotation = 0; // 0= frontal 1=douche 2=fontaine;
  hp() {
    font_hp = createFont("Arial",32);
  }


  hp(hp hpCopy) {
    this();
    this.nickname = hpCopy.nickname;
    this.position[0]=hpCopy.position[0];
    this.position[1]=hpCopy.position[1];
    this.position[2]=hpCopy.position[2];
    this.type=hpCopy.type;
    this.idHp=hpCopy.idHp;
    this.placement=hpCopy.placement;
    calculateDraw();
    this.linkPicture=hpCopy.linkPicture;
    this.imageHp=hpCopy.imageHp;
    this.frequence_rep_min=hpCopy.frequence_rep_min;
    this.frequence_rep_max=hpCopy.frequence_rep_max; 
    this.impedance_nominale=hpCopy.impedance_nominale; 
    this.puissance_rms=hpCopy.puissance_rms;
    this.puissance_crete=hpCopy.puissance_crete;
    this.rotation=hpCopy.rotation;
    this.opening=hpCopy.opening;
    this.useToBeTwo = hpCopy.useToBeTwo;
    this.numero=hpCopy.numero;
  }

  hp(int x, int y, int z, String type, int placement, String linkPicture, int frequence_rep_min, int frequence_rep_max, float impedance_nominale, int puissance_rms, int puissance_crete, int idHp, int useToBeTwo, float opening, int numero, String nickname) {
    this();
    position[0]=x;
    position[1]=y;
    position[2]=z;
    this.nickname = nickname;
    this.type=type;
    this.idHp=idHp;
    this.placement=placement;
    this.opening=opening;
    calculateDraw();
    this.linkPicture=linkPicture;
    imageHp=loadImage(linkPicture);
    imageHp.resize(100, 0);
    this.numero=numero;
    this.frequence_rep_min=frequence_rep_min;
    this.frequence_rep_max=frequence_rep_max; 
    this.impedance_nominale=impedance_nominale; 
    this.puissance_rms=puissance_rms;
    this.puissance_crete=puissance_crete;
    if (useToBeTwo==1) {
      this.useToBeTwo=true;
    }
  }

  boolean isOnHp(int x, int y) {
    return(position[0]+xLength>x&&position[0]-xLength<x&&position[1]+yLength>y&&position[1]-yLength<y);
  } 

  void calculateDraw() {
    //coord rect
    coordCorners[0][0]=(int)(position[0]+20*widthCoeff*cos(rotation+angleCorners));
    coordCorners[1][0]=(int)(position[0]+20*widthCoeff*cos(rotation+pi-angleCorners));
    coordCorners[2][0]=(int)(position[0]+20*widthCoeff*cos(rotation+pi+angleCorners));
    coordCorners[3][0]=(int)(position[0]+20*widthCoeff*cos(rotation-angleCorners));
    coordCorners[0][1]=(int)(position[1]+20*widthCoeff*sin(rotation+angleCorners));
    coordCorners[1][1]=(int)(position[1]+20*widthCoeff*sin(rotation+pi-angleCorners));
    coordCorners[2][1]=(int)(position[1]+20*widthCoeff*sin(rotation+pi+angleCorners));
    coordCorners[3][1]=(int)(position[1]+20*widthCoeff*sin(rotation-angleCorners));
    //Coord arrows up
    coordCorners[4][0]=(int)(position[0]+25*widthCoeff*cos(rotation-opening));
    coordCorners[5][0]=(int)(position[0]+22*widthCoeff*cos(rotation-0.1-opening));
    coordCorners[6][0]=(int)(position[0]+22*widthCoeff*cos(rotation+0.1-opening));
    coordCorners[4][1]=(int)(position[1]+25*widthCoeff*sin(rotation-opening));
    coordCorners[5][1]=(int)(position[1]+22*widthCoeff*sin(rotation-0.1-opening));
    coordCorners[6][1]=(int)(position[1]+22*widthCoeff*sin(rotation+0.1-opening));
    //coord arrow down
    coordCorners[7][0]=(int)(position[0]+25*widthCoeff*cos(rotation+opening));
    coordCorners[8][0]=(int)(position[0]+22*widthCoeff*cos(rotation-0.1+opening));
    coordCorners[9][0]=(int)(position[0]+22*widthCoeff*cos(rotation+0.1+opening));
    coordCorners[7][1]=(int)(position[1]+25*widthCoeff*sin(rotation+opening));
    coordCorners[8][1]=(int)(position[1]+22*widthCoeff*sin(rotation-0.1+opening));
    coordCorners[9][1]=(int)(position[1]+22*widthCoeff*sin(rotation+0.1+opening));
  }
  void setRotation(float angle)
  {
    rotation=angle;
    calculateDraw();
  }
  void setOpening(float angle)
  {
    opening=angle;
    calculateDraw();
  }

  void draw() {

//    calculateDraw();

    //Couleur noire
    textFont(font_hp, 12*widthCoeff);
    strokeWeight(10);
    stroke(#ffffff);
    

    for (int i =0;i<3;i++) {
      line(coordCorners[i][0], coordCorners[i][1], coordCorners[i+1][0], coordCorners[i+1][1]);
    }

    line(coordCorners[0][0], coordCorners[0][1], coordCorners[3][0], coordCorners[3][1]);
    if (xRotation==0) { 
      line(position [0], position[1], coordCorners[4][0], coordCorners[4][1]);

      line(coordCorners[4][0], coordCorners[4][1], coordCorners[5][0], coordCorners[5][1]);
      line(coordCorners[4][0], coordCorners[4][1], coordCorners[6][0], coordCorners[6][1]);



      line(position [0], position[1], coordCorners[7][0], coordCorners[7][1]);

      line(coordCorners[7][0], coordCorners[7][1], coordCorners[8][0], coordCorners[8][1]);
      line(coordCorners[7][0], coordCorners[7][1], coordCorners[9][0], coordCorners[9][1]);
    }
    if (xRotation==1) { 
      line(coordCorners[0][0], coordCorners[0][1], coordCorners[2][0], coordCorners[2][1]);

      line(coordCorners[1][0], coordCorners[1][1], coordCorners[3][0], coordCorners[3][1]);
    }
    else if (xRotation==2) { 
      fill(#E1E6FA);
      ellipse(position[0], position[1], 4, 4);
    }
    if (piste!=null) {
      fill(#ffffff);
      ellipse(position[0]+20, position[1]-20, 15, 15);
    }
    if (sortie!=0) {
      fill(#ffffff);
      ellipse(position[0]+20, position[1]+20, 15, 15);
    }
    if (curseur!=0) {
      fill(#ffffff);
      ellipse(position[0]-20, position[1]-20, 15, 15);
    } 

    if (OnFocus) {
      noStroke();
      fill(#ABC8E2);
      ellipse(position[0], position[1], 70, 70);
    }
    noStroke();
    fill(#000000);

    if (piste!=null) {

      fill(#000000);
      text(piste, position[0]-15, position[1]-16);
      
    }
    if (sortie!=0) {

      fill(#000000);
      text(sortie, position[0]+15, position[1]-16);
    }
    if (curseur!=0) {

      fill(#000000);
      text(curseur, position[0], position[1]-32);
    }
    //Couleur Au dessus
    stroke(#E1E6FA);
    if (isSelected) {
      noFill();
      strokeWeight(5);
      stroke(#ABC8E2);

      ellipse(position[0], position[1], 100, 100);
      stroke(#E1E6FA);
    }
    fill(#000000);
    strokeWeight(1);
    //text(type+" "+numero, position[0], position[1]-32);
    strokeWeight(2);
    if (position[2]==1)
    {
      stroke(#bc6337);
    }
    if (position[2]==0)
    {
      stroke(#61bc4f);
    }
    if (position[2]==2)
    {
      stroke(#a51c1c);
    }

    for (int i =0;i<3;i++) {
      line(coordCorners[i][0], coordCorners[i][1], coordCorners[i+1][0], coordCorners[i+1][1]);
    }

    line(coordCorners[0][0], coordCorners[0][1], coordCorners[3][0], coordCorners[3][1]);
    if (xRotation==0) { 
      line(position [0], position[1], coordCorners[4][0], coordCorners[4][1]);

      line(coordCorners[4][0], coordCorners[4][1], coordCorners[5][0], coordCorners[5][1]);
      line(coordCorners[4][0], coordCorners[4][1], coordCorners[6][0], coordCorners[6][1]);



      line(position [0], position[1], coordCorners[7][0], coordCorners[7][1]);

      line(coordCorners[7][0], coordCorners[7][1], coordCorners[8][0], coordCorners[8][1]);
      line(coordCorners[7][0], coordCorners[7][1], coordCorners[9][0], coordCorners[9][1]);
    }
    if (xRotation==1) { 
      line(coordCorners[0][0], coordCorners[0][1], coordCorners[2][0], coordCorners[2][1]);

      line(coordCorners[1][0], coordCorners[1][1], coordCorners[3][0], coordCorners[3][1]);
    }
    fill(#000000);
    if (xRotation==2) { 

      fill(#E1E6FA);
      ellipse(position[0], position[1], 4, 4);
    }

    fill(#000000);
    strokeWeight(1);
    
    text(nickname+numero, position[0], position[1]-25);
    /* if(!OnFocus && piste!=null){
     noStroke();
     fill(#ffffff,200);
     ellipse(position[0], position[1], 100, 100);
     }*/

    //setRotation(rotation+0.05);

    //imageMode(CENTER);
    /*  background (#C0C0C0);
     stroke(strokeColor);
     fill(fillColor);
     strokeWeight(2);
     translate(position[0], position[1]);
     
     rect(0, 0, xLength, yLength);
     line (xLength,yLength/2,xLength+arrowLength,yLength/2);
     fill(strokeColor);
     triangle(xLength+arrowLength,yLength/2-5,xLength+10+arrowLength,yLength/2,xLength+arrowLength,yLength/2+5);
     //line(position[0]+(xLength)*cos(rotation)-(yLength/2)*sin(rotation), position[1]+(xLength*sin(rotation))+(yLength/2)*cos(rotation), position[0]+xLength-3+arrowLength, position[1]+yLength/2+(arrowLength));
     rotation = rotation+=0.05;
     //  triangle(position[0]+xLength-3+arrowLength*cos(rotation),position[1]+yLength/2+arrowLength*sin(rotation));*/
     textFont(font, 16*widthCoeff);
  }
}

