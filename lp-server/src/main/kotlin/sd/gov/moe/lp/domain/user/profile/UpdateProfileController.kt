package sd.gov.moe.lp.domain.user.profile

import sd.gov.moe.lp.common.DataResponse
import sd.gov.moe.lp.common.Handler
import sd.gov.moe.lp.common.auth.loggedin.AuthorizedClientData
import sd.gov.moe.lp.common.exceptions.UnauthenticatedException
import sd.gov.moe.lp.data.tables.UsersTable
import sd.gov.moe.lp.dto.domain.user.profile.UpdateUserProfileDto
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.util.*

class UpdateProfileController : Handler<UpdateUserProfileDto.RequestDto, Unit>(
    UpdateUserProfileDto.RequestDto::class
) {
    @Suppress("RemoveRedundantQualifierName")
    override fun process(requestDto: UpdateUserProfileDto.RequestDto, clientData: AuthorizedClientData?): DataResponse<Unit> {
        val clientId = UUID.fromString(clientData?.id ?: throw UnauthenticatedException())
        transaction {
            UsersTable.update({ UsersTable.clientId eq clientId }) {
                it[UsersTable.callingCode] = requestDto.callingCode
                it[UsersTable.localPhone] = requestDto.localPhone
                it[UsersTable.fullName] = requestDto.fullName
            }
        }
        return DataResponse()
    }
}