package br.com.zup

import io.grpc.ManagedChannel
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Singleton
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@MicronautTest(transactional = false)
internal class FreteServiceTest(val grpcCliente: FreteServiceGrpc.FreteServiceBlockingStub){
    /**
     * 1- caminho feliz
     * 2- null or blank
     * 3- placa invalida
     * 4- permissao
     */
    @Test
    fun `deve verificar valor`(){
        //cenario

        //acao
        val response = grpcCliente.salvar(FreteRequest.newBuilder()
            .setCep("38742-242")
            .build())

        //validacao
        with(response){
            assertNotNull(valor)
        }
    }

    @Test
    fun `deve dar erro de null`(){
        //cenario

        //acao
        val error = assertThrows<StatusRuntimeException>{
            grpcCliente.salvar(FreteRequest.newBuilder()
                .setCep("")
                .build())
        }
        //validacao
        with(error){
            assertEquals(Status.INVALID_ARGUMENT.code, status.code)
            assertEquals("cep deve ser preenchido", status.description)
        }
    }

    @Factory
    class Cliente(){

        @Singleton
        fun clientGrpc(@GrpcChannel(GrpcServerChannel.NAME) channel : ManagedChannel): FreteServiceGrpc.FreteServiceBlockingStub{
            return FreteServiceGrpc.newBlockingStub(channel)
        }
    }
}