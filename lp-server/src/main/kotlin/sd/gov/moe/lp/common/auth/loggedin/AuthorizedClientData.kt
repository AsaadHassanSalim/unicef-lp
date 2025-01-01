package sd.gov.moe.lp.common.auth.loggedin

import sd.gov.moe.lp.dto.models.roles.Privilege
import io.ktor.server.auth.*

/*
 * Copyright 2017-2020 Narbase technologies and contributors. Use of this source code is governed by the MIT License.
 */

data class AuthorizedClientData(
    val id: String,
    val timestamp: Long,
    val privileges: List<Privilege> = listOf(),
) : Principal