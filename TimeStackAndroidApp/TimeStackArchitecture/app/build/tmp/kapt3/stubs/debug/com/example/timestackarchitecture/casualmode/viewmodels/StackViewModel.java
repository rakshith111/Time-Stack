package com.example.timestackarchitecture.casualmode.viewmodels;

import java.lang.System;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u0002\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\fJ\u000e\u0010\u0017\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\fJ\u000e\u0010\u0018\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\fR\u0017\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR7\u0010\r\u001a\b\u0012\u0004\u0012\u00020\f0\u000b2\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000b8F@FX\u0086\u008e\u0002\u00a2\u0006\u0012\n\u0004\b\u0012\u0010\u0013\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0019"}, d2 = {"Lcom/example/timestackarchitecture/casualmode/viewmodels/StackViewModel;", "Landroidx/lifecycle/ViewModel;", "stackRepository", "Lcom/example/timestackarchitecture/casualmode/data/StackRepository;", "(Lcom/example/timestackarchitecture/casualmode/data/StackRepository;)V", "selectedItems", "Landroidx/compose/runtime/snapshots/SnapshotStateList;", "", "getSelectedItems", "()Landroidx/compose/runtime/snapshots/SnapshotStateList;", "<set-?>", "", "Lcom/example/timestackarchitecture/casualmode/data/StackData;", "stackList", "getStackList", "()Ljava/util/List;", "setStackList", "(Ljava/util/List;)V", "stackList$delegate", "Landroidx/compose/runtime/MutableState;", "insertStack", "", "stack", "removeStack", "updateStack", "app_debug"})
public final class StackViewModel extends androidx.lifecycle.ViewModel {
    private final com.example.timestackarchitecture.casualmode.data.StackRepository stackRepository = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.compose.runtime.MutableState stackList$delegate = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.compose.runtime.snapshots.SnapshotStateList<java.lang.Integer> selectedItems = null;
    
    public StackViewModel(@org.jetbrains.annotations.NotNull
    com.example.timestackarchitecture.casualmode.data.StackRepository stackRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<com.example.timestackarchitecture.casualmode.data.StackData> getStackList() {
        return null;
    }
    
    public final void setStackList(@org.jetbrains.annotations.NotNull
    java.util.List<com.example.timestackarchitecture.casualmode.data.StackData> p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.compose.runtime.snapshots.SnapshotStateList<java.lang.Integer> getSelectedItems() {
        return null;
    }
    
    public final void insertStack(@org.jetbrains.annotations.NotNull
    com.example.timestackarchitecture.casualmode.data.StackData stack) {
    }
    
    public final void updateStack(@org.jetbrains.annotations.NotNull
    com.example.timestackarchitecture.casualmode.data.StackData stack) {
    }
    
    public final void removeStack(@org.jetbrains.annotations.NotNull
    com.example.timestackarchitecture.casualmode.data.StackData stack) {
    }
}