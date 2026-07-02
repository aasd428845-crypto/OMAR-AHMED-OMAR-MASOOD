package com.wasal.app.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.wasal.app.R
import com.wasal.app.ui.navigation.Screen
import com.wasal.app.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun OtpScreen(navController: NavController, phone: String) {
    val viewModel: AuthViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    var otpValue by remember { mutableStateOf("") }
    val isLoading = uiState is AuthUiState.Loading

    // Countdown Timer for Resend
    var timeLeft by remember { mutableStateOf(60) }
    LaunchedEffect(timeLeft) {
        if (timeLeft > 0) {
            delay(1000)
            timeLeft -= 1
        }
    }

    // Handle Success Navigation
    LaunchedEffect(uiState) {
        if (uiState is AuthUiState.Success) {
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.Login.route) { inclusive = true }
            }
        }
    }

    // Snackbar error notifier
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(uiState) {
        if (uiState is AuthUiState.Error) {
            val errorMessage = (uiState as AuthUiState.Error).message
            snackbarHostState.showSnackbar(errorMessage)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = WasalBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(WasalBackground)
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(32.dp))

            // Logo Header
            Image(
                painter = painterResource(R.drawable.wasal_logo),
                contentDescription = "وصال",
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(24.dp))
            )
            
            Spacer(Modifier.height(12.dp))
            
            Text(
                text = "توصيل أسرع، حياة أسهل",
                style = MaterialTheme.typography.bodyMedium,
                color = WasalGrey,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(24.dp))

            // Verification Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = WasalWhite)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "أدخل رمز التحقق",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = WasalBlack,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = "تم إرسال رمز إلى +967 $phone",
                        style = MaterialTheme.typography.bodyMedium,
                        color = WasalGrey,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(28.dp))

                    // Row of 6 Box cells
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // Hidden actual input field
                        BasicTextField(
                            value = otpValue,
                            onValueChange = { input ->
                                if (input.length <= 6 && input.all { it.isDigit() }) {
                                    otpValue = input
                                }
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            // Row displaying visual boxes
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                repeat(6) { index ->
                                    val digit = if (index < otpValue.length) otpValue[index].toString() else ""
                                    val isFocused = index == otpValue.length
                                    
                                    Box(
                                        modifier = Modifier
                                            .width(44.dp)
                                            .height(56.dp)
                                            .background(
                                                color = if (isFocused) WasalGreenSurface else Color(0xFFF9F9F9),
                                                shape = RoundedCornerShape(12.dp)
                                            )
                                            .border(
                                                width = if (isFocused) 2.dp else 1.dp,
                                                color = if (isFocused) WasalGreen else Color(0xFFDDDDDD),
                                                shape = RoundedCornerShape(12.dp)
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = digit,
                                            style = MaterialTheme.typography.titleLarge.copy(fontSize = 22.sp),
                                            color = WasalBlack,
                                            fontWeight = FontWeight.Bold,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(32.dp))

                    // Verify button
                    val isOtpComplete = otpValue.length == 6
                    Button(
                        onClick = { viewModel.verifyOTP(phone, otpValue) },
                        enabled = isOtpComplete && !isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = WasalGreen,
                            disabledContainerColor = Color(0xFFEEEEEE),
                            disabledContentColor = WasalGrey
                        )
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = WasalWhite,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = "تحقق",
                                style = MaterialTheme.typography.titleMedium,
                                color = if (isOtpComplete) WasalWhite else WasalGrey,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(Modifier.height(24.dp))

                    // Countdown and Resend
                    if (timeLeft > 0) {
                        Text(
                            text = "إعادة الإرسال بعد $timeLeft ثانية",
                            style = MaterialTheme.typography.bodySmall,
                            color = WasalGrey
                        )
                    } else {
                        TextButton(
                            onClick = {
                                timeLeft = 60
                                viewModel.loginWithPhone(phone)
                            }
                        ) {
                            Text(
                                text = "إعادة إرسال الرمز",
                                style = MaterialTheme.typography.bodyMedium,
                                color = WasalGreen,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // Back button
            Row(
                modifier = Modifier
                    .clickable { navController.popBackStack() }
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = WasalGrey,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = "العودة للخطوة السابقة",
                    style = MaterialTheme.typography.bodyMedium,
                    color = WasalGrey
                )
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}
