import java.io.FileWriter
import java.io.IOException


fun ubyteToString(text : Array<UByte>) : String{
        var result = ""
        for(byte in text){
            result += byte.toByte().toChar()
        }
        return result
    }

fun main(){

    try {
        FileWriter("output.txt", false).use { writer ->
            // запись всей строки
            writer.write(ubyteToString(Receiver.read()))
            // запись по символам
            writer.append('\n')
            writer.flush()
        }
    } catch (ex: IOException) {
        println(ex.message)
    }
}