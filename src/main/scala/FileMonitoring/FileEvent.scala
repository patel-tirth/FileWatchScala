package FileMonitoring

import java.io.File
import java.util.EventObject;

class FileEvent(val file:File) extends EventObject(file){
  def getFile: File = {
    getSource.asInstanceOf[File]
  }
}
