package sd.gov.moe.lp.data.access.roles

import sd.gov.moe.lp.data.columntypes.RolePrivilegesColumn
import sd.gov.moe.lp.data.models.roles.Role
import sd.gov.moe.lp.data.models.utils.ListAndTotal
import sd.gov.moe.lp.data.tables.roles.RolesTable
import sd.gov.moe.lp.data.tables.utils.ilike
import sd.gov.moe.lp.domain.user.crud.andWhere
import sd.gov.moe.lp.dto.models.roles.Privilege
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.util.*

/*
 * Copyright 2017-2020 Narbase technologies and contributors. Use of this source code is governed by the MIT License.
 */
object RolesDao {
    fun getList(pageNo: Long, pageSize: Int, searchTerm: String): ListAndTotal<Role> {
        val query = RolesTable.selectAll().where { RolesTable.isDeleted eq false }
        if (searchTerm.isNotBlank()) {
            query.andWhere {
                (RolesTable.name ilike "%$searchTerm%")
            }
        }
        val count = query.count()
        val list = query
            .orderBy(RolesTable.createdOn to SortOrder.DESC)
            .limit(pageSize, pageNo * pageSize)
            .map(::toModel)
        return ListAndTotal(list, count)
    }

    fun get(id: UUID) = RolesTable
        .selectAll().where { RolesTable.id eq id }
        .map(::toModel)
        .first()


    fun toModel(row: ResultRow): Role {
        val roleData = row[RolesTable.role]
        return Role(
            row[RolesTable.id].value,
            row[RolesTable.createdOn],
            row[RolesTable.name],
            roleData.privileges,
        )
    }

    fun create(name: String, privileges: List<Privilege>): UUID {
        val id = RolesTable.insert {
            it[RolesTable.name] = name
            it[role] = RolePrivilegesColumn(privileges)
        } get RolesTable.id
        return id.value
    }

    fun update(id: UUID, name: String, privileges: List<Privilege>) {
        RolesTable.update({
            RolesTable.id eq id
        }) {
            it[RolesTable.name] = name
            it[role] = RolePrivilegesColumn(privileges)
        }
    }

    fun delete(id: UUID) {
        RolesTable.update({
            RolesTable.id eq id
        }) {
            it[isDeleted] = true
        }
    }
}