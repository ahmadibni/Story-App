package com.example.submission1intermediate.ui.activity

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.example.submission1intermediate.databinding.ActivityMainBinding
import com.example.submission1intermediate.ui.viewmodel.MainViewModel
import com.example.submission1intermediate.utils.ViewModelFactory
import com.example.submission1intermediate.utils.Result

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setButtonEnable()
        setTextListener()
        playAnimation()

        viewModel.session.observe(this) { isLogin ->
            if (isLogin == true) {
                val moveToHome = Intent(this, HomeActivity::class.java)
                startActivity(moveToHome)
            }
        }

        binding.myButtonLogin.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()

            binding.edLoginEmail.clearFocus()
            binding.edLoginPassword.clearFocus()
            viewModel.logUser(email, password).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> showLoading(true)
                        is Result.Success -> {
                            showLoading(false)
                            val token = "Bearer " + result.data.loginResult.token
                            val userName = result.data.loginResult.name
                            viewModel.saveSession(token, true, userName)
                        }
                        is Result.Error -> {
                            showLoading(false)
                            Toast.makeText(
                                this,
                                "Terjadi kesalahan",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }

        binding.btnRegisterActivity.setOnClickListener {
            Intent(this, RegisterActivity::class.java).also {
                startActivity(it)
            }
        }
    }


    private fun playAnimation() {
        val anim1 = ObjectAnimator.ofFloat(binding.textView2, View.ALPHA, 1f).setDuration(500)
        val anim2 = ObjectAnimator.ofFloat(binding.textView, View.ALPHA, 1f).setDuration(500)
        val anim3 = ObjectAnimator.ofFloat(binding.edLoginEmail, View.ALPHA, 1f).setDuration(500)
        val anim4 = ObjectAnimator.ofFloat(binding.edLoginPassword, View.ALPHA, 1f).setDuration(500)
        val anim5 = ObjectAnimator.ofFloat(binding.myButtonLogin, View.ALPHA, 1f).setDuration(500)
        val anim6 =
            ObjectAnimator.ofFloat(binding.btnRegisterActivity, View.ALPHA, 1f).setDuration(500)

        val together = AnimatorSet().apply {
            playTogether(anim5, anim6)
        }

        AnimatorSet().apply {
            playSequentially(anim1, anim2, anim3, anim4, together)
            start()
        }
    }

    private fun setTextListener() {
        binding.edLoginEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setButtonEnable()
            }

            override fun afterTextChanged(s: Editable) {}
        })

        binding.edLoginPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setButtonEnable()
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }

    private fun setButtonEnable() {
        val passResult = binding.edLoginPassword.text.toString()
        val emailResult = binding.edLoginEmail.text.toString().trim()
        binding.myButtonLogin.isEnabled = isValidEmail(emailResult) && passResult.length >= 8
    }

    private fun isValidEmail(email: CharSequence): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBarMain.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}