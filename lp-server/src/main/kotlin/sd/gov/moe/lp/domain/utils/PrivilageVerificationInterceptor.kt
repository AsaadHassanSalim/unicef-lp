package sd.gov.moe.lp.domain.utils

import sd.gov.moe.lp.common.auth.loggedin.AuthorizedClientData
import sd.gov.moe.lp.common.exceptions.UnauthenticatedException
import sd.gov.moe.lp.dto.models.roles.Privilege
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

/*
 * Copyright 2017-2020 Narbase technologies and contributors. Use of this source code is governed by the MIT License.
 */

fun Route.addPrivilegeVerificationInterceptor(privilege: Privilege) {
    addPrivilegeVerificationInterceptor(listOf(privilege))
}

fun Route.addPrivilegeVerificationInterceptor(privileges: List<Privilege>) {
    intercept(ApplicationCallPipeline.Call) {
        val authorizedClientData = call.principal<AuthorizedClientData>()
        if (authorizedClientData?.privileges?.firstOrNull { it in privileges } == null)
            throw UnauthenticatedException()
    }
}