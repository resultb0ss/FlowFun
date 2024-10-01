import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.withContext
import kotlin.system.measureTimeMillis

suspend fun main() = coroutineScope {
    println("Введите количество пользователей для которых нужно сгенерировать пароли")
    val inputCountId = readln().toInt()
    println("Введите букву с которой они должны начинаться")
    val inputFirstChar = readln()
    val idMap: MutableMap<String,String> = mutableMapOf()

    val time = measureTimeMillis {

        withContext(newSingleThreadContext("MyThread")){
            val firstFlow = getListId(inputCountId).asFlow()
            val secondFlow = getListOfPassword(inputFirstChar,inputCountId).asFlow()
            firstFlow.zip(secondFlow){a,b -> idMap.put(a,b)}
                .collect()
        }
    }
    println("Времени на создание паролей было затрачено $time")
    println(idMap)


}

fun createPassword(): String{
    val password = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    val passArray: ArrayList<String> = arrayListOf()
    for (i in 1..6){
        var num = password.random()
        if ((i % 2 != 0) && (i.equals('a'..'z'))){
            num = num.uppercaseChar()
        }
        passArray.add(num.toString())
    }
    return passArray.reduce{ a,b -> "$a$b"}
}

fun getListOfPassword(input: String, length: Int): List<String>{
    var password = ""
    val passwordList: MutableList<String> = mutableListOf()
    while (passwordList.size != length) {
        password = createPassword()
        if (password[0].toString() == input) {
            passwordList.add(password)
        }
    }
    return passwordList.toList()
}

fun getListId(length: Int): List<String>{
    val idList: MutableList<String> = mutableListOf()
    var num = 0.0f
    for (i in 1..length){
        num = (i * 0.000001).toFloat()
        val numToAddList = String.format("%,5f",num)
        idList.add(numToAddList.slice(2..numToAddList.length - 1))
    }
    return idList
}
