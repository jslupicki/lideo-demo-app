package integration.stepdefs;

import cucumber.api.TypeRegistry;
import cucumber.api.TypeRegistryConfigurer;
import io.cucumber.cucumberexpressions.ParameterType;
import java.util.Locale;

public class TypeRegistryConfiguration implements TypeRegistryConfigurer {

  @Override
  public Locale locale() {
    return Locale.ENGLISH;
  }

  @Override
  public void configureTypeRegistry(TypeRegistry typeRegistry) {
    typeRegistry.defineParameterType(new ParameterType<>(
        "bool",
        "true|false|TRUE|FALSE",
        Boolean.class,
        Boolean::parseBoolean)
    );

  }
}
