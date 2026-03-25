package com.taxmate

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TaxmateApplication

fun main(args: Array<String>) {
    runApplication<TaxmateApplication>(*args)
}
