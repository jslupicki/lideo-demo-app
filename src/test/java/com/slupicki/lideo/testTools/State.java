package com.slupicki.lideo.testTools;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_SINGLETON;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.slupicki.lideo.model.Client;
import com.slupicki.lideo.model.Flight;
import com.slupicki.lideo.model.Payment;
import com.slupicki.lideo.model.Reservation;
import java.util.List;
import java.util.Map;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@Scope(SCOPE_SINGLETON)
public class State {

  private enum Key {
    HTTP_STATUS,
    RESPONSE_BODY,
    CLIENT,
    CLIENT_ID,
    TEST_RESULT,
    FOUND_FLIGHTS,
    PAYMENT,
    RESERVATION
  }

  private static final Map<Key, Object> stateMap = Maps.newHashMap();

  public void clear() {
    stateMap.clear();
  }

  public void setHttpStatus(HttpStatus statusCode) {
    stateMap.put(Key.HTTP_STATUS, statusCode);
  }

  public HttpStatus getHttpStatus() {
    return (HttpStatus) stateMap.get(Key.HTTP_STATUS);
  }

  public void setResponseBody(String responseBody) {
    stateMap.put(Key.RESPONSE_BODY, responseBody);
  }

  public String getResponseBody() {
    return (String) stateMap.get(Key.RESPONSE_BODY);
  }

  public void setClient(Client client) {
    stateMap.put(Key.CLIENT, client);
  }

  public Client getClient() {
    return (Client) stateMap.get(Key.CLIENT);
  }

  public void setClientId(Long id) {
    stateMap.put(Key.CLIENT_ID, id);
  }

  public Long getClientId() {
    return (Long) stateMap.get(Key.CLIENT_ID);
  }

  public void setTestResult(Boolean result) {
    stateMap.put(Key.TEST_RESULT, result);
  }

  public Boolean getTestResult() {
    return (Boolean) stateMap.get(Key.TEST_RESULT);
  }

  public void setFoundFlights(Iterable<Flight> flights) {
    stateMap.put(Key.FOUND_FLIGHTS, Lists.newArrayList(flights));
  }

  @SuppressWarnings("unchecked")
  public List<Flight> getFoundFlights() {
    return (List<Flight>) stateMap.get(Key.FOUND_FLIGHTS);
  }

  public void setPayment(Payment payment) {
    stateMap.put(Key.PAYMENT, payment);
  }

  public Payment getPayment() {
    return (Payment) stateMap.get(Key.PAYMENT);
  }

  public void setReservation(Reservation reservation) {
    stateMap.put(Key.RESERVATION, reservation);
  }

  public Reservation getReservation() {
    return (Reservation) stateMap.get(Key.RESERVATION);
  }
}
