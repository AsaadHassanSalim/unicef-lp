package sd.gov.moe.lp.dto.domain.client.token

import kotlin.js.JsExport

@JsExport
object RemoveTokenDtos {

    class RequestDto(
        val token: String
    )

    class ResponseDto
}