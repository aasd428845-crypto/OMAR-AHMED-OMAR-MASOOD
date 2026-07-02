import React from 'react';
import PhoneSimulator from './components/PhoneSimulator';
import CodeInspector from './components/CodeInspector';
import { Smartphone, FileCode, CheckCircle, Database, ShieldAlert, Cpu } from 'lucide-react';

export default function App() {
  return (
    <div className="min-h-screen bg-slate-950 text-slate-100 flex flex-col font-sans select-none">
      {/* Top Professional Header */}
      <header className="bg-slate-900 border-b border-slate-800 px-6 py-4 sticky top-0 z-50">
        <div className="max-w-7xl mx-auto flex flex-col md:flex-row md:items-center md:justify-between gap-4">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 bg-[#1A5C3A] rounded-xl flex items-center justify-center border border-emerald-500/30">
              <span className="text-xl font-extrabold text-[#FFB800]">وصال</span>
            </div>
            <div>
              <div className="flex items-center gap-2">
                <h1 className="text-lg font-bold tracking-tight text-white">منصة وصال للتوصيل • Wasal Android SDK</h1>
                <span className="text-xs bg-emerald-500/10 text-emerald-400 border border-emerald-500/20 px-2.5 py-0.5 rounded-full font-bold">وصال 🇾🇪</span>
              </div>
              <p className="text-xs text-slate-400">نظام أندرويد متكامل لتوصيل الطعام والطرود باللغة العربية (Kotlin / Compose / Supabase)</p>
            </div>
          </div>

          <div className="flex items-center gap-3.5 flex-wrap">
            <div className="bg-slate-950/60 px-3 py-1.5 rounded-lg border border-slate-800 flex items-center gap-1.5">
              <Cpu size={14} className="text-emerald-400" />
              <span className="text-[10px] text-slate-300 font-mono">Min SDK: 26 | Target: 34</span>
            </div>
            <div className="bg-slate-950/60 px-3 py-1.5 rounded-lg border border-slate-800 flex items-center gap-1.5">
              <Database size={14} className="text-emerald-400" />
              <span className="text-[10px] text-slate-300 font-mono">Supabase client lazy init</span>
            </div>
          </div>
        </div>
      </header>

      {/* Main Workspace Layout (Bento Grid) */}
      <main className="flex-1 max-w-7xl w-full mx-auto p-6 flex flex-col lg:flex-row gap-8 items-stretch">
        
        {/* Column 1: Interactive Android App Simulator */}
        <div className="flex-1 flex flex-col items-center">
          <div className="w-full max-w-[400px] flex flex-col">
            <div className="flex items-center gap-2 mb-3.5 px-2">
              <Smartphone size={16} className="text-emerald-400" />
              <span className="text-xs font-bold text-slate-300 uppercase tracking-wider font-mono">Interactive Live App Simulator</span>
            </div>
            <div className="p-4 bg-slate-900 border border-slate-800 rounded-3xl shadow-xl flex justify-center">
              <PhoneSimulator />
            </div>
          </div>
        </div>

        {/* Column 2: Android Jetpack Compose Source Code Viewer */}
        <div className="flex-[1.4] flex flex-col min-w-0">
          <div className="flex items-center gap-2 mb-3.5 px-2">
            <FileCode size={16} className="text-emerald-400" />
            <span className="text-xs font-bold text-slate-300 uppercase tracking-wider font-mono">Jetpack Compose & Kotlin Project Files</span>
          </div>
          <div className="flex-1 min-h-[600px] lg:h-0">
            <CodeInspector />
          </div>
        </div>

      </main>

      {/* Platform Architecture & اليمن Notes Block */}
      <section className="bg-slate-900/60 border-t border-slate-800 py-8 px-6 mt-12">
        <div className="max-w-7xl mx-auto grid grid-cols-1 md:grid-cols-3 gap-8">
          
          <div className="space-y-2">
            <div className="flex items-center gap-2">
              <CheckCircle size={16} className="text-emerald-400" />
              <h3 className="text-sm font-bold text-white">بنية برمجية معتمدة (MVVM)</h3>
            </div>
            <p className="text-xs text-slate-400 leading-relaxed text-right" dir="rtl">
              تم بناء التطبيق باستخدام أحدث الممارسات البرمجية الموصى بها من جوجل، مثل Jetpack Compose لبناء الواجهات الرسومية المتجاوبة ونمط المستودعات (Repository Pattern) لفصل منطق البيانات والاتصال بسيرفرات سوبابيس.
            </p>
          </div>

          <div className="space-y-2">
            <div className="flex items-center gap-2">
              <CheckCircle size={16} className="text-emerald-400" />
              <h3 className="text-sm font-bold text-white">منظومة عربية متكاملة (RTL)</h3>
            </div>
            <p className="text-xs text-slate-400 leading-relaxed text-right" dir="rtl">
              يدعم التطبيق التوجيه العربي الافتراضي كلياً (Right-to-Left) مع محاذاة الخطوط Cairo للغة العربية، وتخصيص الواجهات والمدخلات لتناسب السوق اليمني (منسق رقم الهاتف الصنعاني، العملة ر.ي، والخرائط المحلية).
            </p>
          </div>

          <div className="space-y-2">
            <div className="flex items-center gap-2">
              <ShieldAlert size={16} className="text-amber-400" />
              <h3 className="text-sm font-bold text-white">الاتصال بسوبابيس (Supabase Client)</h3>
            </div>
            <p className="text-xs text-slate-400 leading-relaxed text-right" dir="rtl">
              تم تضمين ملف SupabaseClient الذي يقوم بتهيئة الاتصال بقاعدة بيانات سوبابيس بشكل آمن وكسول (Lazy Initialization) عند الحاجة مع تفعيل مصادقة المستخدم وتخزين الملفات والربط الفوري بالبيانات Realtime.
            </p>
          </div>

        </div>
      </section>

      {/* Professional Humble Footer */}
      <footer className="bg-slate-950 border-t border-slate-900 py-4 px-6 text-center text-xs text-slate-500 font-mono">
        وصال (Wasal) Android Deliveries Platform • Built for Yemen 🇾🇪
      </footer>
    </div>
  );
}
