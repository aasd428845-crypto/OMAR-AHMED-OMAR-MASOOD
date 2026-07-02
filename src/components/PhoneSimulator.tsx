import React, { useState, useEffect } from 'react';
import {
  Restaurant,
  MenuItem,
  MenuCategory,
  CartItem,
  DeliveryBanner,
  Order
} from '../types';
import {
  YEMENI_CITIES,
  CATEGORIES,
  BANNERS,
  RESTAURANTS,
  MENU_ITEMS
} from '../data';
import {
  MapPin,
  Search,
  ChevronLeft,
  ChevronRight,
  Plus,
  Minus,
  Trash2,
  ShoppingBag,
  History,
  User,
  Bell,
  Check,
  Package,
  Bike,
  Phone,
  MessageSquare,
  Compass,
  ArrowRight,
  ShieldCheck,
  CreditCard,
  X,
  PlusCircle,
  Mail,
  Lock,
  Eye,
  EyeOff
} from 'lucide-react';

export default function PhoneSimulator() {
  // Mobile Frame States
  const [currentScreen, setCurrentScreen] = useState<string>('splash');
  const [previousScreens, setPreviousScreens] = useState<string[]>([]);
  const [currentCity, setCurrentCity] = useState<string>('صنعاء');
  const [currentUser, setCurrentUser] = useState<{ name: string; phone: string; address: string } | null>({
    name: 'أحمد اليماني',
    phone: '777123456',
    address: 'صنعاء، حي حدة، شارع بيروت'
  });

  // Business States
  const [selectedCategory, setSelectedCategory] = useState<string>('cat_all');
  const [selectedRestaurant, setSelectedRestaurant] = useState<Restaurant | null>(null);
  const [cart, setCart] = useState<CartItem[]>([]);
  const [activeOrder, setActiveOrder] = useState<Order | null>(null);
  const [ordersHistory, setOrdersHistory] = useState<Order[]>([
    {
      id: 'WS-9843',
      restaurantName: 'مطاعم الشيباني الحديثة',
      items: [
        { name: 'نصف حبة مندي دجاج بلدي', quantity: 1, price: 3800 },
        { name: 'سلته صنعانية باللحم', quantity: 1, price: 4500 }
      ],
      totalPrice: 9100,
      date: '2026-06-24',
      status: 'delivered'
    }
  ]);

  // Notifications state
  const [notifications, setNotifications] = useState<string[]>([
    'تم قبول طلبك بنجاح من مطاعم الشيباني',
    'خصم يصل إلى ٣٠٪ على الوجبات الشعبية نهاية الأسبوع!',
    'مرحباً بك في وصال - أسرع تطبيق توصيل في اليمن 🇾🇪'
  ]);

  // Package delivery states
  const [packageDelivery, setPackageDelivery] = useState({
    pickup: 'صنعاء، جولة الرويشان',
    dropoff: 'صنعاء، جولة المصباحي',
    packageType: 'أوراق ومستندات',
    weight: 'أقل من ١ كجم',
    notes: 'يرجى تسليمه بحذر شديد.'
  });

  // UI state utilities
  const [loginPhone, setLoginPhone] = useState<string>('');
  const [loginOTP, setLoginOTP] = useState<string>('');
  const [otpSent, setOtpSent] = useState<boolean>(false);
  const [otpPhone, setOtpPhone] = useState<string>('');
  const [otpCountdown, setOtpCountdown] = useState<number>(60);
  const [loginTab, setLoginTab] = useState<number>(0); // 0 = Email, 1 = Phone
  const [loginEmail, setLoginEmail] = useState<string>('');
  const [loginPassword, setLoginPassword] = useState<string>('');
  const [loginPasswordVisible, setLoginPasswordVisible] = useState<boolean>(false);
  const [registerName, setRegisterName] = useState<string>('');
  const [registerEmail, setRegisterEmail] = useState<string>('');
  const [registerPhone, setRegisterPhone] = useState<string>('');
  const [registerPassword, setRegisterPassword] = useState<string>('');
  const [registerConfirmPassword, setRegisterConfirmPassword] = useState<string>('');
  const [registerPasswordVisible, setRegisterPasswordVisible] = useState<boolean>(false);
  const [registerConfirmPasswordVisible, setRegisterConfirmPasswordVisible] = useState<boolean>(false);
  const [registerCity, setRegisterCity] = useState<string>('صنعاء');
  const [formSubmitted, setFormSubmitted] = useState<boolean>(false);

  // OTP Countdown timer effect
  useEffect(() => {
    let interval: any;
    if (otpSent && otpCountdown > 0) {
      interval = setInterval(() => {
        setOtpCountdown((prev) => prev - 1);
      }, 1000);
    }
    return () => {
      if (interval) clearInterval(interval);
    };
  }, [otpSent, otpCountdown]);

  const [cartConflictRestaurant, setCartConflictRestaurant] = useState<{
    item: MenuItem;
    restaurant: Restaurant;
  } | null>(null);

  // Auto-advance Splash screen
  useEffect(() => {
    if (currentScreen === 'splash') {
      const timer = setTimeout(() => {
        navigate('login');
      }, 1500);
      return () => clearTimeout(timer);
    }
  }, [currentScreen]);

  // Simulated Tracker Updates
  useEffect(() => {
    if (activeOrder && activeOrder.status !== 'delivered') {
      const interval = setInterval(() => {
        setActiveOrder((prev) => {
          if (!prev) return null;
          let nextStatus: Order['status'] = prev.status;
          if (prev.status === 'pending') nextStatus = 'preparing';
          else if (prev.status === 'preparing') nextStatus = 'picked_up';
          else if (prev.status === 'picked_up') nextStatus = 'on_the_way';
          else if (prev.status === 'on_the_way') {
            nextStatus = 'delivered';
            // Also push to history when delivered
            setOrdersHistory((history) => [
              { ...prev, status: 'delivered' as const },
              ...history
            ]);
            // Add notification
            setNotifications((notes) => [
              `تم تسليم طلبك بنجاح من ${prev.restaurantName}. بالهناء والشفاء!`,
              ...notes
            ]);
          }
          return { ...prev, status: nextStatus };
        });
      }, 7000);
      return () => clearInterval(interval);
    }
  }, [activeOrder]);

  const navigate = (screen: string) => {
    setPreviousScreens((prev) => [...prev, currentScreen]);
    setCurrentScreen(screen);
  };

  const goBack = () => {
    if (previousScreens.length > 0) {
      const prev = previousScreens[previousScreens.length - 1];
      setPreviousScreens((prevList) => prevList.slice(0, -1));
      setCurrentScreen(prev);
    }
  };

  const getCartTotal = () => {
    const itemsTotal = cart.reduce((sum, item) => sum + item.menuItem.price * item.quantity, 0);
    const deliveryFee = cart.length > 0 ? 800 : 0;
    return { itemsTotal, deliveryFee, grandTotal: itemsTotal + deliveryFee };
  };

  const getCartCount = () => {
    return cart.reduce((sum, item) => sum + item.quantity, 0);
  };

  const addToCart = (item: MenuItem, restaurant: Restaurant) => {
    const existingRestaurantId = cart.length > 0 ? cart[0].restaurantId : null;

    if (existingRestaurantId && existingRestaurantId !== restaurant.id) {
      // Conflict: item from different restaurant
      setCartConflictRestaurant({ item, restaurant });
      return;
    }

    setCart((prev) => {
      const existingIdx = prev.findIndex((i) => i.menuItem.id === item.id);
      if (existingIdx !== -1) {
        return prev.map((item, idx) =>
          idx === existingIdx ? { ...item, quantity: item.quantity + 1 } : item
        );
      } else {
        return [
          ...prev,
          {
            menuItem: item,
            quantity: 1,
            restaurantId: restaurant.id,
            restaurantName: restaurant.nameAr
          }
        ];
      }
    });
  };

  const updateCartQuantity = (itemId: string, delta: number) => {
    setCart((prev) =>
      prev
        .map((item) => {
          if (item.menuItem.id === itemId) {
            const newQty = item.quantity + delta;
            return { ...item, quantity: newQty };
          }
          return item;
        })
        .filter((item) => item.quantity > 0)
    );
  };

  const resolveCartConflict = (confirm: boolean) => {
    if (confirm && cartConflictRestaurant) {
      // Clear cart first, then add new restaurant's item
      const { item, restaurant } = cartConflictRestaurant;
      setCart([
        {
          menuItem: item,
          quantity: 1,
          restaurantId: restaurant.id,
          restaurantName: restaurant.nameAr
        }
      ]);
    }
    setCartConflictRestaurant(null);
  };

  // Clock state
  const [time, setTime] = useState<string>('12:00');
  useEffect(() => {
    const updateTime = () => {
      const now = new Date();
      setTime(now.toLocaleTimeString('ar-YE', { hour: '2-digit', minute: '2-digit', hour12: false }));
    };
    updateTime();
    const interval = setInterval(updateTime, 15000);
    return () => clearInterval(interval);
  }, []);

  return (
    <div className="flex flex-col items-center justify-center p-4">
      {/* Phone Shell */}
      <div className="relative w-[375px] h-[780px] bg-slate-950 rounded-[50px] shadow-[0_25px_50px_-12px_rgba(0,0,0,0.7)] border-[12px] border-slate-800 flex flex-col overflow-hidden select-none">
        
        {/* Dynamic Island Camera Notch */}
        <div className="absolute top-2.5 left-1/2 transform -translate-x-1/2 w-32 h-6 bg-black rounded-full z-50 flex items-center justify-center">
          <div className="w-2.5 h-2.5 bg-slate-900 rounded-full ml-12"></div>
          <div className="w-1.5 h-1.5 bg-slate-950 rounded-full"></div>
        </div>

        {/* Status Bar */}
        <div className="h-11 px-6 bg-emerald-900 text-white flex items-center justify-between text-xs font-mono font-bold z-40">
          <span className="font-sans select-none">{time}</span>
          <div className="flex items-center gap-1.5 text-[10px]">
            <span>4G</span>
            <div className="w-4 h-2 border.5 border-white rounded-sm p-0.5 flex items-center">
              <div className="w-full h-full bg-white rounded-2xs"></div>
            </div>
            <span>🔋 94%</span>
          </div>
        </div>

        {/* Screen Area */}
        <div className="flex-1 bg-slate-50 flex flex-col overflow-hidden relative text-right" dir="rtl">
          
          {/* SCREEN: SPLASH */}
          {currentScreen === 'splash' && (
            <div className="absolute inset-0 bg-[#1A5C3A] flex flex-col items-center justify-center text-white z-50">
              <div className="flex flex-col items-center animate-pulse">
                {/* Custom Logo Glyph */}
                <div className="w-24 h-24 bg-white/10 rounded-3xl flex items-center justify-center mb-6 shadow-xl border border-white/20">
                  <span className="text-5xl font-black text-[#FFB800] font-sans">وصال</span>
                </div>
                <h1 className="text-4xl font-extrabold tracking-wide mb-2 font-sans text-center text-[#FFB800]">وصَـال</h1>
                <p className="text-emerald-100 text-xs font-medium tracking-wider mb-6">منصتك للتوصيل السريع في اليمن</p>
                <div className="w-8 h-8 border-3 border-emerald-200 border-t-[#FFB800] rounded-full animate-spin"></div>
              </div>
            </div>
          )}

          {/* SCREEN: LOGIN & OTP */}
          {currentScreen === 'login' && (
            <div className="flex-1 flex flex-col justify-between p-5 bg-[#F8F9FA] overflow-y-auto select-none">
              {!otpSent ? (
                /* LOGIN SCREEN */
                <div className="w-full flex flex-col items-center">
                  <div className="mt-4 mb-1">
                    <div className="w-20 h-20 bg-[#1A5C3A] rounded-2xl flex flex-col items-center justify-center shadow-lg border border-white/10 relative overflow-hidden">
                      <div className="absolute top-2 w-8 h-1 bg-[#FFB800] rounded-full"></div>
                      <span className="text-3xl font-black text-[#FFB800] font-sans mt-2">وصال</span>
                    </div>
                  </div>
                  <p className="text-slate-500 text-xs font-semibold mb-5">توصيل أسرع، حياة أسهل</p>

                  {/* Auth Card */}
                  <div className="w-full bg-white rounded-2xl p-5 shadow-sm border border-slate-100 mb-6">
                    <h3 className="text-lg font-black text-slate-800 text-center mb-1">تسجيل الدخول</h3>
                    <p className="text-[11px] text-slate-400 text-center mb-5">أهلاً بعودتك! اختر طريقة تسجيل الدخول</p>

                    {/* Google Outlined Button */}
                    <button
                      onClick={() => {
                        setCurrentUser({
                          name: 'أحمد اليماني',
                          phone: '777123456',
                          address: 'صنعاء، حي حدة، شارع بيروت'
                        });
                        navigate('home');
                      }}
                      className="w-full h-11 border border-slate-200 hover:bg-slate-50 active:bg-slate-100 rounded-xl flex items-center justify-center gap-2 mb-4 transition-all text-slate-700 font-bold text-xs"
                    >
                      <span>تسجيل الدخول بـ Google</span>
                      <svg className="w-4 h-4" viewBox="0 0 24 24">
                        <path fill="#EA4335" d="M12.24,5a6.85,6.85 0,0 1,4.85 1.9l3.6,-3.6A11.81,11.81 0,0 0,12.24 0,11.93 11.93 0,0 0,1.38 7.08l4.13,3.2A7.14,7.14 0,0 1,12.24 5z"/>
                        <path fill="#FBBC05" d="M1.38,7.08A11.94,11.94 0,0 0,1.38 16.92l4.13,-3.2a7.1,7.1 0,0 1,0,-6.64l-4.13,-3.08z"/>
                        <path fill="#34A853" d="M12.24,19a7.12,7.12 0,0 1,-6.73 -4.84l-4.13,3.2A11.94,11.94 0,0 0,12.24 24,11.75 11.75 0,0 0,20.48 17.08l-4.22,-3.2A6.87,6.87 0,0 1,12.24 19z"/>
                        <path fill="#4285F4" d="M24,12a11.53,11.53 0,0 0,-0.23 -2.25H12.24v4.51h6.6a5.64,5.64 0,0 1,-2.44 3.71l4.22,3.2A11.83,11.83 0,0 0,24 12z"/>
                      </svg>
                    </button>

                    {/* Tabs */}
                    <div className="w-full h-11 bg-slate-100 rounded-xl p-1 flex items-center mb-5">
                      <button
                        onClick={() => setLoginTab(0)}
                        className={`flex-1 h-full rounded-lg font-bold text-xs flex items-center justify-center gap-1.5 transition-all ${loginTab === 0 ? 'bg-[#1A5C3A] text-white shadow-sm' : 'text-slate-500'}`}
                      >
                        <Mail size={13} />
                        <span>البريد الإلكتروني</span>
                      </button>
                      <button
                        onClick={() => setLoginTab(1)}
                        className={`flex-1 h-full rounded-lg font-bold text-xs flex items-center justify-center gap-1.5 transition-all ${loginTab === 1 ? 'bg-[#1A5C3A] text-white shadow-sm' : 'text-slate-500'}`}
                      >
                        <Phone size={13} />
                        <span>رقم الهاتف</span>
                      </button>
                    </div>

                    {/* Email Tab view */}
                    {loginTab === 0 ? (
                      <div className="space-y-4 animate-fadeIn">
                        <div>
                          <label className="block text-xs font-bold text-slate-700 mb-1.5 text-right">البريد الإلكتروني</label>
                          <div className="flex gap-2 items-center border border-slate-200 rounded-xl px-3 py-2 bg-slate-50 focus-within:border-[#1A5C3A] focus-within:bg-white transition-all">
                            <Mail size={16} className="text-slate-400" />
                            <input
                              type="email"
                              placeholder="example@email.com"
                              value={loginEmail}
                              onChange={(e) => setLoginEmail(e.target.value)}
                              className="w-full bg-transparent text-slate-800 text-xs focus:outline-none text-left font-mono"
                            />
                          </div>
                        </div>

                        <div>
                          <label className="block text-xs font-bold text-slate-700 mb-1.5 text-right">كلمة السر</label>
                          <div className="flex gap-2 items-center border border-slate-200 rounded-xl px-3 py-2 bg-slate-50 focus-within:border-[#1A5C3A] focus-within:bg-white transition-all">
                            <Lock size={16} className="text-slate-400" />
                            <input
                              type={loginPasswordVisible ? 'text' : 'password'}
                              placeholder="••••••••"
                              value={loginPassword}
                              onChange={(e) => setLoginPassword(e.target.value)}
                              className="w-full bg-transparent text-slate-800 text-xs focus:outline-none"
                            />
                            <button onClick={() => setLoginPasswordVisible(!loginPasswordVisible)} className="text-slate-400 hover:text-slate-600 focus:outline-none">
                              {loginPasswordVisible ? <EyeOff size={16} /> : <Eye size={16} />}
                            </button>
                          </div>
                        </div>

                        <button
                          onClick={() => {
                            if (loginEmail && loginPassword) {
                              setCurrentUser({
                                name: 'أحمد اليماني',
                                phone: '777123456',
                                address: 'صنعاء، حي حدة، شارع بيروت'
                              });
                              navigate('home');
                            }
                          }}
                          disabled={!loginEmail || !loginPassword}
                          className="w-full h-11 bg-[#1A5C3A] hover:bg-[#124128] disabled:opacity-50 text-white font-bold rounded-xl text-xs transition shadow-md shadow-emerald-900/10 mt-2"
                        >
                          تسجيل الدخول
                        </button>
                        <p className="text-[10px] text-slate-400 text-center font-medium">يُستخدم هذا الخيار للحسابات التي تم إنشاؤها بالبريد الإلكتروني</p>
                      </div>
                    ) : (
                      /* Phone Tab view */
                      <div className="space-y-4 animate-fadeIn">
                        <div>
                          <label className="block text-xs font-bold text-slate-700 mb-1.5 text-right">رقم الهاتف</label>
                          <div className="flex gap-2 items-center">
                            <div className="flex-1 flex gap-2 items-center border border-slate-200 rounded-xl px-3 py-2 bg-slate-50 focus-within:border-[#1A5C3A] focus-within:bg-white transition-all">
                              <input
                                type="tel"
                                placeholder="7XX XXX XXX"
                                value={loginPhone}
                                onChange={(e) => setLoginPhone(e.target.value.replace(/\D/g, ''))}
                                className="w-full bg-transparent text-slate-800 text-xs focus:outline-none text-left font-mono font-bold tracking-wider"
                                maxLength={9}
                              />
                            </div>
                            <div className="w-16 h-10 bg-emerald-50 rounded-xl border border-[#1A5C3A] flex items-center justify-center font-bold text-xs text-[#1A5C3A] font-mono select-none">
                              +967
                            </div>
                          </div>
                          <p className="text-[10px] text-slate-400 text-right mt-1.5">أدخل رقمك بدون رمز البلد (+967)</p>
                        </div>

                        <button
                          onClick={() => {
                            if (loginPhone.length >= 9) {
                              setOtpPhone(loginPhone);
                              setOtpSent(true);
                              setOtpCountdown(60);
                            }
                          }}
                          disabled={loginPhone.length < 9}
                          className="w-full h-11 bg-[#1A5C3A] hover:bg-[#124128] disabled:bg-slate-200 disabled:text-slate-400 text-white font-bold rounded-xl text-xs transition mt-2 shadow-md"
                        >
                          إرسال رمز التحقق
                        </button>
                      </div>
                    )}

                    <div className="border-t border-slate-100 my-4"></div>

                    <button className="w-full text-center text-[10px] text-slate-400 hover:text-slate-600 font-bold py-1 mb-2">
                      لا تستطيع الوصول لحسابك؟
                    </button>

                    <div className="flex justify-center items-center gap-1.5 text-[11px]">
                      <span className="text-slate-400">ليس لديك حساب؟</span>
                      <button onClick={() => navigate('register')} className="text-[#1A5C3A] font-black underline hover:text-[#124128]">
                        إنشاء حساب جديد
                      </button>
                    </div>
                  </div>
                </div>
              ) : (
                /* SIMULATED OTP VERIFICATION SCREEN */
                <div className="w-full flex flex-col items-center animate-fadeIn">
                  <div className="mt-4 mb-1">
                    <div className="w-20 h-20 bg-[#1A5C3A] rounded-2xl flex flex-col items-center justify-center shadow-lg border border-white/10">
                      <span className="text-3xl font-black text-[#FFB800] font-sans">وصال</span>
                    </div>
                  </div>
                  <p className="text-slate-500 text-xs font-semibold mb-5">توصيل أسرع، حياة أسهل</p>

                  <div className="w-full bg-white rounded-2xl p-5 shadow-sm border border-slate-100 mb-6">
                    <h3 className="text-lg font-black text-slate-800 text-center mb-1">أدخل رمز التحقق</h3>
                    <p className="text-[11px] text-slate-400 text-center mb-6">
                      تم إرسال رمز إلى <span className="font-mono font-bold text-slate-600">+967 {otpPhone}</span>
                    </p>

                    {/* Simulated 6-digit box grid */}
                    <div className="relative w-full h-12 flex justify-center items-center gap-2 mb-8">
                      <input
                        type="text"
                        maxLength={6}
                        value={loginOTP}
                        onChange={(e) => setLoginOTP(e.target.value.replace(/\D/g, ''))}
                        className="absolute inset-0 opacity-0 w-full h-full cursor-pointer z-20 text-center font-mono font-bold"
                      />
                      {Array.from({ length: 6 }).map((_, index) => {
                        const val = loginOTP[index] || '';
                        const isCurrent = index === loginOTP.length;
                        return (
                          <div
                            key={index}
                            className={`w-9 h-11 rounded-lg border flex items-center justify-center font-mono font-black text-lg transition-all ${
                              isCurrent ? 'border-2 border-[#1A5C3A] bg-emerald-50/50' : 'border-slate-200 bg-slate-50'
                            } text-slate-800`}
                          >
                            {val}
                          </div>
                        );
                      })}
                    </div>

                    <button
                      onClick={() => {
                        if (loginOTP.length === 6) {
                          setCurrentUser({
                            name: 'أحمد اليماني',
                            phone: otpPhone || '777123456',
                            address: 'صنعاء، حي حدة، شارع بيروت'
                          });
                          navigate('home');
                        }
                      }}
                      disabled={loginOTP.length < 6}
                      className="w-full h-11 bg-[#1A5C3A] hover:bg-[#124128] disabled:bg-slate-200 disabled:text-slate-400 text-white font-bold rounded-xl text-xs transition shadow-md"
                    >
                      تحقق
                    </button>

                    <div className="flex flex-col items-center justify-center mt-6 text-xs gap-2">
                      {otpCountdown > 0 ? (
                        <span className="text-slate-400 text-[11px]">إعادة الإرسال بعد {otpCountdown} ثانية</span>
                      ) : (
                        <button
                          onClick={() => {
                            setOtpCountdown(60);
                            setLoginOTP('');
                          }}
                          className="text-[#1A5C3A] font-extrabold hover:underline"
                        >
                          إعادة إرسال الرمز
                        </button>
                      )}
                    </div>
                  </div>

                  {/* Back button */}
                  <button
                    onClick={() => {
                      setOtpSent(false);
                      setLoginOTP('');
                    }}
                    className="flex items-center gap-1.5 py-2 px-4 hover:bg-slate-100 rounded-xl text-slate-500 font-bold text-[11px] transition-all"
                  >
                    <ArrowRight size={14} />
                    <span>العودة للخطوة السابقة</span>
                  </button>
                </div>
              )}

              {/* Bottom link outside the card */}
              {!otpSent && (
                <button
                  onClick={() => navigate('home')}
                  className="mt-2 text-slate-400 hover:text-slate-600 font-bold text-xs flex items-center justify-center gap-1 py-1"
                >
                  <ArrowRight size={14} />
                  <span>العودة للصفحة الرئيسية كزائر</span>
                </button>
              )}
            </div>
          )}

          {/* SCREEN: REGISTER */}
          {currentScreen === 'register' && (
            <div className="flex-1 flex flex-col p-5 bg-[#F8F9FA] overflow-y-auto select-none">
              {/* Header Row */}
              <div className="flex items-center gap-2 mb-4">
                <button
                  onClick={goBack}
                  className="p-1.5 hover:bg-slate-200 active:bg-slate-300 rounded-full text-slate-700 transition-all"
                >
                  <ArrowRight size={18} />
                </button>
                <h2 className="text-sm font-black text-slate-800">إنشاء حساب جديد</h2>
              </div>

              {/* Logo */}
              <div className="flex flex-col items-center mb-3">
                <div className="w-16 h-16 bg-[#1A5C3A] rounded-xl flex items-center justify-center shadow-md">
                  <span className="text-2xl font-black text-[#FFB800]">وصال</span>
                </div>
                <p className="text-[10px] text-slate-400 font-bold mt-1">توصيل أسرع، حياة أسهل</p>
              </div>

              {/* Register Card */}
              <div className="w-full bg-white rounded-2xl p-5 shadow-sm border border-slate-100 mb-4">
                <h3 className="text-md font-black text-slate-800 mb-1">تسجيل مستخدم جديد</h3>
                <p className="text-[10px] text-slate-400 mb-4">أدخل بياناتك لإنشاء حسابك وتفعيل خدمات التوصيل</p>

                {/* Local validation error banner */}
                {formSubmitted && (!registerName || !registerEmail || !registerPhone || !registerPassword || !registerConfirmPassword) && (
                  <div className="bg-red-50 text-red-600 p-2.5 rounded-lg text-[10px] font-bold text-center mb-4 border border-red-100">
                    يرجى ملء جميع الخانات المطلوبة قبل المتابعة
                  </div>
                )}

                {formSubmitted && registerPassword && registerConfirmPassword && registerPassword !== registerConfirmPassword && (
                  <div className="bg-red-50 text-red-600 p-2.5 rounded-lg text-[10px] font-bold text-center mb-4 border border-red-100">
                    خطأ: كلمتا المرور غير متطابقتين!
                  </div>
                )}

                <div className="space-y-3.5">
                  {/* Name */}
                  <div>
                    <label className="block text-[10px] font-bold text-slate-700 mb-1">الاسم الكامل</label>
                    <div className="flex gap-2 items-center border border-slate-200 rounded-lg px-3 py-1.5 bg-slate-50 focus-within:border-[#1A5C3A] focus-within:bg-white transition-all">
                      <User size={14} className="text-slate-400" />
                      <input
                        type="text"
                        placeholder="أحمد علي اليماني"
                        value={registerName}
                        onChange={(e) => setRegisterName(e.target.value)}
                        className="w-full bg-transparent text-slate-800 text-xs focus:outline-none"
                      />
                    </div>
                  </div>

                  {/* Email */}
                  <div>
                    <label className="block text-[10px] font-bold text-slate-700 mb-1">البريد الإلكتروني</label>
                    <div className="flex gap-2 items-center border border-slate-200 rounded-lg px-3 py-1.5 bg-slate-50 focus-within:border-[#1A5C3A] focus-within:bg-white transition-all">
                      <Mail size={14} className="text-slate-400" />
                      <input
                        type="email"
                        placeholder="example@email.com"
                        value={registerEmail}
                        onChange={(e) => setRegisterEmail(e.target.value)}
                        className="w-full bg-transparent text-slate-800 text-xs focus:outline-none text-left font-mono"
                      />
                    </div>
                  </div>

                  {/* Phone */}
                  <div>
                    <label className="block text-[10px] font-bold text-slate-700 mb-1">رقم الهاتف المحمول</label>
                    <div className="flex gap-2 items-center">
                      <div className="flex-1 flex gap-2 items-center border border-slate-200 rounded-lg px-3 py-1.5 bg-slate-50 focus-within:border-[#1A5C3A] focus-within:bg-white transition-all">
                        <input
                          type="tel"
                          placeholder="7XX XXX XXX"
                          value={registerPhone}
                          onChange={(e) => setRegisterPhone(e.target.value.replace(/\D/g, ''))}
                          className="w-full bg-transparent text-slate-800 text-xs focus:outline-none text-left font-mono"
                          maxLength={9}
                        />
                      </div>
                      <div className="w-14 h-8 bg-emerald-50 rounded-lg border border-[#1A5C3A] flex items-center justify-center font-bold text-[10px] text-[#1A5C3A] font-mono select-none">
                        +967
                      </div>
                    </div>
                  </div>

                  {/* Password */}
                  <div>
                    <label className="block text-[10px] font-bold text-slate-700 mb-1">كلمة السر</label>
                    <div className="flex gap-2 items-center border border-slate-200 rounded-lg px-3 py-1.5 bg-slate-50 focus-within:border-[#1A5C3A] focus-within:bg-white transition-all">
                      <Lock size={14} className="text-slate-400" />
                      <input
                        type={registerPasswordVisible ? 'text' : 'password'}
                        placeholder="••••••••"
                        value={registerPassword}
                        onChange={(e) => setRegisterPassword(e.target.value)}
                        className="w-full bg-transparent text-slate-800 text-xs focus:outline-none"
                      />
                      <button onClick={() => setRegisterPasswordVisible(!registerPasswordVisible)} className="text-slate-400 hover:text-slate-600 focus:outline-none">
                        {registerPasswordVisible ? <EyeOff size={14} /> : <Eye size={14} />}
                      </button>
                    </div>
                  </div>

                  {/* Confirm Password */}
                  <div>
                    <label className="block text-[10px] font-bold text-slate-700 mb-1">تأكيد كلمة السر</label>
                    <div className="flex gap-2 items-center border border-slate-200 rounded-lg px-3 py-1.5 bg-slate-50 focus-within:border-[#1A5C3A] focus-within:bg-white transition-all">
                      <Lock size={14} className="text-slate-400" />
                      <input
                        type={registerConfirmPasswordVisible ? 'text' : 'password'}
                        placeholder="••••••••"
                        value={registerConfirmPassword}
                        onChange={(e) => setRegisterConfirmPassword(e.target.value)}
                        className="w-full bg-transparent text-slate-800 text-xs focus:outline-none"
                      />
                      <button onClick={() => setRegisterConfirmPasswordVisible(!registerConfirmPasswordVisible)} className="text-slate-400 hover:text-slate-600 focus:outline-none">
                        {registerConfirmPasswordVisible ? <EyeOff size={14} /> : <Eye size={14} />}
                      </button>
                    </div>
                  </div>
                </div>

                <button
                  onClick={() => {
                    setFormSubmitted(true);
                    if (
                      registerName &&
                      registerEmail &&
                      registerPhone.length >= 9 &&
                      registerPassword &&
                      registerConfirmPassword &&
                      registerPassword === registerConfirmPassword
                    ) {
                      setCurrentUser({
                        name: registerName,
                        phone: registerPhone,
                        address: 'اليمن، صنعاء، شارع الستين'
                      });
                      navigate('home');
                    }
                  }}
                  className="w-full h-11 bg-[#1A5C3A] hover:bg-[#124128] text-white font-bold rounded-xl text-xs transition shadow-md mt-6"
                >
                  إنشاء الحساب
                </button>
              </div>

              {/* Already have an account links */}
              <div className="flex justify-center items-center gap-1.5 text-xs text-slate-500 py-1">
                <span>لديك حساب بالفعل؟</span>
                <button onClick={() => navigate('login')} className="text-[#1A5C3A] font-extrabold underline hover:text-[#124128]">
                  تسجيل الدخول
                </button>
              </div>
            </div>
          )}

          {/* SCREEN: HOME */}
          {currentScreen === 'home' && (
            <div className="flex-1 flex flex-col bg-slate-50 overflow-y-auto pb-20">
              {/* Home Header */}
              <div className="bg-emerald-700 text-white px-5 py-4 flex items-center justify-between shadow-md">
                <div className="flex items-center gap-2">
                  <div className="p-1.5 bg-white/10 rounded-full"><MapPin size={16} /></div>
                  <div>
                    <span className="text-[10px] text-emerald-200 block">موقع التوصيل</span>
                    <span className="text-xs font-bold">{currentUser?.address || 'صنعاء، حي حدة'}</span>
                  </div>
                </div>
                <div className="flex items-center gap-2">
                  <button onClick={() => navigate('notifications')} className="p-2 bg-white/10 rounded-full relative">
                    <Bell size={16} />
                    <span className="absolute top-0.5 right-0.5 w-2 h-2 bg-amber-500 rounded-full"></span>
                  </button>
                </div>
              </div>

              {/* Package Delivery Quick Rail Banner */}
              <div className="m-4 p-4 bg-gradient-to-l from-emerald-800 to-emerald-900 rounded-2xl text-white shadow-md relative overflow-hidden">
                <div className="relative z-10">
                  <span className="px-2 py-0.5 bg-amber-500 text-emerald-950 font-bold rounded-full text-[9px] uppercase tracking-wide">جديد</span>
                  <h3 className="text-sm font-bold mt-1.5">وصال ديليفري للطرود 📦</h3>
                  <p className="text-[10px] text-emerald-100 mt-1 max-w-[200px] leading-relaxed">أرسل أو استلم طعام، هدايا، مستندات أو أي طرد داخل مدينتك بضغطة زر واحدة!</p>
                  <button
                    onClick={() => navigate('delivery_request')}
                    className="mt-3 px-3 py-1.5 bg-amber-500 hover:bg-amber-600 text-slate-900 font-bold rounded-lg text-[10px] transition-all"
                  >
                    اطلب مندوب طرود الآن
                  </button>
                </div>
                <div className="absolute left-[-20px] bottom-[-20px] opacity-20 text-white">
                  <Package size={140} />
                </div>
              </div>

              {/* Slider Promo Banners */}
              <div className="px-4 mb-4">
                <h4 className="text-xs font-bold text-slate-800 mb-2">عروض حصرية لك 🎁</h4>
                <div className="flex gap-3 overflow-x-auto pb-2 scrollbar-none snap-x">
                  {BANNERS.map((b) => (
                    <div key={b.id} className="min-w-[260px] h-32 bg-slate-100 rounded-xl overflow-hidden relative shadow-sm snap-start flex-shrink-0">
                      <img src={b.imageUrl} alt={b.title} className="w-full h-full object-cover" />
                      <div className="absolute inset-0 bg-gradient-to-t from-slate-950/80 to-transparent flex flex-col justify-end p-3 text-white">
                        {b.badgeText && <span className="absolute top-2 right-2 bg-amber-500 text-slate-950 font-black text-[9px] px-1.5 py-0.5 rounded-full">{b.badgeText}</span>}
                        <h5 className="text-xs font-bold">{b.title}</h5>
                        <p className="text-[9px] text-slate-200 mt-0.5">{b.subtitle}</p>
                      </div>
                    </div>
                  ))}
                </div>
              </div>

              {/* Categories Grid */}
              <div className="px-4 mb-4">
                <h4 className="text-xs font-bold text-slate-800 mb-2">ماذا تريد أن تطلب اليوم؟</h4>
                <div className="grid grid-cols-4 gap-3">
                  <button
                    onClick={() => navigate('restaurants')}
                    className="p-3 bg-white rounded-xl flex flex-col items-center justify-center border border-slate-150 shadow-xs hover:border-emerald-300 transition"
                  >
                    <span className="text-2xl mb-1">🍔</span>
                    <span className="text-[10px] font-bold text-slate-700">مطاعم</span>
                  </button>
                  <button
                    onClick={() => navigate('delivery_request')}
                    className="p-3 bg-white rounded-xl flex flex-col items-center justify-center border border-slate-150 shadow-xs hover:border-emerald-300 transition"
                  >
                    <span className="text-2xl mb-1">📦</span>
                    <span className="text-[10px] font-bold text-slate-700">توصيل طرود</span>
                  </button>
                  <button
                    onClick={() => { setSelectedCategory('cat_sweets'); navigate('restaurants'); }}
                    className="p-3 bg-white rounded-xl flex flex-col items-center justify-center border border-slate-150 shadow-xs hover:border-emerald-300 transition"
                  >
                    <span className="text-2xl mb-1">🍯</span>
                    <span className="text-[10px] font-bold text-slate-700">حلويات</span>
                  </button>
                  <button
                    onClick={() => { setSelectedCategory('cat_drinks'); navigate('restaurants'); }}
                    className="p-3 bg-white rounded-xl flex flex-col items-center justify-center border border-slate-150 shadow-xs hover:border-emerald-300 transition"
                  >
                    <span className="text-2xl mb-1">🍹</span>
                    <span className="text-[10px] font-bold text-slate-700">مشروبات</span>
                  </button>
                </div>
              </div>

              {/* Promoted Restaurants List */}
              <div className="px-4">
                <div className="flex items-center justify-between mb-2">
                  <h4 className="text-xs font-bold text-slate-800">مطاعم مميزة وصال ⭐️</h4>
                  <button onClick={() => navigate('restaurants')} className="text-[10px] font-bold text-emerald-700 hover:underline">عرض الكل</button>
                </div>

                <div className="space-y-3">
                  {RESTAURANTS.map((r) => (
                    <div
                      key={r.id}
                      onClick={() => { setSelectedRestaurant(r); navigate('restaurant_menu'); }}
                      className="bg-white rounded-xl border border-slate-150 p-3 flex gap-3 cursor-pointer hover:border-emerald-200 transition shadow-xs"
                    >
                      <div className="w-16 h-16 rounded-lg overflow-hidden flex-shrink-0 bg-slate-100">
                        <img src={r.logoUrl} alt={r.nameAr} className="w-full h-full object-cover" />
                      </div>
                      <div className="flex-1 min-w-0">
                        <h5 className="text-xs font-bold text-slate-800 truncate">{r.nameAr}</h5>
                        <p className="text-[10px] text-slate-400 mt-0.5 truncate">أطباق يمنية • بهارات ومندي بلدي</p>
                        
                        <div className="flex items-center gap-3 mt-2 text-[9px] text-slate-500">
                          <span className="flex items-center gap-1 font-bold text-amber-500">⭐ {r.rating}</span>
                          <span>🕒 {r.estimatedDeliveryTime} دقيقة</span>
                          <span>🚴 {r.deliveryFee} ر.ي</span>
                        </div>
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            </div>
          )}

          {/* SCREEN: RESTAURANTS LIST */}
          {currentScreen === 'restaurants' && (
            <div className="flex-1 flex flex-col bg-slate-50 overflow-y-auto pb-20">
              <div className="bg-emerald-700 text-white px-4 py-3 flex items-center justify-between sticky top-0 z-10">
                <div className="flex items-center gap-2">
                  <button onClick={goBack} className="p-1 hover:bg-white/10 rounded-full"><ChevronRight size={20} /></button>
                  <h3 className="text-sm font-bold">دليل المطاعم</h3>
                </div>
                <button onClick={() => navigate('cart')} className="relative p-2 bg-white/10 rounded-full">
                  <ShoppingBag size={15} />
                  {getCartCount() > 0 && (
                    <span className="absolute top-0.5 right-0.5 bg-amber-500 text-slate-900 font-extrabold text-[8px] w-4 h-4 rounded-full flex items-center justify-center">
                      {getCartCount()}
                    </span>
                  )}
                </button>
              </div>

              {/* Menu Categories Tabs */}
              <div className="flex gap-2 p-3 bg-white border-b border-slate-100 overflow-x-auto scrollbar-none sticky top-[44px] z-10">
                {CATEGORIES.map((cat) => {
                  const isSelected = selectedCategory === cat.id;
                  return (
                    <button
                      key={cat.id}
                      onClick={() => setSelectedCategory(cat.id)}
                      className={`px-3 py-1.5 rounded-full text-[10px] font-bold flex items-center gap-1 transition-all flex-shrink-0 ${
                        isSelected
                          ? 'bg-emerald-700 text-white'
                          : 'bg-slate-100 text-slate-600 hover:bg-slate-200'
                      }`}
                    >
                      <span>{cat.image}</span>
                      <span>{cat.nameAr}</span>
                    </button>
                  );
                })}
              </div>

              {/* Restaurants list filtering */}
              <div className="p-4 space-y-3">
                {RESTAURANTS.filter(r => selectedCategory === 'cat_all' || r.category === selectedCategory).map((r) => (
                  <div
                    key={r.id}
                    onClick={() => { setSelectedRestaurant(r); navigate('restaurant_menu'); }}
                    className="bg-white rounded-xl border border-slate-200 overflow-hidden cursor-pointer hover:border-emerald-300 transition shadow-xs flex flex-col"
                  >
                    <div className="h-28 bg-slate-200 relative">
                      <img src={r.coverImage} alt={r.nameAr} className="w-full h-full object-cover" />
                      <div className="absolute top-2 left-2 bg-white/90 backdrop-blur-xs text-emerald-800 font-bold text-[9px] px-2 py-0.5 rounded-full">
                        توصيل سريع
                      </div>
                    </div>
                    
                    <div className="p-3 flex gap-3">
                      <div className="w-12 h-12 rounded-lg overflow-hidden border border-slate-100 bg-slate-50 flex-shrink-0 mt-[-20px] relative z-10 shadow-sm">
                        <img src={r.logoUrl} alt={r.nameAr} className="w-full h-full object-cover" />
                      </div>

                      <div className="flex-1 min-w-0">
                        <h4 className="text-xs font-bold text-slate-800 truncate">{r.nameAr}</h4>
                        <div className="flex items-center gap-2 mt-1.5 text-[9px] text-slate-500">
                          <span className="text-amber-500 font-bold">⭐ {r.rating}</span>
                          <span>•</span>
                          <span>🕒 {r.estimatedDeliveryTime} دقيقة</span>
                          <span>•</span>
                          <span>🚴 {r.deliveryFee} ر.ي</span>
                        </div>
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          )}

          {/* SCREEN: RESTAURANT MENU */}
          {currentScreen === 'restaurant_menu' && selectedRestaurant && (
            <div className="flex-1 flex flex-col bg-slate-50 overflow-y-auto pb-20">
              {/* Back Button Overlay */}
              <div className="h-40 relative">
                <img src={selectedRestaurant.coverImage} alt={selectedRestaurant.nameAr} className="w-full h-full object-cover" />
                <div className="absolute inset-0 bg-slate-950/30"></div>
                
                <div className="absolute top-3 right-3 left-3 flex justify-between items-center">
                  <button onClick={goBack} className="p-1.5 bg-black/40 text-white rounded-full hover:bg-black/60 transition">
                    <ChevronRight size={20} />
                  </button>
                  <button onClick={() => navigate('cart')} className="p-1.5 bg-black/40 text-white rounded-full hover:bg-black/60 relative transition">
                    <ShoppingBag size={18} />
                    {getCartCount() > 0 && (
                      <span className="absolute top-0 right-0 bg-amber-500 text-slate-950 font-bold text-[8px] w-4 h-4 rounded-full flex items-center justify-center">
                        {getCartCount()}
                      </span>
                    )}
                  </button>
                </div>

                <div className="absolute bottom-[-15px] right-4 flex gap-3 items-end">
                  <div className="w-16 h-16 rounded-xl overflow-hidden border-2 border-white bg-white shadow-md">
                    <img src={selectedRestaurant.logoUrl} alt={selectedRestaurant.nameAr} className="w-full h-full object-cover" />
                  </div>
                  <div className="text-white pb-4">
                    <h3 className="text-sm font-extrabold text-white truncate drop-shadow-md">{selectedRestaurant.nameAr}</h3>
                    <div className="flex items-center gap-1.5 text-[9px] text-slate-200 mt-1 drop-shadow-xs">
                      <span>⭐ {selectedRestaurant.rating}</span>
                      <span>•</span>
                      <span>التوصيل: {selectedRestaurant.deliveryFee} ر.ي</span>
                    </div>
                  </div>
                </div>
              </div>

              {/* Items List */}
              <div className="p-4 mt-6 space-y-4">
                <h4 className="text-xs font-bold text-slate-700 border-r-3 border-emerald-600 pr-2 mb-3">الوجبات الأكثر مبيعاً 🔥</h4>
                
                {MENU_ITEMS.filter((item) => item.restaurantId === selectedRestaurant.id).map((item) => (
                  <div key={item.id} className="bg-white rounded-xl border border-slate-150 p-3 flex gap-3">
                    <div className="w-16 h-16 rounded-lg overflow-hidden bg-slate-100 flex-shrink-0">
                      <img src={item.imageUrl} alt={item.nameAr} className="w-full h-full object-cover" />
                    </div>

                    <div className="flex-1 min-w-0 flex flex-col justify-between">
                      <div>
                        <h5 className="text-[11px] font-bold text-slate-800 line-clamp-1">{item.nameAr}</h5>
                        <p className="text-[9px] text-slate-400 line-clamp-2 mt-0.5 leading-relaxed">{item.description}</p>
                      </div>

                      <div className="flex items-center justify-between mt-2">
                        <div className="flex items-baseline gap-1.5">
                          <span className="text-xs font-bold text-emerald-800">{item.price} ر.ي</span>
                          {item.originalPrice && (
                            <span className="text-[9px] text-slate-400 line-through">{item.originalPrice} ر.ي</span>
                          )}
                        </div>

                        {/* Cart count control or add button */}
                        {cart.some((c) => c.menuItem.id === item.id) ? (
                          <div className="flex items-center gap-2 bg-emerald-50 rounded-lg p-1 border border-emerald-100">
                            <button
                              onClick={() => updateCartQuantity(item.id, 1)}
                              className="w-5 h-5 bg-emerald-700 hover:bg-emerald-800 text-white rounded-md flex items-center justify-center"
                            >
                              <Plus size={11} />
                            </button>
                            <span className="text-xs font-bold text-emerald-950 px-1">
                              {cart.find((c) => c.menuItem.id === item.id)?.quantity}
                            </span>
                            <button
                              onClick={() => updateCartQuantity(item.id, -1)}
                              className="w-5 h-5 bg-slate-200 hover:bg-slate-300 text-slate-700 rounded-md flex items-center justify-center"
                            >
                              <Minus size={11} />
                            </button>
                          </div>
                        ) : (
                          <button
                            onClick={() => addToCart(item, selectedRestaurant)}
                            className="px-2.5 py-1 bg-emerald-700 hover:bg-emerald-800 text-white text-[10px] font-bold rounded-lg transition"
                          >
                            أضف للسلة
                          </button>
                        )}
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          )}

          {/* SCREEN: CART */}
          {currentScreen === 'cart' && (
            <div className="flex-1 flex flex-col bg-slate-50 overflow-y-auto pb-20">
              <div className="bg-emerald-700 text-white px-4 py-3 flex items-center justify-between">
                <div className="flex items-center gap-2">
                  <button onClick={goBack} className="p-1 hover:bg-white/10 rounded-full"><ChevronRight size={20} /></button>
                  <h3 className="text-sm font-bold">سلة المشتريات</h3>
                </div>
                {cart.length > 0 && (
                  <button onClick={() => setCart([])} className="text-emerald-100 text-[10px] font-bold hover:underline">
                    إفراغ السلة
                  </button>
                )}
              </div>

              {cart.length === 0 ? (
                <div className="flex-1 flex flex-col items-center justify-center p-8">
                  <div className="p-4 bg-slate-100 rounded-full text-slate-400 mb-4"><ShoppingBag size={48} /></div>
                  <h4 className="text-sm font-bold text-slate-800">سلتك فارغة حالياً</h4>
                  <p className="text-xs text-slate-400 text-center mt-1">تصفح المطاعم المميزة وأضف أفضل الأطباق اليمنية إلى سلتك!</p>
                  <button
                    onClick={() => navigate('restaurants')}
                    className="mt-6 px-6 py-2.5 bg-emerald-700 hover:bg-emerald-800 text-white font-bold rounded-xl text-xs transition"
                  >
                    تصفح المطاعم
                  </button>
                </div>
              ) : (
                <div className="p-4 flex-1 flex flex-col justify-between">
                  <div className="space-y-4">
                    <div className="p-3 bg-emerald-50 rounded-xl text-emerald-900 text-xs font-bold border border-emerald-100">
                      طلب من: {cart[0].restaurantName}
                    </div>

                    <div className="space-y-3">
                      {cart.map((item) => (
                        <div key={item.menuItem.id} className="bg-white rounded-xl border border-slate-150 p-3 flex gap-3 items-center justify-between">
                          <div className="flex-1 min-w-0">
                            <h5 className="text-[11px] font-bold text-slate-800 truncate">{item.menuItem.nameAr}</h5>
                            <span className="text-xs font-bold text-emerald-800 block mt-1">{item.menuItem.price * item.quantity} ر.ي</span>
                          </div>

                          <div className="flex items-center gap-2 bg-slate-50 p-1 rounded-lg border border-slate-100 flex-shrink-0">
                            <button
                              onClick={() => updateCartQuantity(item.menuItem.id, 1)}
                              className="w-6 h-6 bg-emerald-700 text-white rounded-md flex items-center justify-center hover:bg-emerald-800 transition"
                            >
                              <Plus size={12} />
                            </button>
                            <span className="text-xs font-bold text-slate-800 px-1">{item.quantity}</span>
                            <button
                              onClick={() => updateCartQuantity(item.menuItem.id, -1)}
                              className="w-6 h-6 bg-slate-200 text-slate-700 rounded-md flex items-center justify-center hover:bg-slate-300 transition"
                            >
                              <Minus size={12} />
                            </button>
                          </div>
                        </div>
                      ))}
                    </div>
                  </div>

                  <div className="bg-white border border-slate-150 rounded-2xl p-4 mt-8 space-y-3">
                    <div className="flex justify-between text-xs text-slate-500">
                      <span>إجمالي الوجبات:</span>
                      <span className="font-bold">{getCartTotal().itemsTotal} ر.ي</span>
                    </div>
                    <div className="flex justify-between text-xs text-slate-500">
                      <span>رسوم التوصيل:</span>
                      <span className="font-bold">{getCartTotal().deliveryFee} ر.ي</span>
                    </div>
                    <div className="border-t border-dashed border-slate-150 pt-3 flex justify-between text-sm font-extrabold text-slate-800">
                      <span>الإجمالي الكلي:</span>
                      <span className="text-emerald-800 font-black">{getCartTotal().grandTotal} ر.ي</span>
                    </div>

                    <button
                      onClick={() => navigate('checkout')}
                      className="w-full py-3 mt-4 bg-emerald-700 hover:bg-emerald-800 text-white font-bold rounded-xl text-xs transition shadow-md shadow-emerald-700/15"
                    >
                      المتابعة لإتمام الطلب
                    </button>
                  </div>
                </div>
              )}
            </div>
          )}

          {/* SCREEN: CHECKOUT */}
          {currentScreen === 'checkout' && (
            <div className="flex-1 flex flex-col bg-slate-50 overflow-y-auto pb-20">
              <div className="bg-emerald-700 text-white px-4 py-3 flex items-center justify-between">
                <div className="flex items-center gap-2">
                  <button onClick={goBack} className="p-1 hover:bg-white/10 rounded-full"><ChevronRight size={20} /></button>
                  <h3 className="text-sm font-bold">تأكيد وإرسال الطلب</h3>
                </div>
              </div>

              <div className="p-4 space-y-4">
                {/* Location summary */}
                <div className="bg-white rounded-xl border border-slate-200 p-3.5 space-y-2">
                  <div className="flex items-center gap-2 text-slate-700 font-bold text-xs border-b border-slate-100 pb-2">
                    <MapPin size={15} className="text-emerald-700" />
                    <span>عنوان التوصيل</span>
                  </div>
                  <p className="text-xs text-slate-600 font-semibold">{currentUser?.address}</p>
                </div>

                {/* Payment option */}
                <div className="bg-white rounded-xl border border-slate-200 p-3.5 space-y-2">
                  <div className="flex items-center gap-2 text-slate-700 font-bold text-xs border-b border-slate-100 pb-2">
                    <CreditCard size={15} className="text-emerald-700" />
                    <span>طريقة الدفع</span>
                  </div>
                  
                  <div className="flex items-center justify-between p-2.5 bg-emerald-50 rounded-xl border border-emerald-100">
                    <div className="flex items-center gap-2">
                      <div className="p-1.5 bg-emerald-700 text-white rounded-full"><Check size={12} /></div>
                      <div>
                        <span className="text-xs font-bold text-emerald-950 block">الدفع نقداً عند الاستلام</span>
                        <span className="text-[10px] text-emerald-600">الدفع كاش للمندوب فور وصول وجبتك</span>
                      </div>
                    </div>
                  </div>
                </div>

                {/* Order Invoice */}
                <div className="bg-white rounded-xl border border-slate-200 p-3.5 space-y-3">
                  <span className="text-xs font-bold text-slate-700 block">تفاصيل الفاتورة</span>
                  
                  {cart.map((item) => (
                    <div key={item.menuItem.id} className="flex justify-between text-[11px] text-slate-500">
                      <span>{item.menuItem.nameAr} (×{item.quantity})</span>
                      <span className="font-semibold">{item.menuItem.price * item.quantity} ر.ي</span>
                    </div>
                  ))}

                  <div className="border-t border-dashed border-slate-150 pt-2.5 flex justify-between text-xs text-slate-800 font-bold">
                    <span>رسوم التوصيل:</span>
                    <span>{getCartTotal().deliveryFee} ر.ي</span>
                  </div>

                  <div className="border-t border-slate-150 pt-2.5 flex justify-between text-sm font-extrabold text-emerald-800">
                    <span>الإجمالي الكلي:</span>
                    <span>{getCartTotal().grandTotal} ر.ي</span>
                  </div>
                </div>

                {/* Bottom Order Button */}
                <button
                  onClick={() => {
                    const newOrderId = `WS-${Math.floor(1000 + Math.random() * 9000)}`;
                    const placedOrder: Order = {
                      id: newOrderId,
                      restaurantName: cart[0]?.restaurantName || 'مطعم يمني شريك',
                      items: cart.map((i) => ({ name: i.menuItem.nameAr, quantity: i.quantity, price: i.menuItem.price })),
                      totalPrice: getCartTotal().grandTotal,
                      date: new Date().toISOString().split('T')[0],
                      status: 'pending'
                    };
                    setActiveOrder(placedOrder);
                    setCart([]); // Empty the cart
                    navigate('order_tracking');
                  }}
                  className="w-full py-3.5 bg-[#FFB800] hover:bg-amber-500 text-slate-950 font-black rounded-xl text-xs transition shadow-md shadow-amber-500/10 text-center"
                >
                  إرسال الطلب للمطعم
                </button>
              </div>
            </div>
          )}

          {/* SCREEN: ORDER TRACKING */}
          {currentScreen === 'order_tracking' && activeOrder && (
            <div className="flex-1 flex flex-col bg-slate-50 overflow-y-auto pb-20">
              <div className="bg-emerald-700 text-white px-4 py-3 flex items-center justify-between">
                <div className="flex items-center gap-2">
                  <button onClick={() => navigate('home')} className="p-1 hover:bg-white/10 rounded-full"><ChevronRight size={20} /></button>
                  <h3 className="text-sm font-bold">تتبع طلبك مباشر</h3>
                </div>
                <span className="bg-white/20 px-2 py-0.5 rounded-full text-[10px] font-mono font-bold">{activeOrder.id}</span>
              </div>

              {/* Dynamic Map Simulation Visualizer */}
              <div className="h-44 bg-slate-200 border-b border-slate-300 relative overflow-hidden flex items-center justify-center">
                {/* Stylized Grid Map */}
                <div className="absolute inset-0 opacity-25 bg-[radial-gradient(#1A5C3A_1px,transparent_1px)] [background-size:16px_16px]"></div>
                
                {/* Moving Scooter Animator */}
                <div className="relative flex flex-col items-center">
                  <div className="p-3 bg-emerald-700 text-white rounded-full shadow-lg border border-emerald-500 relative z-10 animate-bounce">
                    <Bike size={24} />
                  </div>
                  <div className="w-8 h-1.5 bg-slate-950/20 rounded-full blur-xs mt-1 animate-pulse"></div>
                  
                  <span className="text-[10px] font-bold text-slate-700 mt-2 bg-white/90 px-3 py-1 rounded-full border border-slate-200">
                    {activeOrder.status === 'pending' && 'جاري مراجعة طلبك...'}
                    {activeOrder.status === 'preparing' && 'المطعم يقوم بتحضير طعامك الآن'}
                    {activeOrder.status === 'picked_up' && 'المندوب يستلم طلبك من المطعم'}
                    {activeOrder.status === 'on_the_way' && 'المندوب في طريقه إليك!'}
                    {activeOrder.status === 'delivered' && 'تم تسليم وجبتك بالهناء والشفاء!'}
                  </span>
                </div>
              </div>

              {/* Delivery Stepper Status */}
              <div className="p-5 bg-white m-4 rounded-2xl border border-slate-200 shadow-xs space-y-4">
                <h4 className="text-xs font-bold text-slate-800 mb-2 border-b border-slate-100 pb-2">مراحل الطلب</h4>
                
                <div className="space-y-4">
                  {[
                    { key: 'pending', label: 'تم استقبال الطلب وصال', icon: <Check size={12} /> },
                    { key: 'preparing', label: 'المطعم يقوم بالتحضير', icon: <Package size={12} /> },
                    { key: 'picked_up', label: 'تم تسليم الطلب للمندوب', icon: <Bike size={12} /> },
                    { key: 'on_the_way', label: 'المندوب في الطريق إليك', icon: <MapPin size={12} /> }
                  ].map((step, idx) => {
                    const statuses = ['pending', 'preparing', 'picked_up', 'on_the_way', 'delivered'];
                    const activeIdx = statuses.indexOf(activeOrder.status);
                    const stepIdx = statuses.indexOf(step.key);
                    const isCompleted = activeIdx >= stepIdx;

                    return (
                      <div key={step.key} className="flex items-center gap-3.5">
                        <div className={`w-6 h-6 rounded-full flex items-center justify-center text-xs font-bold transition-all ${
                          isCompleted ? 'bg-emerald-700 text-white shadow-sm' : 'bg-slate-100 text-slate-400'
                        }`}>
                          {step.icon}
                        </div>
                        <span className={`text-[11px] font-bold ${
                          isCompleted ? 'text-slate-800' : 'text-slate-400'
                        }`}>{step.label}</span>
                      </div>
                    );
                  })}
                </div>
              </div>

              {/* Delivery Agent info */}
              <div className="mx-4 p-4 bg-emerald-50 border border-emerald-100 rounded-2xl flex items-center justify-between">
                <div className="flex items-center gap-3">
                  <div className="w-10 h-10 rounded-full bg-emerald-700 text-white flex items-center justify-center font-bold text-sm">م</div>
                  <div>
                    <span className="text-[10px] text-emerald-600 block">مندوب التوصيل وصال</span>
                    <span className="text-xs font-bold text-slate-800">مختار الصنعاني</span>
                  </div>
                </div>
                
                <div className="flex gap-2">
                  <button className="p-2 bg-emerald-600 hover:bg-emerald-700 text-white rounded-full shadow-xs"><Phone size={14} /></button>
                  <button className="p-2 bg-emerald-600 hover:bg-emerald-700 text-white rounded-full shadow-xs"><MessageSquare size={14} /></button>
                </div>
              </div>
            </div>
          )}

          {/* SCREEN: ORDERS HISTORY */}
          {currentScreen === 'orders_history' && (
            <div className="flex-1 flex flex-col bg-slate-50 overflow-y-auto pb-20">
              <div className="bg-emerald-700 text-white px-4 py-3 flex items-center justify-between">
                <div className="flex items-center gap-2">
                  <button onClick={goBack} className="p-1 hover:bg-white/10 rounded-full"><ChevronRight size={20} /></button>
                  <h3 className="text-sm font-bold">سجل طلباتي</h3>
                </div>
              </div>

              <div className="p-4 space-y-3.5">
                {ordersHistory.map((order) => (
                  <div key={order.id} className="bg-white border border-slate-200 rounded-2xl p-4 shadow-2xs space-y-3">
                    <div className="flex justify-between items-center border-b border-slate-100 pb-2.5">
                      <div>
                        <h4 className="text-xs font-bold text-slate-800">{order.restaurantName}</h4>
                        <span className="text-[10px] text-slate-400 font-mono mt-0.5 block">{order.date} • {order.id}</span>
                      </div>
                      <span className={`text-[9px] font-bold px-2 py-0.5 rounded-full ${
                        order.status === 'delivered' ? 'bg-emerald-50 text-emerald-800 border border-emerald-100' : 'bg-amber-50 text-amber-800 border border-amber-100'
                      }`}>
                        {order.status === 'delivered' ? 'تم التوصيل' : 'جاري التوصيل'}
                      </span>
                    </div>

                    <div className="space-y-1">
                      {order.items.map((i, idx) => (
                        <div key={idx} className="flex justify-between text-[11px] text-slate-500">
                          <span>{i.name} (×{i.quantity})</span>
                          <span>{i.price * i.quantity} ر.ي</span>
                        </div>
                      ))}
                    </div>

                    <div className="border-t border-slate-100 pt-2.5 flex justify-between items-center">
                      <span className="text-xs font-bold text-slate-800">المجموع الكلي:</span>
                      <span className="text-xs font-extrabold text-emerald-800">{order.totalPrice} ر.ي</span>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          )}

          {/* SCREEN: DELIVERY REQUEST (PACKAGE COURIER) */}
          {currentScreen === 'delivery_request' && (
            <div className="flex-1 flex flex-col bg-slate-50 overflow-y-auto pb-20">
              <div className="bg-emerald-700 text-white px-4 py-3 flex items-center justify-between">
                <div className="flex items-center gap-2">
                  <button onClick={goBack} className="p-1 hover:bg-white/10 rounded-full"><ChevronRight size={20} /></button>
                  <h3 className="text-sm font-bold">وصال ديليفري للطرود</h3>
                </div>
              </div>

              <div className="p-4 space-y-4">
                <div className="bg-white rounded-xl border border-slate-200 p-4 space-y-3">
                  <span className="text-xs font-bold text-slate-700 block border-r-3 border-emerald-600 pr-2">توصيل أي شيء في دقائق 🛵</span>
                  <p className="text-[10px] text-slate-500 leading-relaxed">
                    من خلال هذه الخدمة يمكنك إرسال أو استلام أي طرد (هدايا، مفاتيح، أوراق، مستندات، أطعمة، أجهزة) من أي مكان داخل مدينتك.
                  </p>
                </div>

                <div className="bg-white rounded-xl border border-slate-200 p-4 space-y-3.5">
                  <div>
                    <label className="block text-xs font-bold text-slate-600 mb-1">نقطة الاستلام (من أين؟)</label>
                    <input
                      type="text"
                      value={packageDelivery.pickup}
                      onChange={(e) => setPackageDelivery({ ...packageDelivery, pickup: e.target.value })}
                      className="w-full border border-slate-200 rounded-xl px-3 py-2 text-xs text-slate-800 focus:outline-none focus:border-emerald-500 bg-slate-50"
                    />
                  </div>

                  <div>
                    <label className="block text-xs font-bold text-slate-600 mb-1">نقطة التسليم (إلى أين؟)</label>
                    <input
                      type="text"
                      value={packageDelivery.dropoff}
                      onChange={(e) => setPackageDelivery({ ...packageDelivery, dropoff: e.target.value })}
                      className="w-full border border-slate-200 rounded-xl px-3 py-2 text-xs text-slate-800 focus:outline-none focus:border-emerald-500 bg-slate-50"
                    />
                  </div>

                  <div>
                    <label className="block text-xs font-bold text-slate-600 mb-1">نوع الطرد</label>
                    <input
                      type="text"
                      value={packageDelivery.packageType}
                      onChange={(e) => setPackageDelivery({ ...packageDelivery, packageType: e.target.value })}
                      className="w-full border border-slate-200 rounded-xl px-3 py-2 text-xs text-slate-800 focus:outline-none focus:border-emerald-500 bg-slate-50"
                    />
                  </div>

                  <div className="flex gap-4">
                    <div className="flex-1">
                      <label className="block text-xs font-bold text-slate-600 mb-1">الوزن التقريبي</label>
                      <select
                        value={packageDelivery.weight}
                        onChange={(e) => setPackageDelivery({ ...packageDelivery, weight: e.target.value })}
                        className="w-full border border-slate-200 rounded-xl px-3 py-2 text-xs text-slate-800 focus:outline-none focus:border-emerald-500 bg-slate-50"
                      >
                        <option>أقل من ١ كجم</option>
                        <option>من ١ إلى ٥ كجم</option>
                        <option>من ٥ إلى ١٠ كجم</option>
                      </select>
                    </div>
                  </div>
                </div>

                {/* Estimation */}
                <div className="bg-emerald-50 border border-emerald-100 rounded-xl p-3 flex justify-between items-center text-xs text-emerald-950 font-bold">
                  <span>التكلفة التقريبية للتوصيل:</span>
                  <span className="text-emerald-800 font-extrabold text-sm">1,200 ر.ي</span>
                </div>

                <button
                  onClick={() => {
                    // Create tracking order for package
                    const placedOrder: Order = {
                      id: `WS-PKG-${Math.floor(1000 + Math.random() * 9000)}`,
                      restaurantName: `توصيل طرد: ${packageDelivery.packageType}`,
                      items: [
                        { name: `من: ${packageDelivery.pickup}`, quantity: 1, price: 1200 },
                        { name: `إلى: ${packageDelivery.dropoff}`, quantity: 1, price: 0 }
                      ],
                      totalPrice: 1200,
                      date: new Date().toISOString().split('T')[0],
                      status: 'pending'
                    };
                    setActiveOrder(placedOrder);
                    navigate('order_tracking');
                  }}
                  className="w-full py-3.5 bg-[#FFB800] hover:bg-amber-500 text-slate-950 font-black rounded-xl text-xs transition shadow-md shadow-amber-500/10 text-center"
                >
                  تأكيد وطلب مندوب وصال
                </button>
              </div>
            </div>
          )}

          {/* SCREEN: PROFILE */}
          {currentScreen === 'profile' && (
            <div className="flex-1 flex flex-col bg-slate-50 overflow-y-auto pb-20">
              <div className="bg-emerald-700 text-white px-4 py-3 flex items-center justify-between">
                <div className="flex items-center gap-2">
                  <button onClick={goBack} className="p-1 hover:bg-white/10 rounded-full"><ChevronRight size={20} /></button>
                  <h3 className="text-sm font-bold">الملف الشخصي</h3>
                </div>
              </div>

              <div className="p-4 space-y-4">
                {/* Profile Card */}
                <div className="bg-white rounded-2xl border border-slate-200 p-4 flex gap-4 items-center shadow-xs">
                  <div className="w-14 h-14 bg-emerald-700 text-white rounded-full flex items-center justify-center font-bold text-lg shadow-sm">
                    {currentUser?.name ? currentUser.name[0] : 'أ'}
                  </div>
                  <div>
                    <h4 className="text-sm font-bold text-slate-800">{currentUser?.name}</h4>
                    <span className="text-xs text-slate-400 font-mono mt-1 block">967{currentUser?.phone}+</span>
                  </div>
                </div>

                {/* Wasal Wallet Card */}
                <div className="bg-gradient-to-l from-emerald-800 to-emerald-900 rounded-2xl p-4 text-white shadow-md">
                  <span className="text-[10px] text-emerald-200 block uppercase tracking-wide">رصيد محفظة وصال 💳</span>
                  <div className="flex justify-between items-baseline mt-1.5">
                    <span className="text-2xl font-black text-amber-400">14,500 ر.ي</span>
                    <button className="px-3 py-1 bg-white/20 hover:bg-white/30 text-white text-[10px] font-bold rounded-lg transition">شحن المحفظة</button>
                  </div>
                </div>

                {/* Navigation links */}
                <div className="bg-white rounded-2xl border border-slate-200 overflow-hidden divide-y divide-slate-100 shadow-xs">
                  <button
                    onClick={() => navigate('orders_history')}
                    className="w-full p-3.5 hover:bg-slate-50 transition flex justify-between items-center text-slate-700 font-semibold text-xs"
                  >
                    <div className="flex items-center gap-2.5">
                      <History size={16} className="text-slate-400" />
                      <span>سجل طلباتي</span>
                    </div>
                    <ChevronLeft size={16} className="text-slate-400" />
                  </button>

                  <button
                    onClick={() => navigate('notifications')}
                    className="w-full p-3.5 hover:bg-slate-50 transition flex justify-between items-center text-slate-700 font-semibold text-xs"
                  >
                    <div className="flex items-center gap-2.5">
                      <Bell size={16} className="text-slate-400" />
                      <span>الإشعارات والتنبيهات</span>
                    </div>
                    <ChevronLeft size={16} className="text-slate-400" />
                  </button>

                  <button
                    onClick={() => {
                      setCurrentUser(null);
                      setOtpSent(false);
                      setLoginPhone('');
                      setLoginOTP('');
                      navigate('login');
                    }}
                    className="w-full p-3.5 hover:bg-red-50 text-red-600 transition flex justify-between items-center font-bold text-xs"
                  >
                    <div className="flex items-center gap-2.5">
                      <X size={16} />
                      <span>تسجيل الخروج</span>
                    </div>
                  </button>
                </div>
              </div>
            </div>
          )}

          {/* SCREEN: NOTIFICATIONS */}
          {currentScreen === 'notifications' && (
            <div className="flex-1 flex flex-col bg-slate-50 overflow-y-auto pb-20">
              <div className="bg-emerald-700 text-white px-4 py-3 flex items-center justify-between">
                <div className="flex items-center gap-2">
                  <button onClick={goBack} className="p-1 hover:bg-white/10 rounded-full"><ChevronRight size={20} /></button>
                  <h3 className="text-sm font-bold">الإشعارات</h3>
                </div>
              </div>

              <div className="p-4 space-y-3">
                {notifications.map((note, idx) => (
                  <div key={idx} className="bg-white border border-slate-150 rounded-xl p-3 flex gap-3 items-start shadow-xs animate-fadeIn">
                    <div className="p-1.5 bg-emerald-50 text-emerald-700 rounded-full flex-shrink-0 mt-0.5">
                      <Bell size={13} />
                    </div>
                    <div className="flex-1">
                      <p className="text-xs text-slate-700 leading-relaxed font-medium">{note}</p>
                      <span className="text-[9px] text-slate-400 block mt-1 font-mono">الآن</span>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          )}

          {/* Shared Bottom Navigation (Home, Restaurants, OrdersHistory, Profile) */}
          {['home', 'restaurants', 'orders_history', 'profile'].includes(currentScreen) && (
            <div className="absolute bottom-0 left-0 right-0 h-16 bg-white border-t border-slate-200 flex items-center justify-around z-30 shadow-lg px-4">
              <button
                onClick={() => navigate('home')}
                className={`flex flex-col items-center justify-center gap-1 ${
                  currentScreen === 'home' ? 'text-emerald-700' : 'text-slate-400 hover:text-slate-600'
                }`}
              >
                <Compass size={18} />
                <span className="text-[9px] font-bold">الرئيسية</span>
              </button>

              <button
                onClick={() => navigate('restaurants')}
                className={`flex flex-col items-center justify-center gap-1 ${
                  currentScreen === 'restaurants' ? 'text-emerald-700' : 'text-slate-400 hover:text-slate-600'
                }`}
              >
                <ShoppingBag size={18} />
                <span className="text-[9px] font-bold">المطاعم</span>
              </button>

              <button
                onClick={() => navigate('orders_history')}
                className={`flex flex-col items-center justify-center gap-1 ${
                  currentScreen === 'orders_history' ? 'text-emerald-700' : 'text-slate-400 hover:text-slate-600'
                }`}
              >
                <History size={18} />
                <span className="text-[9px] font-bold">طلباتي</span>
              </button>

              <button
                onClick={() => navigate('profile')}
                className={`flex flex-col items-center justify-center gap-1 ${
                  currentScreen === 'profile' ? 'text-emerald-700' : 'text-slate-400 hover:text-slate-600'
                }`}
              >
                <User size={18} />
                <span className="text-[9px] font-bold">حسابي</span>
              </button>
            </div>
          )}

          {/* Cart Float Button Overlay */}
          {['home', 'restaurants', 'restaurant_menu'].includes(currentScreen) && getCartCount() > 0 && (
            <div className="absolute bottom-18 left-4 right-4 z-40 animate-slideUp">
              <button
                onClick={() => navigate('cart')}
                className="w-full bg-[#FFB800] hover:bg-amber-500 active:bg-amber-600 text-slate-900 font-extrabold px-4 py-3 rounded-xl flex items-center justify-between shadow-lg"
              >
                <div className="flex items-center gap-2">
                  <span className="bg-slate-900 text-white font-extrabold text-[10px] w-5 h-5 rounded-full flex items-center justify-center">
                    {getCartCount()}
                  </span>
                  <span className="text-xs">سلة المشتريات وصال</span>
                </div>
                <div className="flex items-center gap-1.5 text-xs">
                  <span>{getCartTotal().itemsTotal} ر.ي</span>
                  <ChevronLeft size={16} />
                </div>
              </button>
            </div>
          )}

          {/* CART CONFLICT DIALOG */}
          {cartConflictRestaurant && (
            <div className="absolute inset-0 bg-black/60 flex items-center justify-center p-6 z-50 animate-fadeIn">
              <div className="bg-white rounded-3xl p-5 text-right w-full max-w-[300px] shadow-2xl border border-slate-100">
                <h4 className="text-xs font-black text-red-600 mb-2">تنبيه تعارض السلة!</h4>
                <p className="text-[11px] text-slate-600 leading-relaxed mb-4">
                  سلتك الحالية تحتوي على وجبات من مطعم آخر. لتتمكن من إضافة هذا الصنف، يجب إفراغ السلة وبدء طلب جديد من مطعم <span className="font-bold text-emerald-800">{cartConflictRestaurant.restaurant.nameAr}</span>.
                </p>
                <div className="flex gap-2">
                  <button
                    onClick={() => resolveCartConflict(true)}
                    className="flex-1 py-2 bg-emerald-700 hover:bg-emerald-800 text-white font-bold rounded-lg text-[10px] transition"
                  >
                    إفراغ والبدء مجدداً
                  </button>
                  <button
                    onClick={() => resolveCartConflict(false)}
                    className="flex-1 py-2 bg-slate-100 hover:bg-slate-200 text-slate-700 font-bold rounded-lg text-[10px] transition"
                  >
                    إلغاء التغيير
                  </button>
                </div>
              </div>
            </div>
          )}
        </div>

        {/* Android Native Home Button Bar */}
        <div className="h-7 bg-black flex items-center justify-center relative">
          <div className="w-28 h-1 bg-white/40 rounded-full"></div>
        </div>
      </div>
    </div>
  );
}
