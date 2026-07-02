import React, { useState } from 'react';
import { KOTLIN_FILES } from '../data';
import { FileCode, Clipboard, Check, Terminal, FolderOpen, Info } from 'lucide-react';

export default function CodeInspector() {
  const [selectedFile, setSelectedFile] = useState(KOTLIN_FILES[0]);
  const [copied, setCopied] = useState(false);

  const handleCopy = () => {
    navigator.clipboard.writeText(selectedFile.content);
    setCopied(true);
    setTimeout(() => setCopied(false), 2000);
  };

  return (
    <div className="flex flex-col h-full bg-slate-900 border border-slate-800 rounded-2xl overflow-hidden shadow-2xl" id="code-inspector">
      {/* Inspector Header */}
      <div className="flex items-center justify-between px-6 py-4 bg-slate-950 border-b border-slate-800">
        <div className="flex items-center gap-3">
          <div className="p-2 bg-emerald-500/10 rounded-lg text-emerald-400">
            <FileCode size={20} />
          </div>
          <div>
            <h2 className="text-sm font-bold text-slate-100 tracking-wide font-mono">Wasal Android Source Inspector</h2>
            <p className="text-xs text-slate-400">ملفات الكود البرمجي لمشروع أندرويد</p>
          </div>
        </div>

        <button
          onClick={handleCopy}
          className="flex items-center gap-2 px-3.5 py-1.5 bg-slate-800 hover:bg-slate-700 active:bg-slate-750 text-slate-200 hover:text-white rounded-lg transition-all duration-200 text-xs font-medium border border-slate-700"
        >
          {copied ? (
            <>
              <Check size={14} className="text-emerald-400" />
              <span className="text-emerald-400 font-mono">Copied!</span>
            </>
          ) : (
            <>
              <Clipboard size={14} />
              <span className="font-mono">Copy Code</span>
            </>
          )}
        </button>
      </div>

      {/* Main Content Pane */}
      <div className="flex flex-1 min-h-0 divide-x divide-slate-800 flex-col md:flex-row">
        {/* File Browser Sidebar (Left) */}
        <div className="w-full md:w-64 bg-slate-950 p-4 overflow-y-auto flex-shrink-0 flex flex-col gap-3">
          <div className="flex items-center gap-2 px-2 text-xs font-semibold text-slate-400 uppercase tracking-wider font-mono">
            <FolderOpen size={13} />
            <span>Project Files</span>
          </div>
          
          <div className="flex flex-col gap-1.5">
            {KOTLIN_FILES.map((file) => {
              const isSelected = file.path === selectedFile.path;
              return (
                <button
                  key={file.path}
                  onClick={() => setSelectedFile(file)}
                  className={`w-full text-left px-3 py-2 rounded-lg transition-all duration-150 flex flex-col gap-1 border ${
                    isSelected
                      ? 'bg-emerald-500/10 border-emerald-500/20 text-emerald-300'
                      : 'bg-transparent border-transparent text-slate-400 hover:bg-slate-800 hover:text-slate-200'
                  }`}
                >
                  <span className="text-xs font-semibold font-mono truncate">{file.name}</span>
                  <span className="text-[10px] opacity-75 font-mono truncate">{file.path}</span>
                </button>
              );
            })}
          </div>
        </div>

        {/* Code Content Area (Right) */}
        <div className="flex-1 flex flex-col min-w-0 bg-slate-900 overflow-hidden">
          {/* File description info bar */}
          <div className="bg-slate-950/60 px-6 py-3 border-b border-slate-800 flex items-start gap-2.5">
            <Info size={16} className="text-emerald-500 flex-shrink-0 mt-0.5" />
            <div className="text-xs text-slate-300 leading-relaxed font-sans text-right" dir="rtl">
              <span className="font-bold text-emerald-400 ml-1">شرح الملف:</span>
              {selectedFile.description}
            </div>
          </div>

          {/* Real code box */}
          <div className="flex-1 p-6 overflow-auto font-mono text-xs text-slate-300 leading-relaxed bg-slate-900 select-text">
            <pre className="whitespace-pre">
              <code>{selectedFile.content}</code>
            </pre>
          </div>
        </div>
      </div>

      {/* Footer Info */}
      <div className="px-6 py-3 bg-slate-950 border-t border-slate-800 flex items-center justify-between text-[10px] text-slate-400 font-mono">
        <div className="flex items-center gap-1.5">
          <Terminal size={12} className="text-slate-500" />
          <span>Active: Kotlin 1.9.24 / Compose Material3</span>
        </div>
        <span>Target SDK: 34</span>
      </div>
    </div>
  );
}
