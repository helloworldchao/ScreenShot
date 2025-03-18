import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import java.awt.Rectangle
import java.awt.Robot
import java.awt.Toolkit
import java.io.File
import javax.imageio.ImageIO

private fun takeScreenshot() {
    try {
        val robot = Robot()
        val screenRect = Rectangle(Toolkit.getDefaultToolkit().screenSize)
        val awtImage = robot.createScreenCapture(screenRect)

        val outputFile = File("screenshot_${System.currentTimeMillis()}.png")
        ImageIO.write(awtImage, "png", outputFile)
        println("截图已保存至: ${outputFile.absolutePath}")
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@Composable
@Preview
fun App() {
    var remark by remember { mutableStateOf("") }
    var savePath by remember { mutableStateOf("") }
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

                }, content = { Text("选择保存路径") })
                Spacer(modifier = Modifier.padding(2.dp))
                Text(text = "保存路径：${savePath}")
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        takeScreenshot()
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
    Window(
        title = "截图工具",
        state = rememberWindowState(
            size = DpSize(500.dp, 300.dp),
            position = WindowPosition.Aligned(Alignment.Center)
        ),
        onCloseRequest = ::exitApplication,
        resizable = false
    ) {
        App()
    }
}
