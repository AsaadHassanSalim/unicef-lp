package sd.gov.moe.lp.web.views.startup

import com.narbase.kunafa.core.components.Component
import com.narbase.kunafa.core.components.View
import com.narbase.kunafa.core.components.textView
import com.narbase.kunafa.core.components.verticalLayout
import com.narbase.kunafa.core.css.*
import com.narbase.kunafa.core.dimensions.px
import com.narbase.kunafa.core.drawable.Color
import com.narbase.kunafa.core.lifecycle.LifecycleOwner
import sd.gov.moe.lp.web.common.AppColors
import sd.gov.moe.lp.web.network.makeNotVisible
import sd.gov.moe.lp.web.network.makeVisible
import sd.gov.moe.lp.web.translations.localized
import sd.gov.moe.lp.web.utils.BasicUiState
import sd.gov.moe.lp.web.utils.logoutUser
import sd.gov.moe.lp.web.utils.views.*

/*
 * Copyright 2017-2020 Narbase technologies and contributors. Use of this source code is governed by the MIT License.
 */
class StartupComponent(private val onReadyToStart: () -> Unit) : Component() {

    private val viewModel = StartupViewModel()
    private var gettingConfigView: View? = null
    private var loadingView: View? = null
    private var errorView: View? = null
    private var permissivePopUpDialog: PopUpDialog? = null

    override fun onViewCreated(lifecycleOwner: LifecycleOwner) {
        super.onViewCreated(lifecycleOwner)
        viewModel.getConfigUiState.observe { onConfigUiStateChanged(it) }
        viewModel.getConfig()
    }

    private fun onConfigUiStateChanged(uiState: BasicUiState?) {
        uiState ?: return
        makeNotVisible(loadingView, errorView)
        when (uiState) {
            BasicUiState.Loading -> makeVisible(loadingView)
            BasicUiState.Loaded -> {
                onConfigLoaded()
            }

            BasicUiState.Error -> makeVisible(errorView)
        }
    }

    private fun onConfigLoaded() {
        onStartupSuccessful()
    }

    private fun onStartupSuccessful() {
        rootView?.parent?.unMount(this)
        onReadyToStart()
    }

    override fun View?.getView() = verticalLayout {
        permissivePopUpDialog = popUpDialog { }
        style {
            matchParentDimensions
            alignItems = Alignment.Center
            justifyContent = JustifyContent.Center
            backgroundColor = Color.white
        }

        gettingConfigView = verticalLayout {
            style {
                matchParentDimensions
                alignItems = Alignment.Center
                justifyContent = JustifyContent.Center
                backgroundColor = Color.white
            }

            loadingView = verticalLayout {

                textView {
                    text = "Starting up..".localized()
                }
                loadingIndicator()
            }
            errorView = verticalLayout {

                textView {
                    text = "Failed to load. Retry".localized()
                    style {
                        color = AppColors.redLight
                        pointerCursor()
                    }
                }
                onClick = {
                    viewModel.getConfig()
                }
            }

            textView {
                text = "Logout".localized()
                style {
                    borderRadius = 4.px
                    margin = 32.px
                    padding = 8.px
                    pointerCursor()
                    fontSize = 14.px
                    color = AppColors.text
                    hover {
                        backgroundColor = AppColors.separatorLight
                    }
                }
                onClick = {
                    logoutUser()
                }
            }
        }

    }
}
