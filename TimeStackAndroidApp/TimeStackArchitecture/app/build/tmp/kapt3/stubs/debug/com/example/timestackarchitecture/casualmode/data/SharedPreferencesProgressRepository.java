package com.example.timestackarchitecture.casualmode.data;

import java.lang.System;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\b\b\u0007\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\n\u001a\u00020\u000bJ\u0006\u0010\f\u001a\u00020\u000bJ\u0006\u0010\r\u001a\u00020\u000eJ\u0006\u0010\u000f\u001a\u00020\u000eJ\u000e\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u000bJ\u000e\u0010\u0013\u001a\u00020\u00112\u0006\u0010\u0014\u001a\u00020\u000eJ\u000e\u0010\u0015\u001a\u00020\u00112\u0006\u0010\u0016\u001a\u00020\u000bJ\u000e\u0010\u0017\u001a\u00020\u00112\u0006\u0010\u0018\u001a\u00020\u000eR\u0016\u0010\u0005\u001a\n \u0007*\u0004\u0018\u00010\u00060\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\b\u001a\n \u0007*\u0004\u0018\u00010\u00060\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\t\u001a\n \u0007*\u0004\u0018\u00010\u00060\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0019"}, d2 = {"Lcom/example/timestackarchitecture/casualmode/data/SharedPreferencesProgressRepository;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "alarmTriggered", "Landroid/content/SharedPreferences;", "kotlin.jvm.PlatformType", "currentTimePrefs", "sharedPrefTimer", "firstTime", "", "getAlarmTriggered", "getStartTime", "", "getTimerProgress", "saveAlarmTriggered", "", "isAlarmTriggered", "saveCurrentTime", "currentTime", "saveFirstTime", "isFirstTime", "saveTimerProgress", "progress", "app_debug"})
public final class SharedPreferencesProgressRepository {
    private final android.content.SharedPreferences sharedPrefTimer = null;
    private final android.content.SharedPreferences currentTimePrefs = null;
    private final android.content.SharedPreferences alarmTriggered = null;
    
    public SharedPreferencesProgressRepository(@org.jetbrains.annotations.NotNull
    android.content.Context context) {
        super();
    }
    
    public final void saveTimerProgress(long progress) {
    }
    
    public final long getTimerProgress() {
        return 0L;
    }
    
    public final void saveCurrentTime(long currentTime) {
    }
    
    public final long getStartTime() {
        return 0L;
    }
    
    public final boolean firstTime() {
        return false;
    }
    
    public final void saveFirstTime(boolean isFirstTime) {
    }
    
    public final boolean getAlarmTriggered() {
        return false;
    }
    
    public final void saveAlarmTriggered(boolean isAlarmTriggered) {
    }
}