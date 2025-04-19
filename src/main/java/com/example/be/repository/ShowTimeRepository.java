package com.example.be.repository;

import com.example.be.entity.ShowTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ShowTimeRepository extends JpaRepository<ShowTime, Long> {
    List<ShowTime> findAll();
    @Query("SELECT st FROM ShowTime st " +
            "JOIN FETCH st.movie m " + // JOIN FETCH với Movie
            "JOIN FETCH st.room r " +  // JOIN FETCH với Room
            "WHERE st.showDate >= :today") // Lọc theo ngày
    List<ShowTime> findAllWithMovieAndRoom(@Param("today") LocalDate today);


}
