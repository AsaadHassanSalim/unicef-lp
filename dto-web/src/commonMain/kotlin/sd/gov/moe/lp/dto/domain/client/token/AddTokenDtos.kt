package sd.gov.moe.lp.dto.domain.client.token

import kotlin.js.JsExport

@JsExport
object AddTokenDtos {

    class RequestDto(
        val token: String
    )

    class ResponseDto
}