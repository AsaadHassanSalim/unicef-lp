/*
 * Copyright 2017-2020 Narbase technologies and contributors. Use of this source code is governed by the MIT License.
 */


package sd.gov.moe.lp.common

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.reflections.Reflections
import sd.gov.moe.lp.common.auth.loggedin.AuthorizedClientData
import sd.gov.moe.lp.common.exceptions.DisabledUserException
import sd.gov.moe.lp.data.tables.UsersTable
import sd.gov.moe.lp.deployment.Environment
import sd.gov.moe.lp.deployment.LaunchConfig
import sd.gov.moe.lp.domain.user.crud.*
import sd.gov.moe.lp.domain.user.profile.HelloWorldController
import sd.gov.moe.lp.domain.utils.addPrivilegeVerificationInterceptor
import sd.gov.moe.lp.dto.models.roles.Privilege
import sd.gov.moe.lp.router.CrudEndPoint
import sd.gov.moe.lp.router.EndPoint
import sd.gov.moe.lp.router.newSubEndPoint
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.selectAll
import java.util.*
import java.util.logging.Logger
import io.ktor.server.routing.Route as KtorRoute
import io.ktor.server.routing.Routing as KtorRouting
import sd.gov.moe.lp.router.Routing as CommonRouting


fun KtorRouting.setupCommonRoutes() {
    val logger = Logger.getLogger("setupCommonRoutes")
    CommonRouting.init()
    val commonRoutingEndPoints = CommonRouting.endPoints
    val registeredEndpointPathes = mutableSetOf<String>()

    val reflections = Reflections("sd.gov.moe.lp")
    reflections.getSubTypesOf(EndpointHandler::class.java).forEach {
        logger.info("Registering controller: ${it.simpleName}")
        val defaultConstructors = it.constructors.first { it.parameterCount == 0 }
        println("HelloWorldController::class.java.classLoader = ${HelloWorldController::class.java.classLoader}")
        println("EndpointHandler::class.java.classLoader = ${EndpointHandler::class.java.classLoader}")
        println("defaultConstructors::class.java.classLoader = ${defaultConstructors::class.java.classLoader}")
        val handler = defaultConstructors.newInstance() as EndpointHandler<*, *>
        registeredEndpointPathes.add(handler.endPoint.path)
        registerRoute(handler, handler.endPoint)
    }

    reflections.getSubTypesOf(BasicEndpointHandler::class.java).forEach {
        logger.info("Registering basic controller: ${it.simpleName}")
        val defaultConstructors = it.constructors.first { it.parameterCount == 0 }
        val handler = defaultConstructors.newInstance() as BasicEndpointHandler<*>
        registeredEndpointPathes.add(handler.endPoint.path)
        registerRoute(handler, handler.endPoint)
    }

    reflections.getSubTypesOf(EndpointCrudController::class.java).forEach {
        logger.info("Registering crud controller: ${it.simpleName}")
        val defaultConstructors = it.constructors.first { it.parameterCount == 0 }
        val crud = defaultConstructors.newInstance() as EndpointCrudController<Any, Any>
        registeredEndpointPathes.addAll(crud.endPoints.map { it.path })
        crud.endPoints.forEach {
            val endPoint = it

            if (CrudEndPoint.CrudEndpoints.AddEndpoint in endPoint.crudPrivileges) {
                registerRoute(
                    CreateController(crud::createItem, crud.dtoClass),
                    newSubEndPoint("/add", endPoint)
                )
            }
            if (CrudEndPoint.CrudEndpoints.DetailsEndpoint in endPoint.crudPrivileges) {
                registerRoute(
                    GetItemController(crud::getItem),
                    newSubEndPoint("/details", endPoint)
                )
            }
            if (CrudEndPoint.CrudEndpoints.UpdateEndpoint in endPoint.crudPrivileges) {
                registerRoute(
                    UpdateController(crud::updateItem, crud.dtoClass),
                    newSubEndPoint("/update", endPoint)
                )
            }
            if (CrudEndPoint.CrudEndpoints.DeleteEndpoint in endPoint.crudPrivileges) {
                registerRoute(
                    DeleteController(crud::deleteItem),
                    newSubEndPoint("/delete", endPoint)
                )
            }
            if (CrudEndPoint.CrudEndpoints.ListEndpoint in endPoint.crudPrivileges) {
                registerRoute(
                    GetListController(
                        crud.ListRequestDtoClass,
                        crud::getItemsList,
                        crud.defaultPageSize
                    ),
                    newSubEndPoint("/list", endPoint)
                )
            }

        }
    }

    val unregisterEndPoints = commonRoutingEndPoints.filter { it.path !in registeredEndpointPathes }
    if (unregisterEndPoints.isNotEmpty()) {
        System.err.println("Unregistered EndPoints:")
        unregisterEndPoints.forEach {
            System.err.println(it)
        }
        throw IllegalStateException("Some endpoints are unregistered!")
    }
    if (LaunchConfig.environment == Environment.Dev) {
        printRoutes()
    }


}


private fun KtorRouting.registerRoute(handler: BasicHandler<*>, endPoint: EndPoint<*, *>) {
    var authenticatedRoute: KtorRoute = this@registerRoute
    endPoint.authentication.forEach { auth ->
        authenticatedRoute.apply { authenticate(auth.configurationName) current@{ authenticatedRoute = this@current } }
    }
    authenticatedRoute.apply { registerRouteWithoutAuth(endPoint, handler) }

}

private fun KtorRoute.registerRoute(handler: BasicHandler<*>, endPoint: EndPoint<*, *>) {
    var authenticatedRoute: KtorRoute = this@registerRoute
    endPoint.authentication.forEach { auth ->
        authenticatedRoute.apply { authenticate(auth.configurationName) current@{ authenticatedRoute = this@current } }
    }
    authenticatedRoute.apply { registerRouteWithoutAuth(endPoint, handler) }

}

private fun KtorRoute.registerRouteWithoutAuth(
    endPoint: EndPoint<*, *>,
    handler: BasicHandler<*>,
) {
    if (endPoint.privileges.isEmpty()) {
        post(endPoint.path) { handler.handle(call) }
    } else {
        addPrivilegeVerificationInterceptor(endPoint.privileges)
        addDisableAccountInterceptor(endPoint.privileges)
        post(endPoint.path) {
            handler.handle(call)
        }
    }
}


private fun KtorRoute.addDisableAccountInterceptor(privileges: List<Privilege>) {
    intercept(ApplicationCallPipeline.Call) {
        val authorizedClientData = call.principal<AuthorizedClientData>()
        val isInactive = transaction {
            authorizedClientData?.id?.let { clientId ->
                UsersTable.selectAll().where { UsersTable.clientId eq UUID.fromString(clientId) }
                    .firstOrNull()
                    ?.let { it[UsersTable.isInactive] || it[UsersTable.isDeleted] }
                    ?: true
            }
        }
        if (isInactive == true)
            throw DisabledUserException()
    }
}

fun KtorRouting.printRoutes() {
    val allRoutes = allRoutes(this)
    val allRoutesWithMethod = allRoutes.filter { it.selector is HttpMethodRouteSelector }
    allRoutesWithMethod.forEach {
        println("route: $it")
    }
}

fun allRoutes(root: KtorRoute): List<KtorRoute> {
    return listOf(root) + root.children.flatMap { allRoutes(it) }
}