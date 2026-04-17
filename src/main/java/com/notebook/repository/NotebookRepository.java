package com.notebook.repository;

import com.notebook.entity.Notebook;
import com.notebook.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotebookRepository extends JpaRepository<Notebook, Long> {

    List<Notebook> findByUserOrderByUpdatedAtDesc(User user);

    /** Pinned first, then by updatedAt desc */
    List<Notebook> findByUserOrderByPinnedDescUpdatedAtDesc(User user);

    /** Search by keyword in title or content */
    @Query("SELECT n FROM Notebook n WHERE n.user = :user " +
           "AND (:keyword IS NULL OR :keyword = '' OR " +
           "     LOWER(n.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "     LOWER(n.content) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND (:category IS NULL OR :category = '' OR n.category = :category) " +
           "ORDER BY n.pinned DESC, n.updatedAt DESC")
    List<Notebook> searchNotebooks(@Param("user") User user,
                                   @Param("keyword") String keyword,
                                   @Param("category") String category);

    /** Same but sorted A-Z by title */
    @Query("SELECT n FROM Notebook n WHERE n.user = :user " +
           "AND (:keyword IS NULL OR :keyword = '' OR " +
           "     LOWER(n.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "     LOWER(n.content) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND (:category IS NULL OR :category = '' OR n.category = :category) " +
           "ORDER BY n.pinned DESC, n.title ASC")
    List<Notebook> searchNotebooksAlphabetically(@Param("user") User user,
                                                  @Param("keyword") String keyword,
                                                  @Param("category") String category);
}
