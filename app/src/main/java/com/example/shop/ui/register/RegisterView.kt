package com.example.shop.ui.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
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
import dagger.hilt.android.lifecycle.HiltViewModel

data class RegisterState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val error: String? = null,
    val loading: Boolean = false
)

@Composable
fun RegisterView(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val viewModel: RegisterViewModel = hiltViewModel()
    val uiState by viewModel.uiState

    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            modifier = Modifier.padding(8.dp),
            value = uiState.name,
            onValueChange = { viewModel.onChangeName(it) },
            label = { Text("name") },
        )

        TextField(
            modifier = Modifier.padding(8.dp),
            value = uiState.email,
            onValueChange = { viewModel.onChangeEmail(it) },
            label = { Text("email") },
        )

        TextField(
            modifier = Modifier.padding(8.dp),
            value = uiState.password,
            onValueChange = { viewModel.onChangePassword(it) },
            label = { Text("password") },
        )



        Button(
            modifier = Modifier.padding(8.dp),
            onClick = {
                viewModel.register(onRegisterSuccess = {
                    navController.navigate("quotes")
                })
            },
            enabled = !uiState.loading
        ) {
            Text("Register")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterViewPreview() {
    ShopTheme {
        RegisterView(navController = rememberNavController())
    }
}
