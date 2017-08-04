
import components.Button
import components.Canvas
import kotlinx.html.div
import react.dom.ReactDOM
import react.dom.render
import kotlin.browser.document
import kotlin.browser.window

/**
 * Created by cout970 on 2017/05/10.
 */

private val fileSystem = runtime.wrappers.require("fs")

fun main(args: Array<String>) {

    console.log("Reloading")
    enableAutoReload()

    ReactDOM.render(container = document.getElementById("base")) {
        div {
            Button {}
            Canvas {}
        }
    }

    WebGlRenderer.init()
}

private var lastModified: Int = 0

fun enableAutoReload(){
    fileSystem.stat("./run/frontend.js") { err, stat ->
        if (stat != undefined) {
            lastModified = -stat.mtime.unsafeCast<Int>()
            window.setInterval(::reloadWhenNecessary, 500)
        } else {
            console.error("Unable to find frontentd.js")
        }
        return@stat Unit
    }
}

fun reloadWhenNecessary() {
    fileSystem.stat("./run/frontend.js") { a, stats ->
        val hash = -stats.mtime.unsafeCast<Int>()
        if (lastModified != hash) {
            window.location.reload()
        }
    }
}