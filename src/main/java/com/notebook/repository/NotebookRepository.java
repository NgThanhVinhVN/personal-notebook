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

    /**
     * Search by keyword + optional category filter (Many-to-Many).
     * Dùng EXISTS để tránh DISTINCT và trùng lặp khi JOIN.
     * sort: updatedAt DESC (gần nhất sửa)
     */
    @Query("SELECT n FROM Notebook n WHERE n.user = :user " +
           "AND (:keyword IS NULL OR :keyword = '' OR " +
           "     LOWER(n.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "     LOWER(n.content) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND (:category IS NULL OR :category = '' OR " +
           "     EXISTS (SELECT c FROM n.categories c WHERE c.name = :category)) " +
           "ORDER BY n.pinned DESC, n.updatedAt DESC")
    List<Notebook> searchNotebooks(@Param("user") User user,
                                   @Param("keyword") String keyword,
                                   @Param("category") String category);

    /** sort: title A→Z */
    @Query("SELECT n FROM Notebook n WHERE n.user = :user " +
           "AND (:keyword IS NULL OR :keyword = '' OR " +
           "     LOWER(n.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "     LOWER(n.content) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND (:category IS NULL OR :category = '' OR " +
           "     EXISTS (SELECT c FROM n.categories c WHERE c.name = :category)) " +
           "ORDER BY n.pinned DESC, n.title ASC")
    List<Notebook> searchNotebooksAlphabetically(@Param("user") User user,
                                                  @Param("keyword") String keyword,
                                                  @Param("category") String category);

    /** sort: title Z->A */
    @Query("SELECT n FROM Notebook n WHERE n.user = :user " +
           "AND (:keyword IS NULL OR :keyword = '' OR " +
           "     LOWER(n.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "     LOWER(n.content) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND (:category IS NULL OR :category = '' OR " +
           "     EXISTS (SELECT c FROM n.categories c WHERE c.name = :category)) " +
           "ORDER BY n.pinned DESC, n.title DESC")
    List<Notebook> searchNotebooksAlphabeticallyDesc(@Param("user") User user,
                                                  @Param("keyword") String keyword,
                                                  @Param("category") String category);

    /** sort: createdAt DESC (mới tạo nhất) */
    @Query("SELECT n FROM Notebook n WHERE n.user = :user " +
           "AND (:keyword IS NULL OR :keyword = '' OR " +
           "     LOWER(n.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "     LOWER(n.content) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND (:category IS NULL OR :category = '' OR " +
           "     EXISTS (SELECT c FROM n.categories c WHERE c.name = :category)) " +
           "ORDER BY n.pinned DESC, n.createdAt DESC")
    List<Notebook> searchNotebooksNewest(@Param("user") User user,
                                         @Param("keyword") String keyword,
                                         @Param("category") String category);

    /** sort: createdAt ASC (cũ tạo nhất) */
    @Query("SELECT n FROM Notebook n WHERE n.user = :user " +
           "AND (:keyword IS NULL OR :keyword = '' OR " +
           "     LOWER(n.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "     LOWER(n.content) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND (:category IS NULL OR :category = '' OR " +
           "     EXISTS (SELECT c FROM n.categories c WHERE c.name = :category)) " +
           "ORDER BY n.pinned DESC, n.createdAt ASC")
    List<Notebook> searchNotebooksOldest(@Param("user") User user,
                                         @Param("keyword") String keyword,
                                         @Param("category") String category);

    /**
     * Đếm số notebook còn đang gắn với category (dùng để quyết định xóa orphan).
     */
    @Query("SELECT COUNT(n) FROM Notebook n JOIN n.categories c WHERE c.id = :categoryId")
    long countNotebooksByCategory(@Param("categoryId") Long categoryId);
}
