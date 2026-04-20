package com.notebook.repository;

import com.notebook.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByName(String name);

    /** Tất cả category, sắp xếp theo tên A→Z (dùng cho dropdown/chip-selector) */
    List<Category> findAllByOrderByNameAsc();
}
