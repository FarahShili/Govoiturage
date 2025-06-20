package com.fst.Govoiturage.Repository;

import com.fst.Govoiturage.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    User findById(long id);
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.reviewsReceived WHERE u.id = :id")
    Optional<User> findByIdWithReviews(@Param("id") Long id);

    List<User> findByRidesIsNotEmpty();
}
