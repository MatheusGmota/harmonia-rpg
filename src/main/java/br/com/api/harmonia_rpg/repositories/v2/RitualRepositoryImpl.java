package br.com.api.harmonia_rpg.repositories.v2;

import br.com.api.harmonia_rpg.domain.entities.Ritual;
import com.google.cloud.firestore.*;
import org.springframework.stereotype.Repository;

@Repository
public class RitualRepositoryImpl extends FirestoreGenericRepository<Ritual> {
    public RitualRepositoryImpl(Firestore firestore) {
        super(firestore, Ritual.class);
    }

    @Override
    protected String getSubCollectionName() {
        return "rituais";
    }
}