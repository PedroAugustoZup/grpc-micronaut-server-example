package br.com.zup

import io.micronaut.runtime.Micronaut

fun main(args: Array<String>) {
	Micronaut.build()
		.args(*args)
		.packages("br.com.zup")
		.start()
}

