package com.lubin.aclass

fun main(){
    val days= arrayOf(1,5,12,23)
    days[2]=15 //println(days[2])
    println(days.get(2))
    val dayList= mutableListOf<Int>(1,5,12,23)
    dayList.add(5)//預設產生出來的物件都是immutable-->mutableListOf->[1,5,12,23,5]
    dayList.removeAt(0)//刪除第一個索引值１->[5.12.23,5]
    dayList[2]=15//[5,15,23,5]
    println(dayList[2])//value=15
    println(dayList)//value=[5,12,15,5]
    println(dayList.size)//共４個索引值
    for (i in dayList){
        println(i)//5,12,15,5
    }
    dayList.forEach {
        println(it)//5,12,15,5
    }
    dayList.forEachIndexed { index, value ->
        print("$index->$value \t")
        //0->5 1->12 2->15 3->5
    }
}

class MyClass{

}

fun hello(){
    println("Hello world.")
}