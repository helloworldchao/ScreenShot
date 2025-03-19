import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
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

@OptIn(DelicateCoroutinesApi::class)
private fun takeScreenshot(note: String, savePath: String) {
    GlobalScope.launch {
        withContext(Dispatchers.IO) {
            try {
                val robot = Robot()
                val screenRect = Rectangle(Toolkit.getDefaultToolkit().screenSize)
                val awtImage = robot.createScreenCapture(screenRect)

                val g: Graphics = awtImage.graphics
                val font = Font("宋体", Font.BOLD, 30)
                val imgIcon = ImageIcon()
                val img = imgIcon.image

                // 添加水印
                g.drawImage(img, 0, 0, null)
                g.color = Color.RED
                g.font = font
                g.drawString(note, 30, 60)
                g.dispose()

                val outputFile = File(savePath, "screenshot_${System.currentTimeMillis()}.png")
                ImageIO.write(awtImage, "png", outputFile)
                println("截图已保存至: ${outputFile.absolutePath}")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

@Composable
@Preview
fun App(updateMinimized: (status: Boolean) -> Unit) {
    var remark by remember { mutableStateOf("") }
    var savePath by remember { mutableStateOf(System.getProperty("user.home") + File.separator + "Desktop") }
    var aboutDialogVisible by remember { mutableStateOf(false) }

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
                        updateMinimized(true)
                        takeScreenshot(remark, savePath)
                        updateMinimized(false)
                    }) {
                    Text("点击截图")
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
        }

        if (aboutDialogVisible) {
            AlertDialog(
                title = { Text("关于作者") },
                text = { Text("作者：helloworldchao\n更新时间：2025/03/18 00:00:00\n版本号：v2.0") },
                buttons = {},
                onDismissRequest = { aboutDialogVisible = false }
            )
        }
    }
}

fun main() = application {
    val windowState = rememberWindowState(
        size = DpSize(500.dp, 300.dp),
        position = WindowPosition.Aligned(Alignment.Center)
    )
    Window(
        title = "截图工具",
        state = windowState,
        onCloseRequest = ::exitApplication,
        resizable = false
    ) {
        App { status ->
            windowState.isMinimized = status
        }
    }
}
