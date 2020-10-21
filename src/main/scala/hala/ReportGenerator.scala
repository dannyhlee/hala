package hala

object ReportGenerator {
  def ipTest(address : String) = {
    val loc = requests.get("https://ipapi.co/"+address+"/json")
    loc
  }
}
