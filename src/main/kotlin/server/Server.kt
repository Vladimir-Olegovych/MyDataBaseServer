package server

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.Input
import com.esotericsoftware.kryo.io.Output
import json.Json
import models.Info
import org.apache.commons.io.IOUtils
import java.net.ServerSocket
import java.util.*

class Server {

    private lateinit var serverSocket: ServerSocket
    private lateinit var json: Json

    fun start() {
        val kryo = Kryo()
        val port = 27015

        try {
            serverSocket = ServerSocket(port)

            println("server_started")

            kryo.register(Info::class.java)
            kryo.register(ArrayList<Info>()::class.java)

            while (true) {
                val socket = serverSocket.accept()
                println("connect_${socket.inetAddress}:${socket.port}")

                val input = Input(socket.getInputStream())
                val output = Output(socket.getOutputStream())

                val get = kryo.readObject(input, Info::class.java)
                json = Json(get.name, get.info)

                when (get.info) {
                    GET_JSON_ARRAY -> {
                        kryo.writeObject(output, json.getJson())
                        output.flush()
                    }
                    GET_MESSAGE -> {
                        kryo.writeObject(output, json.getJson()[json.getJson().size - 1])
                        output.flush()
                    }
                    else -> {
                        json.printJson()
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
