package daggerok

import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans

interface MyService {
  fun sayHi(): String
}

class MyServiceImpl : MyService {
  override fun sayHi() = "hello!"
}

fun beans() = beans {
  bean<MyService> {
    MyServiceImpl()
  }
}

class KotlinConfigDslApplication {

  val myService: MyService

  constructor() {
    val context = GenericApplicationContext().apply {
      beans().initialize(this)
      refresh()
    }
    myService = context.getBean(MyService::class.java)
  }

  fun greet() {
    println("message: ${myService.sayHi()}")
  }
}

fun main(args: Array<String>) {
  val app = KotlinConfigDslApplication()
  app.greet()
}
