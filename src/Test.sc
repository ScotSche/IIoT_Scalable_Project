var target = 449

if(target % 25 != 0) {
  val tmpValue = target % 25
  if(tmpValue < 25/2){
    target -= tmpValue
  }
  else{
    target += (25 - tmpValue)
  }
}