package dev.dancr.circuit.config

import com.google.gson.Gson
import dev.dancr.circuit.Circuit
import io.github.classgraph.ClassGraph
import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader

object ConfigScanner {

    private const val PACKAGE_PATH = "dev.dancr"
    private val ANNOTATION_NAME = Config::class.qualifiedName

    private val configs: HashMap<Class<*>, Any> = hashMapOf()

    init {
        ClassGraph().enableAllInfo().acceptPackages(PACKAGE_PATH).scan().use {
            for (clazz in it.getClassesWithAnnotation(ANNOTATION_NAME)) {
                val fileName = clazz.getAnnotationInfo(ANNOTATION_NAME).parameterValues.get("fileName").value as String
                val fileReader = FileReader(getFile("$fileName.json").absolutePath)

                configs[clazz.loadClass()] = Gson().fromJson(fileReader, clazz.loadClass())

            }
        }
    }

    private fun getFile(fileName: String) : File {
        if (!Circuit.plugin.dataFolder.exists()) throw FileNotFoundException("Configuration file $fileName does not exist")
        return File(Circuit.plugin.dataFolder, fileName)
    }

    internal inline fun <reified T> getConfig() : T {
        return configs[T::class.java] as T
    }
}