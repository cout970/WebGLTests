import electron.BrowserWindow
import electron.app

/**
 * Created by cout970 on 2017/05/10.
 */

// Path to the current directory
private val fileDir: String get() = js("__dirname").toString()

// Reference to the actual window
var mainWindow: BrowserWindow? = null


fun main(args: Array<String>) {

    app.onEvent("ready", ::onCreate)
    app.onEvent("activate") {
        if (mainWindow == null) onCreate()
    }
    // Needed for OSX
    app.onEvent("window-all-closed") {
        if (js("process.platform") != "darwin") {
            app.quit()
        }
    }
}

fun onCreate() {
    // Create window
    val window = BrowserWindow {
        width = 800
        height = 600
    }
    // Save window so garbage collector don't kill the app
    mainWindow = window
    loadIndex()

    // Opens debug tools at start
    window.webContents.openDevTools()

    // when the window is closed it is destroyed, can be re-created in the event 'activate'
    window.onEvent("closed") {
        mainWindow = null
    }
}

fun loadIndex() {

    // url to the main page
    val index = "file:///${fileDir.replace('\\', '/')}/index.html"
    mainWindow?.loadURL(index)
}
