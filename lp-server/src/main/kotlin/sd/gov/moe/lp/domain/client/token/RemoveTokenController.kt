package sd.gov.moe.lp.domain.client.token

import sd.gov.moe.lp.common.DataResponse
import sd.gov.moe.lp.common.Handler
import sd.gov.moe.lp.common.auth.loggedin.AuthorizedClientData
import sd.gov.moe.lp.common.exceptions.UnauthenticatedException
import sd.gov.moe.lp.data.models.clients.Client
import sd.gov.moe.lp.data.tables.DeviceTokensTable
import sd.gov.moe.lp.dto.domain.client.token.RemoveTokenDtos.RequestDto
import sd.gov.moe.lp.dto.domain.client.token.RemoveTokenDtos.ResponseDto
import sd.gov.moe.lp.domain.utils.client
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction

class RemoveTokenController : Handler<RequestDto, ResponseDto>(RequestDto::class) {

    override fun process(requestDto: RequestDto, clientData: AuthorizedClientData?): DataResponse<ResponseDto> {
        val client = clientData?.client ?: throw UnauthenticatedException()
        transaction {
            removeClientToken(client, requestDto.token)
        }
        return DataResponse(ResponseDto())
    }

    companion object {
        fun removeClientToken(client: Client, token: String) {
            DeviceTokensTable.deleteWhere {
                (DeviceTokensTable.token eq token) and (DeviceTokensTable.clientId eq client.id)
            }

        }
    }
}