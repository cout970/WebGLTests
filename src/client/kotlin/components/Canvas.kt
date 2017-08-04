package components

import kotlinx.html.canvas
import kotlinx.html.id
import kotlinx.html.style
import react.RProps
import react.RState
import react.ReactComponentSpec
import react.dom.ReactDOMBuilder
import react.dom.ReactDOMComponent
import runtime.wrappers.jsStyle

/**
 * Created by cout970 on 2017/08/04.
 */
class Canvas : ReactDOMComponent<Canvas.Props, Canvas.State>() {

    companion object : ReactComponentSpec<Canvas, Canvas.Props, Canvas.State>

    init {
        state = State()
    }

    override fun ReactDOMBuilder.render() {
        canvas {
            id = "glcanvas"
            width = "640"
            height = "480"
            style = jsStyle { border = "solid 1px black" }
        }
    }

    class Props : RProps()
    class State : RState
}