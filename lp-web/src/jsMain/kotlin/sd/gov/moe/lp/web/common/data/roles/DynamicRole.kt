package sd.gov.moe.lp.web.common.data.roles

import sd.gov.moe.lp.dto.models.roles.Privilege
import kotlin.js.Date

/*
 * Copyright 2017-2020 Narbase technologies and contributors. Use of this source code is governed by the MIT License.
 */
data class DynamicRole(
    val id: String,
    val createdOn: Date,
    val name: String,
    val privileges: List<Privilege>,
    val isDoctor: Boolean,
)