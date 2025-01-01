package sd.gov.moe.lp.domain.client

import com.auth0.jwt.JWT
import sd.gov.moe.lp.dto.common.network.DataResponse
import sd.gov.moe.lp.common.auth.AuthenticationConstants
import sd.gov.moe.lp.common.auth.algorithm
import sd.gov.moe.lp.common.auth.loggedin.AuthorizedClientData
import sd.gov.moe.lp.common.exceptions.UnauthenticatedException
import sd.gov.moe.lp.domain.client.token.AddTokenController
import sd.gov.moe.lp.domain.client.token.RemoveTokenController
import sd.gov.moe.lp.dto.common.auth.TokenDtos
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.setupClientRoutes(jwtIssuer: String, jwtAudience: String) {
    authenticate(AuthenticationConstants.BASIC_AUTH) {
        post("/oauth/token") {
            val principal = call.principal<AuthorizedClientData>() ?: throw UnauthenticatedException()
            val privileges = principal.privileges.map { it.name }.toTypedArray()
            val token = generateJwtToken(jwtIssuer, jwtAudience, principal, privileges)

            val dataResponse = DataResponse(TokenDtos.JwtTokenDto(token))
            call.respond(dataResponse)
        }
    }

    authenticate(AuthenticationConstants.JWT_AUTH) {
        route("/api/client") {
            route("/v1") {
                post("/token/add") { AddTokenController().handle(call) }
                post("/token/remove") { RemoveTokenController().handle(call) }
            }
        }
    }
}

fun generateJwtToken(
    jwtIssuer: String,
    jwtAudience: String,
    principal: AuthorizedClientData,
    privileges: Array<String>
) =
    JWT.create()
        .withIssuer(jwtIssuer)
        .withAudience(jwtAudience)
        .withClaim("clientId", principal.id)
        .withClaim("timestamp", principal.timestamp)
        .withArrayClaim("privileges", privileges)
        .sign(algorithm)