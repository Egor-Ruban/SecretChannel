import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
class Transmitter {
    companion object {

        private var state = false
        private fun powerShell(commands: Array<String>, dir: File) {

            val powerShellProcess: Process = Runtime.getRuntime().exec(commands, null, dir)

            powerShellProcess.outputStream.close()

            val stderr = BufferedReader(
                InputStreamReader(
                    powerShellProcess.errorStream
                )
            )

            //println(stderr.readLines().toString())
        }

        private fun openRepository() {
            //println("open")
            val dir = File("C:/DEV/forChannel")
            val commands = Array(2) { "" }
            commands[0] = "powershell.exe"
            commands[1] = "hub create"
            powerShell(commands, dir)
        }

        private fun closeRepository() {
            val dir = File("C:/DEV/forChannel")
            val commands = Array(2) { "" }
            commands[0] = "powershell.exe"
            commands[1] = "hub delete --yes forChannel"
            powerShell(commands, dir)
        }

        private fun changeState() {
            when (state) {
                true -> closeRepository()
                false -> openRepository()
            }
            state = !state
        }

        private fun prepare(){
            closeRepository()
        }

        private fun sendByte(data : UByte) {
            //Thread.sleep(1000)

            for (i in 0..7) {
                val startTime = System.currentTimeMillis()
                val bit = data.toUInt().shr(7 - i).and(1u)
                if (bit == 1u) {
                    changeState()
                }
                val spentTime = System.currentTimeMillis() - startTime
                //println("программа выполнялась $spentTime миллисекунд")
                Thread.sleep(4500-spentTime)
            }
        }

        fun send(text : Array<UByte>){
            prepare();
            sendByte(text.lastIndex.toUByte())
            for(byte in text){
                sendByte(byte)
            }
        }

        fun send(text: String){
            prepare();
            sendByte(text.length.toUByte())
            for(byte in text){
                println("${byte.toByte().toUByte()} is $byte")
                sendByte(byte.toByte().toUByte())
            }
        }

    }
}