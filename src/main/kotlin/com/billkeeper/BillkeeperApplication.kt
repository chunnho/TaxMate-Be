package com.billkeeper

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BillkeeperApplication

fun main(args: Array<String>) {
    runApplication<BillkeeperApplication>(*args)
}
