package model.dao;

import model.Questao;

import javax.persistence.*;
import java.util.List;

@NamedQuery(name = "Questao.codigo", query = "SELECT q FROM Questao q WHERE q.codigo = :codigo")
public class DaoQuestao {

    //abre uma conexao para o banco de dados.
    private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("jpa_prjProva");
    private static EntityManager entityManager = entityManagerFactory.createEntityManager();

    public boolean salvar(Questao questao) {
        return this.salvar(questao, true);
    }

    public boolean salvar(Questao questao, boolean autoCommit) {
        if (autoCommit == true)
            entityManager.getTransaction().begin();
        try {
            entityManager.persist(questao);
        } catch (PersistenceException e) {
            if (autoCommit == true)
                entityManager.getTransaction().rollback();
            return false;
        }
        if (autoCommit == true)
            entityManager.getTransaction().commit();
        return true;
    }

    public boolean remover(Questao questao) {
        return this.remover(questao, true);
    }

    public boolean remover(Questao questao, boolean autoCommit) {
        if (autoCommit == true)
            entityManager.getTransaction().begin();
        try {
            entityManager.remove(questao);
        } catch (PersistenceException e) {
            if (autoCommit == true)
                entityManager.getTransaction().rollback();
            return false;
        }
        if (autoCommit == true)
            entityManager.getTransaction().commit();
        return true;
    }

    public List<Questao> obterTodasQuestoes() {
        Query query = entityManager.createQuery("SELECT q FROM Questao q");
        List<Questao> resultado  = query.getResultList();
        Questao[] retorno = new Questao[resultado.size()];
        for(int i = 0; i < resultado.size(); i++)
            retorno[i] = resultado.get(i);
        return List.of(retorno);

    }

    //ObterQuestaoPelo...
    //Qual atributo que vamos usar pra obter a questao ?
    // consertar namedQuery




}
