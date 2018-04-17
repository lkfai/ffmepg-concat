package com.aei.ffmpeg.concat

import mu.KotlinLogging
import java.io.File
import java.io.IOException
import java.nio.file.Files

private val logger = KotlinLogging.logger {}

class FFmpegConfig {
    fun generate(workDir: File, outConfig: String) {
        val outFile = File(workDir, outConfig)
        if (outFile.exists()) {
            outFile.delete()
        }
        Files.list(workDir.toPath())
                .filter { f -> !f.fileName.toString().matches(".*_\\d+$".toRegex()) }
                .findFirst()
                .ifPresent { f -> throw IllegalArgumentException("Filenames must end with '_' digits, illegal file: ${f.fileName}") }
        Files.list(workDir.toPath())
                .sorted { o1, o2 ->
                    val i1 = o1.fileName.toString().substringAfterLast("_").toInt()
                    val i2 = o2.fileName.toString().substringAfterLast("_").toInt()
                    if (i1 == i2) 0 else if (i1 < i2) -1 else 1
                }.forEachOrdered { file ->
                    logger.debug { "Generate ${file.toAbsolutePath()}" }
                    try {
                        outFile.appendText("file '${file.toAbsolutePath()}'${System.lineSeparator()}")
                    } catch (e: IOException) {
                        logger.error(e) { e.message }
                    }
                }
    }
}

class FFmpegRunner {
    @Throws(IOException::class, InterruptedException::class)
    fun spawn(workDir: String,
              outConfig: String,
              outFilename: String) {
        var pb = ProcessBuilder("which", "ffmpeg")
        var pc = pb.start()
        Thread.sleep(500)
        if (pc.exitValue() != 0) {
            logger.error { "ffmpeg not installed, please install ffmpeg via https://ffmpeg.org/download.html" }
            return
        }
        FFmpegConfig().generate(File(workDir), outConfig)
        pb = ProcessBuilder("ffmpeg", "-f", "concat", "-safe", "0", "-i", outConfig, "-c", "copy", "-bsf:a", "aac_adtstoasc", outFilename)
        pb.inheritIO()
        pc = pb.start()
        pc.waitFor()
    }
}

fun main(args: Array<String>) {
    val workDir = System.getProperty("user.dir")
    val outFilename = "out.mp4"
    val outConfig = "config.dat"
    val ffmpegRunner = FFmpegRunner()
    ffmpegRunner.spawn(workDir, outConfig, outFilename)
}