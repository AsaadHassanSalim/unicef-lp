package sd.gov.moe.lp.dto.domain.admin

import sd.gov.moe.lp.dto.common.StringUUID
import kotlin.js.JsExport

@JsExport
object EnableStaffDtos {

    class RequestDto(
        val userId: StringUUID,
        val isActive: Boolean

    )
    class ResponseDto()
}