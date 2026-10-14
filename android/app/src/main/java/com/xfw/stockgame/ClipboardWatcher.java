package com.xfw.stockgame;

import android.content.ClipboardManager;
import android.content.Context;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

@CapacitorPlugin(name = "ClipboardWatcher")
public class ClipboardWatcher extends Plugin {

    private ClipboardManager clipboard;
    private ClipboardManager.OnPrimaryClipChangedListener listener;
    private boolean watching = false;

    @PluginMethod
    public void start(PluginCall call) {
        if (watching) { call.resolve(); return; }
        Context ctx = getContext();
        clipboard = (ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE);
        listener = new ClipboardManager.OnPrimaryClipChangedListener() {
            @Override
            public void onPrimaryClipChanged() {
                try {
                    if (clipboard != null && clipboard.hasPrimaryClip()) {
                        CharSequence text = clipboard.getPrimaryClip().getItemAt(0).coerceToText(ctx);
                        JSObject ret = new JSObject();
                        ret.put("value", text != null ? text.toString() : "");
                        notifyListeners("clipboardChanged", ret);
                    }
                } catch (Exception e) { /* 忽略 */ }
            }
        };
        clipboard.addPrimaryClipChangedListener(listener);
        watching = true;
        call.resolve();
    }

    @PluginMethod
    public void stop(PluginCall call) {
        if (clipboard != null && listener != null) {
            clipboard.removePrimaryClipChangedListener(listener);
        }
        watching = false;
        call.resolve();
    }

    @Override
    public void load() {
        super.load();
        // 插件加载时自动开始监听
        try {
            Context ctx = getContext();
            clipboard = (ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE);
            listener = new ClipboardManager.OnPrimaryClipChangedListener() {
                @Override
                public void onPrimaryClipChanged() {
                    try {
                        if (clipboard != null && clipboard.hasPrimaryClip()) {
                            CharSequence text = clipboard.getPrimaryClip().getItemAt(0).coerceToText(ctx);
                            JSObject ret = new JSObject();
                            ret.put("value", text != null ? text.toString() : "");
                            notifyListeners("clipboardChanged", ret);
                        }
                    } catch (Exception e) { /* 忽略 */ }
                }
            };
            clipboard.addPrimaryClipChangedListener(listener);
            watching = true;
        } catch (Exception e) { /* 忽略 */ }
    }
}
