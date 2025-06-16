package com.projetoProvaTcc.repository;

import com.projetoProvaTcc.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    //método pra buscar tag pelo tagName
    List<Tag> findAllByTagNameIn(List<String> tagNames);
}
