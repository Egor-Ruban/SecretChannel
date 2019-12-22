import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

class Receiver {

    companion object {
        private var state = false
        private fun powerShell(commands: Array<String>, dir: File) : String?{

            val powerShellProcess: Process = Runtime.getRuntime().exec(commands, null, dir)

            powerShellProcess.outputStream.close()

            val stderr = BufferedReader(
                InputStreamReader(
                    powerShellProcess.errorStream
                )
            )

            return stderr.readLines().toString()
        }

        private fun clone() : Boolean {
           // println("hey")
            val dir = File("C:/forChannel")
            val commands = Array(2) { "" }
            commands[0] = "powershell.exe"
            commands[1] = "Remove-Item -Path C:/forChannel/* -Recurse -Exclude scan -Force"
            powerShell(commands,dir)

            commands[0] = "powershell.exe"
            commands[1] = "hub clone https://github.com/Egor-Ruban/forChannel"
            val str = powerShell(commands, dir)
            //println(str)
            return !((str?.contains("not found")) ?: true) // true/false
        }

        private fun checkRepository() : Boolean{
            return if(clone() == state){
                false
            } else {
                state = !state
                true
            }
        }

        private fun readByte() : UByte{

            var data = 0.toUByte()
            for (i in 0..7) {
                val startTime = System.currentTimeMillis()
                var bit = checkRepository()
                if(bit){
                    val mask = 1 shl (7-i)
                    data = data.or(mask.toUByte());
                } else {
                    var mask = 1 shl (7-i)
                    mask = mask.inv()
                    data = data.and(mask.toUByte())
                }
                var spentTime = System.currentTimeMillis() - startTime
                //println("программа выполнялась $spentTime миллисекунд")
                if(4500-spentTime < 0) spentTime = 4500
                Thread.sleep(4500-spentTime)
            }
            println(data)
            return data
        }

        fun read() : Array<UByte>{
            //Thread.sleep(2000)
            val size = readByte()
            val data : Array<UByte> = Array(size.toInt()){0u.toUByte() }
            for(i in 0 until size.toInt()){
                data[i] = readByte()
                //print(data[i])
            }
            println(ubyteToString(data))
            return data
        }
    }
}