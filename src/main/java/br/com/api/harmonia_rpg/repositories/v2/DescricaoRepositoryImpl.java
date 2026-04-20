package br.com.api.harmonia_rpg.repositories.v2;

import br.com.api.harmonia_rpg.domain.entities.Descricao;
import com.google.cloud.firestore.Firestore;
import org.springframework.stereotype.Repository;

@Repository
public class DescricaoRepositoryImpl extends FirestoreGenericRepository<Descricao> {
    public DescricaoRepositoryImpl(Firestore firestore) {
        super(firestore, Descricao.class);
    }

    @Override
    protected String getSubCollectionName() {
        return "descricao";
    }
}
