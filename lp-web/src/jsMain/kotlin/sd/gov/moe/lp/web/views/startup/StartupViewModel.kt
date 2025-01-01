package sd.gov.moe.lp.web.views.startup

import com.narbase.kunafa.core.lifecycle.Observable
import sd.gov.moe.lp.dto.domain.user.profile.GetProfileDto
import sd.gov.moe.lp.dto.models.roles.Privilege
import sd.gov.moe.lp.web.network.ServerCaller
import sd.gov.moe.lp.web.network.basicNetworkCall
import sd.gov.moe.lp.web.storage.CurrentUserProfile
import sd.gov.moe.lp.web.storage.SessionInfo
import sd.gov.moe.lp.web.storage.StorageManager
import sd.gov.moe.lp.web.utils.BasicUiState

/*
 * Copyright 2017-2020 Narbase technologies and contributors. Use of this source code is governed by the MIT License.
 */

class StartupViewModel {
    val getConfigUiState = Observable<BasicUiState>()


    fun getConfig() {
        if (StorageManager.isUserLoggedIn().not()) {
            SessionInfo.currentUser = null
            getConfigUiState.value = BasicUiState.Loaded
            return
        }
        basicNetworkCall(getConfigUiState) {
            val profile = ServerCaller.getUserProfiles().data.profile
            SessionInfo.currentUser = profile.toCurrentUserProfile()
            // You can check app version or subscription here as well
        }
    }

    private fun GetProfileDto.UserProfile.toCurrentUserProfile() =
        CurrentUserProfile(
            clientId,
            userId,
            fullName,
            username,
            callingCode,
            localPhone,
            privileges.map { Privilege.valueOf(it) })
}
