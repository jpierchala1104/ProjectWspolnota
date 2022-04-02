package com.example.projektwspolnota.login.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast

import com.example.projektwspolnota.R
import com.example.projektwspolnota.login.mvi.*
import com.example.projektwspolnota.resolution.ui.MainActivity
import com.example.projektwspolnota.services.SessionService
import com.hannesdorfmann.mosby3.mvi.MviActivity
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : MviActivity<LoginView, LoginViewPresenter>(), LoginView {

    override fun createPresenter(): LoginViewPresenter {
        return LoginViewPresenter(LoginComponentUseCase(SessionService))
    }

    override val initIntent: Observable<Unit> = Observable.just(Unit)

    override val loginIntent: Observable<Credentials>
        get() = login
            .clicks()
            .map {
                Credentials(username.text.toString(), password.text.toString())
            }

    override fun render(state: LoginState) {
        if (state.isLoggingIn) {
            loading.start()
            loading.visibility = View.VISIBLE
        } else {
            loading.visibility = View.GONE
            loading.stop()
            if (SessionService.isAuthenticated() && state.loggedIn != true)
            {
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
                state.loggedIn = true
                password.text.clear()
            }
        }
        if (state.errorMessage != null) {
            Toast.makeText(this, state.errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)
    }
}
