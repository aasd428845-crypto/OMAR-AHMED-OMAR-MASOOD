package com.wasal.app.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
fun RegisterScreen(navController: NavController) {
    val viewModel: AuthViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    var submitted by remember { mutableStateOf(false) }

    val isLoading = uiState is AuthUiState.Loading

    // Validation flags
    val isNameInvalid = submitted && fullName.trim().isEmpty()
    val isEmailInvalid = submitted && (email.trim().isEmpty() || !email.contains("@"))
    val isPhoneInvalid = submitted && phone.length != 9
    val isPasswordInvalid = submitted && password.length < 6
    val isConfirmPasswordInvalid = submitted && confirmPassword != password

    // Navigate on Success
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

            // Register Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = WasalWhite)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        text = "إنشاء حساب جديد",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = WasalBlack
                    )
                    
                    Spacer(Modifier.height(4.dp))
                    
                    Text(
                        text = "انضم إلى وصال وابدأ الطلب",
                        style = MaterialTheme.typography.bodyMedium,
                        color = WasalGrey,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )

                    Spacer(Modifier.height(24.dp))

                    // 1. Full Name
                    Text(
                        text = "الاسم الكامل",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Right,
                        color = WasalBlack
                    )
                    Spacer(Modifier.height(6.dp))
                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        placeholder = { Text("أدخل الاسم الكامل", color = WasalGrey.copy(alpha = 0.6f)) },
                        leadingIcon = { Icon(Icons.Default.Person, null, tint = WasalGrey) },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        isError = isNameInvalid,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = WasalGreen,
                            unfocusedBorderColor = WasalDivider,
                            focusedTextColor = WasalBlack,
                            unfocusedTextColor = WasalBlack
                        )
                    )
                    if (isNameInvalid) {
                        Text(
                            text = "يرجى إدخال اسمك الكامل",
                            style = MaterialTheme.typography.bodySmall,
                            color = WasalRed,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Right
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    // 2. Email Address
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
                                "example@email.com",
                                color = WasalGrey.copy(alpha = 0.6f),
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Left
                            )
                        },
                        leadingIcon = { Icon(Icons.Default.Email, null, tint = WasalGrey) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        isError = isEmailInvalid,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = WasalGreen,
                            unfocusedBorderColor = WasalDivider,
                            focusedTextColor = WasalBlack,
                            unfocusedTextColor = WasalBlack
                        ),
                        textStyle = LocalTextStyle.current.copy(textDirection = TextDirection.Ltr)
                    )
                    if (isEmailInvalid) {
                        Text(
                            text = "البريد الإلكتروني المدخل غير صالح",
                            style = MaterialTheme.typography.bodySmall,
                            color = WasalRed,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Right
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    // 3. Phone number
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
                        OutlinedTextField(
                            value = phone,
                            onValueChange = { input -> phone = input.filter { it.isDigit() } },
                            modifier = Modifier
                                .weight(1f)
                                .height(52.dp),
                            placeholder = {
                                Text(
                                    "7XX XXX XXX",
                                    color = WasalGrey.copy(alpha = 0.6f),
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Left
                                )
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            isError = isPhoneInvalid,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = WasalGreen,
                                unfocusedBorderColor = WasalDivider,
                                focusedTextColor = WasalBlack,
                                unfocusedTextColor = WasalBlack
                            ),
                            textStyle = LocalTextStyle.current.copy(textDirection = TextDirection.Ltr)
                        )

                        Spacer(Modifier.width(8.dp))

                        Box(
                            modifier = Modifier
                                .width(72.dp)
                                .height(52.dp)
                                .background(WasalGreenSurface, RoundedCornerShape(12.dp))
                                .border(BorderStroke(1.dp, if (isPhoneInvalid) WasalRed else WasalGreen), RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "+967",
                                style = MaterialTheme.typography.bodyLarge,
                                color = if (isPhoneInvalid) WasalRed else WasalGreen,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    if (isPhoneInvalid) {
                        Text(
                            text = "رقم الهاتف يجب أن يتكون من 9 أرقام",
                            style = MaterialTheme.typography.bodySmall,
                            color = WasalRed,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Right
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    // 4. Password
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
                        placeholder = { Text("أدخل كلمة السر (٦ أحرف على الأقل)", color = WasalGrey.copy(alpha = 0.6f)) },
                        leadingIcon = { Icon(Icons.Default.Lock, null, tint = WasalGrey) },
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
                        isError = isPasswordInvalid,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = WasalGreen,
                            unfocusedBorderColor = WasalDivider,
                            focusedTextColor = WasalBlack,
                            unfocusedTextColor = WasalBlack
                        )
                    )
                    if (isPasswordInvalid) {
                        Text(
                            text = "يجب أن تكون كلمة السر من 6 خانات على الأقل",
                            style = MaterialTheme.typography.bodySmall,
                            color = WasalRed,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Right
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    // 5. Confirm Password
                    Text(
                        text = "تأكيد كلمة السر",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Right,
                        color = WasalBlack
                    )
                    Spacer(Modifier.height(6.dp))
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        placeholder = { Text("أعد كتابة كلمة السر لتأكيدها", color = WasalGrey.copy(alpha = 0.6f)) },
                        leadingIcon = { Icon(Icons.Default.Lock, null, tint = WasalGrey) },
                        trailingIcon = {
                            IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                                Icon(
                                    imageVector = if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = null,
                                    tint = WasalGrey
                                )
                            }
                        },
                        visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        isError = isConfirmPasswordInvalid,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = WasalGreen,
                            unfocusedBorderColor = WasalDivider,
                            focusedTextColor = WasalBlack,
                            unfocusedTextColor = WasalBlack
                        )
                    )
                    if (isConfirmPasswordInvalid) {
                        Text(
                            text = "كلمتا السر غير متطابقتين",
                            style = MaterialTheme.typography.bodySmall,
                            color = WasalRed,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Right
                        )
                    }

                    Spacer(Modifier.height(28.dp))

                    // Register Button
                    Button(
                        onClick = {
                            submitted = true
                            val isValid = fullName.trim().isNotEmpty() &&
                                    email.contains("@") &&
                                    phone.length == 9 &&
                                    password.length >= 6 &&
                                    confirmPassword == password
                            
                            if (isValid) {
                                viewModel.registerWithEmail(email, password, fullName, phone)
                            }
                        },
                        enabled = !isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = WasalGreen)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = WasalWhite,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = "إنشاء الحساب",
                                style = MaterialTheme.typography.titleMedium,
                                color = WasalWhite,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(Modifier.height(18.dp))

                    // Link back to Login Screen
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "لديك حساب بالفعل؟",
                            style = MaterialTheme.typography.bodySmall,
                            color = WasalGrey
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = "تسجيل الدخول",
                            style = MaterialTheme.typography.bodySmall,
                            color = WasalGreen,
                            fontWeight = FontWeight.Bold,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier.clickable {
                                navController.navigate(Screen.Login.route) {
                                    popUpTo(Screen.Register.route) { inclusive = true }
                                }
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // Guest Back button
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
