package com.savely.socksapp.repository;

import com.savely.socksapp.entity.Sock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SockRepository extends JpaRepository<Sock, Long> {
    Optional<Sock> findByColorAndCottonPart(String color, int cottonPart);

    List<Sock> findByColorAndCottonPartGreaterThan(String color, int cottonPart);

    List<Sock> findByColorAndCottonPartLessThan(String color, int cottonPart);
}
