package sd.gov.moe.lp.data.dto.roles

import sd.gov.moe.lp.dto.common.valueOfDto
import sd.gov.moe.lp.dto.models.roles.DynamicRoleDto
import sd.gov.moe.lp.dto.models.roles.Privilege

/*
 * Copyright 2017-2020 Narbase technologies and contributors. Use of this source code is governed by the MIT License.
 */

typealias PrivilegeName = String

val DynamicRoleDto.privilegesEnums
    get() = privileges.mapNotNull { valueOfDto<Privilege>(it) }
