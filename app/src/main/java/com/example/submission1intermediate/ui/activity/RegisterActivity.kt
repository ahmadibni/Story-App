package com.example.submission1intermediate.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.example.submission1intermediate.databinding.ActivityRegisterBinding
import com.example.submission1intermediate.ui.viewmodel.RegisterViewModel
import com.example.submission1intermediate.utils.ViewModelFactory
import com.example.submission1intermediate.utils.Result

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    companion object{
        private const val FIELD_REQUIRED = "Field tidak boleh kosong"
        private const val FIELD_IS_NOT_VALID = "Email tidak valid"
        private const val FIELD_MIN = "Min. 8 Karakter"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()

            if(name.isEmpty()){
                binding.edRegisterName.error = FIELD_REQUIRED
                return@setOnClickListener
            }
            if(!isValidEmail(email)){
                binding.edRegisterEmail.error = FIELD_IS_NOT_VALID
                return@setOnClickListener
            }
            if(password.length < 8){
                binding.edRegisterPassword.error = FIELD_MIN
                return@setOnClickListener
            }
            viewModel.registerUser(name, email, password).observe(this){result->
                if(result != null){
                    when(result){
                        is Result.Loading -> showLoading(true)
                        is Result.Success -> {
                            showLoading(false)
                            finish()
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
    }

    private fun showLoading(isLoading: Boolean){
        binding.progressBarRegister.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun isValidEmail(email: CharSequence): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}