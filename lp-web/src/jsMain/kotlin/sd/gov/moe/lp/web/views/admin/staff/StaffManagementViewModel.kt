package sd.gov.moe.lp.web.views.admin.staff

import com.narbase.kunafa.core.lifecycle.Observable
import sd.gov.moe.lp.dto.common.network.CommonCodes.BASIC_SUCCESS
import sd.gov.moe.lp.web.network.UnknownErrorException
import sd.gov.moe.lp.web.network.basicNetworkCall
import sd.gov.moe.lp.web.network.calls.settings.AdminStaffServerCaller
import sd.gov.moe.lp.dto.domain.admin.AdminStaffServerCallerDtos
import sd.gov.moe.lp.web.network.calls.settings.EnableUserDto
import sd.gov.moe.lp.dto.common.network.crud.CrudDto
import sd.gov.moe.lp.web.utils.BasicUiState
import sd.gov.moe.lp.dto.common.network.DataResponse

/*
 * Copyright 2017-2020 Narbase technologies and contributors. Use of this source code is governed by the MIT License.
 */
class StaffManagementViewModel(private val showCurrent: Boolean) {

    private var searchTerm = ""
    val uiState = Observable<BasicUiState>()
    val upsertUiState = Observable<BasicUiState>()
    val userActiveUiState = Observable<BasicUiState>()
    var data: List<AdminStaffServerCallerDtos.StaffDto> = listOf()

    var pageNo = 0
    var pageSize = 20
    var total = 0

    fun getStaff() {
        upsertUiState.value = null
        basicNetworkCall(uiState) {
            val dto = CrudDto.GetList.Request(
                pageNo, pageSize,
                searchTerm = searchTerm,
                data = AdminStaffServerCallerDtos.Filters(
                    getInactive = showCurrent.not(),
                    clientId = null
                )
            )
            val response = AdminStaffServerCaller.getList(dto).data
            data = response.list.toList()
            total = response.total
        }
    }

    fun searchFor(term: String) {
        pageNo = 0
        searchTerm = term
        uiState.value = BasicUiState.Loaded
        getStaff()
    }

    fun getNextPage() {
        pageNo++
        getStaff()
    }

    fun getPreviousPage() {
        pageNo--
        getStaff()
    }

    fun addStaffMember(dto: AdminStaffServerCallerDtos.StaffDto) {
        basicNetworkCall(upsertUiState) {
            val response = AdminStaffServerCaller.add(dto)
            if (response.status != BASIC_SUCCESS) {
                throw UnknownErrorException()
            }
        }

    }

    fun editStaffMember(dto: AdminStaffServerCallerDtos.StaffDto) {
        basicNetworkCall(upsertUiState) {
            val response = AdminStaffServerCaller.update(dto)
            if (response.status != BASIC_SUCCESS) {
                throw UnknownErrorException()
            }
        }

    }

    fun setUserActive(userId: String, isActive: Boolean) {
        basicNetworkCall(userActiveUiState) {
            val dto = EnableUserDto.RequestDto(userId, isActive)
            val response = AdminStaffServerCaller.setUserActive(dto)
            if (response.status != BASIC_SUCCESS) {
                throw UnknownErrorException()
            }
        }

    }
}
