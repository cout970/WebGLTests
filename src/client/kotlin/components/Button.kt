package components

import kotlinx.html.classes
import kotlinx.html.div
import kotlinx.html.js.onClickFunction
import react.RProps
import react.RState
import react.ReactComponentSpec
import react.dom.ReactDOMBuilder
import react.dom.ReactDOMComponent

/**
 * Created by cout970 on 2017/08/04.
 */
class Button : ReactDOMComponent<Button.Props, Button.State>() {

    companion object : ReactComponentSpec<Button, Button.Props, Button.State>

    init {
        state = State()
    }

    override fun ReactDOMBuilder.render() {
        div {
            onClickFunction = {
                this@Button.replaceState(State(!state.on))
            }

            if (state.on) {
                classes = setOf("button", "on")
                +"On"
            } else {
                classes = setOf("button", "off")
                +"Off"
            }
        }
    }

    class Props : RProps()
    data class State(val on: Boolean = false) : RState
}