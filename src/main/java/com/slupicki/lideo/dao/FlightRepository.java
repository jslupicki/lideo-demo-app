package com.slupicki.lideo.dao;

import com.slupicki.lideo.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlightRepository extends JpaRepository<Flight, Long> {

}
