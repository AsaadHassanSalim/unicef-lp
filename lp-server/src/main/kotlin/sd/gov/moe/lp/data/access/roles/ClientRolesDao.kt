package sd.gov.moe.lp.data.access.roles

import sd.gov.moe.lp.data.models.roles.Role
import sd.gov.moe.lp.data.tables.ClientsTable
import sd.gov.moe.lp.data.tables.roles.ClientsRolesTable
import sd.gov.moe.lp.data.tables.roles.RolesTable
import sd.gov.moe.lp.data.tables.utils.toEntityId
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.util.*

/*
 * Copyright 2017-2020 Narbase technologies and contributors. Use of this source code is governed by the MIT License.
 */
object ClientRolesDao {
    fun getClientRoles(clientId: UUID): List<Role> {
        val dynamicRolesDao = RolesDao
        return ClientsRolesTable
            .leftJoin(RolesTable, { dynamicRoleId }, { RolesTable.id })
            .selectAll().where { ClientsRolesTable.clientId eq clientId }.map(dynamicRolesDao::toModel)
    }

    fun saveClientRoles(clientId: UUID, rolesIds: List<UUID>) {
        ClientsRolesTable.deleteWhere {
            ClientsRolesTable.clientId eq clientId
        }
        ClientsRolesTable.batchInsert(rolesIds) { roleId ->
            this[ClientsRolesTable.clientId] = clientId.toEntityId(ClientsTable)
            this[ClientsRolesTable.dynamicRoleId] = roleId.toEntityId(RolesTable)
        }
    }
}