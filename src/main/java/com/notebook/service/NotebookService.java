package com.notebook.service;

import com.notebook.dto.NotebookDto;
import com.notebook.entity.Notebook;
import com.notebook.entity.User;
import com.notebook.repository.NotebookRepository;
import com.notebook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotebookService {

    private final NotebookRepository notebookRepository;
    private final UserRepository userRepository;

    @Autowired
    public NotebookService(NotebookRepository notebookRepository, UserRepository userRepository) {
        this.notebookRepository = notebookRepository;
        this.userRepository = userRepository;
    }

    public void saveNotebook(NotebookDto notebookDto, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Notebook notebook = new Notebook();
        if (notebookDto.getId() != null) {
            notebook = notebookRepository.findById(notebookDto.getId())
                    .orElse(new Notebook());
        }
        notebook.setTitle(notebookDto.getTitle());
        notebook.setContent(notebookDto.getContent());
        notebook.setCategory(notebookDto.getCategory());
        notebook.setUser(user);

        notebookRepository.save(notebook);
    }

    public List<Notebook> findNotebooksByUser(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user != null) {
            return notebookRepository.findByUserOrderByUpdatedAtDesc(user);
        }
        return List.of();
    }

    public List<Notebook> findAllNotebooks() {
        return notebookRepository.findAll();
    }

    public Notebook getNotebookById(Long id) {
        return notebookRepository.findById(id).orElse(null);
    }

    public void deleteNotebook(Long id) {
        notebookRepository.deleteById(id);
    }
}
