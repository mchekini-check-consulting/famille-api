package fr.checkconsulting.gardeenfant.repository;

import fr.checkconsulting.gardeenfant.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Message, String> {
    List<Message> getAllByEmailSourceOrEmailDest(String emailSrc, String emailDest);

    @Query("select count(timeMessage) from Message m where m.emailDest = :emailDest and m.consumed = 0")
    Number getUnreadMessages(@Param("emailDest") String emailDest);

    @Query("select emailSource, count(timeMessage) from Message m where m.emailDest = :emailDest and m.consumed = 0 group by m.emailSource")
    List[] getUnreadMessagesByNounou(@Param("emailDest") String emailDest);

    @Modifying
    @Query("update Message m set m.consumed = 1 where m.emailSource = :emailSource and m.emailDest = :emailDest")
    @Transactional
    int setMessageRead(@Param("emailSource") String emailSource, @Param("emailDest") String emailDest);
}
