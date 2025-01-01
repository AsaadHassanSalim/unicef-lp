package sd.gov.moe.lp.web.login

import com.narbase.kunafa.core.components.Button
import com.narbase.kunafa.core.components.ImageView
import com.narbase.kunafa.core.components.TextInput
import com.narbase.kunafa.core.components.TextView
import com.narbase.kunafa.core.css.classRuleSet
import com.narbase.kunafa.core.css.color
import sd.gov.moe.lp.dto.common.auth.TokenDtos
import sd.gov.moe.lp.dto.common.network.CommonCodes.BASIC_SUCCESS
import sd.gov.moe.lp.web.common.AppColors
import sd.gov.moe.lp.web.network.ServerCaller
import sd.gov.moe.lp.web.network.networkCall
import sd.gov.moe.lp.web.storage.StorageManager
import org.w3c.xhr.XMLHttpRequest


class LoginViewController(
    private val navigator: LoginPageNavigator
) {
    var loginButton: Button? = null
    var usernameTextInput: TextInput? = null
    var passwordTextInput: TextInput? = null
    var loadingImageView: ImageView? = null
    var statusTextView: TextView? = null

    private val successfullTextClass = classRuleSet {
        color = AppColors.greenLight
    }
    private val errorTextClass = classRuleSet {
        color = AppColors.redLight
    }
    private val noInternetTextClass = classRuleSet {
        color = AppColors.separatorLight
    }

    fun onViewMounted() {
        passwordTextInput?.text = ""
        statusTextView?.text = ""
    }

    fun onViewCreated() {
        setupClickListeners()
    }

    private fun setupClickListeners() {
        loginButton?.onClick = {
            loadingImageView?.isVisible = true
            loginButton?.isVisible = false
            statusTextView?.isVisible = false

            ServerCaller.authenticateUser(
                usernameTextInput?.text?.trim(),
                passwordTextInput?.text,
                onSuccess = { xmlHttp ->
                    if (xmlHttp.status == 200.toShort()) {
                        onSuccess(xmlHttp)
                    } else {
                        onUnauthenticated()
                    }
                },
                onError = {
                    onFailure()
                }
            )

        }
    }

    private fun onUnauthenticated() {
        loadingImageView?.isVisible = false
        loginButton?.isVisible = true
        statusTextView?.addRuleSet(errorTextClass)
        statusTextView?.removeRuleSet(successfullTextClass)
        statusTextView?.removeRuleSet(noInternetTextClass)
        statusTextView?.text = "Username or password are incorrect" //todo change this message to include wrong slug
        statusTextView?.isVisible = true
    }

    private fun onErrorWithMessage(message: String) {
        loadingImageView?.isVisible = false
        loginButton?.isVisible = true
        statusTextView?.addRuleSet(errorTextClass)
        statusTextView?.removeRuleSet(successfullTextClass)
        statusTextView?.removeRuleSet(noInternetTextClass)
        statusTextView?.text = message
        statusTextView?.isVisible = true
    }

    private fun onUserDisabled() {
        loadingImageView?.isVisible = false
        loginButton?.isVisible = true
        statusTextView?.addRuleSet(errorTextClass)
        statusTextView?.removeRuleSet(successfullTextClass)
        statusTextView?.removeRuleSet(noInternetTextClass)
        statusTextView?.text = "Your account has been disabled by the admin"
        statusTextView?.isVisible = true
    }

    private fun onSuccess(xmlHttp: XMLHttpRequest) {
        networkCall(
            onConnectionError = { onErrorWithMessage("Connection Error! Try again") },
            onUnknownError = { onErrorWithMessage("Unknown error occurred. Try again or contact support.") },
            onUnauthorized = { onErrorWithMessage("You don't have the required authorization") },
            onUserDisabled = { onUserDisabled() }) {
            val accessToken = (JSON.parse<TokenDtos.TokenResponse>(xmlHttp.responseText)).data?.access_token
            StorageManager.accessToken = accessToken
            val response = ServerCaller.login()
            if (response.status == "$BASIC_SUCCESS") {

                StorageManager.isFirstLogin = response.data.isFirstLogin
                loadingImageView?.isVisible = false
                loginButton?.isVisible = true
                statusTextView?.addRuleSet(successfullTextClass)
                statusTextView?.removeRuleSet(errorTextClass)
                statusTextView?.removeRuleSet(noInternetTextClass)
                statusTextView?.text = "Logged in successfully"
                statusTextView?.isVisible = true
                navigator.onLoggedInSuccessful()
            } else {
                StorageManager.accessToken = null
                onFailure()
            }
        }
    }

    private fun onFailure() {
        loadingImageView?.isVisible = false
        loginButton?.isVisible = true
        statusTextView?.removeRuleSet(errorTextClass)
        statusTextView?.removeRuleSet(successfullTextClass)
        statusTextView?.addRuleSet(noInternetTextClass)
        statusTextView?.text = "No internet connection"
        statusTextView?.isVisible = true
    }

}

