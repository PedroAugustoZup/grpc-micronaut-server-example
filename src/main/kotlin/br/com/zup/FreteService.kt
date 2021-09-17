package br.com.zup

import com.google.protobuf.Any
import com.google.rpc.Code
import io.grpc.Status
import io.grpc.protobuf.StatusProto
import io.grpc.stub.StreamObserver
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory
import kotlin.random.Random

@Singleton
class FreteService: FreteServiceGrpc.FreteServiceImplBase() {
    private val logger = LoggerFactory.getLogger(FreteService::class.java)

    override fun salvar(request: FreteRequest?, responseObserver: StreamObserver<FreteResponse>?) {
        logger.info("Frete sendo calculado para a request: $request")

        val cep = request!!.cep
        if(cep.isNullOrBlank()){
            val error = Status.INVALID_ARGUMENT.withDescription("cep deve ser preenchido")
                                .asRuntimeException()
            responseObserver?.onError(error)
            return
        }

        if(!cep.matches("[0-9]{5}-[\\d]{3}".toRegex())){
            responseObserver?.onError(Status.INVALID_ARGUMENT
                .withDescription("formato invalido")
                .augmentDescription("formato esperado: 99999-999")
                .asRuntimeException())
        }

        if(cep.endsWith("333")){
            val statusProp: com.google.rpc.Status = com.google.rpc.Status.newBuilder()
                            .setCode(Code.PERMISSION_DENIED.number)//com.google.rpc.Code
                            .setMessage("sem permissao")
                            .addDetails(Any.pack(ErroDetails.newBuilder()
                                                .setCodigo(401)
                                                .setMenssagem("token invalido")
                                                .build()))
                            .build()

            responseObserver?.onError(StatusProto.toStatusRuntimeException(statusProp))
            return
        }

        var valor = 0.0
        try {
            valor = Random.nextDouble(from = 0.0, until = 140.0)
            if(valor>100.0) throw IllegalArgumentException("Erro inesperado")
        }catch (e: Exception){
            responseObserver?.onError(Status.INTERNAL
                .withDescription(e.message)
                .withCause(e)
                .asRuntimeException())
            return
        }
        val response = FreteResponse.newBuilder()
            .setValor(valor)
            .build()
        responseObserver?.onNext(response)

        logger.info("Frete calculado: $response")
        responseObserver?.onCompleted()
    }
}