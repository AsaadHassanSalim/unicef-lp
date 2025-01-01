package sd.gov.moe.lp.web

import com.narbase.kunafa.core.lifecycle.Observable
import sd.gov.moe.lp.web.login.LoginPageNavigator
import sd.gov.moe.lp.web.storage.StorageManager
import sd.gov.moe.lp.web.views.basePage.HomePageNavigator


class AppViewController : LoginPageNavigator,
    HomePageNavigator {

    companion object {
        val loginState = Observable<Boolean>()
    }

    override fun onLoggedInSuccessful() {
        StorageManager.setUserLoggedIn(true)
        loginState.value = true
    }

    override fun onLogoutSelected() {
        StorageManager.setUserLoggedIn(false)
        loginState.value = false
    }

    fun onViewCreated() {
        loginState.value = StorageManager.isUserLoggedIn()
    }

}
