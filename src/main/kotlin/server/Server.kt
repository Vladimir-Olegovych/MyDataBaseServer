package server

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.Input
import com.esotericsoftware.kryo.io.Output
import json.Json
import models.Info
import org.apache.commons.io.IOUtils
import java.net.ServerSocket


private lateinit var serverSocket: ServerSocket
private lateinit var json: Json
class Server {
    fun start() {
        val kryo = Kryo()

        val port = 27015
        try {
            serverSocket = ServerSocket(port)
            while (true) {
                val socket = serverSocket.accept()

                val inputStream = socket.getInputStream()
                val input = Input(inputStream)

                val outputStream = socket.getOutputStream()
                val output = Output(outputStream)

                kryo.register(Info::class.java)
                val obj = kryo.readObject(input, Info::class.java)

                if (obj.info == "get_json_info_10471994_JSON") {
                    json = Json("null", "null")
                    kryo.register(ArrayList<Info>()::class.java)
                    kryo.writeObject(output, json.getJson())
                    output.flush()
                } else {
                    json = Json(obj.name, obj.info)
                    json.printJson()

                    kryo.register(ArrayList<Info>()::class.java)
                    kryo.writeObject(output, json.getJson())
                    output.flush()
                }
            }


        } catch (e: Throwable) {
            e.printStackTrace()
        } finally {
            IOUtils.closeQuietly(serverSocket)
        }
    }
}