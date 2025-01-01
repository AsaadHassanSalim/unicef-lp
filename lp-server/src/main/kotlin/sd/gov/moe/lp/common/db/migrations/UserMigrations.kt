package sd.gov.moe.lp.common.db.migrations

import sd.gov.moe.lp.common.db.migrations.usermigrations.AddRolesTable
import sd.gov.moe.lp.common.db.migrations.usermigrations.InitialMigration

/*
 * Copyright 2017-2020 Narbase technologies and contributors. Use of this source code is governed by the MIT License.
 */

fun initializeUserMigrations() {
    Migrations.userMigrations = listOf(
        InitialMigration,
        AddRolesTable,

        )
}