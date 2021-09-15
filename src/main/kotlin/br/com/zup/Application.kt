package br.com.zup

import io.grpc.ServerBuilder
fun main(args: Array<String>) {
	val servidor = ServerBuilder
		.forPort(50051)
		.addService(FreteService())
		.build()

	servidor.start()
	servidor.awaitTermination()
}

