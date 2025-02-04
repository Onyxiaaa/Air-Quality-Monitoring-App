 // Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false

}
 allprojects {
     repositories {
         google()
         jcenter()
         maven {
             url = uri("https://repo.eclipse.org/content/repositories/paho-releases/")
             url = uri("https://www.jitpack.io")
         }
     }
 }