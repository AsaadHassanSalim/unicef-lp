package sd.gov.moe.lp.dto.domain.usersmanagement

import sd.gov.moe.lp.dto.common.IdDto
import sd.gov.moe.lp.dto.models.roles.RoleDto
import kotlin.js.JsExport

/*
 * Copyright 2017-2020 Narbase technologies and contributors. Use of this source code is governed by the MIT License.
 */
@JsExport
object UsersCrudDto {

    class Filters(
        val getInactive: Boolean?,
        val clientId: IdDto?,
    )


    class User(
        var clientId: IdDto?,
        var userId: IdDto?,
        val username: String,
        val password: String,
        val fullName: String,
        val callingCode: String,
        val localPhone: String,
        val dynamicRoles: Array<RoleDto>
    )
}