package sd.gov.moe.lp.domain.user.profile

import sd.gov.moe.lp.common.DataResponse
import sd.gov.moe.lp.common.Handler
import sd.gov.moe.lp.common.auth.loggedin.AuthorizedClientData
import sd.gov.moe.lp.common.exceptions.UnauthenticatedException
import sd.gov.moe.lp.data.access.users.UsersRepository
import sd.gov.moe.lp.data.conversions.users.toProfileDto
import sd.gov.moe.lp.dto.domain.user.profile.GetProfileDto
import java.util.*

class GetProfileController : Handler<GetProfileDto.Request, GetProfileDto.Response>(GetProfileDto.Request::class) {

    override fun process(requestDto: GetProfileDto.Request, clientData: AuthorizedClientData?)
            : DataResponse<GetProfileDto.Response> {

        val clientId = UUID.fromString(clientData?.id ?: throw UnauthenticatedException())
        val userRm = UsersRepository.get(clientId)
        return DataResponse(GetProfileDto.Response(userRm.toProfileDto()))
    }
}