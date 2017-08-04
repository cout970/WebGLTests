import org.khronos.webgl.*
import org.w3c.dom.HTMLCanvasElement
import kotlin.browser.document
import kotlin.browser.window
import kotlin.js.Date

/**
 * Created by cout970 on 2017/08/04.
 */
typealias GL = WebGLRenderingContext

object WebGlRenderer {

    lateinit var canvas: HTMLCanvasElement
    lateinit var gl: GL
    lateinit var shaderProgram: WebGLProgram
    lateinit var vertex_buffer: WebGLBuffer

    fun init() {
        canvas = document.getElementById("glcanvas") as HTMLCanvasElement

        gl = initWebGL(canvas) ?: return

        try {
            shaderProgram = initShaders()
        } catch (e: IllegalStateException) {
            console.error(e.message)
            return
        }
        vertex_buffer = createVBO()

        // 20 ms = 1/60fps
        window.setInterval(WebGlRenderer::loop, 20)
    }

    fun loop() {

        gl.clearColor(0.0f, 0.0f, 0.0f, 1.0f)
        gl.enable(GL.DEPTH_TEST)
        gl.depthFunc(GL.LEQUAL)
        gl.clear(GL.COLOR_BUFFER_BIT or GL.DEPTH_BUFFER_BIT)

        gl.viewport(0, 0, canvas.width, canvas.height)

        printErrors()
        renderVBO(shaderProgram, vertex_buffer)
    }

    fun printErrors() {
        var lastError = gl.getError()
        while (lastError != GL.NO_ERROR) {
            console.log(lastError)
            lastError = gl.getError()
        }
    }

    fun renderVBO(shaderProgram: WebGLProgram, vertex_buffer: WebGLBuffer) {
        // Get the attribute location
        val coord = gl.getAttribLocation(shaderProgram, "pos")
        val time = gl.getUniformLocation(shaderProgram, "time")

        // Bind vertex buffer object
        gl.bindBuffer(GL.ARRAY_BUFFER, vertex_buffer)

        // Point an attribute to the currently bound VBO
        gl.vertexAttribPointer(coord, 3, GL.FLOAT, false, 0, 0)

        gl.uniform1f(time, (Date().getTime().toFloat() / 1000) % 360)

        // Enable the attribute
        gl.enableVertexAttribArray(coord)


        // Draw the buffer
        gl.drawArrays(GL.TRIANGLES, 0, 3)
    }

    fun initWebGL(canvas: HTMLCanvasElement): WebGLRenderingContext? {
        return try {
            canvas.getContext("webgl") as WebGLRenderingContext
        } catch (e: Exception) {
            console.log("Error: $e")
            null
        }
    }

    fun initShaders(): WebGLProgram {
        // Create a shader program object to store
        // the combined shader program
        val shaderProgram = gl.createProgram()!!

        // Attach a vertex shader
        gl.attachShader(shaderProgram, createShader(GL.VERTEX_SHADER, vertexShader))

        // Attach a fragment shader
        gl.attachShader(shaderProgram, createShader(GL.FRAGMENT_SHADER, fragmentShader))

        // Link both programs
        gl.linkProgram(shaderProgram)

        // Use the combined shader program object
        gl.useProgram(shaderProgram)

        return shaderProgram
    }

    fun createShader(type: Int, code: String): WebGLShader {
        // Create a vertex shader object
        val shader = gl.createShader(type)!!

        // Attach vertex shader source code
        gl.shaderSource(shader, code)

        // Compile the vertex shader
        gl.compileShader(shader)

        if (gl.getShaderParameter(shader, GL.COMPILE_STATUS) == false) {
            val log = gl.getShaderInfoLog(shader)
            val codeLines = code.lines().mapIndexed { index: Int, s: String -> "$index $s" }.joinToString("\n")
            throw IllegalStateException("Unable to compile shader: \n" +
                                        "$log\n" +
                                        "Source code: \n" +
                                        codeLines)
        }

        return shader
    }

    fun createVBO(): WebGLBuffer {

        val vertex_buffer = gl.createBuffer()!!
        val vertices = arrayOf(
                -0.5f, 0.5f, 0.0f,
                0.0f, 0.5f, 0.0f,
                -0.25f, 0.25f, 0.0f
        )

        //Bind appropriate array buffer to it
        gl.bindBuffer(GL.ARRAY_BUFFER, vertex_buffer)

        // Pass the vertex data to the buffer
        gl.bufferData(GL.ARRAY_BUFFER, Float32Array(vertices), GL.STATIC_DRAW)

        // Unbind the buffer
        gl.bindBuffer(GL.ARRAY_BUFFER, null)

        return vertex_buffer
    }

    val vertexShader = """
attribute  vec3 pos;
uniform float time;

void main(){
    gl_Position = vec4(pos.x * sin(time), pos.yz, 1.0);
}
"""

    val fragmentShader = """
void main(){
    gl_FragColor = vec4(1.0, 0.5, 0.0, 1.0);
}
"""
}