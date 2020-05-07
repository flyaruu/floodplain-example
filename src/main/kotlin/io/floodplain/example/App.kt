/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package io.floodplain.example

import io.floodplain.kotlindsl.each
import io.floodplain.kotlindsl.filter
import io.floodplain.kotlindsl.fork
import io.floodplain.kotlindsl.message.empty
import io.floodplain.kotlindsl.mongoConfig
import io.floodplain.kotlindsl.mongoSink
import io.floodplain.kotlindsl.postgresSource
import io.floodplain.kotlindsl.postgresSourceConfig
import io.floodplain.kotlindsl.sink
import io.floodplain.kotlindsl.source
import io.floodplain.kotlindsl.stream
import java.net.URL

fun main(args: Array<String>) {
    stream {
        val postgres = postgresSourceConfig("local","postgres",5432,"postgres","mysecretpassword","dvdrental")
        val mongodb = mongoConfig("mongo","mongodb://mongo","mydatabase")
        postgresSource("public","payment",postgres) {
            each {
                    key,msg,_->println("Record: ${msg}")
            }
            mongoSink("payments","mytopic",mongodb)
        }
    }.renderAndStart(URL("http://localhost:8083/connectors"),"localhost:9092")
}
fun main3(args: Array<String>) {
    stream {
        val postgres = postgresSourceConfig("local","postgres",5432,"postgres","mysecretpassword","dvdrental")
        val mongodb = mongoConfig("mongo","mongodb://mongo","mydatabase")
        postgresSource("public","payment",postgres) {
            each {
                key,msg,_->println("Record: ${msg}")
            }
            mongoSink("payments","mytopic",mongodb)
        }
    }.renderAndStart(URL("http://localhost:8083/connectors"),"localhost:9092")
}

fun main2(args: Array<String>) {
    stream {
        source("@mytopic") {
            fork(
                {
                    sink("sink1")
                },
                {
                    filter {
                        key,value->key== "key1"
                    }
                    sink("filteredtopic")
                }
            )
        }
    }.renderAndTest {
        input("@mytopic","key1", empty().set("name","frank"))
        input("@mytopic","key2", empty().set("name","bob"))
        println("# in sink1: ${outputSize("sink1")} in filtered: ${outputSize("filteredtopic")}")
    }
}