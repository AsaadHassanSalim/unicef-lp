package sd.gov.moe.lp.domain.admin.staff

import sd.gov.moe.lp.common.DataResponse
import sd.gov.moe.lp.common.Handler
import sd.gov.moe.lp.common.auth.loggedin.AuthorizedClientData
import sd.gov.moe.lp.common.toUUID
import sd.gov.moe.lp.data.tables.UsersTable
import sd.gov.moe.lp.dto.domain.admin.EnableStaffDtos
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

/*
 * Copyright 2017-2020 Narbase technologies and contributors. Use of this source code is governed by the MIT License.
 */

class EnableStaffController :
    Handler<EnableStaffDtos.RequestDto, EnableStaffDtos.ResponseDto>(EnableStaffDtos.RequestDto::class) {
    override fun process(requestDto: EnableStaffDtos.RequestDto, clientData: AuthorizedClientData?): DataResponse<EnableStaffDtos.ResponseDto> {

        transaction {
            UsersTable.update({
                (UsersTable.id eq requestDto.userId.toUUID())
            }) {
                it[isInactive] = requestDto.isActive.not()
            }
        }
        return DataResponse(EnableStaffDtos.ResponseDto())
    }

}