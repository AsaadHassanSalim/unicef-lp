package sd.gov.moe.lp.dto.models.roles

import kotlin.js.JsExport

@JsExport
data class DynamicRoleDto(
    val id: String?,
    val name: String,
    val privileges: Array<PrivilegeName>,
    val isDoctor: Boolean
)