package sd.gov.moe.lp.domain.user.login

import sd.gov.moe.lp.common.DataResponse
import sd.gov.moe.lp.common.Handler
import sd.gov.moe.lp.common.auth.loggedin.AuthorizedClientData
import sd.gov.moe.lp.data.access.clients.ClientsDao
import sd.gov.moe.lp.domain.utils.authenticatedClient
import sd.gov.moe.lp.dto.domain.user.login.LoginDto
import org.jetbrains.exposed.sql.transactions.transaction

class LoginController : Handler<LoginDto.Request, LoginDto.Response>(LoginDto.Request::class) {

    override fun process(
        requestDto: LoginDto.Request,
        clientData: AuthorizedClientData?
    ): DataResponse<LoginDto.Response> {
        val client = clientData.authenticatedClient

        val clientLastLogin = client.lastLogin
        transaction { ClientsDao.updateLastLogin(client.id) }

        return DataResponse(LoginDto.Response(clientLastLogin == null))
    }

}