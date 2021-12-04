package FileMonitoring

import org.slf4j.{Logger, LoggerFactory}

import java.io.{File, IOException}
import java.nio.file.StandardWatchEventKinds._
import java.nio.file._
import java.util
class FileWatcher(val folder: File) extends Runnable{

  protected var listeners = new util.ArrayList[FileListener]
  protected val watchServices = new util.ArrayList[WatchService]
  val logger: Logger = LoggerFactory.getLogger(this.getClass)

  override def run(): Unit = {
    try{
      val watchService = FileSystems.getDefault.newWatchService
      try {
        logger.info("inside run")
        val path = Paths.get(folder.getAbsolutePath)
        path.register(watchService, ENTRY_CREATE,ENTRY_MODIFY,ENTRY_DELETE)
        watchServices.add(watchService)
        var poll = true
        while (poll) {
          logger.info("inside run while")
          poll = pollEvents(watchService)
        }
      }
    } catch{
      case e@(_: IOException | _: InterruptedException | _: ClosedWatchServiceException) =>
        Thread.currentThread.interrupt()
    }
  }

  def notifyListeners(kind: WatchEvent.Kind[_], file: File): Unit = {
    val event = new FileEvent(file)
    logger.info("notifying listeners")
    kind match {
      case ENTRY_MODIFY =>  listeners.forEach( listener => listener.onModified(event))
      case ENTRY_DELETE => listeners.forEach( listener => listener.onDeleted(event))
//      case ENTRY_CREATE => listeners.forEach( listener => listener.onCreated(event)) if (file.isDirectory) new FileMonitoring.FileWatcher(file).setListeners(listeners).watch()
    }
  }

  @throws[InterruptedException]
  protected  def pollEvents(watchService: WatchService): Boolean = {
    logger.info("poll events")

    val key = watchService.take
    logger.info("poll events 2")
    val path = key.watchable.asInstanceOf[Path]

    key.pollEvents().forEach { event =>

      notifyListeners(event.kind, path.resolve(event.context.asInstanceOf[Path]).toFile)
      logger.info("event kind " + event.kind())
    }
    key.reset
  }

  def watch(): Unit = {
    logger.info("inside watch function")
    if(folder.exists()){
      val thread = new Thread(this)
      thread.setDaemon(true)
      thread.start()
    }
  }

  def getListeners: util.ArrayList[FileListener] = listeners

  def setListeners(listeners: util.ArrayList[FileListener]): FileWatcher = {
    this.listeners = listeners
    this
  }
  def addListener(listener: FileListener): FileWatcher = {
    listeners.add(listener)
    this
  }

  def removeListener(listener: FileListener): FileWatcher = {
    listeners.remove(listener)
    this
  }
}
