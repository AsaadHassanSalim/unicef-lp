package sd.gov.moe.lp.domain.user.profile

import sd.gov.moe.lp.common.DataResponse
import sd.gov.moe.lp.common.Handler
import sd.gov.moe.lp.common.auth.basic.PasswordEncoder
import sd.gov.moe.lp.common.auth.loggedin.AuthorizedClientData
import sd.gov.moe.lp.common.exceptions.UnauthenticatedException
import sd.gov.moe.lp.data.tables.ClientsTable
import sd.gov.moe.lp.dto.domain.user.profile.UpdatePasswordDto
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

/*
 * Copyright 2017-2020 Narbase technologies and contributors. Use of this source code is governed by the MIT License.
 */

class UserPasswordController :
    Handler<UpdatePasswordDto.Request, UpdatePasswordDto.Response>(UpdatePasswordDto.Request::class) {


    override fun process(
        requestDto: UpdatePasswordDto.Request, clientData: AuthorizedClientData?
    ): DataResponse<UpdatePasswordDto.Response> {
        val clientId = UUID.fromString(clientData?.id ?: throw UnauthenticatedException(""))

        val didUpdate = updateUserPassword(clientId, requestDto.oldPassword, requestDto.newPassword)
        return DataResponse(UpdatePasswordDto.Response(didUpdate))
    }

    private fun updateUserPassword(clientId: UUID, oldPassword: String, newPassword: String): Boolean {

        val passwordEncoder = PasswordEncoder()
        val existingPasswordHash = transaction {
            ClientsTable.select(ClientsTable.passwordHash).where { ClientsTable.id eq clientId }.limit(1)
                .firstOrNull()?.get(ClientsTable.passwordHash)
        } ?: ""
        if (passwordEncoder.checkPassword(oldPassword, existingPasswordHash).not()) {
            return false
        }
        transaction {
            ClientsTable.update({ ClientsTable.id eq clientId }) {
                it[passwordHash] = passwordEncoder.encode(newPassword)
            }
        }
        return true
    }
}