package com.example.timestackarchitecture;

import java.lang.System;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010\u0013\u001a\u00020\u00142\b\u0010\u0015\u001a\u0004\u0018\u00010\u0016H\u0015J\b\u0010\u0017\u001a\u00020\u0014H\u0014J\b\u0010\u0018\u001a\u00020\u0014H\u0014J\b\u0010\u0019\u001a\u00020\u0014H\u0014R\u001c\u0010\u0003\u001a\u0010\u0012\f\u0012\n \u0006*\u0004\u0018\u00010\u00050\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u0007\u001a\u00020\b8\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\fR\u001e\u0010\r\u001a\u00020\u000e8\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\u0010\"\u0004\b\u0011\u0010\u0012\u00a8\u0006\u001a"}, d2 = {"Lcom/example/timestackarchitecture/MainActivity;", "Landroidx/activity/ComponentActivity;", "()V", "requestPermissionLauncher", "Landroidx/activity/result/ActivityResultLauncher;", "", "kotlin.jvm.PlatformType", "stackViewModelFactory", "Lcom/example/timestackarchitecture/casualmode/viewmodels/StackViewModelFactory;", "getStackViewModelFactory", "()Lcom/example/timestackarchitecture/casualmode/viewmodels/StackViewModelFactory;", "setStackViewModelFactory", "(Lcom/example/timestackarchitecture/casualmode/viewmodels/StackViewModelFactory;)V", "timerViewModelFactory", "Lcom/example/timestackarchitecture/casualmode/viewmodels/TimerViewModelFactory;", "getTimerViewModelFactory", "()Lcom/example/timestackarchitecture/casualmode/viewmodels/TimerViewModelFactory;", "setTimerViewModelFactory", "(Lcom/example/timestackarchitecture/casualmode/viewmodels/TimerViewModelFactory;)V", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "onDestroy", "onPause", "onResume", "app_debug"})
@dagger.hilt.android.AndroidEntryPoint
public final class MainActivity extends androidx.activity.ComponentActivity {
    @javax.inject.Inject
    public com.example.timestackarchitecture.casualmode.viewmodels.StackViewModelFactory stackViewModelFactory;
    @javax.inject.Inject
    public com.example.timestackarchitecture.casualmode.viewmodels.TimerViewModelFactory timerViewModelFactory;
    private final androidx.activity.result.ActivityResultLauncher<java.lang.String> requestPermissionLauncher = null;
    
    public MainActivity() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.example.timestackarchitecture.casualmode.viewmodels.StackViewModelFactory getStackViewModelFactory() {
        return null;
    }
    
    public final void setStackViewModelFactory(@org.jetbrains.annotations.NotNull
    com.example.timestackarchitecture.casualmode.viewmodels.StackViewModelFactory p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.example.timestackarchitecture.casualmode.viewmodels.TimerViewModelFactory getTimerViewModelFactory() {
        return null;
    }
    
    public final void setTimerViewModelFactory(@org.jetbrains.annotations.NotNull
    com.example.timestackarchitecture.casualmode.viewmodels.TimerViewModelFactory p0) {
    }
    
    @androidx.annotation.RequiresApi(value = android.os.Build.VERSION_CODES.O)
    @kotlin.OptIn(markerClass = {androidx.compose.animation.ExperimentalAnimationApi.class})
    @java.lang.Override
    protected void onCreate(@org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
    }
    
    @java.lang.Override
    protected void onPause() {
    }
    
    @java.lang.Override
    protected void onDestroy() {
    }
    
    @java.lang.Override
    protected void onResume() {
    }
}