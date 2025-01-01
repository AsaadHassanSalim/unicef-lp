package sd.gov.moe.lp.dto.models.roles

import sd.gov.moe.lp.dto.common.IdDto
import kotlin.js.JsExport

typealias PrivilegeName = String
@JsExport
data class RoleDto(
    val id: IdDto?,
    val name: String,
    val privileges: List<PrivilegeName>,
)