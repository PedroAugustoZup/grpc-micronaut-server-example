package br.com.zup

import io.grpc.stub.StreamObserver
import kotlin.random.Random

class FreteService: FreteServiceGrpc.FreteServiceImplBase() {

    override fun salvar(request: FreteRequest?, responseObserver: StreamObserver<FreteResponse>?) {
        if(!request?.cep.isNullOrBlank()){
            responseObserver?.onNext(FreteResponse
                .newBuilder()
                .setValor(Random.nextDouble(from = 10.00, until = 140.00))
                .build())
        }else{
            responseObserver?.onNext(FreteResponse
                .newBuilder()
                .setValor(0.0)
                .build())
        }
        responseObserver?.onCompleted()
    }
}