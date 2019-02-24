package integration.stepdefs;

import com.google.common.collect.ImmutableList;
import com.slupicki.lideo.model.Client;
import com.slupicki.lideo.model.Flight;
import com.slupicki.lideo.model.FlightDTO;
import com.slupicki.lideo.model.Payment;
import com.slupicki.lideo.model.PaymentDTO;
import com.slupicki.lideo.model.Reservation;
import com.slupicki.lideo.model.ReservationDTO;
import cucumber.api.TypeRegistry;
import cucumber.api.TypeRegistryConfigurer;
import io.cucumber.cucumberexpressions.ParameterType;
import io.cucumber.datatable.DataTableType;
import io.cucumber.datatable.TableEntryTransformer;
import io.vavr.control.Try;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.assertj.core.util.Strings;
import org.springframework.stereotype.Component;

@Component
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

    // Fields in table (all optional):
    // | id | departure | arrival | departure_time | free_seats | price_per_seat |
    typeRegistry.defineDataTableType(
        new DataTableType(
            Flight.class,
            (TableEntryTransformer<Flight>) map ->
                Flight.builder()
                    .id(sanitize(map.get("id"), Long::parseUnsignedLong))
                    .departure(sanitize(map.get("departure"), s -> s))
                    .arrival(sanitize(map.get("arrival"), s -> s))
                    .departureTime(parseZonedDateTime(map.get("departure_time")))
                    .freeSeats(sanitize(map.get("free_seats"), Integer::parseUnsignedInt))
                    .pricePerSeat(sanitize(map.get("price_per_seat"), BigDecimal::new))
                    .build()
        )
    );

    // Fields in table (all optional):
    // | departure | arrival | from_date | to_date | seats |
    typeRegistry.defineDataTableType(
        new DataTableType(
            FlightDTO.class,
            (TableEntryTransformer<FlightDTO>) map ->
                FlightDTO.builder()
                    .departure(sanitize(map.get("departure"), s -> s))
                    .arrival(sanitize(map.get("arrival"), s -> s))
                    .fromDepartureTime(parseZonedDateTime(map.get("from_date")))
                    .toDepartureTime(parseZonedDateTime(map.get("to_date")))
                    .seats(sanitize(map.get("seats"), Integer::parseUnsignedInt))
                    .build()
        )
    );

    // Fields in table (all optional):
    // | id | name | surname | address | login | password |
    typeRegistry.defineDataTableType(
        new DataTableType(
            Client.class,
            (TableEntryTransformer<Client>) map ->
                Client.builder()
                    .id(sanitize(map.get("id"), Long::parseUnsignedLong))
                    .name(sanitize(map.get("name"), s -> s))
                    .surname(sanitize(map.get("surname"), s -> s))
                    .address(sanitize(map.get("address"), s -> s))
                    .login(sanitize(map.get("login"), s -> s))
                    .password(sanitize(map.get("password"), s -> s))
                    .build()
        )
    );

    // Fields in table (id, amount mandatory):
    // | id | amount | external_id | paid | created_at |
    typeRegistry.defineDataTableType(
        new DataTableType(
            Payment.class,
            (TableEntryTransformer<Payment>) map ->
                Payment.builder()
                    .id(sanitize(map.get("id"), Long::parseUnsignedLong))
                    .amount(sanitize(map.get("amount"), BigDecimal::new))
                    .externalId(sanitize(map.get("external_id"), s -> s))
                    .paid(sanitize(map.get("paid"), Boolean::parseBoolean))
                    .createdAt(parseZonedDateTime(map.get("created_at")))
                    .build()
        )
    );

    // Fields in table (payment_id mandatory):
    // | payment_id | external_id |
    typeRegistry.defineDataTableType(
        new DataTableType(
            PaymentDTO.class,
            (TableEntryTransformer<PaymentDTO>) map ->
                PaymentDTO.builder()
                    .paymentId(sanitize(map.get("payment_id"), Long::parseUnsignedLong))
                    .externalId(sanitize(map.get("external_id"), s -> s))
                    .build()
        )
    );

    // Fields in table (client_id, flight_id and payment_id mandatory):
    // | id | client_id | flight_id | payment_id | seats | price | cancellation |
    typeRegistry.defineDataTableType(
        new DataTableType(
            Reservation.class,
            (TableEntryTransformer<Reservation>) map ->
                Reservation.builder()
                    .id(sanitize(map.get("id"), Long::parseUnsignedLong))
                    .client(sanitize(map.get("client_id"), s -> Client.builder().id(Long.parseUnsignedLong(s)).build()))
                    .flight(sanitize(map.get("flight_id"), s -> Flight.builder().id(Long.parseUnsignedLong(s)).build()))
                    .payment(sanitize(map.get("payment_id"), s -> Payment.builder().id(Long.parseUnsignedLong(s)).build()))
                    .seats(sanitize(map.get("seats"), Integer::parseUnsignedInt))
                    .price(sanitize(map.get("price"), BigDecimal::new))
                    .cancellation(sanitize(map.get("cancellation"), Boolean::parseBoolean))
                    .build()
        )
    );

    // Fields in table (only faster_check_in is optional):
    // | client_id | flight_id | seats | faster_check_in |
    typeRegistry.defineDataTableType(
        new DataTableType(
            ReservationDTO.class,
            (TableEntryTransformer<ReservationDTO>) map ->
                ReservationDTO.builder()
                    .clientId(sanitize(map.get("client_id"), Long::parseUnsignedLong))
                    .flightId(sanitize(map.get("flight_id"), Long::parseUnsignedLong))
                    .seats(sanitize(map.get("seats"), Integer::parseUnsignedInt))
                    .fasterCheckIn(sanitize(map.get("faster_check_in"), Boolean::parseBoolean))
                    .build()
        )
    );
  }

  public static ZonedDateTime parseZonedDateTime(String time) {
    if (Strings.isNullOrEmpty(time)) {
      return null;
    }
    for (DateTimeFormatter dateTimeFormatter : KNOWN_DATE_TIME_FORMATS) {
      try {
        return Try.of(() -> ZonedDateTime.parse(time, dateTimeFormatter))
            .recover(e -> LocalDate.parse(time, dateTimeFormatter).atStartOfDay(ZoneId.systemDefault()))
            .getOrElseThrow(t -> t);
      } catch (Throwable e) {
        // ignore - wrong format
      }
    }
    throw new RuntimeException("Can't parse date of '" + time + "' - unknown format");
  }

  private <T> T sanitize(String parameter, Function<String, T> converter) {
    if (Strings.isNullOrEmpty(parameter)) {
      return null;
    }
    return converter.apply(parameter);
  }
}
