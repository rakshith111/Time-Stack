// Generated by Dagger (https://dagger.dev).
package com.example.timestackarchitecture.casualmode.viewmodels;

import com.example.timestackarchitecture.casualmode.data.StackRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class StackViewModelFactory_Factory implements Factory<StackViewModelFactory> {
  private final Provider<StackRepository> repositoryProvider;

  public StackViewModelFactory_Factory(Provider<StackRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public StackViewModelFactory get() {
    return newInstance(repositoryProvider.get());
  }

  public static StackViewModelFactory_Factory create(Provider<StackRepository> repositoryProvider) {
    return new StackViewModelFactory_Factory(repositoryProvider);
  }

  public static StackViewModelFactory newInstance(StackRepository repository) {
    return new StackViewModelFactory(repository);
  }
}
