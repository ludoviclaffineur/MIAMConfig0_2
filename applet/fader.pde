class fader {
  int posX;
  int posY;
  String name;
  int lengthFader=100;
  fader(){}
  
   fader(int posX, int posY,String name){
     this.posX=posX;
     this.posY=posY;
     this.name=name;
   }
   
   void draw(){
     
     line (posX, posY,posX, posY-lengthFader);
     fill(204, 102, 0);
     rect (posX-15,posY-lengthFader/2-5,30,10);
     fill(#000000);
     text (name,posX-4,posY+15);
   }
  
}
