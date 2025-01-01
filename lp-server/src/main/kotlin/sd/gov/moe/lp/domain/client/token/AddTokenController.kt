package sd.gov.moe.lp.domain.client.token

import sd.gov.moe.lp.common.DataResponse
import sd.gov.moe.lp.common.Handler
import sd.gov.moe.lp.common.auth.loggedin.AuthorizedClientData
import sd.gov.moe.lp.common.exceptions.UnauthenticatedException
import sd.gov.moe.lp.data.tables.ClientsTable
import sd.gov.moe.lp.data.tables.DeviceTokensTable
import sd.gov.moe.lp.data.tables.utils.toEntityId
import sd.gov.moe.lp.dto.domain.client.token.AddTokenDtos.RequestDto
import sd.gov.moe.lp.dto.domain.client.token.AddTokenDtos.ResponseDto
import sd.gov.moe.lp.domain.client.token.RemoveTokenController.Companion.removeClientToken
import sd.gov.moe.lp.domain.utils.client
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime

class AddTokenController : Handler<RequestDto, ResponseDto>(RequestDto::class) {

    override fun process(requestDto: RequestDto, clientData: AuthorizedClientData?): DataResponse<ResponseDto> {
        val client = clientData?.client ?: throw UnauthenticatedException()
        transaction {
            removeClientToken(client, requestDto.token)
            DeviceTokensTable.insert {
                it[token] = requestDto.token
                it[clientId] = client.id.toEntityId(ClientsTable)
                it[createdOn] = DateTime()
            }
        }
        return DataResponse(ResponseDto())
    }
}