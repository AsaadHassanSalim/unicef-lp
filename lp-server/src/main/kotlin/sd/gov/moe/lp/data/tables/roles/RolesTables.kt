package sd.gov.moe.lp.data.tables.roles

import sd.gov.moe.lp.data.columntypes.RolePrivilegesColumn
import sd.gov.moe.lp.data.columntypes.jsonColumn
import sd.gov.moe.lp.data.tables.*
import org.jetbrains.exposed.dao.id.UUIDTable

/*
 * Copyright 2017-2020 Narbase technologies and contributors. Use of this source code is governed by the MIT License.
 */

object RolesTable : LoggedTable, DeletableTable, UUIDTable("roles") {
    override val createdOn = createdOnColumn()
    override val isDeleted = deletedColumn()
    val name = text("name")
    val role = jsonColumn<RolePrivilegesColumn>("role")
}

object ClientsRolesTable : LoggedTable, UUIDTable("clients_roles") {
    val clientId = reference("client_id", ClientsTable)
    val dynamicRoleId = reference("role_id", RolesTable)
    override val createdOn = createdOnColumn()

}
