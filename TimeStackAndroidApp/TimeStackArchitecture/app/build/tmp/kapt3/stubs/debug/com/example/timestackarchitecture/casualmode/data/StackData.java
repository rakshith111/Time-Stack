package com.example.timestackarchitecture.casualmode.data;

import java.lang.System;

@androidx.room.Entity(tableName = "stack_table")
@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u001c\b\u0087\b\u0018\u00002\u00020\u0001B-\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\t\u00a2\u0006\u0002\u0010\u000bJ\t\u0010\u001b\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001c\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u001d\u001a\u00020\u0007H\u00c6\u0003J\t\u0010\u001e\u001a\u00020\tH\u00c6\u0003J\t\u0010\u001f\u001a\u00020\tH\u00c6\u0003J;\u0010 \u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\tH\u00c6\u0001J\u0013\u0010!\u001a\u00020\t2\b\u0010\"\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010#\u001a\u00020\u0003H\u00d6\u0001J\t\u0010$\u001a\u00020\u0005H\u00d6\u0001R\u001e\u0010\b\u001a\u00020\t8\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000fR\u0016\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u001e\u0010\n\u001a\u00020\t8\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\r\"\u0004\b\u0012\u0010\u000fR\u001e\u0010\u0004\u001a\u00020\u00058\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0014\"\u0004\b\u0015\u0010\u0016R\u001e\u0010\u0006\u001a\u00020\u00078\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\u0018\"\u0004\b\u0019\u0010\u001a\u00a8\u0006%"}, d2 = {"Lcom/example/timestackarchitecture/casualmode/data/StackData;", "", "id", "", "stackName", "", "stackTime", "", "activeStack", "", "isPlaying", "(ILjava/lang/String;JZZ)V", "getActiveStack", "()Z", "setActiveStack", "(Z)V", "getId", "()I", "setPlaying", "getStackName", "()Ljava/lang/String;", "setStackName", "(Ljava/lang/String;)V", "getStackTime", "()J", "setStackTime", "(J)V", "component1", "component2", "component3", "component4", "component5", "copy", "equals", "other", "hashCode", "toString", "app_debug"})
public final class StackData {
    @androidx.room.ColumnInfo(name = "stack_id")
    @androidx.room.PrimaryKey(autoGenerate = true)
    private final int id = 0;
    @org.jetbrains.annotations.NotNull
    @androidx.room.ColumnInfo(name = "stack_name")
    private java.lang.String stackName;
    @androidx.room.ColumnInfo(name = "stack_time")
    private long stackTime;
    @androidx.room.ColumnInfo(name = "stack_active")
    private boolean activeStack;
    @androidx.room.ColumnInfo(name = "stack_is_playing")
    private boolean isPlaying;
    
    @org.jetbrains.annotations.NotNull
    public final com.example.timestackarchitecture.casualmode.data.StackData copy(int id, @org.jetbrains.annotations.NotNull
    java.lang.String stackName, long stackTime, boolean activeStack, boolean isPlaying) {
        return null;
    }
    
    @java.lang.Override
    public boolean equals(@org.jetbrains.annotations.Nullable
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override
    public int hashCode() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String toString() {
        return null;
    }
    
    public StackData(int id, @org.jetbrains.annotations.NotNull
    java.lang.String stackName, long stackTime, boolean activeStack, boolean isPlaying) {
        super();
    }
    
    public final int component1() {
        return 0;
    }
    
    public final int getId() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getStackName() {
        return null;
    }
    
    public final void setStackName(@org.jetbrains.annotations.NotNull
    java.lang.String p0) {
    }
    
    public final long component3() {
        return 0L;
    }
    
    public final long getStackTime() {
        return 0L;
    }
    
    public final void setStackTime(long p0) {
    }
    
    public final boolean component4() {
        return false;
    }
    
    public final boolean getActiveStack() {
        return false;
    }
    
    public final void setActiveStack(boolean p0) {
    }
    
    public final boolean component5() {
        return false;
    }
    
    public final boolean isPlaying() {
        return false;
    }
    
    public final void setPlaying(boolean p0) {
    }
}