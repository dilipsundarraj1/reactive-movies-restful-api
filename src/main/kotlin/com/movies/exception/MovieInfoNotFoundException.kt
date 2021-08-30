package com.movies.exception

import org.apache.logging.log4j.message.Message
import java.lang.RuntimeException

class MovieInfoNotFoundException(message : String?, throwable: Throwable?) : RuntimeException(message, throwable) {

}
