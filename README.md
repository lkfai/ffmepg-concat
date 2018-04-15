# ffmpeg-concat
![Jenkins](https://img.shields.io/jenkins/s/https/jenkins.qa.ubuntu.com/view/Precise/view/All%20Precise/job/precise-desktop-amd64_default.svg)
![bitHound](https://img.shields.io/bithound/dependencies/github/rexxars/sse-channel.svg)

**Abstract**

This is a [ffmpeg](https://www.ffmpeg.org/download.html) wrapper.

It parses all files in the current directory and generate config.dat which is consumed by ffmpeg to aggregate into one file.

**Prerequisites**

System must already installed

* [JDK8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [ffmpeg](https://www.ffmpeg.org/download.html)

**Usage**

Run <code>java -jar ffmpeg-concat-0.0.1-SNAPSHOT-jar-with-dependencies.jar</code>
