package com.projetoProvaTcc.repository;

import com.projetoProvaTcc.model.Tag;

import jakarta.persistence.*;
import java.util.List;

@NamedQuery(name = "Tag.codigo", query = "SELECT t FROM Tag t WHERE t.codigo = :codigo")
public class DaoTag {

    //abre uma conexao para o banco de dados.
    private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("jpa_prjProva");
    private static EntityManager entityManager = entityManagerFactory.createEntityManager();

    public boolean salvar(Tag tag) {
        return this.salvar(tag, true);
    }

    public boolean salvar(Tag tag, boolean autoCommit) {
        if (autoCommit == true)
            entityManager.getTransaction().begin();
        try {
            entityManager.persist(tag);
        } catch (PersistenceException e) {
            if (autoCommit == true)
                entityManager.getTransaction().rollback();
            return false;
        }
        if (autoCommit == true)
            entityManager.getTransaction().commit();
        return true;
    }

    public boolean remover(Tag tag) {
        return this.remover(tag, true);
    }

    public boolean remover(Tag tag, boolean autoCommit) {
        if (autoCommit == true)
            entityManager.getTransaction().begin();
        try {
            entityManager.remove(tag);
        } catch (PersistenceException e) {
            if (autoCommit == true)
                entityManager.getTransaction().rollback();
            return false;
        }
        if (autoCommit == true)
            entityManager.getTransaction().commit();
        return true;
    }

    public List<Tag> obterTodasTag() {
        Query query = entityManager.createQuery("SELECT t FROM Tag t");
        List<Tag> resultado  = query.getResultList();
        Tag[] retorno = new Tag[resultado.size()];
        for(int i = 0; i < resultado.size(); i++)
            retorno[i] = resultado.get(i);
        return List.of(retorno);

    }
}
