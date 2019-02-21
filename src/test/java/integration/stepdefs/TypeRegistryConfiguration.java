package integration.stepdefs;

import com.google.common.collect.ImmutableList;
import com.slupicki.lideo.model.Flight;
import cucumber.api.TypeRegistry;
import cucumber.api.TypeRegistryConfigurer;
import io.cucumber.cucumberexpressions.ParameterType;
import io.cucumber.datatable.DataTableType;
import io.cucumber.datatable.TableEntryTransformer;
import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import org.assertj.core.util.Strings;

public class TypeRegistryConfiguration implements TypeRegistryConfigurer {

  private static final List<DateTimeFormatter> KNOWN_DATE_TIME_FORMATS = ImmutableList.of(
      "yyyy-MM-dd",
      "yyyy-MM-dd HH:mm:ss",
      "yyyy-MM-dd HH:mm",
      "dd-MM-yyyy",
      "dd-MM-yyyy HH:mm:ss",
      "dd-MM-yyyy HH:mm"
  ).stream()
      .map(s -> DateTimeFormatter.ofPattern(s).withZone(ZoneId.systemDefault()))
      .collect(Collectors.toList());

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

    typeRegistry.defineDataTableType(
        new DataTableType(
            Flight.class,
            (TableEntryTransformer<Flight>) map ->
                Flight.builder()
                    .arrival(map.get("arrival"))
                    .departure(map.get("departure"))
                    .freeSeats(Strings.isNullOrEmpty(map.get("free_seats")) ? null : Integer.parseUnsignedInt(map.get("free_seats")))
                    .pricePerSeat(Strings.isNullOrEmpty(map.get("price_per_seat")) ? null : new BigDecimal(map.get("price_per_seat")))
                    .departureTime(parseZonedDateTime(map.get("departure_time")))
                    .id(Strings.isNullOrEmpty(map.get("id")) ? null : Long.parseUnsignedLong(map.get("id")))
                    .build()
        )
    );
  }

  private ZonedDateTime parseZonedDateTime(String departureTime) {
    if (Strings.isNullOrEmpty(departureTime)) {
      return null;
    }
    for (DateTimeFormatter dateTimeFormatter : KNOWN_DATE_TIME_FORMATS) {
      try {
        return ZonedDateTime.parse(departureTime, dateTimeFormatter);
      } catch (Exception e) {
        // ignore - wrong format
      }
    }
    throw new RuntimeException("Can't parse date of '" + departureTime + "' - unknown format");
  }
}
