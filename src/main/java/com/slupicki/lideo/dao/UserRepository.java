package com.slupicki.lideo.dao;

import com.slupicki.lideo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
