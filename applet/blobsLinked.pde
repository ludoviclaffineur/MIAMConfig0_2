class blobsLinked {
  TuioCursor blobOne;
  TuioCursor blobTwo;
  float alphaFromCos;
  float alphaFromSin;
  ;
  blobsLinked(TuioCursor blobOne, TuioCursor blobTwo) {
    this.blobOne=blobOne;
    this.blobTwo=blobTwo;
  }
  float getRotation() {
    PVector p1 = new PVector(-blobOne.getX(), -blobOne.getY());
    PVector p2 = new PVector(blobTwo.getX(), blobTwo.getY());
    PVector p3 = PVector.add(p1, p2);
    if (p3.y <0) {
      return -PVector.angleBetween(new PVector(3, 0), p3);
    } 
    else {
      return PVector.angleBetween(new PVector(3, 0), p3);
    }
  }
}

