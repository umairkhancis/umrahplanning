package com.noorifytech.multiplatform.backend

import io.ktor.application.*
import io.ktor.auth.Principal
import io.ktor.auth.authentication
import io.ktor.features.*
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.default
import io.ktor.http.content.files
import io.ktor.http.content.static
import io.ktor.request.header
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.Routing
import io.ktor.util.error
import io.ktor.websocket.WebSockets

internal fun Application.main() {
    val config = environment.config
    val serviceConfig = config.config("service")
    val mode = serviceConfig.property("environment").getString()
    log.info("Environment: $mode")
    val sessionizeConfig = config.config("sessionize")
    val sessionizeUrl = sessionizeConfig.property("url").getString()
    val sessionizeInterval = sessionizeConfig.property("interval").getString().toLong()
    val production = mode == "production"

    if (!production) {
        install(CallLogging)
    }

    install(DefaultHeaders)
    install(ConditionalHeaders)
    install(Compression)
    install(PartialContent)
    install(AutoHeadResponse)
    install(WebSockets)
    install(XForwardedHeaderSupport)
    install(StatusPages) {
        exception<ServiceUnavailable> { _ ->
            call.respond(HttpStatusCode.ServiceUnavailable)
        }
        exception<BadRequest> { _ ->
            call.respond(HttpStatusCode.BadRequest)
        }
        exception<Unauthorized> { _ ->
            call.respond(HttpStatusCode.Unauthorized)
        }
        exception<NotFound> { _ ->
            call.respond(HttpStatusCode.NotFound)
        }
        exception<SecretInvalidError> { _ ->
            call.respond(HttpStatusCode.Forbidden)
        }
        exception<Throwable> { cause ->
            environment.log.error(cause)
            call.respond(HttpStatusCode.InternalServerError)
        }
    }

    install(ContentNegotiation) {
        register(ContentType.Application.Json, KotlinxConverter())
    }

    install(CORS) {
        anyHost()
        header(HttpHeaders.Authorization)
        allowCredentials = true
        listOf(HttpMethod.Put, HttpMethod.Delete, HttpMethod.Options).forEach { method(it) }
    }

    val database = Database(this)
    install(Routing) {
        authenticate()
        static {
            default("static/index.html")
            files("static")
        }
        api(database, production, sessionizeUrl)
    }

//    launchSyncJob(sessionizeUrl, sessionizeInterval)
}

private fun Route.authenticate() {
    val bearer = "Bearer "
    intercept(ApplicationCallPipeline.Features) {
        val authorization = call.request.header(HttpHeaders.Authorization) ?: return@intercept
        if (!authorization.startsWith(bearer)) return@intercept
        val token = authorization.removePrefix(bearer).trim()
        call.authentication.principal(KotlinConfPrincipal(token))
    }
}

internal class KotlinConfPrincipal(val token: String) : Principal
