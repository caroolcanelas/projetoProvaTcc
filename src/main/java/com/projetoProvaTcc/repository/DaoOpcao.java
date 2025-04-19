package com.projetoProvaTcc.repository;

import com.projetoProvaTcc.model.Opcao;

import jakarta.persistence.*;
import java.util.List;

@NamedQuery(name = "Opcao.codigo", query = "SELECT o FROM Opcao o WHERE o.codigo = :codigo")
public class DaoOpcao {

    //abre uma conexao para o banco de dados.
    private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("jpa_prjProva");
    private static EntityManager entityManager = entityManagerFactory.createEntityManager();

    public boolean salvar(Opcao opcao) {
        return this.salvar(opcao, true);
    }

    public boolean salvar(Opcao opcao, boolean autoCommit) {
        if (autoCommit == true)
            entityManager.getTransaction().begin();
        try {
            entityManager.persist(opcao);
        } catch (PersistenceException e) {
            if (autoCommit == true)
                entityManager.getTransaction().rollback();
            return false;
        }
        if (autoCommit == true)
            entityManager.getTransaction().commit();
        return true;
    }

    public boolean remover(Opcao opcao) {
        return this.remover(opcao, true);
    }

    public boolean remover(Opcao opcao, boolean autoCommit) {
        if (autoCommit == true)
            entityManager.getTransaction().begin();
        try {
            entityManager.remove(opcao);
        } catch (PersistenceException e) {
            if (autoCommit == true)
                entityManager.getTransaction().rollback();
            return false;
        }
        if (autoCommit == true)
            entityManager.getTransaction().commit();
        return true;
    }

    public List<Opcao> obterTodasOpcoes() {
        Query query = entityManager.createQuery("SELECT o FROM Opcao o");
        List<Opcao> resultado  = query.getResultList();
        Opcao[] retorno = new Opcao[resultado.size()];
        for(int i = 0; i < resultado.size(); i++)
            retorno[i] = resultado.get(i);
        return List.of(retorno);

    }
}
