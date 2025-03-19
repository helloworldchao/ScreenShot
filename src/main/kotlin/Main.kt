import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import kotlinx.coroutines.*
import java.awt.*
import java.io.File
import javax.imageio.ImageIO
import javax.swing.ImageIcon
import javax.swing.JFileChooser
import kotlin.math.abs
import kotlin.math.min

private fun takeScreenshot(note: String, savePath: String, bounds: Rectangle? = null): File? {
    try {
        val robot = Robot()
        val screenRect = bounds ?: Rectangle(Toolkit.getDefaultToolkit().screenSize)
        val awtImage = robot.createScreenCapture(screenRect)

        val g: Graphics = awtImage.graphics
        val font = Font("宋体", Font.BOLD, 30)
        val imgIcon = ImageIcon()
        val img = imgIcon.image

        // 添加水印
        g.drawImage(img, 0, 0, null)
        g.color = java.awt.Color.RED
        g.font = font
        g.drawString(note, 30, 60)
        g.dispose()

        val outputFile = File(savePath, "screenshot_${System.currentTimeMillis()}.png")
        ImageIO.write(awtImage, "png", outputFile)
        println("截图已保存至: ${outputFile.absolutePath}")
        return outputFile
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}

@Composable
fun SelectionOverlay(onSelection: (Rectangle) -> Unit) {
    var startPoint by remember { mutableStateOf<Point?>(null) }
    var currentPoint by remember { mutableStateOf<Point?>(null) }
    var startOffset by remember { mutableStateOf<Offset?>(null) }
    var currentOffset by remember { mutableStateOf<Offset?>(null) }
    var isSelecting by remember { mutableStateOf(false) }
    val screenSize = Toolkit.getDefaultToolkit().screenSize

    Window(
        transparent = true,
        undecorated = true,
        state = rememberWindowState(
            position = WindowPosition(0.dp, 0.dp),
            size = DpSize(screenSize.width.dp, screenSize.height.dp)
        ),
        onCloseRequest = {}
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            startOffset = offset
                            currentOffset = offset
                            startPoint = MouseInfo.getPointerInfo().location
                            currentPoint = startPoint
                            isSelecting = true
                        },
                        onDrag = { change, dragAmount ->
                            currentOffset = Offset(
                                currentOffset!!.x + dragAmount.x,
                                currentOffset!!.y + dragAmount.y
                            )
                            currentPoint = MouseInfo.getPointerInfo().location
                        },
                        onDragEnd = {
                            if (startPoint != null && currentPoint != null) {
                                val x = min(startPoint!!.x, currentPoint!!.x)
                                val y = min(startPoint!!.y, currentPoint!!.y)
                                val width = abs(currentPoint!!.x - startPoint!!.x)
                                val height = abs(currentPoint!!.y - startPoint!!.y)
                                if (width > 0 && height > 0) {
                                    onSelection(Rectangle(x, y, width, height))
                                }
                            }
                            isSelecting = false
                            startPoint = null
                            currentPoint = null
                            startOffset = null
                            currentOffset = null
                        }
                    )
                }
        ) {
            drawRect(
                color = Color.Black.copy(alpha = 0.3f),
                size = Size(size.width, size.height)
            )
            if (isSelecting && startOffset != null && currentOffset != null) {
                drawRect(
                    color = Color.White.copy(alpha = 0.2f),
                    topLeft = Offset(
                        min(startOffset!!.x, currentOffset!!.x),
                        min(startOffset!!.y, currentOffset!!.y)
                    ),
                    size = Size(
                        abs(currentOffset!!.x - startOffset!!.x),
                        abs(currentOffset!!.y - startOffset!!.y)
                    )
                )
                drawRect(
                    color = Color.White,
                    topLeft = Offset(
                        min(startOffset!!.x, currentOffset!!.x),
                        min(startOffset!!.y, currentOffset!!.y)
                    ),
                    size = Size(
                        abs(currentOffset!!.x - startOffset!!.x),
                        abs(currentOffset!!.y - startOffset!!.y)
                    ),
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1f)
                )
            }
        }
    }
}

@OptIn(DelicateCoroutinesApi::class)
@Composable
@Preview
fun App(updateWindowVisible: (windowVisible: Boolean) -> Unit) {
    var remark by remember { mutableStateOf("") }
    var savedFile by remember { mutableStateOf<File?>(null) }
    var savePath by remember { mutableStateOf(System.getProperty("user.home") + File.separator + "Desktop") }
    var aboutDialogVisible by remember { mutableStateOf(false) }
    var tipDialogVisible by remember { mutableStateOf(false) }
    var tip by remember { mutableStateOf("") }
    var showSelectionOverlay by remember { mutableStateOf(false) }

    MaterialTheme {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            TextField(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                value = remark,
                onValueChange = { remark = it },
                label = { Text("水印备注") }
            )
            Spacer(modifier = Modifier.padding(2.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = {
                    JFileChooser(savePath).apply {
                        fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
                        showOpenDialog(ComposeWindow())
                    }.selectedFile?.let {
                        savePath = it.absolutePath
                    }
                }, content = { Text("选择保存路径") })
                Spacer(modifier = Modifier.padding(2.dp))
                Text(text = "保存路径：${savePath}")
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        GlobalScope.launch {
                            withContext(Dispatchers.IO) {
                                updateWindowVisible(false)
                                delay(500)
                                savedFile = takeScreenshot(remark, savePath)
                                updateWindowVisible(true)
                            }
                        }
                    }) {
                    Text("全屏截图")
                }
                Spacer(modifier = Modifier.padding(2.dp))
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = { showSelectionOverlay = true }
                ) {
                    Text("区域截图")
                }
                Spacer(modifier = Modifier.padding(2.dp))
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        aboutDialogVisible = true
                    }) {
                    Text("关于作者")
                }
            }
            savedFile?.let {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("已保存至：")
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            try {
                                Desktop.getDesktop().open(savedFile!!.parentFile)
                            } catch (_: Exception) {
                                tip = "文件夹不存在"
                                tipDialogVisible = true
                            }
                        }) {
                        Text("打开文件夹")
                    }
                    Spacer(modifier = Modifier.padding(2.dp))
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            try {
                                Desktop.getDesktop().open(savedFile)
                            } catch (_: Exception) {
                                tip = "文件不存在"
                                tipDialogVisible = true
                            }
                        }) {
                        Text("打开文件")
                    }
                }
                Text("${savedFile?.absolutePath}")
            }
        }

        if (showSelectionOverlay) {
            updateWindowVisible(false)
            SelectionOverlay { bounds ->
                GlobalScope.launch {
                    withContext(Dispatchers.IO) {
                        delay(500)
                        savedFile = takeScreenshot(remark, savePath, bounds)
                        updateWindowVisible(true)
                    }
                }
                showSelectionOverlay = false
            }
        }

        if (tipDialogVisible) {
            AlertDialog(
                title = { Text("警告") },
                text = { Text(tip) },
                buttons = {},
                onDismissRequest = {
                    tipDialogVisible = false
                    tip = ""
                }
            )
        }

        if (aboutDialogVisible) {
            AlertDialog(
                title = { Text("关于作者") },
                text = { Text("作者：helloworldchao\n联系方式：helloworldchao@outlook.com\n更新时间：${Version.UPDATE_TIME}\n版本号：v${Version.PACKAGE_VERSION}") },
                buttons = {},
                onDismissRequest = { aboutDialogVisible = false }
            )
        }
    }
}

fun main() = application {
    var windowVisible by remember { mutableStateOf(true) }
    val windowState = rememberWindowState(
        size = DpSize(500.dp, 400.dp),
        position = WindowPosition.Aligned(Alignment.Center)
    )
    Window(
        visible = windowVisible,
        title = "截图工具",
        state = windowState,
        onCloseRequest = ::exitApplication,
        resizable = false
    ) {
        App { visible ->
            windowVisible = visible
        }
    }
}
