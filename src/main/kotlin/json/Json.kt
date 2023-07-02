package json

import com.fasterxml.jackson.databind.ObjectMapper
import models.Info
import java.io.File

class Json(private val name: String, private val info: String){

    private val objectMapper = ObjectMapper()

    fun getJson(): ArrayList<Info>{
        val list = ArrayList<Info>()
        try {
        readJson().forEach { list.add(Info(it.name, it.info)) }
        }catch (e: Throwable){
            list.add(Info("null", "null"))
        }
        return list
    }

    fun printJson(){
        val array = arrayOf(Info(name, info))
        try { writeJson(readJson().plus(array)) }
        catch (e: Throwable) { writeJson(array) }
    }

    private fun readJson(): Array<Info>{
        return objectMapper.readValue(File("bd.json"), Array<Info>::class.java)
    }

    private fun writeJson(array: Array<Info>){
        objectMapper.writeValue(File("bd.json"), array)
    }
}