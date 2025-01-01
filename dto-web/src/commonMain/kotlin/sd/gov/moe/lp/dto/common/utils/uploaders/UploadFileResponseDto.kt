package sd.gov.moe.lp.dto.common.utils.uploaders

import kotlin.js.JsExport

@JsExport
class UploadFileResponseDto(
    val url: String,
    val fileName: String
)