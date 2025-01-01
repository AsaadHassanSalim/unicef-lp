/*
 * Copyright 2017-2020 Narbase technologies and contributors. Use of this source code is governed by the MIT License.
 */

package sd.gov.moe.lp.domain.user.crud

import sd.gov.moe.lp.router.CrudEndPoint
import kotlin.reflect.KClass


abstract class EndpointCrudController<DtoType : Any, ListRequestDtoType : Any>(
    val endPoints: List<CrudEndPoint<DtoType, ListRequestDtoType>>,
    dtoClass: KClass<DtoType>,
    ListRequestDtoClass: KClass<ListRequestDtoType>,
) : CrudController<DtoType, ListRequestDtoType>(dtoClass, ListRequestDtoClass)


