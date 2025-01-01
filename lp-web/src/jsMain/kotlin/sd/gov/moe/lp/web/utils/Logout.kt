package sd.gov.moe.lp.web.utils

import sd.gov.moe.lp.web.AppViewController
import sd.gov.moe.lp.web.storage.StorageManager

fun logoutUser() {
    StorageManager.accessToken = null
    StorageManager.setUserLoggedIn(false)
    AppViewController.loginState.value = false
}
