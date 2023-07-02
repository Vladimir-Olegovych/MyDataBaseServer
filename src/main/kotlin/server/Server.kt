package server

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.Input
import com.esotericsoftware.kryo.io.Output
import json.Json
import models.Info
import org.apache.commons.io.IOUtils
import java.net.ServerSocket

class Server {

    private lateinit var serverSocket: ServerSocket
    private lateinit var json: Json

    fun start() {
        val kryo = Kryo()
        val port = 27015
        var num = 0

        try {
            serverSocket = ServerSocket(port)
            while (true) {
                val socket = serverSocket.accept()

                num += 1
                println("new client [$num]")


                val inputStream = socket.getInputStream()
                val input = Input(inputStream)

                val outputStream = socket.getOutputStream()
                val output = Output(outputStream)

                kryo.register(Info::class.java)
                kryo.register(ArrayList<Info>()::class.java)

                val get = kryo.readObject(input, Info::class.java)
                json = Json(get.name, get.info)

                when (get.info) {
                    GET_JSON_ARRAY -> {
                        kryo.writeObject(output, json.getJson())
                        output.flush()
                    }
                    GET_MESSAGE -> {
                        kryo.writeObject(output, json.getJson().get(json.getJson().size - 1))
                        output.flush()
                    }
                    else -> {
                        json.printJson()
                        kryo.writeObject(output, json.getJson().get(json.getJson().size - 1))
                        output.flush()
                    }
                }
            }


        } catch (e: Throwable) {
            e.printStackTrace()
        } finally {
            IOUtils.closeQuietly(serverSocket)
        }
    }
    companion object {
        const val GET_JSON_ARRAY = "get_json_array_19789139"
        const val GET_MESSAGE = "get_massage_json_19789139"
    }
}