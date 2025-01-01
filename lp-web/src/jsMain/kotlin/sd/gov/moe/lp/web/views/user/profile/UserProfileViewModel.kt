package sd.gov.moe.lp.web.views.user.profile

import com.narbase.kunafa.core.lifecycle.Observable
import sd.gov.moe.lp.dto.domain.user.profile.GetProfileDto
import sd.gov.moe.lp.dto.domain.user.profile.UpdateUserProfileDto
import sd.gov.moe.lp.web.network.ServerCaller
import sd.gov.moe.lp.web.network.basicNetworkCall
import sd.gov.moe.lp.web.utils.BasicUiState

/*
 * Copyright 2017-2020 Narbase technologies and contributors. Use of this source code is governed by the MIT License.
 */
class UserProfileViewModel {
    val getProfileUiState = Observable<BasicUiState>()
    val updateProfileUiState = Observable<BasicUiState>()

    var loadedProfile: GetProfileDto.UserProfile? = null

    fun getProfile() {

        basicNetworkCall(getProfileUiState) {
            val response = ServerCaller.getUserProfiles()
            loadedProfile = response.data.profile
        }
    }

    fun updateProfile(
        fullName: String,
        callingCode: String,
        localPhone: String
    ) {
        basicNetworkCall(updateProfileUiState) {
            val dto = UpdateUserProfileDto.RequestDto(fullName, callingCode, localPhone)
            ServerCaller.updateUserProfile(dto)
        }
    }

}
