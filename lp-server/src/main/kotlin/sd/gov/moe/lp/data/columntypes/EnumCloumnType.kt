package sd.gov.moe.lp.data.columntypes

import sd.gov.moe.lp.dto.common.enums.EnumPersistenceName
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import kotlin.reflect.KClass

/*
 * Copyright 2017-2020 Narbase technologies and contributors. Use of this source code is governed by the MIT License.
 */


fun <E> Table.enum(name: String, enumClass: KClass<E>): Column<E> where E : Enum<E>, E : EnumPersistenceName =
    customEnumeration(
        name,
        toDb = { it.persistenceName },
        fromDb = { persistenceName ->
            enumClass.java.enumConstants.firstOrNull { it.persistenceName == persistenceName }
                ?: throw DbEnumCorruptedException()
        }
    )

class InvalidPersistedEnumValueException(message: String) : Exception(message)



class DbEnumCorruptedException(message: String = "The value returned from Db doesn't match any of the enum constants") :
    Exception(message)
