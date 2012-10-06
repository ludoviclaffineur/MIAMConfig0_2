class hp {
  String type;
  boolean rotationActivated=false;
  boolean openingScale=false;
  boolean isSelected = false;
  int[] position = new int [3]; //x,y and Z. Z in { 0=low,1=middle, 2=high}
  color fillColor=#6699FF;
  color strokeColor = #000000;
  int xLength=25;
  int coordCorners [][]=new int[10][2];
  int yLength=50;
  int arrowLength=25;
  int id;
  float pi=3.1415927;
  float angleCorners=pi/3;
  float opening=pi/6;
  float rotation=pi/7;
  hp() {
  }

  hp(int x, int y, int z, String type, int id) {
    position[0]=x;
    position[1]=y;
    position[2]=z;
    this.type=type;
    this.id=id;
    calculateDraw();
  }
  
boolean isOnHp(int x, int y){
     return(position[0]+xLength>x&&position[0]-xLength<x&&position[1]+yLength>y&&position[1]-yLength<y);
} 

void calculateDraw(){
  //coord rect
  coordCorners[0][0]=(int)(position[0]+20*cos(rotation+angleCorners));
  coordCorners[1][0]=(int)(position[0]+20*cos(rotation+pi-angleCorners));
  coordCorners[2][0]=(int)(position[0]+20*cos(rotation+pi+angleCorners));
  coordCorners[3][0]=(int)(position[0]+20*cos(rotation-angleCorners));
  coordCorners[0][1]=(int)(position[1]+20*sin(rotation+angleCorners));
  coordCorners[1][1]=(int)(position[1]+20*sin(rotation+pi-angleCorners));
  coordCorners[2][1]=(int)(position[1]+20*sin(rotation+pi+angleCorners));
  coordCorners[3][1]=(int)(position[1]+20*sin(rotation-angleCorners));
  //Coord arrows up
  coordCorners[4][0]=(int)(position[0]+25*cos(rotation-opening));
  coordCorners[5][0]=(int)(position[0]+22*cos(rotation-0.1-opening));
  coordCorners[6][0]=(int)(position[0]+22*cos(rotation+0.1-opening));
  coordCorners[4][1]=(int)(position[1]+25*sin(rotation-opening));
  coordCorners[5][1]=(int)(position[1]+22*sin(rotation-0.1-opening));
  coordCorners[6][1]=(int)(position[1]+22*sin(rotation+0.1-opening));
  //coord arrow down
  coordCorners[7][0]=(int)(position[0]+25*cos(rotation+opening));
  coordCorners[8][0]=(int)(position[0]+22*cos(rotation-0.1+opening));
  coordCorners[9][0]=(int)(position[0]+22*cos(rotation+0.1+opening));
  coordCorners[7][1]=(int)(position[1]+25*sin(rotation+opening));
  coordCorners[8][1]=(int)(position[1]+22*sin(rotation-0.1+opening));
  coordCorners[9][1]=(int)(position[1]+22*sin(rotation+0.1+opening));
  
  
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
    
    calculateDraw();
     stroke(#E1E6FA);
    if(isSelected){
      noFill();
      strokeWeight(5);
      stroke(#ABC8E2);
      ellipse(position[0],position[1], 100,100);
      stroke(#E1E6FA);
      strokeWeight(1);
    }
    for (int i =0;i<3;i++){
      line(coordCorners[i][0],coordCorners[i][1],coordCorners[i+1][0],coordCorners[i+1][1]);
    }
    line(coordCorners[0][0],coordCorners[0][1],coordCorners[3][0],coordCorners[3][1]);
    
    line(position [0],position[1],coordCorners[4][0],coordCorners[4][1]);
    
    line(coordCorners[4][0],coordCorners[4][1],coordCorners[5][0],coordCorners[5][1]);
    line(coordCorners[4][0],coordCorners[4][1],coordCorners[6][0],coordCorners[6][1]);
    
   
    
    line(position [0],position[1],coordCorners[7][0],coordCorners[7][1]);
    
    line(coordCorners[7][0],coordCorners[7][1],coordCorners[8][0],coordCorners[8][1]);
    line(coordCorners[7][0],coordCorners[7][1],coordCorners[9][0],coordCorners[9][1]);
    fill(#E1E6FA);
    text(id,position[0]+20,position[1]);
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
  }
}

