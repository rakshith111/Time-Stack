package com.example.timestackarchitecture.casualmode.viewmodels;

import java.lang.System;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0006\b\u0007\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\u0007\u001a\u00020\bJ\u0006\u0010\t\u001a\u00020\bJ\u0006\u0010\n\u001a\u00020\u000bJ\u0006\u0010\f\u001a\u00020\u000bJ\u000e\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\bJ\u000e\u0010\u0010\u001a\u00020\u000e2\u0006\u0010\u0011\u001a\u00020\u000bJ\u000e\u0010\u0012\u001a\u00020\u000e2\u0006\u0010\u0011\u001a\u00020\u000bJ\u000e\u0010\u0013\u001a\u00020\u000e2\u0006\u0010\u0007\u001a\u00020\bR\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0014"}, d2 = {"Lcom/example/timestackarchitecture/casualmode/viewmodels/TimerViewModel;", "Landroidx/lifecycle/ViewModel;", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "sharedPreferencesManager", "Lcom/example/timestackarchitecture/casualmode/data/SharedPreferencesProgressRepository;", "firstTime", "", "getAlarmTriggered", "getProgress", "", "getStartTime", "saveAlarmTriggered", "", "isAlarmTriggered", "saveCurrentTime", "currentTime", "saveProgress", "setFirstTime", "app_debug"})
public final class TimerViewModel extends androidx.lifecycle.ViewModel {
    private com.example.timestackarchitecture.casualmode.data.SharedPreferencesProgressRepository sharedPreferencesManager;
    
    public TimerViewModel(@org.jetbrains.annotations.NotNull
    android.content.Context context) {
        super();
    }
    
    public final long getProgress() {
        return 0L;
    }
    
    public final void saveProgress(long currentTime) {
    }
    
    public final long getStartTime() {
        return 0L;
    }
    
    public final void saveCurrentTime(long currentTime) {
    }
    
    public final boolean firstTime() {
        return false;
    }
    
    public final void setFirstTime(boolean firstTime) {
    }
    
    public final boolean getAlarmTriggered() {
        return false;
    }
    
    public final void saveAlarmTriggered(boolean isAlarmTriggered) {
    }
}