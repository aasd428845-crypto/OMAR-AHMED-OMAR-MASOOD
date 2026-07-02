package com.wasal.app.ui.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.wasal.app.R
import com.wasal.app.ui.navigation.Screen
import com.wasal.app.ui.theme.*

@Composable
fun LoginScreen(navController: NavController) {
    val viewModel: AuthViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // State variables
    var selectedTab by remember { mutableStateOf(0) } // 0 = Email, 1 = Phone
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var phone by remember { mutableStateOf("") }
    val isLoading = uiState is AuthUiState.Loading

    // Navigation on Success
    LaunchedEffect(uiState) {
        if (uiState is AuthUiState.Success) {
            val route = (uiState as AuthUiState.Success).route
            navController.navigate(route) {
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

            // Logo Centered
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

            // Auth white Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = WasalWhite)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    // Title
                    Text(
                        text = "تسجيل الدخول",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = WasalBlack
                    )
                    
                    Spacer(Modifier.height(4.dp))
                    
                    Text(
                        text = "أهلاً بعودتك! اختر طريقة تسجيل الدخول",
                        style = MaterialTheme.typography.bodyMedium,
                        color = WasalGrey,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )

                    Spacer(Modifier.height(20.dp))

                    // Google Sign-In Button (Outlined)
                    OutlinedButton(
                        onClick = { viewModel.signInWithGoogle(context) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color(0xFFDDDDDD)),
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = WasalWhite)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "تسجيل الدخول بـ Google",
                                color = WasalBlack,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(Modifier.width(10.dp))
                            Icon(
                                painter = painterResource(R.drawable.ic_google),
                                contentDescription = "Google",
                                tint = Color.Unspecified,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Spacer(Modifier.height(20.dp))

                    // Tab Container
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .background(Color(0xFFF5F5F5), RoundedCornerShape(12.dp))
                            .padding(4.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Tab 1: Email
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .background(
                                        color = if (selectedTab == 0) WasalGreen else Color.Transparent,
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .clickable { selectedTab = 0 },
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Email,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp),
                                        tint = if (selectedTab == 0) WasalWhite else WasalGrey
                                    )
                                    Spacer(Modifier.width(6.dp))
                                    Text(
                                        text = "البريد الإلكتروني",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                                        color = if (selectedTab == 0) WasalWhite else WasalGrey
                                    )
                                }
                            }

                            // Tab 2: Phone
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .background(
                                        color = if (selectedTab == 1) WasalGreen else Color.Transparent,
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .clickable { selectedTab = 1 },
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Phone,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp),
                                        tint = if (selectedTab == 1) WasalWhite else WasalGrey
                                    )
                                    Spacer(Modifier.width(6.dp))
                                    Text(
                                        text = "رقم الهاتف",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                                        color = if (selectedTab == 1) WasalWhite else WasalGrey
                                    )
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(24.dp))

                    // Tab View switching
                    if (selectedTab == 0) {
                        // Email Sign In Tab
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "البريد الإلكتروني",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Right,
                                color = WasalBlack
                            )
                            Spacer(Modifier.height(6.dp))
                            OutlinedTextField(
                                value = email,
                                onValueChange = { email = it },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp),
                                placeholder = {
                                    Text(
                                        text = "example@email.com",
                                        color = WasalGrey.copy(alpha = 0.6f),
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Left
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Email,
                                        contentDescription = null,
                                        tint = WasalGrey
                                    )
                                },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = WasalGreen,
                                    unfocusedBorderColor = WasalDivider,
                                    focusedTextColor = WasalBlack,
                                    unfocusedTextColor = WasalBlack
                                ),
                                textStyle = LocalTextStyle.current.copy(textDirection = TextDirection.Ltr)
                            )

                            Spacer(Modifier.height(16.dp))

                            Text(
                                text = "كلمة السر",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Right,
                                color = WasalBlack
                            )
                            Spacer(Modifier.height(6.dp))
                            OutlinedTextField(
                                value = password,
                                onValueChange = { password = it },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp),
                                placeholder = {
                                    Text(
                                        text = "••••••••",
                                        color = WasalGrey.copy(alpha = 0.6f)
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = null,
                                        tint = WasalGrey
                                    )
                                },
                                trailingIcon = {
                                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                        Icon(
                                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                            contentDescription = null,
                                            tint = WasalGrey
                                        )
                                    }
                                },
                                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = WasalGreen,
                                    unfocusedBorderColor = WasalDivider,
                                    focusedTextColor = WasalBlack,
                                    unfocusedTextColor = WasalBlack
                                )
                            )

                            Spacer(Modifier.height(24.dp))

                            // Action Login button
                            Button(
                                onClick = { viewModel.loginWithEmail(email, password) },
                                enabled = email.isNotBlank() && password.isNotBlank() && !isLoading,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = WasalGreen,
                                    disabledContainerColor = WasalGreen.copy(alpha = 0.5f)
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
                                        text = "تسجيل الدخول",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = WasalWhite,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Spacer(Modifier.height(10.dp))
                            
                            Text(
                                text = "يُسستخدم هذا الخيار للحسابات التي تم إنشاؤها بالبريد الإلكتروني",
                                style = MaterialTheme.typography.bodySmall,
                                color = WasalGrey,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        // Phone OTP Login Tab
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "رقم الهاتف",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Right,
                                color = WasalBlack
                            )
                            Spacer(Modifier.height(6.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Phone number text field
                                OutlinedTextField(
                                    value = phone,
                                    onValueChange = { input -> phone = input.filter { it.isDigit() } },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(52.dp),
                                    placeholder = {
                                        Text(
                                            text = "7XX XXX XXX",
                                            color = WasalGrey.copy(alpha = 0.6f),
                                            modifier = Modifier.fillMaxWidth(),
                                            textAlign = TextAlign.Left
                                        )
                                    },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                    singleLine = true,
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = WasalGreen,
                                        unfocusedBorderColor = WasalDivider,
                                        focusedTextColor = WasalBlack,
                                        unfocusedTextColor = WasalBlack
                                    ),
                                    textStyle = LocalTextStyle.current.copy(textDirection = TextDirection.Ltr)
                                )

                                Spacer(Modifier.width(8.dp))

                                // +967 Prefix Box on Right (visually RTL leading/leading visually on the right)
                                Box(
                                    modifier = Modifier
                                        .width(72.dp)
                                        .height(52.dp)
                                        .background(WasalGreenSurface, RoundedCornerShape(12.dp))
                                        .border(BorderStroke(1.dp, WasalGreen), RoundedCornerShape(12.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "+967",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = WasalGreen,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Spacer(Modifier.height(6.dp))
                            
                            Text(
                                text = "أدخل رقمك بدون رمز البلد (+967)",
                                style = MaterialTheme.typography.bodySmall,
                                color = WasalGrey,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Right
                            )

                            Spacer(Modifier.height(24.dp))

                            // Send OTP Button
                            val isPhoneValid = phone.length >= 9
                            Button(
                                onClick = { viewModel.loginWithPhone(phone) },
                                enabled = isPhoneValid && !isLoading,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isPhoneValid) WasalGreen else Color(0xFFDDDDDD),
                                    contentColor = if (isPhoneValid) WasalWhite else WasalGrey,
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
                                        text = "إرسال رمز التحقق",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isPhoneValid) WasalWhite else WasalGrey
                                    )
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(20.dp))
                    
                    HorizontalDivider(color = WasalDivider, thickness = 1.dp)
                    
                    Spacer(Modifier.height(10.dp))

                    // Forgot access action
                    TextButton(
                        onClick = { /* Handle Forgot Account */ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "لا تستطيع الوصول لحسابك؟",
                            style = MaterialTheme.typography.bodySmall,
                            color = WasalGrey,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // Register action
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "ليس لديك حساب؟",
                            style = MaterialTheme.typography.bodySmall,
                            color = WasalGrey
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = "إنشاء حساب جديد",
                            style = MaterialTheme.typography.bodySmall,
                            color = WasalGreen,
                            fontWeight = FontWeight.Bold,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier.clickable {
                                navController.navigate(Screen.Register.route)
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // Bottom Return to Main Guest Button
            Row(
                modifier = Modifier
                    .clickable {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
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
                    text = "العودة للصفحة الرئيسية",
                    style = MaterialTheme.typography.bodyMedium,
                    color = WasalGrey
                )
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}
