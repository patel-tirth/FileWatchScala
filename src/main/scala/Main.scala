import org.slf4j.{Logger, LoggerFactory}

import java.io.{File, FileWriter}

object Main extends App {
  val folder = new File("src/test")
  val watcher = new FileWatcher(folder)
  val logger: Logger = LoggerFactory.getLogger(this.getClass)

  watcher.addListener(new FileAdapter() {
    override def onModified(event: FileEvent): Unit = {
     print("modified")
      logger.info("modified")
    }
  }).watch()

  val file = new File(folder + "/test.txt")
  try {
    val writer = new FileWriter(file)
    try {
      writer.write("This is some string")
      Thread.sleep(2000)
      writer.append(" This is another string to append to file")
      Thread.sleep(2000)
      writer.append(" This is yet another string to append to file")
    } finally if (writer != null) writer.close()
  }
  Thread.sleep(2000)
}