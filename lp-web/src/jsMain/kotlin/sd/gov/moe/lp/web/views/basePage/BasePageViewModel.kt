package sd.gov.moe.lp.web.views.basePage

import sd.gov.moe.lp.web.utils.notifications.NotificationsController


class BasePageViewModel {

    fun onViewCreated() {
        NotificationsController.connect()
    }

    data class RouteDetails(var href: String, val title: String, val image: String? = null)

}
