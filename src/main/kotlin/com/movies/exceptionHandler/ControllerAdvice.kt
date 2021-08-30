package com.movies.exceptionHandler

import mu.KLogging
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.support.WebExchangeBindException
import org.springframework.web.context.request.WebRequest
import java.time.LocalDateTime

@Component
@ControllerAdvice
class ControllerAdvice {

    companion object : KLogging()

    @ExceptionHandler(RuntimeException::class)
    fun handleRuntimeException(ex: RuntimeException): ResponseEntity<String?>? {
        //logger.error("Exception caught in handleRuntimeException :  {} ", ex.message)
        logger.error("Exception caught in handleRuntimeException :  {} ", ex.message, ex) // this takes care of logging the whole stack trace
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.message)
    }

    @ExceptionHandler(WebExchangeBindException::class)
    fun handleMethodArgumentNotValid(
        ex: WebExchangeBindException,
    ): ResponseEntity<List<String>> {
        val errors = ex.bindingResult.allErrors
            .map { error -> error.defaultMessage!! }
            .sorted()
        logger.error("Method Argument Not Valid: ${ex.message}")
        logger.error("errors are: $errors and the size is : ${errors.size}")
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(errors)
    }

}