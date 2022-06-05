package com.example.weathernew.lessons


fun main(){
    val s1 = Soldier()
    s1.instrument = Shovels.ShovelWide()
    s1.instrument.excavate()
}

class Soldier() {

    lateinit var instrument:Shovel
}
sealed class Shovels{
    class ShovelWide():Shovel("wide"){
        override fun excavate() {
            // кто-то копает 1 способом
        }

    }

        class ShovelThin():Shovel("thin"){
            override fun excavate() {
                // кто-то копает 2 способом
            }

        }
}

abstract class Shovel(val name: String){
    abstract  fun excavate()
}