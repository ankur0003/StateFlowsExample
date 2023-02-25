package com.ankur.stateflowexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.ankur.stateflowexample.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    val viewModel :MyViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnLogin.setOnClickListener {
            viewModel.login(
                binding.etUsername.text.toString(),
                binding.etPassword.text.toString()
            )
        }
        //now we need to launch corooutine to collect a flow
        // we are not going too use a launch here because it can lead to crashes
        //if ui keeps getting updated in the background
        // instead we will use a launchWhenStarted
        lifecycleScope.launchWhenStarted {
            //this coroutine will be launched
            // once this activity is in started state
            viewModel.loginUIState.collect{
                when(it){
                    is MyViewModel.LoginUIState.Success ->{
                        Snackbar.make(
                            binding.root,
                            "Successfully logged in",Snackbar.LENGTH_LONG
                        ).show()

                        binding.progressBar.isVisible = false
                    }
                    is MyViewModel.LoginUIState.Error ->{
                        Snackbar.make(
                            binding.root,
                            it.message,Snackbar.LENGTH_LONG
                        ).show()
                        binding.progressBar.isVisible = false

                    }
                    is MyViewModel.LoginUIState.Loading ->{
                        binding.progressBar.isVisible = true

                    }
                    else ->Unit
                }
            }


        }
    }
}