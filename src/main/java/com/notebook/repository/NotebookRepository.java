package com.notebook.repository;

import com.notebook.entity.Notebook;
import com.notebook.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotebookRepository extends JpaRepository<Notebook, Long> {
    List<Notebook> findByUserOrderByUpdatedAtDesc(User user);
}
