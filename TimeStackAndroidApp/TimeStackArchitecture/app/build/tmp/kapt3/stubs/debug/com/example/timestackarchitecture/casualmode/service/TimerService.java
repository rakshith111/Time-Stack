package com.example.timestackarchitecture.casualmode.service;

import java.lang.System;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0007\u0018\u0000 \u00142\u00020\u0001:\u0001\u0014B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0002J\u0014\u0010\u0005\u001a\u0004\u0018\u00010\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\bH\u0016J\"\u0010\t\u001a\u00020\n2\b\u0010\u0007\u001a\u0004\u0018\u00010\b2\u0006\u0010\u000b\u001a\u00020\n2\u0006\u0010\f\u001a\u00020\nH\u0017J\u0006\u0010\r\u001a\u00020\u000eJ\u000e\u0010\u000f\u001a\u00020\u000e2\u0006\u0010\u0010\u001a\u00020\u0011J\u0006\u0010\u0012\u001a\u00020\u000eJ\u000e\u0010\u0013\u001a\u00020\u000e2\u0006\u0010\u0010\u001a\u00020\u0011\u00a8\u0006\u0015"}, d2 = {"Lcom/example/timestackarchitecture/casualmode/service/TimerService;", "Landroid/app/Service;", "()V", "createNotification", "Landroid/app/Notification;", "onBind", "Landroid/os/IBinder;", "intent", "Landroid/content/Intent;", "onStartCommand", "", "flags", "startId", "startNotificationRingtone", "", "startRingtone", "context", "Landroid/content/Context;", "stopRingtone", "updateNotificationContent", "Companion", "app_debug"})
public final class TimerService extends android.app.Service {
    @org.jetbrains.annotations.NotNull
    public static final com.example.timestackarchitecture.casualmode.service.TimerService.Companion Companion = null;
    private static java.lang.Long duration;
    private static android.media.Ringtone ringtone;
    private static android.media.MediaPlayer NotificationRingtone;
    private static boolean isDeviceActive = true;
    @org.jetbrains.annotations.NotNull
    private static java.lang.String stackName = "";
    @org.jetbrains.annotations.NotNull
    private static java.lang.String convertedTime = "";
    
    public TimerService() {
        super();
    }
    
    @androidx.annotation.RequiresApi(value = android.os.Build.VERSION_CODES.O)
    @java.lang.Override
    public int onStartCommand(@org.jetbrains.annotations.Nullable
    android.content.Intent intent, int flags, int startId) {
        return 0;
    }
    
    private final android.app.Notification createNotification() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    @java.lang.Override
    public android.os.IBinder onBind(@org.jetbrains.annotations.Nullable
    android.content.Intent intent) {
        return null;
    }
    
    public final void updateNotificationContent(@org.jetbrains.annotations.NotNull
    android.content.Context context) {
    }
    
    public final void stopRingtone() {
    }
    
    public final void startRingtone(@org.jetbrains.annotations.NotNull
    android.content.Context context) {
    }
    
    public final void startNotificationRingtone() {
    }
    
    @kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0005\u001a\u00020\u0006X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u0012\u0010\u000b\u001a\u0004\u0018\u00010\fX\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010\rR\u001a\u0010\u000e\u001a\u00020\u000fX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u0010\"\u0004\b\u0011\u0010\u0012R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082.\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0015\u001a\u00020\u0006X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\b\"\u0004\b\u0017\u0010\n\u00a8\u0006\u0018"}, d2 = {"Lcom/example/timestackarchitecture/casualmode/service/TimerService$Companion;", "", "()V", "NotificationRingtone", "Landroid/media/MediaPlayer;", "convertedTime", "", "getConvertedTime", "()Ljava/lang/String;", "setConvertedTime", "(Ljava/lang/String;)V", "duration", "", "Ljava/lang/Long;", "isDeviceActive", "", "()Z", "setDeviceActive", "(Z)V", "ringtone", "Landroid/media/Ringtone;", "stackName", "getStackName", "setStackName", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        public final boolean isDeviceActive() {
            return false;
        }
        
        public final void setDeviceActive(boolean p0) {
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String getStackName() {
            return null;
        }
        
        public final void setStackName(@org.jetbrains.annotations.NotNull
        java.lang.String p0) {
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String getConvertedTime() {
            return null;
        }
        
        public final void setConvertedTime(@org.jetbrains.annotations.NotNull
        java.lang.String p0) {
        }
    }
}