package sd.gov.moe.lp.web

import com.narbase.kunafa.core.components.Component
import com.narbase.kunafa.core.components.View
import com.narbase.kunafa.core.components.verticalLayout
import com.narbase.kunafa.core.css.*
import com.narbase.kunafa.core.dimensions.dependent.matchParent
import com.narbase.kunafa.core.lifecycle.LifecycleOwner
import com.narbase.kunafa.core.routing.Router
import sd.gov.moe.lp.web.login.LoginPageContent
import sd.gov.moe.lp.web.storage.StorageManager
import sd.gov.moe.lp.web.utils.eventbus.EventBus
import sd.gov.moe.lp.web.utils.notifications.NotificationsController
import sd.gov.moe.lp.web.views.basePage.BasePageComponent
import sd.gov.moe.lp.web.views.basePage.BasePageViewModel
import sd.gov.moe.lp.web.views.startup.StartupComponent
import kotlinx.browser.window

class AppComponent(
    private val appViewController: AppViewController,
    private val loginPage: LoginPageContent
) : Component() {
    private var basePage: BasePageComponent? = null

    override fun onViewCreated(lifecycleOwner: LifecycleOwner) {
        super.onViewCreated(lifecycleOwner)
        stringRuleSet("*") {
            outline = "none"
        }
        stringRuleSet("html, body, textarea, button, input") {
            fontFamily = StorageManager.language.appFontFamily
        }
        stringRuleSet(":not(.normalBar)::-webkit-scrollbar") {
            display = "none"
            this["-ms-overflow-style"] = "none"
        }
        stringRuleSet("textarea::-webkit-scrollbar") {
            display = "initial"
        }
    }

    override fun onViewMounted(lifecycleOwner: LifecycleOwner) {
        setupObservers()
        appViewController.onViewCreated()

    }

    override fun View?.getView(): View {
        return verticalLayout {
            id = "appView"
            style {
                width = matchParent
                height = matchParent
            }
        }
    }

    private fun setupObservers() {
        AppViewController.loginState.observe { loginState ->
            Router.invalidateCache()
            EventBus.clearAll()
            rootView?.clearAllChildren()
            NotificationsController.disconnect()
            when (loginState) {
                true -> {
                    rootView?.mount(StartupComponent(onReadyToStart = {
                        basePage = BasePageComponent(BasePageViewModel())
                        basePage?.let { rootView?.mount(it) }
                    }))
                }

                null, false -> {
                    val shouldDirect = window.location.pathname.trim().trim('/').isNotBlank()
                    if (shouldDirect) {
                        Router.navigateTo("/")
                    }
                    rootView?.mount(loginPage)
                }
            }
        }
    }

}
