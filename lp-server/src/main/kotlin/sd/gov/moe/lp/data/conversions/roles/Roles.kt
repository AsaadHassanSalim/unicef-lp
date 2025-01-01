package sd.gov.moe.lp.data.conversions.roles

import sd.gov.moe.lp.data.conversions.id.toDto
import sd.gov.moe.lp.data.models.roles.Role
import sd.gov.moe.lp.dto.common.valueOfDto
import sd.gov.moe.lp.dto.models.roles.Privilege
import sd.gov.moe.lp.dto.models.roles.RoleDto

/*
 * Copyright 2017-2020 Narbase technologies and contributors. Use of this source code is governed by the MIT License.
 */

fun Role.toDto() = RoleDto(id.toDto(), name, privileges.map { it.dtoName })

val RoleDto.privilegesEnums get() = privileges.mapNotNull { valueOfDto<Privilege>(it) }

