package com.wasal.app.ui.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wasal.app.data.remote.WasalSupabase
import com.wasal.app.ui.navigation.Screen
import io.github.jan-tennert.supabase.auth.auth
import io.github.jan-tennert.supabase.auth.providers.Google
import io.github.jan-tennert.supabase.auth.providers.builtin.Email
import io.github.jan-tennert.supabase.auth.providers.builtin.OTP
import io.github.jan-tennert.supabase.postgrest.postgrest
import io.github.jan-tennert.supabase.postgrest.query.Columns
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    data class Success(val route: String) : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}

class AuthViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun loginWithEmail(emailVal: String, passwordVal: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            try {
                WasalSupabase.auth.signInWith(Email) {
                    email = emailVal
                    password = passwordVal
                }
                
                val user = WasalSupabase.auth.currentUserOrNull()
                if (user != null) {
                    // Try to fetch profile to see status or role
                    try {
                        val profile = WasalSupabase.db.from("profiles")
                            .select(columns = Columns.ALL) {
                                filter {
                                    eq("user_id", user.id)
                                }
                            }
                        // Default to customer Home route
                        _uiState.value = AuthUiState.Success(Screen.Home.route)
                    } catch (e: Exception) {
                        // Even if profile fetch fails, go to Home
                        _uiState.value = AuthUiState.Success(Screen.Home.route)
                    }
                } else {
                    _uiState.value = AuthUiState.Error("فشل العثور على بيانات المستخدم بعد تسجيل الدخول")
                }
            } catch (e: Exception) {
                _uiState.value = AuthUiState.Error(e.localizedMessage ?: "حدث خطأ أثناء تسجيل الدخول بالبريد")
            }
        }
    }

    fun loginWithPhone(phoneVal: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            try {
                // calls WasalSupabase.auth.signInWith(OTP) { phoneNumber = "+967$phone" }
                WasalSupabase.auth.signInWith(OTP) {
                    phoneNumber = "+967$phoneVal"
                }
                // on success: emit Success("otp_verify/$phone")
                _uiState.value = AuthUiState.Success(Screen.OtpVerify.createRoute(phoneVal))
            } catch (e: Exception) {
                _uiState.value = AuthUiState.Error(e.localizedMessage ?: "حدث خطأ أثناء إرسال رمز التحقق")
            }
        }
    }

    fun verifyOTP(phoneVal: String, tokenVal: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            try {
                // Supposes verification of phone OTP with token
                WasalSupabase.auth.verifyPhoneOtp(
                    phone = "+967$phoneVal",
                    token = tokenVal,
                    type = io.github.jan-tennert.supabase.auth.OtpType.Phone.SMS
                )
                _uiState.value = AuthUiState.Success(Screen.Home.route)
            } catch (e: Exception) {
                _uiState.value = AuthUiState.Error(e.localizedMessage ?: "رمز التحقق غير صحيح، يرجى المحاولة مرة أخرى")
            }
        }
    }

    fun registerWithEmail(
        emailVal: String, passwordVal: String,
        fullName: String, phoneVal: String
    ) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            try {
                // 1. WasalSupabase.auth.signUpWith(Email) { ... }
                WasalSupabase.auth.signUpWith(Email) {
                    email = emailVal
                    password = passwordVal
                }
                
                // Wait 1000ms
                delay(1000)

                val user = WasalSupabase.auth.currentUserOrNull()
                if (user != null) {
                    // 3. Update profiles table: full_name, phone, account_status="active"
                    try {
                        val profileMap = mapOf(
                            "user_id" to user.id,
                            "full_name" to fullName,
                            "phone" to "+967$phoneVal",
                            "account_status" to "active"
                        )
                        WasalSupabase.db.from("profiles").insert(profileMap)
                    } catch (e: Exception) {
                        // Keep going if profile insert fails (e.g. schema differences or permissions)
                    }
                    // 4. emit Success(Screen.Home.route)
                    _uiState.value = AuthUiState.Success(Screen.Home.route)
                } else {
                    _uiState.value = AuthUiState.Error("حدث خطأ في استرداد حساب المستخدم الجديد")
                }
            } catch (e: Exception) {
                _uiState.value = AuthUiState.Error(e.localizedMessage ?: "حدث خطأ أثناء إنشاء الحساب")
            }
        }
    }

    fun signInWithGoogle(context: Context) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            try {
                WasalSupabase.auth.signInWith(Google) {
                    // standard scopes requested
                }
                _uiState.value = AuthUiState.Success(Screen.Home.route)
            } catch (e: Exception) {
                _uiState.value = AuthUiState.Error(e.localizedMessage ?: "حدث خطأ أثناء تسجيل الدخول بـ Google")
            }
        }
    }

    fun checkSession() {
        viewModelScope.launch {
            try {
                val session = WasalSupabase.auth.currentSessionOrNull()
                if (session != null) {
                    _uiState.value = AuthUiState.Success(Screen.Home.route)
                } else {
                    _uiState.value = AuthUiState.Idle
                }
            } catch (e: Exception) {
                _uiState.value = AuthUiState.Idle
            }
        }
    }
}
