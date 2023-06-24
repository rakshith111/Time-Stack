package com.example.timestackarchitecture;

import dagger.hilt.InstallIn;
import dagger.hilt.codegen.OriginatingElement;
import dagger.hilt.components.SingletonComponent;
import dagger.hilt.internal.GeneratedEntryPoint;

@OriginatingElement(
    topLevelClass = StackApplication.class
)
@GeneratedEntryPoint
@InstallIn(SingletonComponent.class)
public interface StackApplication_GeneratedInjector {
  void injectStackApplication(StackApplication stackApplication);
}
