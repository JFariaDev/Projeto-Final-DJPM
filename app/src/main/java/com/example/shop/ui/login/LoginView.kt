package com.example.shop.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.shop.ui.theme.ShopTheme

data class LoginState(
    var email : String = "",
    var password : String = "",
    var error : String? = null,
    var loading : Boolean = false
)
@Composable
fun LoginView(
    modifier: Modifier = Modifier,
    navController : NavController
){

    val viewModel : LoginViewModel = hiltViewModel()
    val uiState by viewModel.uiState

    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        TextField(
            modifier = Modifier.padding(8.dp),
            value = uiState.email,
            onValueChange = {
                viewModel.onChangeEmail(it)
            },
            label = {
                Text("email")
            }
        )
        TextField(
            modifier = Modifier.padding(8.dp),
            value = uiState.password,
            onValueChange = {
                viewModel.onChangePassword(it)
            },
            label = {
                Text("password")
            }
        )
        if (uiState.error != null) {
            Text(uiState.error!!)
        }
        Button(
            modifier = Modifier.padding(8.dp),
            onClick = {
                viewModel.login(onLoginSuccess = {
                    navController.navigate("quotes")

                })

            }) {
            Text("Login")
        }
        TextButton(
            modifier = Modifier.padding(8.dp),
            onClick = { navController.navigate("register") }
        ) {
            Text("Register")
        }
    }

}

@Preview(showBackground = true)
@Composable
fun LoginViewPreview() {
    ShopTheme {
        LoginView(
            navController = rememberNavController()
        )
    }
}