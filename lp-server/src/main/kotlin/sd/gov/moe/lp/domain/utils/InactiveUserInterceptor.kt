package sd.gov.moe.lp.domain.utils

import sd.gov.moe.lp.common.auth.loggedin.AuthorizedClientData
import sd.gov.moe.lp.common.exceptions.DisabledUserException
import sd.gov.moe.lp.data.tables.UsersTable
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

/*
 * Copyright 2017-2020 Narbase technologies and contributors. Use of this source code is governed by the MIT License.
 */


fun Route.addInactiveUserInterceptor() {
    intercept(ApplicationCallPipeline.Call) {
        val authorizedClientData = call.principal<AuthorizedClientData>()
        val isInactive = transaction {
            authorizedClientData?.id?.let { clientId ->
                UsersTable.selectAll().where { UsersTable.clientId eq UUID.fromString(clientId) }.firstOrNull()
                    ?.get(UsersTable.isInactive)
            }
        }
        if (isInactive == true)
            throw DisabledUserException()
    }
}