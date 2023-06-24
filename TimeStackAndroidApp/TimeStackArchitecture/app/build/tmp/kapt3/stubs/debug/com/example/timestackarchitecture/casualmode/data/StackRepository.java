package com.example.timestackarchitecture.casualmode.data;

import java.lang.System;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001J\u0011\u0010\u0002\u001a\u00020\u0003H\u00a6@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0004J\u0019\u0010\u0005\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u0007H\u00a6@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\bJ\u0014\u0010\t\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00070\u000b0\nH&J\u0019\u0010\f\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u0007H\u00a6@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\bJ\u0019\u0010\r\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u0007H\u00a6@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\b\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u000e"}, d2 = {"Lcom/example/timestackarchitecture/casualmode/data/StackRepository;", "", "deleteAllStacks", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteStack", "stack", "Lcom/example/timestackarchitecture/casualmode/data/StackData;", "(Lcom/example/timestackarchitecture/casualmode/data/StackData;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getStacks", "Lkotlinx/coroutines/flow/Flow;", "", "insertStack", "updateStack", "app_debug"})
public abstract interface StackRepository {
    
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object insertStack(@org.jetbrains.annotations.NotNull
    com.example.timestackarchitecture.casualmode.data.StackData stack, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);
    
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object updateStack(@org.jetbrains.annotations.NotNull
    com.example.timestackarchitecture.casualmode.data.StackData stack, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);
    
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object deleteStack(@org.jetbrains.annotations.NotNull
    com.example.timestackarchitecture.casualmode.data.StackData stack, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);
    
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object deleteAllStacks(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);
    
    @org.jetbrains.annotations.NotNull
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.example.timestackarchitecture.casualmode.data.StackData>> getStacks();
}