package com.example.be.repository;

import com.example.be.entity.Room;
import com.example.be.entity.ShowTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ShowTimeRepository extends JpaRepository<ShowTime, Long> {
    List<ShowTime> findAll();
    @Query("SELECT s FROM ShowTime s ORDER BY s.showDate DESC, s.startTime DESC")
    List<ShowTime> findAllDesc();


    @Query("SELECT st FROM ShowTime st " +
            "JOIN FETCH st.movie m " + // JOIN FETCH với Movie
            "JOIN FETCH st.room r " +  // JOIN FETCH với Room
            "WHERE st.showDate >= :today") // Lọc theo ngày
    List<ShowTime> findAllWithMovieAndRoom(@Param("today") LocalDate today);

    @Query(value = "SELECT r.* FROM room r " +
            "WHERE r.id NOT IN (" +
            "    SELECT st.room_id FROM showtime st " +
            "    WHERE st.show_date = :showDate " +
            "    AND (" +
            "        (:startTime < st.end_time AND :endTime > st.start_time)" +
            "    )" + " AND st.show_date >= :today" +
            ")", nativeQuery = true)
    List<Room> findAvailableRooms(@Param("showDate") LocalDate showDate,
                                  @Param("startTime") LocalTime startTime,
                                  @Param("endTime") LocalTime endTime);

    List<ShowTime> findByShowDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("""
       SELECT st
       FROM ShowTime st 
       JOIN FETCH st.room r
       WHERE r.id = :roomId
         AND st.showDate >= :today 
       ORDER BY st.showDate, st.startTime
       """)
    List<ShowTime> findByRoomFromToday(@Param("roomId") Long roomId,
                                       @Param("today")  LocalDate today);

}
